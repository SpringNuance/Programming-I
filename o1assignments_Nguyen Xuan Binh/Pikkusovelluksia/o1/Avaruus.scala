package o1

// Tämä koodi liittyy lukuihin 2.6 ja 2.7 ja on esitelty siellä.

class Taivaankappale(val nimi: String, val sade: Double, var sijainti: Pos) {

  def halkaisija = this.sade * 2

  override def toString = this.nimi

}


class Avaruus(koko: Int) {
  val leveys = koko * 2
  val korkeus = koko

  val maa = new Taivaankappale("Maa", 15.9, new Pos(10,  this.korkeus / 2))
  val kuu = new Taivaankappale("Kuu", 4.3,  new Pos(971, this.korkeus / 2))

  override def toString = s"${this.leveys}x${this.korkeus} alue avaruudessa"
}


