package o1

// Tämä koodi liittyy lukuun 2.7 ja on esitelty siellä.

object Avaruusohjelma extends App {

  val avaruus = new Avaruus(500)

  val tausta = rectangle(avaruus.leveys, avaruus.korkeus, Black)
  val maanKuva = circle(avaruus.maa.sade * 2, MediumBlue)
  val kuunKuva = circle(avaruus.kuu.sade * 2, Beige)

  // Kirjoita toimiva koodi kysymysmerkeillä merkittyihin kohtiin
  val gui = new View(???, "Yksinkertainen näkymä avaruuteen") {
    def makePic = ???
  }

  ??? // Tässä pitäisi käynnistää gui-muuttujan osoittama näkymä.

}

