package o1

// This class is introduced in Chapter 2.6.

class Customer(val name: String, val customerNumber: Int, val email: String, val address: String) {

  def description = "#" + this.customerNumber + " " + this.name + " <" + this.email + ">"

}

