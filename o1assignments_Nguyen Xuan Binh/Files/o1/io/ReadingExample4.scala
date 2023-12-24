package o1.io

import scala.io.Source
import o1.play

// This program is introduced in Chapter 11.3.

object ReadingExample4 extends App {

  val file = Source.fromFile("Files/running_up_that_hill.txt")

  try {
    val entireContents = file.mkString
    play(entireContents)
  } finally {
    file.close()
  }

}