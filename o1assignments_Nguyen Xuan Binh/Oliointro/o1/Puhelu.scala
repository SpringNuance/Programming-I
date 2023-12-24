package o1

// Tämä luokka liittyy lukuun 2.3 ja on esitelty siellä.

class Puhelu(alkuhintaIlmanMpm: Double, minuuttihintaIlmanMpm: Double, val kesto: Double) {

  val VerkkomaksuAloitus = 0.0099
  val VerkkomaksuMinuutilta = 0.0199
  val alkuhinta = alkuhintaIlmanMpm + VerkkomaksuAloitus
  val minuuttihinta = minuuttihintaIlmanMpm + VerkkomaksuMinuutilta

  def kokonaishinta = this.alkuhinta + this.minuuttihinta * this.kesto

  def kuvaus =
    "%.2f min, %.5fe (%.5fe + %.5fe/min)".format(this.kesto, this.kokonaishinta, this.alkuhinta, this.minuuttihinta)

}
