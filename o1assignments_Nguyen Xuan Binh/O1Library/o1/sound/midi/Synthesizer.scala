
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.sound.midi

import javax.sound.midi.MidiChannel

/** This object is an interface to the underlying MIDI synthesizer. It is a thin Scala wrapper
  * around the [[https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Synthesizer.html
  * synthesizer in the Java MIDI API]]. Uses the piano sound. */
object Synthesizer {

  /** the underlying Java MIDI synthesizer */
  lazy val peer = javax.sound.midi.MidiSystem.getSynthesizer

  /** The channels available in the underlying MIDI synthesizer */
  lazy val channels: Array[MidiChannel] = this.peer.getChannels.flatMap( Option(_) ) // the channel array returned by getChannels may contain nulls

  /** The first channel available in the underlying MIDI synthesizer.
    * Accessing this value throws a [[MidiUnavailableException]] in case no channels are available. */
  lazy val defaultChannel: MidiChannel = this.channels.headOption.getOrElse(throw new MidiUnavailableException("no MIDI channels available"))

  /** The latench of the underlying MIDI synthesizer, in milliseconds. */
  lazy val latency: Long = this.peer.getLatency


  /** Prepares the synthesizer for use. This method needs to be called before
    * calling [[noteOn]] for the first time. */
  def open(): Unit = {
    this.peer.open()
  }


  /** Frees up resources associated with the synthesizers’s Java peer. */
  def close(): Unit = {
    this.peer.close()
  }


  /** Plays a single note on the synthesized piano. The sound will fade away
    * eventually or may be cut off using [[noteOff]].
    *
    * You must call [[open]] once before playing any music with this method,
    * or no sound will play. The method throws a [[MidiUnavailableException]]
    * if no MIDI channels are available.
    *
    * @param note    the note to play on the piano
    * @param volume  the volume to play it at (between 0 and 127; if you pass
    *                in zero, this will work like [[noteOff]])*/
  def noteOn(note: Pitch, volume: Int): Unit = {
    this.defaultChannel.noteOn(note.midiID(DefaultOctave), volume)
  }


  /** Terminates the playing of an ongoing note played with [[noteOn]].
    *
    * This method throws a [[MidiUnavailableException]] if no MIDI channels are available.
    *
    * @param note  the note to terminate. */
  def noteOff(note: Pitch): Unit = {
    this.defaultChannel.noteOff(note.midiID(DefaultOctave))
  }
}
