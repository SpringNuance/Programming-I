package o1.matrix

// This is a bonus program not currently in use in ebook chapters.

object MatrixTest extends App {

  val original = new Matrix(Array(Array(1, 2, 3), Array(4, 5, 6)))

  println("The Matrix:")
  println(original)

  println("The Matrix Revolutions:")
  println(original.transpose)
  println(original.transpose.transpose)


}