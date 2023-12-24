package o1

// This class is introduced in Chapter 2.6.

class Treasure(val value: Double, val challenge: Int) {

  def guardian = new Monster("troll", this.challenge)

  def appeal = this.value / this.guardian.healthNow

  override def toString = "treasure (worth " + this.value + ") guarded by " + this.guardian

}
