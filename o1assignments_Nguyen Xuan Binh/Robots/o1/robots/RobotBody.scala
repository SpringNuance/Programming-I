package o1.robots

import o1._


/** The class `RobotBody` represents virtual robots (or "bots") which inhabit two-dimensional
  * grid worlds. More specifically, each instance of the class represents a "robot body"
  * (or "chassis" or "hardware") and basic functionality of such a robot. Each "robot body"
  * is associated with a "robot brain" that controls the body and determines what functionality
  * is activated and when.
  *
  * A robot is equipped with the various capabilities:
  *
  * - It can sense its own surroundings (location, facing, the world that it is in).
  *
  * - It can spin around in any one of the four main compass directions.
  *
  * - It can move into the next square in a given direction.
  *
  * - It can sense whether it is broken or not, and whether it is "stuck" in a square between
  *   four walls.
  *
  * When a robot's `takeTurn` method is called, it uses its "brain" to figure out what to do
  * (move, turn about, etc.). Robots with different kinds of brains behave differently.
  *
  * @param world            the world inhabited by the robot
  * @param initialLocation  the initial location of the robot in its world
  * @param initialFacing    the direction the robot initially faces in
  *
  * @see [[RobotWorld]]
  * @see [[RobotBrain]] */
class RobotBody(val world: RobotWorld, initialLocation: GridPos, initialFacing: CompassDir) {

  /** the robot's brain (if it has one) */
  var brain: Option[RobotBrain] = None              // most-recent holder (it is possible for a robot to change brains (it will be, eventually, anyway))
  private var coordinates = initialLocation         // gatherer: changes in relation to the old location
  private var isBroken = false                      // flag: can be broken or intact
  private var facesTowards = initialFacing          // most-recent holder (can be changed arbitrarily)


  /** Returns the coordinates that indicate the robot's current location in the robot world. */
  def location:GridPos = this.coordinates


  /** Returns the square the robot is currently in. */
  def locationSquare:Square = this.world(this.location)


  /** Returns a square that neighbors the robot's current location in the given direction. */
  def neighboringSquare(direction: CompassDir):Square = world(this.location.neighbor(direction))


  /** Returns the direction the robot is currently facing in. */
  def facing:CompassDir = this.facesTowards


  /** Turns the robot to face in the specified direction. */
  def spinTowards(newFacing: CompassDir):Unit = {
    this.facesTowards = newFacing
  }


  /** Turns the robot 90 degrees clockwise. */
  def spinClockwise():Unit = {
   facesTowards = facesTowards.clockwise
  }

  /** Causes the robot to malfunction (typically as the result of a collision).
    * A broken robot does not do anything during its turns.
    * @see [[fix]] */
  def destroy():Unit = {
    this.isBroken = true
  }


  /** Repairs a broken robot. The robot can now start taking its turns normally.
    * @see [[destroy]] */
  def fix():Unit = {
    this.isBroken = false
  }


  /** Determines whether the robot is currently intact or not. A robot is intact
    * unless it has been broken with the `destroy` method and not fixed since. */
  def isIntact:Boolean = !this.isBroken


  /** Relocates the robot within its current world to the square next to the robot's
    * current location, in the given direction. The direction does not necessarily have
    * to be the same one that the robot is originally facing in.
    *
    * This method turns the robot to face in the direction it moves in.
    *
    * Two robots can never be in the same square; neither can a robot and a wall. If the
    * robot's would-be location is not empty, a collision occurs instead and the robot
    * does not change locations (but still turns to face whatever it collided with).
    *
    * If the moving robot collides with a wall, the robot itself breaks. If a moving robot
    * collides with another robot, the other robot breaks and the moving robot stays intact.
    *
    * @return `true` if the robot successfully changed locations, `false` if it
    *         did not (even if it changed facing) */
  def moveTowards(direction: CompassDir):Boolean = {
    this.spinTowards(direction)
    val targetCoordinates = this.location.neighbor(direction)
    val targetSquare = this.world(targetCoordinates)
    val managedToMove = targetSquare.addRobot(this)
    if (managedToMove) {
      this.locationSquare.clear()
      this.coordinates = targetCoordinates
    }
    managedToMove
  }


  /** Gives the robot a turn to act.
    *
    * A broken robot does nothing during its turn; a brainless robot likewise does nothing.
    * An intact robot with a brain consults its brain to find out what to do with its turn.
    * It does that by calling the brain's `controlTurn` method.
    *
    * @see [[RobotBrain.controlTurn]] */
def takeTurn():Unit = {
  if (this.isIntact) {
    this.brain.foreach(_.controlTurn())
  }
}

}


