

import o1._

// This assignment from Chapter 6.2 is completely optional.

object Task10 extends App {

  def blur(pic: Pic): Pic = {
    ??? // TODO: implement this
  }

  // Implement the blur function above so that it takes in a Pic and returns a
  // blurred version of it. The blurred version is formed by transforming each
  // pixel in the given Pic as follows:
  //
  //    1) Find out the pixel's "neighborhood": the pixel itself and
  //       those pixels that are adjacent to it. Diagonally adjacent
  //       counts, so a pixel's neighborhood will consist of up to nine
  //       pixels in total. (The neighborhood of a border pixel is smaller.)
  //
  //    2) Compute the redness of the neighborhood: the average of those
  //       pixels' red components. Do the same for the green component and
  //       the blue component.
  //
  //    3) That produced three values: use those as the blurred pixel's
  //       red, green, and blue components, respectively.
  //
  //  When your function works, the following code will display a photo
  //  side-by-side with a slightly blurred version.

  val originalPic = Pic("kid.png")
  val blurredPic = blur(originalPic)
  originalPic.leftOf(blurredPic).show()

}


