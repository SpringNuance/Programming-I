package o1.hofuncs

// This program is introduced in Chapter 6.1.

import o1._

object Task9 extends App {
  // PHASE 1/2:
  // Implement turnElementsIntoResult so that it computes its return value by
  // applying a given function (its third parameter) to the numbers contained in
  // a given vector (its first parameter). The second parameter indicates the
  // base value of the computation (which gets returned if the vector is empty).
  //
  // For example, say "numbers" contains the integers 20, 10, and 5, "initialValue"
  // equals 0 and "operation" is a function that returns the sum of its two parameters.
  //
  //   1. The "operation" is first applied to "initialValue" and
  //      the first number in the vector. 0 + 20 equals 20.
  //   2. The "operation" is then applied to the previous result
  //      and the next element. 20 + 10 = 30.
  //   3. The operation is again applied to the previous result
  //      and the next element. 30 + 5 = 35.
  //   4. The last element has been processed, so it's time to
  //      stop and return 35.
  //
  // As you will have noticed, this example demonstrates one way of summing up
  // the elements of a vector.
  def turnElementsIntoResult(numbers: Vector[Int], initialValue: Int, operation: (Int, Int) => Int) = {
    var gatherer = initialValue
    for (i <- numbers){
      gatherer = operation(gatherer,i)
      }
    gatherer
    }


  val exampleNumbers = Vector(100, 25, -12, 0, 50, 0)
  println("The numbers are: " + exampleNumbers.mkString(", "))

  def addToSum(existingResult: Int, nextNumber: Int) = existingResult + nextNumber

  println("Sum: " + turnElementsIntoResult(exampleNumbers, 0, addToSum)) // should print: Sum: 163


  // PHASE 2/2:
  // Use turnElementsIntoResult to compute:
  //   1) how many positive numbers there are among "exampleNumbers"
  //   2) the product of all the other "exampleNumbers" except for zeros
  // To that end, flesh out the two helper functions below and replace the zero
  // results with calls to turnElementsIntoResult. You'll need to design the
  // helper functions yourself so that they work in combination with
  // turnElementsIntoResult to produce the desired results.

  def positiveCount(number1: Int, number2: Int) = {
     var count = number1
     if (number2 > 0){
       count += 1
     }
     count

  } // TODO: add the appropriate parameters and a method body

  def productOfNonZeros(number1: Int, number2:Int) = {
     var count = number1
     if (number2 != 0){
       count *= number2
     }
     count
  } // TODO: add the appropriate parameters and a method body

  val howManyPositives = turnElementsIntoResult(exampleNumbers, 0, positiveCount) // TODO: pass the appropriate function as a parameter to turnElementsIntoResult; also pick an initial value that works for counting elements
  val product = turnElementsIntoResult(exampleNumbers, 1, productOfNonZeros)           // TODO: pass the appropriate function as a parameter to turnElementsIntoResult; also pick an initial value that works for multiplying elements
  println("Number of positive elements: " + howManyPositives)  // should print: Number of positive elements: 3
  println("Product: " + product)                               // should print: Product: -1500000
}

