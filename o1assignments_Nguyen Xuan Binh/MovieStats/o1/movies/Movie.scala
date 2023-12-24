package o1.movies

// This class is introduced in Chapter 9.2.

class Movie(val name: String, val year: Int, val position: Int, val rating: Double, val directors: Vector[String]) {
  override def toString = this.name
}
