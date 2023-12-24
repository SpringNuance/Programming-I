
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.carsim.gui

import java.util.Locale
import java.awt.Color
import scala.util._
import org.openstreetmap.gui.jmapviewer._
import org.openstreetmap.gui.jmapviewer.interfaces._
import MapPanel._
import CarMap._
import CarSim.Directions.Segment


/** The class `CarMarker` represents the markers used in CarSim to represent cars graphically.
  * These markers are linked with a destination marker and can move about, changing color as
  * the car consumes fuel.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this class works or can be used.''' */
class CarMarker(val map: CarMap, val car: CarEnhancement) extends Marker(null, null, car.location, 5, MapMarker.STYLE.FIXED, CarMarker.fuelStyle(car)) {

  val destinationMarker: Marker = new DestinationMarker(car.location)
  this.destinationMarker.visible = false
  this.refresh()

  def advanceAlongRoute() = {
    val distanceTraveled = this.car.advance()
    this.coordinates = this.car.location
    if (this.car.isAtDestination) {
      this.destinationMarker.visible = false
    }
    this.refresh()
    distanceTraveled
  }

  def updateDestination(segments: Iterable[Segment]) = {
    if (segments.nonEmpty) {
      val newDestination = segments.last.destination
      this.destinationMarker.coordinates = directionsCoordsToMapViewerCoordinate(newDestination)
      this.destinationMarker.visible = true
    }
  }

  def refresh() = {
    this.setStyle(CarMarker.fuelStyle(car))
    this.map.repaint()
  }

  override def toString = {
    "Car at (%.3f,%.3f). Fuel at %.1f%%. Driven: %.1f km".formatLocal(Locale.US, this.coordinates.x, this.coordinates.y, this.car.fuelRatio.abs, this.car.kilometersDriven)
  }

  private def coordinates: Coordinate = this.car.location

}

private object CarMarker {

  def fuelColor(car: CarEnhancement) = Color.getHSBColor(car.fuelRatio.toInt * 0.0033f, 1.0f, 1.0f)

  def fuelStyle(car: CarEnhancement) = new Style(Color.black, fuelColor(car), null, null)

}
