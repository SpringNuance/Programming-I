package o1.io

import scala.io.Source

// This program is introduced in Chapter 11.3.
// Note: This is an example of a program that fails to work properly.

object ReadingExample6 extends App {

  val file = Source.fromFile("Files/example.txt")

  try {

    println("LINES OF TEXT:")
    var lineNumber = 1              // stepper: 1, 2, 3 ...
    for (line <- file.getLines) {
      println(s"$lineNumber: $line")
      lineNumber += 1
    }
    println("LENGTHS OF EACH LINE:")
    for (line <- file.getLines) {
      println(s"${line.length} characters")
    }
    println("THE END")

  } finally {
    file.close()
  }

}