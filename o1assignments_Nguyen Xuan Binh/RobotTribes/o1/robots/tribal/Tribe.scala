package o1.robots.tribal


import o1.robots.RobotBrain

import scala.io.Source
import scala.collection.mutable.Buffer
import scala.collection.mutable.Map
import scala.util.Random
import o1.util.{forEachLine,ConvenientCollection}


/** A "tribe" of robots is essentially a program which makes any robots who follow that program
  * --- the "members" of the tribe --- to behave in a certain way.
  *
  * '''NOTE TO STUDENTS: You are not required to understand this class’s implementation.
  * However, you should read the text below so you know how to program in RoboSpeak.'''
  *
  *
  * ==RoboSpeak==
  *
  * As a Plan A, tribal robots spend their turn attacking nearby enemies (see class [[TribalBot]]).
  * However, when there is no enemy directly in front, the tribal bot will instead follow its
  * tribe's program to maneuver itself.
  *
  * Tribal programs are written in a programming language called '''RoboSpeak'''. RoboSpeak can be
  * described as "machine code for tribal robots". It features a smallish number of simple instructions
  * (commands) which can be combined to build programs that direct tribal robots' actions.
  * A description of RoboSpeak appears below.
  *
  *
  * ==Action Instructions==
  *
  * There are four "action instructions" that cause a tribal bot to spend its turn on something concrete:
  *
  *  - ''move'': Attempt to move forward one step into an empty square.
  *
  *  - ''spin'': Spin 90 degrees. Unless otherwise specified (see below),
  *    the robot spins clockwise.
  *
  *  - ''uturn'': Spin 180 degrees.
  *
  *  - ''wait'': Stand still. Don't move, don't spin.
  *
  * Even if a robot fails to execute an action instruction (this happens if there is something
  * blocking movement), the attempt ends the robot's turn.
  *
  *
  * ==An Introductory Example==
  *
  * By themselves, action instructions are not enough to write interesting RoboSpeak programs.
  * To control the use of action instructions, RoboSpeak provides a variety of "logic instructions"
  * which allow a tribal bot to reason about its state and surroundings.
  *
  * Here is a simple RoboSpeak program. It causes the members of a tribe to move forward as long
  * as they can, turning clockwise whenever they run into a wall.
  *
  * {{{
  * ifwall 4
  * move
  * goto 1
  * spin
  * goto 1
  * }}}
  *
  * This program can be paraphrased as: "If you see a wall in front of you, go to line 4 (where you spin clockwise
  * before returning to line 1). Otherwise, move one step forward and return to line 1." Either moving or spinning
  * ends the robot's turn. Next turn, it resumes where it left off. In this example, it so happens that the
  * robot will always start its turns by returning to line 1 (because both the ''move'' and ''spin''
  * commands are followed by ''goto 1'').
  *
  * Only the two action instructions listed above end a tribal bot's turn: a robot can execute any
  * number of non-action instructions during a turn.
  *
  *
  * ==Basic Logic==
  *
  * Some basic logic instructions are:
  *
  *  - ''goto N'': Continue executing the program from line N.
  *
  *  - ''switch'': Change the direction in which the robot spins when the ''spin'' instruction is executed.
  *                If the robot was previously spinning clockwise, it now spins counterclockwise.
  *                If it was spinning counterclockwise, it now spins clockwise. Note that switching
  *                only affects future spins; it does not actually spin the robot immediately.
  *
  *  - ''ifempty N'': If there is nothing in the square in front of you, continue executing the program
  *                from line N, otherwise resume execution from the next line. The command ''ifnempty N''
  *                does exactly the opposite, and jumps to line N if and only if the square is not empty.
  *
  *  - ''iffriend N'': If there is a friend (a robot of the same tribe) in the square in front of you,
  *                resume executing the program from line N, otherwise continue execution from the next line.
  *                The command ''ifnfriend N'' does exactly the opposite, and jumps to line N if and only
  *                if there is no friend present.
  *
  *  - ''ifwall N'': If there is a wall in the square in front of you, continue executing the program from
  *                line N, otherwise resume execution from the next line. The command ''ifnwall N'' does
  *                exactly the opposite, and jumps to line N if and only if there is no wall present.
  *
  *  - ''ifrandom N'': "Flip a coin": 50% chance of continuing from line N; 50% chance of continuing from
  *                the next line.
  *
  *
  * ==Labels==
  *
  * Since using line number literals is highly inconvenient when editing any program longer than a
  * dozen lines or so, RoboSpeak allows the programmer to define '''labels'''. Labels are essentially
  * names for program locations. They can be used instead of line numbers in RoboSpeak instructions.
  *
  * A label is defined by line that contains a single word (the name of the label)
  * followed by a colon. Here is the previous RoboSpeak program rewritten using labels:
  *
  * {{{
  * start:
  *   ifwall wallfound
  *   move
  *   goto start
  * wallfound:
  *   spin
  *   goto start
  * }}}
  *
  * A label definition itself does not instruct a tribal bot to do anything.
  * It is simply a name for a location in code.
  *
  * Anywhere a line number is required as a parameter to a RoboSpeak instruction, a label name
  * may be used instead.
  *
  *
  * ==Comments and Whitespace==
  *
  * In RoboSpeak, the hash character (#) means that the rest of the line is a comment and does not
  * have any effect on robot behavior. Empty lines are similarly ignored. Using whitespace for
  * indentation is encouraged.
  *
  * Here is a slightly more complex tribe whose code has been explained using comments.
  *
  * {{{
  * #########################################################
  * # A member of the Tiger Tribe moves straight forward,   #
  * # hacking anything it sees in front of it. When its     #
  * # route is blocked, it turns either left or right at    #
  * # random.                                               #
  * #########################################################
  *
  * start:                        # Main loop: turn or move
  *   ifnempty turn               # turn if facing an obstacle
  *   move                        # move otherwise
  *   goto start                  # repeat the above
  *
  * turn:                         # Turns left or right at random
  *   ifrandom noswitch
  *   switch
  * noswitch:
  *   spin
  *   goto start
  * }}}
  *
  *
  * ==Pacifist Tribes==
  *
  * If a RoboSpeak program contains a line that consists of the word ''pacifist'', then members of the
  * tribe will not try to hack anyone at the beginning of their turns (the bunny tribe is an example).
  *
  *
  * ==Using Memory Slots==
  *
  * Each tribal bot has four '''memory slots''' called ''mem'', ''mem2'', ''radar'', and ''hackline''.
  * Each can store an integer value. Some commands directly manipulate these slots:
  *
  *  - ''set S N'': Set the value of slot S to N. The parameter S must be a slot name. For instance,
  *    ''set mem 5'' stores the value five in ''mem'', replacing any previously stored value.
  *
  *  - ''add1 S'': Increment the value of slot S by exactly one, replacing any previously stored value.
  *
  *  - ''plus S A B'': Compute the sum of A and B, then store the result in slot S.
  *
  *  - ''minus S A B'': Compute A minus B, then store the result in slot S.
  *
  * You can use a slot name as a parameter. For instance, ''goto mem'' causes execution to jump to
  * the line indicated by the current value of ''mem''; ''set mem radar'' copies the value of ''radar''
  * into ''mem''; and ''plus mem2 mem 10'' sums the value of ''mem'' with the number ten and stores
  * the result in ''mem2''.
  *
  * In addition to the logic instructions listed above, RoboSpeak features a few commands that compare
  * numerical values or do arithmetic on them. These commands work best in combination with memory slots:
  *
  *  - ''ifeq A B N'': If A equals B then continue executing the program from line N, otherwise
  *                    continue execution from the next line. For instance, ''ifeq mem 100 22''
  *                    jumps to line 22 if the memory slot ''mem'' contains the value 100.
  *                    The command ''ifneq A B N'' does exactly the opposite, and jumps to N
  *                    if and only if A and B are not equal.
  *
  *  - ''ifgt A B N'': If A is greater than B then continue executing the program from line N,
  *                    otherwise resume execution from the next line.
  *
  *  - ''iflt A B N'': If A is less than B then continue executing the program from line N,
  *                    otherwise resume execution from the next line.
  *
  * The slots ''mem'' and ''mem2'' may be used for anything. The slots ''radar'' and ''hackline'' have
  * special purposes --- see below.
  *
  *
  * ==Radar Commands==
  *
  * The ''radar'' memory slot is a special one that is automatically assigned a new value whenever
  * the robot uses any of the available radar commands:
  *
  *  - ''enemiesnear'': Use the radar to find out how many non-pacifist enemy bots are
  *                     within two steps of the acting robot. (See diagram below.) Store
  *                     this number in ''radar''.
  *
  *  - ''friendsnear'': Use the radar to find out how many friendly robots are within
  *                     two steps of the acting robot. (See diagram below.) Store this number
  *                     in ''radar''. The robot does not count itself as a friend.
  *
  *  - ''foddernear'': Use the radar to find out how many pacifist bots are within
  *                    two steps of the acting robot. (See diagram below.) Store this number
  *                    in ''radar''. This count does not include the acting robot, even if
  *                    it is a pacifist one. Non-tribal bots count as pacifists for this purpose.
  *
  *  - ''fodderleft'': Use the radar to produce a count of how many "fodder bots"
  *                    (see ''foddernear'' above) there still are in the entire robot world.
  *                    Store this number in ''radar''.
  *
  *  - ''score'': Use the radar to produce a number that indicates who is leading the ongoing
  *                    tribal fight and by how much. Store this number in ''radar''. A positive
  *                    number means that the acting robot's tribe is more populous than any
  *                    other tribe in the robot world, while a negative number means that another
  *                    tribe is leading. The magnitude of the lead is also given: for instance,
  *                    a score of 5 means that the acting robot's tribe has five more members than
  *                    the second-placed one, and -10 means that the acting robot's tribe has ten
  *                    fewer members than the leading tribe. Pacifist tribes are ignored when
  *                    determining the score.
  *
  * A "step" means a non-diagonal move to an adjacent square. For instance, in the diagram below, robots
  * A, B, C, D, and E are within two steps of robot R, but robots F and G aren't. (# represents a wall,
  * and . represents an empty floor square.)
  *
  * {{{
  * ###########
  * #..A......#
  * #.D#RBC...#
  * #.....G...#
  * #.F.E.....#
  * ###########
  * }}}
  *
  * In addition to the radar-based commands listed above, two commands use a directed short-range radar:
  * (see also [[TribalBot.directedRadar]]):
  *
  *  - ''enemiesdir D'': Find out how many non-pacifist enemy bots are close by in the direction
  *              indicated by the parameter D. Store that number in ''radar''. D must evaluate to a
  *              number between 0 and 3 (inclusive): 0 means the direction where the robot is facing;
  *              1 means the direction 90 degrees clockwise from there (to the robot’s right); 2 means
  *              behind the robot; 3 means to the robot’s left. An enemy counts as being close if it
  *              is within one step of the nearest neighboring square in that direction (that square
  *              included). Since there are four possible locations for such enemies, this command
  *              puts a number between 0 and 4 (inclusive) in ''radar''.
  *
  *  - ''friendsdir N'': As ''enemiesdir'' above but counts friendly robots instead. The robot does
  *              not count itself as its own friend, so the result is between 0 and 4 (inclusive).
  *
  *
  * ==Hacking and Talking==
  *
  * As noted above, non-pacifist tribal bots always automatically spend their turn hacking a nearby
  * enemy whenever possible. In its future turns, the hacked enemy robot will execute its new tribal
  * program starting at whichever line number is stored in the hacking robot's ''hackline'' memory
  * slot. As the default value of all memory slots is 1, hacked robots start running their programs
  * from the beginning, unless a ''set'' command has been used to modify the value of ''hackline''.
  *
  * Robots can affect how their existing tribemates behave. The instruction ''talk N'' transmits a
  * command to a nearby friendly robot. Assuming that there is a friendly robot in front of the acting
  * robot, the friend jumps to line N in its RoboSpeak program. The next time the friend gets a turn,
  * it will resume executing its program from line N onwards. If there is no friendly robot right in
  * front of the acting robot, this instruction achieves nothing. Talking does not end a robot's turn.
  *
  * There's also an instruction for "talking louder". The instruction ''shout N'' works like ''talk N''
  * except that it talks to all friendly robots nearby (where nearness is defined as for ''friendsnear'';
  * see above).
  *
  * A robot that is hacked or talked to is rebooted. Its state (spinning direction, memory slots, etc.)
  * are reset.
  *
  *
  * ==Subprograms==
  *
  * RoboSpeak provides a mechanism for making subprogram calls. Subprogram calls are similar to
  * function calls in Scala in that they cause a specified sequence of instructions to be executed,
  * after which execution resumes at the location where the call was made. However, they are much
  * simpler than function calls in that RoboSpeak subprograms can not receive any parameters, don't
  * produce return values, and are never invoked on objects. (RoboSpeak is not an object-oriented
  * language.)
  *
  * Two RoboSpeak instructions are directly related to subprogram calls:
  *
  *  - ''callsub N'': Calls a subprogram that starts at line N, causing program execution to jump
  *             to that line. Although this is similar to ''goto N'', there is a crucial difference.
  *             Whereas the ''goto'' command simply moves program execution from one place to
  *             another, ''callsub'' causes the robot to remember that it is making a subprogram
  *             call. More specifically, the robot will remember that there is a location where the
  *             subprogram was called and where program execution is supposed to resume once the
  *             end of the subprogram has been reached.
  *
  *  - ''return'': Marks the end of a subprogram's code. When this instruction is executed the robot
  *             returns to where the subprogram was called and resumes program execution at the
  *             following RoboSpeak instruction.
  *
  * Here is an example of a tribe that makes use of subprogram calls. Notice that just like Scala
  * functions can call other functions, subprograms can also call other subprograms.
  *
  * {{{
  * # A member of the Patrolman Tribe moves clockwise in rectangles,
  * # hacking every enemy it encounters. When it runs into obstacles,
  * # it takes a look around counterclockwise before proceeding.
  *
  * start:
  *   callsub advance     # repeat three times: try to advance
  *   callsub advance
  *   callsub advance
  *   spin                # turn clockwise
  *   goto start
  *
  * advance:              # move unless obstacle in front
  *   ifnempty cantmove
  *   move
  *   return
  * cantmove:             # spin around counterclockwise
  *   switch
  *   spin
  *   spin
  *   spin
  *   switch
  *   return
  * }}}
  *
  * @param name      the name of the tribe
  * @param fileName  the path to a file containing the RoboSpeak program that defines the tribe's behavior */
