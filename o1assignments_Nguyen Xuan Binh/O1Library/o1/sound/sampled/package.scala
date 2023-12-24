
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.sound

import javax.sound.sampled.Clip

/** This is one of O1’s sound packages (the other being [[o1.sound.midi]]). This package
  * provides a simple interface for loading recorded sound samples.
  *
  * The contents of the package have aliases in the top-level package [[o1]], so they are
  * accessible to students simply via `import o1._`. */
package object sampled {

  /** a constant that you can pass to [[Sound.play play]] to make the sound repeat indefinitely */
  val KeepRepeating: Int = Clip.LOOP_CONTINUOUSLY

  /** a constant that you can pass to various methods in this package to silence a sound sample completely */
  val Mute: Float = Float.MinValue

}

