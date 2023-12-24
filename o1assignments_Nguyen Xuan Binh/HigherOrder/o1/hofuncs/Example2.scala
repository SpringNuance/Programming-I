package o1.hofuncs

// These example functions are introduced in Chapter 6.1.

object Example2 extends App {

  def compareLengths(string1: String, string2: String) = string1.length - string2.length

  def compareIntContent(string1: String, string2: String) = string1.toInt - string2.toInt

  def compareChars(string1: String, string2: String) = string1.compareToIgnoreCase(string2)

  def areSorted(first: String, second: String, third: String, compare: (String, String) => Int) =
    compare(first, second) <= 0 && compare(second, third) <= 0

  println(areSorted("Java", "Scala", "Haskell", compareLengths))
  println(areSorted("Haskell", "Java", "Scala", compareLengths))
  println(areSorted("Java", "Scala", "Haskell", compareChars))
  println(areSorted("Haskell", "Java", "Scala", compareChars))
  println(areSorted("200", "123", "1000", compareIntContent))
  println(areSorted("200", "123", "1000", compareLengths))

}

