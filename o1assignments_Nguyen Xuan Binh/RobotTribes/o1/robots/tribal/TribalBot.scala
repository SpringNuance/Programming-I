package o1.robots.tribal

import o1.robots._
import o1._

import scala.collection.mutable.Map
import scala.collection.mutable.Stack


/** The class `TribalBot` represents the "brains" (or AI) of robots that belong to
  * a "robot tribe" and behave according to the rules of that tribe. Tribal robots
  * consider other tribal robots of the same tribe their friends and all other robots
  * their enemies. A tribal robot is capable of attacking enemies and thereby converting
  * them to their own tribe. As a consequence, tribes can be quite aggressive as they
  * compete for survival in a robot world.
  *
  *
  * ==Robot Actions==
  *
  * Each tribal robot is capable of doing one (but only one) of the following things
  * during its turn:
  *
  *  - Moving one square forward into an empty floor space.
  *  - Spinning 90 degrees (without moving).
  *  - Attacking -- "hacking" -- an enemy robot immediately in front of it (see below).
  *
  * Hacking is automatic whenever a tribal robot sees an enemy, but how a tribal robot
  * moves about and spins depends on its tribe. Each tribe has its own program, written
  * in the RoboSpeak language. All members of the tribe follow this program, which
  * defines the sequences of actions that those robots take. See the documentation of
  * class [[Tribe]] for details about RoboSpeak.
  *
  *
  * ==Hacking==
  *
  * A tribal robot starts its turn by looking at whether there is an enemy robot in front
  *  of it. An enemy robot is any robot that is not a member of the same tribe. If there
  * is an enemy robot in the square right in front of the acting tribal bot, then the
  * tribal bot attacks it by "hacking". Hacking is a kind of brainwashing: it converts
  * the enemy robot into a member of the acting robot's tribe. The victim will begin
  * executing its new tribe's RoboSpeak program from a line determined by the hacking
  * robot.
  *
  *
  * ==Memory==
  *
  * Each tribal bot has some limited memory resources available, which it draws on when
  * it executes the tribe's RoboSpeak program. A tribal bot remembers the following
  * information:
  *
  *  - Where it is: a tribal bot knows which robot body it is controlling, just like any
  *    other `RobotBrain` does, and can therefore determine its location and facing.
  *
  *  - Where its allegiances lie: a tribal bot knows which tribe it belongs to.
  *
  *  - What it's supposed to do next: a tribal bot knows which instruction in its tribe's
  *    RoboSpeak program it is supposed to execute next once it gets its next turn.
  *    (This allows the tribal bot to save the program position it is at when it ends a turn.
  *    It can later resume program execution where it left off the previous turn.)
  *
  *  - Which way it's turning: A tribal bot remembers if it's supposed to be turning
  *    clockwise or counterclockwise when it next makes a turn.
  *
  *  - Which subprogram calls have been made: A tribal bot has a call stack where it stores
  *    frames representing calls to RoboSpeak subprograms. Each such frame is represented by
  *    a [[Frame]] object. This enables the tribal bot to execute subprograms and return to
  *    the correct line in the tribe's RoboSpeak program whenever the end of a subprogram
  *    is reached.
  *
  *  - The contents of three memory slots (or "registers") called ''mem'', ''radar'' and
  *    ''hackline''. The slot ''mem'' can be used by the RoboSpeak program for any purpose.
  *    The intended use of ''radar'' is to store the readings produced by the robot's radar.
  *    The slot ''hackline'' is meant for configuring hacked robots. See the text below and
  *    class [[Tribe]] for more details.
  *
  *
  * ==Sensors==
  *
  * A tribal bot is (only) capable of seeing the single square directly in front of it.
  * It can determine what there is in that square but no further. It also has a ''radar'' that
  * can be used either ''short-range'' to determine how many enemies or friends are located
  * within exactly two steps of the acting robot, or ''long-range'' to count the memberships
  * of entire tribes in the robot world. Again, more details can be found in the documentation
  * for class [[Tribe]].
  *
  *
  * ==Initial state==
  *
  * When a tribal bot is created, it starts executing its tribal program from the
  * beginning (unless otherwise specified by a hacking robot). Its call stack is initially empty,
  * its memory slots all contain the value 1, and it is considered to be turning clockwise.
  *
  * As a new tribal bot brain "plugs into" a robot body, it changes the robot's name.
  * Each new member of a tribe receives a new rather impersonal name of the form "Tribe#number",
  * e.g., "Tiger#123". The ID number that comes after the hash (#) is unique for every member
  * of a tribe.
  *
  *
  * @param body   the robot body that the tribal bot brain will control
  * @param tribe  the tribe that this brain belongs to
  *
  * @see [[Tribe]]
  * @see [[Frame]] */
