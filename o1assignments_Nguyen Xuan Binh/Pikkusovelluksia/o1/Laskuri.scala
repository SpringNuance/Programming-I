package o1

// Tämä luokka liittyy lukuun 3.1 ja on esitelty siellä.

class Laskuri(var arvo: Int) {

  def etene() = {
    this.arvo = this.arvo + 1
  }

  override def toString = "arvo " + this.arvo

}
