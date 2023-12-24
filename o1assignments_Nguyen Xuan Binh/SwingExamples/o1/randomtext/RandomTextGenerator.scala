package o1.randomtext


////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary that
// you understand or even look at the code in this file.
// However, you do need to be able to use the class, so
// read the Scaladoc!
//////////////////////////////////////////////////////////////

import scala.util._

/** Instances of the class `RandomTextGenerator` provide a specific form of functionality:
  * they can take in some text as source data and produce randomly generated nonsense that
  * resembles that text.
  *
  * A random text generator is characterized by its "level of coherence". This is an
  * integer between 1 and 12 that affects the type of nonsense that the generator produces:
  *
  *  - Level 1: The generator produces a jumble of random letters whose
  *    frequencies are similar to those in the original document.
  *
  *  - Level 2: The generator produces something that vaguely looks like
  *    an imaginary human language.
  *
  *  - Level 3: The generator produces text that definitely looks like
  *    some kind of human language but makes little to no sense.
  *
  *  - Level 4: The generator produces gibberish that occasionally makes
  *    sense.
  *
  *  - Levels 5 to 12: The generator produces increasingly coherent
  *    nonsense text.
  *
  * The above assumes that the source was a suitable human-readable text. Giving something
  * else as the source is liable to produce something less interesting.
  *
  * @param coherence  the level of coherence of this generator, as defined above */
class RandomTextGenerator(coherence: Int) {
  import RandomTextGenerator._

  private val depth = coherence.max(1).min(MaximumDepth)


  /** Returns a fragment of random text generated based on the text in the indicated plain text file.
    * @param sourceFileOrURL  either the name of a local plain text file in the working directory
    *                         (module folder) or a web address pointing to a plain text file somewhere on the web.
    *                         E.g. `"myfile.txt"` or `"http://www.textfiles.com/etext/FICTION/alice.txt"`.
    *                         (Local files work faster.) Please provide a plain text file, not a Word document,
    *                         HTML page or some such, unless you want really weird (but boring) output.
    * @return random text or an error message string if something went wrong */
  def randomize(sourceFileOrURL: String) = {
    if (sourceFileOrURL.trim.isEmpty) {
      "No source text file name or URL given."
    } else {
      val input = sourceText(sourceFileOrURL.trim, this.depth, MaximumInputSize)
      input.map( this.createStreamOfNonsense(_) ) match {
        case Success(nonsense) => nonsense.take(OutputSize).mkString
        case Failure(problem)  => "Could not generate text. Failed to read the source:\n  " + problem
      }
    }
  }

  private def createStreamOfNonsense(data: String) = {
    import o1.util.ConvenientSeq

    val starts = if (this.depth > 1) {
      val possibleStarts = data.toSeq.sliding(this.depth - 1).map( _.unwrap ).toSeq
      val preferredStarts = possibleStarts.filter( slice => slice.length < 2 || (slice(0).isUpper && slice(1).isLower) )
      if (preferredStarts.nonEmpty) preferredStarts else possibleStarts
    } else {
      Seq("")
    }

    val charDistribution = new CharDistribution(data, this.depth)

    def nonsense(previous: String): LazyList[Char] = {
      val next = charDistribution.generateRandomChar(previous)
      if (previous.nonEmpty)
        previous.head #:: nonsense(previous.tail + next)
      else
        next #:: nonsense(previous)
    }

    nonsense(starts.randomElement())
  }

}


private class CharDistribution(data: String, depth: Int) {
  import scala.collection.immutable.TreeMap
  import scala.collection.immutable.SortedMap
  import Function.tupled

  type SliceFreqs = Map[String, Int]
  type FollowerFreqs = Map[Char, Int]         // e.g., b->2, a->5, c->1
  type CumulativeFreqs = SortedMap[Int, Char] // e.g., 2->b, 7->a, 8->a

  private val cumulativeFreqsByPrelude: Map[String, CumulativeFreqs] = {
    def prelude(slice: String, freq: Int) = slice.init
    def followerFreq(slice: String, freq: Int) = (slice.last, freq)
    def followerFreqs(sliceFreqs: SliceFreqs) = sliceFreqs.map(tupled(followerFreq))
    def accumulate(accumulatingFreq: (Int, Char), nextFollowerFreq: (Char, Int)) =
      (accumulatingFreq, nextFollowerFreq) match {
        case ((freqSoFar, _), (follower, freq)) => ((freqSoFar + freq), follower)
      }
    def cumulativeFrequency(sliceToFreq: FollowerFreqs) = TreeMap(sliceToFreq.toSeq.scanLeft(0 -> ' ')(accumulate): _*)

    import o1.util.ConvenientCollection
    val allSlicesInData        = data.toSeq.sliding(depth).map( _.unwrap ).toSeq
    val sliceFreqs             = allSlicesInData.frequencies
    val sliceFreqsByPrelude    = sliceFreqs.groupBy(tupled(prelude))
    val followerFreqsByPrelude = sliceFreqsByPrelude.view.mapValues(followerFreqs)
    followerFreqsByPrelude.mapValues(cumulativeFrequency).toMap
  }

  def generateRandomChar(precedingChars: String) = {
    val cumulativeFreqs = this.cumulativeFreqsByPrelude(precedingChars)
    val max = cumulativeFreqs.last._1
    val randomKey = Random.nextInt(max) + 1
    cumulativeFreqs.minAfter(randomKey) match {
      case Some((freq, randomChar)) => randomChar
      case None => throw new AssertionError("Failed to pick a random character from a frequency distribution.")
    }
  }
}



private object RandomTextGenerator {
  import scala.io.Source
  import o1.util._

  val OutputSize = 2000
  val MaximumInputSize = 100000
  val MaximumDepth = 12

  def sourceText(urlOrFilename: String, minimumInputSize: Int, maximumInputSize: Int) = {
    def grabInput(input: Source) = input.take(maximumInputSize).mkString.replaceAll("\\s+", " ")
    def validated(input: String) = if (input.length >= minimumInputSize) Success(input) else Failure(new Exception("Too little data to work with."))
    for {
      source <- tryForSource(urlOrFilename)
      input  = useAndClose(source)(grabInput)
      result <- validated(input)
    } yield result
  }

}
