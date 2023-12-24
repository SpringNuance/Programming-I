package o1.auctionhouse

/** A small program that uses the class `FixedPriceSale`.
  * May be expanded by the student as desired.
  *
  * Assuming the class `FixedPriceSale` has been implemented
  * correctly, the output of this program (as given) is:
  * {{{
  * TWO SALES CREATED.
  * days left: 4 & 2
  * buyers:    None & None
  * open:      true & true
  * expired:   false & false
  *
  * ONE DAY PASSES.
  * days left: 3 & 1
  * buyers:    None & None
  * open:      true & true
  * expired:   false & false
  *
  * THE FIRST ITEM IS BOUGHT. A DAY PASSES.
  * days left: 3 & 0
  * buyers:    Some(T. Teekkari) & None
  * open:      false & false
  * expired:   false & true
  *
  * A DAY PASSES. ATTEMPTS ARE MADE TO BUY EACH OF THE ITEMS.
  * days left: 3 & 0
  * buyers:    Some(T. Teekkari) & None
  * open:      false & false
  * expired:   false & true
  * }}}
  */
object FixedPriceSaleTest extends App {


  def checkIfOpen(candidate: EnglishAuction) = candidate.isOpen

  def checkIfHandbag(candidate: EnglishAuction) = candidate.description.toLowerCase.contains("handbag")

  val house = new AuctionHouse("ReBay")
  house.addItem(new EnglishAuction("A glorious handbag", 100, 14))
  house.addItem(new EnglishAuction("Collectible Easter Bunny China Thimble", 1, 10))

  println(house.findAll(checkIfOpen))    // finds both auctions
  println(house.findAll(checkIfHandbag)) // finds only the first auction

  // Feel free to modify this program.

  /* Initialize a couple of sales:
  val bookSale = new FixedPriceSale("Programming in Scala, 3rd Ed.", 30, 4)
  val cardSale = new FixedPriceSale("100 POKEMON CARDS RARE!!!", 50, 2)
  println("TWO SALES CREATED.")

  // Report on the current state of the sales.
  println("days left: " + bookSale.daysLeft  + " & " + cardSale.daysLeft)
  println("buyers:    " + bookSale.buyer     + " & " + cardSale.buyer)
  println("open:      " + bookSale.isOpen    + " & " + cardSale.isOpen)
  println("expired:   " + bookSale.isExpired + " & " + cardSale.isExpired)

  // Stuff happens to the sales.
  println("\nONE DAY PASSES.")
  bookSale.advanceOneDay()
  cardSale.advanceOneDay()

  // Report on the current state of the sales.
  println("days left: " + bookSale.daysLeft  + " & " + cardSale.daysLeft)
  println("buyers:    " + bookSale.buyer     + " & " + cardSale.buyer)
  println("open:      " + bookSale.isOpen    + " & " + cardSale.isOpen)
  println("expired:   " + bookSale.isExpired + " & " + cardSale.isExpired)

  // More stuff happens.
  println("\nTHE FIRST ITEM IS BOUGHT. A DAY PASSES.")
  bookSale.buy("T. Teekkari")
  bookSale.advanceOneDay()
  cardSale.advanceOneDay()

  // Report on the current state of the sales.
  println("days left: " + bookSale.daysLeft  + " & " + cardSale.daysLeft)
  println("buyers:    " + bookSale.buyer     + " & " + cardSale.buyer)
  println("open:      " + bookSale.isOpen    + " & " + cardSale.isOpen)
  println("expired:   " + bookSale.isExpired + " & " + cardSale.isExpired)

  // Still more stuff happens. (Or does it?)
  println("\nA DAY PASSES. ATTEMPTS ARE MADE TO BUY EACH OF THE ITEMS.")
  bookSale.advanceOneDay()
  cardSale.advanceOneDay()
  bookSale.buy("E. A. M. Windsor")
  cardSale.buy("E. A. M. Windsor")

  // Report on the current state of the sales.
  println("days left: " + bookSale.daysLeft  + " & " + cardSale.daysLeft)
  println("buyers:    " + bookSale.buyer     + " & " + cardSale.buyer)
  println("open:      " + bookSale.isOpen    + " & " + cardSale.isOpen)
  println("expired:   " + bookSale.isExpired + " & " + cardSale.isExpired)

  // Optional assignment: create a function that prints out the information
  // about the book sale and card sale, so that all the println commands
  // don't have to be repeated.


   */

}