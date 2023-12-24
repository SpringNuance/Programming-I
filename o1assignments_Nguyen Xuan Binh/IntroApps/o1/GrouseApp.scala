package o1

// This program is introduced in Chapter 3.4.

object GrouseApp extends App {
  val whiteBackground = rectangle(600, 600, White)
  val endOfWorldPic = rectangle(600, 600, Black)

  val grouse = new Grouse

  val view = new View(grouse, 50) {

    def makePic = grouse.toPic.onto(whiteBackground)

    override def onTick() = {
      grouse.shrink()
    }

    override def isDone = grouse.foretellsDoom

  }

  view.start()
}
