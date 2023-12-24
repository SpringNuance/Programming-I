package o1.gui


import java.awt.image.BufferedImage

import scala.swing.Swing._
import scala.swing._
import scala.swing.event._

import o1.grid.{Grid, GridPos}
import o1.util.ConvenientInt


/** A `BasicGridDisplay` object is a graphics component that displays a rectangular grid
  * as defined by a [[Grid]] object and provides mouse controls for manipulating the grid.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand
  * how this class works or can be used.'''
  *
  * @param grid          the grid to be displayed
  * @param maxSquareSize the maximum width of a single square in the grid, in pixels. The `BasiGridView` object
  *                      scales itself to use most of the available screen space, but won't go over this limit */
private[o1] abstract class BasicGridDisplay[GridType <: Grid[Element], Element](val grid: GridType, maxSquareSize: Int) extends Component {
  display =>

  val popup: Popup

  val width: Int = grid.width

  val height: Int = grid.height

  val squareSize: Int = this.determineSquareSize(maxSquareSize)

  private val cells = this.createCells

  this.border = CompoundBorder(EmptyBorder(10, 10, 10, 10), EtchedBorder)
  this.preferredSize = new Dimension(squareSize * width, squareSize * height)

  this.listenTo(this.mouse.clicks)
  this.listenTo(this.mouse.moves)

  this.reactions += {
    case MouseReleased(_, point, modifiers, _, triggersPopup) =>
      if (triggersPopup) {
        popup.show(display, point.x, point.y)
      } else if ((modifiers & Key.Modifier.Meta) == 0) {
        for (element <- this.elementAt(point)) {
          this.elementClicked(element)
          this.update()
        }
      }
    case MousePressed(_, point, _, _, triggersPopup) =>
      if (triggersPopup) {
        popup.show(display, point.x, point.y)
      }
    case MouseMoved(_, point, _) =>
      this.tooltip = this.elementAt(point).flatMap(this.tooltipFor(_)).orNull
  }


  def elementClicked(element: Element): Unit


  private def determineSquareSize(upperLimit: Int) = {
    val screenSize = this.toolkit.getScreenSize
    val widthMax = (screenSize.width - 175) / this.width
    val heightMax = (screenSize.height - 175) / this.height
    widthMax atMost heightMax atMost upperLimit atLeast 1
  }


  private def createCells: Array[Array[Cell]] = Array.tabulate(height, width)(new Cell(_, _))


  override def paint(graphics: Graphics2D): Unit = {
    for (row <- this.cells; cell <- row) {
      if (graphics.getClip.intersects(cell.bounds)) {
        cell.render(graphics)
      }
    }
  }


  def update(): Unit = {
    for (row <- this.cells; cell <- row) {
      if (cell.updatePics()) {
        this.repaint(cell.bounds.x, cell.bounds.y, cell.bounds.width, cell.bounds.height)
      }
    }
  }


  def elementVisuals(element: Element): Array[BufferedImage] // N.B. null values allowed within return array

  def missingElementVisuals: Array[BufferedImage]            // N.B. null values allowed within return array


  def elementAt(point: Point) = this.posAtPoint(point).map(this.grid(_))


  def posAtPoint(point: Point): Option[GridPos] = {
    val pos = GridPos(point.x / this.squareSize, point.y / this.squareSize)
    if (this.grid.contains(pos)) Some(pos) else None
  }


  def tooltipFor(element: Element): Option[String] = None


  def scale(original: BufferedImage): BufferedImage = this.scale(original, this.squareSize)


  def scale(original: BufferedImage, targetSize: Int): BufferedImage = {

    def nextStep(dim: Int, target: Int) = target.max(dim / 2)

    import java.awt.RenderingHints._
    import java.awt.Transparency._
    import java.awt.image.BufferedImage._

    val width = nextStep(original.getWidth, targetSize)
    val height = nextStep(original.getHeight, targetSize)
    val scaled = new BufferedImage(width, height, if (original.getTransparency == OPAQUE) TYPE_INT_RGB else TYPE_INT_ARGB)
    val tempGraphics = scaled.createGraphics()

    tempGraphics.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC)
    tempGraphics.drawImage(original, 0, 0, width, height, null)
    tempGraphics.dispose()

    if (width == targetSize && height == targetSize) scaled else this.scale(scaled, targetSize)

  }




  private class Cell(row: Int, column: Int) {
    val pos = GridPos(column, row)
    val bounds = new Rectangle(display.squareSize * pos.x, display.squareSize * pos.y, display.squareSize, display.squareSize)
    private var pics = Array[BufferedImage]()

    def element = Option(display.grid.elementAt(pos))

    def updatePics(): Boolean = {
      val newPics = this.element.map(display.elementVisuals).getOrElse(display.missingElementVisuals)
      val hasChanged = !(newPics sameElements this.pics)
      if (hasChanged) {
        this.pics = newPics
      }
      hasChanged
    }

    def render(gridGraphics: Graphics2D): Unit = {
      for (cellPic <- this.pics; if cellPic != null) {
        val cellX = this.bounds.x + ((this.bounds.width - cellPic.width) / 2)
        val cellY = this.bounds.y + ((this.bounds.height - cellPic.getHeight) / 2)
        gridGraphics.drawImage(cellPic, cellX, cellY, null)
      }
    }
  }




  protected class Popup extends PopupMenu {
    protected var chosenGridPos: Option[GridPos] = None

    abstract class PopupAction(name: String) extends MenuItem(name) {
      this.action = Action(name){
        for (c <- chosenGridPos) {
          PopupAction.this.apply(c)
          display.update()
        }
      }
      def isApplicable(pos: GridPos): Boolean
      def apply(pos: GridPos): Unit
    }


    object PopupAction {
      val AlwaysApplicable: Any => Boolean = (a: Any) => true
    }




    class PosAction(name: String, val applies: GridPos => Boolean)(val perform: GridPos => Unit)
        extends PopupAction(name) {

      override def isApplicable(pos: GridPos) = this.applies(pos)

      def apply(pos: GridPos): Unit = {
        this.perform(pos)
      }
    }




    class ElementAction(name: String, val applies: Element => Boolean)(val perform: Element => Unit)
        extends PopupAction(name) {

      override def isApplicable(pos: GridPos) = this.applies(display.grid(pos))

      def apply(pos: GridPos): Unit = {
        this.perform(display.grid(pos))
      }
    }


    def += (menuItem: Component): contents.type = {
      this.contents += menuItem
    }

    def ++= (menuActions: Seq[Component]): contents.type = {
      this.contents ++= menuActions
    }

    def chooseEnabled(): Unit = {
      for (here <- this.chosenGridPos; elem <- this.contents) {
        elem match {
          case popupAction: PopupAction =>
            elem.enabled = popupAction.isApplicable(here)
          case otherwise => // nothing
        }
      }
    }

    override def show(component: Component, x: Int, y: Int): Unit = {
      this.chosenGridPos = posAtPoint(new Point(x, y))
      this.chooseEnabled()
      super.show(component, x, y)
    }

  }


}

