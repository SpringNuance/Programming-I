package o1.hofuncs

// This example is introduced in Chapter 6.1.

import o1._

object Example4 extends App {

  val size = Color.Max + 1

  def blueGradient(x: Int, y: Int) = Color(0, 0, x.toDouble / (size - 1) * Color.Max)

  def artwork(x: Int, y: Int) =
    if (x * x > y * 100)  Red
    else if (x + y < 200) Black
    else if (y % 10 < 5)  Blue
    else                  White

  val pic1 = Pic.generate(size, size, blueGradient)
  val pic2 = Pic.generate(size, size * 2, artwork)

  (pic1 above pic2).show()

}

