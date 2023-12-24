package o1.goodstuff

/** The class `Experience` represents entries in a user's experience diary.
  * Each entry represents an experience as evaluated by the diarist.
  *
  * An experience object is immutable once created; there are no methods to
  * change any of its attributes.
  *
  * @param name         a name that identifies the experience, such as "Hotel Anna, Helsinki" or "Sangre de Toro 2007"
  * @param description  the diarist's description of the experience, such as "Nice but unremarkable"
  * @param price        the price (per unit) in euros
  * @param rating       a rating given by the diarist to the experience (from 1 to 10 or on some other scale) */
class Experience(val name: String, val description: String, val price: Double, val rating: Int) {


  /** Returns the "value for money" for the experience. Value for money
    * equals the experience's rating divided by its price. */
  def valueForMoney = this.rating / this.price


  /** Returns `true` if the experience has a higher rating than the experience
    * given as a parameter, and `false` otherwise. */
  def isBetterThan(another: Experience) = this.rating > another.rating


  /** Compares two experiences --- this one and the one given as a parameter ---
    * and returns the one with the higher rating. If both have the same rating,
    * arbitrarily returns one of the two. */
  def chooseBetter(another: Experience) = if (this.isBetterThan(another)) this else another

}

