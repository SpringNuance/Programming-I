package o1.io

import java.io.PrintWriter
import scala.util.Random

// This program is introduced in Chapter 11.3.

object WritingExample extends App {

  val fileName = "Files/random.txt"
  val file = new PrintWriter(fileName)
  try {
    for (n <- 1 to 10000) {
      file.println(Random.nextInt(100))
    }
    println("Created a file " + fileName + " that contains pseudorandom numbers.")
    println("In case the file already existed, its old contents were replaced with new numbers.")
  } finally {
    file.close()
  }
}

