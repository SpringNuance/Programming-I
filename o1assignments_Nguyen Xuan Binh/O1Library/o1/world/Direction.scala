
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.world


import scala.math._


/** Each instance of this class represents a direction on a two-dimensional plane. A direction
  * consists of two components: `dx` and `dy`, each of which is a number between -1.0 and +1.0
  * (inclusive). `dx` indicates the degree to which the x coordinate changes when moving in that
  * direction; `dy` is the same for the y coordinate. The x coordinate increases rightwards and
  * y downwards.
  *
  * For example, moving straight rightwards can be thought of a moving in a `Direction` with a
  * `dx` of +1.0 and a `dy` of 0.0; by comparison, the upward direction would have a `dx` of 0.0
  * and a `dy` of -1.0. . Diagonals have non-zero values for both `dx` and `dy`.
  *
  * The `dx` and `dy` of any `Direction` always sum up to (roughly) 1.0, with the exception of the
  * special value [[Direction$.NoDirection `NoDirection`]] which represents the lack of a direction
  * and as zero for both components. (In this, a `Direction` is different than a [[Velocity]],
  * which is a combination of a direction of movement and a speed.)
  *
  * You don’t instantiate `Direction` directly; instead, you create `Direction`s with the methods
  * on the [[Direction$ companion object]]. Among other things, you can:
  *
  *  - construct a direction from two differences between
  *    coordinates, as in `Direction.fromDeltas(-100, 50)`; or
  *  - create a direction that corresponds to a given angle, as
  *    in `Direction.fromDegrees(60)` and
  *    `Direction.fromRadians(-scala.math.Pi / 4)`.
  *
  * Some of the methods of a `Direction` object also create and return new `Direction`s, as does
  * the [[Pos.directionOf directionOf]] method on `Pos` objects. Moreover, the [[Direction$
  * companion object]] of this class has a few predefined `Direction` constants: [[Direction$.Up
  * `Up`]], [[Direction$.Down `Down`]], [[Direction$.Left `Left`]], [[Direction$.Right `Right`]],
  * and [[Direction$.NoDirection `NoDirection`]].
  *
  * `Direction` objects are immutable.
  *
  * This class has an alias in the top-level package [[o1]], so it’s accessible to students
  * simply via `import o1._`.
  *
  * @param dx  the amount of change in the x coordinate when moving in this direction, relative to `dy`
  * @param dy  the amount of change in the y coordinate when moving in this direction, relative to `dx` */
sealed case class Direction private(val dx: Double, val dy: Double) { // dx*dx+dy*dy ~= 1, or both zero


  /** Returns this direction as radians. (Zero radians points rightwards, positive angles are
    * counterclockwise from there, negative ones clockwise.) */
  def toRadians: Double = {
    val atan = atan2(-this.dy, this.dx)
    if (atan < 0) atan + 2 * Pi else atan
  }

  /** Returns this direction as degrees. (Zero degrees points rightwards, positive angles are
    * counterclockwise from there, negative ones clockwise.) */
  def toDegrees: Double = this.toRadians.toDegrees



  /** Returns a direction that has the opposite `dx` as this one and the same `dy`. */
  def switchX: Direction = Direction(-dx,  dy)

  /** Returns a direction that has the opposite `dy` as this one and the same `dx`. */
  def switchY: Direction = Direction( dx, -dy)

  /** Returns a direction that has the opposite `dx` and the opposite `dy` as this one. */
  def opposite: Direction= Direction(-dx, -dy)




  /** Returns `true` if this direction is a clearly rightward one (with a `dx` over zero) or at least
    * non-leftward (with a `dx` of exactly zero). Only returns `false` if `dx` is less than zero. */
  def isRightward: Boolean = this.dx >= 0

  /** Returns `true` if this direction is a clearly leftward one (with a `dx` under zero) or at least
    * non-rightward (with a `dx` of exactly zero). Only returns `false` if `dx` is over zero. */
  def isLeftward: Boolean = this.dx <= 0

  /** Returns `true` if this direction is a clearly downward one (with a `dy` over zero) or at least
    * non-upward (with a `dy` of exactly zero). Only returns `false` if `dy` is less than zero. */
  def isDownward: Boolean = this.dy >= 0

  /** Returns `true` if this direction is a clearly upward one (with a `dy` under zero) or at least
    * non-downward (with a `dy` of exactly zero). Only returns `false` if `dy` is over zero. */
  def isUpward: Boolean = this.dy <= 0

  /** Determines whether this direction and the given one point towards the same quarter of the unit circle.
   *  That is, determines whether the two directions’ `dx` have same sign as the other and whether
   *  their `dy`s also have the same signs. Zero values count as both positive and negative for this purpose. */
  def sharesQuadrant(another: Direction): Boolean = {
    def sameHalf(dx1: Double, dx2: Double) = dx1.sign == dx2.sign || dx1.sign == 0 || dx2.sign == 0
    sameHalf(this.dx, another.dx) && sameHalf(this.dy, another.dy)
  }



  /** Returns a direction that is removed from this direction by the given number of degrees counterclockwise.
    * For instance, `counterclockwise(90)` produces a direction at a right angle to the original. */
  def counterclockwise(degrees: Double): Direction = Direction.fromDegrees(this.toDegrees + degrees)

  /** Returns a direction that is removed from this direction by the given number of degrees clockwise.
    * For instance, `clockwise(90)` produces a direction at a right angle to the original. */
  def clockwise(degrees: Double): Direction = Direction.fromDegrees(this.toDegrees - degrees)

  /** Returns a direction that is, clockwise, removed from this direction by a number of degrees specified
    * by the given direction. Calling `dir1 + dir2` is equivalent to calling `dir1.counterclockwise(dir2.toDegrees) */
  def +(another: Direction): Direction = Direction.fromDegrees(this.toDegrees + another.toDegrees)



  /** Returns a `String` description of the `Direction`; e.g., `"dx=1.00,dy=0.00"`.
    * Uses two decimals for each coordinate. */
  def roughly: String = f"dx=$dx%1.2f,dy=$dy%1.2f"

}



