

package o1.stars

import o1._


/** Each object of type `StarMap` represents a specific perspective of a night sky.
  *
  * @param stars           the stars visible in the sky
  * @param constellations  the constellations visible in the sky */
class StarMap(val stars: Vector[Star], val constellations: Vector[Constellation]) {

  /** The number of stars visible on this map. */
  def numberOfStars = this.stars.size

  /** The number of constellations visible on this map. */
  def numberOfConstellations = this.constellations.size

}


