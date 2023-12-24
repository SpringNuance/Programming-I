package o1.legal

// TODO: define class CourtCase

class CourtCase(val plaintiff: Entity, val defendant: Entity) {
  override def toString = s"${plaintiff.name} v. ${defendant.name}"
}