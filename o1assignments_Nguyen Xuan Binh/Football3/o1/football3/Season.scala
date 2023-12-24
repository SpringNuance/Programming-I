package o1.football3

import scala.collection.mutable.Buffer

class Season() {

  private val matches = Buffer[Match]()
  private var biggestWinClub: Option[Match] = None

  def matchNumber(x:Int) = {
    if (matches.isEmpty){
      None
    } else matches.lift(x)
  }

  def latestMatch: Option[Match] = {
    if (matches.isEmpty){
      None
    } else Some(matches.last)
  }

  def numberOfMatches = matches.size

  def addResult(newMatch: Match) = {
    this.matches += newMatch
    this.biggestWinClub match {
      case None => this.biggestWinClub = Some(newMatch)
      case Some(oldMatch) => {
        var newBiggest = if ((newMatch.goalDifference).abs > (oldMatch.goalDifference).abs) newMatch else oldMatch
      this.biggestWinClub = Some(newBiggest)
      }
    }
  }

  def biggestWin()= {
    if (matches.isEmpty){
      None
    } else this.biggestWinClub
  }
}

