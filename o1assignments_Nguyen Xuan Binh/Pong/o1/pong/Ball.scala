package o1.pong

import scala.util.Random
import o1._
import Velocity.Still


class Ball(var pos: Pos = Screen.center, var velocity: Velocity = Still, var status: Ball.Status = Ball.InFlight) extends MovingObjectInContainer {

  val radius = BallRadius
  val width  = radius * 2
  val height = radius * 2
  val container = Court


  def advance() = {
    this.moveFreely()
    this.status = Ball.InFlight
  }


  def bouncesOffWall = this.top < Court.top || this.bottom > Court.bottom


  def isWayOut = this.right <= Screen.left - WayOutMargin || this.left >= Screen.right + WayOutMargin


  def bounceVertical() = {
    this.bringYToContainer()
    //this.velocity = this.velocity.switchY
    this.status = Ball.InFlight
  }


  def hit(newVelocity: Velocity, newStatus: Ball.Status) = {
    this.velocity = newVelocity
    this.status = newStatus
  }


  def serve() = {
    val generalDirection = if (Random.nextBoolean()) Direction.Right else Direction.Left
    val randomDirection = launchDirection(generalDirection, math.random() * 2 - 1)
    this.pos = Screen.center
    this.velocity = Velocity(randomDirection, Paddles.InitialLaunchSpeed)
    this.status = Ball.InFlight
  }

}


object Ball {

  sealed trait Status
  case object InFlight     extends Status
  case object BeingHit     extends Status
  case object BeingSmashed extends Status

}

