package o1.election

import scala.collection.mutable.Buffer           // This is useful in early versions of the class.
import scala.math.Ordering.Double.TotalOrdering  // This will be useful in later assignments.

// Write your code here.

class District(val name: String, val seats: Int, val candidates: Vector[Candidate]) {

  private val listOfCandidates = candidates

  override def toString: String = s"$name: ${listOfCandidates.size} candidates, $seats seats"

  def printCandidates() = {
    this.listOfCandidates.foreach(println(_))
    //this line can be also written as def printCandidates() = this.liftOfCandidates.foreach(println)

  }

  def candidatesFrom(partyName: String) = {
    this.listOfCandidates.filter(_.party.contains(partyName))
    //                       or ( _.party == party )
  }

  def topCandidate = this.listOfCandidates.maxBy(_.votes)

  // def topCandidate = {
  //      var canWithMostVotesSoFar = this.listOfCandidates.head
  //      for (current <- this.listOfCandidates.tail) {
  //        if (current.votes > canWithMostVotesSoFar.votes) {
  //          canWithMostVotesSoFar = current
  //        }
  //      }
  //      canWithMostVotesSoFar
  //  }


  def totalVotes = this.countVotes(this.candidates)

  def totalVotes(party: String) = this.countVotes(this.candidatesFrom(party))

  private def countVotes(candidates: Vector[Candidate]) = candidates.map( _.votes ).sum
                                        // alternatively: candidates.foldLeft(0)( _ + _.votes )

/**Returns a mapping from parties to their candidates. That is, returns a Map whose keys
   are the names of all the parties that have candidates in this district. For each key,
   the value is a vector containing the candidates from that party in arbitrary order.
  See also rankingsWithinParties
  **/
  def candidatesByParty: Map[String, Vector[Candidate]] = this.candidates.groupBy(_.party)


  /** Returns a mapping from parties to their top candidates. That is, returns a Map whose keys
are the names of all the parties that have candidates in this district. For each key, the value
is the candidate from that party with the most votes.
If multiple candidates from a single party received the same number of votes, this method
chooses one of them arbitrarily.
**/
  def topCandidatesByParty: Map[String, Candidate] = {
    candidatesByParty.map(vector => vector._1 -> vector._2.maxBy(_.votes))
  }


  /** Returns a mapping from parties to their vote totals. That is, returns a Map whose keys
are the names of all the parties that have candidates in this district. For each key,
the value is the number of votes received in total by all the members of that party in this district.
**/
  def votesByParty: Map[String, Int] = candidatesByParty.map(vector => vector._1 -> totalVotes(vector._1))




/** Returns a mapping from parties to ranking lists of those parties' candidates. That is,
returns a Map whose keys are the names of all the parties that have candidates in this district.
For each key, the value is a vector containing the candidates from that party in order by the
 number of votes they received, starting with the highest. If multiple candidates from
  a single party received the same number of votes, this method orders them in an arbitrary way.
**/
  def rankingsWithinParties: Map[String, Vector[Candidate]] = {
    candidatesByParty.map(vector => vector._1 -> vector._2.sortBy(-_.votes))
  }


  /** Returns a vector containing the names of all the parties that have candidates in this district,
 ordered by the total number of votes received by each party. Descending order is used, so the party
with the most votes comes first. If multiple parties received the same number of votes,
this method orders them in an arbitrary way.
**/
  def rankingOfParties: Vector[String] = {
    votesByParty.toVector.sortBy(-_._2).map(_._1)
  }


  /**Returns a mapping of candidates to their distribution figures (Finnish: vertailuluku).
That is, returns a Map whose keys are all the candidates in this district and whose values
are the distribution figures of those candidates.
The distribution figure of a candidate is obtained as follows. Take the position (rank) of
the candidate in the ranking list within his or her own party (as defined by rankingsWithinParties)
. For instance, the most-voted-for candidate within a party has a rank of 1,
the second-most-voted-for has a rank of two, and so on. Divide the total number of votes received
   by the candidate's party by the candidate's rank, and you have the candidate's distribution figure.
  If multiple candidates from a single party received the same number of votes,
the arbitrary order chosen by rankingsWithinParties is used here, despite the fact that this is
likely to be a undesirable feature in a real-world system.
  See also
votesByParty
rankingsWithinParties
electedCandidates
  **/
  def distributionFigures: Map[Candidate, Double] = {
    def figures(candidate: Candidate): Double = votesByParty.apply(candidate.party).toDouble/(rankingsWithinParties.apply(candidate.party).indexOf(candidate) + 1)
    candidates.map(candidate => candidate -> figures(candidate)).toMap
  }


/** Returns all the candidates who will be elected on the basis of the vote.
The seats available are given to the candidates with the highest distribution figures.
If multiple candidates happen to have the same distribution figure, an arbitrary order is used,
despite the fact that this is likely to be a undesirable feature in a real-world system.

@ returns    the elected candidates in descending order by distribution figure

See also
distributionFigures
**/
  def electedCandidates: Vector[Candidate] = distributionFigures.toVector.sortBy(-_._2).map(_._1).take(seats)

}


