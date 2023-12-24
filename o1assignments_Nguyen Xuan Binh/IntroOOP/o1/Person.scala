package o1

// This class is introduced in Chapter 2.4.

class Individual(val name: String) {

  def say(phrase: String) = this.name + ": " + phrase

  def reactToSriracha     = this.say("What a nice sauce.")

  def reactToKryptonite   = this.say("What a strange mineral.")
}


