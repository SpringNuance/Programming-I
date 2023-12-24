package o1.excursion

/** A small program that uses the class `Excursion`. */
object ExcursionTest extends App {

  println("This small program uses some of the methods from class Excursion.")
  runFactoryScenario()
  println("Done.")


  def runFactoryScenario() = {
    // A new excursion with no participants yet, and a limit of 4 participants.
    val testTrip = new Excursion("Chocolate Factory", 4)
    println(testTrip)
    println("Last one who makes it: " + testTrip.lastParticipant)

    // First participant:
    println(testTrip.registerInterest("Augustus"))
    println(testTrip)

    // Three more, all these still fit:
    println(testTrip.registerInterest("Charlie"))
    println(testTrip.registerInterest("Veruca"))
    println(testTrip.registerInterest("Mike"))
    println(testTrip)
    println("Last one who makes it: " + testTrip.lastParticipant)

    // One more participant: the limit has been reached,
    // so Mike's still the last one to make it to the excursion.
    println(testTrip.registerInterest("Violet"))
    println(testTrip)
    println("Last one who makes it: " + testTrip.lastParticipant)
  }

}
