package o1.io

import scala.io.Source

// This program is introduced in Chapter 11.3.

object ReadingExample7 extends App {

  val file = Source.fromFile("Files/example.txt")

  try {

    val vectorOfLines = file.getLines.toVector

    println("LINES OF TEXT:")
    var lineNumber = 1              // stepper: 1, 2, 3, ...
    for (line <- vectorOfLines) {
      println(s"$lineNumber: $line")
      lineNumber += 1
    }

    println("LENGTHS OF EACH LINE:")
    for (line <- vectorOfLines) {
      println(s"${line.length} characters")
    }
    println("THE END")

  } finally {
    file.close()
  }


}