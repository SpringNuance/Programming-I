package o1.swingtest

import scala.swing._

// This program is introduced in Chapter 12.3 of the ebook.

object ComponentTestApp extends SimpleSwingApplication {

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

  // A method that returns the main window of the application:

  def top = this.buttonWindow

}

