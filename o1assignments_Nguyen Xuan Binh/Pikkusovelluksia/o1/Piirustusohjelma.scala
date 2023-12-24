package o1

// Tämä koodi liittyy lukuun 3.1 ja on esitelty siellä.

object Piirustusohjelma extends App {

  val teos = new Taideprojekti(rectangle(600, 600, White))

  val nakyma = new View(teos) {
    def makePic = teos.kuva

    // Tapahtumankäsittelykoodi puuttuu.
  }

  nakyma.start()

}

