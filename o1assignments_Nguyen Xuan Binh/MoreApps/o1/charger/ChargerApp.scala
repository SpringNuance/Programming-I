package o1.charger

import o1._

// This program is introduced in Chapter 3.6.

/** A very simple app that lets the user control a “charger” onscreen. */
object ChargerApp extends App {

  val charger = new Charger(new Pos(350, 350), 25)

  val gui = new View(charger) {

    private val background = square(700, LightGray)
    private val chargerPic = Pic("bull.png")

    def makePic = background.place(chargerPic, charger.pos)

    override def onTick() = {
      charger.move()
    }

    override def onKeyDown(key: Key) = {
      if (key == Key.Up) {
        charger.accelerate(Direction.Up)
      } // TODO: other directions missing
    }

  }

  gui.start()

}