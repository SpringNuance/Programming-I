package o1

import constants._

// Define class Bug here.

class Bug(private var currentPos: Pos) {
  val radius = 15
  private var yVelocity:Double = 0
  def pos = currentPos
  def move() = {
    this.currentPos = this.currentPos.clampY(0,350)
    this.currentPos
  }
  def flap(y:Double) = {
    yVelocity= -y
    move()
  }
  def fall() = {
    if (currentPos.y >= 350)
      {
   this.currentPos = this.currentPos.addY(yVelocity)
    move()
      }
    else {
    yVelocity = yVelocity + 2
    this.currentPos = this.currentPos.addY(yVelocity)
    move()
    }
  }

  def isInBounds = {
  if (currentPos.y > 0 && currentPos.y < 350) true else false
  }

  override def toString = "This Bug is at" + this.currentPos
}
