package o1.looptest
import scala.io.StdIn._

// This program is associated with Chapter 5.6.

object Task4 extends App {

  // Modify this program slightly so that it uses the "until" method instead of "to" .
  // (Revisit Chapter 5.2 for a refresher, if you need one.)  The program should
  // continue to produce exactly the same output as it did before the modification.

  val userInput = readLine("Write something, please, anything: ")
  for (index <- 0 until userInput.length) {
    val character = userInput(index)
    println("Index " + index + " stores " + character + ", which is character #" + character.toInt + " in Unicode.")
  }

}

