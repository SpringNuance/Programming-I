
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.robots.gui

import scala.swing._
import o1.robots._
import o1.{GridPos,CompassDir,Pic}
import o1.gui.{Escapable,O1WindowDefaults}
import o1.gui.Dialog._
import o1.gui.layout._
import o1.util.ConvenientSeq
import scala.swing.event.Key
import scala.collection.mutable.Buffer
import scala.collection.immutable.ArraySeq




/** The class `RobotWindow` represents GUI windows that serve as the main
  * window of a [[RobotApp]].
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how
  * this class works or can be used.''' */
class RobotWindow extends MainFrame with Escapable with O1WindowDefaults {

  this.title = "Robots"
  this.location = new Point(20, 20)
  this.menuBar = new MenuBar {
    contents += new Menu("Program") {
      mnemonic = Key.P
      contents += new MenuItem(Action("Help") { //noinspection ForwardReference
        display("Right-click the map to add robots or walls.\nUse the buttons in lower right corner to make robots act.\nCreate new worlds using the menus.", RelativeTo(worldPane)) } )
      contents += new MenuItem(Action("Quit") { dispose() } )
    }
  }
  protected val worldPane = new FlowPanel
  protected var worldView: RobotsDisplay = _
  protected val nextRobotTurnButton = new Button {
    action = Action("Next robot") { worldView.nextRobotTurn() }
    preferredSize = new Dimension(this.preferredSize.width * 2, this.preferredSize.height + 3)
  }
  protected val advanceFullRoundButton = new Button {
    action = Action("Full turn") { worldView.advanceTurns(1) }
    preferredSize = nextRobotTurnButton.preferredSize
  }
  val buttonPanel = new FlowPanel(nextRobotTurnButton, advanceFullRoundButton)
  this.defaultButton = advanceFullRoundButton

  this.contents = new FlowPanel(new EasyPanel {
    placeNW(worldPane,   (0, 0), TwoWide, FillBoth(1, 1), (0, 0, 0, 0))
    placeNE(buttonPanel, (0, 1), OneSlot, NoFill(1, 0),   (0, 0, 0, 0))
  } )

  protected def createView(world: RobotWorld): RobotsDisplay = new RobotsDisplay(this, world)

  def focusButton = this.advanceFullRoundButton

  def displayWorld(world: RobotWorld) = {
    this.worldView = this.createView(world)
    this.worldPane.contents.clear()
    this.worldPane.contents += this.worldView
    this.worldView.update()
    this.pack()
  }


  def update(world: RobotWorld) = {
    this.updateButtons(world.nextRobot)
  }

  protected def updateButtons(nextRobot: Option[RobotBody]) = {
    val wasEnabled = this.focusButton.enabled
    this.advanceFullRoundButton.enabled  = nextRobot.isDefined
    this.nextRobotTurnButton.enabled = nextRobot.isDefined
    this.nextRobotTurnButton.text    = "Next robot" + nextRobot.flatMap( _.brain ).map(" (" + _.name + ")").getOrElse("")
    if (!wasEnabled) {
      this.focusButton.requestFocusInWindow()
    }
  }


  import o1.gui.BasicGridDisplay
  import o1.robots._
  import scala.util.Random
  import o1.util.ConvenientCollection
  import o1.util.assignments.arg
  import scala.swing.Separator
  import RobotType._

  protected class RobotsDisplay(val parent: RobotWindow, val world: RobotWorld) extends BasicGridDisplay[RobotWorld, Square](world, RobotsDisplay.MaxSquareSize) {
    view =>

    override def update() = {
      super.update()
      this.parent.update(this.world)
    }

    val popup = new SquarePopup

    override def tooltipFor(square: Square) = for (body <- square.robot) yield tooltipFor(body.brain)

    private def tooltipFor(brain: Option[RobotBrain]): String = brain.map(tooltipFor).getOrElse("brainless unrepairable robot body")

    def tooltipFor(brain: RobotBrain): String = brain.toString + " the " + brain.getClass.getSimpleName

    def elementClicked(square: Square) = {
      for (robot <- square.robot) {
        robot.takeTurn()
      }
    }

    def advanceTurns(howMany: Int): Unit = {
      for (repeat <- 1 to howMany) {
        this.world.advanceFullRound()
      }
      this.update()
    }

    def nextRobotTurn(): Unit = {
      this.world.advanceTurn()
      this.update()
    }

    protected class SquarePopup extends Popup {
      import PopupAction._

      class RobotAction(name: String, applies: RobotBody => Boolean)(perform: RobotBody => Unit)
        extends ElementAction(name, _.robot.exists( applies(_) ) )( _.robot.foreach(perform) )

      abstract class AbstractAddRobotItem(name: String) extends PopupAction(name) {

        def isApplicable(coords: GridPos) = world(coords).isEmpty && isAvailable

        val isAvailable: Boolean

        def requestName(): Option[String] = {
          requestAnyLine("Robot name:", RelativeTo(view))
        }

        def requestFacing(): Option[CompassDir] = {
          val dirs         = CompassDir.Clockwise
          val dirMap       = dirs.mapGroups( _.toString.toLowerCase )( _.head )
          val randomDir    = dirs.randomElement()
          val randomChoice = ("random" -> randomDir)
          val choices      = randomChoice._1 :: dirMap.keys.toList
          for (choice <- requestChoice("Initial facing:", choices, RelativeTo(view))) yield (dirMap + randomChoice)(choice)
        }

        def requestBrain(body: RobotBody): Option[RobotBrain]

        def apply(coords: GridPos) = {
          for (facing <- this.requestFacing()) {
            val newRobot: RobotBody = world.addRobot(coords, facing)
            newRobot.brain = this.requestBrain(newRobot)
          }
        }
      }

