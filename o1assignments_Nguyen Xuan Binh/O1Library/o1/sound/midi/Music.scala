
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.sound.midi

import o1.util.ConvenientInt


/** This companion object of [[Music class `Music`]] just provides a factory method. */
object Music {
  /** Constructs a music object from the given string, which must follow the format specified in
    * the [[o1.sound.midi package overview]]. An `IllegalArgumentException` is throw if it doesn’t.
    *
    * (This is equivalent to parsing the string with [[parse]].)  */
  def apply(musicString: String): Music = parse(musicString)
}


/** Represents a piece of music that may consist of multiple [[Voice]]s, which in turn consist
  * of [[MusicElem]]s such as notes.
  *
  * Instead of using the constructor, you can use the [[Music$ companion object]] to construct a
  * `Music` object from a string: e.g., `Music("cdefg&>gfedc")`.
  *
  * @param tempoSetting  a tempo setting (in beats per minute) for the music, if there is one
  * @param voices        the voices that, played simultaneously, make up the piece of music;
  *                      no more than [[MaxVoices]] plus a possible percussion track */
case class Music(val tempoSetting: Option[Int], val voices: Seq[Voice]) {
  import Music._

  if (tempoSetting.exists( _ <= 0 ))                       throw new IllegalArgumentException(s"Tempo needs to be a positive integer.")
  if (voices.filterNot( _.isPercussion ).size > MaxVoices) throw new IllegalArgumentException(s"A maximum of $MaxVoices tracks in parallel plus percussion track allowed.")
  if (voices.count( _.isPercussion ) > 1)                  throw new IllegalArgumentException("No more than one percussion track allowed.")

  /** The tempo of the music in beats per minute. This equals [[tempoSetting]]
    * or [[DefaultTempo]], if that’s not set. */
  lazy val tempo: Int = tempoSetting.getOrElse(DefaultTempo)


  /** whether the `Music` object has any [[MusicElem]]s at all */
  lazy val nonEmpty: Boolean = this.voices.exists( _.nonEmpty )


  /** Plays this music using the MIDI synthesizer. This is equivalent to calling
    * [[o1.sound.midi.play(music:String)* o1.sound.midi.play]]. Students have access
    * to the same functionality via the `play` method on the top-level package [[o1]]. */
  def play(): Unit = {
    o1.sound.midi.play(this)
  }


  /** Returns the underlying MIDI sequence. */
  def toMidi: Sequence = {
    val seq = new Sequence(Sequence.PPQ, PPQ)
    val (percussion, regular) = this.voices.partition( _.isPercussion )
    regular.filter( _.nonEmpty ).foreach( _.addToSequence(seq) )
    percussion.foreach( _.addToSequence(seq) )
    seq
  }

}


/** Represents a single voice within a piece of [[Music]]. A voice consists of [[MusicElem]]s,
  * primarily notes, in order.
  *
  * @param notes         the notes (and possible other [[MusicElem]]s) that compose this voice
  * @param isPercussion  whether the notes should not be interpresed as regular notes but as the
  *                      special sounds defined for the [[http://www.midi.org/techspecs/gm1sound.php
  *                      MIDI standard]]’s percussion channel */
case class Voice(val notes: Seq[MusicElem], val isPercussion: Boolean) {
  import Music._
  import javax.sound.midi.ShortMessage._

  /** whether the voice has any [[MusicElem]]s at all */
  lazy val nonEmpty: Boolean = this.notes.nonEmpty

  /** the total `length` (duration) of all the notes in the voice */
  lazy val length: Int = this.notes.map( _.length ).sum


  private[midi] def addToSequence(seq: Sequence): Unit = {
    val nextFree    = seq.getTracks.length
    val trackNumber = if (this.isPercussion) PercussionChannel else if (nextFree < PercussionChannel) nextFree else nextFree + 1
    val track       = seq.createTrack()
    track.add(new MidiEvent(new ShortMessage(STOP), trackPosition(this.length + PPQ / 4)))
    var position      = 0
    var currentOctave = DefaultOctave

    def addEvent(onOrOff: Int, note: Note, position: Int) = {
      track.add(new MidiEvent(new ShortMessage(onOrOff, trackNumber, note.pitch.midiID(currentOctave), note.volume), trackPosition(position)))
    }

    def processElement(element: MusicElem): Unit = {
      element match {
        case note: Note         => addEvent(NOTE_ON, note, position); addEvent(NOTE_OFF, note, position + note.audibleLength)
        case chord: Chord       => chord.notes.foreach(processElement)
        case Instrument(number) => track.add(new MidiEvent(new ShortMessage(PROGRAM_CHANGE, trackNumber, number - 1, 0), trackPosition(position)))
        case OctaveShift(shift) => currentOctave += shift
        case Pause              => // nothing
      }
    }

    for (element <- this.notes) {
      processElement(element)
      position += element.length
    }
  }
}

