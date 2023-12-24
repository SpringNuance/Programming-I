package o1.llama

import scala.collection.immutable.SortedMap

// Note to students: What you need to know about this class is said in Chapter 12.3 of the ebook.

/** A `Llama` is a mutable object that has a reducing amount of patience left.
  * Curiously, it knows English. */
class Llama {

  private val comments = SortedMap(  0 -> "Ow.",
                                   100 -> "Watch out! I'm going to blow!",
                                   250 -> "Holding it together but furious on the inside.",
                                   500 -> "I'm about to call the police.",
                                   750 -> "Stop that at once!",
                                   900 -> "You're annoying.")
  private val originalPatience = 1000
  private var remainingPatience = originalPatience;   // stepper


  /** Returns what the llama thinks of the world and the program user. */
  def stateOfMind = {
    val comment = this.comments.find( this.remainingPatience <= _._1 ).map( _._2 ).getOrElse("I love being a llama.")
    f"(Patience left: ${patienceLevel * 100}%.0f)       $comment"
  }


  /** Greatly reduces the llama's patience. */
  def slap() = {
    this.reducePatience(200)
  }


  /** Substantially reduces the llama's patience. */
  def poke() = {
    this.reducePatience(50)
  }


  /** Reduces the llama's patience somewhat. */
  def scratch() = {
    this.reducePatience(5)
  }


  /** Reduces the llama's patience by the given amount. */
  private def reducePatience(howMuch: Int) = {
    this.remainingPatience = (this.remainingPatience - howMuch).max(0)
  }


  /** Returns the patience level of the llama as a percentage between 0 and 1. */
  def patienceLevel = this.remainingPatience.toDouble / this.originalPatience


  /** Determines whether the llama's patience has entirely run out. */
  def isOutOfPatience = this.remainingPatience == 0


}
