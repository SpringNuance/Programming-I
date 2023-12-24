package o1.charger

import o1._
import scala.math._

// This program is introduced in Chapter 3.6.

/** A "charger" is an entity that moves about on a two-dimensional plane.
  * It is capable of accelerating in parallel to the two coordinate axes.
  *
  * @param pos       the position of the charger
  * @param maxSpeed  a limit beyond which the charger cannot accelerate */
class Charger(var pos: Pos, val maxSpeed: Double) {

  private var angle: Direction = Direction.Right // most-recent holder: the direction the charger most recently turned to
  private var speed = 0.0                        // gatherer: increases when accelerating; reset to zero when turning

  /** The direction the charger's front is facing.
    * A charger can only move in the direction it is currently facing. */
  def heading = this.angle


  /** Instructs the charger to accelerate in the given direction. If that direction is
    * the charger's current heading, the charger increases its speed by one (but no higher
    * than its `maxSpeed`). Otherwise, the charger's speed drops to zero and the charger
    * heads towards the new direction. */
  def accelerate(dirOfMovement: Direction) =  {
    if (this.heading == dirOfMovement) {
      // TODO: the speed should be adjusted here
    } else {
      // TODO: the speed and angle should be adjusted here
    }
  }

  /** Moves the charger within its coordinate system. The charger's position changes
    * a number of units equal to the charger's `speed`, in a straight line towards
    * the charger's `heading`. */
  def move() = {
    val xMove = this.angle.dx * this.speed
    // TODO: should also consider the y axis and adjust the charger's position
  }

}


