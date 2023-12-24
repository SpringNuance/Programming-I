package o1.auctionhouse

import scala.math._

class EnglishAuction(val description: String, val startingPrice: Int, duration: Int) {

  // Keep these variables; they will be useful for implementing the methods.
  private var highestBid = new Bid(None, startingPrice)       // most-wanted holder
  private var secondHighestBid = new Bid(None, startingPrice) // most-wanted holder
  private var remainingDays = ??? // TODO: replace ??? with a suitable expression


  // However, the method implementations given below leave a lot to be desired.


  def daysLeft: Int = ??? // TODO: replace with a working method implementation


  def hasNoBids: Boolean = ???


  def advanceOneDay() = {
    // TODO: implement this method
  }


  def isOpen: Boolean = ??? // TODO: replace with a working method implementation


  def isExpired: Boolean = ??? // TODO: replace with a working method implementation


  def buyer: Option[String] = ??? // TODO: replace with a working method implementation


  def price: Int = ??? // TODO: replace with a working method implementation


  def requiredBid: Int = ??? // TODO: replace with a working method implementation


  def bid(bidder: String, amount: Int): Boolean = {
    ??? // TODO: replace with a working method implementation
  }


  override def toString = this.description


}
