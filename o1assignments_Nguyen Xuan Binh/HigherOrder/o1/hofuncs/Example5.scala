package o1.hofuncs

// These example functions are introduced in Chapter 6.1.

import scala.util.Random
import scala.collection.mutable.Buffer

object Example5 extends App {

  def next(number: Int) = number + 1
  def doubled(original: Int) = 2 * original

  println(Vector.tabulate(10)(doubled))
  println(Buffer.tabulate(15)(next))

  def parity(index: Int) = index % 2 == 0
  println(Vector.tabulate(5)(parity))
  println(Vector.tabulate(5)(parity).mkString("\t"))

  def randomElement(upperLimit: Int) = Random.nextInt(upperLimit + 1)
  val randomButAscending = Vector.tabulate(30)(randomElement)
  println(randomButAscending.mkString(","))

}

