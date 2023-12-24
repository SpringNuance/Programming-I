package o1.pong

import o1._
import Direction.{Left,Right}
import Velocity.Still

class Game {

  val leftPaddle  = new Paddle(Right, InitialLeftPaddleCenter,  Still)
  val rightPaddle = new Paddle(Left,  InitialRightPaddleCenter, Still)
  val ball        = new Ball
  this.ball.serve()

  def timePasses() = {
    this.moveBall()
    if (this.ball.isWayOut) {
      this.ball.serve()
    }
    this.leftPaddle.advance()
    this.rightPaddle.advance()
  }


  private def moveBall() = {
    this.ball.advance()
    if (this.rightPaddle.blocks(this.ball)) {
      this.rightPaddle.launch(this.ball)
    } else if (this.leftPaddle.blocks(this.ball)) {
      this.leftPaddle.launch(this.ball)
    } else if (this.ball.bouncesOffWall) {
      this.ball.bounceVertical()
    }
  }


}

