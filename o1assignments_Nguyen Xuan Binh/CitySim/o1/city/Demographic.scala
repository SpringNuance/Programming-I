package o1.city

import o1.Color


// TODO: define trait Demographic and turn this into a type hierarchy

sealed trait Demographic

final class Occupied(val label: Color) extends Demographic {
  override def toString = s"occupied by the $label demographic"
}


object Vacant extends Demographic {
  override def toString = "vacant residence"
}

