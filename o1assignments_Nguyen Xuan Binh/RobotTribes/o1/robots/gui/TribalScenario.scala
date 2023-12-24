package o1.robots.gui

import o1.robots.tribal._
import o1.robots._
import Scenario._
import o1._
import o1.gui.Dialog._
import scala.swing.Component
import javax.swing.KeyStroke
import scala.util.Random

////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////



/** This class represents ready-made tribal robot fight scenarios (that the user can choose to
  * load up from the Tribal menu).
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this class works or can be used.'''
  *
  * @param name  the name of the scenario, to be displayed in the menu */
abstract class TribalScenario(name: String) extends RandomScenario(name) {


  protected def makeWorld(width: Int, height: Int, walls: Int, bunnies: Int, tribalCounts: Map[Tribe, Int]) = {

    def robotsNear(loc: GridPos) = world.neighbors(loc, false).exists( _.robot.isDefined )

    def addRobot(loc: GridPos, tribe: Tribe) = {
      val newBody = world.addRobot(loc, this.makeDirection())
      newBody.brain = Some(new TribalBot(newBody, tribe))
    }

    def addRobots(counts: Iterable[(Tribe, Int)], locs: Seq[GridPos]) = {
      val tribes = for ((tribe, count) <- counts; _ <- 1 to count) yield tribe
      for ((loc, tribe) <- locs zip tribes) {
        addRobot(loc, tribe)
      }
    }

    lazy val world = new RobotWorld(width, height)
    val free = for (y <- 0 until height; x <- 0 until width) yield GridPos(x + 1, y + 1)
    val (wallLocs, possibleRobotLocs) = Random.shuffle(free).splitAt(walls)
    val (fighterLocs, leftoverLocs) = possibleRobotLocs.splitAt(tribalCounts.values.sum)
    wallLocs.foreach( world.addWall(_) )
    addRobots(tribalCounts.toIterable, fighterLocs)
    addRobots(TribeLoader.BunnyTribe.map( _ -> bunnies ), leftoverLocs.filterNot(robotsNear))
    world

  }


}



/** This companion object of class `TribalScenario` provides access to certain statically defined
  * instances of the class.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this object works or can be used.''' */
object TribalScenario {

  val All = Seq(Rematch, BasicDuel, RandomWorldDuel, CustomDuel, GrandMelee, TigerInTheWoods, TigerPatrol)

  abstract class DuelScenario(name: String) extends TribalScenario(name) {
    import DuelScenario.Settings

    def requestBunnies(locator: Component) = {
      this.requestParameter("Number of bunnies (in addition to dueling tribes):", 0, locator)
    }

    def requestTribeCount(locator: Component) = {
      this.requestParameter("Initial number of members in each dueling tribe:", 0, locator)
    }

    def requestTribe(previouslyChosen: Option[Tribe], locator: Component) = {
      val tribes = TribeLoader.All.values.filter( _ != previouslyChosen ).flatten
      val prompt = if (previouslyChosen.isEmpty) "Select a tribe:" else "Select another tribe:"
      requestChoice(prompt, tribes.toSeq, RelativeTo(locator))
    }

    def setup(locator: Component) = {
      val settings = this.requestSettings(locator)
      if (settings.isDefined) {
        DuelScenario.latest = settings
      }
      settings.map( this.createWorld(_) )
    }

    def createWorld(settings: Settings) = {
      val width   = settings.width()
      val height  = settings.height(width)
      val walls   = settings.walls(width * height)
      val bunnies = settings.bunnies(width * height)
      val tribals = settings.tribals(width * height)
      makeWorld(width, height, walls, bunnies, Map(settings.first -> tribals, settings.second -> tribals))
    }

    def requestSettings(locator: Component): Option[Settings]

  }

  object DuelScenario {
    var latest: Option[DuelScenario.Settings] = None

    abstract class Settings(val first: Tribe, val second: Tribe) {
      def width(): Int
      def height(width: Int): Int
      def walls(size: Int): Int
      def bunnies(size: Int): Int
      def tribals(size: Int): Int
    }

