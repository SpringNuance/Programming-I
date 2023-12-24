package o1.money


class Currency(val coinValues: Vector[Int]) {

  def numberOfCoinTypes = this.coinValues.size

  def waysToSplit(sum: Int) = ??? // TODO: implement this as an optional exercise; see Chapter 12.1




  override def toString = this.coinValues.mkString(",")

}


object Currency {

  val EUR = new Currency(Vector(1, 2, 5, 10, 20, 50, 100, 200))
  val USD = new Currency(Vector(1, 5, 10, 25, 50))

}