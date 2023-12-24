package o1

import constants._
import scala.math._
import scala.util.Random
// This class is introduced in Chapter 2.6.

class Obstacle(val radius: Int) {
  private var currentPos = randomLaunchPosition()
  def pos = this.currentPos

  def isActive() = {
      if (-this.radius <= this.currentPos.x) true else false
  }

  def approach() = {
    if (isActive()) {
    this.currentPos = this.currentPos.addX(-ObstacleSpeed)
    }
    else currentPos = randomLaunchPosition()
  }

  private def randomLaunchPosition() = {
  val launchX = ViewWidth + this.radius + Random.nextInt(500)
  val launchY = Random.nextInt(ViewHeight)
  new Pos(launchX, launchY)
}

  def touches(bug: Bug) = {
      var distance = bug.pos.distance(this.pos)
      var x = distance < bug.radius + this.radius
          x
  }

  override def toString = "center at " + this.pos + ", radius " + this.radius
}
