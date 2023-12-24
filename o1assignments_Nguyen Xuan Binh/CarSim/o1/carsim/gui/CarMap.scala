
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.carsim.gui

import scala.swing.{MenuItem, Point, Action, PopupMenu, Swing}
import scala.swing.event._
import scala.util._
import scala.util.control.NonFatal
import scala.collection.mutable.Buffer
import CarSim.Directions
import CarSim.Pos
import Directions.Segment
import o1.gui.Dialog._
import o1.util.ConvenientDouble
import scala.concurrent._
import java.awt.Cursor
import java.awt.dnd.DragSource
import org.openstreetmap.gui.jmapviewer._
import org.openstreetmap.gui.jmapviewer.interfaces._
import Ordering.Double.TotalOrdering
import ExecutionContext.Implicits.global
import MapPanel._
import CarMap._


/** A "car map" is the graphical map shown in a CarSim application window. It may contain
  * any number of cars which are drawn onto the map. A car map listens for mouse commands
  * that signal the creation of cars or the execution of car methods.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this class works or can be used.''' */
class CarMap(tileSource: TileSource, center: Pos, zoomLevel: Int) extends MapPanel(tileSource) {
  thisMap =>

  this.tileLoader = Try(new OsmFileCacheTileLoader(this)).getOrElse(new OsmTileLoader(this))
  this.recenter(center, zoomLevel)
  this.markersVisible = true

  // Each car has its own thread, which moves a car marker:
  private val carThreads = Buffer[CarThread]()

  // GUI state:

  private var clickedPoint: Option[Point] = None
  private var clickedCar: Option[CarThread] = None
  private var mostRecentCarThread: Option[CarThread] = None
  private var mouseDragStart: Option[Point] = None
  private var mostRecentRoute = Iterable[RouteMarker]()
  private var highlightRoutesEnabled = false

  // Convenience accessors:

  def mostRecentCar = this.mostRecentCarThread

  def mostRecentCar_=(car: Option[CarThread]): Unit = {
    this.mostRecentCarThread = car
    this.driveHereItem.enabled = this.mostRecentCarThread.isDefined
  }

  def highlightRoutes = this.highlightRoutesEnabled

  def highlightRoutes_=(enabled: Boolean) = {
    this.highlightRoutesEnabled = enabled
    this.displayRouteHighlight(this.mostRecentRoute)
  }


  // Reacting to the mouse:

  this.listenTo(this.mouse.clicks)
  this.listenTo(this.mouse.moves)
  this.reactions += {
    case event: MousePressed =>
      this.stopDragging()
      this.clickedCar = this.carAt(event.point)
      if (this.clickedCar.isDefined && event.isRightClick) {
        this.startDragging(event.point)
      }
    case event: MouseReleased =>
      val releasePoint = event.point
      def isLongDrag(start: Point, end: Point) = math.hypot(end.x - start.x, end.y - start.y) > 7
      if (this.mouseDragStart.exists( isLongDrag(_, releasePoint) )) {
        this.clickedCar.foreach( this.attemptDrive(_, releasePoint) )
        this.stopDragging()
      } else if (event.isRightClick) {
        this.mouseDragStart = None
        this.showPopupMenuAt(releasePoint)
      }
    case MouseMoved(_, point, _) =>
      this.tooltip = this.carAt(point).map( _.marker.toString ).orNull
    case MouseDragged(_, _, _) =>
      if (this.mouseDragStart.isDefined) {
        thisMap.cursor = DragSource.DefaultLinkDrop
      }
  }



  // The menu that pops up when an empty pixel is right-clicked:

  private val driveHereItem = new MenuItem(Action("Drive the most recently used car here") {
    for (point <- clickedPoint; car <- mostRecentCar) {
      this.attemptDrive(car, point)
    }
  })

  this.driveHereItem.enabled = false
  private val emptyPixelMenu = new PopupMenu {
    contents += new MenuItem(Action("Add new car here...") { thisMap.clickedPoint.foreach(createNewCar) })
    contents += driveHereItem
  }

  // The menu that pops up when a car is right-clicked:

  private val carMenu = new PopupMenu {
    contents += new MenuItem(Action("Drive...") {
      for {
        carDriven <- thisMap.clickedCar
        destination <- requestAnyLine("Destination, please: ", RelativeTo(thisMap)).filter( _.nonEmpty )
      } {
        thisMap.attemptDrive(carDriven, destination)
      }
    })
    contents += new MenuItem(Action("Fuel...")  {
      for {
        carFueled <- thisMap.clickedCar
        amountWanted <- requestDouble("Add how many liters of fuel?", _ >= 0, "A non-negative number is required.", RelativeTo(this))
      } {
        thisMap.mostRecentCar = Some(carFueled)
        val gainedFuel = carFueled.fuel(amountWanted)
        if (gainedFuel == 0.0 && amountWanted > 0.0) {
          display("Could not add any fuel.", RelativeTo(thisMap))
        } else if (gainedFuel < amountWanted) {
          display(s"Could only add $gainedFuel liters.", RelativeTo(thisMap))
        }
      }
    })
    contents += new MenuItem(Action("Fill up")  {
      thisMap.clickedCar.foreach( _.fillUp() )
      thisMap.mostRecentCar = thisMap.clickedCar orElse thisMap.mostRecentCar
    })

    listenTo(this)
    reactions += {
      case PopupMenuCanceled(_) => thisMap.clickedCar.foreach( _.unpause() )
    }

  }

  // Creating and adding cars:

