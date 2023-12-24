package o1

// This code is introduced in Chapter 2.7.

object SpaceProgram extends App {
  val space = new AreaInSpace(500)
  val emptySpacePic = rectangle(space.width, space.height, Black)
  val earthPic = circle(space.earth.radius * 2, MediumBlue)
  val moonPic  = circle(space.moon.radius  * 2, Beige)
  // Replace the question marks below with code that works.
  val gui = new View(space, "A Very Simple View of Space") {
  def makePic = {
    val earthAdded = emptySpacePic.place(earthPic, space.earth.location)
    earthAdded.place(moonPic, space.moon.location)
  }
}
   gui.start() // Should launch the view that variable "gui" refers to.
}


