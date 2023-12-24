package o1.io

import scala.io.Source

// This program is introduced in Chapter 11.3.

object ReadingExample5 extends App {

  val file = Source.fromFile("Files/example.txt")

  try {
    for ((line, index) <- file.getLines.zipWithIndex) {
      println(s"${index + 1}: $line")
    }
  } finally {
    file.close()
  }

}

// An alternative implementation for the try block:
//    var lineNumber = 1              // stepper: 1, 2, 3, ...
//    for (line <- file.getLines) {
//      println(s"$lineNumber: $line")
//      lineNumber += 1
//    }

