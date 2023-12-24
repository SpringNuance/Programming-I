package o1.randomtext

import scala.swing._
import scala.swing.event._

/** The singleton object `RandomTextApp` represents the Random Text application.
  * The object serves as a possible entry point for the game, and can be run to
  * start up a graphical user interface.
  *
  * The application provides end user access to the functionality provided by the
  * class [[o1.randomtext.RandomTextGenerator]]. There is a text field for providing
  * a data source for random text generation, and a text area for displaying the
  * text produced. A button is available that the user can click to indicate that
  * they wish to generate a new batch of nonsense. */
object RandomTextApp extends SimpleSwingApplication {

  // Components:

  val prompt = new Label("Source file or URL:")
  val sourceField = new TextField("alice.txt", 50)
  val randomizeButton = new Button("Randomize!")

  val outputArea = new TextArea("Random stuff will appear here.", 30, 85)
  outputArea.editable = false
  outputArea.lineWrap = true

  // Layout:

  val topRow = new FlowPanel
  topRow.contents += prompt
  topRow.contents += sourceField
  topRow.contents += randomizeButton

  val wholeLayout = new BoxPanel(Orientation.Vertical)
  wholeLayout.contents += topRow
  wholeLayout.contents += outputArea

  val window = new MainFrame
  window.title = "Random Text Generator"
  window.resizable = false
  window.contents = wholeLayout

  def top = this.window

}


