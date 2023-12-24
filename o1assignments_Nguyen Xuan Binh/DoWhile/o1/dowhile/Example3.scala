package o1.dowhile

// This program is introduced in Chapter 8.3.

import scala.io.StdIn._

object Example3 extends App {

  var name = ""
  while (name.isEmpty) {
    name = readLine("Enter your name (at least one character, please): ")
    println("The name is " + name.length + (if (name.length != 1) " characters" else " character") + " long.")
  }

  println("Name entered OK.")

}

