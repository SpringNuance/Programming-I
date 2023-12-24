package o1

// This class is introduced in Chapter 3.4.

class Grouse {

  private var size = 400
  private val basePic = Pic("bird.png")

  def foretellsDoom = this.size <= 0

  def shrink() = {
    if (this.size > 0) {
      this.size = this.size - 1
    }
  }

  def toPic = this.basePic.scaleTo(this.size)

}
