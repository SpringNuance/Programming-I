package o1.soda

// This class is dicussed in Chapter 3.5.

/** The class `VendingMachine` can be used to simulate simple virtual soda vending
  * machines. A vending machine sells bottles of a single type of soft drink at a
  * certain price per bottle.
  *
  * The methods of the class correspond to the actions of buyers and maintenance
  * personnel on an actual vending machine. For instance, a purchase is made by
  * first inserting some money possibly several times with each one adding to the
  * previous insertion, and then selecting to buy a bottle. This interaction is
  * captured by the methods `insertMoney` and `buyBottle`.
  *
  * Some of the methods are illustrated in the following REPL session, in which
  * the vending machine initially contains 10 bottles with a price of 250 cents
  * (2.5 euros) each:
  *
  * {{{
  *  INPUT: val machine = new VendingMachine(250, 10)
  * OUTPUT: machine: o1.soda.VendingMachine = earned 0.0 euros, inserted 0 cents, 10 bottles left
  *
  *  INPUT: machine.insertMoney(300)
  *
  *  INPUT: "got change: " + machine.sellBottle()
  * OUTPUT: res0: String = got change: 50
  *
  *  INPUT: machine
  * OUTPUT: res1: o1.soda.VendingMachine = earned 2.5 euros, inserted 0 cents, 9 bottles left
  *
  *  INPUT: machine.insertMoney(50)
  *
  *  INPUT: machine.insertMoney(300)
  *
  *  INPUT: machine.sellBottle()
  * OUTPUT: res2: Int = 100
  *
  *  INPUT: machine
  * OUTPUT: res3: o1.soda.VendingMachine = earned 5.0 euros, inserted 0 cents, 8 bottles left
  * }}}
  *
  * As the example illustrates, a vending machine object has a mutable state:
  * it keeps track of how much money the user has inserted, for instance. The
  * machine also tracks the number of bottles it contains, the current (modifiable)
  * price per bottle, and the money it has "earned" by selling bottles.
  *
  * @param bottlePrice  the price of a single bottle
  * @param bottleCount  the initial number of bottles in the machine */
class VendingMachine(var bottlePrice: Int, private var bottleCount: Int) {

  private var earnedCash = 0     // gatherer (which can be reset to zero)
  private var insertedCash = 0   // gatherer (which can be reset to zero)
  private var BuyOrNot: Option[Int] = None

  /** (A maintenance action:) Adds a number of bottles to the machine.
    * The virtual vending machine has no upper limit for bottles.
    * @param newBottles  the number of bottles to be added (a positive integer) */
  def addBottles(newBottles: Int) = {
    this.bottleCount = this.bottleCount + newBottles
  }


  /** (A customer action:) Adds some money towards a forthcoming purchase. */
  def insertMoney(amount: Int) = {
    this.insertedCash = this.insertedCash + amount
  }


  /** Determines whether the machine is out of bottles. */
  def isSoldOut = this.bottleCount == 0


  /** Determines whether enough money has been inserted to purchase a bottle. */
  def enoughMoneyInserted = this.insertedCash >= this.bottlePrice


  /** (A maintenance action:) Removes all the earned cash from the machine.
    * @return the amount of money previously earned by the machine through sales */
  def emptyCashbox() = {
    val got = this.earnedCash   // temporary variable
    this.earnedCash = 0
    got
  }


  /** Produces a textual description of the vending machine's current state. */
  override def toString = {
    val earnings = this.earnedCash / 100.0
    val bottleStatus = if (this.isSoldOut) "SOLD OUT" else s"$bottleCount bottles left"
    s"earned $earnings euros, inserted $insertedCash cents, $bottleStatus"
  }


  /** (A customer action:) Sells a bottle of virtual soda. This can only be done,
    * however, if enough money has been inserted and there are bottles left in
    * the machine. If the sale was successful, the method adds the earnings in
    * the machine's cashbox, removes a single bottle, and sets up for the next
    * transaction by resetting the amount of inserted cash to zero.
    * @return the amount of money (0 or more) given to the buyer as change,
    *         or a negative value to signal an unsuccessful purchase */
  def sellBottle() = {  // alternative implementations for this method will be discussed in later chapters
    if (this.isSoldOut) {
      BuyOrNot = None
      BuyOrNot
    } else if (!this.enoughMoneyInserted) {
      BuyOrNot = None
      BuyOrNot
    } else {
      this.earnedCash = this.earnedCash + this.bottlePrice
      this.bottleCount = this.bottleCount - 1
      val changeGiven = this.insertedCash - this.bottlePrice    // temporary
      this.insertedCash = 0
      BuyOrNot = Some(changeGiven)
      BuyOrNot
    }
  }

}