  private def createNewCar(pixel: Point) = {
    val location = this.pixelToCoordinates(pixel)
    for {
      consumption <- requestDouble("Enter a fuel consumption rate (liters / 100 km) for the new car:", _ >= 0, "Non-negative number required.", RelativeTo(this))
      tankSize    <- requestDouble("Enter the size of its fuel tank in liters:",                       _ > 0, "Positive number required.", RelativeTo(this))
      fuelInTank  <- requestDouble("Enter amount of fuel in tank, in liters:",                         n => (n >= 0 && n <= tankSize), "A number between zero and the tank size, please.", RelativeTo(this))
    } { this.addCar(new CarEnhancement(consumption, tankSize, fuelInTank, location)) }
  }

  def addCar(car: CarEnhancement) = {
    val carMarker = new CarMarker(this, car)
    this += carMarker
    this += carMarker.destinationMarker
    val carThread = new CarThread(carMarker)
    this.carThreads += carThread
    this.mostRecentCar = Some(carThread)
    carThread.start()
  }

  // Various helper methods for using the cars:

  private def showPopupMenuAt(point: Point) = {
    this.clickedPoint = Some(point)
    this.clickedCar = carAt(point)
    val menu = if (this.clickedCar.isDefined) carMenu else emptyPixelMenu
    for (carThread <- this.clickedCar) {
      carThread.pause()
    }
    menu.show(this, point.x, point.y)
  }


  private def startDragging(start: Point) = {
    this.mouseDragStart = Some(start)
    this.clickedCar.foreach( _.pause() )
  }

  private def stopDragging() = {
    this.cursor = new Cursor(Cursor.DEFAULT_CURSOR)
    this.mouseDragStart = None
    this.clickedCar.foreach( _.unpause() )
  }


  private def carAt(pixel: Point) = {

    def distance(carThread: CarThread) = {
      val marker = carThread.marker
      val location = marker.coordinates
      val carPixel = this.coordinatesToPixel(location.x, location.y, false)
      val closeEnough = (pixel.x - carPixel.x).abs <= marker.radius && (pixel.y - carPixel.y).abs <= marker.radius
      if (closeEnough) pixel.distance(carPixel) else Double.PositiveInfinity
    }


    val closest = this.carThreads.reverse.minByOption(distance)
    closest.filterNot( distance(_) == Double.PositiveInfinity )
  }


  private def attemptDrive(car: CarThread, pixel: Point): Unit = {
    val location: Pos = this.pixelToCoordinates(pixel)
    this.attemptDrive(car, toDirectionsFormat(location))
  }


  private def attemptDrive(carThread: CarThread, destination: String) = {

    def startDriving(route: IndexedSeq[Segment]) = {
      this.mostRecentCar = Some(carThread)
      this.newRouteHighlight(route)
      carThread.startDriving(route)
    }

    Future(carThread.car.findRoute(destination)).onComplete {
      case Success(result)                                     => Swing.onEDT { startDriving(result) }
      case Failure(directionsUnavailable: DirectionsException) => display(directionsUnavailable.message, RelativeTo(this))
      case Failure(NonFatal(otherProblem))                     => otherProblem.printStackTrace()
    }

  }


  def newRouteHighlight(route: IndexedSeq[Segment]): Unit = {

    val MaxNumberOfMarkers = 20000 // cap the number of segments _that are shown_ to avoid certain performance issues (all segments are nevertheless used in routing)
    lazy val numbersTilMax = (0 until MaxNumberOfMarkers)

    def dropEvenlyTillMax(elements: IndexedSeq[Segment]) = {
      val dropRatio = (elements.size.toDouble / MaxNumberOfMarkers) atLeast 1.0
      val includedIndices = numbersTilMax.map( index => (index * dropRatio).toInt )
      val included = Buffer[Segment]()
      for (index <- includedIndices) {
        included += elements(index)
      }
      included
    }

    val filteredRoute = if (route.size > MaxNumberOfMarkers) dropEvenlyTillMax(route) else route
    displayRouteHighlight(filteredRoute.map( segment => new RouteMarker(segment.destination) ))
  }


  private def displayRouteHighlight(route: Iterable[RouteMarker]) = {
    for (marker <- this.mostRecentRoute) {
      this -= marker
    }
    this.mostRecentRoute = route
    if (this.highlightRoutes) {
      this ++= this.mostRecentRoute
    }
    // This is a hack to make car markers appear on top of all other markers:
    val carMarkers = this.carThreads.map( _.marker )
    this --= carMarkers
    this ++= carMarkers
  }



}


private object CarMap {
  import scala.language.implicitConversions

  // conversions between coordinate systems
  implicit def directionsCoordsToMapViewerCoordinate(coords: Directions.Coords): Coordinate = new Coordinate(coords.lat, coords.lng)
  implicit def locationToMapViewerCoordinate        (location: Pos): Coordinate = new Coordinate(location.x, location.y)
  implicit def mapViewerCoordinateToLocation        (coords: Coordinate): Pos   = Pos(coords.getLat, coords.getLon)

  private[carsim] def toDirectionsFormat(pos: Pos) = s"${pos.x},${pos.y}"

  // Map markers:
  import java.awt.Color

  class DestinationMarker(location: Coordinate)
    extends Marker(null, null, location, 8, MapMarker.STYLE.FIXED, new Style(Color.black, new Color(100, 100, 255), null, null))

  class RouteMarker(location: Coordinate)
    extends Marker(null, null, location, 1, MapMarker.STYLE.FIXED, new Style(Color.blue, Color.blue, null, null))


  // Utilities

  class DirectionsException(val message: String) extends Exception(message)

  implicit class MouseEventEnhancement(event: MouseEvent) {
    def isRightClick = this.event.peer.getButton == 3
  }

}

