package o1.blood


class BloodType(val abo: String, val rhesus: Boolean) {


  override def toString = {
    if (this.rhesus) s"$abo+" else s"$abo-"
  }


  def hasSafeRhesusFor(recipient: BloodType) = {
    !(this.rhesus && !recipient.rhesus)
  }


  def hasSafeABOFor(recipient: BloodType) = {
    (this.abo == "O") || (recipient.abo == "AB") || (this.abo == "A" && recipient.abo == "A") || (this.abo == "B" && recipient.abo == "B")
  }


  def canDonateTo(recipient: BloodType) = {
    this.hasSafeABOFor(recipient) && this.hasSafeRhesusFor(recipient)
  }


  def canReceiveFrom(donor: BloodType) = {
    donor.hasSafeABOFor(this) && donor.hasSafeRhesusFor(this)
  }


}

