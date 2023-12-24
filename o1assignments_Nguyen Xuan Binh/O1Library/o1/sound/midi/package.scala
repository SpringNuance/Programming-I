
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.sound


import o1.util._
import scala.util.parsing.combinator.RegexParsers


/** This is one of O1’s sound packages (the other being [[o1.sound.sampled]]). This package provides
  * a simple interface to a part of the more generic MIDI API. In particular, it lets students
  * play MIDI music by writing notes a `String` and passing them to a function. That function,
  * [[play(music:String)* play]], has an alias in the top-level package [[o1]], so it’s
  * accessible to students simply via `import o1._`.
  *
  * Here is a summary of the notation used in the musical `String`s that you pass to
  * [[play(music:String)* play]] and some other functions in this package:
  *
  *  - `"cdefgah"`                plays seven notes at the default tempo of 120. ''(N.B. the seventh note is `h`, not `b`.)''
  *  - `"CDEFGAH"`                plays them louder.
  *  - `"CDEFGAH/240"`            plays them at a double tempo of 240.
  *  - `"CD E"`                   has a pause between the second and third note.
  *  - `"CD-E---"`                has a longer second note and a longer still third note.
  *  - `"C.D.E"`                  produces a staccato-like effect on the first two notes (playing them shorter followed by a pause).
  *  - `">CDE<<<CDE"`             plays three notes in a higher octave then shifts three octaves down before playing them again.
  *  - `"C7D3E"`                  plays the c in Octave #7, the d in Octave #3, and the e in the default Octave #5.
  *  - `"CbDBE#7F"`               has a c-flat, a d-flat, an e-sharp in Octave #7, and a natural f. b and B are equivalent.
  *  - `"C♭D♭E♯7F♮"`              is a fancy-pants way of writing the same thing.
  *  - `"CDE[13]CDE"`             plays three notes using the default Instrument #1, then again using Instrument #13.
  *  - `"(CEG)(DF#A)(EG#H)---"`   plays three chords, the last of which is longer.
  *  - `"CDE&<<[28]efg&[110]  F"` simultaneously plays the three parts separated by `&`s.
  *  - `"P:CDE"`                  uses the MIDI percussion channel: each "note"` represents a different percussion instrument.
  *  - `"C|D||||E"`               means the same as ̀`"cde"`: the `|`s don't do anything, but you can use them to mark bars or whatever.
  *
  * For a numbered list of the instruments, see [[http://www.midi.org/techspecs/gm1sound.php
  * the General MIDI Sound Set]]; the [[Instrument$ `Instrument` object]] contains the same
  * list as Scala constants. */
