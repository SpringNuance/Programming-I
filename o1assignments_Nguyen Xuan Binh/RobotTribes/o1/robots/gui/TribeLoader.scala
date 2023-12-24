package o1.robots.gui

import o1.robots.tribal._
import o1.util._
import o1.gui.Dialog._
import scala.collection.immutable.SortedMap
import scala.util.control.NonFatal
import o1.util.localURL
import java.nio.file.Paths

////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////


/** The singleton object `TribeLoader` is capable of checking what robot
  * tribes are available in a particular folder, and loading the ones that are.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this object
  * works or can be used.''' */
object TribeLoader {

  private val StandardTribeNames = Seq("guardian", "tiger", "patrolman", "headless", "bunny")
  val Suffix = ".tribe"
  private def tribeName(path: Path) = path.fileName.dropRight(Suffix.length)
  private def isTribeFile(candidate: Path) = candidate.fileName.endsWith(Suffix) && candidate.fileName.length > Suffix.length

  val All = loadTribes
  val BunnyTribe = All.values.flatten.find( _.name == "bunny" )

  type TribeMap = SortedMap[Path, Option[Tribe]]


  private def loadTribes: TribeMap =
    this.tribesDir.flatMap(loadTribeDir).getOrElse(SortedMap())


  private def tribesDir = {
    val name = "tribes/"
    val folderUnderWorkingDir = Path(name)
    if (folderUnderWorkingDir.isReadable) {
      Some(folderUnderWorkingDir)
    } else {
      val folderUnderClassDir = localURL(name).map( url => Paths.get(url.toURI) )
      folderUnderClassDir.filter( _.isReadable )
    }
  }


  private def loadTribeDir(dir: Path): Option[TribeMap] = {
    def isStandard(dir: Path) = StandardTribeNames.exists( name => dir.endsWith(name + Suffix) )
    val newestFirstOrder   = Ordering.by( (p: Path) => p.lastModified ).reverse
    val standardTribeOrder = Ordering.by( (p: Path) => StandardTribeNames.indexOf(tribeName(p)) )
    for (tribeFiles <- dir.listFiles(isTribeFile)) yield {
      val (standardTribes, customTribes) = tribeFiles.partition(isStandard)
      val sortedTribes = customTribes.sorted(newestFirstOrder) ++ standardTribes.sorted(standardTribeOrder)
      val nameTribePairs = for (file <- tribeFiles) yield file -> this.readTribe(file)
      SortedMap(nameTribePairs:_*)(Ordering.by(sortedTribes.indexOf))
    }
  }


  private def readTribe(path: Path): Option[Tribe] =
    Try(new Tribe(tribeName(path), path.toString)) match {
      case Failure(roboSpeakProblem: TribeFileException) =>
        display("Problem in the RoboSpeak file " + path.fileName + ":\n" + roboSpeakProblem.getMessage, Centered)
        System.err.println(roboSpeakProblem)
        None
      case Failure(NonFatal(otherProblem)) =>
        System.err.println(otherProblem)
        throw otherProblem
      case Success(tribe) =>
        Some(tribe)
    }


}

