package o1.dowhile

// This task is part of Chapter 8.3.

import scala.io.StdIn._

object Task1 extends App {
  // TODO: Reimplement this program using a while or do-while loop instead of lazy-lists.
  def report(input: String) = "The input is " + input.length + " characters long."
  val inputs = LazyList.continually( readLine("Enter some text: ") )
  inputs.takeWhile( _ != "please" ).map(report).foreach(println)
}

