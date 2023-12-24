package o1.robots.gui

import scala.swing.SimpleSwingApplication
import o1.robots.RobotWorld


////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////


/** The singleton object `RobotApp` serves as an entry point for (the basic version of)
  * Robots simulator and starts up the user interface.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this object works
  * or can be used, apart from the fact that you can use this file to start the program.''' */
object RobotApp extends RobotProgram(new RobotWindow with RobotScenarios)

/** The class `RobotsProgram` represents (variations of) the robot simulators application.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this class works or can be used.''' */
class RobotProgram(window: RobotWindow, initialWorld: RobotWorld = RobotProgram.makeInitialWorld) extends SimpleSwingApplication {

  this.window.displayWorld(initialWorld)

  def top = this.window

}

private object RobotProgram {
   def makeInitialWorld = {
    val (floorW, floorH) = (12, 9)
    val world = new RobotWorld(floorW, floorH)
    require(world.width == floorW + 2 && world.height == floorH + 2, s"The created world has width=${world.width} and height=${world.height}. It should have had width=${floorW+2} and height=${floorH+2} (which includes surrounding wall squares).")
    world
  }
}

