////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.carsim.gui

import scala.util._
import CarSim.Directions.Segment

/** The class `CarThread` represents threads of execution each of which is responsible
  * for controlling a single `Car` object and the `CarMarker` that represents it visually.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this class works or can be used.''' */
class CarThread(val marker: CarMarker) extends Thread {

  private var pauseRequested = false
  val car = this.marker.car
  private var markerSpeed = 0.0

  override def run() = {
    while (true) {
      this.car.synchronized {
        while (this.car.isIdle || this.pauseRequested) {
          this.car.wait()
        }
      }
      val distanceTraveled = this.marker.advanceAlongRoute()
      Thread.sleep((distanceTraveled * this.markerSpeed).toInt)
    }
  }

  def startDriving(segments: Iterable[Segment]) = {

    this.car.synchronized {
      this.markerSpeed = determineSpeed(segments)
      this.marker.updateDestination(segments)
      this.unpause()
    }

    def determineSpeed(segments: Iterable[Segment]) = {
      if (segments.nonEmpty) {
        val distance = segments.map( _.distance ).sum
        val animationDuration = (distance * 0.05).max(4000).min(240000)
        animationDuration / distance
      } else {
        0.0
      }
    }

  }

  def fuel(wantedAmount: Double) = {
    this.car.synchronized {
      val actualAmount = this.car.fuel(wantedAmount)
      this.unpause()
      actualAmount
    }
  }

  def fillUp() = {
    this.car.synchronized {
      this.car.fillUp()
      this.unpause()
    }
  }

  def pause() = {
    this.car.synchronized {
      this.pauseRequested = true
    }
  }

  def unpause() = {
    this.car.synchronized {
      this.marker.refresh()
      this.pauseRequested = false
      this.car.notify()
    }
  }

}
