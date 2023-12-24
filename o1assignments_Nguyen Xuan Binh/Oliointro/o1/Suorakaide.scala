package o1

// Tämä luokka liittyy lukuun 2.4 ja on esitelty siellä.

class Suorakaide(annettuSivunPituus: Double, annettuToinenSivunPituus: Double) {

  val sivu1 = annettuSivunPituus
  val sivu2 = annettuToinenSivunPituus

  def ala = this.sivu1 * this.sivu2

  // jne. (Täällä voisi olla muita metodeita.)

}
