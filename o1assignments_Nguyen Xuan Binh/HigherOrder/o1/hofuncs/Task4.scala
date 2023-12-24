package o1.hofuncs

// This program is introduced in Chapter 6.1.

import o1._

object Task4 extends App {

  // Your task: Examine the function colorfulGradient (below). Generate
  // a new 350-by-350 image so that each pixel's value is determined by
  // colorfulGradient. Store the result in generatedPic.

  def colorfulGradient(x: Int, y: Int) = Color(x, y, (x + y) % (Color.Max + 1))

  val generatedPic: Pic = Pic.generate(350,350,colorfulGradient)
  generatedPic.show()

}

