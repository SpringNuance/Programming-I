package o1.hofuncs

// This program is introduced in Chapter 6.1.

import o1._

object Task6 extends App {

  val original   = Pic("llama.png")
  val silhouette = rectangle(original.width, original.height, White).place(circle(140, Black), Pos(320, 165))

  // Your task: use the combine function and a helper function (which you can name)
  // to combine "original" and "silhouette" (above). The combination should
  // contain a part of "original" in the shape of "silhouette", set against a black
  // background. More specifically:
  //
  //  * Where the silhouette has a black pixel, the
  //    combination should retain the pixel of the original.
  //  * Where the silhouette has anything other than a
  //    black pixel, the combination should have a black pixel.
  //
  // Store the result in "combination" (below).


  val combination: Pic = ???
  combination.show()
}

