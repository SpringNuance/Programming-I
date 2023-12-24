package o1.blood


/** This app goes through all the possible combinations of donors and recipients in the
  * ABO+Rh system and produces a printout detailing which combinators are safe. To determine
  * whether a combination is safe, the app uses class `BloodType`.
  *
  * The output is expected to match [[https://en.wikipedia.org/wiki/Blood_type#Red_blood_cell_compatibility
  * the one provided by Wikipedia]]:
  *
  * Note to students: You don't need to understand how this app works just yet.
  * You can run it to test your `BloodType` class, though. */
object BloodTest extends App {

  val allTypes = for (abo <- Seq("A","B","AB","O"); rh <- Seq(true, false)) yield new BloodType(abo, rh)
  for (donor <- allTypes; recipient <- allTypes) {
    println((s"$donor can donate to $recipient:").padTo(23, ' ') + donor.canDonateTo(recipient))
  }

}