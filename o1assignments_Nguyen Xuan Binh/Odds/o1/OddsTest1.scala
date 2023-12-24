package o1

// This program is developed in Chapters 2.7 and 3.4.
// It creates a single Odds object and uses some of its methods.

import scala.io.StdIn._
import scala.math.pow

object OddsTest1 extends App {

  println("Please enter the odds of an event as two integers on separate lines.")
  println("For instance, to enter the odds 5/1 (one in six chance of happening), write 5 and 1 on separate lines.")
  val firstInput = readInt()
  val secondInput = readInt()
  var odds = new Odds(firstInput,secondInput)
  println("The odds you entered are:")
  println("In fractional format: " + odds.fractional)
  println("In decimal format: " + odds.decimal)
  println("In moneyline format: " + odds.moneyline)
  println("Event probability: " + odds.probability)
  println("Reverse odds: " + odds.not.fractional)
  println("Odds of happening twice: " + (pow((odds.wont+odds.will),2)-pow(odds.will,2))+ "/" + (pow(odds.will,2)))
  println("Please enter the size of a bet:")
  val thirdInput = readDouble()
  println("If successful, the bettor would claim " + odds.winnings(thirdInput))
  println("Please enter the odds of a second event as two integers on separate lines.")
  val fourthInput = readInt()
  val fifthInput = readInt()
  var odds2 = new Odds(fourthInput,fifthInput)
  println("The odds of both events happening are: " + odds.both(odds2))
  println("The odds of one or both happening are: " + odds.either(odds2))
}
