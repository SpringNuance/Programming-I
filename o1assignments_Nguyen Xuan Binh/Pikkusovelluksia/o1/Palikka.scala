package o1

// Tämä luokka liittyy lukuun 2.7 ja on esitelty siellä.

class Palikka(val koko: Int, val sijainti: Pos, val vari: Color) {
  override def toString = this.vari.toString + " block at " + this.sijainti
}

