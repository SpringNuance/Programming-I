package o1

// Tämä koodi liittyy lukuun 3.1 ja on esitelty siellä.

object Klikkausohjelma extends App {
  val klikkauslaskuri = new Laskuri(5)
  val tausta = rectangle(500, 500, Black)

  val nakyma = new View(klikkauslaskuri) {
    def makePic = tausta.place(circle(klikkauslaskuri.arvo, White), new Pos(100, 100))

    override def onClick(klikkauskohta: Pos) = {
      klikkauslaskuri.etene()
      println("Klikkaus koordinaateissa " + klikkauskohta + "; " + klikkauslaskuri)
    }
  }

  nakyma.start()
}
