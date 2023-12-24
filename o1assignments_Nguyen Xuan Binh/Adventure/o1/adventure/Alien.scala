package o1.adventure

/** Aliens have health. If they are dead, they are "safe". If they are still alive,
  * when you escape the room, they will "scratch" your body and you lose health. Type shoot to kill aliens */
class Alien(var name: String, var description: String, var health: Int) {
    def isDead = nowHealth == 0
    def isAlive = nowHealth > 0
    def currentHealth = nowHealth
    var nowHealth = health
    override def toString = s"${this.name}, ${this.description}, current health at $currentHealth"
}
