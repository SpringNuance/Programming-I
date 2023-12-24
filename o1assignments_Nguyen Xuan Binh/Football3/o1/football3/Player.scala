package o1.football3


/** The class `Player` represents football players in a match statistics program.
  * Only some very basic information about each player is recorded.
  *
  * A player object is immutable after creation.
  *
  * @param name      the name of the player
  * @param employer  the club the player plays for */
class Player(val name: String, val employer: Club) {

  override def toString = this.name + " (" + this.employer + ")"

}
