package o1

// This program is introduced in Chapter 3.1.

object PaintingApp extends App {

  val artwork = new ArtProject(rectangle(600, 600, White))

  val gui = new View(artwork) {
    def makePic = artwork.image

    // The event-handling code is missing.
  }

  gui.start()

}

