package o1.swingtest

import scala.swing._
import scala.swing.event._

// This program is introduced in Chapter 12.3 of the ebook.

object EventTestApp extends SimpleSwingApplication {

  // Main components:

  val firstButton = new Button("Press me, please")
  val secondButton = new Button("No, press ME!")
  val prompt = new Label("Press one of the buttons.")

  // Component layout within the window:

  val allPartsTogether = new BoxPanel(Orientation.Vertical)
  allPartsTogether.contents += prompt
  allPartsTogether.contents += firstButton
  allPartsTogether.contents += secondButton

  val buttonWindow = new MainFrame
  buttonWindow.contents = allPartsTogether
  buttonWindow.title = "Swing Test App"

  // Event handling:

  this.listenTo(firstButton)
  this.listenTo(secondButton)
  this.reactions += {
    case clickEvent: ButtonClicked =>
      Dialog.showMessage(allPartsTogether, "You pressed the button that says: " + clickEvent.source.text, "Info")
  }

  // A method that returns the main window of the application:

  def top = this.buttonWindow

}

