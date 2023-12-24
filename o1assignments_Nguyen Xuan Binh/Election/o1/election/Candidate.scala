package o1.election


/** Represents a candidate in an election results application.
  *
  * A candidate object is immutable.
  *
  * @param name   the candidate's name
  * @param votes  the number of votes received by the candidate
  * @param party  the name (or abbreviation) of the candidate's party */
class Candidate(val name: String, val votes: Int, val party: String) {


  /** Returns a short textual description of the candidate. */
  override def toString = s"$name ($party): $votes"


  /** Determines if the candidate has more votes than the other, given candidate. */
  def >(another: Candidate) = this.votes > another.votes


}

