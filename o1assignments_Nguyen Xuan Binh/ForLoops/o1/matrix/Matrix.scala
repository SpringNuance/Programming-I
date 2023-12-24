package o1.matrix

// This is a bonus class not currently in use in ebook chapters.

class Matrix(val contents: Array[Array[Double]]) {  // contents is assumed to be "rectangular" and to have at least one row

  def width = this.contents(0).length

  def height = this.contents.length

  override def toString = {
    var picture = ""                          // gatherer
    for (currentRow <- this.contents) {
      for (currentElement <- currentRow) {
        picture += s"$currentElement\t"
      }
      picture += "\n"
    }
    picture
  }

  def transpose = { // There is also a standard API method for this.
    val transposeWidth = this.height                                             // temporary
    val transposeHeight = this.width                                             // temporary
    val transposeContents = Array.ofDim[Double](transposeHeight, transposeWidth) // container
    for (row <- 0 until transposeHeight) {                                // row is a stepper
      for (col <- 0 until transposeWidth) {                               // col is a stepper
        transposeContents(row)(col) = this.contents(col)(row)
      }
    }
    new Matrix(transposeContents)
  }

  def rowSum(row: Int) = this.contents(row).sum

  def columnSum(col: Int) = this.transpose.rowSum(col) // easy to read, but inefficient
  // Alternative short implementations using late-course techniques:
  // def columnSum(col: Int) = this.contents.map( _(col) ).sum
  // def columnSum(col: Int) = this.contents.foldLeft(0.0)( _ + _(col) )

}

