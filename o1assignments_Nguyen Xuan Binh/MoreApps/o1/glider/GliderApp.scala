package o1.glider

import o1._

// This program is introduced in Chapter 3.6.

/** A very simple app that lets the user control a “glider” onscreen. */
object GliderApp extends App {

  val glider = new Glider(new Pos(350, 350), 25)

  val gui = new View(glider) {

    private val background = square(700, LightBlue)
    private val gliderPic = Pic("glider.png").scaleTo(50)

    def makePic = {
      val rotatedglider = gliderPic.counterclockwise(glider.heading.toDegrees)
      background.place(rotatedglider, glider.pos)
    }

    override def onTick() = {
      glider.glide()
    }

    override def onKeyDown(key: Key) = {
      if (key == Key.Up) {
        glider.isAccelerating = true
      } // TODO: other actions
    }

    override def onKeyUp(key: Key) = {
      if (key == Key.Up) {
        glider.isAccelerating = false
      } // TODO: other actions
    }

  }

  gui.start()

}