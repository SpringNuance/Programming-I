package o1.people

/** A `TravelCard` object represents a travel card meant for accessing
  * public transportation. A travel card may be valid or invalid; a
  * card's status can be set as invalid, if the allocated time expires
  * or if the card is stolen, for instance.
  *
  * @param id  a number that identifies the travel card from others */
class TravelCard(val id: Int) {

  /** whether the travel card is valid or not */
  var isValid: Boolean = true

  /** Returns a compact string description of the person. */
  override def toString = {
    val validity = if (this.isValid) "valid" else "invalid"
    s"$id($validity)"
  }

}
