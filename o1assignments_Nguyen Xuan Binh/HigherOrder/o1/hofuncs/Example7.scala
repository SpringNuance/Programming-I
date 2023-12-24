package o1.hofuncs

// This example is introduced in Chapter 6.1.

import o1._

object Example7 extends App {

  def naiveAverage(color1: Color, color2: Color) =
    Color((color1.red   + color2.red)   / 2,
          (color1.green + color2.green) / 2,
          (color1.blue  + color2.blue)  / 2)

  val pic1 = Pic("lostgarden/tree-tall.png")
  val pic2 = Pic("lostgarden/girl-horn.png")
  val combinedPic = pic1.combine(pic2, naiveAverage)
  combinedPic.show()

}

