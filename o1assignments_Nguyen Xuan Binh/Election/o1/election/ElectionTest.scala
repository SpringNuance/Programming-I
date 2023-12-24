package o1.election
package o1

/** A small program that uses the classes in this package. May be customized by the student. */
object ElectionTest extends App {

  val candidates = Vector(
    new Candidate("Outi Alanko-Kahiluoto", 2590, "VIHR"),
    new Candidate("Pentti Arajärvi",       3682, "SDP"),
    new Candidate("Paavo Arhinmäki",       6467, "VAS"),
    new Candidate("Sirpa Asko-Seljavaara", 2250, "KOK"),
    new Candidate("Juha Hakola ",          2571, "KOK"),
    new Candidate("Jussi Halla-aho",       6034, "PS"),
    new Candidate("Heidi Hautala",         2916, "VIHR"),
    new Candidate("Eero Heinäluoma",       5243, "SDP"),
    new Candidate("Päivi Lipponen",        1675, "SDP"),
    new Candidate("Silvia Modig",          2003, "VAS"),
    new Candidate("Lasse Männistö",        2469, "KOK"),
    new Candidate("Jaana Pelkonen",        3156, "KOK"),
    new Candidate("Tom Packalén",          1928, "PS"),
    new Candidate("Wille Rydman",          2241, "KOK"),
    new Candidate("Anni Sinnemäki",        2609, "VIHR"),
    new Candidate("Osmo Soininvaara",      5889, "VIHR"),
    new Candidate("Johanna Sumuvuori",     2180, "VIHR"),
    new Candidate("Astrid Thors",          2298, "RKP"),
    new Candidate("Jan Vapaavuori",        7894, "KOK")
  )
  val helsinki = new District("Mini-Helsinki", 8, candidates)
  println(helsinki)

  println("\nA list of all the candidates:")
  helsinki.printCandidates()

  println("\nCandidates from KOK and PS:")
  println(helsinki.candidatesFrom("KOK").mkString("\t"))
  println(helsinki.candidatesFrom("PS").mkString("\t"))

  println("\nTotal number of votes: " + helsinki.totalVotes)

  println("\nThe candidate with the most votes: " + helsinki.topCandidate)

  println("\nTotal votes for VAS: " + helsinki.totalVotes("VAS"))
  println("Total votes for RKP: " + helsinki.totalVotes("RKP"))


  // THE FOLLOWING CODE IS ASSOCIATED WITH CHAPTER 9.2.
  // DON'T UNCOMMENT IT BEFORE YOU REACH THAT CHAPTER.


  println("\nCandidates for each party:")
  println(helsinki.candidatesByParty.values.map( _.mkString("\t") ).mkString("\n"))

  println("\nTop candidate for each party:")
  println(helsinki.topCandidatesByParty.values.mkString("\t"))

  println("\nTotal number of votes for each party:")
  println(helsinki.votesByParty.mkString("\t"))

  // 2/2

  println("\nParties ordered by total votes:")
  println(helsinki.rankingOfParties.mkString(", "))
  //
  println("\nThe candidates ranked within their respective parties:")
  val rankingMap = helsinki.rankingsWithinParties.map( entry => entry._1 -> entry._2.mkString("\t") )
  println(rankingMap.mkString("\n"))
  //
  println("\nThe distribution figures for each candidate:")
  println(helsinki.distributionFigures.mkString("\n"))
  //
  println("\nThe elected candidates, in order by distribution figure:")
  println(helsinki.electedCandidates.map( _.name ).mkString(", "))


}