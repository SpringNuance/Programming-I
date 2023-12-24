
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.football3.gui

import scala.swing._
import scala.swing.event._
import o1.football3._
import o1.gui.layout._
import o1.gui.Dialog._
import o1.gui.O1AppDefaults
import o1.util.assignments._
import scala.util._
import scala.swing.ListView.IntervalMode._


/** The singleton object `FootballApp` represents a simple application that a programmer
  * can use to experiment with the package `o1.football2`.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this object works
  * or can be used, apart from the fact that you can use this file to start the program.''' */
object FootballApp extends SimpleSwingApplication with O1AppDefaults {

  def top = new MainFrame {

    this.title = "Football3 Test App"
    val content = new SeasonPanel(this)
    this.contents = content
    this.defaultButton = content.startButton
    this.resizable = false
    this.pack()
    this.centerOnScreen()

    class SeasonPanel(val owner: Window) extends EasyPanel {

      val seasonClass = new DynamicClass[AnyRef]("o1.football3.Season", Seq())
      val season = Try(seasonClass.instantiate()).map(new EnhancedSeason(_)).recover {
        case missing: InvalidSignature => println(missing.message); throw missing
      }

      if (season.isFailure) {
        println("Season statistics and the list of matches will not be shown. Individual matches can still be tracked.")
      }

      val countStat, highestStat, latestStat = new Label
      val stats = new EasyPanel {
        placeNW(countStat,   (0, 0), OneSlot, Slight, (0, 5, 3, 5))
        placeNW(latestStat,  (1, 0), OneSlot, Slight, (0, 5, 3, 5))
        placeNW(highestStat, (1, 1), OneSlot, Slight, (0, 5, 3, 5))
      }
      val matchList = new ListView[String] {
        border = Swing.EtchedBorder
        selection.intervalMode = Single
      }
      this.listenTo(matchList.selection)
      reactions += {
        case ListSelectionChanged(source, _, isAdjusting) =>
          if (!isAdjusting) {
            for (season <- this.season; game <- season(source.selection.anchorIndex)) {
              this.matchView.game = game
            }
          }
      }

      val matchView = new MatchPanel(true)

      val startButton = new Button("New...")
      this.listenTo(startButton)
      reactions += {
        case ButtonClicked(source) =>
          this.addMatch()
      }

      val playedLabel = new Label("<html><b>Played:</b></html>")
      val matchScroller = new ScrollPane(matchList) {
        this.preferredSize = new Dimension(80, this.preferredSize.height)
      }

      withStudentSolution(SeasonPanel.this) {
        this.updateStatus()
      }

      placeNW(playedLabel,   (0, 0), OneSlot, Slight,          (5, 5, 3, 5))
      placeNW(matchScroller, (0, 1), OneSlot, FillVertical(1), (0, 5, 3, 5))
      placeNW(startButton,   (0, 2), OneSlot, Slight,          (0, 5, 3, 5))
      placeNW(matchView,     (1, 0), TwoHigh, FillBoth(1, 1),  (5, 5, 3, 5))
      placeNW(stats,         (1, 2), OneSlot, Slight,          (5, 5, 3, 0))

      def addMatch() = {
        for {
          home <- requestChoice("Home team:", ExampleLeague.Clubs,                        RelativeTo(this))
          away <- requestChoice("Away team:", ExampleLeague.Clubs.filterNot( _ == home ), RelativeTo(this))
        } {
          val dialog = new OngoingMatchDialog(this.owner, home, away)
          dialog.open()
          for (newMatch <- dialog.finishedMatch; season <- this.season) {
            withStudentSolution(SeasonPanel.this) {
              season.applyDynamic[Unit]("addResult")(classOf[Match] -> arg(newMatch)) // temporarily(?) rephrased because IJ complains unnecessarily about season.addResult[Unit](classOf[Match] -> arg(newMatch))
              this.updateStatus()
              this.matchList.selectIndices(season.numberOfMatches[Int] - 1)
            }
          }
        }
      }

      private def updateStatus() = {
        for (season <- this.season) {
          val matchText =  if (season.numberOfMatches[Int] == 1) "1 match" else (season.numberOfMatches[Int].toString + " matches")
          this.countStat.text   = s"<html><b>${matchText}.</b></html>"
          this.latestStat.text  = s"<html><b>Latest:</b> ${season.latestMatch[Option[Match]].getOrElse("n/a")}</html>"
          this.highestStat.text = s"<html><b>Biggest win:</b> ${season.biggestWin[Option[Match]].getOrElse("n/a")}</html>"
          this.matchList.listData = season.matches.map( _.toShortString )
        }
      }


      // dynamic wrapper around Season, with a couple of added convenience methods
      class EnhancedSeason(wrapped: AnyRef) extends DynamicObject(wrapped) {
        def apply(n: Int) = this.applyDynamic[Option[Match]]("matchNumber")(classOf[Int] -> arg(n)) // temporarily(?) rephrased because IJ complains unnecessarily about this.matchNumber[Option[Match]](classOf[Int] -> arg(n))
        def matches = (0 until this.numberOfMatches[Int]).flatMap( this(_) )
      }


      implicit class EnhancedMatch(val game: Match) {
        def toShortString = this.game.home.abbreviation + "-" + this.game.away.abbreviation
      }

    }

  }

}


