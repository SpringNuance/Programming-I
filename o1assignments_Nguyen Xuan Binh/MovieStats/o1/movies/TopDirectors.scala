package o1.movies

// This program is associated with Chapter 9.2.

object TopDirectors extends App {

  val allMovies          = new MovieListFile("omdb_movies_2015.txt").movies              // Vector[Movie]
  val allDirectors       = allMovies.flatMap( _.directors )                              // Vector[String]
  val groupedByDirector  = allDirectors.groupBy( dir => dir )                            // Map[String,Vector[String]]
  val countsByDirector   = groupedByDirector.map( entry => (entry._1 -> entry._2.size) )  // Map[String,Int]
  val directorCountPairs = countsByDirector.toVector
  val directorsSorted    = directorCountPairs.sortBy( -_._2 )

  for ((director, count) <- directorsSorted) {
    println(s"$director: $count movies")
  }

}