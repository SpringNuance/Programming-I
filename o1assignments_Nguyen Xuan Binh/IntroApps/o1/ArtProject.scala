package o1

// This class is introduced in Chapter 3.1.

class ArtProject(canvas: Pic) {
  var image = canvas                          // gatherer
  var brush = circle(10, Black)               // most-recent holder

  def paint(where: Pos) = {
    // Assign a new value to "image": position a new copy of the "brush" on top
    // of the old image at the coordinates indicated by the parameter "where".
  }

}


