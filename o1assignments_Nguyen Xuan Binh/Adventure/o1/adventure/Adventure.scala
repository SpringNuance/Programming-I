package o1.adventure


class Adventure {

/** The title of the adventure game. */
  val title = "A Spaceship Inavded by Aliens Game"

  private val AX1 = new Room("AX1")
  private val AX2 = new Room("AX2")
  private val AX3 = new Room("AX3")
  private val AY1 = new Room("AY1")
  private val AY2 = new Room("AY2")
  private val AY3 = new Room("AY3")
  private val AZ1 = new Room("AZ1")
  private val AZ2 = new Room("AZ2")
  private val AZ3 = new Room("AZ3")

  private val BX1 = new Room("BX1")
  private val BX2 = new Room("BX2")
  private val BX3 = new Room("BX3")
  private val BY1 = new Room("BY1")
  private val BY2 = new Room("BY2")
  private val BY3 = new Room("BY3")
  private val BZ1 = new Room("BZ1")
  private val BZ2 = new Room("BZ2")
  private val BZ3 = new Room("BZ3")

  private val CX1 = new Room("CX1")
  private val CX2 = new Room("CX2")
  private val CX3 = new Room("CX3")
  private val CY1 = new Room("CY1")
  private val CY2 = new Room("CY2")
  private val CY3 = new Room("CY3")
  private val CZ1 = new Room("CZ1")
  private val CZ2 = new Room("CZ2")
  private val CZ3 = new Room("CZ3")

  private val controlRoom = new Room("Control Room")
  private val destination = controlRoom

  private val allLocations = Vector(AX1,AX2,AX3,AY1,AY2,AY3,AZ1,AZ2,AZ3,BX1,BX2,BX3,BY1,BY2,BY3,BZ1,BZ2,BZ3,CX1,CX2,CX3,CY1,CY2,CY3,CZ1,CZ2,CZ3)
  /** Assuming the person always turn their face to the control Room direction no matter what room they go to, these are the relative neighbors of the room
    * All Rooms having A in its name has no front rooms, except AY1 whose front room is the Control Room
    * All Rooms having C in its name has no back rooms
    * All Rooms having X in its name has no left rooms
    * All Rooms having Z in its name has no right rooms
    * All Rooms having 1 in its name has no down rooms
    * All Rooms having 3 in its name has no up rooms
    */

  AX1.setNeighbors(Vector(                        "back" -> BX1, "right" -> AY1,                "up" -> AX2               ))
  AX2.setNeighbors(Vector(                        "back" -> BX2, "right" -> AY2,                "up" -> AX3, "down" -> AX1))
  AX3.setNeighbors(Vector(                        "back" -> BX3, "right" -> AY3,                             "down" -> AX2))
  AY1.setNeighbors(Vector("front" -> controlRoom, "back" -> BY1, "right" -> AZ1, "left" -> AX1, "up" -> AY2               ))
  AY2.setNeighbors(Vector(                        "back" -> BY2, "right" -> AZ2, "left" -> AX2, "up" -> AY3, "down" -> AY1))
  AY3.setNeighbors(Vector(                        "back" -> BY3, "right" -> AZ3, "left" -> AX3,              "down" -> AY2))
  AZ1.setNeighbors(Vector(                        "back" -> BZ1,                 "left" -> AY1, "up" -> AZ2               ))
  AZ2.setNeighbors(Vector(                        "back" -> BZ2,                 "left" -> AY2, "up" -> AZ3, "down" -> AZ1))
  AZ3.setNeighbors(Vector(                        "back" -> BZ3,                 "left" -> AY3,              "down" -> AZ2))

  BX1.setNeighbors(Vector("front" -> AX1,         "back" -> CX1, "right" -> BY1,                "up" -> BX2               ))
  BX2.setNeighbors(Vector("front" -> AX2,         "back" -> CX2, "right" -> BY2,                "up" -> BX3, "down" -> BX1))
  BX3.setNeighbors(Vector("front" -> AX3,         "back" -> CX3, "right" -> BY3,                             "down" -> BX2))
  BY1.setNeighbors(Vector("front" -> AY1,         "back" -> CY1, "right" -> BZ1, "left" -> BX1, "up" -> BY2               ))
  BY2.setNeighbors(Vector("front" -> AY2,         "back" -> CY2, "right" -> BZ2, "left" -> BX2, "up" -> BY3, "down" -> BY1))
  BY3.setNeighbors(Vector("front" -> AY3,         "back" -> CY3, "right" -> BZ3, "left" -> BX3,              "down" -> BY2))
  BZ1.setNeighbors(Vector("front" -> AZ1,         "back" -> CZ1,                 "left" -> BY1, "up" -> BZ2               ))
  BZ2.setNeighbors(Vector("front" -> AZ2,         "back" -> CZ2,                 "left" -> BY2, "up" -> BZ3, "down" -> BZ1))
  BZ3.setNeighbors(Vector("front" -> AZ3,         "back" -> CZ3,                 "left" -> BY3,              "down" -> BZ2))