class Tribe(val name: String, fileName: String) {

  private var nextMemberID: Long = 0          // stepper: 0, 1, 2, ...

  var isPacifist = false
  private val instructions = Buffer[Instruction]()
  private val labels = Map[String, Instruction]()
  private val grammar = new RoboSpeakGrammar(this)
  this.readProgram(fileName) // initializes instructions, labels and isPacifist based on the contents of the RoboSpeak file


  /** Reads a given RoboSpeak program file and parses the instructions and labels
    * contained therein by calling parseLine on each line of the program. */
  private def readProgram(fileName: String) = {
    val tribeFile = Source.fromFile(fileName)
    def clean(line: String) = line.takeWhile( _ != '#' ).trim.toLowerCase
    forEachLine(tribeFile)(clean _ andThen this.parseLine)
  }


  /** Given a line of a RoboSpeak program, adds the information represented by the line to this tribe object. */
  private def parseLine(line: String) = {
    val parsedLine = this.grammar.parse(line)
    this.instructions += parsedLine.instruction
    for (label <- parsedLine.label) {
      this.labels += label -> parsedLine.instruction
    }
    if (parsedLine.directive.contains("pacifist")) {
      this.isPacifist = true
    }
  }


  /** Returns a new, unused name for a new member of this tribe. This name consists
    * of the tribe's name and a unique ID number for the new member. */
  def newName() = {
    this.nextMemberID += 1
    this.name.capitalize + "#" + this.nextMemberID
  }


