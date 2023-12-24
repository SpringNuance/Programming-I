package o1.sentiment.ui

import o1.sentiment._
import scala.io.StdIn._

/** This app loads training data for a [[SentimentAnalyzer]] from a text file
  * that contains movie reviews. It then interacts with the user in the text
  * console, interactively prompting them to enter movie reviews and using the
  * analyzer to report whether those reviews are positive or negative.
  * The session ends when the user enters an empty input.
  *
  * (The training data is originally from the Rotten Tomatoes review aggregator,
  * was collected by the Sentiment Analysis project at Stanford University. It
  * has been further pre-processed for educational purposes first by Eric D. Manley
  * and Timothy M. Urness, then by Juha Sorva.) */
object MovieSentimentApp extends App {

  /** A [[SentimentAnalyzer]] trained with a set of movie reviews and their human-assigned sentiment ratings. */
  val analyzer = SentimentAnalyzer.fromFile("sample_reviews_from_rotten_tomatoes.txt")

  /** Asks the user to enter a movie review and returns the input as a (slightly reformatted) string. */
  def requestMovieReview(): String = {
    cleanText(readLine("\nPlease comment on a movie or hit Enter to quit: "))
  }

  /** Given a movie review, returns a string report of whether it seems positive or negative. */
  def analyze(review: String): String = {
    def category(average: Double) = if (average >= 0) "positive" else "negative"
    analyzer.rate(review) match {
      case Some(average) => f"I think this sentiment is ${category(average)}. (Average word sentiment: $average%.2f.)"
      case None          => "No recognized words in the given text."
    }
  }
  var inputs = LazyList.continually(requestMovieReview())
  inputs.takeWhile(_ != "").map(analyze(_)).foreach(println)


  // TODO: should use the functions above to interact with the user

  println("Bye.")

}

