package o1.legal

abstract class JuridicalPerson(name: String) extends Entity(name)

class Group(val members: Vector[Entity]) extends JuridicalPerson("group"){
  val leader: Entity = members.head
  def contact: NaturalPerson = leader.contact
  def kind: String = "group of " + members.size + " led by " + leader.name
}

class GeographicalFeature(name: String, val kind: String, val representative: Entity) extends JuridicalPerson(name) {
  def contact: NaturalPerson = representative.contact

}

abstract class HumanOrganization(name: String, contact1: NaturalPerson) extends JuridicalPerson(name) {
  val contact = this.contact1
  def kind: String
  override def toString = this.name + "(" + kind + ")"
}
// TODO: define classes JuridicalPerson, HumanOrganization, GeographicalFeature, and Group.

class Nation(name: String, contact: NaturalPerson) extends HumanOrganization(name, contact) {
  def kind = "sovereign nation"

}

class Municipality(name: String, val nation: Nation, contact: NaturalPerson) extends HumanOrganization(name, contact) {
  def kind = "municipality of " + this.nation.name
}


class Corporation(val id: String, val seeksProfit: Boolean, name: String, contact: NaturalPerson) extends HumanOrganization(name, contact) {
  def kind = (if (this.seeksProfit) "for-" else "non-") + "profit corporation"
}