class TribalBot(body: RobotBody, val tribe: Tribe) extends RobotBrain(tribe.newName(), body) {

  private var (nextInstruction,       // serves as a "program counter register" for the robot
               turningClockwise,      // flag
               callStack,             // container: all the frames of active subprogram calls
               memorySlots)           // container of most-recent holders (Int-valued memory slots or "registers")
               = this.initialState(1) // set the values of all four vars from the tuple returned by initialState


  // Returns a tuple that describes the initial state of a bot after it has just been restarted at the given line.
  private def initialState(startLine: Int): (Instruction, Boolean, Stack[Frame], Map[String,Int]) =
    (this.tribe.instructionAt(startLine), true, new Stack(), Map().withDefaultValue(1))


  /** Resets the robot's state. All the robot's memory slots are set to the initial
    * value of 1. When it next gets a turn, the robot starts executing its tribal program
    * from the given line onwards with an empty call stack.
    * @param startLine  a line number in the robot's tribe's RoboSpeak program;
    *                   the robot will execute the instruction on this line next */
  def boot(startLine: Int) = {
    val (next, clockwise, stack, slots) = this.initialState(startLine)
    this.nextInstruction  = next
    this.turningClockwise = clockwise
    this.callStack        = stack
    this.memorySlots      = slots
  }


  /** Checks the the square that neighbors the tribal bot in the given direction to
    * see if it contains something that the bot considers an obstacle. To tribal bots,
    * walls are obstacles but other robots aren’t. This behavior overrides the default
    * implementation from class `RobotBrain`. */
  override def mayMoveTowards(direction: CompassDir) = this.body.neighboringSquare(direction).isEmpty


  /** A tribal robot prioritizes hacking: if there is an enemy present directly in front,
    * the acting bot spends its turn hacking it. (Exception: there are "pacifist" tribes
    * that don't hack.)
    *
    * If no enemy is present, or if the tribal bot is a pacifist, the bot executes its
    * tribe's RoboSpeak program until it performs one of the actions that end its turn
    * (or at least tries to perform such an action).
    *
    * Essentially, this method is responsible for the tribal bot's equivalent of a
    * [[http://en.wikipedia.org/wiki/Instruction_cycle fetch-execute cycle]]
    * during the robot's turns. In the case of a tribal robot, this involves these two steps:
    *
    *   1. Execute an instruction.
    *
    *   2. Fetch the instruction that is to be executed next.
    *
    * The cycle is repeated until the robot's turn is over. The turn is over when the robot
    * executes one of the instructions that ends its turn or when the end of the tribe's
    * program is reached.
    *
    * Instructions are represented by `Instruction` objects. The tribal bot object's job
    * is made easy by the fact that the method `execute` in class [[Instruction]] not only
    * executes the instruction (Step 1 above), but also returns the instruction that is to
    * be executed next (Step 2 above).
    *
    * Unless otherwise specified, a robot will resume execution where it left off the
    * previous turn.
    *
    * This method assumes that it is called only if the robot is not broken and not stuck.
    *
    * @see [[Instruction]] */
  def moveBody() = {
     if (this.seesEnemy && !this.tribe.isPacifist) hack()
     else {
        while (!nextInstruction.endsTurn){
        nextInstruction = this.nextInstruction.execute(this)
     }
        nextInstruction = this.nextInstruction.execute(this)
     }
  } // TODO: implement this method
  // Study the documentation and Scala code of the TribalBot class and the docs of the Instruction trait.
  // Implement the moveBody method in TribalBot.
  // Use the nextInstruction variable defined in the same class.
  // The execute method on Instruction objects returns a value. Make sure to use that value.
  // moveBody should call hack. Include that method call in your implementation already, even though it
  // doesn’t actually do anything yet, since hack’s implementation is empty.


