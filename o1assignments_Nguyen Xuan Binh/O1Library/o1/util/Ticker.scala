
package o1.util

////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

import java.awt.event.ActionEvent
import Ticker._
import javax.swing.{AbstractAction, Action, Timer}


private[o1] object Ticker {

  sealed abstract class TickState

  final case class TickEvery(interval: Int) extends TickState
  case object NotTicking extends TickState


  object TickState {

    def fromDouble(interval: Double): TickState =
      if (interval.isInfinity) NotTicking
      else                     TickEvery((interval atLeast 0 atMost Int.MaxValue).toInt)

    def fromTickRate(rate: Double): TickState = fromDouble(1000 / rate)

  }

}


private[o1] final class Ticker private(additionalInitialDelay: Int, private var tickState: TickState, private val timedAction: Action) {

  def this(additionalInitialDelay: Int, interval: TickState)(timedBlock: => Unit) =
    this(additionalInitialDelay, interval, new AbstractAction {
      def actionPerformed(tick: ActionEvent) = timedBlock
    })

  private[this] val javaTimer = {
    val interval = this.tickState match {
      case TickEvery(interval) => interval
      case NotTicking          => 0
    }
    val timer = new Timer(interval, this.timedAction)
    timer.setRepeats(true)
    timer.setInitialDelay(additionalInitialDelay + interval)
    timer
  }

  def adjust(newState: TickState): Unit = {
    this.javaTimer.setInitialDelay(0)
    val wasStill = this.tickState == NotTicking
    this.tickState = newState
    newState match {
      case TickEvery(interval) =>
        this.javaTimer.setDelay(interval)
        if (wasStill) {
          this.start()
        }
      case NotTicking =>
        this.stop()
    }
  }

  def start(): Unit = {
    if (this.tickState.isInstanceOf[TickEvery]) {
      this.javaTimer.start()
    }
  }

  def stop(): Unit = {
    this.javaTimer.stop()
  }

  def isRunning: Boolean = this.javaTimer.isRunning
}


