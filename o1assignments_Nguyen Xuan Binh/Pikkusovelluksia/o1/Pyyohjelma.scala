package o1

// Tämä koodi liittyy lukuun 3.4 ja on esitelty siellä.

object Pyyohjelma extends App {
  val valkoinenTausta = rectangle(600, 600, White)
  val maailmanlopunKuva = rectangle(600, 600, Black)

  val pyy = new Pyy

  val nakyma = new View(pyy, 50) {

    def makePic = pyy.kuvaksi.onto(valkoinenTausta)

    override def onTick() = {
      pyy.pienene()
    }

    override def isDone = pyy.loppuKoitti

  }

  nakyma.start()
}
