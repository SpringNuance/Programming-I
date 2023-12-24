
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.world


import o1.util.ConvenientDouble
import scala.math._



/** Each instance of this class represents movement in the context of a two-dimensional plane.
  * A velocity is a combination of a [[Direction]] of movement with a speed (a non-negative
  * `Double`).
  *
  * Another way to think about a `Velocity` is to divide it in two components: `dx` and `dy`.
  * `dx` indicates the amount (and direction) of movement along the x axis; `dy` is the same
  * for the y axis. The x coordinate increases rightwards and y downwards. For instance,
  * a velocity with a `dx` of 10 and a `dy` of zero indicates rightward movement, and a velocity
  * with a `dx` of -100 and a `dy` of -100 indicates faster movement leftwards and upwards.
  *
  * There are many ways to create a `Velocity` using the methods on the [[Velocity$ companion object]].
  * Among other things, you can:
  *
  *  - construct a velocity from a direction and a (positive
  *    or zero) speed, as in `Velocity(Direction.Left, 50)`;
  *  - construct a direction from `dx` and `dy`, as in
  *    `Velocity(-100, 50)`; or
  *  - determine the velocity needed to go from one
  *    [[Pos]] to another in a single unit of time, as
  *    in `Velocity.between(pos1, pos2)`.
  *
  * Many of the methods on `Velocity` objects also create and return new velocities.
  *
  * `Velocity` objects are immutable.
  *
  * This class has an alias in the top-level package [[o1]], so it’s accessible to students
  * simply via `import o1._`.
  *
  * @param direction  the direction of movement
  * @param speed      the speed of movement; ''this cannot be negative''  */
