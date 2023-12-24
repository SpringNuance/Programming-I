package o1.time
import scala.math._

/** Each instance of the class `Moment` represents a "moment" on a time scale,
  * identified by an integer number.
  *
  * Different time scales may be used in different contexts. `Moment` objects
  * could be used to represent different years, months, dates, hours, etc.,
  * depending on what is desired.
  *
  * A `Moment` object is immutable after it has been created. That is, its
  * state can not be changed in any way.
  *
  * @param time  an integer indicating which moment the object should represent (2019, for instance) */
class Moment(private val time: Int) {

  /** Produces a string representation of this moment. (It consists of just the digits.) */
  override def toString = this.time.toString


  /** Determines the length of time between this moment and another given moment.
    * For instance, if this moment represents the year 2000 and `another` the year
    * 2019, returns 19. The distance is always either positive or zero. */
  def distance(another: Moment) = abs(another.time - this.time)


  /** Determines whether this moment is later than another given moment. */
  def isLaterThan(another: Moment) = this.time > another.time


  /** Returns the later of two moments, either this one or the moment provided as a
    * parameter. If there is no difference, one of the two is returned arbitrarily. */
  def later(another: Moment) = if (this.isLaterThan(another)) this else another


  /** Returns the earlier of two moments, either this one or the moment provided as a
    * parameter. If there is no difference, one of the two is returned arbitrarily. */
  def earlier(another: Moment) = if (this.isLaterThan(another)) another else this


  // A couple of methods are missing; add them down here, for instance.


}

