package o1.looptest

// This example is discussed in Chapter 5.6.

object Example4 extends App {

  val someString = "llama"
  for (index <- 0 to someString.length - 1) {
    val character = someString(index)
    println("Index " + index + " stores " + character + ", which is character #" + character.toInt + " in Unicode.")
  }

}

