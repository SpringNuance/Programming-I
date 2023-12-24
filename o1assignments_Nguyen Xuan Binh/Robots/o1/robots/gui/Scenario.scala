
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.robots.gui

import o1.robots._
import RobotType._
import o1.gui.Dialog._
import scala.swing._
import o1._
import o1.util.ConvenientSeq
import javax.swing.KeyStroke


/** This class represents ready-made robot world scenarios (that the user can choose
  * to load up from the Scenario menu).
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this class works or can be used.'''
  *
  * @param name  the name of the scenario, to be displayed in the menu */
abstract class Scenario(val name: String) {

  def accelerator: Option[KeyStroke] = None

  def setup(locator: Component): Option[RobotWorld]

  def isAvailable: Boolean

  protected def requestParameter(prompt: String, min: Int, locator: Component) = {
    requestInt(prompt, _ >= min, s"Please enter an integer no lower than $min.", RelativeTo(locator))
  }

  protected def requestWidth(locator: Component) = {
    requestParameter("Width of the world:", 1, locator)
  }

  protected def requestHeight(locator: Component) = {
    requestParameter("Height of the world:", 1, locator)
  }

  protected def requestWallCount(locator: Component) = {
    requestParameter("Number of walls:", 0, locator)
  }

  protected def requestRobotCount(locator: Component) = {
    requestParameter("Number of robots:", 0, locator)
  }

}


/** This companion object of class `Scenario` provides access to certain statically defined
  * instances of the class.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this object works or can be used.''' */
object Scenario {

  val All = Seq( EmptyWorld, Noses, Bar, Verona, Termination, OneOfEach,
                 new RandomDefault("Random (default, small)", 10),
                 new RandomDefault("Random (default, big)", 40),
                 new RandomCustom("Random (custom)", RobotType.All),
                 new RandomCustom("Random (custom, no psychos)", RobotType.All.filterNot( _ == Psychobot )) )

  object EmptyWorld extends Scenario("Empty (custom size)") {

    def isAvailable = true

    def setup(locator: Component) = {
      for {
        width <- this.requestWidth(locator)
        height <- this.requestHeight(locator)
      } yield new RobotWorld(width, height)
    }

  }

  abstract class MapScenario(name: String) extends Scenario(name) {

    protected def map: Seq[String]

    protected def addBots(world: RobotWorld): Unit

    private def addWalls(world: RobotWorld) = {
      val wallLocs = for {
        y <- this.map.indices
        x <- this.map(y).indices
        if (this.map(y)(x) == '#')
      } yield new GridPos(x + 1, y + 1)
      wallLocs.foreach( world.addWall(_) )
    }

    protected def addBot(world: RobotWorld, x: Int, y: Int, facing: CompassDir) = {
      world.addRobot(new GridPos(x + 1, y + 1), facing)
    }

    def setup(locator: Component) = {
      val world = new RobotWorld(this.map.head.length, this.map.size)
      this.addWalls(world)
      this.addBots(world)
      Some(world)
    }

  }

  object Noses extends MapScenario("Noses") {

    def isAvailable = Nosebot.isUsable

    protected val map =
      Seq("...#......",
          ".#.....#..",
          "..........",
          "...####...",
          "..........",
          ".#........")

    protected def addBots(world: RobotWorld) = {
      val gogol     = this.addBot(world, 9, 5, South)
      val pinocchio = this.addBot(world, 3, 2, West)
      val cyrano    = this.addBot(world, 4, 0, West)
      val kleopatra = this.addBot(world, 3, 5, North)
      val malden    = this.addBot(world, 7, 3, East)
      gogol.brain     = Spinbot("Gogol", gogol)
      pinocchio.brain = Nosebot("Pinocchio", pinocchio)
      cyrano.brain    = Nosebot("Cyrano", cyrano)
      kleopatra.brain = Nosebot("Kleopatra", kleopatra)
      malden.brain    = Nosebot("Malden", malden)
    }

  }

  object Bar extends MapScenario("Bar") {

    def isAvailable = Staggerbot.isUsable && Nosebot.isUsable

    protected val map =
      Seq("..#......#..",
          "..########..",
          "............",
          "........##..",
          ".##.....##..",
          ".##.....##..",
          ".##.........",
          ".....###....",
          ".....###....",
          "............")

    protected def addBots(world: RobotWorld) = {
      val tender = this.addBot(world, 4, 0, South)
      tender.brain = Nosebot("Tender", tender)
      val flyLocs = for {
        row <- this.map.indices
        col <- this.map(row).indices
        if (row != 0 || col < 2 || col > 9)
        coords = new GridPos(col + 1, row + 1)
        if (world(coords).isEmpty && (row + col) % 3 != 2)
      } yield coords
      var flyCount = 0
      for (loc <- flyLocs) {
        flyCount += 1
        val fly = world.addRobot(loc, CompassDir.Clockwise(flyCount % 4))
        fly.brain = Staggerbot("Fly " + flyCount, fly, Integer.valueOf(flyCount))
      }
    }
  }

  object Verona extends MapScenario("Verona") {

