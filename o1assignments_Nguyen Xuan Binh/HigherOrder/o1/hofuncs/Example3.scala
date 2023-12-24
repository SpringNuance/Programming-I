package o1.hofuncs

// This example is introduced in Chapter 6.1.

import o1._

object Example3 extends App {

  def swapGreenAndBlue(original: Color) = Color(original.red, original.blue, original.green)

  val originalPic = Pic("defense.png")
  val manipulatedPic = originalPic.transformColors(swapGreenAndBlue)
  originalPic.leftOf(manipulatedPic).show()

}

