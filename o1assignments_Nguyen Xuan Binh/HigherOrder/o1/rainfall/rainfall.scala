package o1

package object rainfall {

  def averageRainfall(data: Vector[Int]) = {
    var realData = data.take(data.indexOf(999999)).filter(_>=0)
    if (realData.isEmpty) None else Some(realData.sum/realData.size)
  }

  def averageRainfallFromStrings(data: Vector[String]) = {
    var realData = data.take(data.indexOf("999999")).map(_.toIntOption).flatten.filter(_>=0)
    if (realData.isEmpty) None else Some(realData.sum/realData.size)
  }

  def drySpell(data: Vector[Int], length: Int) = data.sliding(length).toVector.indexWhere(_.forall(x => (x>=0 && x <=5)))


  // What goes here is described in Chapter 6.4.






}