/** This companion object of [[Direction class `Direction`]] provides some constants of type `Direction`
  * and methods for creating new `Direction` instances.
  *
  * The object has an alias in the top-level package [[o1]], so it’s accessible to students
  * simply via `import o1._`. */
object Direction {

  /** the “straight upwards” direction with a `dx` of zero, and a `dy` of -1 */
  val Up: Direction = new Direction(0, -1) { override val toString = "Direction.Up" }
  /** the “straight downwards” direction with a `dx` of zero, and a `dy` of +1 */
  val Down: Direction = new Direction(0,  1) { override val toString = "Direction.Down" }
  /** the “straight leftwards” direction with a `dx` of -1, and a `dy` of zero */
  val Left: Direction = new Direction(-1, 0) { override val toString = "Direction.Left" }
  /** the “straight rightwards” direction with a `dx` of +1, and a `dy` of zero */
  val Right: Direction = new Direction(1,  0) { override val toString = "Direction.Right" }

  /** a “non-direction” whose `dx` and `dy` are both zero */
  val NoDirection = new Direction(0, 0) { override val toString = "(no direction)" }


  /** Takes in an angle as radians and returns the corresponding [[Direction]]. Zero radians
    * points rightwards; positive angles are counterclockwise from there, negative ones clockwise. */
  def fromRadians(angle: Double) = Direction(cos(angle), -sin(angle))


  /** Takes in an angle as degrees and returns the corresponding [[Direction]]. Zero degrees
    * points rightwards; positive angles are counterclockwise from there, negative ones clockwise. */
  def fromDegrees(angle: Double) = fromRadians(angle.toRadians)


  /** Takes in a difference between x coordinates and a difference between y coordinates and
    * constructs a [[Direction]] from them.
    *
    * @param dx  a difference between two x coordinates; it is ''not'' necessary that this is normalized
    *            between -1.0 and +1.0 like the `dx` and `dy` of the resulting [[Direction]] object
    * @param dy  a difference between two x coordinates; it is ''not'' necessary that this is normalized
    *            between -1.0 and +1.0 like the `dx` and `dy` of the resulting [[Direction]] object
    * @return the [[Direction]] that corresponds to the given deltas. If both are exactly equal to zero, returns [[NoDirection]]. */
  def fromDeltas(dx: Double, dy: Double) = if (dx == 0.0 && dy == 0.0) NoDirection else fromRadians(atan2(-dy, dx))


  /** Returns a randomly chosen direction (evenly distributed over 360 degrees). */
  def random() = {
    Direction.fromDegrees(scala.util.Random.nextInt(360))
  }

  private type Key = scala.swing.event.Key.Value
  private val  Key = scala.swing.event.Key
  private val ArrowToDir = Map(Key.Up -> Up, Key.Left -> Left, Key.Down-> Down, Key.Right-> Right)
  private val WASDToDir  = Map(Key.W  -> Up, Key.A    -> Left, Key.S   -> Down, Key.D    -> Right)
  private val KeyToDir   = ArrowToDir ++ WASDToDir


  /** Returns the [[Direction]] that corresponds to the given arrow key. For example, the right arrow corresponds to `Right`.
    * @param key  any key on the keyboard
    * @return one of [[Up]], [[Down]], [[Left]], [[Right]]; `None` if the given key was not one of the four arrow keys
    * @see [[fromWASD]], [[fromKey]] */
  def fromArrowKey(key: Key) = ArrowToDir.get(key)

  /** Returns the [[Direction]] that corresponds to the given WASD key. For example, the D key corresponds to `East`.
    * @param key  any key on the keyboard
    * @return one of [[Up]], [[Down]], [[Left]], [[Right]]; `None` if the given key was not one of the four WASD keys
    * @see [[fromArrowKey]], [[fromKey]] */
  def fromWASD(key: Key) = WASDToDir.get(key)

  /** Returns the [[Direction]] that corresponds to the given arrow key. For example, the right arrow or the D key corresponds to `East`.
    * @param key  any key on the keyboard
    * @return one of [[Up]], [[Down]], [[Left]], [[Right]]; `None` if the given key was not one of the four arrow keys or one of the four WASD keys
    * @see [[fromArrowKey]], [[fromWASD]] */
  def fromKey(key: Key) = KeyToDir.get(key)

}

