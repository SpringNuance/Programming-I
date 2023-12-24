
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.carsim.gui

// a scala wrapper around the CarSim-relevant functionality of JMapViewer, with some added utility methods
import scala.swing._
import scala.swing.event._
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.Color
import org.openstreetmap.gui.jmapviewer.interfaces._
import org.openstreetmap.gui.jmapviewer._
import MapPanel._


/** The class `MapPanel` is a Scala wrapper around the `JMapViewer` class of the JMapViewer Java library.
  * It provides a zoomable world map.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this class works or can be used.''' */
class MapPanel(val tileSource: TileSource) extends Component with Publisher with TileLoaderListener {

  private val storedTileLoader: TileLoader = null

  override lazy val peer: JMapViewer = {
    val javaPanel = new JMapViewer(new MemoryTileCache(), 8) {
      attribution = new AttributionSupport { // attribution overridden; handled differently
        override def paintAttribution(g: java.awt.Graphics, width: Int, height: Int, topLeft: Coordinate, bottomRight: Coordinate, zoom: Int, observer: java.awt.image.ImageObserver) = { }
      }
    }
    javaPanel.setTileSource(tileSource)
    val mapController = new DefaultMapController(javaPanel)
    mapController.setDoubleClickZoomEnabled(false)
    mapController.setMovementMouseButton(MouseEvent.BUTTON1)
    javaPanel
  }

  def +=(marker: Marker) = {
    this.peer.addMapMarker(marker)
  }

  def ++=(markers: Iterable[Marker]) = {
    markers.foreach( this.peer.addMapMarker )
  }

  def -=(marker: Marker) = {
    this.peer.removeMapMarker(marker)
  }

  def --=(markers: Iterable[Marker]) = {
    markers.foreach( this.peer.removeMapMarker )
  }

  def coordinatesToPixel(latitude: Double, longitude: Double, checkOutside: Boolean) = this.peer.getMapPosition(latitude, longitude, checkOutside)

  def pixelToCoordinates(pixel: Point) = this.peer.getPosition(pixel)

  def tileLoader = this.storedTileLoader
  def tileLoader_=(loader: TileLoader) = {
    this.peer.setTileLoader(loader)
  }

  def recenter(center: Coordinate, zoomLevel: Int) = {
    this.peer.setDisplayPosition(center, zoomLevel)
  }

  def markersVisible = peer.getMapMarkersVisible

  def markersVisible_=(visible: Boolean) = {
    this.peer.setMapMarkerVisible(visible)
  }

  def tileLoadingFinished(tile: Tile, success: Boolean) = {
    peer.tileLoadingFinished(tile, success)
  }

  def getTileCache = peer.getTileCache

}


/** The singleton object `MapPanel` provides a selection of utilities for use
  * with the class of the same name.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how
  * this singleton object works or can be used.''' */
object MapPanel {

  type Marker = MapMarkerCircle

  implicit class NicerMarker(val marker: Marker) {

    def radius = this.marker.getRadius

    def visible = this.marker.isVisible

    def coordinates = new Coordinate(marker.getLat, marker.getLon)

    def coordinates_=(coordinates: Coordinate) = {
      this.marker.setLat(coordinates.getLat)
      this.marker.setLon(coordinates.getLon)
    }

    def visible_=(visibility: Boolean) = {
      this.marker.setVisible(visibility)
    }
  }

}


