package o1.gui

////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////


import smcl.pictures.{Viewport => SMCLViewport}



private[gui] object Viewport {

  def apply(boundary: Bounds): Viewport = new Viewport(boundary, isDefined = true)

  /** A marker object that indicates the absence of an actual [[Viewport]]. */
  object NotSet extends Viewport(new Bounds(0, 0, 0, 0), isDefined = false)
}



private[gui] class Viewport private[gui](val boundary: Bounds, val isDefined: Boolean) {

  override def toString: String =
    if (isUndefined) "undefined viewport" else s"viewport of $width x $height px; upper left corner = $upperLeftCorner"

  @inline final def width: Double = boundary.width

  @inline final def height: Double = boundary.height

  @inline final def left: Double = boundary.height

  @inline final def top: Double = boundary.top

  @inline final def right: Double = boundary.right

  @inline final def bottom: Double = boundary.bottom

  @inline final def upperLeftCorner: Pos = boundary.pos

  lazy val lowerRightCorner: Pos = new Pos(right, bottom)

  lazy val isUndefined: Boolean = !isDefined

  private[gui] def toSMCLViewport: Option[SMCLViewport] =
    if (this.isUndefined) None else Some(SMCLViewport(this.boundary.toSMCLBounds))



}

