package o1.excursion

import scala.collection.mutable.Buffer
import scala.math._


/** The class `Excursion` represents group trips that would-be participants
  * need to sign up for in advance.
  * @param name   the name of the excursion
  * @param limit  the maximum number of people that can participate */
class Excursion(val name: String, val limit: Int) {

  private val interestedStudents = Buffer[String]()


  /** Returns the number of people who have registered an interest in
    * taking part in the excursion. (This number may exceed the limit.)
    * @see [[registerInterest]] */
  def numberOfInterested = this.interestedStudents.size


  /** Determines whether the excursion is full, that is, if as many or more
    * people have registered an interest in the excursion as can take part. */
  def isFull = this.numberOfInterested >= this.limit


  /** Returns the number of people queuing up for places. Unless the
    * excursion is full, this number will be zero. */
  def queueSize = max(this.numberOfInterested - this.limit, 0)


  /** Registers the given person as wishing to participate in the excursion.
    * @return `true` if there was place for the new participant,
    *         `false` if the person was placed in queue instead */
  def registerInterest(newPersonName: String) = {
    val fits = !this.isFull
    this.interestedStudents += newPersonName
    fits
  }


  /** Returns the number of people that will take part in the excursion,
    * as things stand. This will be a number between 0 (if no-one has
    * registered an interest) and `limit` (if the excursion is full). */
  def numberOfParticipants = min(this.numberOfInterested, this.limit)


  /** Determines the latest person who has registered an interest in
    * participating ''and'' who fits in. The person's name is wrapped in
    * an `Option`; `None` is returned if there are no participants at all. */
  def lastParticipant =
    if (this.numberOfInterested > 0) {
      val numberOfLast = this.numberOfParticipants
      Some(this.interestedStudents(numberOfLast))
    } else {
      None
    }


  /** Returns a textual description of the excursion. */
  override def toString =
    this.name + ": " +
      this.numberOfParticipants + "/" + this.limit +
      " with " + this.queueSize + " in queue"


  // Etc.
  // There could be other methods here, e.g., for cancelling a registration.

}
