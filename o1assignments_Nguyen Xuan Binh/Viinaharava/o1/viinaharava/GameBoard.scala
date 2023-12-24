package o1.viinaharava

import o1._
import scala.util.Random


/** Each instance of the class `GameBoard` represents a board for a game of Viinaharava.
  * A game board is a `Grid` whose elements are `Glass` objects.
  *
  * A `GameBoard` is first initialized with water glasses (by [[initialElements]]). However,
  * a number of the glasses are then immediately poured full of booze while the rest keep their
  * default content of water.
  *
  * Once created, a `GameBoard` does not replace any of its `Glass` objects with another.
  * However, the state of each individual `Glass` may change.
  *
  * @param width       the number of glasses in each row of the game board
  * @param height      the number of rows of glasses in the game board
  * @param boozeCount  the number of booze glasses on the game board. When a `GameBoard` object
  *                    is created, it randomly chooses this many different locations on the
  *                    board and pours booze in them. */
class GameBoard(width: Int, height: Int, boozeCount: Int) extends Grid[Glass](width, height) {

  /** Generates the elements that initially occupy the grid. In the case of a `GameBoard` grid,
    * this means generating a new `Glass` object for each location on the board. This method is
    * automatically invoked by the superclass `Grid` to initialize the contents of the grid. */
  def initialElements = {
    val allLocations = (0 until this.size).map( n => GridPos(n % this.width, n / this.width) )
    allLocations.map( loc => new Glass(this, loc) )
  }

  this.placeBoozeAtRandom(boozeCount) // this call happens after the superclass has used initialElements to fill the board with water

  // Randomly selects `howMany` different glasses on the board and calls the pourBooze
  // method of each of those Glass objects.
  private def placeBoozeAtRandom(howMany: Int) = {
    Random.shuffle(this.allElements).take(howMany).map(_.pourBooze())
  }
 // TODO: implement this method

  /** Determines whether all the water on the board has been drunk already.
    * This is the case if and only if each of glasses either contains booze or is empty. */
  def isOutOfWater: Boolean = {
    var outOfWater = false
    if (this.allElements.filter(!_.isBooze).forall(_.isEmpty)) outOfWater = true
    outOfWater
  }

  // TODO: implement this method properly


  /** Virtually drinks the contents of the given glass.
    *
    * For a glass of water, “drinking” it just means that the glass is emptied.
    * (If the water glass was already empty, this method does nothing.)
    *
    * For a glass of booze, “drinking” it empties not just that glass but also all the
    * other glasses of booze on the game board. (This ends the game of Viinaharava and
    * all the locations of booze glasses are revealed as a result.)
    * @param target  a glass on this board */
  def drink(target: Glass): Unit = {
    if (target.isBooze) {
       boozeGlasses.map(_.empty())
    } else {
       if (target.isSafe && !target.isEmpty){
         target.empty()
         target.neighbors.foreach(drink(_))
    } else target.empty()
  }
}


  /** Returns a collection of all the booze glasses currently on the board. */
  def boozeGlasses = this.allElements.filter( _.isBooze )



}

