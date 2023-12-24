package o1.llama

import scala.swing._
import scala.swing.event._
import javax.swing.ImageIcon

// This program is introduced in Chapter 12.3 of the ebook.

object LlamaApp extends SimpleSwingApplication {

  // A couple of images:
  val alivePic = new ImageIcon(this.getClass.getResource("pics/alive.jpg"))
  val deadPic  = new ImageIcon(this.getClass.getResource("pics/dead.jpg"))

  // Access to the model (internal logic of the app):
  var targetLlama = new Llama

  // Components:
  val commentary = new Label
  val pictureLabel = new Label
  val startOverButton = new Button("Again!")
  this.updateLlamaView()

  // Events:
  this.listenTo(startOverButton)
  this.listenTo(pictureLabel.mouse.clicks)
  this.listenTo(pictureLabel.mouse.wheel)

  this.reactions += {
    case wheelEvent: MouseWheelMoved =>
      targetLlama.scratch()
      updateLlamaView()

    case clickEvent: MouseClicked =>
      if (clickEvent.clicks > 1) {  // double-click (or triple, etc.)
        targetLlama.slap()
      } else {
        targetLlama.poke()
      }
      updateLlamaView()

    case clickEvent: ButtonClicked =>
      targetLlama = new Llama
      updateLlamaView()
  }

  private def updateLlamaView() = {
    this.commentary.text = this.targetLlama.stateOfMind
    this.pictureLabel.icon = if (this.targetLlama.isOutOfPatience) this.deadPic else this.alivePic
  }


  // Layout:
  val verticalPanel = new BoxPanel(Orientation.Vertical)
  verticalPanel.contents += commentary
  verticalPanel.contents += pictureLabel
  verticalPanel.contents += startOverButton

  val llamaWindow = new MainFrame
  llamaWindow.title = "A Llama"
  llamaWindow.resizable = false
  llamaWindow.contents = verticalPanel

  def top = this.llamaWindow


}

