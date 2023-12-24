package o1

// Tämä luokka liittyy lukuun 2.4 ja on esitelty siellä.

class Henkilo(val nimi: String) {

  def lausu(lause: String) = this.nimi + ": " + lause

  def reagoiSrirachaan     = this.lausu("Onpa hyvä kastike.")

  def reagoiKryptoniittiin = this.lausu("Onpa kumma mineraali.")
}