  /** Returns the instruction at the given line number. If there is no such line in the program,
    * an instruction is returned that terminates the RoboSpeak program when executed. */
  def instructionAt(lineNumber: Int) = instructions.lift(lineNumber - 1).getOrElse(new Instruction.TerminateProgram(lineNumber))


  /** Returns the line number where the given label appears in this tribe's
    * RoboSpeak program; `None` if there is no such label. */
  private def lineAt(label: String) = this.labels.get(label).map( _.lineNumber )


  /** The description is simply the tribe's name.*/
  override def toString = this.name



  /////////////////////////////////////////////////////
  // INSTRUCTIONS WITHIN A PARTICULAR TRIBAL PROGRAM
  /////////////////////////////////////////////////////

  private[tribal] object Instruction {

    private type Param = Either[String, Int]
    private val program = Tribe.this

    trait EndsTurn extends Instruction {
      def endsTurn = true
    }

    trait ContinuesTurn extends Instruction {
      def endsTurn = false
    }

    class TerminateProgram(val lineNumber: Int) extends Instruction with EndsTurn {
      def execute(actor: TribalBot) = {
        this
      }
    }

    abstract class AbstractImpl extends Instruction {

      lazy val followingLine = program.instructionAt(this.lineNumber + 1)

      lazy val lineNumber = {
        val index = program.instructions.indexWhere( _ eq this )
        assert(index >= 0, "Instruction not found within its own program.")
        index + 1
      }

      def eval(actor: TribalBot, parameter: Param) = parameter match {
        case Right(integer) =>
          integer
        case Left(slotName) if (Tribe.Slots.contains(slotName)) =>
          actor.slotValue(slotName)
        case Left(label) =>
          program.lineAt(label).getOrElse(throw new TribeFileException(s"'$label' is not a valid line number, label, or slot name."))
      }

      final def findInstruction(actor: TribalBot, numberSlotOrLabel: Param) =
        program.instructionAt(this.eval(actor, numberSlotOrLabel))

      override def toString = this.getClass.getName

      final def execute(actor: TribalBot) = {
        this.apply(actor)
        this.next(actor)
      }

      def apply(actor: TribalBot): Unit

      def next(actor: TribalBot): Instruction
    }


    trait ToNextLine extends AbstractImpl {
      def next(actor: TribalBot) = this.followingLine
    }


    abstract class Simple(val doAction: TribalBot => Unit) extends AbstractImpl {
      def apply(actor: TribalBot) = {
        this.doAction(actor)
      }
    }

    abstract class Jump extends Simple( _ => { } ) with ContinuesTurn

    abstract class Action(act: TribalBot => Unit) extends Simple(act)

    abstract class Branch(val branchLine: Param) extends Jump {
      def shouldBranch(actor: TribalBot): Boolean
      def next(actor: TribalBot) = if (this.shouldBranch(actor)) this.findInstruction(actor, this.branchLine) else this.followingLine
    }

    abstract class SimpleCheck(branchLine: Param, val checkCondition: TribalBot => Boolean) extends Branch(branchLine) {
      def shouldBranch(actor: TribalBot) = this.checkCondition(actor)
    }

    abstract class Comparison(val number: Param, val number2: Param, branchLine: Param, val compare: (Int, Int) => Boolean) extends Branch(branchLine) {
      def shouldBranch(actor: TribalBot) = this.compare(this.eval(actor, number), this.eval(actor, number2))
    }


    abstract class Arithmetic(resultSlot: String, val operand1: Param, val operand2: Param, val compute: (Int, Int) => Int) extends Load(resultSlot) {
      def newSlotValue(actor: TribalBot) = this.compute(this.eval(actor, this.operand1), this.eval(actor, this.operand2))
    }

    class Plus (resultSlot: String, operand1: Param, operand2: Param) extends Arithmetic(resultSlot, operand1, operand2, _ + _ )
    class Minus(resultSlot: String, operand1: Param, operand2: Param) extends Arithmetic(resultSlot, operand1, operand2, _ - _ )


    abstract class Load(val targetSlot: String) extends AbstractImpl with ToNextLine with ContinuesTurn {
      assert(Tribe.Slots.contains(targetSlot))
      def apply(actor: TribalBot) = {
        actor.setSlotValue(this.targetSlot, this.newSlotValue(actor))
      }
      def slotValue(actor: TribalBot) = actor.slotValue(this.targetSlot)
      def newSlotValue(actor: TribalBot): Int
    }

    class Move extends  Simple( _.moveCarefully() ) with ToNextLine with EndsTurn
    class Spin extends  Simple( _.spin() )          with ToNextLine with EndsTurn
    class Wait extends  Simple( _ => { } )          with ToNextLine with EndsTurn
    class UTurn extends Simple(robo => { robo.spin(); robo.spin() } ) with ToNextLine with EndsTurn



    abstract class Pass extends Jump with ToNextLine
    class LabelDefinition extends Pass
    class NoCommand       extends Pass
    class Directive       extends Pass

    class Switch extends Simple( _.switchDirection() ) with ToNextLine with ContinuesTurn

    class Talk(val line: Param) extends AbstractImpl with ToNextLine with ContinuesTurn {
      def apply(actor: TribalBot) = {
        actor.talk(this.eval(actor, this.line))
      }
    }

    class Shout(line: Param) extends AbstractImpl with ToNextLine with ContinuesTurn {
      def apply(actor: TribalBot) = {
        actor.shout(this.eval(actor, this.line))
      }
    }


    class IfEmpty(branchLine: Param)   extends SimpleCheck(branchLine, _.seesFloor)
    class IfNEmpty(branchLine: Param)  extends SimpleCheck(branchLine, !_.seesFloor)
    class IfFriend(branchLine: Param)  extends SimpleCheck(branchLine, _.seesFriend)
    class IfNFriend(branchLine: Param) extends SimpleCheck(branchLine, !_.seesFriend)
    class IfWall(branchLine: Param)    extends SimpleCheck(branchLine, _.seesWall)
    class IfNWall(branchLine: Param)   extends SimpleCheck(branchLine, !_.seesWall)
    class IfRandom(branchLine: Param)  extends SimpleCheck(branchLine, _ => Random.nextBoolean())
    class Goto(branchLine: Param)      extends SimpleCheck(branchLine, _ => true)

    class IfEq (number: Param, number2: Param, branchLine: Param) extends Comparison(number, number2, branchLine, _ == _ )
    class IfNEq(number: Param, number2: Param, branchLine: Param) extends Comparison(number, number2, branchLine, _ != _ )
    class IfGT (number: Param, number2: Param, branchLine: Param) extends Comparison(number, number2, branchLine, _ > _ )
    class IfLT (number: Param, number2: Param, branchLine: Param) extends Comparison(number, number2, branchLine, _ < _ )

    class CallSub(val sub: Param) extends AbstractImpl with ContinuesTurn {
      def apply(actor: TribalBot) = {
        actor.callSubprogram(this.lineNumber)
      }
      def next(actor: TribalBot) = this.findInstruction(actor, this.sub)
    }

    class Return extends Jump {
      def next(actor: TribalBot) = {
        program.instructionAt(actor.returnFromSubprogram() + 1)
      }
    }

    class Set(targetSlot: String, val newValue: Param) extends Load(targetSlot) {
      def newSlotValue(actor: TribalBot) = this.eval(actor, this.newValue)
    }

    class Add1(targetSlot: String) extends Load(targetSlot) {
      def newSlotValue(actor: TribalBot) = this.slotValue(actor) + 1
    }


    abstract class RadarCount(val includeInCount: (TribalBot, RobotBrain) => Boolean) extends Load(Tribe.RadarSlot) {
      def brainsOnRadar(actor: TribalBot): Iterable[RobotBrain]
      def newSlotValue(actor: TribalBot) = this.brainsOnRadar(actor).count( this.includeInCount(actor, _) )
    }

    trait UsesShortRadar    extends RadarCount { def brainsOnRadar(actor: TribalBot) = actor.shortRadar }
    trait UsesLongRadar     extends RadarCount { def brainsOnRadar(actor: TribalBot) = actor.longRadar  }
    trait UsesDirectedRadar extends RadarCount {
      val dirNumber: Param
      def brainsOnRadar(actor: TribalBot) = actor.directedRadar(this.eval(actor, dirNumber))
    }

    class EnemiesNear extends RadarCount( _.isThreatenedBy(_) ) with UsesShortRadar
    class FriendsNear extends RadarCount( _.isFriend(_)       ) with UsesShortRadar
    class FodderNear  extends RadarCount( _.seesAsFodder(_)   ) with UsesShortRadar
    class FodderLeft  extends RadarCount( _.seesAsFodder(_)   ) with UsesLongRadar

    class EnemiesDir(val dirNumber: Param) extends RadarCount( _.isThreatenedBy(_) ) with UsesDirectedRadar
    class FriendsDir(val dirNumber: Param) extends RadarCount( _.isFriend(_) )       with UsesDirectedRadar

    class Score extends Load(Tribe.RadarSlot) {
      def newSlotValue(actor: TribalBot) = {
        val tribesPerRobot = for {
          currentBot   <- actor.longRadar
          currentTribe <- actor.determineTribe(currentBot)
          if (!currentTribe.isPacifist)
        } yield currentTribe
        val tribeCounts = tribesPerRobot.frequencies
        val myCount = tribeCounts.getOrElse(actor.tribe, 0)
        val competitors = (tribeCounts - actor.tribe)
        if (competitors.isEmpty) myCount else myCount - competitors.values.max
      }
    }
  }

}


/** This is the companion object of class `Tribe`.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this object works or can be used.'''
  *
  * @see the class [[Tribe]] */
object Tribe {

  /** the name of the memory slot that's used by tribal bots' radar */
  val RadarSlot = "radar"
  /** the name of the memory slot that's used by tribal bots' hacking action */
  val HackSlot = "hackline"
  /** the names of all the tribal bots' memory slots */
  val Slots = Vector(HackSlot, "mem2", "mem", RadarSlot)

}


