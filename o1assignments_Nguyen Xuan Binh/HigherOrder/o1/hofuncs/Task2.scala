package o1.hofuncs

// This program is introduced in Chapter 6.1.

import o1._

object Task2 extends App {

  def toEliminateGreen(color: Color) = {
    var Color1 = Color(color.red*10, 0, color.blue)
    Color1
  }
  // FIRST, READ THE INTRODUCTION TO THIS TASK IN THE EBOOK CHAPTER.
  // Then reveal the hidden image:
  //
  // 1. Define a function for revealing a single pixel. Pick a name for it.
  //    It should take a Color and return a Color that is equal to the given
  //    one except that the green component is zero. This will eliminate the
  //    green noise.
  //
  // 2. Use transformColors apply your function to every pixel in
  //    scrambledPic. (The appropriate line of code is indicated below.)
  //
  // 3. Run the program. It displays the scrambled image. Click it.
  //    A dark black rectangle should show up. This is the output of your
  //    (incomplete) filter. The resulting image is black because all that
  //    remains are the red components of each pixel and they have very small
  //    values. However, if you peer closely, you may be able to see some red
  //    tones, perhaps even the outline of a person near the middle of the image.
  //
  // 3. Amplify the red component: modify your function so that it multiplies
  //    the red component by ten. Run the program again and voilà!




  val scrambledPic = Pic("hidden1.png")
  val solvedPic = scrambledPic.transformColors(toEliminateGreen)
  launchUnscramblerGUI(scrambledPic, solvedPic)
}

// The hidden image is based on a photo by Duane Wessels, published under CC (http://creativecommons.org/licenses/by-nc-sa/3.0/). This program is in the public domain.
