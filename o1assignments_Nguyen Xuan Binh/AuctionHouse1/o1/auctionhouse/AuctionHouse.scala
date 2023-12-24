package o1.auctionhouse

import scala.collection.mutable.Buffer


/** The class `AuctionHouse` represents electronic auction houses. It provides methods for
  * adding auctions and producing statistics about the items being sold, among other things.
  *
  * This version of class `AuctionHouse` can only handle English-style auctions (of type
  * `EnglishAuction`). Other ways of selling items are not supported.
  *
  * @param name  the name of the auction house */
class AuctionHouse(val name: String) {

  private val items = Buffer[EnglishAuction]()


  /** Adds the given auction to the auction house. */
  def addItem(item: EnglishAuction): Unit = {
    this.items += item
  }


  /** Removes the given auction from the auction house, assuming it was there. */
  def removeItem(item: EnglishAuction): Unit = {
    this.items -= item
  }


  /** Produces a textual representation of this auction house. */
  override def toString = if (this.items.isEmpty) this.name else this.name + ":\n" + this.items.mkString("\n")


  /** Records one day as having passed. This is equivalent to calling
    * `auction.advanceOneDay()` for each of the auctions in this auction house.
    * @see [[EnglishAuction.advanceOneDay]] */
  def nextDay() = {
    for (current <- this.items) {
      current.advanceOneDay()
    }
  }


  /** Returns the current total price of all the items that have been put up for sale in this
    * auction house. The total includes the prices of all auctions, be they open or closed. */
  def totalPrice = {
    var totalSoFar = 0
    for (current <- this.items) {
      totalSoFar += current.price
    }
    totalSoFar
  }


  /** Returns the current average price of all the items that have been put up for sale in this
    * auction house. The average is computed from the prices of all auctions, be they open or closed. */
  def averagePrice = this.totalPrice.toDouble / this.items.size


  /** Returns the number of auctions in this auction house that are currently open. */
  def numberOfOpenItems = {
    var openCount = 0
    for (current <- this.items) {
      if (current.isOpen) {
        openCount += 1
      }
    }
    openCount
  }


  /** Returns the priciest item in the auction house, that is, the item whose current price is
    * the highest. Both open and closed items are considered. The item is returned in an `Option`
    * wrapper; if there are no auctions at all, `None` is returned. */
  def priciest = {
    if (this.items.isEmpty) {
      None
    } else {
      var priciestSoFar = this.items.head
      for (current <- this.items) {
        if (current.price > priciestSoFar.price) {
          priciestSoFar = current
        }
      }
      Some(priciestSoFar)
    }
  }


  /** Returns a collection that contains the purchases of a single buyer. This means all the
    * (open or closed) items that have either already been bought by the given person, or that
    * have the person as the highest bidder.
    * @param buyer  the name of the buyer whose purchases should be returned */
  def purchasesOf(buyer: String) = {
    val purchases = Buffer[EnglishAuction]()
    for (current <- this.items) {
      if (current.buyer == Some(buyer)) {
        purchases += current
      }
    }
    purchases.toVector
  }

  def findAll(checkCriterion: EnglishAuction => Boolean) = {
    val found = Buffer[EnglishAuction]()
    for (currentItem <- this.items) {
      if (checkCriterion(currentItem)) {
        found += currentItem
      }
    }
    found.toVector
  }

}