    class SimpleSettings(first: Tribe, second: Tribe, val width: Int, val height: Int, val walls: Int, val bunnies: Int, val tribals: Int) extends Settings(first, second) {
      def height(width: Int) = this.height
      def walls(size: Int)   = this.walls
      def bunnies(size: Int) = this.bunnies
      def tribals(size: Int) = this.tribals
    }

  }

  abstract class SpecificDuel(name: String, firstTribeName: String, secondTribeName: String, val size: Int, val walls: Int, val bunnies: Int, val tribals: Int) extends DuelScenario(name) {
    duel =>

    lazy val firstTribe  = TribeLoader.All.values.flatten.find( _.name == firstTribeName )
    lazy val secondTribe = TribeLoader.All.values.flatten.find( _.name == secondTribeName )

    def isAvailable = this.firstTribe.isDefined && this.secondTribe.isDefined

    def requestSettings(locator: Component) = {
      for {
        first  <- this.firstTribe
        second <- this.secondTribe
      } yield new DuelScenario.Settings(first, second) {
        def width()            = duel.size
        def height(width: Int) = duel.size
        def walls(size: Int)   = duel.walls
        def bunnies(size: Int) = duel.bunnies
        def tribals(size: Int) = duel.tribals
      }
    }
  }


  object TigerInTheWoods extends SpecificDuel("Tiger in the woods", "tiger", "bunny",     50, 20, 1000, 1 )
  object TigerPatrol     extends SpecificDuel("Tiger patrol",       "tiger", "patrolman", 12, 5,     5, 10)


  object BasicDuel extends DuelScenario("Basic duel") {

    def isAvailable = TribeLoader.All.size >= 2

    def requestSettings(locator: Component) = {
      for {
        first  <- this.requestTribe(None, locator)
        second <- this.requestTribe(Some(first), locator)
      } yield new DuelScenario.SimpleSettings(first, second, 20, 20, 10, 30, 6)
    }

  }

  object Rematch extends DuelScenario("Rematch (previous duel)") {

    override def accelerator = Some(KeyStroke.getKeyStroke("control R"))

    def isAvailable = DuelScenario.latest.isDefined

    def requestSettings(locator: Component) = {
      DuelScenario.latest
    }

  }

  object RandomWorldDuel extends DuelScenario("Random world duel") {

    def isAvailable = TribeLoader.All.size >= 2

    def requestSettings(locator: Component) = {
      for {
        first  <- this.requestTribe(None, locator)
        second <- this.requestTribe(Some(first), locator)
      } yield new DuelScenario.Settings(first, second) {
        def width()            = 5 + Random.nextInt(25)
        def height(width: Int) = width - 1 + Random.nextInt(3)
        def walls(size: Int)   = Random.nextInt(size / 10)
        def bunnies(size: Int) = Random.nextInt(size / 7)
        def tribals(size: Int) = 2 + Random.nextInt(size / 15)
      }
    }

  }

  object CustomDuel extends DuelScenario("Custom duel") {

    def isAvailable = TribeLoader.All.size >= 2

    def requestSettings(locator: Component) = {
      for {
        first   <- this.requestTribe(None, locator)
        second  <- this.requestTribe(Some(first), locator)
        width   <- this.requestWidth(locator)
        height  <- this.requestHeight(locator)
        walls   <- this.requestWallCount(locator)
        bunnies <- this.requestBunnies(locator)
        tribals <- this.requestTribeCount(locator)
      } yield new DuelScenario.SimpleSettings(first, second, width, height, walls, bunnies, tribals)
    }

  }

  object GrandMelee extends TribalScenario("Grand mêlée") {

    def isAvailable = TribeLoader.All.nonEmpty

    def setup(locator: Component) = {
      val tribes = TribeLoader.All.flatMap( _._2 )
      for {
        width       <- this.requestWidth(locator)
        height      <- this.requestHeight(locator)
        walls       <- this.requestWallCount(locator)
        totalRobots <- this.requestRobotCount(locator)
      } yield this.makeWorld(width, height, walls, 0, tribes.zip(LazyList.continually(totalRobots / tribes.size)).toMap)
    }

  }


}