    def isAvailable = Staggerbot.isUsable && Lovebot.isUsable

    protected val map =
      Seq("........##.##.",
          ".......#######",
          ".......#######",
          "........#####.",
          ".........###..",
          "..........#...",
          "..............",
          "..##.##.......",
          ".#######......",
          ".#######......",
          "..#####.......",
          "...###........",
          "....#.........")

    protected def addBots(world: RobotWorld) = {
      val romeo  = this.addBot(world, 12, 9, West)
      val juliet = this.addBot(world, 0, 0, East)
      romeo.brain  = Staggerbot("Romeo", romeo, Integer.valueOf(4))
      juliet.brain = Lovebot("Juliet", juliet, romeo)
    }
  }

  object Termination extends MapScenario("Termination") {

    def isAvailable = Psychobot.isUsable && Nosebot.isUsable

    protected val map =
      Seq("...#......",
          ".#.....#..",
          "..........",
          "...#####..",
          "..........",
          ".#........")

    protected def addBots(world: RobotWorld) = {
      o1.play("[40]<<<" + "(d)-----------------------" * 2 + "&P:<<" + "(dC)(dC) (dC) (dC)(dC)     " * 2 + "(DCCCCe<E>)(DCCCCe<E>) (DCCCCe<E>) (DCCCCe<E>)(DCCCCe<E>)     " * 2 + "/180")
      val arska   = this.addBot(world, 3, 2, West)
      val snap    = this.addBot(world, 4, 0, West)
      val crackle = this.addBot(world, 3, 5, North)
      val pop     = this.addBot(world, 0, 3, East)
      arska.brain   = Psychobot("Arska", arska)
      snap.brain    = Nosebot("Snap!", snap)
      crackle.brain = Nosebot("Crackle!", crackle)
      pop.brain     = Nosebot("Pop!", pop)
    }
  }

  object OneOfEach extends MapScenario("One of each") {

    def isAvailable = Nosebot.isUsable && Staggerbot.isUsable && Lovebot.isUsable && Psychobot.isUsable

    protected val map =
      Seq("#................",
          "######........###",
          "#....#........#..",
          "#...###......###.",
          "#....#........#..",
          "#................",
          "......#..........",
          ".##..#######...#.",
          ".##...#....######",
          "...........#...#.")

    protected def addBots(world: RobotWorld) = {
      val romu   = this.addBot(world, 12, 1, West)
      val niina  = this.addBot(world, 5, 8, North)
      val pertsa = this.addBot(world, 2, 2, North)
      val lauri  = this.addBot(world, 16, 0, South)
      val daniel = this.addBot(world, 9, 5, South)
      romu.brain   = Spinbot("Romu", romu)
      niina.brain  = Nosebot("Niina", niina)
      pertsa.brain = Psychobot("Pertsa", pertsa)
      lauri.brain  = Lovebot("Lauri", lauri, niina)
      daniel.brain = Staggerbot("Daniel", daniel, Integer.valueOf(133))
    }
  }

  abstract class RandomScenario(name: String) extends Scenario(name) {

    def makeDirection() = {
      CompassDir.Clockwise.randomElement()
    }

  }

  abstract class BasicRandomScenario(name: String, val acceptedTypes: Seq[RobotType[RobotBrain]]) extends RandomScenario(name) {

    def isAvailable = this.acceptedTypes.exists( _.isUsable )

    protected def generateWorld(width: Int, height: Int, walls: Int, robots: Int) = {
      val world = new RobotWorld(width, height)
      val free = (for (y <- 0 until height; x <- 0 until width) yield new GridPos(x + 1, y + 1))
      val targets = new util.Random().shuffle(free).take(walls + robots)
      val (wallLocs, robotLocs) = targets.splitAt(walls)
      wallLocs.foreach( world.addWall(_) )
      val bodies = robotLocs.map( world.addRobot(_, this.makeDirection() ))
      var number = 0
      for (body <- bodies) {
        number += 1
        body.brain = Some(this.makeBrain(body, number))
      }
      world
    }

    def makeBrain(body: RobotBody, robotNumber: Int) = {
      import scala.util.Random
      val name = "Random " + robotNumber
      val types = this.acceptedTypes.filter( _.isUsable )
      val randomType = types.randomElement()
      randomType.instantiateRandom(name, body)
    }

  }

  class RandomCustom(name: String, acceptedTypes: Seq[RobotType[RobotBrain]]) extends BasicRandomScenario(name, acceptedTypes) {

    def setup(locator: Component) = {
      for {
        width  <- this.requestWidth(locator)
        height <- this.requestHeight(locator)
        walls  <- this.requestWallCount(locator)
        robots <- this.requestRobotCount(locator)
      } yield this.generateWorld(width, height, walls, robots)
    }

  }

  class RandomDefault(name: String, size: Int) extends BasicRandomScenario(name, RobotType.All.filterNot( _ == Psychobot )) {
    def setup(locator: Component) = {
      Some(this.generateWorld(size, size, (size * size) / 25, (size * size) / 6))
    }
  }



}