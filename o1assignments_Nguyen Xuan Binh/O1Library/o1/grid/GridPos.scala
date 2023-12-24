
package o1.grid

import CompassDir._


/** An object of type `GridPos` represents a pair of integer coordinates on two axes named x and y.
  * Such a pair can be used to reference a point on a [[Grid]].
  *
  * In this coordinate system, `x` increases "eastwards" and `y` increases "southwards".
  *
  * `GridPos` objecta are immutable.
  *
  * This class has an alias in the top-level package [[o1]], so it’s accessible to students
  * simply via `import o1._`.
  *
  * @param x  an x coordinate
  * @param y  a y coordinate */
case class GridPos(val x: Int, val y: Int) {

  /** Returns another grid position that is in the given direction from this one
    * and at a given distance.
    *
    * For instance, say this position has an `x` of 10 and a `y` of 20. If `direction` is
    * `CompassDir.North` and `distance` is 3, then the result has an `x` of 10 and a `y` of 17.
    *
    * @see [[CompassDir.xStep]], [[CompassDir.yStep]] */
  def relative(direction: CompassDir, distance: Int) =
    new GridPos(this.x + direction.xStep * distance,
                this.y + direction.yStep * distance)


  /** Returns a grid position that "neighbors" this one in the given direction.
    * For instance, if this position has an `x` of 10 and a `y` of 20, and the `direction`
    * parameter is given the value `CompassDir.South`, then the result has an `x` of 10
    * and a `y` of 21. Calling this method is essentially the same as calling `relative`
    * with a `distance` of one. */
  def neighbor(direction: CompassDir): GridPos = this.relative(direction, 1)


  /** Returns an infinite lazy-list of locations at increasing distances from this `GridPos` in
    * the given direction. For example, if this `GridPos` is (10,1), and the direction is `South`,
    * returns a list of (10,2), (10,3), (10,4), etc. */
  def pathTowards(direction: CompassDir): LazyList[GridPos] = LazyList.from(1).map( this.relative(direction, _) )


  /** Determines whether this grid position equals the given one. This is the case if
    * the two have identical x and y coordinates. */
  def ==(another: GridPos): Boolean = this.x == another.x && this.y == another.y


  /** Returns the difference between the x coordinate of this `GridPos` and that of the given
    * `GridPos`. The result is negative if the x coordinate of this  `GridPos` is greater. */
  def xDiff(another: GridPos): Int = another.x - this.x


  /** Returns the difference between the y coordinate of this `GridPos` and that of the given
    * `GridPos`. The result is negative if the y coordinate of this  `GridPos` is greater. */
  def yDiff(another: GridPos): Int = another.y - this.y


  /** Returns the [[xDiff]] and [[yDiff]] between this `GridPos` and the given one as a pair. */
  def diff(another: GridPos): (Int, Int) = (this.xDiff(another), this.yDiff(another))


  /** Determines if the given `GridPos` is west or east of this one. Returns `East` if this
    * `GridPos`’s x coordinate is less than `another`’s and `West` if the opposite is true.
    * Wraps that return value in an `Option`; `None` means that the x coordinates are equal. */
  def xDirectionOf(another: GridPos): Option[CompassDir] = {
    val dx = this.xDiff(another)
    if (dx < 0) Some(West) else if (dx > 0) Some(East) else None
  }

  /** Determines if the given `GridPos` is north or south of this one. Returns `South` if this
    * `GridPos`’s y coordinate is less than `another`’s and `North` if the opposite is true.
    * Wraps that direction in an `Option`; `None` means that the y coordinates are equal. */
  def yDirectionOf(another: GridPos): Option[CompassDir] = {
    val dy = this.yDiff(another)
    if (dy < 0) Some(North) else if (dy > 0) Some(South) else None
  }


  /** Returns the "grid distance" between this `GridPos` and the given one.
    * The grid distance between `a` and `b` equals `a.xDiff(b).abs + a.yDiff(b).abs`. */
  def distance(another: GridPos): Int = this.xDiff(another).abs + this.yDiff(another).abs


  /** Returns a textual description of this position. The description is of the form `"(x,y)"`. */
  override def toString = s"($x,$y)"

}
