
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.sound.midi

import o1.util.ConvenientInt

private object Pitch {
  val OctaveSemitones = "C D EF G A H"
  val MinID           = 0
  val MaxID           = 127
}

/** A `Pitch` is a frequency for a note to be played at.
  *
  * @param name        the name of the corresponding note, which provides the base frequency;
  *                    one of "cdefgah" or their upper-case equivalents
  * @param accidental  an adjustment to the base frequency
  * @param octave      the number of the octave relative to [[DefaultOctave]]; e.g., -2 means two octaves
  *                    lower than the default; passing in `None` has the same effect as passing iuzero */
case class Pitch(val name: Char, val accidental: Accidental, val octave: Option[Int]) extends OccursInChord {
  import Pitch._

  private val numberWithinOctave = OctaveSemitones.indexOf(this.name.toUpper)


  private[midi] def stringID = {
    val octaveDiff = this.octave.map( _ - DefaultOctave ).getOrElse(0)
    val octaveMark = if (octaveDiff < 0) "<" * -octaveDiff else ">" * octaveDiff
    octaveMark + this.name + this.accidental
  }

  private[midi] def midiID(defaultOctave: Int) = {
    val octave = this.octave.getOrElse(defaultOctave)
    val octaveStartID = OctaveSemitones.length * octave
    val globalNumber = octaveStartID + this.numberWithinOctave + this.accidental.shift
    globalNumber atLeast MinID atMost MaxID
  }

  private[midi] def whenInChord(chordLength: Int, chordIsStaccato: Boolean) = Note(this, chordLength, chordIsStaccato)

}

/** An adjustment to the frequency of a [[Pitch]]: a [[Flat]], a [[Sharp]], or a [[Natural]]. */
sealed trait Accidental extends Product with Serializable {
  /** the shift from the base frequency caused by the accidental (in semitones).
    * A positive number means a higher frequency, a negative number a lower one. */
  def shift: Int
}

/** Represents the concept of a flat note. Assigning this accidental to a [[Pitch]]
  * makes it slightly lower. */
case object Flat extends Accidental {
  /** the shift from the base frequency is -1 for a `Flat` note: one semitone lower */
  val shift = -1
  /** a string representation of the accidental: "♭" */
  override def toString = "♭"
}

/** Represents the concept of a sharp note. Assigning this accidental to a [[Pitch]]
  * makes it slightly higher. */
case object Sharp extends Accidental {
  /** the shift from the base frequency is +1 for a `Sharp` note: one semitone higher */
  val shift = 1
  /** a string representation of the accidental: "#" */
  override def toString = "#"
}

/** Represents the concept of a natural note (neither [[Sharp]] nor [[Flat]]. */
case object Natural extends Accidental {
  /** the shift from the base frequency is zero for a `Natural` note: no shift at all */
  val shift = 0
  /** a string representation of the accidental: the empty string */
  override def toString = ""
}

