package o1.hofuncs

// These example functions are introduced in Chapter 6.1.

object Example1 extends App {

  def next(number: Int) = number + 1

  def doubled(original: Int) = 2 * original

  def twice(operation: Int => Int, target: Int) = operation(operation(target))

  println(twice(next, 1000))
  println(twice(doubled, 1000))

}

