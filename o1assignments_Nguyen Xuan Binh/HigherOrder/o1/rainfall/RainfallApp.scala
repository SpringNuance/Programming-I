package o1.rainfall

import scala.io.StdIn._

object RainfallApp extends App {

  // Enter your program here as instructed in Chapter 7.1.
  val inputs = LazyList.continually(readLine("Enter rainfall (or 999999 to stop): ").toInt)
  var x = inputs.filter(_ >= 0).takeWhile(_ != 999999).toVector
  if (x.isEmpty){
     println("No valid data. Cannot compute average.")
  } else {
    var z = x.sum/x.size
    println("The average is " + z)
  }
}