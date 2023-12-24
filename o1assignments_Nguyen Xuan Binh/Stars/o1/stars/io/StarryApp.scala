

package o1.stars.io

import o1.{Pos,View}
import o1.stars.{StarMap,StarCoords}
import o1.gui.EverpresentTooltips


/** Launches a GUI that presents the user with a map of the night sky.
  *
  * The app uses [[SkyFiles]] to read the star map from a local folder.
  * It then uses [[SkyPic]] to turn that data into a [[o1.Pic Pic]]. */
object StarryApp extends App {

  val starDataFolder = "northern" // You can use either "northern" or "test" here.

  val dataFolderPath = s"o1/stars/$starDataFolder/"

  SkyFiles.readStarMap(dataFolderPath) match {
    case Some(starMap) =>
      this.showInGUI(starMap)
    case None =>
      println(s"Failed to access star data in folder $dataFolderPath.")
  }

  private def showInGUI(sky: StarMap) = {
    val Size = 650
    val starView = new View(sky, tickRate=0, title="Stars") with EverpresentTooltips {

      val starrySky = SkyPic.create(sky, Size)

      def makePic = this.starrySky

      override def onMouseMove(mousePosition: Pos) = {
        val mouseCoord = StarCoords.fromPercentages(mousePosition.x / Size, mousePosition.y / Size)
        val nearbyNames = sky.constellations.filter( _.isNearish(mouseCoord) ).map( _.name )
        this.tooltip = s"$mouseCoord ${nearbyNames.mkString(", ")}"
      }
    }

    starView.start()
  }

}
