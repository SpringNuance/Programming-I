package o1.peeveli

import GameState.Unrevealed


/** Each instance of class `GameState` represents a single state within the Peeveli variant of
  * Hangman: What does the (partially visible) target word look like to the guesser? How many
  * incorrect guesses can the guesser still make? Which guesses have already been made? Moreover,
  * our dishonest hangman needs an additional piece of information: Which words are still credible
  * solutions given the earlier guesses?
  *
  * Chapter 9.3 of the ebook has a detailed discussion of the internal logic of the Peeveli game.
  *
  * While a player plays a game of Peeveli, the game will move from one state to another. Even so,
  * each `GameState` object is immutable. Each successive state is represented by a new `GameState`
  * object, which is generated by calling the current state's `guessLetter` method.
  *
  * @param missesAllowed      the number of incorrect guesses that the guesser can still make before
  *                           losing the game. A negative number means that the game is over.
  * @param previousGuesses    a string that contains all the previously guessed characters in order
  * @param visibleWord        the version of the target word that is visible to the guesser. In a
  *                           state that represents the beginning of the game, this will consist
  *                           of unrevealed characters only (e.g., `"_____"`; see [[GameState Unrevealed]]).
  *                           In later states, more and more characters will be visible (e.g., `"C___O"`).
  * @param possibleSolutions  all the words in the game's vocabulary that match the `visibleWord`
  *                           parameter and are therefore plausible correct solutions */
class GameState(val missesAllowed: Int, val previousGuesses: String, val visibleWord: String, val possibleSolutions: Vector[String])  {
  private var missesAllow = this.missesAllowed
  private var lastGuess = this.previousGuesses
  private var seenWord = this.visibleWord
  private var possibleSols = this.possibleSolutions
  /** Creates a new `GameState` that represents the initial state of a new game of Peeveli.
    * All the letters of the target word are unrevealed.
    *
    * Note to students: This is an additional constructor for the class (see optional materials
    * in Chapter 4.1). You don’t need to use it.
    *
    * @param missesAllowed  the number of incorrect guesses the guesser is allowed to make
    * @param length         the number of characters in the target word that the guesser will look for
    * @param vocabulary     a collection of known words; all words of exactly `length` characters in
    *                       the vocabulary are potential target words */
  def this(missesAllowed: Int, length: Int, vocabulary: Vector[String]) = {
    this(missesAllowed, "", Unrevealed.toString * length, vocabulary.map( _.toUpperCase )) // This means: pass these parameters to the "default constructor" defined in the class header.
  }


  /** Returns the length of the target word. */
  def wordLength = this.visibleWord.length


  /** Returns the number of all known words that are (still) possible solutions to this
    * game of Peeveli, given the guesses that have already been made. */
  def numberOfSolutions = this.possibleSolutions.size


  /** Returns `true` if the player has missed with more guesses than allowed and has therefore
    * lost the game; returns `false` otherwise. */
  def isLost: Boolean = missesAllow < 0


  // TODO: replace with a proper implementation


  /** Returns `true` if the guesser has won the game, that is, if they haven't missed too many
    * times and all the letters in the target word are visible. Returns `false` otherwise. */
  def isWon: Boolean = missesAllow >= 0 && !seenWord.contains(Unrevealed)
  // TODO: replace with a proper implementation


  /** Returns a version of the currently visible target word so that additional characters are
    * revealed as indicated by the given pattern. For example, if `visibleWord` is `"C___O"`
    * and the pattern is `"__LL_"`, returns `"C_LLO"`. This method assumes that it receives
    * a parameter of equal length as the target word. */
  private def reveal(patternToReveal: String) = {
    var newVisibleWord = ""
    for (index <- patternToReveal.indices){
      if (patternToReveal(index) == Unrevealed) newVisibleWord = newVisibleWord + this.seenWord(index) else newVisibleWord = newVisibleWord + patternToReveal(index)
    }
   newVisibleWord.toUpperCase
  }
  // It’s a good idea to implement the private method reveal and use it in guessLetter.



  /** Returns a new `GameState` that follows this current one given that the guesser guesses a
    * particular letter. The rationale behind moving from one state to another is described in
    * Chapter 9.3 of the ebook.
    *
    * The next `GameState` will certainly have one more letter in [[previousGuesses]] than the
    * present one. In addition, it may have more visible letters in the target word, fewer missesm
    * allowed, and/or fewer potential solutions remaining.
    *
    * The player will always spend a missed solution attempt if the guess did not reveal any new
    * letters. This happens even if the player had already guessed the same letter before.
    *
    * @param guess  a guessed letter; this can be in either case but is always interpreted as an upper-case character
    * @return the state of the game after the newest guess */

  def guessLetter(guess: Char): GameState = {
    val realGuess = guess.toUpper

    def turnToUnrevealed(word: String):String = {
      var realWord = word
       for (index <- realWord.indices) {
         if (realWord(index)!= realGuess) {
           realWord = realWord.replace(realWord(index),'_')
       }
    }
      realWord
    }

    def positions(word: String): Vector[Int] = {
      var realWord = word
      var positionGuess = Vector[Int]()
      for (index <- realWord.indices){
         if (realWord(index) == realGuess) positionGuess= positionGuess :+ index
       }
      positionGuess
    }

    def largestGroup(vocab: Vector[String]) = {
      var y = vocab.groupBy(positions(_)).toVector
      var x = y.map(_._2).maxBy(_.size)
      x
  }
// .map(word => turnToUnrevealed(word) -> word)

    lastGuess = lastGuess + realGuess

    possibleSols = largestGroup(possibleSols)

    seenWord = reveal(turnToUnrevealed((possibleSols.head)))

    missesAllow = if (seenWord == visibleWord) {
      missesAllow -=1
      missesAllow
    } else missesAllowed

    new GameState(missesAllow, lastGuess, seenWord, possibleSols)

  }

  /** Returns a string description of the game state. */
  override def toString =
    this.visibleWord + ", " +
      "missed guesses allowed: " + this.missesAllowed + ", " +
      "guesses made: " + (if (this.previousGuesses.isEmpty) "none" else this.previousGuesses) + ", " +
      "solutions left: " + this.numberOfSolutions



}



/** This companion object just provides a single constant.
  * @see class [[GameState]] */
object GameState {

  /** the character that is used by Peeveli to signify unrevealed letters */
  val Unrevealed = '_'

}

   // TODO: remove the incorrect code below and write a proper implementation for the method
  // When you write literals, remember that whereas String literals go in double quotation marks as in "myString",
  // Char literals such as 'c' use single quotes.
  // Use groupBy