package o1

// This program is introduced in Chapter 2.7.

object BlockApp extends App {

  val background = rectangle(500, 500, Black)

  val block = new Block(20, new Pos(300, 50), Gray)

  val viewOfBlock = new View(block, "An uninteractive test app") {
    def makePic = {
      val blockPic = rectangle(block.size, block.size, block.color)
      background.place(blockPic, block.location)
    }
  }

  viewOfBlock.start()

}