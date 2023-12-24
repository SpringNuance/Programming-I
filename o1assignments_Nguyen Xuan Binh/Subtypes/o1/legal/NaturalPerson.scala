package o1.legal



// TODO: define classes NaturalPerson, FullCapacityPerson, and ReducedCapacityPerson

abstract class NaturalPerson(val personID: String, name: String) extends Entity(name) {
   def kind: String = "human"
}

class FullCapacityPerson(personID: String, name: String) extends NaturalPerson(personID, name) {
  def contact: FullCapacityPerson = this
  override def kind: String = super.kind + " in full capacity"
}

class ReducedCapacityPerson(personID: String, name: String, val restriction: Restriction, val guardian: FullCapacityPerson) extends NaturalPerson(personID, name) {
  def contact: FullCapacityPerson = guardian
  override def kind: String = super.kind + " with " + this.restriction
}
