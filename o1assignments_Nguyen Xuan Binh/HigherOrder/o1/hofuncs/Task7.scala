package o1.hofuncs

// This program is introduced in Chapter 6.1.

object Task7 extends App {

  def printCube(n: Int) = {
    println(n * n * n)
  }

  def printIfPositive(number: Int) = {
    if (number > 0) {
      println(number)
    }
  }

  // Your task: implement this method so that "whatToDo" is executed on every integer in "numbers".
  def repeatForEachElement(numbers: Vector[Int], whatToDo: Int => Unit) = {
    for (i <- numbers){
       whatToDo(i)
    }
  }


  val exampleNumbers = Vector(5, 10, -20, -10, 5)
  println("Cubes:")
  repeatForEachElement(exampleNumbers, printCube)       // should print, on separate lines: 125, 1000, -8000, -1000, 125
  println("Positives:")
  repeatForEachElement(exampleNumbers, printIfPositive) // should print, on separate lines: 5, 10, 5


}

