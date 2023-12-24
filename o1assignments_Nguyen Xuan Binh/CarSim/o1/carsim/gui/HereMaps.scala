////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.carsim.gui

import org.openstreetmap.gui.jmapviewer.tilesources.AbstractOsmTileSource
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource.TileUpdate

import scala.util._
import requests.RequestFailedException
import ujson.{Value => Json}
import upickle.default.{ReadWriter, macroRW, read}

import scala.collection.immutable.ArraySeq


/** The singleton object `HereMaps` provides an interface to the Here.com routing and map service over the internet.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this object works or can be used.'''
  *
  * (This class is not really a part of the GUI ''per se''. It is here as an artifact of the programming assigment.) */
object HereMaps { // See: https://developer.here.com/documentation

  def findRoute(origin: String, destination: String): Seq[Segment] = {

    def scrapeRouteSegments(directionsJson: Json): Seq[Segment] = {
      val firstAndOnlyStep = read[Step](directionsJson("response")("route").arr(0))
      firstAndOnlyStep.segments
    }

    val originCoords          = parseCoords(origin)      getOrElse error("Invalid origin of travel: " + origin)
    val destinationCoords     = parseCoords(destination) getOrElse coordsOfNamedPlace(destination, maybeNear=originCoords)
    val searchParameters      = Map("app_id"         -> APIKeys.AppId,         "app_code" -> APIKeys.AppCode,
                                    "representation" -> "overview",            "routeattributes" -> "shape",
                                    "mode"           -> "fastest;car;traffic:disabled",
                                    "waypoint0"      -> s"$originCoords",      "waypoint1" -> s"$destinationCoords")
    val searchURL: String     = "https://route.api.here.com/routing/7.2/calculateroute.json"
    val responseBody: String  = Try(requests.get(searchURL, params = searchParameters)) match {
      case Success(response)                       => attempt(response.text, "No routa data received.")
      case Failure(failed: RequestFailedException) => error("HERE Maps failed to provide a route to that location.")
      case Failure(anyOtherProblem)                => error("Failed to access HERE Maps route search. Please check your network connection.")
    }
    val directionsJson        = attempt(ujson.read(responseBody), "Received an invalid JSON response from the HERE Maps route service.")
    attempt(scrapeRouteSegments(directionsJson), "Failed to get the route. Received an invalid JSON response from HERE Maps.")
  }


  case class Coords(val lat: Double, val lng: Double) {
    override def toString = s"geo!${lat},${lng}"
  }

  case class Segment(val distance: Double, val origin: Coords, val destination: Coords)

  case class Step(val shape: ArraySeq[String], val summary: Json) {
    lazy val segments: ArraySeq[Segment] = {
      val totalDistance  = this.summary("distance").num.toInt
      def segmentWeight(a: Coords, b: Coords) = math.hypot(b.lat - a.lat, b.lng - a.lng)
      val points         = shape.map(x => convertToCoords(x))
      val weightsAndEnds = for ((from, to) <- points zip points.tail) yield (segmentWeight(from, to), from, to)
      val sumOfWeights   = weightsAndEnds.foldLeft(0.0)( _ + _._1 )
      def estimatedLength(weight: Double) = weight / sumOfWeights * totalDistance // the length in meters for each polyline segment is not provided, so we use this simple metric that is good enough for current purposes
      for ((dist, from, to) <- weightsAndEnds) yield Segment(estimatedLength(dist), from, to)
    }

    private def convertToCoords(coords: String): Coords = {
      val parts = coords.split(",").map( _.toDouble )
      new Coords(parts(0), parts(1))
    }
  }

  def coordsOfNamedPlace(placeName: String, maybeNear: Coords): Coords = {
    val searchParameters   = Map("app_id"     -> APIKeys.AppId, "app_code"   -> APIKeys.AppCode,
                                 "searchtext" -> placeName,     "maxresults" -> "1",
                                 "prox"       -> s"${maybeNear.lat},${maybeNear.lng},200000")
    val searchURL    = "https://geocoder.api.here.com/6.2/geocode.json"
    val response     = attempt(requests.get(searchURL, params = searchParameters), "Failed to access HERE Maps coordinate search. Please make sure your network connection is working.")
    val locationJson = attempt(ujson.read(response.text), "Received an invalid JSON response from HERE Maps coordinate search.")
    println(locationJson.render(indent=4))
    val coordsJson   = attempt(locationJson("Response")("View")(0)("Result")(0)("Location")("NavigationPosition")(0), s"No coordinates available for '$placeName'.")
    Coords(coordsJson("Latitude").num, coordsJson("Longitude").num)
  }

  // read-writers for converting between json and case classes:
  object Coords  { implicit val rw: ReadWriter[Coords]  = macroRW }
  object Segment { implicit val rw: ReadWriter[Segment] = macroRW }
  object Step    { implicit val rw: ReadWriter[Step]    = macroRW }



  private object APIKeys { // Please do not use these API keys in other projects. You can create your own for free at Here.com if you want.
    val AppId  = "683xUCoL3LPk49DlRwIM"
    val AppCode = "09umXyLDjeevTBdp_bm7ow"
  }

  private def error(message: String) = throw new CarMap.DirectionsException(message)
  private def attempt[Result](action: =>Result, message: String) = Try(action) getOrElse error(message)

  private val HttpOK = 200
  private val LatCommaLong = {
    val Comma = """\s*,\s*"""
    val Coord = """((?:-)?\d+(?:\.\d+)?)"""
    (Coord + Comma + Coord).r
  }
  private def parseCoords(coordsString: String): Option[Coords] =
    coordsString match {
      case LatCommaLong(lat, lng) => Try(Coords(lat.toDouble, lng.toDouble)).toOption
      case _                      => None
    }



  class TileSource extends AbstractOsmTileSource("HERE", "") {

    val servers = Array("1", "2", "3", "4")
    var server = 0

    override def getBaseUrl: String = {
      server = (server + 1) % servers.length
      s"https://${servers(server)}.base.maps.api.here.com/maptile/2.1/maptile/newest/normal.day"
    }

    override def getTileUrl(zoom: Int, tilex: Int, tiley: Int): String = {
      this.getBaseUrl() + s"/${zoom}/${tilex}/${tiley}/256/png8?app_id=${APIKeys.AppId}&app_code=${APIKeys.AppCode}"
    }

    override def getTileUpdate: TileUpdate = {
      TileUpdate.IfNoneMatch
    }

    override def getMaxZoom = 16

  }


}