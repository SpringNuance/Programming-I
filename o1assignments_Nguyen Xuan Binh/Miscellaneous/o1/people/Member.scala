package o1.people

class Member(val id: Int, val name: String, val yearOfBirth: Int, val yearOfDeath: Option[Int]) {
  def isAlive() = {
  yearOfDeath match {
    case Some(yearOfDeath) => false
    case None => true
  }
  }
  override def toString = {
    if (this.isAlive()) {
   s"$name($yearOfBirth-)"
    } else {
   s"$name($yearOfBirth-${yearOfDeath.getOrElse()})"
    }
  }
}
