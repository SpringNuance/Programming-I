package o1.auctionhouse

class FixedPriceSale(val description: String, val price: Integer, private var remainingDays: Integer) {

  private var nameOfBuyer: Option[String] = None

  override def toString: String = description

  def daysLeft() = remainingDays

  def buyer() = nameOfBuyer

  def isExpired() = if (remainingDays == 0) true else false


  def isOpen() = if (remainingDays > 0 && nameOfBuyer.isEmpty) true else false


  def advanceOneDay() = {
    if (remainingDays > 0 && nameOfBuyer.isEmpty) {
        remainingDays -= 1
        remainingDays
    } else remainingDays
  }


  def buy(name: String) = {
    nameOfBuyer match {
      case None => {
        if (this.isOpen()) {
          nameOfBuyer = Some(name)
          true
        } else false
      }
      case Some(name) => false
    }
  }

}
