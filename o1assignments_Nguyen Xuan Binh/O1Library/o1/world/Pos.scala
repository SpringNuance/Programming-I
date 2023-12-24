
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.world


import java.awt.Point
import scala.math.hypot
import o1.util._

import smcl.modeling.d2.{Pos => SMCLPos}



/** Each instance of this class represents a location on a two-dimensional plane. A `Pos` object is
  * essentially a pair of two coordinates, `x` and `y`. `Pos` objects are immutable.
  *
  * This class has an alias in the top-level package [[o1]], so it’s accessible to students simply
  * via `import o1._`.
  *
  * @param x  the x coordinate of the `Pos`
  * @param y  the y coordinate of the `Pos` */
final case class Pos(val x: Double, val y: Double) {



  /** Adds the two given values to the `x` and `y` of this `Pos`, respectively. Returns the resulting `Pos`.
    * The expressions `myPosadd(dx, dy)` and `myPosoffset(dx, dy)` are equivalent. */
  @inline def add(dx: Double, dy: Double) = Pos(this.x + dx, this.y + dy)

  /** Returns the `Pos` that is `dx` and `dy` removed from this `Pos`.
    * The expressions `myPosoffset(dx, dy)` and `myPosadd(dx, dy)` are equivalent. */
  @inline def offset(dx: Double, dy: Double): Pos = this.add(dx, dy)

  /** Adds the `x` and `y` of the given `Pos` to the `x` and `y` of this `Pos`, respectively. Returns the resulting `Pos`.
    * The expressions `pos1.add(pos2)`, `pos1.offset(pos2)`, and `pos1 + pos2` are equivalent. */
  @inline def add(pos: Pos): Pos = this.add(pos.x, pos.y)

  /** Returns the `Pos` that is removed from this `Pos` by the amount indicated by the given `Pos`.
    * The expressions `pos1.offset(pos2)`, `pos1.add(pos2)`, and `pos1 + pos2` are equivalent. */
  @inline def offset(another: Pos): Pos = this.add(another)

  /** Adds the `x` and `y` of the given `Pos` to the `x` and `y` of this `Pos`, respectively. Returns the resulting `Pos`.
    * The expressions `pos1 + pos2`, `pos1.add(pos2)`, and `pos1.offset(pos2)` are equivalent. */
  @inline def +(another: Pos): Pos = this.add(another)





  /** Subtracts the `x` and `y` of the given `Pos` from the `x` and `y` of this `Pos`, respectively. Returns the resulting `Pos`.
    * The expressions `pos1.subtract(pos2)` and `pos1 - pos2` are equivalent. */
  @inline def subtract(another: Pos): Pos = this + -another

  /** Subtracts the `x` and `y` of the given `Pos` from the `x` and `y` of this `Pos`, respectively. Returns the resulting `Pos`.
    * The expressions `pos1 - pos2` and `pos1.subtract(pos2)` are equivalent. */
  @inline def -(another: Pos): Pos = this + -another

  /** Returns the `Pos` that is the opposite of this `Pos`. For instance, the opposite of (2,-3) is (-2,3). */
  @inline def unary_- = Pos(-this.x, -this.y)





  /** Returns a `Pos` whose `x` coordinate is less than this `Pos`’s by the given amount. */
  @inline def subtractX(dx: Double): Pos = this.setX(this.x - dx)

  /** Returns a `Pos` whose `y` coordinate is less than this `Pos`’s by the given amount. */
  @inline def subtractY(dy: Double): Pos = this.setY(this.y - dy)

  /** Returns a `Pos` whose `x` coordinate is greater than this `Pos`’s by the given amount. */
  @inline def addX(dx: Double): Pos = this.setX(this.x + dx)

  /** Returns a `Pos` whose `y` coordinate is greater than this `Pos`’s by the given amount. */
  @inline def addY(dy: Double): Pos = this.setY(this.y + dy)

  /** Returns a `Pos` whose two coordinates are obtained by multiplying this one’s `x` and `y` by the given factor.
    * For instance, (2,-3) multiplied by 10 is (20,-30). The expressions `myPos.multiply(n)` and `myPos * n` are equivalent. */
  @inline def multiply(factor: Double): Pos = Pos(this.x * factor, this.y * factor)

  /** Returns a `Pos` whose two coordinates are obtained by dividing this one’s `x` and `y` by the given number.
    * For instance, (20,-30) divided by 10 is (2,-3). The expressions `myPos.divide(n)` and `myPos / n` are equivalent. */
  @inline def divide(divisor: Double): Pos = Pos(this.x / divisor, this.y / divisor)

  /** Returns a `Pos` whose two coordinates are obtained by multiplying this one’s `x` and `y` by the given factor.
    * For instance, (2,-3) multiplied by 10 is (20,-30). The expressions `myPos * n` and `myPos.multiply(n)` are equivalent. */
  @inline def *(factor: Double): Pos = this.multiply(factor)

  /** Returns a `Pos` whose two coordinates are obtained by dividing this one’s `x` and `y` by the given number.
    * For instance, (20,-30) divided by 10 is (2,-3). The expressions `myPos / n` and `myPos.divide(n)` are equivalent. */
  @inline def /(divisor: Double): Pos = this.divide(divisor)

  /** Returns a `Pos` whose `x` equals the given value and whose `y` equals this `Pos`’s `y`. */
  @inline def setX(newX: Double): Pos = this.copy(x = newX)

  /** Returns a `Pos` whose `x` equals this `Pos`’s `x` and whose `y` equals the given value. */
  @inline def setY(newY: Double): Pos = this.copy(y = newY)



  /** Returns a `Pos` whose `x` and `y` are determined by applying the given function to the `x` and `y` of this `Pos`.
    * @param computeNew  a function that takes in an x and a y and returns a pair that contains the coordinates of the resulting `Pos` */
  @inline def withXY(computeNew: (Double, Double) => (Double, Double)): Pos = Pos(computeNew(this.x, this.y))

  /** Returns a `Pos` whose `x` is determined by applying the given function to the `x` of this `Pos` and whose `y` equals that of this `Pos`.
    * @param computeNew  a function that takes in an x coordinate and returns the x for the resulting `Pos` */
  @inline def withX(computeNew: Double => Double): Pos = this.setX(computeNew(this.x))

  /** Returns a `Pos` whose `x` equals that of this `Pos` and whose `y` is determined by applying the given function to the `y` of this `Pos`.
    * @param computeNew  a function that takes in a y coordinate and returns the y for the resulting `Pos` */
  @inline def withY(computeNew: Double => Double): Pos = this.setY(computeNew(this.y))







  /** Returns the difference between this `Pos`’s `x` coordinate and the given one’s.
    * @return the difference, which is positive if the given `Pos`’s `x` is greater than this one’s and negative if the reverse is true */
  @inline def xDiff(another: Pos): Double = another.x - this.x

  /** Returns the difference between this `Pos`’s `y` coordinate and the given one’s.
    * @return the difference, which is positive if the given `Pos`’s `y` is greater than this one’s and negative if the reverse is true */
  @inline def yDiff(another: Pos): Double = another.y - this.y



  /** Returns a `Pos` that is as close to this `Pos` as possible but has a `y` coordinate less than or equal to the given limit.
    * @see [[noHigherThan]], [[noFurtherRightThan]], [[clampY]], [[clamp]] */
  @inline def noLowerThan(yMax: Double): Pos = Pos(this.x, this.y atMost yMax)

  /** Returns a `Pos` that is as close to this `Pos` as possible but has a `y` coordinate equal to or greater than the given limit.
    * @see [[noLowerThan]], [[noFurtherLeftThan]], [[clampY]], [[clamp]] */
  @inline def noHigherThan(yMin: Double): Pos = Pos(this.x, this.y atLeast yMin)

  /** Returns a `Pos` that is as close to this `Pos` as possible but has a `x` coordinate equal to or greater than the given limit.
    * @see [[noHigherThan]], [[noFurtherRightThan]], [[clampX]], [[clamp]] */
  @inline def noFurtherLeftThan(xMin: Double): Pos = Pos(this.x atLeast xMin, this.y)

  /** Returns a `Pos` that is as close to this `Pos` as possible but has a `y` coordinate less than or equal to the given limit.
    * @see [[noLowerThan]], [[noFurtherLeftThan]], [[clampX]], [[clamp]] */
  @inline def noFurtherRightThan(xMax: Double): Pos = Pos(this.x atMost xMax, this.y)


  /** Returns a `Pos` that is as close to this `Pos` as possible but whose `x` is between the given lower and upper bounds (inclusive).
    * @param min  the least possible value of `x` in the resulting `Pos`; must not exceed `max`
    * @param max  the greatest possible value of `x` in the resulting `Pos`; must not be below `min`
    * @see [[noFurtherLeftThan]], [[noFurtherRightThan]], [[clampY]], [[clamp]] */
  @inline def clampX(min: Double, max: Double): Pos = Pos(this.x atLeast min atMost max, this.y)

  /** Returns a `Pos` that is as close to this `Pos` as possible but whose `y` is between the given lower and upper bounds (inclusive).
    * @param min  the least possible value of `y` in the resulting `Pos`; must not exceed `max`
    * @param max  the greatest possible value of `y` in the resulting `Pos`; must not be below `min`
    * @see [[noLowerThan]], [[noHigherThan]], [[clampX]], [[clamp]] */
  @inline def clampY(min: Double, max: Double): Pos = Pos(this.x, this.y atLeast min atMost max)

  /** Returns a `Pos` that is as close to this `Pos` as possible but whose `x` and `y` are between the given lower and upper bounds (inclusive).
    * @param xMin  the least possible value of `x` in the resulting `Pos`; must not exceed `xMax`
    * @param xMax  the greatest possible value of `x` in the resulting `Pos`; must not be below `xMin`
    * @param yMin  the least possible value of `y` in the resulting `Pos`; must not exceed `yMax`
    * @param yMax  the greatest possible value of `y` in the resulting `Pos`; must not be below `yMin`
    * @see [[noLowerThan]], [[noHigherThan]], [[noFurtherLeftThan]], [[noFurtherRightThan]], [[clampX]], [[clampY]] */
  @inline def clamp(xMin: Double, xMax: Double, yMin: Double, yMax: Double): Pos =
    Pos(this.x atLeast xMin atMost xMax, this.y atLeast yMin atMost yMax)





  /** Returns the [[Direction]] from this `Pos` towards the given other `Pos`. */
  @inline def directionOf(destination: Pos): Direction = Direction.fromDeltas(this.xDiff(destination), this.yDiff(destination))

  /** Returns a `Pos` those `x` and `y` capture the relative distance from this `Pos` towards the given `Pos`.
    * That is, subtracts the `x` and `y` of the this `Pos` from the `x` and `y` of given `Pos`, respectively.
    * The expressions `pos1.vectorTo(pos2)` and `pos2 - pos1` are equivalent. */
  @inline def vectorTo(destination: Pos): Pos = destination - this


  /** Returns the `Pos` that an object moving at the given velocity reaches in one unit of time.
    * That is, adds the `dx` and `dy` components of the given velocity to this `Pos` and returns the result. */
  @inline def nextPos(velocity: Velocity): Pos = this + velocity.toPos


  /** Returns the distance between this `Pos` and the given `Pos` “as the crow flies”. */
  @inline def distance(another: Pos): Double = hypot(this.xDiff(another), this.yDiff(another))





  /** Returns a `String` description of the `Pos` that is identical to [[toString]]
    * except it has two decimals for each coordinate. */
  @inline def roughly = f"$x%1.2f,$y%1.2f"

  /** Returns a `String` description of the `Pos`. It has the form `"(x,y)"`.
    * @see [[roughly]] */
  @inline override def toString = s"($x,$y)"

  /** Returns the `x` and `y` coordinates of the `Pos` as a tuple. */
  @inline def toPair: (Double, Double) = (this.x, this.y)

  /** Applies the given function to the two coordinates of the `Pos` and returns the result. */
  @inline def turnInto[Result](f: (Double, Double) => Result): Result =
    f(this.x, this.y)

  @inline private[o1] def toSMCLPos: SMCLPos = SMCLPos(this.x, this.y)



}




/** This companion object of [[Pos class `Pos`]] provides alternative methods for creating new `Pos` objects.
  * It has an alias in the top-level package [[o1]], so it’s accessible to students simply via `import o1._`. */
object Pos {

  /** Constructs a [[Pos]] instance from the given pair of x and y coordinates. */
  @inline def apply(xyPair: (Double, Double)): Pos = new Pos(xyPair._1, xyPair._2)


  /** Constructs a [[Pos]] instance from the coordinates of the given `Point` object. */
  @inline def apply(point: Point): Pos = new Pos(point.getX, point.getY)

}
