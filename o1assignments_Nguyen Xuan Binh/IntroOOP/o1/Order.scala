package o1

// This class is introduced in Chapter 2.6.

class Order(val number: Int, val orderer: Customer) {

  var totalPrice = 0.0   // gatherer

  def addProduct(pricePerUnit: Double, numberOfUnits: Int) = {
    this.totalPrice = this.totalPrice + pricePerUnit * numberOfUnits
  }
  var isExpress = false

  def description = "order " + this.number + ", ordered by " + this.orderer.description + ", total " + this.totalPrice + " euro"

}