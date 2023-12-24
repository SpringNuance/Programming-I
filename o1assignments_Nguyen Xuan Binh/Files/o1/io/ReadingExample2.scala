package o1.io

import scala.io.Source

// This program is introduced in Chapter 11.3.

object ReadingExample2 extends App {

  val file = Source.fromFile("Files/example.txt")

  try {
    println("The first  character in the file is " + file.next() + ".")
    println("The second character in the file is " + file.next() + ".")
    println("The third  character in the file is " + file.next() + ".")
    println("The fourth character in the file is " + file.next() + ".")
    println("The fifth  character in the file is " + file.next() + ".")
    println("The sixth  character in the file is " + file.next() + ".")
  } finally {
    file.close()
  }

}