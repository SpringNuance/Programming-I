package o1

// Tämä luokka liittyy lukuun 2.6 ja on esitelty siellä.

class Aarre(val arvo: Double, val haaste: Int) {

  def vartija = new Hirvio("peikko", this.haaste)

  def houkuttelevuus = this.arvo / this.vartija.nykykunto

  override def toString = "aarre (arvo " + this.arvo + "), jota vartioi " + this.vartija

}
