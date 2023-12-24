
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.football3.gui

import scala.swing._
import java.awt.{Color, Font}
import o1.football3._
import Swing._


/** A `MatchPanel` is a GUI component that is capable of displaying the current
  * status of a single `Match`. It uses one of two color schemes, depending on
  * whether the displayed match has finished or not.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how
  * this class works or can be used.''' */
class MatchPanel(val isFinished: Boolean) extends Component {

  private var shownGame: Option[Match] = None
  private val backgroundColor = if (this.isFinished) Color.black else Color.green.darker.darker
  private val mainTextColor = Color.white
  private val dynamicColor = if (this.isFinished) Color.green else Color.cyan
  private val mainFont = new Font("Monospaced", Font.BOLD, 24)
  if (!this.isFinished) {
    this.border = CompoundBorder(EmptyBorder(5), LineBorder(Color.white))
  }

  this.preferredSize = new Dimension(800, 160)

  def game = this.shownGame

  def game_=(shown: Match) = {
    this.shownGame = Some(shown)
    this.repaint()
  }

  override def paintComponent(g: Graphics2D) = {

    g.setColor(this.backgroundColor)
    g.fillRect(0, 0, this.size.width, this.size.height)

    if (this.game.isEmpty) {
      g.drawString("Create matches by clicking New", 50, 50)
    }
    for (game <- this.game) {
      g.setColor(this.mainTextColor)
      g.setFont(this.mainFont)
      drawCentered(g, game.location, 50)
      drawCentered(g, pad(game.home.name, 19, true) + "          " + pad(game.away.name, 19, false), 75)
      g.setColor(this.dynamicColor)
      drawCentered(g, pad(game.homeGoals.toString, 3, true) + " - " + pad(game.awayGoals.toString, 3, false), 75)
      val scorerFromStudentCode: Option[Player] = game.winningScorer
      for (scorer <- scorerFromStudentCode) {
        drawCentered(g, "Winning goal:", 100)
        drawCentered(g, s"${scorer.name} (${scorer.employer.abbreviation})", 125)
      }
    }

    def pad(text: String, width: Int, left: Boolean) = {
      val padding = " " * (width - text.length)
      if (left) padding + text else text + padding
    }

    def drawCentered(g: Graphics2D, text: String, y: Int) = {
      val textMetrics = g.getFontMetrics()
      val centeredX = (this.size.width / 2) - (textMetrics.stringWidth(text) / 2)
      g.drawString(text, centeredX, y)
    }

  }

}