package object midi {

  private[midi] val PPQ: Int = 48

  /** The default tempo (120 beats per minute). This is used by various methods in
    * with this package when playing music, unless otherwise specified. */
  val DefaultTempo: Int = 120

  /** The number (5) of the default octave to play notes from. */
  val DefaultOctave: Int = 5

  /** the maximum number of simultaneous [[Voice]]s supported by this library.
    * This number doesn’t include the additional percussion track. */
  val MaxVoices: Int = 15


  private[midi] val LengthPerBeat: Int = 4


  private[midi] val PercussionChannel: Int = 9

  /** A number (127) that corresponds to the highest possible volume setting available to this
    * MIDI package. (The lowest is zero.) */
  val HighVolume: Int = 127


  /** A number (80) that corresponds to a “medium” volume on the scale from 0 to 127 available
    * to this MIDI package. */
  val MediumVolume: Int = 80

  private[midi] def trackPosition(position: Int): Int = PPQ * position / LengthPerBeat


  /** Parses the given musical `String` and plays the music that it describes on the
    * MIDI synthesizer. The `String` must be formatted as described in the [[o1.sound.midi
    * package overview]]. Prints out an error message in case the `String` was invalid. */
  def play(music: String): Unit = {
    Try(Music(music)) match {
      case Success(parsedMusic) =>
        this.play(parsedMusic)
      case Failure(problem)     =>
        println(s"Failed to play. Please check the string you used, which is repeated below:\n$music\nHere is the report from the parser: $problem")
    }
  }

  /** Plays the music described by the given [[Music]] object on the MIDI synthesizer. */
  def play(music: Music): Unit = {
    if (this.isInTestMode) {
      println("playing in test mode: " + music)
    } else if (music.nonEmpty) {
      Sequencer.open()
      Sequencer.start(music)
    }
  }


  /** An error type that signals the underlying Java MIDI toolkit is unavailable. */
  type MidiUnavailableException = javax.sound.midi.MidiUnavailableException

  private[midi] type MidiSystem = javax.sound.midi.MidiSystem

  private[midi] type MidiEvent = javax.sound.midi.MidiEvent

  private[midi] type ShortMessage = javax.sound.midi.ShortMessage

  private[midi] type Sequence = javax.sound.midi.Sequence


  private[midi] object Sequence {
    val PPQ: Float = javax.sound.midi.Sequence.PPQ
  }


  private[o1] implicit class MusicLiteral(val noteData: StringContext) extends AnyVal {
    def music(embeddedBits: Any*) = Music(noteData.s(embeddedBits: _*))
  }



  private val Ignored = raw"[_|\n\r]"
  private val MarginStop = '|'


  /** Parses the given musical `String` and returns the result as a [[Music]] object.
    * The `String` must be formatted as described in the [[o1.sound.midi package overview]].
    * Throws an `IllegalArgumentException` in case the `String` was invalid. */
  def parse(musicString: String): Music = {
    val cleanedMusic = musicString.stripMargin(MarginStop).replaceAll(Ignored, "")
    TheParser.parse(cleanedMusic)
  }

  private object TheParser extends RegexParsers {
    import scala.language.postfixOps

    def parse(input: String) = this.parseAll(this.music, input) match {
      case Success  (parsedMusic, _) => parsedMusic
      case NoSuccess(message,     _) => throw new IllegalArgumentException(message)
    }

    def music      = (voices | zeroVoices) ~ (tempo?)     ^^ { case voices~tempo           => Music(tempo, voices)                        }
    def tempo      = "/" ~> integer
    def moreVoices = ("&" ~> voice)*
    def zeroVoices = ""                                   ^^^ List[Voice]()
    def voices     = voice ~ moreVoices                   ^^ { case voice~voices           => voice :: voices                             }
    def voice      = ("[Pp]:".r?) ~ (songElem*)           ^^ { case percuss~elements       => Voice(elements, percuss.isDefined)          }
    def songElem   = (note|chord|pause|instrument|shift)
    def shift      = "<|>".r                              ^^ (bracket => OctaveShift(if (bracket == ">") 1 else -1))
    def pitch      = name ~ accidental ~ (octave?)        ^^ { case name~accidental~octave => Pitch(name, accidental, octave)             }
    def name       = keyName                              ^^ (name => name.head)
    def note       = pitch ~ length                       ^^ { case pitch~((len,stacc))    => Note(pitch, len, stacc)                     }
    def chord      = ("(" ~> (chordBit+) <~ ")") ~ length ^^ { case bits~((len,stacc))     => Chord(bits, len, stacc)                     }
    def chordBit   = pitch | shift
    def length     = "-*".r ~ ("."?)                      ^^ { case stay~staccato          => ((stay.length + 1) * 2, staccato.isDefined) }
    def instrument = "[" ~> integer <~ "]"                ^^ (number => Instrument(number))
    def octave     = "\\d".r                              ^^ (digit => digit.toInt)
    def integer    = "\\d+".r                             ^^ (digits => digits.toInt)
    def accidental = flat | sharp | natural
    def flat       = "b|♭|B".r                           ^^^ Flat
    def sharp      = "#|♯".r                              ^^^ Sharp
    def natural    = ("♮"?)                               ^^^ Natural
    def pause      = " ".r                                ^^^ Pause

    private val keyName = (Pitch.OctaveSemitones + Pitch.OctaveSemitones.toLowerCase).replaceAll(" ", "").mkString("|").r

    override val skipWhitespace = false

  }

  // Setting this to `true` disables actual playing of MIDI sound.
  // (This is useful for purposes of automatic testing and assessment of certain student programs.
  // Generally, there should be no need to touch this setting, which defaults to `false`.)
  private[o1] var isInTestMode = false

}

