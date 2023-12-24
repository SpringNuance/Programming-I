
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////


package o1.carsim.gui

import o1.carsim._
import o1.util.ifImplemented
import CarEnhancement._
import CarSim.Directions
import CarSim.Pos
import CarMap.toDirectionsFormat
import Directions.Segment


/** The class `CarEnhancement` represents cars using the student-created `Car` class to do it.
  * Its functionality contains some things we didn't want to put in the car exercise.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this class works or can be used.'''
  *
  * (This class is not really a part of the GUI ''per se'', but is here as an artifact of the programming assigment.) */
class CarEnhancement(consumption: Double, tankSize: Double, fuelInTank: Double, initialLocation: Pos) {

  private val car = new Car(consumption, tankSize, fuelInTank, initialLocation) {
    override def fuelRatio: Double =    ifImplemented { if (hasFiniteFuel) super.fuelRatio else 100.0                   } getOrElse 0.0
    override def fuelRange: Double =    ifImplemented { if (hasFiniteFuel) super.fuelRange else Double.PositiveInfinity } getOrElse 0.0
    override def location: Pos =        ifImplemented { super.location                                                  } getOrElse initialLocation
    override def metersDriven: Double = ifImplemented { super.metersDriven                                              } getOrElse 0.0
  }

  private var itinerary = Iterator[Segment]()

  private var reachedDestination = false

  def isAtDestination = this.reachedDestination

  def location = this.car.location

  def isOutOfFuel = this.hasFiniteFuel && this.car.fuelRatio < 0.0000000000000000000001

  def findRoute(destination: String) = {
    this.reachedDestination = false
    val segments = Directions.findRoute(toDirectionsFormat(this.car.location), destination).toIndexedSeq
    this.itinerary = segments.iterator
    segments
  }

  def fuel(amount: Double) = {
    this.car.fuel(amount)
  }

  def fillUp() = {
    this.car.fuel()
  }

  def advance() = {
    val currentSegment = this.itinerary.next()
    this.car.drive(currentSegment.destination, currentSegment.distance)
    if (this.itinerary.isEmpty) {
      this.reachedDestination = true
    }
    currentSegment.distance
  }

  def isIdle = this.itinerary.isEmpty || this.isOutOfFuel

  private def hasFiniteFuel: Boolean = this.car.fuelConsumption > 0

  def fuelRatio = this.car.fuelRatio

  def kilometersDriven = this.car.metersDriven / 1000


}


private object CarEnhancement {
  import scala.language.implicitConversions

  implicit def directionsServiceCoordsToLocation(coords: Directions.Coords): Pos = Pos(coords.lat, coords.lng)

}