  /** "Brainwashes" an enemy robot that is currently right in front of the acting robot,
    * assuming there is one there. The victim of the hack will receive a new tribal bot brain
    * of the enemy tribe, which replaces its old brain. Consequently, it will receive a new
    * name and will start behaving differently than before.
    *
    * When its next turn comes, the victim will start executing the tribal RoboSpeak program
    * of the attacker's tribe, using its fresh brain. Anything that the victim's old brain had
    * in memory (stack frames, memory slot values, etc.) is lost; the victim is now considered
    * to be turning clockwise.
    *
    * The victim will start executing its new RoboSpeak program from the line indicated by the
    * hacking robot's ''hackline'' memory slot. The default value for this memory slot is 1,
    * so unless the hacking robot's tribal program specifies otherwise, the victim starts
    * from the first line of RoboSpeak. (See the description of RoboSpeak in class [[Tribe]]
    * for more details about how to affect ''hackline''.)
    *
    * If there is no enemy in front of the acting robot, this method does nothing.
    *
    * @see [[enemyInFront]] */
  def hack() = {
    enemyInFront match {
      case Some(brains) =>
        var tribalbrains = new TribalBot(brains.body, this.tribe)
        tribalbrains.boot(memorySlots("hackline"))
        brains.body.brain = Some(tribalbrains)
    }


  }  // TODO: provide a working implementation

  /** Returns the contents of a named memory slot. */
  def slotValue(slotName: String) = this.memorySlots(slotName)


  /** Sets the contents of a named memory slot. The new value replaces the old value stored in the slot */
  def setSlotValue(slotName: String, newValue: Int) = {
    this.memorySlots += slotName -> newValue
  }


  /** Switches the spinning direction of the tribal bot. If it was previously set
    * to turn clockwise, it now turns counterclockwise, and vice versa.
    * This method does not actually turn the robot, it only affects future spins.
    * @see [[spin]] */
  def switchDirection() = {
    this.turningClockwise = !this.turningClockwise
  }


  /** Spins the robot 90 degrees in whichever direction it is currently turning.
    * @see [[switchDirection]] */
  def spin() = {
    val currentFacing = this.body.facing
    val newFacing = if (this.turningClockwise) currentFacing.clockwise else currentFacing.counterClockwise
    this.body.spinTowards(newFacing)
  }


  /** Determines whether this robot is a member of the given tribe. */
  def belongsTo(tribe: Tribe) = this.tribe == tribe


  /** Determines the tribe of a robot. Returns the tribe of the given robot
    * wrapped in an `Option`, or `None` if the robot is not a tribal bot. */
  def determineTribe(another: RobotBrain): Option[Tribe] = {
    another match {
      case some: TribalBot => Some(some.tribe)
      case _ => None
    }
  }

  /** Determines whether this robot considers the given robot to be "fodder", that is, an
    * unthreatening resource. A tribal bot never sees itself as fodder, but does see all
    * other pacifist robots (including ones from its own tribe if it's a pacifist) as fodder.
    * All non-tribal bots are considered fodder by all tribal bots. */
  def seesAsFodder(robot: RobotBrain) = determineTribe(robot).forall( _.isPacifist ) && this != robot


  /** Determines whether this robot considers the given robot tribe a threat.
    * A tribal bot considers all non-pacifist tribes threats except its own. */
  def isThreatenedBy(tribe: Tribe)= !this.belongsTo(tribe) && !tribe.isPacifist


  /** Determines whether this robot considers the given robot to be a threat.
    * A tribal bot considers all members of all non-pacifist tribes except its
    * own to be threats. Non-tribal bots are never considered to be threats. */
  def isThreatenedBy(robot: RobotBrain): Boolean = determineTribe(robot).exists( this.isThreatenedBy(_) )


  /** Determines if the given robot is a friend of this robot. That is, determines if the
    * given robot is a tribal robot of the same tribe (but is not this robot itself). */
  def isFriend(robot: RobotBrain): Boolean = {
     if (robot != this){
     determineTribe(robot) match {
      case Some(tribal) => tribal == this.tribe
      case None => false
    }
      } else false
  }

  /** Returns the brains of the friendly robot immediately in front of this robot,
    * if there is one. The brains returned in an `Option` wrapper, and `None` is returned
    * if there is no robot in the square or if the robot that is there is not a friend.
    * @see [[isFriend]] */
  def friendInFront = this.robotInFront.collect{ case tribal: TribalBot => tribal }.filter( this.isFriend(_) )


  /** Returns the brains of the enemy robot immediately in front of this robot, if there is one.
    * Any robot that is not a friend is an enemy. The brains are wrapped in an `Option`, and `None`
    * is returned if there is no robot in the square or if the robot that is there is a friend.
    * @see [[isFriend]] */
  def enemyInFront = this.robotInFront.filter( !this.isFriend(_) )


