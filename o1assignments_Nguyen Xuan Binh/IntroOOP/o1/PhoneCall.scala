package o1

// This class is introduced in Chapter 2.3.

class PhoneCall(initialFeeExcludingNetwork: Double, pricePerMinuteExcludingNetwork: Double, val duration: Double) {

  val NetworkFeeInitially = 0.0099
  val NetworkFeePerMinute = 0.0199
  val initialFee = initialFeeExcludingNetwork + NetworkFeeInitially
  val pricePerMinute = pricePerMinuteExcludingNetwork + NetworkFeePerMinute

  def totalPrice = this.initialFee + this.pricePerMinute * this.duration

  def description =
    "%.2f min, %.5fe (%.5fe + %.5fe/min)".format(this.duration, this.totalPrice, this.initialFee, this.pricePerMinute)
}



