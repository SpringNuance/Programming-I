package o1

// Tämä koodi liittyy lukuun 3.4 ja on esitelty siellä.

class Pyy {

  private var koko = 400
  private val peruskuva = Pic("bird.png")

  def loppuKoitti = this.koko <= 0

  def pienene() = {
    if (this.koko > 0) {
      this.koko = this.koko - 1
    }
  }

  def kuvaksi = this.peruskuva.scaleTo(this.koko)

}

