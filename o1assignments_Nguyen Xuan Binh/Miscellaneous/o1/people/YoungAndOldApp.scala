package o1.people

import scala.Vector


// This program is introduced in Chapter 8.4. You can ignore it until then.

object YoungAndOldApp extends App {

  // Replace the question marks below with the appropriate method call.
  // Uncomment the println statements at the end of the program.
  //
  // The program should first print out the Members who were born in 1980
  // or later, followed by the clients born earlier than that. Like this:
  // RECENT:
  // Yona(1984-)
  // Gaga(1986-)
  // ANCIENT:
  // Bono(1960-)
  // Cher(1946-)
  // Nico(1938-1988)
  // Pelé(1940-)
  // Enya(1961-)
  // Stig(1978-)
  // Nero(37-68)
  //
  // You only need to call a single method on a Vector to solve this tiny assignment.
  //
  // The program uses class Member from the same package.

  val members = Vector(new Member(4321, "Bono", 1960, None),
                       new Member(1234, "Cher", 1946, None),
                       new Member(4444, "Nico", 1938, Some(1988)),
                       new Member(7777, "Yona", 1984, None),
                       new Member(1111, "Pelé", 1940, None),
                       new Member(6666, "Enya", 1961, None),
                       new Member(5555, "Stig", 1978, None),
                       new Member(2222, "Nero", 37,   Some(68)),
                       new Member(3333, "Gaga", 1986, None))

  val (recents, ancients) = members.partition(_.yearOfBirth >= 1980)

  println("RECENT:")
  recents.foreach(println)
  println("ANCIENT:")
  ancients.foreach(println)
}