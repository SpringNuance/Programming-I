package o1.viinaharava

import o1.GridPos

/** The class `Glass` represents glasses in a game of Viinaharava. A glass object
  * is mutable: it is initially full of water but can be modified to contain booze
  * instead and can be emptied.
  * @param board     the game board that the glass is located on
  * @param location  the location of the glass on the game board */
class Glass(val board: GameBoard, val location: GridPos) {

  private var isFull = true
  private var isWater = true
  private var danger = 0


  /** Determines whether the glass is empty. */
  def isEmpty = !this.isFull


  /** Determines whether the glass is a glass of booze. A glass counts as a booze class
    * even if the booze has already been drunk. */
  def isBooze = !this.isWater


  /** Returns the number of neighboring booze glasses (diagonals included).
    *
    * Note to students: This method only returns the danger level of the glass.
    * It does NOT change the level; that is [[pourBooze]]’s job. */
  def dangerLevel = this.danger


  /** Determines whether the glass has a [[dangerLevel]] of zero. */
  def isSafe = this.danger == 0


  /** Returns this glass's neighboring glasses (diagonals included) on the game board. */
  def neighbors: Vector[Glass] = board.neighbors(this.location,true)
  // TODO: implement this method


  /** If the glass is a water glass (full or empty), turns it into a full booze glass.
    * This raises the danger levels of neighboring glasses. If the glass was a booze
    * glass (full or empty) to begin with, this method does nothing.
    * @see [[dangerLevel]] */
  def pourBooze() = {
    if (this.isWater){
      this.isWater = false
      this.isFull = true
      this.neighbors.map(_.danger += 1)
    }

  }
  // TODO: implementation missing

  /** Empties the glass. This just means that the glass is now empty instead of full.
    * (If the glass was already empty, this method does nothing but return `false`.)
    * @return `true` if the glass was full, `false` if it wasn’t */
  def empty(): Boolean = {
    val wasFull = this.isFull
    this.isFull = false
    wasFull
  }
}
