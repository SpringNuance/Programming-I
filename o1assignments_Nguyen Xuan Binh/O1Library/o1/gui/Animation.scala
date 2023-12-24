package o1.gui

////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////


import o1.gui.Animation._
import o1.gui.View._
import o1.util.ConvenientCollection


/** An `Animation` object has an iterable sequence of [[Pic]]s which it can show in a
  * separate window (a simple [[o1.gui.immutable.ViewFrame View]]).
  * @param frames            the [[Pic]]s that the animation consists of, in order; they should be identical in size
  * @param frameRate         a target speed (in [[Pic]]s per second), which the `Animation` will roughly observe if possible
  * @param terminateOnClose  whether the entire application should exit when the animation window is closed
  * @param atEnd             what should happen once the animation reaches the final [[Pic]] */
class Animation(frames: Iterable[Pic], val frameRate: Double, val terminateOnClose: Boolean, val atEnd: AtEnd) {
  animation =>

  private val eachFrameOnce = frames.toLazy

  /** Returns the frames of this animation as a `LazyList`. If [[atEnd]] is set to [[Animation
    * Animation.Loop]], this will be a repeating, infinite list. Otherwise, it just contains
    * each frame once, as set by the constructor parameter. */
  def frameStream: LazyList[Pic] =
    if (this.atEnd != Animation.Loop) eachFrameOnce else eachFrameOnce #::: frameStream


  private val screen = new Screen

  /** Starts a [[o1.gui.immutable.ViewFrame view]] that displays the animation.
    * @see [[hide]], [[o1.gui.immutable.ViewFrame ViewFrame.start]] */
  def show(): Unit = {
    this.screen.start()
  }

  /** Closes the [[o1.gui.immutable.ViewFrame view]] that displays the animation.
    * @see [[show]], [[o1.gui.immutable.ViewFrame View.start]] */
  def hide(): Unit = {
    this.screen.close()
  }


  private class Screen extends immutable.View(initialState = this.frameStream, frameRate, terminateOnClose = this.terminateOnClose,
                                              closeWhenDone = (this.atEnd == Close), refreshPolicy = UnlessSameReference) with HasPauseToggle {

    def makePic(upcomingFrames: LazyList[Pic]): Pic =
      upcomingFrames.headOption.getOrElse(throw NothingToDraw)

    override def onTick(upcomingFrames: LazyList[Pic]): LazyList[Pic] = {
      if (upcomingFrames.nonEmpty) {
        upcomingFrames.tail
      } else {
        if (this.closeWhenDone) {
          this.close()
        }
        upcomingFrames
      }
    }

    override def onMouseDown(upcomingFrames: LazyList[Pic], position: Pos): LazyList[Pic] = {
      this.togglePause()
      upcomingFrames
    }
  }

}




/** This companion object of [[Animation class Animation]] provides a couple of convenience methods
  * ([[show]], [[generate]]) for starting animations and an enumeration ([[AtEnd]]) for use with
  * the class. */
object Animation {

  /** The objects that inherit this “enumeration class” each specify a different
    * policy for handling what happens once an [[Animation]] reaches the last frame.
    * @see [[LeaveLastFrameVisible]], [[Loop]], [[Close]] */
  sealed abstract class AtEnd extends Product with Serializable

  /** Used as an [[Animation]] parameter to signal that the animation should repeat ''ad infinitum''. */
  case object Loop extends AtEnd
  /** Used as an [[Animation]] parameter to signal that the animation window should automatically close
    * after the last frame. */
  case object Close extends AtEnd
  /** Used as an [[Animation]] parameter to signal that the animation should stay stopped but
    * visible at the last frame once it’s done. */
  case object LeaveLastFrameVisible extends AtEnd

  /** Creates and immediately starts an [[Animation]] as specified by the parameters.
    * @param frames            the [[Pic]]s that the animation consists of, in order; they should be identical in size
    * @param frameRate         a target speed (in [[Pic]]s per second), which the `Animation` will roughly observe if possible
    * @param terminateOnClose  whether the entire application should exit when the animation window is closed
    * @param atEnd             what should happen once the animation reaches the final [[Pic]]
    * @return the running animation */
  def show(frames: Iterable[Pic], frameRate: Double = 5, terminateOnClose: Boolean = false,
           atEnd: AtEnd = LeaveLastFrameVisible): Animation = {
    val theAnim = new Animation(frames, frameRate, terminateOnClose, atEnd)
    theAnim.show()
    theAnim
  }

  /** Uses a given function to generate an animation, then starts and returns that animation.
    * @param makeFrame         a function that generates the frames of the animation:
    *                          it takes a frame number (>= 0) and returns the corresponding [[Pic]]
    * @param numberOfFrames    the number of frames in the animation; the numbers `0 <= n < numberOfFrames`
    *                          get passed to `makeFrame` to generate the animation
    * @param frameRate         a target speed (in [[Pic]]s per second), which the `Animation` will roughly observe if possible
    * @param terminateOnClose  whether the entire application should exit when the animation window is closed
    * @param atEnd             what should happen once the animation reaches the final [[Pic]]
    * @return the running animation */
  def generate(makeFrame: Int => Pic, numberOfFrames: Int = Int.MaxValue, frameRate: Double = 5,
               terminateOnClose: Boolean = false, atEnd: AtEnd = LeaveLastFrameVisible): Animation = {
    val frames = LazyList.tabulate(numberOfFrames)(makeFrame)
    this.show(frames, frameRate, terminateOnClose, atEnd)
  }

}
