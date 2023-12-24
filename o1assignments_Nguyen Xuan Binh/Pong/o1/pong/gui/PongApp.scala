package o1.pong.gui

import o1._
import o1.pong._
import o1.Direction._

object PongApp extends App {

  val game = new Game

  val pongView = new View(game, TicksPerSecond, "Pong") {

    val fieldPic = {
      val wallPic = rectangle(Screen.width, WallThickness, Black)
      val blank   = rectangle(Screen.width, Screen.height, LightGray)
      blank.place(wallPic, its = TopLeft,    atMy = TopLeft)
           .place(wallPic, its = BottomLeft, atMy = BottomLeft)
    }
    val paddlePic = rectangle(Paddles.Thickness, Paddles.Size, White)
    val ballPic   = circle(BallRadius * 2, Red)
    val smackSound = Sound("sounds/slap.wav")


    override def onTick() = {
      game.timePasses()
    }


    override def onKeyDown(key: Key) = key match {
      case Key.W    => game.leftPaddle.push(Up)
      case Key.S    => game.leftPaddle.push(Down)
      case Key.Up   => game.rightPaddle.push(Up)
      case Key.Down => game.rightPaddle.push(Down)
      case otherKey => // do nothing
    }


    override def onKeyUp(key: Key) = key match {
      case Key.W  | Key.S    => game.leftPaddle.stop()
      case Key.Up | Key.Down => game.rightPaddle.stop()
      case otherKey          => // do nothing
    }


    def makePic = fieldPic.place(paddlePic -> game.leftPaddle.pos,
                                 paddlePic -> game.rightPaddle.pos,
                                 ballPic   -> game.ball.pos)


    override def sound = game.ball.status match {
      case Ball.BeingHit     => Some(smackSound.withVolume(-8))
      case Ball.BeingSmashed => Some(smackSound.withVolume(+5))
      case Ball.InFlight     => None
    }

  }

  pongView.start()

}
