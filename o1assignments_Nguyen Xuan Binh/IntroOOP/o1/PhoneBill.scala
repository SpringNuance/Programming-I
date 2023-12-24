package o1

// This class is introduced in Chapter 2.3.

class PhoneBill(var customerName: String) {

  private var callsMade = List[PhoneCall]()

  def addCall(newCall: PhoneCall) = {
    this.callsMade = newCall :: callsMade
  }

  def totalPrice = this.callsMade.map( _.totalPrice ).sum

  def breakdown = "INVOICE --- " + this.customerName + ":" + this.callsMade.map( "\n - " + _.description ).mkString("")

}
import o1.PhoneCall
