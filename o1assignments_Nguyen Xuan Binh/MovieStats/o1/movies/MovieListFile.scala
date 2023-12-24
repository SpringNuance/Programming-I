package o1.movies

import o1.util.readFileLines

////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

class MovieListFile(val fileName: String) {

  val movies = {
    val linesInVector = readFileLines("o1/movies/" + this.fileName).getOrElse(Vector()) // naive error handling used in this example: empty result if no such file
    this.parse(linesInVector)
  }

  private def parse(listItems: Vector[String]): Vector[Movie] = {
    val tokenized = listItems.map( _.split(";") )
    tokenized.map( tokens => new Movie(tokens(0), tokens(1).toInt, tokens(2).toInt, tokens(3).toDouble, tokens(4).split(",").toVector) )
  }

  override def toString = this.fileName + " (contains: " + this.movies.size + " movies)"

}