  /** Determines whether the square immediately in front of the robot is an unpassable obstacle. */
  def seesWall = this.squareInFront.isUnpassable


  /** Determines whether the square immediately in front of the robot is an empty floor space. */
  def seesFloor = this.squareInFront.isEmpty


  /** Determines whether the square immediately in front of the robot contains an enemy robot.
    * @see [[enemyInFront]] */
  def seesEnemy = this.enemyInFront.isDefined


  /** Determines whether the square immediately in front of the robot contains an friendly robot.
    * @see [[friendInFront]] */
  def seesFriend = this.friendInFront.isDefined


  /** "Talks" to a friendly robot that is currently right in front of the acting robot, if
    * there is a friendly robot there. In practice, this means that the talking robot gives
    * an order to its friendly "listener", causing the listener to move to the given line
    * number in the two robots' shared tribal program. The listener is "rebooted" and starts
    * at the given line number with a clean slate, as if it had just been created.
    *
    * If there is no friendly robot in front of the acting robot, this method does nothing.
    *
    * @see [[isFriend]] */
    // * @param newLine  a line number referencing the RoboSpeak program of the two friends' shared tribe
  def talk(lineNumber: Int) = {
      if (friendInFront.isDefined) {
        friendInFront.foreach(_.boot(lineNumber))
      }
  }


  /** "Shouts" to all friendly robots nearby. The message reaches all the friendly robots
    * that are within the robot’s [[shortRadar short radar]]: this is equivalent to
    * [[talk talking]] to each of those other robots.
    * @see [[shortRadar]], [[isFriend]], [[talk]] */
    // * @param newLine  a new line number for the recipients (see [[talk]])
  def shout(lineNumber: Int) = {
    val friends = this.shortRadar.collect{ case tribal: TribalBot => tribal }.filter( this.isFriend(_) )
    friends.foreach( _.boot(lineNumber) )
  }

  /** Starts the execution of a subprogram. That is to say, creates a new stack frame and places
    * ("pushes") it on top of the robot's call stack.
    * @param callingLine  the line number of the call site. This number is to be stored on the
    *                     stack so that program execution can resume at the correct location
    *                     once the robot returns from the subprogram. */
  def callSubprogram(callingLine: Int): Unit = {
    var newFrame = new Frame(callingLine)
    callStack.push(newFrame)
  } // TODO: provide a working implementation

  /** Ends the execution of a subprogram. This means that the topmost stack frame is removed
    * ("popped") from the robot's call stack.
    *
    * This method fails with an `IllegalStateException` in case there are no frames on the stack.
    *
    * @return a line number that indicates where the subprogram call (whose execution ended) had been
    *         called from (and where execution should therefore resume) */
  def returnFromSubprogram() = {
    def failWithUnderflowError = throw new IllegalStateException("Stack underflow: No active subprogram to return from.")
    if (callStack.isEmpty) failWithUnderflowError
    else callStack.pop().returnLine
  }  // TODO: provide a working implementation; call failUnderlowError in case the stack is empty


  /** Returns the brains of all the robots in the same robot world with this one. */
  def longRadar: Vector[RobotBrain] = {
    this.world.robotList.flatMap(_.brain)
  }


  /** Returns the brains of all the robots within two steps of this one.
    * (See [[Tribe]] for a definition of "steps".) */
  def shortRadar: Vector[RobotBrain] = {
    def isNear(robot: RobotBrain) = this.location.distance(robot.location) <= 2
    this.longRadar.filter(isNear)
  }


  /** Returns the brains of all the robots near this one in a specific direction. More specifically,
    * if ''D'' is the target direction and ''S'' is the square right next to this brain in direction D,
    * this method returns all the robot brains that are either in S or right next to S (orthogonally,
    * not diagonally). That list will always include this robot brain itself. (A total of one to
    * five robot brains will thus be returned.)
    * @param dirNumber  a number between 0 and 3 that indicates the target direction relative
    *                   to this robot's current facing: 0 means the direction the robot is now
    *                   facing; 1 means 90 degrees clockwise from that facing; 2 means the direction
    *                   behind the robot, and 3 means 90 degrees counterclockwise from the facing. */
  def directedRadar(dirNumber: Int) = {
    var dir = this.facing
    for (i <- 1 to dirNumber){
      dir = dir.clockwise
    }
    var trueNextSquare = this.location.neighbor(dir)
    this.shortRadar.filter(_.location.distance(trueNextSquare) <= 1)
  }
}

