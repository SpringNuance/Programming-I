
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.carsim.gui

import scala.swing._
import scala.swing.event._
import java.awt.Font
import java.awt.font.TextAttribute
import java.awt.Desktop
import java.net.URI
import FlowPanel._
import BorderPanel.Position._
import java.awt.Cursor
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource
import o1.gui._
import CarSim.Pos


/** The singleton object `CarSim` represents the CarSim application, which allows its user
  * to simulate simple car behavior (movement, fueling). The object serves as an entry
  * point for the application, and can be run  to start up the user interface.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this object works
  * or can be used, apart from the fact that you can use this file to start the program.''' */
object CarSim extends SimpleSwingApplication with O1AppDefaults with EverpresentTooltips {


  def top = new MainFrame {
    val map = new CarMap(new HereMaps.TileSource(), Pos(60.186727, 24.822589), 5)
    map.addCar(new CarEnhancement(4.0, 70.0, 15.0, Pos(60.186727, 24.822589)))
    this.title = "CarSim"
    this.location = new Point(100, 100)

    this.contents = new BorderPanel {

      val bottomRow = new BorderPanel {
        val attributions = new FlowPanel(FlowPanel.Alignment.Right)(new Attribution("Maps and routes by HERE.com.", "https://www.here.com/en"),
                                                                    new Attribution("Uses JMapViewer.", "http://svn.openstreetmap.org/applications/viewer/jmapviewer/"))
        val instructions = new Label("   Drag to scroll, wheel to zoom. Right-click to add cars. Right-click or right-drag a car to use it.")
        layout(instructions) = West
        layout(attributions) = East
      }

      layout(map) = Center
      layout(bottomRow) = South

      preferredSize = new Dimension(1200, 600)
    }

    this.menuBar = new MenuBar {
      contents += new Menu("Program") {
        contents += new MenuItem(Action("Quit") { dispose() })
      }
      contents += new Menu("Settings") {
        contents += new CheckMenuItem("Highlight latest route") {
          this.listenTo(this)
          reactions += {
            case ButtonClicked(_) =>
              map.highlightRoutes = this.peer.getState
          }
        }
      }
    }
  }


  private class Attribution(text: String, linkText: String) extends Label(text) {
    this.font = AttributionFont
    val link = new URI(linkText)

    this.listenTo(this.mouse.moves)
    this.listenTo(this.mouse.clicks)

    for (desktop <- if (Desktop.isDesktopSupported) Some(Desktop.getDesktop) else None) {
      this.reactions += {
        case MouseEntered(_, _, _) =>
          this.font = AttributionLinkFont
          this.cursor = new Cursor(Cursor.HAND_CURSOR)
        case MouseExited(_, _, _) =>
          this.font = AttributionFont
          this.cursor = new Cursor(Cursor.DEFAULT_CURSOR)
        case MouseClicked(_, _, _, _, _) =>
          desktop.browse(this.link)
      }
    }

  }

  private val AttributionFont = new Font("Arial", Font.PLAIN, 10)
  private val AttributionLinkFont = {
    val props = new java.util.HashMap[TextAttribute, Integer]
    props.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON)
    AttributionFont.deriveFont(props)
  }

  val Directions = HereMaps
  type Pos = o1.world.Pos
  val  Pos = o1.world.Pos

}
