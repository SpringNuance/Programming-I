package o1.goodstuff.gui


import o1.goodstuff.Category
import scala.swing.SimpleSwingApplication
import o1.gui.O1AppDefaults


////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

/** The singleton object `GoodStuff` represents the GoodStuff experience diary application, and serves
  * as an entry point for the program. It can be run to start up the GoodStuff user interface.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this object works
  * or can be used, apart from the fact that you can use this file to start the program.''' */
object GoodStuff extends SimpleSwingApplication with O1AppDefaults {

  val hotelCategory = new Category("Hotel", "night")    // only a single category in this simple app
  val window = new CategoryDisplayWindow(hotelCategory) // a window that displays the category

  def top = this.window   // the category window is the "top window" (main window) of this app

}
