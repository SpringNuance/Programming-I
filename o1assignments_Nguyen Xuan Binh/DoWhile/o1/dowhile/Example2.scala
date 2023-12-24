package o1.dowhile

// This program is introduced in Chapter 8.3.

object Example2 extends App {

  var number = 1
  println(number)

  do {
    println(number)
    number += 4
    println(number)
  } while (number < 10)

  println(number)

}

