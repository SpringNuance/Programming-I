
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.city.gui

import o1._
import o1.city.Demographic
import o1.city.Occupied
import o1.city.Vacant


// The default colors used in the GUI to represent different populations.
private[gui] object ColorScheme {

  val DemographicColors = Vector(Red, Blue, Green, Yellow, Black, Purple, Pink, LightBlue, Brown, Gray, SandyBrown)

  val VacantColor = White

  def apply(dg: Demographic) = dg match {
    case occupied: Occupied => occupied.label
    case Vacant             => White
  }

}


