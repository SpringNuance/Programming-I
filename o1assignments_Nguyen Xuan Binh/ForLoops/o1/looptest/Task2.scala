package o1.looptest

// This program is associated with Chapter 5.6.

object Task2 extends App {

  val data = "Urho Kaleva Kekkonen, John Fitzgerald Kennedy, North Atlantic Treaty Organization"

  // Flesh out the loop below. It should extract all characters except lower-case
  // letters from the data string, gathering them in the result variable.

  // Note: Char objects have a method named isLower (see example below). It returns
  // a Boolean value that indicates if the character is a lower-case letter.

  var result = ""  // gatherer: collects the output string letter by letter
  for (character <- data) {
    if (!character.isLower) {
      result += character
    }
  }
  println(result)  // this should print out: U K K, J F K, N A T O

}

