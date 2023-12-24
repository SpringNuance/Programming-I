package o1.adventure.draft

import scala.io.StdIn._

/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * '''NOTE: The AdventureDraft module is not even close to being well designed.
  * See Chapter 9.1 in the course materials.''' */
class Adventure {

  val title = "A Forest Adventure"

  val middle      = new Area("Forest", "You are somewhere in the forest. There are a lot of trees here.\nBirds are singing.")
  val northForest = new Area("Forest", "You are somewhere in the forest. A tangle of bushes blocks further passage north.\nBirds are singing.")
  val southForest = new Area("Forest", "The forest just goes on and on.")
  val clearing    = new Area("Forest Clearing", "You are at a small clearing in the middle of forest.\nNearly invisible, twisted paths lead in many directions.")
  val tangle      = new Area("Tangle of Bushes", "You are in a dense tangle of bushes. It's hard to see exactly where you're going.")
  val home        = new Area("Home", "Home sweet home!")
  val destination = home

  middle.northNeighbor = Some(northForest)
  middle.eastNeighbor  = Some(tangle)
  middle.southNeighbor = Some(southForest)
  middle.westNeighbor  = Some(clearing)

  northForest.eastNeighbor  = Some(tangle)
  northForest.southNeighbor = Some(middle)
  northForest.westNeighbor  = Some(clearing)

  southForest.northNeighbor = Some(middle)
  southForest.eastNeighbor  = Some(tangle)
  southForest.southNeighbor = Some(southForest)
  southForest.westNeighbor  = Some(clearing)

  clearing.northNeighbor = Some(northForest)
  clearing.eastNeighbor  = Some(middle)
  clearing.southNeighbor = Some(southForest)
  clearing.westNeighbor  = Some(northForest)

  tangle.northNeighbor = Some(northForest)
  tangle.eastNeighbor  = Some(home)
  tangle.southNeighbor = Some(southForest)
  tangle.westNeighbor  = Some(northForest)

  home.westNeighbor = Some(tangle)

  /** The character who is the protagonist of the adventure and whom the real-life player controls. */
  val player = new Player(middle)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 40


  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = this.player.location == this.destination


  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit


  /** Prints out a description of the player character's current location, as seen by the character. */
  def printAreaInfo() = {
    val area = this.player.location
    println("\n\n" + area.name)
    println("-" * area.name.length)
    println(area.description)
    print("\nExits available:")
    if (area.northNeighbor.isDefined) {
      print(" north")
    }
    if (area.eastNeighbor.isDefined) {
      print(" east")
    }
    if (area.southNeighbor.isDefined) {
      print(" south")
    }
    if (area.westNeighbor.isDefined) {
      print(" west")
    }
    println()
    println()
  }


  /** Prints out a message that is to be displayed to the player at the beginning of the game. */
  def printWelcome() = {
    println("You are lost in the woods. Find your way back home.\n")
    println("Better hurry, 'cause Scalatut elämät is on real soon now. And you can't miss Scalkkarit, right?")
  }


  /** Prints out a message that is to be displayed to the player at the end of the game.
    * The message will be different depending on whether or not the player has completed the quest. */
  def printGoodbye() = {
    println()
    if (this.isComplete) {
      println("Home at last... and phew, just in time! Well done!")
    } else if (this.turnCount == this.timeLimit) {
      println("Oh no! Time's up. Starved of entertainment, you collapse and weep like a child.\nGame over!")
    } else { // game over due to player quitting
      println("Quitter!")
    }
  }


  /** Runs the game. First, a welcome message is printed, then the player gets the chance
    * to play any number of turns until the game is over, and finally a goodbye message is printed. */
  def run() = {
    this.printWelcome()
    while (!this.isOver) {
      this.printAreaInfo()
      this.playTurn()
    }
    this.printGoodbye()
  }


  /** Requests a command from the player, plays a game turn accordingly,
    * and prints out a report of what happened. */
  def playTurn() = {
    println()
    val command = readLine("Command: ")
    val action = new Action(command)
    if (action.execute(this.player)) {
      this.turnCount += 1
    } else {
      println("Unknown command: \"" + command + "\".")
    }
  }

}