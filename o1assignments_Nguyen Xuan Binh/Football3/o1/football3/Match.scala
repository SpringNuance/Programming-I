package o1.football3

import scala.collection.mutable.Buffer



class Match(val home: Club, val away: Club) {

  private val homeScorers = Buffer[Player]()    // container: goalscorers of the home team are added here
  private val awayScorers = Buffer[Player]()    // container: goalscorers of the away team are added here
  private var goals = 0
  private var homeCount = 0
  private var awayCount = 0
  private var winnerClub:Option[Club] = None
  private var winnerScorer: Option[Player] = None


  def winningScorer = {
     if (this.isHomeWin) {
       winnerScorer = Some(homeScorers(awayScorers.size))
       winnerScorer
     } else if (this.isAwayWin) {
       winnerScorer = Some(awayScorers(homeScorers.size))
       winnerScorer
     } else {
       winnerScorer = None
       winnerScorer
     }
  }

  def winnerName = {
    this.winner match {
      case Some(club) => s"$club"
      case None => s"no winner"
    }
  }

  def winner = {
     if (this.isHomeWin) {
       winnerClub = Some(home)
       winnerClub
     } else if (this.isAwayWin) {
       winnerClub = Some(away)
       winnerClub
     } else {
       winnerClub = None
       winnerClub
     }

  }



  def addGoal(scorer: Player): Unit = {
    if (scorer.employer == this.home) {
      this.homeScorers += scorer
      homeCount += 1
    } else {
      this.awayScorers += scorer
      awayCount += 1
    }
      goals += 1
  }


  def allScorers = (homeScorers ++ awayScorers).toVector

  def hasScorer(scorer: Player) = {
    if (allScorers.indexOf(scorer) >= 0) true else false
  }
  def homeGoals = this.homeCount
  /** Returns the number of goals that have been scored (so far) by the away team. */
  def awayGoals = this.awayCount

  /** Returns the total number of goals scored by the two teams. */
  def totalGoals = goals

  /** Determines whether this match is entirely goalless, that is, whether neither
    * team has scored a single goal. */
  def isGoalless = if (this.totalGoals > 0) false else true


  /** Returns a boolean value indicating whether the home team won (or would win
    * if the match ended with the current score). Tied scores produce `false`. */
  def isHomeWin = this.homeGoals > this.awayGoals


  /** Returns a boolean value indicating whether the away team won (or would win
    * if the match ended with the current score). Tied scores produce `false`. */
  def isAwayWin = this.homeGoals < this.awayGoals


  /** Returns a boolean value indicating whether the game ended in a draw (or
    * would do so if the match ended with the current score). */
  def isTied = this.homeGoals == this.awayGoals


  /** Determines whether this match has a higher total score than another given match.
    * @return `true` if more goals were scored in total in this match than in the given match, `false` otherwise */
  def isHigherScoringThan(anotherMatch: Match) = this.totalGoals > anotherMatch.totalGoals


  /** Returns the goal difference of the match. The sign of the number indicates
    * which team scored more goals.
    * @return the goal difference as a positive number if the home team won, a
    *         negative number if the away team won, or zero in case of a tied match */
  def goalDifference = this.homeGoals - this.awayGoals

  def location = home.stadium

  override def toString = {
    if (this.homeGoals < this.awayGoals) {
    s"${home.name} vs. ${away.name} at ${location}: ${awayGoals}-${homeGoals} to ${away.name}"
    }
    else if (this.homeGoals > this.awayGoals) {
    s"${home.name} vs. ${away.name} at ${location}: ${homeGoals}-${awayGoals} to ${home.name}"
    }
    else if (this.homeGoals == 0 && this.awayGoals == 0) {
         s"${home.name} vs. ${away.name} at ${location}: tied at nil-nil"
         }
         else s"${home.name} vs. ${away.name} at ${location}: tied at ${homeGoals}-all"
    }

}