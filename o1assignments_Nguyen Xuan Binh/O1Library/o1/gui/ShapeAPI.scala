package o1.gui


import smcl.modeling.d2.{Pos => SMCLPos}
import smcl.pictures.{Circle => SMCLCircle, Ellipse => SMCLEllipse, Line => SMCLLine, Rectangle => SMCLRectangle, StarPentagon => SMCLStarPentagon, Triangle => SMCLTriangle}

import o1.gui.PicHistory._
import o1.util.ConvenientDouble
import Anchor._


private[gui] trait ShapeAPI {
  private val StarCuspRadiusFactor: Double = 0.201
  private def creationOp(method: String, shapeName: String) = PicHistory(op.Create(method = method, simpleDescription = shapeName + "-shape"))
  private def creationOp(method: String): PicHistory = creationOp(method, method)

  /** Creates a new [[Pic]] that portrays a filled rectangle.
    * @param width   the width of the rectangle and the [[Pic]]
    * @param height  the height of the rectangle and the [[Pic]]
    * @param color   the color of the rectangle and thus the only color visible in the [[Pic]]
    * @param anchor  an anchor for the new [[Pic]]
    * @return a [[Pic]] of the rectangle (a vector graphic) */
  def rectangle(width: Double, height: Double, color: Color, anchor: Anchor = Center): Pic = {
    val smclContent = SMCLRectangle(
        baseLengthInPixels = width atLeast 0,
        heightInPixels = height atLeast 0,
        hasBorder = false,
        hasFilling = true,
        color = color.smclColor,
        fillColor = color.smclColor)
    Pic(smclContent, anchor, creationOp("rectangle"))
  }


  /** Creates a new [[Pic]] that portrays a filled rectangle. Sets its [[Anchor]] at [[TopLeft]].
    * @param bounds  the width and height of the rectangle and the [[Pic]]
    * @param color   the color of the rectangle and thus the only color visible in the [[Pic]]
    * @return a [[Pic]] of the rectangle (a vector graphic) */
  def rectangle(bounds: Bounds, color: Color): Pic = {
    val smclContent = SMCLRectangle(baseLengthInPixels = bounds.width atLeast 0,
                                    heightInPixels = bounds.height atLeast 0, hasBorder = false,
                                    hasFilling = true, color = color.smclColor, fillColor = color.smclColor)
    Pic(smclContent, TopLeft, creationOp("rectangle"))
  }


  /** Creates a new [[Pic]] that portrays a filled square.
    * @param side    the width and height of the square and the [[Pic]]
    * @param color   the color of the square and thus the only color visible in the [[Pic]]
    * @param anchor  an anchor for the new [[Pic]]
    * @return a [[Pic]] of the square (a vector graphic) */
  def square(side: Double, color: Color, anchor: Anchor = Center): Pic = {
    val smclContent = SMCLRectangle(sideLengthInPixels = side atLeast 0, hasBorder = false, hasFilling = true,
                                    color = color.smclColor, fillColor = color.smclColor)
    Pic(smclContent, anchor, creationOp("square"))
  }


  /** Creates a new [[Pic]] that portrays a filled rectangle and sets its [[Anchor]] at [[TopLeft]].
    * @param width   the width of the rectangle and the [[Pic]]
    * @param height  the height of the rectangle and the [[Pic]]
    * @param color   the color of the rectangle and thus the only color visible in the [[Pic]]
    * @return a [[Pic]] of the rectangle (a vector graphic) */
  def emptyCanvas(width: Double, height: Double, color: Color = colors.White): Pic = {
    val smclContent = SMCLRectangle(baseLengthInPixels = width atLeast 0, heightInPixels = height atLeast 0,
                                    hasBorder = false, hasFilling = true, color = color.smclColor, fillColor = color.smclColor
                                   ).moveUpperLeftCornerTo(SMCLPos.Origo).toPicture.setViewportToContentBoundary(None)
    Pic(smclContent, TopLeft, creationOp("emptyCanvas", "rectangle"))
  }


  /** Creates a new [[Pic]] that portrays a filled circle. The background is fully transparent.
    * @param diameter  the diameter of the circle, which also sets the width and height of the [[Pic]]
    * @param color     the color of the circle
    * @param anchor    an anchor for the new [[Pic]]
    * @return a [[Pic]] of the circle (a vector graphic) */
  def circle(diameter: Double, color: Color, anchor: Anchor = Center): Pic = {
    val smclContent = SMCLCircle(center = SMCLPos.Origo, radiusInPixels = (diameter / 2.0) atLeast 0, hasBorder = false,
                                 hasFilling = true, color = color.smclColor, fillColor = color.smclColor)
    Pic(smclContent, anchor, creationOp("circle"))
  }


  /** Creates a new [[Pic]] that portrays an ellipse. The background is fully transparent.
    * @param width     the width of the ellipse and the [[Pic]]
    * @param height    the height of the ellipse and the [[Pic]]
    * @param color     the color of the ellipse
    * @param anchor    an anchor for the new [[Pic]]
    * @return a [[Pic]] of the ellipse (a vector graphic) */
  def ellipse(width: Double, height: Double, color: Color, anchor: Anchor = Center): Pic = {
    val smclContent = SMCLEllipse(center = SMCLPos.Origo, semiMajorAxisInPixels = (width / 2.0) atLeast 0,
                                  semiMinorAxisInPixels = (height / 2.0) atLeast 0, hasBorder = false,
                                  hasFilling = true, color = color.smclColor, fillColor = color.smclColor)
    Pic(smclContent, anchor, creationOp("ellipse"))
  }


  /** Creates a new [[Pic]] that portrays an isosceles triangle. The base of the triangle is
    * at the bottom of the image and the apex is at the top center. The background is fully
    * transparent.
    * @param width     the width of the triangle’s base, which determines the width of the [[Pic]], too
    * @param height    the height of the triangle, which determines the height of the [[Pic]], too
    * @param color     the color of the triangle
    * @param anchor    an anchor for the new [[Pic]]
    * @return a [[Pic]] of the triangle (a vector graphic) */
  def triangle(width: Double, height: Double, color: Color, anchor: Anchor = Center): Pic = {
    val smclContent = SMCLTriangle.basedOnHeightAndBase(height = height atLeast 0, baseLength = width atLeast 0, hasBorder = false,
                                                        hasFilling = true, color = color.smclColor, fillColor = color.smclColor)
    Pic(smclContent, anchor, creationOp("triangle"))
  }


  /** Creates a new [[Pic]] that portrays a five-pointed star. The background is fully transparent.
    * @param width   the width of the star, which determines the dimensions of the [[Pic]]
    * @param color   the color of the star
    * @param anchor  an anchor for the new [[Pic]]
    * @return a [[Pic]] of the star (a vector graphic) */
  def star(width: Double, color: Color, anchor: Anchor = Center): Pic = {
    val smclContent = SMCLStarPentagon(center = SMCLPos.Origo, widthInPixels = width atLeast 0, heightInPixels = width atLeast 0,
                                       cuspRadiusInPixels = (StarCuspRadiusFactor * width) atLeast 0, hasBorder = false,
                                       hasFilling = true, color = color.smclColor, fillColor = color.smclColor)
    Pic(smclContent, anchor, creationOp("star"))
  }


  /** Creates a new [[Pic]] that portrays a thin line. The line is specified in terms of two [[Pos]]
    * objects: imagine drawing a line between the two points on a plane and then cropping the plane to
    * just the part that contains the line.
    *
    * The line always runs from one corner of the resulting [[Pic]] to another. The [[Anchor]] of the
    * [[Pic]] is at one of the four corners: the one that’s closest to `from`.
    *
    * The background is fully transparent.
    *
    * @param from      the “starting point” of the line; the [[Pic]] will anchor at the corresponding corner
    * @param to        the “end point” of the line
    * @param color     the color of the line
    * @return a [[Pic]] of the line (a vector graphic) */
  def line(from: Pos, to: Pos, color: Color): Pic = {
    val anchor = (from.x < to.x, from.y < to.y) match {
      case (true, true)   => TopLeft
      case (true, false)  => BottomLeft
      case (false, true)  => TopRight
      case (false, false) => BottomRight
    }
    val smclContent = SMCLLine(startX = from.x, startY = from.y, endX = to.x, endY = to.y, color = color.smclColor)
    Pic(smclContent, anchor, creationOp("line"))
  }

}
