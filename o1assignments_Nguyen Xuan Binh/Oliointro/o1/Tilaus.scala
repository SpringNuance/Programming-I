package o1

// Tämä luokka liittyy lukuun 2.6 ja on esitelty siellä.

class Tilaus(val numero: Int, val tilaaja: Asiakas) {

  var kokonaishinta = 0.0   // kokooja

  def lisaaTuote(kappalehinta: Double, lukumaara: Int) = {
    this.kokonaishinta = this.kokonaishinta + kappalehinta * lukumaara
  }

  def kuvaus = "tilaus " + this.numero + ", tilaaja: " + this.tilaaja.kuvaus + ", yhteensä " + this.kokonaishinta + " euroa"

}