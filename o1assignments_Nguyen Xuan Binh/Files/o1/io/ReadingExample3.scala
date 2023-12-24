package o1.io

import scala.io.Source

// This program is introduced in Chapter 11.3.

object ReadingExample3 extends App {

  val file = Source.fromFile("Files/example.txt")

  try {
    var charCount = 1              // stepper: 1, 2, ...
    for (char <- file) {
      println("Character #" + charCount + " is " + char + ".")
      charCount += 1
    }
  } finally {
    file.close()
  }

}