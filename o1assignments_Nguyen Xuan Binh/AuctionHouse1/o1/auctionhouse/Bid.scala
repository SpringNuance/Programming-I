package o1.auctionhouse

/** The class `Bid` represents bids made for items up for auction in an electronic auction
  * house. Each `Bid` object stores the name of the bidder --- that is, the customer who made
  * the bid --- as well as the maximum amount the bidder is willing to pay for the item.
  *
  * A bid object may also be created that has no bidder at all. This is useful, because a
  * `Bid` object with no bidder can now be used for representing the initial prices of auctions
  * before any genuine bids have been made.
  *
  * A bid object is immutable after creation.
  *
  * @param bidder  the name of the customer who made the bid, wrapped in an `Option`,
  *                or `None` if the object represents an initial price with no bidder
  * @param limit   the maximum sum of money the bidder is willing to pay for the item
  *
  * @see [[EnglishAuction]] */
class Bid(val bidder: Option[String], val limit: Int) {

  /** Determines whether this bid is an "bidderless bid" that represents an auction's initial price. */
  def isInitialBid = this.bidder.isEmpty


  /** Determines whether this bid beats the given competing bid. A bid beats another if it
    * has a higher limit, or if the limits are equal and the other bid is a bidderless initial bid. */
  def beats(olderBid: Bid) = this.limit > olderBid.limit || (olderBid.isInitialBid && this.limit == olderBid.limit)


  /** Checks whether this bid beats the given older bid, and returns one of the two bids
    * accordingly: this bid if it beats the given one, the given bid otherwise. */
  def winner(olderBid: Bid) = if (this.beats(olderBid)) this else olderBid

}