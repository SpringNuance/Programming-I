package o1.adventure.draft


/** The class `Action` represents actions that a player may take in a text adventure game.
  * `Action` objects are constructed on the basis of textual commands and are, in effect,
  * parsers for such commands. An action object is immutable after creation.
  * @param input  a textual in-game command such as "go east" or "rest" */
class Action(input: String) {

  private val command   = input.trim.toLowerCase
  private val verb      = command.takeWhile( _ != ' ' )
  private val modifiers = command.drop(verb.length).trim


  /** Causes the given player to take the action represented by this object, assuming
    * that the command was understood. Returns a description of what happened as a result
    * of the action (such as "You go west."). The description is returned in an `Option`;
    * if the command was not recognized, `None` is returned. */
  def execute(actor: Player) = {
    if (this.verb == "go") {
      actor.go(this.modifiers)
      true
    } else if (this.verb == "rest") {
      actor.rest()
      true
    } else if (this.verb == "quit") {
      actor.quit()
      true
    } else {
      false
    }
  }


  /** Returns a textual description of the action object, for debugging purposes. */
  override def toString = this.verb + " (modifiers: " + this.modifiers + ")"

}
