

package o1.stars
import Ordering.Double.TotalOrdering


/** Each `Constellation` object represents a constellation visible on a star map.
  * A constellation consists of one or more imaginary "lines" between stars.
  *
  * @param name   the name of the constellation
  * @param lines  one or more pairs of stars; each pair defines an imaginary line between two stars */
class Constellation(val name: String, val lines: Vector[(Star,Star)]) {

  /** All the stars in the constellation. That is, all the stars that are at one end
    * of any of the lines that form the constellation. */
  val stars: Set[Star] = (lines.map(_._1) ++ lines.map(_._2)).toSet
  // TODO: implement (in Chapter 9.2)

  /** A pair of [[StarCoords]] (X,Y), so that X is the smallest x coordinate of any star in the constellation
    * and Y the smallest y coordinate of any star in the constellation. */
  val minCoords = new StarCoords(stars.minBy(_.coords.x).coords.x, stars.minBy(_.coords.y).coords.y) // TODO: replace with a proper implementation (in Chapter 9.2)

  /** A pair of [[StarCoords]] (X,Y), so that X is the greatest x coordinate of any star in the constellation
    * and Y the greatest y coordinate of any star in the constellation. */
  val maxCoords = new StarCoords(stars.maxBy(_.coords.x).coords.x, stars.maxBy(_.coords.y).coords.y) // TODO: replace with a proper implementation (in Chapter 9.2)

  /** Determines whether the given coordinates are "roughly in the neighborhood" of this
    * constellation on the star map. */
  def isNearish(candidate: StarCoords) = {
    def isBetween(value: Double, low: Double, high: Double) = value >= low && value < high
    val (min, max) = (this.minCoords, this.maxCoords)
    isBetween(candidate.x, min.x, max.x) && isBetween(candidate.y, min.y, max.y)
  }

  // Study class Constellation’s documentation and its incomplete code. Notice the following in particular:
  //
  // The stars method, minCoords, and maxCoords are missing.
  // We construct a constellation from a vector of pairs.
  // The variables stars has the type Set[Star]: a set of stars.
  // A set is a collection of elements. Unlike a vector or a buffer, a set can never contain multiple copies
  // of the same element.
  // The elements of a set don’t have numerical indices.
  // One way to form a set is to call toSet on an existing collection (e.g., myVector.toSet). Even if the
  // original collection had multiple copies of the same elements, each element will appear only once in
  // the resulting Set.
  // Add the missing parts:
  //
  // Implement Constellation as specified.

}

