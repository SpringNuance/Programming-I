package o1


// This code is introduced in Chapter 12.1.

package object recursion {


  def isPalindrome(candidate: String): Boolean =
    if (candidate.length <= 1)
      true
    else if (candidate.head != candidate.last)
      false
    else
      isPalindrome(candidate.substring(1, candidate.length - 1))



  def printSquares(upperLimit: Int): Unit = {
    if (upperLimit > 0) {
      printSquares(upperLimit - 1)
      println(upperLimit * upperLimit)
    }
  }


  def isLetterPalindrome(candidate: String): Boolean = isPalindrome(candidate.filter( _.isLetter ).toLowerCase)


  object iterativeVersions {

    def printSquares(upperLimit: Int) = {
      for (number <- 1 to upperLimit) {
        println(number * number)
      }
    }

    def isPalindrome(candidate: String): Boolean = {
      var leftIndex = 0
      var rightIndex = candidate.length - 1
      while (leftIndex < rightIndex) {
        if (candidate(leftIndex) != candidate(rightIndex)) {
          return false
        }
        leftIndex += 1
        rightIndex -= 1
      }
      true
    }

  }


  def fibo(n: Int): Long = if (n <= 1) n else fibo(n - 1) + fibo(n - 2)


  object better {

    def fibo(n: Int) = {
      def fiboHelp(thisFibo: Int, nextFibo: Int, stepsRemaining: Int): Long = {
        if (stepsRemaining <= 0)
          thisFibo
        else
          fiboHelp(nextFibo, thisFibo + nextFibo, stepsRemaining - 1)
      }
      fiboHelp(0, 1, n)
    }

  }

}