package o1.legal


// TODO: define class Entity

abstract class Entity(val name: String) {
  def contact: NaturalPerson
  def kind: String
  override def toString = this.name + "(" + kind + ")"
}