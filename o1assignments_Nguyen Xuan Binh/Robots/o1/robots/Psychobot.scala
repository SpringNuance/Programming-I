package o1.robots

import o1._
class Psychobot(name: String, body: RobotBody) extends RobotBrain(name, body) {
//The class Psychobot represents the "brains" (or AI) of robots which stand still until they see
// another unbroken robot. When this happens, they ram the victim with incredible speed.
  private def firstNonEmptyIn(dir: CompassDir) = {
    var radarLocation = this.location
    while (this.world(radarLocation).isEmpty){
      radarLocation = radarLocation.neighbor(dir)
    }
    this.world(radarLocation)
  }

  private def seesVictim(dir: CompassDir) = this.firstNonEmptyIn(dir).robot.exists( _.isIntact )

  private def directionOfVictim = CompassDir.Clockwise.find(this.seesVictim)

  private def charge(dir: CompassDir): Unit = {
     LazyList.continually( this.body.moveTowards(dir) ).find( _ == false )
  }

  def moveBody() = {
    this.directionOfVictim.foreach(this.charge)
  }

}
  //Moves the robot. During its turn, a psychobot uses its sensors to scan the four main compass
  // directions, always starting with north and continuing clockwise. (It does not change facing
  // while doing this.) When the psychobot notices a mobile robot, it turns to face it, instantly
  // moves an unlimited number of squares towards the robot, and rams into it, causing the victim
  // to break. After the collision, the psychobot remains in the square adjacent to its victim,
  // and stops scanning for the rest of the turn. During its next turn, it will start over by
  // looking north.
  //
  // If there are no victims in any of the four main directions, the robot waits still.
  // It does not attack broken robots. A psychobot can not scan through walls and it can never
  // collide with a wall. It also does not see through robots, not even broken ones.


