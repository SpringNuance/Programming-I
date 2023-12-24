package o1.goodstuff

import scala.collection.mutable.Buffer

/** The class `Category` represents categories of experiences in the GoodStuff diary
  * application. A single category contains zero or more experiences that are similar
  * in some way and are purchased in units of the same kind. A category object could
  * be used represent hotel experiences, for instance, or wines the diarist has tasted.
  *
  * A category object has a mutable state: A newly created category has no experiences
  * yet; experiences must be added to it using the `addExperience` method.
  *
  * @param name  the name of the category, such as "Hotel" or "Wine"
  * @param unit  the name of the pricing unit associated with the experiences in this category, such as "night" or "bottle" */
class Category(val name: String, val unit: String) {

  private val experiences = Buffer[Experience]()   // container: all the experiences recorded by the user in this category
  private var fave: Option[Experience] = None      // most-wanted holder: the best-rated experience in this category


  /** Returns the experience that the user has rated the highest in this category.
    * That is, of all the experiences added into this category so far, returns
    * the one with the highest rating. If there is a tie for first place, the
    * experience recorded first is returned. */
  def favorite = this.fave


  /** Adds a new experience to this category. */
  def addExperience(newExperience: Experience) = {
    this.experiences += newExperience
    this.fave match {
      case None =>
        this.fave = Some(newExperience)
      case Some(oldFave) =>
        val newFave = newExperience.chooseBetter(oldFave)
        this.fave = Some(newFave)
    }
  }


  /** Returns a collection containing all the experiences recorded in this category. */
  def allExperiences = this.experiences.toVector

}

