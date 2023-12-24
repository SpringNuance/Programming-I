package o1.robots.gui

import scala.swing.event.Key
import scala.swing._
import o1.robots._
import o1.util._

////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////


/** The class `RobotWindow` represents GUI windows that serve as the main
  * window of a tribe-enabled Robots application.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this class works or can be used.''' */
class TribalRobotWindow extends RobotWindow {

  protected val next99TurnsButton = new Button {
    action = Action("99 turns") { worldView.advanceTurns(99) }
    preferredSize = nextRobotTurnButton.preferredSize
  }
  this.buttonPanel.contents += next99TurnsButton

  override protected def updateButtons(nextRobot: Option[RobotBody]) = {
    super.updateButtons(nextRobot)
    this.next99TurnsButton.enabled = nextRobot.isDefined
  }

  override protected def createView(world: RobotWorld) = new TribalDisplay(this, world)



  import o1.robots.tribal.TribalBot
  import scala.swing._
  import o1.robots._
  import o1.robots.tribal._
  import o1.util.ConvenientCollection
  import javax.imageio.ImageIO

  protected class TribalDisplay(parent: TribalRobotWindow, world: RobotWorld) extends RobotsDisplay(parent, world) {

    override val popup = new SquarePopup {
      this.contents.prepend(new Menu("Add tribal bot") {
        for ((file, tribe) <- TribeLoader.All) {
          contents += new AddTribalBotItem(file.fileName.takeWhile( _ != '.' ), tribe)
        }
      })

      class AddTribalBotItem(val tribeName: String, val tribe: Option[Tribe]) extends AbstractAddRobotItem(tribeName) {
        val isAvailable = this.tribe.isDefined

        def requestBrain(body: RobotBody) = {
          this.tribe.map( new TribalBot(body, _) )
        }
      }
    }

    override def robotPic(robot: RobotBody) =
      robot.brain match {
        case Some(tribal: TribalBot) => this.tribePics(tribal.tribe)
        case nonTribalBot            => super.robotPic(robot)
      }

    val tribePics = TribeLoader.All.values.flatten.mapTo( this.tribePic(_) )

    private def tribePic(tribe: Tribe) = {
      val picPath = localURL("tribes/" + tribe.name.toLowerCase + ".png")
      val image = picPath.map( file => this.scalePic(ImageIO.read(file), false) )
      image orElse this.getPic("unknown", false) orElse None
    }
  }


}



/** The trait `TribalScenarios` can be mixed into a `TribalRobotWindow`
  * to provide a selection of ready-made scenarios in a GUI menu.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this trait works or can be used.''' */
trait TribalScenarios extends RobotScenarios {
  self: TribalRobotWindow =>

  private final val tribalScenarioMenu = new ScenarioMenu("Tribal") {
    mnemonic = Key.T
    this ++= TribalScenario.All.map(new ScenarioItem(_))
  }

  this.menuBar.contents += tribalScenarioMenu

  override def scenarioMenus = super.scenarioMenus ++ Seq(tribalScenarioMenu)

}




