package o1.looptest

// This example is discussed in Chapter 5.6.

object Example2 extends App {

  var index = 0
  for (character <- "llama") {
    println("Index " + index + " stores " + character + ", which is character #" + character.toInt + " in Unicode.")
    index += 1
  }

}

