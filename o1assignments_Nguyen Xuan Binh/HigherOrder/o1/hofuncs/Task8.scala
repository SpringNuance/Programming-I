package o1.hofuncs

// This program is introduced in Chapter 6.1.

import o1._

object Task8 extends App {

  // TODO: Implement an effectful function transformEachElement. It takes as parameters:
  //  1) a bufferful of names, and
  //  2) a function that transforms a single string to another.
  //
  // transformEachElement should use the given function to replace each element
  // of the buffer with a transformed one. You will need to choose appropriate
  // types and names for its parameters.
  //
  // An example use case is shown below. Leave it there and uncomment the
  // indicated line.



  def transformEachElement(listName: Buffer[String], whatTodo: String => String) ={
    for (i <- listName.indices){
      listName(i) = whatTodo(listName(i))
    }
  }




  // Abbreviates a two-word name. For example: "Sauli Niinistö" --> "S. Niinistö"
  def shortenName(name: String) = {
    val pieces = name.split(" ")
    pieces(0).take(1) + ". " + pieces(1)
  }

  val exampleNames = Buffer("Umberto Eco", "James Joyce", "Dorothy Dunnett")
  println(exampleNames.mkString(", "))     // prints: Umberto Eco, James Joyce, Dorothy Dunnett
  transformEachElement(exampleNames, shortenName)
  println(exampleNames.mkString(", "))     // should print: U. Eco, J. Joyce, D. Dunnett

}

