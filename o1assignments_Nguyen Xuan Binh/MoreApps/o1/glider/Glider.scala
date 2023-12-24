package o1.glider

import o1._
import scala.math._

// This program is introduced in Chapter 3.6.

/** A "glider" is an entity that moves about on a two-dimensional plane.
  * It can accelerate and turn right or left. It has no breaks but is slowed
  * down by friction.
  *
  * @param pos       the position of the glider
  * @param maxSpeed  a limit beyond which the glider cannot accelerate
  */
class Glider(var pos: Pos, val maxSpeed: Double) {

  private var velocity = new Velocity(Direction.Right, 0)

  /** `true` whenever the glider's acceleration is currently on; initially `false` */
  var isAccelerating = false
  /** `true` whenever the glider is being guided leftwards; initially `false` */
  var isTurningLeft  = false
  /** `true` whenever the glider is being guided leftwards; initially `false` */
  var isTurningRight = false

  /** The direction the glider's front is facing. A glider can only move in this direction. */
  def heading = this.velocity.direction


  /** Does the following, in order:
    *
    * 1. Increases the glider's speed by one (but no higher than its `maxSpeed` if the
    *    glider is currently accelerating. Turns the glider 10 degrees left or right if
    *    it is currently being instructed to do so.
    * 2. Moves the glider within its coordinate system. The glider's position changes
    *    a number of units equal to the glider's (new) speed, in a straight line towards
    *    the glider's (new) heading.
    * 3. Finally, reduces the glider's speed by 0.2 due to friction.
    */
  def glide() = {
    this.adjustVelocity()
    this.pos = this.pos.nextPos(this.velocity)
    this.applyFriction()
  }

  private def adjustVelocity() = {
    // TODO: adjust velocity as indicated by isAccelerating, isTurningLeft, and isTurningRight
  }

  private def applyFriction() = {
    this.velocity = this.velocity.slower(0.2)
  }

}


