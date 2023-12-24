package o1.gui


import scala.swing.GridBagPanel.Anchor._
import scala.swing.{GridBagPanel, _}


/** The package `o1.gui.layout` contains utilities that make it more convenient to lay out components in simple
  * [[https://www.scala-lang.org/api/current/scala-swing/scala/swing/GridBagPanel.html GridBagPanel]]s from
  * Scala's Swing library. These utilities are used internally by some of the given GUIs in O1.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this package works or can be used.''' */
package object layout {

  o1.util.smclInit()

  type Anchor = GridBagPanel.Anchor.Value

  val OneSlot:   (Int, Int) = (1, 1)
  val TwoWide:   (Int, Int) = (2, 1)
  val ThreeWide: (Int, Int) = (3, 1)
  val TwoHigh:   (Int, Int) = (1, 2)
  val ThreeHigh: (Int, Int) = (1, 3)

  type Fill = (GridBagPanel.Fill.Value, Int, Int)

  def NoFill(xWeight: Int, yWeight: Int): Fill   = (GridBagPanel.Fill.None,       xWeight, yWeight)
  def FillVertical(weight: Int): Fill            = (GridBagPanel.Fill.Vertical,         0,  weight)
  def FillHorizontal(weight: Int): Fill          = (GridBagPanel.Fill.Horizontal,  weight,       0)
  def FillBoth(xWeight: Int, yWeight: Int): Fill = (GridBagPanel.Fill.Both,       xWeight, yWeight)

  val Slight: Fill = NoFill(0, 0)

  val NoBorder: (Int, Int, Int, Int) = (0, 0, 0, 0)


  trait EasyLayout extends LayoutContainer {

    def constraintsFor(component: Component, xy: (Int, Int), size: (Int, Int), anchor: Anchor, fill: Fill, border: (Int, Int, Int, Int)): Constraints

    def place(component: Component, xy: (Int, Int), size: (Int, Int), anchor: Anchor, fill: Fill, border: (Int, Int, Int, Int)): Unit = {
      layout += component -> constraintsFor(component, xy, size, anchor, fill, border)
    }

    def placeC(component: Component, xy: (Int, Int), size: (Int, Int), fill: Fill, border: (Int, Int, Int, Int)): Unit = {
      this.place(component, xy, size, Center, fill, border)
    }

    def placeN(component: Component, xy: (Int, Int), size: (Int, Int), fill: Fill, border: (Int, Int, Int, Int)): Unit = {
      this.place(component, xy, size, North, fill, border)
    }

    def placeE(component: Component, xy: (Int, Int), size: (Int, Int), fill: Fill, border: (Int, Int, Int, Int)): Unit = {
      this.place(component, xy, size, East, fill, border)
    }

    def placeS(component: Component, xy: (Int, Int), size: (Int, Int), fill: Fill, border: (Int, Int, Int, Int)): Unit = {
      this.place(component, xy, size, South, fill, border)
    }

    def placeW(component: Component, xy: (Int, Int), size: (Int, Int), fill: Fill, border: (Int, Int, Int, Int)): Unit = {
      this.place(component, xy, size, West, fill, border)
    }

    def placeNW(component: Component, xy: (Int, Int), size: (Int, Int), fill: Fill, border: (Int, Int, Int, Int)): Unit = {
      this.place(component, xy, size, NorthWest, fill, border)
    }

    def placeNE(component: Component, xy: (Int, Int), size: (Int, Int), fill: Fill, border: (Int, Int, Int, Int)): Unit = {
      this.place(component, xy, size, NorthEast, fill, border)
    }

    def placeSW(component: Component, xy: (Int, Int), size: (Int, Int), fill: Fill, border: (Int, Int, Int, Int)): Unit = {
      this.place(component, xy, size, SouthWest, fill, border)
    }

    def placeSE(component: Component, xy: (Int, Int), size: (Int, Int), fill: Fill, border: (Int, Int, Int, Int)): Unit = {
      this.place(component, xy, size, SouthEast, fill, border)
    }
  }

  trait EasyPanel extends GridBagPanel with EasyLayout {

    def constraintsFor(component: Component, xy: (Int, Int), size: (Int, Int), anchor: Anchor, fill: Fill, border: (Int, Int, Int, Int)): Constraints = {
      val insets = new Insets(border._1, border._2, border._3, border._4)
      new Constraints(gridx = xy._1, gridy = xy._2, gridwidth = size._1, gridheight = size._2,
                      weightx = fill._2, weighty = fill._3, anchor.id, fill._1.id, insets, ipadx = 0, ipady = 0)
    }
  }

}
