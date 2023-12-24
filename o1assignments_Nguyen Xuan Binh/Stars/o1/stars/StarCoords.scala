

package o1.stars

import o1.Pos
import o1.Pic


/** A `StarCoords` object describes the location of a star on a two-dimensional star map.
  * A location consists of two components, `x` and `y`, each of which has a value between
  * -1.0 and +1.0. The x coordinate ranges from -1.0 at the very left of the map to +1.0 on
  * the right; the y coordinate similarly ranges from -1.0 at the bottom to +1.0 at the top.
  * For instance, a star with `StarCoords` of (0.0,0.0) appears at the exact center of the map.
  *
  * @param x  the x coordinate of a location on the star map, between -1.0 and +1.0
  * @param y  the y coordinate of a location on the star map, between -1.0 and +1.0 */
class StarCoords(val x: Double, val y: Double) {

  /** Converts this pair of star coords to the position (`Pos`) of a pixel within a given
    * image (`Pic`). For instance, if this `StarCoords` represents the middle (0,0)
    * of a star map, and a 400-by-400 pixel image is given, this method returns a `Pos`
    * of (200,200). Another example: if this `StarCoords` is (-0.5,0.2) and the given
    * image is a 1000-by-1000 pixel square, returns a `Pos` of (250,400). */
  def toImagePos(image: Pic) = new Pos(this.xPercent * image.width, this.yPercent * image.height)


  /** Returns a value between 0 and 1 that indicates how many percent from the left this
    * location’s x coordinate is. For example, 0.5 is halfway from the left. */
  def xPercent = StarCoords.asPercent(this.x)

  /** Returns a value between 0 and 1 that indicates how many percent from the top this
    * location’s y coordinate is. For example, 0.25 is a quarter of the way down from the top. */
  def yPercent = StarCoords.asPercent(-this.y)


  /** Returns a string description of the star coords; e.g., "x=0.62, y=0.04". */
  override def toString = f"x=$x%.2f, y=$y%.2f"

}



// Note to students: above is a *class* named StarCoords. Below is a singleton object of the same name.


/** The singleton object `StarCoords` provides some constants and convenience methods for
  * use alongside the `StarCoords` class. */
object StarCoords {

  /** The smallest possible `x` or `y` coordinate value of a `StarCoords`. */
  val MinValue = -1.0

  /** The largest possible `x` or `y` coordinate value of a `StarCoords`. */
  val MaxValue = 1.0


  /** Constructs a `StarCoords` from two values that describe how many
    * percent from the top left corner of the map the location is.
    * (Cf. [[StarCoords.xPercent]] and [[StarCoords.yPercent]].) */
  def fromPercentages(xPercent: Double, yPercent: Double) =
    new StarCoords(MinValue + xPercent * Range, MinValue + (1 - yPercent) * Range)


  private val Range = (MaxValue - MinValue)
  private def asPercent(value: Double) = (value - MinValue) / Range
}
