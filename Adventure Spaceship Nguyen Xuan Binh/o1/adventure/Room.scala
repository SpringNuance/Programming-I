package o1.adventure

import scala.collection.mutable.Map

/** The class `room` represents locations in a text adventure game world.
  * @param name         the name of the room
  */
class Room(var name: String) {

  private val neighbors = Map[String, Room]()
  var bulletsPack = Vector[Bullets]()
  var aliens = Vector[Alien]()
  var potionsPack = Vector[Potion]()
  var repaired = false

  /** Returns the room that can be reached from this area by moving in the given direction. The result
    * is returned in an `Option`; `None` is returned if there is no exit in the given direction. */
  def neighbor(direction: String) = this.neighbors.get(direction)


  /** Adds exits from this room to the given rooms. */
  def setNeighbors(exits: Vector[(String, Room)]) = {
    this.neighbors ++= exits
  }


  /** Returns the exits of a room in a 3D spaceship as the player face always face towards the control room*/
  def exitDescription = "\nExits available: " + this.neighbors.keys.mkString(" ")


  /** Status of the aliens in the current room */
  def alienDescription = {
      if (aliens.isEmpty) s"No aliens in this room\n"
      else if (hasWhatAliveAliens.isEmpty) s"All aliens in this room have been killed\n"
      else s"There are alive aliens in this room:\n${hasWhatAliveAliens.mkString("\n")}\n"
}
  /** Status of bullets in the current room */
  def bulletsDescription = {
    if (bulletsPack.isEmpty || bulletsPack.forall(_.numberBullets == 0)) s"No bullets in this room\n" else s"Yay! $totalBullets bullets in this room\n"
  }

  /** Status of bullets in the current room */
  def potionDescription = {
    if (potionsPack.isEmpty || potionsPack.forall(_.revive == 0)) s"No potions in this room\n" else s"Yay! $totalPotions potions in this room\n"
  }

  /** Status of the room being intact/damaged/repaired */
  def statusDescription = {
    if (this.aliens.isEmpty) s"This room is intact"
    else if (!this.repaired) s"This room has been damaged by aliens"
    else s"this room has been repaired"
  }

  /** Full status of the current room */
  def fullDescription = "Status of " + this + ":\n" + statusDescription + "\n" + alienDescription + bulletsDescription + potionDescription + exitDescription

  /** Adding aliens to the room */
  def addAlien(alien: Alien): Unit = {
     this.aliens = this.aliens :+ alien
  }

  def addAliens(aliens: Vector[Alien]) = {
     this.aliens = this.aliens ++ aliens
  }

  /** Adding bullets to the room */
  def addBullet(bullet: Bullets) = {
     this.bulletsPack = this.bulletsPack :+ bullet
  }

  def addBullets(bullets: Vector[Bullets]) = {
      this.bulletsPack = this.bulletsPack ++ bullets
  }

  /** Adding potions to the room */
  def addPotion(potion: Potion) = {
    this.potionsPack = this.potionsPack :+ potion
  }

  def addPotions(potions: Vector[Potion]) = {
    this.potionsPack = this.potionsPack ++ potions
  }

  /** The aliens in this room, both dead and alive */
  def hasWhatAliens = aliens

  /** The alive aliens in this room */
  def hasWhatAliveAliens = aliens.filter(_.isAlive)

  /** The total health of alive aliens, if there are any alive ones */
  def allHealthOfAliveAliens = {
    var totalHealth = 0
    for (alien <- hasWhatAliveAliens) {
      totalHealth = totalHealth + alien.nowHealth
    }
    totalHealth
  }

  /** Bullets count of the room */
  def totalBullets = {
    var count = 0
    if (containsBullet) {
    for (bullet <- bulletsPack){
      count = count + bullet.numberBullets
    }
  }
    count
  }

  /** Potions count of the room */
  def totalPotions = {
     var count = 0
    if (containsPotion) {
    for (potion <- potionsPack){
      count = count + potion.revive
    }
  }
    count
  }

  /** Checks whether this room has bullets */
  def containsBullet = this.bulletsPack.nonEmpty

  /** Checks whether this room has potions */
  def containsPotion = this.potionsPack.nonEmpty

  /** Checks whether this room has any alive aliens */
  def containsAnyAliveAlien: Boolean = {
    if (this.aliens.isEmpty) false else if (this.aliens.forall(_.nowHealth == 0)) false else true
  }



  /** Returns a single-line description of the area for debugging purposes. */
  override def toString = this.name

}
