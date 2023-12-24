package o1.util

////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

private[o1] class DiscardingThrottler {

  private[this] var isIdle = true

  def ifIdle[Result](possiblyHeavyComputation: => Result) = {
    if (this.isIdle) {
      this.isIdle = false
      val worker = Future(possiblyHeavyComputation)
      worker.onComplete { _ => this.isIdle = true }
      worker
    } else {
      Future.failed(DiscardingThrottler.Busy)
    }
  }
}


private[o1] object DiscardingThrottler {
  object Busy extends RuntimeException("discarding throttler busy; ignoring work request")
}

