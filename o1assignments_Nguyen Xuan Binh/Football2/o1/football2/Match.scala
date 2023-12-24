package o1.football2

import smcl.infrastructure.exceptions.UnsupportedOperationByPlatformError

import scala.collection.mutable.Buffer


/** The class `Match` represents match results in a football match statistics program.
  * A match is played between teams from two clubs: a home club and an away club.
  * Goals scored by players of either team can be added to the match object with the
  * method `addGoal`.
  *
  * The class is expected to be used so that a match object with no goals is initially
  * created as a real-life match starts. Goals are added incrementally as the match
  * progresses. (A match object has mutable state.)
  * The new Match class should keep track of not just the number of goals scored but also the scorers.
  * The old methods addHomeGoal and addAwayGoal have been replaced by a single addGoal method that is expected to record a goal for either team,
  * depending on who scored it. What’s more, our wish list now features a number of new methods. All this requires quite a makeover of class Match.
  *
  * There is a partial implementation of Match in Match.scala. It’s a pretty good start: There are two instance variables for two buffers that
  * let you keep track of the goalscorers. There is a sketch of the new addGoal method. Also given is the new method winner,
  * which now begets an error only because the class is otherwise incomplete.
  *
  * Your job is to complete the class by finishing addGoal and adding the missing methods.
  *
  * Instructions and hints
  * Package o1.football2.gui contains FootballApp: a simple program for interactive testing. In order to work,
  * it needs a Match class that has the specified methods. Once your class is ready for testing, you can run FootballApp
  * and experiment with it in the window that pops up:
  *
  * ../_images/football2_gui.png
  * Use instance variables to refer to buffers and Player objects as suggested in the starter code.
  *
  * See if you can find uses for some of the methods introduced in this chapter.
  *
  * Do not edit Player or Club.
  *
  * Some of the missing methods you already implemented in Chapter 3.5. You can copy those parts of your earliper solution into Football2.
  *
  * If you didn’t do the earlier assignment on Football1, start with that. (You can also see its example solution.)
  * FootballApp only adds goals and reports the scorer of the winning goal. You can additionally experiment with your class in the REPL or
  * write a text-based test app of your own.
  *
  * class Category(val name: String, val unit: String) {
  *
  * private val experiences = Buffer[Experience]()
  * // We still need to add the variable fave here.
  *
  * def favorite = this.fave
  *
  * def addExperience(newExperience: Experience) = {
  * this.experiences += newExperience
  * // We still need to update fave here.
  * }
  * def allExperiences = this.experiences.toVector
  * }
  * @param home  the club whose team plays at home in the match
  * @param away  the club whose team plays away in the match */

class Match(val home: Club, val away: Club) {

  private val homeScorers = Buffer[Player]()    // container: goalscorers of the home team are added here
  private val awayScorers = Buffer[Player]()    // container: goalscorers of the away team are added here
  private var goals = 0
  private var homeCount = 0
  private var awayCount = 0
  /** Returns the number of goals that have been scored (so far) by the home team. */

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
    // TODO: not complete yet!
  def winningScorerName = {
    if(this.homeGoals > this.awayGoals){
  s"${homeScorers(awayScorers.size).name}"
    } else if (this.homeGoals < this.awayGoals) {
      s"${awayScorers(homeScorers.size).name}"
    } else {
      s"no winning goal"
    }
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

  def winnerName = {
    if (this.goalDifference < 0)
      this.away.name
    else if (this.goalDifference > 0)
      this.home.name
    else
      "no winner"
  }

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
