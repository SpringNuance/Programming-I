package o1.blood

// This program is introduced in Chapter 7.2.

object BloodTest extends App {

  println("Using Rhesus blood types only:")
  val allRh = Seq(RhPlus, RhMinus)
  for (donor <- allRh; recipient <- allRh) {
    println(s"$donor can donate to $recipient:\t${donor.canDonateTo(recipient)}")
  }


  // Uncomment this when you're ready to test your ABO implementation.
  /*
  println("\nUsing ABO blood types only:")
  val allABO = Seq(A, B, AB, O)
  for (donor <- allABO; recipient <- allABO) {
    println(s"$donor can donate to $recipient:\t${donor.canDonateTo(recipient)}")
  }
  */


  // If you uncomment this part, also uncomment the class ABORh in BloodType.scala
  /*
  println("\nUsing ABO and Rhesus in combination:")
  val allABORh = for (abo <- allABO; rh <- allRh) yield new ABORh(abo, rh)
  for (donor <- allABORh; recipient <- allABORh) {
    println(s"$donor can donate to $recipient:\t${donor.canDonateTo(recipient)}")
  }
  */

}