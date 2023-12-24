package o1.adventure.draft.ui


/** The singleton object `AdventureTextUI` represents a fully text-based version of the
  * Adventure game application. The object serves as an entry point for the game,
  * and can be run to start up a user interface that operates in the text console.
  *
  * NOTE: The AdventureDraft module is not even close to being well designed.
  * See Chapter 9.1 in the course materials. */
object AdventureTextUI extends App {
  val game = new o1.adventure.draft.Adventure
  game.run()
}