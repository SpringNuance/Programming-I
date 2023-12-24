package o1

// Tämä luokka liittyy lukuun 2.6 ja on esitelty siellä.

class Asiakas(val nimi: String, val asiakasnumero: Int, val email: String, val osoite: String) {

  def kuvaus = "#" + this.asiakasnumero + " " + this.nimi + " <" + this.email + ">"

}



