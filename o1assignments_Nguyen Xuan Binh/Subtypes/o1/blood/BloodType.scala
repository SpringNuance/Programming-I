package o1.blood

// This code is introduced in Chapter 7.2.

trait Rhesus {
  val isPositive: Boolean
  def isNegative = !this.isPositive
  def canDonateTo(recipient: Rhesus) = this.isNegative || this == recipient
  def canReceiveFrom(donor: Rhesus) = donor.canDonateTo(this)
}

object RhPlus extends Rhesus {
  val isPositive = true
  override def toString = "+"
}

object RhMinus extends Rhesus {
  val isPositive = false
  override def toString = "-"
}


// You can write your ABO trait and the four singleton objects here.





















// You don't need to edit the ABORh class below. See the ebook chapter for more info.
/*
class ABORh(val abo: ABO, val rhesus: Rhesus) {
  def canDonateTo(recipient: ABORh) = this.abo.canDonateTo(recipient.abo) && this.rhesus.canDonateTo(recipient.rhesus)
  def canReceiveFrom(donor: ABORh) = donor.canDonateTo(this)
  override def toString = this.abo.toString + this.rhesus.toString
}
*/
