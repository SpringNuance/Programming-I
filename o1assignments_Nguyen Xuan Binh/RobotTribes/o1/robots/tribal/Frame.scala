package o1.robots.tribal


/** Each `Frame` object represents a data frame within a tribal bot's call stack. A frame stores data
  * relevant to a single RoboSpeak subprogram call. A stack frame is associated not with the code
  * of a subprogram, but with a subprogram call that happens during a RoboSpeak program run.
  *
  * Tribal bots' subprogram calls -- unlike methods in Scala -- don't have any parameters or local
  * variables, and very little data needs to be stored in each `CallStackFrame`. In fact, only a
  * single line number needs to be stored to indicate where the subprogram call was made (in order
  * to determine where execution should resume once the bot returns from the subprogram).
  *
  * @param returnLine  the line number of the call site that produced this frame
  * @see [[Tribe]]
  * @see [[TribalBot.callSubprogram]]
  * @see [[TribalBot.returnFromSubprogram]] */
class Frame(val returnLine: Int) {

  /** Returns a textual representation of this frame, for debugging purposes. */
  override def toString = "called from line " + this.returnLine

}

