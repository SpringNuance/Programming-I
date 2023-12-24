package o1.sentiment

import SentimentAnalyzer._
import o1.util.{localSource,useAndClose}
import scala.io.Source


/** An instance of this class is a "sentiment analyzer" taught on a corpus of texts whose
  * sentiments have been appraised and annotated by a human. Given a roughly similar piece
  * of text as those that appear in its training data, the analyzer can assign the text
  * a numerical rating that reflects how positive or negative it is; see the [[rate]] method.
  *
  * One does not simply create `SentimentAnalyzer` objects with `new`. The `SentimentAnalyzer`
  * companion object provides methods for initializing analyzer objects from training data. */
class SentimentAnalyzer private(private val recordedSentiments: Map[String, WordSentiment]) {


  /** Given a short piece of text, returns a numerical estimate of how positive or negative
    * it is. The estimate is based on whether the words in the given text tend to appear in
    * positive or negative texts previously familiar to the analyzer from its training data.
    *
    * The analysis method is quite straightforward: the rating assigned to the given sentiment
    * is simply the average of the rating of each individual word in it. Those word ratings in
    * turn are the averages of the human-rated texts in which they appear in the training data.
    *
    * @param wordOrPhrase  a (short) piece of text to be analyzed
    * @return a numerical rating between -2.0 and +2.0. Positive values suggest positive
    *         sentiments. `None` is returned in case none of the words in `wordOrPhrase`
    *         appear in the training data and no rating can therefore be determined. */
  def rate(wordOrPhrase: String) = {
    def averageSentiment(word: String) = this.recordedSentiments(word).average
    val averagesOfFamiliarWords = wordsOf(wordOrPhrase).flatMap(averageSentiment)
    ratio(averagesOfFamiliarWords.sum, averagesOfFamiliarWords.size)
  }

}


/** This companion object of class [[SentimentAnalyzer]] provides methods for initializing
  * a analyzer instances from training data.
  *
  * The methods expect the data to be in the following line-based format:
  *
  * {{{
  * -1 Aggressive self-glorification and a manipulative whitewash.
  * 2 A comedy-drama of nearly epic proportions rooted in a sincere performance by the title character undergoing midlife crisis.
  * -1 Narratively, Trouble Every Day is a plodding mess.
  * 1 The Importance of Being Earnest, so thick with wit it plays like a reading from Bartlett's Familiar Quotations
  * 0 It's everything you'd expect -- but nothing more.
  * }}}
  *
  * That is, each line of input should start with a sentiment rating of -2, -1, 0, 1, 2.
  * A negative numbers means that the rest of the line expresses a negative sentiment, as
  * determined by a human rater; positive numbers correspondingly reflect positive sentiments. */
object SentimentAnalyzer {


  /** Given the path to a file that contains training data, returns a [[SentimentAnalyzer]]
    * trained on that data.
    *
    * '''N.B.''' This method is unsafe: it will crash with an error if it fails to read the file.
    * If it encounters invalid lines, the method will emit warnings and ignore that data. */
  def fromFile(trainingDataPath: String) = localSource(trainingDataPath) match {
    case Some(file) => this.fromSource(file)
    case None       => throw new RuntimeException("Failed to load sentiment data from file $file.")
  }


  /** Given a `Source` that contains training data, returns a [[SentimentAnalyzer]]
    * trained on that data.
    *
    * '''N.B.''' This method is unsafe: it will crash with an error if it fails to read the source.
    * If it encounters invalid lines, the method will emit warnings and ignore that data. */
  def fromSource(trainingData: Source) = {
    import collection.mutable.Map
    val sentimentsForWord = Map[String,WordSentiment]() withDefaultValue NoneFound

    def parse(line: String): Option[(Int, Seq[String])] = {
      val (sentimentString, review) = line.span( char => !char.isWhitespace )
      sentimentString.toIntOption.map( initialNumber => (initialNumber, wordsOf(cleanText(review))) )
    }

    def addWord(word: String, humanRating: Int) = {
      val oldTotal = sentimentsForWord(word)
      sentimentsForWord(word) = oldTotal + humanRating
    }

    def addLine(line: String) = parse(line) match {
      case Some((humanAssignedRating, words)) =>
        words.foreach( addWord(_, humanAssignedRating) )
      case None =>
        System.err.println(s"""Warning: Invalid sentiment rating ignored in: "$line"""")
    }

    useAndClose(trainingData)( _.getLines.foreach(addLine) )
    new SentimentAnalyzer(sentimentsForWord.toMap withDefaultValue NoneFound)
  }


  // Utilities:
  private def wordsOf(text: String) = text.split(" ").toSeq
  private def ratio(total: Double, count: Int) = if (count > 0) Some(total / count) else None

  private case class WordSentiment(val total: Int, val occurrences: Int) {
    def +(newSentiment: Int) = WordSentiment(total + newSentiment, occurrences + 1)
    def average = ratio(this.total, this.occurrences)
  }
  private val NoneFound = WordSentiment(0, 0)
}