  CX1.setNeighbors(Vector("front" -> BX1,                        "right" -> CY1,                "up" -> CX2               ))
  CX2.setNeighbors(Vector("front" -> BX2,                        "right" -> CY2,                "up" -> CX3, "down" -> CX1))
  CX3.setNeighbors(Vector("front" -> BX3,                        "right" -> CY3,                             "down" -> CX2))
  CY1.setNeighbors(Vector("front" -> BY1,                        "right" -> CZ1, "left" -> CX1, "up" -> CY2               ))
  CY2.setNeighbors(Vector("front" -> BY2,                        "right" -> CZ2, "left" -> CX2, "up" -> CY3, "down" -> CY1))
  CY3.setNeighbors(Vector("front" -> BY3,                        "right" -> CZ3, "left" -> CX3,              "down" -> CY2))
  CZ1.setNeighbors(Vector("front" -> BZ1,                                        "left" -> CY1, "up" -> CZ2               ))
  CZ2.setNeighbors(Vector("front" -> BZ2,                                        "left" -> CY2, "up" -> CZ3, "down" -> CZ1))
  CZ3.setNeighbors(Vector("front" -> BZ2,                                        "left" -> CY3,              "down" -> CZ2))

  controlRoom.setNeighbors(Vector("back" -> AY1))

  /** Adding aliens to random rooms */
  AX1.addAliens(Vector(new Alien("Aayala", "full health at 11", 11), new Alien ("Ondine", "full health at 29",29), new Alien("Xenomorph", "full health at 38", 38)))
  AZ1.addAliens(Vector(new Alien("Dejah", "full Health at 12", 12), new Alien("Gibet", "full health at 28", 28), new Alien("Jixx", "full health at 36", 36)))
  CX1.addAliens(Vector(new Alien("Garrus", "full Health at 13", 13), new Alien("Scarbo", "full health at 27", 27), new Alien("Qeapi", "full health at 34", 34)))
  CZ1.addAliens(Vector(new Alien("Jubal", "full Health at 14", 14), new Alien("Xiqi", "full health at 26", 26), new Alien("Kappa", "full health at 32", 32)))
  AX3.addAliens(Vector(new Alien("Kira", "full Health at 15", 15), new Alien("Lafahet", "full health at 25", 25), new Alien("Ongke", "full health at 31", 31)))
  AZ3.addAliens(Vector(new Alien("Makkan", "full Health at 16", 16), new Alien("Monolith", "full health at 24", 24), new Alien("Acquy", "full health at 33", 33)))
  CX3.addAliens(Vector(new Alien("Padme", "full Health at 17", 17), new Alien("Behemoth", "full health at 23", 23), new Alien("Honma", "full health at 35", 35)))
  CZ3.addAliens(Vector(new Alien("Vito", "full Health at 18", 18), new Alien("Seddah", "full health at 22", 22), new Alien("Xacsong", "full health at 37", 37)))
  BY2.addAliens(Vector(new Alien("Zorg", "full Health at 19", 19), new Alien("Yasuf", "full Health at 21", 21), new Alien("Quaivat", "full health at 39", 39)))

  private val allAlienLocations = Vector(AX1,AZ1,CX1,CZ1,AX3,AZ3,CX3,CZ3,BY2)

  /** Adding bullets to random rooms */
  AY1.addBullet(new Bullets(200))
  AY3.addBullet(new Bullets(79))
  AX2.addBullet(new Bullets(68))
  AZ2.addBullet(new Bullets(65))
  BX1.addBullet(new Bullets(75))
  BZ1.addBullet(new Bullets(72))
  BX3.addBullet(new Bullets(73))
  BZ3.addBullet(new Bullets(86))
  CY1.addBullet(new Bullets(62))
  CY3.addBullet(new Bullets(50))
  CX2.addBullet(new Bullets(66))
  CZ2.addBullet(new Bullets(81))


  /** Adding potions to random rooms */
  AY2.addPotion(new Potion(12))
  CY2.addPotion(new Potion(13))
  BX2.addPotion(new Potion(9))
  BZ2.addPotion(new Potion(8))
  BY1.addPotion(new Potion(11))
  BY3.addPotion(new Potion(10))


  /** The character that the player controls in the game. */
  val player = new Player(controlRoom)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this spaceship game allows before the aliens destroy the spaceship. */
  val timeLimit = 300

  def timeCount = s"Minutes passed: $turnCount. Time limit: $timeLimit minutes"
  /** Determines if the adventure is complete, that is, if the player has won.
    * The player wins when he has killed all aliens, still have at least one unit of health, fix all rooms damaged by aliens and return to the control room on time
    * */
  def isWinning = (this.player.location == controlRoom) && !(allLocations.exists(_.containsAnyAliveAlien)) && (this.player.isAlive) && (this.turnCount < this.timeLimit) && (allAlienLocations.forall(_.repaired))

  /** Determines whether the player has won, lost, or quit, thereby ending the game.
    * The player loses when he quits the game, lose all health or runs out of time
    * */
  def isLosing = this.player.hasQuit || this.turnCount == this.timeLimit || (this.player.isDead)

  /** Helps player know why they lose */
  def losingReason = if (this.player.hasQuit) s"You have given up"
     else if (this.turnCount == this.timeLimit) s"You have run out of time"
       else if (this.player.isDead) s"You have run out of health"
  else ""

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "A team of scientists are on The Covenant spaceship transporting humanity to settle on the planet Origae-6. After leaving a strange exoplanet, the aliens have infiltrated the ship into many rooms. You must kill all the aliens before they totally wrecks the spaceship!"


  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage = {
    if (this.isWinning)
      "You have saved The Covenant and eliminated all extraterrestrial threats! The bright future awaits humanity at Origae-6"
    else losingReason + "\nYou have failed the mission to protect The Covenant. The aliens have destroyed the ship and humanity perished before reaching Origae-6"
  }


  /** Plays a turn by executing the given in-game command, such as "go west". Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */


  def playTurn(command: String) = {
    val action = new Action(command)
    val outcomeReport = action.execute(this.player)
    if (outcomeReport.isDefined) {
      this.turnCount += 1
    }
    outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
  }

}

