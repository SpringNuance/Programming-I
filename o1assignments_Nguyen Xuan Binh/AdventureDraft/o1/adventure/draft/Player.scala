package o1.adventure.draft

/** A `Player` object represents a player character controlled by the real-life user of the program.
  *
  * A player object's state is mutable: the player's location and possessions can change, for instance.
  *
  * '''NOTE: The AdventureDraft module is not even close to being well designed.
  * See Chapter 9.1 in the course materials.'''
  *
  * @param startingArea  the initial location of the player */
class Player(startingArea: Area) {

  private var currentLocation = startingArea
  private var quitCommandGiven = false

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven


  /** Returns the current location of the player. */
  def location = this.currentLocation


  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player's current location towards the direction name.
    * Prints out the results of the action. */
  def go(direction: String) = {
    var destination: Option[Area] = None
    if (direction == "north") {
      destination = this.location.northNeighbor
    } else if (direction == "east") {
      destination = this.location.eastNeighbor
    } else if (direction == "south") {
      destination = this.location.southNeighbor
    } else if (direction == "west") {
      destination = this.location.westNeighbor
    }
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if (destination.isDefined) {
      println("You go " + direction + ".")
    } else {
      println("You can't go " + direction + ".")
    }
  }


  /** Causes the player to rest for a short while. (This has no substantial effect in game terms.)
    * Prints out a message stating that the player rests. */
  def rest() = {
    println("You rest for a while. Better get a move on, though.")
  }


  /** Signals that the player wants to quit the game. */
  def quit() = {
    this.quitCommandGiven = true
  }


  /** Returns a brief description of the player's state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name


}
