package o1.adventure
import scala.math._

/** A player is a protagonist in this game who shall extinguish all aliens and carry on the mission to Origae-6 */

class Player(startingRoom: Room) {

  private var currentLocation = startingRoom        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  var bulletsOwned: Int = 100
  var health = 10

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven


  /** Returns the current location of the player. */
  def location = this.currentLocation

  def isAlive = health > 0

  def isDead = !isAlive

  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player's current location towards the direction name.
    * There's a catch: if the room you are about to leave has alive aliens, you have 3 options:
    * kill all the aliens by shooting them and you can get out of the current room unharmed.
    * If you doesnt have any bullets, you cant avoid escaping the room without losing health by attacks from alive aliens
    * If you does have bullets, but if you forget to shoot and left the room, your health will also be damaged
    * If you does have bullets, but if you have shot all bullets and some of the aliens are still alive, your health will also be damaged when you leave the room
    * So it means that you can only escape a room unharmed if it doesnt have any aliens or has all dead aliens.
    * */
  def go(direction: String) = {
    val destination = this.location.neighbor(direction)
    var lastLocation = this.currentLocation
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if (destination.isDefined) {
      if (lastLocation.hasWhatAliveAliens.nonEmpty) {
         health = health - lastLocation.hasWhatAliveAliens.size
         if (health < 0) health = 0
      "You managed to go " + direction + s" but were attacked by some alive aliens and losed ${lastLocation.hasWhatAliveAliens.size} health"
      }
      else {
        "You go " + direction + " safely."
      }
    } else "You can't go " + direction + "."
  }



  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() = {
    this.quitCommandGiven = true
    ""
  }

  /** shoot method indicates player use gun to kill aliens.
    * Shoot method compares two values: the number of bullets the player has and the total health of all alive aliens in the room.
    * Each bullet equals to one health of each alive alien. When the shoot method is called, the player must spend all the bullets to kill
    * all the aliens. If the total health of all alive aliens were less than the number of bullets the player carry, all aliens becomes "dead"
    * and the remaining bullets the player has equal to the number of bullets before entering the room minus the total health of alive aliens.
    * If the number of bullets was less than the total health of alive aliens, the player is supposed to shoot at each alien consecutively, not
    * shooting equally each alien. It means a player must focus on shooting on one alien until it is dead before continue to the next alien
    * Therefore, when the bullets run out, some aliens may remain unharmed or partially harmed. Of course, the player will inevitably lose health
    * to escape the room as there are still alive aliens. The number of health lose is equal to the number of alive aliens left in the room
  */
  def shoot = {
    if (this.location.aliens.isEmpty) s"There are no aliens here for you to kill"
      else if (this.location.containsAnyAliveAlien) {
        if (bulletsOwned == 0) {
          var size = this.location.hasWhatAliveAliens.size
          s"Oops! You have run out of bullets. You cannot avoid losing $size health after escaping this room"
        }
          else if (this.location.allHealthOfAliveAliens <= this.bulletsOwned) {
            bulletsOwned = bulletsOwned - this.location.allHealthOfAliveAliens
            this.location.hasWhatAliens.foreach(_.nowHealth = 0)
            s"You have successfully killed all aliens in this room"
        } else {
          var bulletsReduced = bulletsOwned
          var counter = 0
          while (bulletsReduced > 0) {
            if (bulletsReduced >= this.location.aliens(counter).nowHealth) {
              bulletsReduced = bulletsReduced - this.location.aliens(counter).nowHealth
              this.location.aliens(counter).nowHealth = 0
              counter += 1
            } else {
              this.location.aliens(counter).nowHealth = this.location.aliens(counter).nowHealth - bulletsReduced
              bulletsReduced = 0
            }
          }
          bulletsOwned = 0
          var size = this.location.hasWhatAliveAliens.size
           s"You have shot all your bullets but unfortunately some of the aliens are still alive. You cannot avoid losing $size health after escaping this room"
        }
    } else s"All the aliens have been killed already. You need not shoot at dead aliens"
  }

  /** help player use items: either charges bullets or gain some health */
  def use(item: String): String = item match {
    case "bullets" =>
      if (this.location.containsBullet && this.location.bulletsPack.exists(_.numberBullets > 0)) {
        for (x <- this.location.bulletsPack) {
           this.bulletsOwned = this.bulletsOwned + x.numberBullets
           x.numberBullets = 0
        }
        s"You have collected all the bullets available in the room and charged them into your gun"
      } else  s"There are no bullets here to collect."
    case "potions" =>
      if (this.location.containsPotion &&  this.location.potionsPack.exists(_.revive > 0)) {
        for (x <- this.location.potionsPack) {
          this.health = this.health + x.revive
          x.revive = 0
        }
        s"You have regained some health by drinking the potion"
      } else s"There are no potions here for you to heal"
  }

  /** repairing the damaged rooms that have aliens. All rooms must be either intact or repaired so the ship can continue to fly */
  def repair: String = {
    if (this.location.aliens.isEmpty) s"You don't have to fix this room. It is intact"
    else if (this.location.containsAnyAliveAlien) s"You cannot fix the room before killing all aliens"
    else {
      if (!this.location.repaired) {
      this.location.repaired = true
      s"You fix the room successfully"
      } else s"You have already fixed the room"
    }
  }

  def help: String = s"Tutorial: \nType go front, go back, go left, go right, go up, go down to move between rooms in the spaceship\nType quit to end the game\nType use + items to use an item\nType use bullets to collect all the bullets in the current room and charge into your gun\nType use potions to collect the potions to gain some health\nType shoot to spend enough bullets to kill all aliens, or spend all bullets if the total health of alien is more than the number of bullets\nType repair to fix rooms that have infiltrating aliens after killing all of them so the ship can fly again. You need not fix rooms without aliens\nYou will win game if: You find all aliens and kill them all, have at least 1 health, repair all rooms damaged by aliens and return to the control Room before time runs out\nYou will lose game if: You lose all health or runs out of time or simply quit\nNotes: For debugging purposes, here are the locations that I place\nAliens: on 8 corners of the 3x3 cube and one group of alien in the central position of the cube (9 rooms have aliens)\nPotions: in 6 center positions of each face of cube's 6 face (6 rooms have potions)\nBullets: on all other locations (not edge and not center of each face of the cube's 6 face) (12 rooms have bullets)\n9 + 6 + 12 = 27 = 3 x 3 cube\nThe game takes approximately 5-15 minutes to finish"

  /** Current status of the player: bullets, health, location */
  def fullDescription = s"\nYou currently has $bulletsOwned bullets in your gun and $health health unit(s) left.\nYou are standing in ${this.location}\n"


  /** Returns a brief description of the player's state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name



}


