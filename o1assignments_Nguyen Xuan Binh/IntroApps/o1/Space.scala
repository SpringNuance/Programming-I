package o1

// This code is introduced in Chapters 2.6 and 2.7.

class CelestialBody(val name: String, val radius: Double, var location: Pos) {
  def diameter = this.radius * 2
  override def toString = this.name
}

class AreaInSpace(size: Int) {
  val width = size * 2
  val height = size
  val earth = new CelestialBody("The Earth", 15.9, new Pos(10,  this.height / 2))
  val moon  = new CelestialBody("The Moon",   4.3, new Pos(971, this.height / 2))
  override def toString = s"${this.width}-by-${this.height} area in space"
}


