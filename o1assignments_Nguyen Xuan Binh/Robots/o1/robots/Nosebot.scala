package o1.robots

import o1._
class Nosebot(name: String, body: RobotBody) extends RobotBrain(name, body) {
  override def mayMoveTowards(direction: CompassDir): Boolean = {
     if (this.body.neighboringSquare(direction).isEmpty) true else false
  }
  // Checks the square that neighbors the nosebot in the given direction to see
  // if it contains something that the nosebot considers an obstacle. Nosebots are
  // careful: they consider every square that isn’t empty to be an obstacle.
  // This behavior overrides the default implementation from class RobotBrain.
  // (A consequence of this is that a nosebot will consider itself stuck unless there is
  // at least one empty square next to it. See isStuck and controlTurn.)
  // Definition Classes
  // Nosebot → RobotBrain

  def attemptMove(): Boolean = {
    if (this.squareInFront.isEmpty){
       this.body.moveTowards(body.facing)
         true
    } else {
      this.body.spinClockwise()
      false
    }
  }
  // Attempts to move the robot one step forward in its current heading (as per
  // moveCarefully). Failing that, turns the robot 90 degrees clockwise instead.
  // returns true if the robot moved forward, false if the route was blocked and the robot turned instead

  def moveBody(): Unit = {
    var i = 0
    while (!attemptMove() && i < 4) {
      i += 1
    }
  }
    // Moves the robot. A nosebot first looks at the next square in the direction it is
    // currently facing. If that square is empty, it moves there and ends its turn.
    // If the square was not empty, the bot turns 90 degrees clockwise and immediately
    // tries again (during the same turn): it again moves a step forward if possible,
    // and turns to check the next direction if necessary. In case the robot completes
    // a 360-degree turnabout without finding a suitable square to move in, it ends its
    // turn without changing location.
    // In other words, a nosebot uses its turn to call attemptMove up to four times.
    //As a nosebot always looks where it's going, so it can never collide with anything
    // during its own turn. A nosebot only ever moves a single square per turn.
    //This method assumes that it is not called if the robot is broken or stuck.
    // You could implement moveBody with a loop.
    //Definition Classes
    //Nosebot → RobotBrain
}