      abstract class AddBasicRobotItem(name: String, val robotType: RobotType[RobotBrain]) extends AbstractAddRobotItem(name) {

        val isAvailable = this.robotType.isUsable

        def requestBrain(body: RobotBody) = {
          for {
            name <- requestName()
            params <- requestParameters(name, body)
          } yield this.robotType.instantiate(params.map(arg): _*)
        }

        def requestParameters(name: String, body: RobotBody): Option[IndexedSeq[Any]]

      }

      trait Parameterless extends AddBasicRobotItem {
        def requestParameters(name: String, body: RobotBody) = Some(ArraySeq[Any](name, body))
      }

      object AddStaggerbotItem extends AddBasicRobotItem("Add staggerbot", Staggerbot) {
        def requestParameters(name: String, body: RobotBody) = {
          for (seed <- requestAnyInt("Random seed:", "Please enter an integer.", RelativeTo(view)))
            yield ArraySeq[Any](name, body, seed)
        }
      }

      object AddLovebotItem extends AddBasicRobotItem("Add lovebot", Lovebot) {
        def requestParameters(name: String, body: RobotBody) = {
          for (beloved <- requestBeloved(body)) yield ArraySeq[Any](name, body, beloved)
        }
        def requestBeloved(self: RobotBody) = {
          val options = world.robotList.filterNot( _ == self ).flatMap( _.brain )
          if (options.isEmpty) {
            Some(self)
          } else if (options.sizeIs == 1) {
            options.headOption.map( _.body )
          } else {
            val chosenBrain = requestChoice("Choose the beloved:", options, RelativeTo(view))
            chosenBrain.map( _.body ).orElse(Some(self))
          }
        }
      }

      val addRobotItems = Seq[PopupAction](
        new AddBasicRobotItem("Add spinbot", Spinbot) with Parameterless,
        new AddBasicRobotItem("Add nosebot", Nosebot) with Parameterless,
        AddLovebotItem,
        AddStaggerbotItem,
        new AddBasicRobotItem("Add psychobot", Psychobot) with Parameterless)
      val addWallItem  = new PosAction("Add wall", world(_).isEmpty )( world.addWall(_) )
      val takeTurnItem = new RobotAction("Play turn", AlwaysApplicable)( _.takeTurn() )
      val fixItem      = new RobotAction("Fix", robot => !robot.isIntact )( _.fix() )
      val destroyItem  = new RobotAction("Destroy", _.isIntact )( _.destroy() )

      this += addWallItem
      this ++= addRobotItems
      this += new Separator
      this += takeTurnItem
      this += fixItem
      this += destroyItem

    }

    import java.awt.image.BufferedImage

    val wallPic       = this.getPic("wall",   true)
    val floorPic      = this.getPic("floor",  true)
    val brokenPic     = this.getPic("broken", true)
    val directionPics = CompassDir.Clockwise.mapTo( dir => this.getPic("arrow_" + dir, true) )
    val robotPics     = loadRobotPics(Seq("Nosebot", "Lovebot", "Psychobot", "Staggerbot", "Spinbot"))

    def robotPic(robot: RobotBody) = this.robotPics(robot.brain.map( _.getClass.getCanonicalName ).getOrElse(""))

    def getPic(name: String, fullSize: Boolean): Option[BufferedImage] =
      Pic.asImage("pictures/" + name.toLowerCase + ".png").map( this.scalePic(_, fullSize) )

    def scalePic(image: BufferedImage, fullSize: Boolean) =
      this.scale(image, if (fullSize) this.squareSize else this.squareSize * 9 / 10)

    def loadRobotPics(types: Iterable[String]) =
      types.mapify( "o1.robots." + _ )( this.getPic(_, false) ).withDefaultValue(this.getPic("unknown", false))

    def missingElementVisuals = Array.empty
    def elementVisuals(square: Square): Array[BufferedImage] = {
      val terrainPic   = if (square.isUnpassable) this.wallPic else this.floorPic
      val statusPic    = if (square.robot.exists( bot => !bot.isIntact )) this.brokenPic else None
      val robotPic     = square.robot.flatMap( this.robotPic(_) )
      val directionPic = square.robot.flatMap( robot => this.directionPics(robot.facing) )
      Array(terrainPic, robotPic, directionPic, statusPic).map( _.orNull )
    }

  }

  private object RobotsDisplay {
    val MaxSquareSize = 60
  }

}


/** The trait `RobotScenarios` can be mixed into a `RobotWindow` to provide a selection
  * of ready-made scenarios in a GUI menu.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this trait works or can be used.''' */
trait RobotScenarios {
  scenarios: RobotWindow =>

  private final val basicScenarioMenu = new ScenarioMenu("World") {
    this ++= Scenario.All.map( new ScenarioItem(_) )
    mnemonic = Key.W
  }

  this.menuBar.contents += basicScenarioMenu

  protected def scenarioMenus = Seq(this.basicScenarioMenu)

  // this is used to hack around a possible scala Swing bug (?): a menu's contents buffer always shows up as empty
  protected class ScenarioMenu(name: String) extends Menu(name) {
    val items = Buffer[ScenarioItem]()
    def ++=(items: Iterable[ScenarioItem]) = {
      this.contents ++= items
      this.items ++= items
    }
  }

  protected class ScenarioItem(val scenario: Scenario) extends MenuItem(scenario.name) {
    this.action = new Action(scenario.name) {
      this.accelerator = scenario.accelerator
      def apply() = {
        for (world <- scenario.setup(scenarios.worldPane)) {
          scenarios.displayWorld(world)
        }
        for (menu <- scenarioMenus; item <- menu.items) {
          item.enabled = item.scenario.isAvailable
        }
      }
    }
    this.enabled = scenario.isAvailable
  }
}

