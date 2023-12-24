package o1.looptest

// This file is associated with Chapter 5.6.

import scala.math._
import scala.io.StdIn._

object Task7 extends App {

  val upperLimit = readLine("Enter an upper limit for the two legs of a right triangle: ").toInt // Do not edit this line.
   for (first <- 1 to upperLimit){
     for (second <- first to upperLimit){
   println(s"$first and $second -----> ${hypot(first, second)}")
     }
   }
  // Below, write a program that prints out lists such as the following.
  // This example illustrates the expected printout when upperLimit is 3:
  // 1 and 1 -----> 1.4142135623730951
  // 1 and 2 -----> 2.23606797749979
  // 1 and 3 -----> 3.1622776601683795
  // 2 and 2 -----> 2.8284271247461903
  // 2 and 3 -----> 3.605551275463989
  // 3 and 3 -----> 4.242640687119285
  //
  // Each line of output corresponds to a right triangle: the decimal number on
  // the right is the length of a hypotenuse and the two integers on the left are
  // the legs (i.e., the other two sides). The program considers each possible
  // combination of legs whose lengths are integers between 1 and upperLimit.
  //
  // The same combination of legs must not appear twice; e.g., the above output
  // does not list both "1 and 3" and "3 and 1". Hypot.
  //
  // Use nested for loops; that is, place one for loop inside another.
  //
  // Hint:
  //  You can use the following line of code as part of your program.
  //  Place it within the two nested loops:






}

