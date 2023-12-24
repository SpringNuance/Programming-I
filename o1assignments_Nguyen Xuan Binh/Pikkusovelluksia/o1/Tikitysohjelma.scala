package o1

// Tämä koodi liittyy lukuun 3.1 ja on esitelty siellä.

object Tikitysohjelma extends App {
  val tikityslaskuri = new Laskuri(0)
  val tausta = rectangle(500, 500, Black)

  val nakyma = new View(tikityslaskuri) {
    def makePic = tausta.place(circle(tikityslaskuri.arvo, White), new Pos(250, 250))

    override def onTick() = {
      tikityslaskuri.etene()
    }
  }

  nakyma.start()
}