final case class Velocity(val direction: Direction, val speed: Double) {
  require(speed >= 0, s"The speed component of a Velocity cannot be negative (was: $speed)")

  /** the amount of change in the x coordinate when moving at this velocity for one unit of time */
  lazy val dx = this.direction.dx * speed
  /** the amount of change in the y coordinate when moving at this velocity for one unit of time */
  lazy val dy = this.direction.dy * speed


  /** Sums this velocity with the given one. The sum’s `dx` equals the sum of the two `dx`s;
    * the same goes for `dy`. */
  def +(another: Velocity): Velocity = Velocity(this.dx + another.dx, this.dy + another.dy)

  /** Subtracts the given velocity from this one. The results’s `dx` equals the difference between the
    * two  `dx`s; the same goes for `dy`. */
  def -(another: Velocity): Velocity = this + -another

  /** Returns a velocity that is equal to this one in speed but has the opposite direction.
    * The expressions `-myVelocity` and `myVelocity.opposite` are equivalent. */
  def unary_- = this.copy(direction = this.direction.opposite)

  /** Returns a velocity that is equal to this one in speed but has the opposite direction.
    * The expressions `myVelocity.opposite` and `-myVelocity` are equivalent. */
  def opposite = this.copy(direction = this.direction.opposite)


  /** Returns a velocity whose direction is the given number of degrees counterclockwise from this one’s.
    * The speed is the same. */
  def counterclockwise(degrees: Double): Velocity = this.changeDirection(this.direction.counterclockwise(degrees))
  /** Returns a velocity whose direction is the given number of degrees clockwise from this one’s.
    * The speed is the same. */
  def clockwise(degrees: Double): Velocity = this.changeDirection(this.direction.clockwise(degrees))


  /** Returns a velocity whose `dx` is the opposite of this one’s. The `dy` is the same, as is the speed. */
  def switchX: Velocity = this.copy(direction = this.direction.switchX)

  /** Returns a velocity whose `dy` is the opposite of this one’s. The `dx` is the same, as is the speed. */
  def switchY: Velocity = this.copy(direction = this.direction.switchY)



  /** Returns a velocity that has the given direction and the same speed as this one. */
  def changeDirection(newDirection: Direction): Velocity = this.copy(direction = newDirection)

  /** Returns a velocity that has the given speed and the same direction as this one. */
  def changeSpeed(newSpeed: Double): Velocity = this.copy(speed = newSpeed)


  /** Returns a velocity whose `speed` equals this velocity’s speed multiplied by the given
    * number (which must be positive or zero). The direction is the same. */
  def *(multiplier: Double): Velocity = this.copy(speed = this.speed * multiplier)

  /** Returns a velocity whose `speed` equals this velocity’s speed divided by the given
    * number (which must be positive). The direction is the same. */
  def /(divisor: Double): Velocity = this * (1 / divisor)

  /** Returns a velocity whose `speed` equals this velocity’s speed plus the given number.
    * The direction is the same. */
  def faster(addition: Double): Velocity  = Velocity(this.direction, (this.speed + addition) atLeast 0)

  /** Returns a velocity whose `speed` equals this velocity’s speed minus the given number.
    * The direction is the same. */
  def slower(reduction: Double): Velocity = Velocity(this.direction, (this.speed - reduction) atLeast 0)

  /** Returns a velocity whose direction equals this one’s but whose `speed` is zero. */
  def stopped: Velocity = this.changeSpeed(0)



  /** Returns a velocity whose `speed` is as close to this one’s as possible without exceeding
    * the given number. The direction is the same. */
  def noFasterThan(maxSpeed: Double): Velocity = this.copy(speed = this.speed atMost maxSpeed atLeast 0)

  /** Returns a velocity whose `speed` is as close to this one’s as possible without being lower
    * than the given number. The direction is the same. */
  def noSlowerThan(minSpeed: Double): Velocity = this.copy(speed = this.speed atLeast minSpeed)




  /** Returns the `Pos` that an object moving at this velocity reaches in one unit of time. That is,
    * adds the `dx` and `dy` components of this velocity to the given `Pos` and returns the result. */
  def nextFrom(position: Pos): Pos = position + this.toPos




  /** Returns a `String` description of the `Velocity`. E.g., `"111.80 with direction dx=0.89,dy=-0.45"`.
    * Uses two decimals for each number. */
  def roughly: String = f"$speed%1.2f with direction ${direction.roughly}"

  /** Returns a [[Pos]] whose `x` and `y` components equal this velocity’s `dx` and `dy`, respectively. */
  def toPos: Pos = Pos(this.dx, this.dy)

}







/** This companion object of [[Velocity class `Velocity`]] exists primarily to provide convenience
  * methods for creating new instances of `Velocity`.
  *
  * The object has an alias in the top-level package [[o1]], so it’s accessible to students
  * simply via `import o1._`. */
object Velocity {

  /** a zero-speed [[Velocity]] with a direction of [[Direction.NoDirection `NoDirection`]] */
  val Still = Velocity(Direction.NoDirection, 0)

  /** Returns a [[Velocity]] with the given `dx` and `dy`. */
  def apply(dx: Double, dy: Double): Velocity = Velocity(Direction.fromDeltas(dx, dy), hypot(dx, dy))


  /** Returns a [[Velocity]] from two given [[Pos]] objects: the direction is determined from the
    * first given [[Pos]] towards and the speed equals the distance between the two `Pos`es. */
  def between(from: Pos, to: Pos): Velocity = Velocity(to - from)

  /** Returns a [[Velocity]] whose `dx` and `dy` equal the `x` and `y` of the given [[Pos]], respectively. */
  def apply(vector: Pos): Velocity = Velocity(vector.x, vector.y)

  /** Returns the sum of all the given velocities. If the given collection is empty, returns [[Velocity.Still]]. */
  def sum(velocities: Seq[Velocity]): Velocity = velocities.reduceLeftOption( _ + _ ) getOrElse Velocity.Still

  /** Returns the average of all the given velocities. If the given collection is empty, returns [[Velocity.Still]]. */
  def average(velocities: Seq[Velocity]): Velocity = if (velocities.nonEmpty) sum(velocities) / velocities.size else Velocity.Still

}
