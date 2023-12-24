package o1.inputs

import io.StdIn._

/** A tiny app for experimenting with user input. Reads in a line of input
  * and uses it both as a string and as a number. */
object InputTest extends App {
  val input = readLine("Please enter an integer: ")
  input.toIntOption match {
    case None => println ("That is not a valid input. Sorry!")
    case Some(number) => {
        val digits = input.length
        println(s"Your number is $digits digits long.")
        val multiplied = input.toInt * digits
        println(s"Multiplying it by its length gives $multiplied.")
    }
  }
}

