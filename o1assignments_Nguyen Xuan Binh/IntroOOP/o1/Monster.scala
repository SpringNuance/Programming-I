package o1

// This class is introduced in Chapter 2.4.

class Monster(val kind: String, val healthMax: Int) {

  var healthNow = healthMax

  val description = this.kind + " (" + this.healthNow + "/" + this.healthMax + ")"

  def sufferDamage(healthLost: Int) = {
    this.healthNow = this.healthNow - healthLost
  }

}
