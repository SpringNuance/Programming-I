package o1

// This program is introduced in Chapter 3.1.

object TickingApp extends App {
  val tickCounter = new Counter(0)
  val background = rectangle(500, 500, Black)

  val gui = new View(tickCounter) {
    def makePic = background.place(square(tickCounter.value, White).clockwise(tickCounter.value), new Pos(250, 250))

    override def onTick() = {
      tickCounter.advance()
    }
  }

  gui.start()
}
