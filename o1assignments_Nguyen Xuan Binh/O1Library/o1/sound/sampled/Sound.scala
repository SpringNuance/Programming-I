
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.sound.sampled

import o1.util.{tryForURL,ConvenientFloat,Try}
import javax.sound.sampled.{Clip,FloatControl,AudioSystem,DataLine}
import java.net.URL





/** This companion object of [[Sound class `Sound`]] provides methods for creating new `Sound` objects.
  * It has an alias in the top-level package [[o1]], so it’s accessible to students simply via `import o1._`. */
object Sound {

  /** Loads a sound sample from a URL or a local file. The sound needs to be in one of these
    * formats: WAV, AIFF, AU, AIFC, SND.
    * @param id      a path or URL that identifies the sound sample; this needs to be a
    *                classpath-relative file path or an URL, as specified for the parameter
    *                of [[o1.util.tryForURL(pathOrURL:String)* o1.util.tryForURL]]
    * @param volume  an adjustment to the sound’s volume: a positive number makes it louder,
    *                a negative one quieter. Pass in [[Mute]] to make the sound completely silent.
    * @return the sound; if a sound matching `id` could not be located, if the system doesn’t
    *          support sound, or if sound support is disabled, returns a silent
    *          [[Sound]] whose `isDefined` method returns `false` */
  def apply(id: String, volume: Float = 0): Sound =
    tryForURL(id).flatMap( validID => Try(this(validID, volume)) ).getOrElse {
      System.err.println(s"Failed to access any sound at the path or URL $id.")
      UndefinedSound(id)
    }

  /** Loads a sound sample from the given URL. The sound needs to be in one of these formats:
    * WAV, AIFF, AU, AIFC, SND. Throws an `IOException` if the URL is inaccessible.
    * @param url      a URL to a sound sample
    * @param volume  an adjustment to the sound’s volume: a positive number makes it louder,
    *                 a negative one quieter. Pass in [[Mute]] to make the sound completely silent.
    * @return the sound; if the system doesn’t support sound, or if sound support is disabled,
    *         returns a silent [[Sound]] whose `isDefined` method returns `false` */
  def apply(url: URL, volume: Float): Sound = {
    val audioInput = AudioSystem.getAudioInputStream(url)
    try {
      val info = new DataLine.Info(classOf[Clip], audioInput.getFormat)
      val clip = AudioSystem.getLine(info).asInstanceOf[Clip]
      clip.open(audioInput)
      LoadedSound(clip, volume, url.toString)
    } catch {
      case noSound: IllegalArgumentException =>
        System.err.println(s"Failed to use $url. The system does not seem to support sound, or sound support is disabled.")
        UndefinedSound(url.toString)
    }
  }


  private case class LoadedSound(val peer: Clip, val volume: Float, val description: String) extends Sound {
    def play(repeats: Int = 0, volume: Float = this.volume) = {
      if (this.peer.isRunning) {
        this.peer.stop()
      }
      if (volume != Mute) {
        try {
          val gain = this.peer.getControl(FloatControl.Type.MASTER_GAIN).asInstanceOf[FloatControl]
          gain.setValue(volume atLeast gain.getMinimum atMost gain.getMaximum)
        } catch {
          case noGain: IllegalArgumentException => // ignored
        }
        this.peer.setFramePosition(0)
        this.peer.loop(repeats)
      }
    }

    def withVolume(differentVolume: Float) = this.copy(volume = differentVolume)

    def stop() = {
      this.peer.stop()
    }

    final def isDefined = true
  }


  private case class UndefinedSound(val description: String) extends Sound {
    val volume = 0
    def play(repeats: Int = 0, volume: Float = this.volume) = {
      System.err.println(s"Failed to play undefined sound $description.")
    }
    def withVolume(differentVolume: Float) = this
    def stop() = { }
    override def toString = super.toString + " (failed to create)"
    final def isDefined = false
  }


}




/** Each instance of this trait represents a recorded sound sample.
  *
  * You create `Sound`s with the methods on the [[Sound$ companion object]]: `Sound("my_sound_file.wav")`.
  * The supported formats are WAV, AIFF, AU, AIFC, and SND.
  *
  * This trait has an alias in the top-level package [[o1]], so it’s accessible to
  * students simply via `import o1._`.
  *
  * This type is essentially a wrapper around the key functionality in [[https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/package-frame.html
  * javax.sound.Sampled]], especially its [[https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/Clip.html Clip]] class. */
trait Sound {

  /** A description of the sound’s origin. Examples:
    *  - `"file:/D:/O1Course/Pong/bin/sounds/slap.wav"`
    *  - `"http://www.aalto.fi/does_not_exist.wav (failed to create)"` */
  val description: String


  /** an adjustment to the loaded sound sample’s volume: a positive number makes it louder
   *  a negative one quieter. A value equal to [[Mute]] means the sound is completely silent. */
  val volume: Float


  /** Returns an other `Sound` otherwise identical to this one but with the given
    * volume adjustment. */
  def withVolume(differentVolume: Float): Sound


  /** Plays the sound sample, possibly multiple times in sequence.
    * @param repeats  how many additional times the sound should play after
    *                 being played once; if you pass in [[KeepRepeating]], the
    *                 sound will repeat indefinitely unless [[stop]] is called
    * @param volume   you may pass in a number to override the sounds’s default
    *                 [[volume]] adjustment */
  def play(repeats: Int = 0, volume: Float = this.volume): Unit


  /** Stops the playing of the sound sample, previously started with [[play]]. */
  def stop(): Unit


  /** whether the `Sound` object represents a successfully loaded sound sample.
    * This will be `true` if the methods on the [[Sound$ companion object]] successfully
    * loaded the sound from a file or a URL, and `false` if there was a problem */
  def isDefined: Boolean


  /** A `String` representation of the sound; equals [[description]]. */
  override def toString = this.description

}

