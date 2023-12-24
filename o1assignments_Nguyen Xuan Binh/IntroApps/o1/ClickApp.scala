package o1

// This program is introduced in Chapter 3.1.

object ClickApp extends App {
  val clickCounter = new Counter(5)
  val background = rectangle(500, 500, Black)

  val gui = new View(clickCounter) {
    def makePic = background.place(circle(clickCounter.value, White), new Pos(100, 100))

    override def onClick(locationOfClick: Pos) = {
      clickCounter.advance()
      println("Click detected at " + locationOfClick + "; " + clickCounter)
    }
  }

  gui.start()
}
