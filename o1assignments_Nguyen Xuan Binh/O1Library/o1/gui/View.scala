package o1.gui



import o1.gui.event._
import View._
import o1.sound.sampled.Sound
import scala.concurrent.{Future, Promise}
import scala.languageFeature.higherKinds


///////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////
///////////// GENERIC TO MUTABLE & IMMUTABLE //////////////////////
///////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////

object View {

  private[o1] trait Controls[Model] {

    private[gui] def onDone(): Unit = { }
    private[gui] def simulate(tickLimit: Int): Unit
    private[gui] def start(): Unit

    private[gui] def makePic(state: Model): Pic
    private[gui] def onStop() = { }
    private[gui] def isDone(state: Model) = false
    private[gui] def isPaused = false
    private[gui] def sound(state: Model): Option[Sound] = None

    private[gui] def onTick(previousState: Model): Model
    private[gui] def onTick(previousState: Model, time: Long): Model

    // Simple GUI handlers:
    private[gui] def onMouseMove(state: Model, position: Pos)  : Model
    private[gui] def onMouseDrag(state: Model, position: Pos)  : Model
    private[gui] def onMouseDown(state: Model, position: Pos)  : Model
    private[gui] def onMouseUp  (state: Model, position: Pos)  : Model
    private[gui] def onWheel    (state: Model, rotation: Int)  : Model
    private[gui] def onClick    (state: Model, position: Pos)  : Model
    private[gui] def onKeyDown  (state: Model, key: Key)       : Model
    private[gui] def onKeyUp    (state: Model, key: Key)       : Model
    private[gui] def onType     (state: Model, character: Char): Model

    // Full GUI handlers:
    private[gui] def onMouseMove (state: Model, event: MouseMoved     ): Model
    private[gui] def onMouseDrag (state: Model, event: MouseDragged   ): Model
    private[gui] def onMouseEnter(state: Model, event: MouseEntered   ): Model
    private[gui] def onMouseExit (state: Model, event: MouseExited    ): Model
    private[gui] def onMouseUp   (state: Model, event: MouseReleased  ): Model
    private[gui] def onMouseDown (state: Model, event: MousePressed   ): Model
    private[gui] def onWheel     (state: Model, event: MouseWheelMoved): Model
    private[gui] def onClick     (state: Model, event: MouseClicked   ): Model
    private[gui] def onKeyDown   (state: Model, event: KeyPressed     ): Model
    private[gui] def onKeyUp     (state: Model, event: KeyReleased    ): Model
    private[gui] def onType      (state: Model, event: KeyTyped       ): Model

    private[gui] def maxLifeSpan: Long = Long.MaxValue
  }

  private[gui] object NoHandlerDefined extends Throwable
  private[gui] def unimplementedDefaultHandler: Nothing = throw NoHandlerDefined


  private[gui] trait TooltipDefaults {
    import javax.swing.ToolTipManager
    ToolTipManager.sharedInstance.setInitialDelay(150)
  }

  private[gui] trait GeneratesTrace[Model,TraceData] extends Controls[Model] {
    import collection.mutable.Buffer


    private[gui] val source: Controls[Model]
    private[gui] val initialState: Model
    private[gui] val extractTrace: Model=>TraceData
    private[this] var tickLimitForStartAndGet = Long.MaxValue
    override private[gui] def maxLifeSpan = this.tickLimitForStartAndGet

    /** Indicates whether the view is paused. This implementation delegates to the
      * underlying `View` that is being traced. */
    final override def isPaused: Boolean = source.isPaused

    /** Returns a brief textual description of the view. */
    final override def toString = "traced " + super.toString

    private val traceBuffer: Buffer[(TraceData, TraceEvent)] =
      Buffer(extractTrace(this.initialState) -> TraceEvent.Init)

    /** Returns a trace of the events processed by this view. The trace comes in a collection
      * of pairs, each of which is composed of a `TraceData` value that describes at the
      * time of the event and a [[View.TraceEvent TraceEvent]] value that describes the event itself. */
    final def trace: Seq[(TraceData, TraceEvent)] = this.traceBuffer.toSeq

    @inline private[gui] def log(state: Model, traceEvent: TraceEvent) = {
      this.traceBuffer += extractTrace(state) -> traceEvent
      state
    }

    /** Simulates this trace-generating view with [[simulate]] and returns the
      * resulting trace. This is equivalent to calling first [[simulate]], then
      * [[trace]]. See also [[startAndGet]].
      * @param tickLimit  the maximum number of ticks to simulate; `Int.MaxValue`
      *                   (which is the default) means there is no such limit */
    final def simulateAndGet(tickLimit: Int = Int.MaxValue): Seq[(TraceData, TraceEvent)] = {
      this.simulate(tickLimit)
      this.trace
    }

    /** Starts this trace-generating view with [[start]] and returns a `Future` that evaluates
      * to the resulting trace. The `Future` succeeds when the view is done; if the view isn’t
      * done after a given number of clock ticks, the `Future` fails with [[View.HasPauseToggle Aborted]],
      * producing a partial trace. See also [[simulateAndGet]].
      * @param tickLimit  the maximum number of ticks to process before the future completes with a failure */
    final def startAndGet(tickLimit: Long = Long.MaxValue): Future[Seq[(TraceData, TraceEvent)]] = {
      this.tickLimitForStartAndGet = tickLimit
      this.start()
      this.promiseOfTrace.future
    }

    private val promiseOfTrace = Promise[Seq[(TraceData, TraceEvent)]]

    private[gui] override def onDone(): Unit = {
      super.onDone()
      this.promiseOfTrace.success(this.trace)
    }

    /** Causes an additional effect when the view is stopped (with `stop()`).
      * This implementation delegates to the underlying `View` that is being traced.
      * In addition, if the traced view had been started with `startAndGet` and reaches
      * its tick limit before being done, this method causes the returned future to
      * complete with a failure. */
    final override def onStop(): Unit = {
      this.promiseOfTrace.tryFailure(Aborted("view did not reach done state in " + this.tickLimitForStartAndGet + " ticks and was aborted; partial trace collected", this.trace))
      source.onStop()
    }


  }


  /** Describes a single tick or GUI event recorded while tracing a `View`.
    * See the [[TraceEvent$ `TraceEvent` companion object]] for specific subtypes. */
  sealed abstract class TraceEvent {
    /** a short description of the event */
    val name: String
  }
  /** This companion object of the sealed `TraceEvent` class provides subtypes of the
    * class, which can be used for tracing different kinds of events. */
  object TraceEvent {

    /** A `TraceEvent` that marks the start of a trace. */
    case object Init extends TraceEvent { val name = "init" }

    /** A `TraceEvent` that records a clock tick.
      * @param time  the number of the tick, if available */
    final case class Tick(val time: Option[Long]) extends TraceEvent {
      val name = "tick"
      override def toString = this.name + this.time.map( t => s"($t)" ).getOrElse("")
    }

    /** A `TraceEvent` that records a GUI event.
      * @param  name  a short description of the GUI event, such as `"click"`, `"key-down"`, or `"mouse-move"`
      * @param  info  additional information about the event
      * @tparam Info  the type of the additional information, such as `Pos`, `Key`, or `MouseMoved` */
    final case class Input[Info](val name: String, val info: Info) extends TraceEvent {
      override def toString = s"$name ($info)"
    }

    private[o1] object Tick {
      def apply() = new Tick(None)
      def apply(time: Long) = new Tick(Some(time))
    }
  }


  /** Represents situations where a `View` hasn’t reached its done state in the allotted number of ticks.
    * @param  partialTrace  the trace collected until the view was aborted.
    * @tparam TraceData     the type of the model-state descriptions in the trace */
  case class Aborted[TraceData](message: String, val partialTrace: Seq[(TraceData, TraceEvent)]) extends RuntimeException(message)



  /** Add this trait on a `View` to give it a pause toggle. You’ll still need to call `togglePause`
    * on whichever event you want to pause the view (e.g., user hitting space bar). */
  trait HasPauseToggle {
    self: Controls[_] =>

    /** Determines whether the view should be paused at the current state. */
    override def isPaused = this.pauseToggle   // N.B. This line generates a (harmless) false-positive error in IJ if type-aware highlighting is enabled.

    /** Whether the view starts in a paused state. By default, always returns `false`. */
    def startsPaused = false

    private var pauseToggle = this.startsPaused

    /** Tells the view to pause if unpaused and vice versa. */
    def togglePause() = {
      this.pauseToggle = !this.pauseToggle
    }
  }



  /** The number of clock ticks (24) that a `View` aims to generate per clock tick,
    * unless otherwise specified. */
  val TicksPerSecondDefault = 24

  private[gui] val TicksPerSecondMax = 1000


  /** A superclass for the different policies for updating the image visible in a `View` in
    * response to a change in the model.
    *
    * '''Note to students: You’re unlikely to need this for anything in O1.''' */
  abstract class RefreshPolicy {
    /** Returns a `Boolean` that indicates whether the `View` should try to update itself
      * after observing a given state of the model.
      * @param from  the model object at the previous update
      * @param to    the current model object (possibly the same object; possibly identical) */
    def shouldRefresh(from: Any, to: Any): Boolean
  }


  /** The [[RefreshPolicy]] of seeking to update the image visible in a `View` as often as possible.
    * (Time-consuming but always safe.)
    *
    * '''Note to students: You’re unlikely to need this for anything in O1.''' */
  case object Always extends RefreshPolicy {
    /** Returns `true` to indicated that the `View` should try to update itself no matter the
      * current and earlier states of the model. */
    def shouldRefresh(from: Any, to: Any) = true
  }
  /** The [[RefreshPolicy]] of seeking to update the image visible in a `View` only when the current model
    * object is different in identity than the previous one shown.
    *
    * '''Note to students: You’re unlikely to need this for anything in O1.''' */
  case object UnlessSameReference extends RefreshPolicy {
    /** Returns `true` if given two references to the same objects or two `AnyVal`s that are equal.
      * @param from  the model object at the previous update
      * @param to    the current model object (possibly the same object; possibly identical) */
    def shouldRefresh(from: Any, to: Any) = (from, to) match {
      case (ref1: AnyRef, ref2: AnyRef) => ref1 ne ref2
      case otherwise                    => from != to
    }
  }
  /** The [[RefreshPolicy]] of seeking to update the image visible in a `View` only when the current model
    * object is non-identical in terms of `equals` than the previous one shown.
    *
    * '''Note to students: You’re unlikely to need this for anything in O1.''' */
  case object UnlessIdentical extends RefreshPolicy {
    /** Returns `true` if given two objects aren’t equal (`!=`).
      * @param from  the model object at the previous update
      * @param to    the current model object (possibly the same object; possibly identical) */
    def shouldRefresh(from: Any, to: Any) = from != to
  }



  private[gui] object NothingToDraw extends RuntimeException



}




///////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////
///////////////////// MUTABLE VARIANT /////////////////////////////
///////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////


/** This package contains the version of `View`s that we primarily use in O1: views to
  * ''mutable'' domain models.
  *
	* The top-level package [[o1]] provides an alias to the [[ViewFrame]] class in this
	* package, so it is available to students as `View` simply by importing `o1._`.
	*
	* There is an alternative implementation of `View`s in [[o1.gui.immutable]]. */
object mutable {
  import View._

  /** An alias for [[ViewFrame]], which is the default sort of `View`. (Cf. the alternative, [[ViewComponent]].)  */
  type View[Model <: AnyRef] = ViewFrame[Model]


  private[mutable] type Controls[Model] = o1.gui.View.Controls[Model]

  private[mutable] trait HasModelField[Model] {
    private[mutable] def initialModel: Model
    /** the model object represented in the view. */
    def model = this.initialModel
  }

  /** Add this trait on a view to enable discarding its model object for another. */
  trait HasReplaceableModel[Model] extends HasModelField[Model] {
    private[this] var currentModel = this.initialModel
    /** the model object most recently set for the view.
      * @see [[model_=]] */
    override def model = this.currentModel
    /** Replaces the model object previously set for the view with the given one. */
    def model_=(replacementModel: Model) = {
      this.currentModel = replacementModel
    }
  }


  /** A Swing-embeddable view (complete with a picture, a ticking clock, event handlers, etc.).
    * It works like a [[ViewFrame]] except that it’s a Swing component, not a standalone GUI frame.
    * See [[ViewFrame]] for an overview.
    *
    * @param initialModel      the model to be displayed in the view (the only required parameter).
    *                          It usually makes sense to use a mutable object here and change its state
    *                          via the event handlers (cf. [[o1.gui.immutable.ViewComponent]]).
    * @param tickRate          the clock of the view will tick roughly this many times per second
    *                          (optional; defaults to 24)
    * @param initialDelay      an additional delay in milliseconds between calling [[start]] and the
    *                          clock starting (optional; defaults to 600)
    * @param refreshPolicy     a policy for how eagerly the view should try to update the graphical
    *                          representation of its model (optional; changing this may improve
    *                          efficiency in some circumstances)
    * @tparam Model  the type of the model object */
  abstract class ViewComponent[Model <: AnyRef](private[mutable] val initialModel: Model, tickRate: Double = TicksPerSecondDefault, initialDelay: Int = 600, refreshPolicy: RefreshPolicy = Always)
           extends ViewImpl.ViewComponent(initialModel, tickRate, initialDelay, refreshPolicy)
           with mutable.ControlDefaults[Model] {

    /** Returns a view that collects of the ticks and GUI events that the `View`’s event handlers
      * process, using the given function to generate that trace. That trace-collecting view, which
      * an instance of the [[Traced]] subclass, delegates the actual event handling to this original
      * view but provides an additional interface for tracing.
      * @param  extractTrace  a function that determines how to describe a model state in the generated trace
      * @tparam TraceData     the type of the model-state descriptions in the trace
      * @see [[traced]], [[tracedPics]] */
    final def tracedWith[TraceData](extractTrace: Model=>TraceData): Traced[TraceData] = new Traced(extractTrace)

    /** A view that wraps around another, collecting a log or ''trace'' of events while delegating
      * its actual event-handling to the wrapped view. Provides additional methods for accessing
      * such traces: [[trace]], [[simulateAndGet]], and [[startAndGet]]. A few examples of using
      * these methods are given below.
      *
      * `simulate` 500 clock ticks on the trace-collecing view and print the trace of clock ticks
      * accompanied by descriptions of the view’äs `model`.
      *
      * {{{
      *   for((traceItem, traceEvent) <- myTracedView.simulateAndGet(500)) {
      *     println(traceEvent + ": " + traceItem)
      *   }
      * }}}
      *
      * Or, equivalently:
      *
      * {{{
      *   myTracedView.simulate(500)
      *   for((traceItem, traceEvent) <- myTracedView.trace) {
      *     println(traceEvent + ": " + traceItem)
      *   }
      * }}}
      *
      *  Alternatively, start the trace-collecting view and run it interactively until it is done or
      * a tick limit is reached. Then print the trace of ticks and GUI events accompanied with
      * descriptions of the view’s `model`.
      *
      * {{{
      *   val futureTrace = myTracedView.startAndGet(tickLimit=100) recover {
      *     case Aborted(message, partialTrace) => partialTrace
      *   }
      *   for(trace <- futureTrace; (traceItem, traceEvent) <- trace) {
      *     println(traceEvent + ": " + traceItem)
      *   }
      * }}}
      *
      * @param  extractTrace  a function that determines how to describe a model state in the generated trace
      * @tparam TraceData     the type of the model-state descriptions in the trace */
    final class Traced[TraceData](private[gui] val extractTrace: Model=>TraceData)
                extends ViewComponent[Model](initialState, ticksPerSecond, initialDelay, refreshPolicy)
                with mutable.TraceGeneratingDefaults[Model,TraceData] {
      private[gui] val source = ViewComponent.this
    }
  }


  /** This class provides a framework for building simple GUIs. Each instance of the class is a graphical
    * view to an object that represents a particular domain; that object is the [[model]] of the view.
    * A `ViewFrame` displays the model as graphics within a GUI frame.
    *
    * This class is available under the alias `View` in the top-level package [[o1]], so students can
    * access it simply by importing `o1._`.
    *
    * The key method in the class is [[makePic]], which the view calls automatically and repeatedly to
    * determine which [[Pic]] to display in the frame at each moment in time. Concrete view objects must
    * add an implementation for this abstract method.
    *
    * A view listens to GUI events within the frame, but it doesn’t really do anything when notified
    * of an event; concrete instances of the class can override this behavior by overriding one of the
    * “on methods” (`onClick`, `onMouseMove`, etc.). The view also runs an internal clock and can react
    * to the passing of time (`onTick`).
    *
    * Just creating a view object is not enough to display it onscreen and start the clock; see the
    * [[start]] method.
    *
    * @param initialModel      the model to be displayed in the view (the only required parameter).
    *                          It usually makes sense to use a mutable object here and change its state
    *                          via the event handlers (cf. [[o1.gui.immutable.ViewFrame]]).
    * @param tickRate          the clock of the view will tick roughly this many times per second
    *                          (optional; defaults to 24)
    * @param title             a string to be displayed in the frame’s title bar (optional)
    * @param initialDelay      an additional delay in milliseconds between calling [[start]] and the
    *                          clock starting (optional; defaults to 600)
    * @param terminateOnClose  whether the entire application should exit when the `ViewFrame` is closed
    *                          (optional; defaults to `true`)
    * @param closeWhenDone     whether the `ViewFrame` should be hidden and its clock stopped once the view
    *                          has reached a “done state” (as per [[isDone]]) (optional; defaults to `false`)
    * @param refreshPolicy     a policy for how eagerly the view should try to update the graphical
    *                          representation of its model (optional; changing this may improve
    *                          efficiency in some circumstances)
    * @tparam Model  the type of the model object  */
  abstract class ViewFrame[Model <: AnyRef](private[mutable] val initialModel: Model, tickRate: Double = TicksPerSecondDefault, title: String = "", initialDelay: Int = 600,
                                            terminateOnClose: Boolean = true, closeWhenDone: Boolean = false, refreshPolicy: RefreshPolicy = Always)
           extends ViewImpl.ViewFrame(initialModel, tickRate, title, initialDelay, terminateOnClose, closeWhenDone, refreshPolicy)
           with mutable.ControlDefaults[Model] {

    /** An alternative constructor. Takes in just the model and the title; uses the defaults for all
      * the other parameters. Please see the multi-parameter constructor for details.
      * @param initialModel      the model to be displayed in the view
      * @param title             a string to be displayed in the frame’s title bar */
    def this(initialModel: Model, title: String) = this(initialModel, TicksPerSecondDefault, title)

    /** An alternative constructor. Takes in just the model and the tick rate; uses the defaults for
      * all the other parameters. Please see the multi-parameter constructor for details.
      * @param initialModel      the model to be displayed in the view
      * @param tickRate          the clock of the view will tick roughly this many times per second */
    def this(initialModel: Model, tickRate: Double) = this(initialModel, tickRate, "")

    /** Returns a view that collects of the ticks and GUI events that the `View`’s event handlers
      * process, using the given function to generate that trace. That trace-collecting view, which
      * an instance of the [[Traced]] subclass, delegates the actual event handling to this original
      * view but provides an additional interface for tracing.
      * @param  extractTrace  a function that determines how to describe a model state in the generated trace
      * @tparam TraceData     the type of the model-state descriptions in the trace
      * @see [[traced]], [[tracedPics]] */
    final def tracedWith[TraceData](extractTrace: Model=>TraceData): Traced[TraceData] = new Traced(extractTrace)

    /** A view that wraps around another, collecting a log or ''trace'' of events while delegating
      * its actual event-handling to the wrapped view. Provides additional methods for accessing
      * such traces: [[trace]], [[simulateAndGet]], and [[startAndGet]]. A few examples of using
      * these methods are given below.
      *
      * `simulate` 500 clock ticks on the trace-collecing view and print the trace of clock ticks
      * accompanied by descriptions of the view’äs `model`.
      *
      * {{{
      *   for((traceItem, traceEvent) <- myTracedView.simulateAndGet(500)) {
      *     println(traceEvent + ": " + traceItem)
      *   }
      * }}}
      *
      * Or, equivalently:
      *
      * {{{
      *   myTracedView.simulate(500)
      *   for((traceItem, traceEvent) <- myTracedView.trace) {
      *     println(traceEvent + ": " + traceItem)
      *   }
      * }}}
      *
      *  Alternatively, start the trace-collecting view and run it interactively until it is done or
      * a tick limit is reached. Then print the trace of ticks and GUI events accompanied with
      * descriptions of the view’s `model`.
      *
      * {{{
      *   val futureTrace = myTracedView.startAndGet(tickLimit=100) recover {
      *     case Aborted(message, partialTrace) => partialTrace
      *   }
      *   for(trace <- futureTrace; (traceItem, traceEvent) <- trace) {
      *     println(traceEvent + ": " + traceItem)
      *   }
      * }}}
      *
      * @param  extractTrace  a function that determines how to describe a model state in the generated trace
      * @tparam TraceData     the type of the model-state descriptions in the trace */
    final class Traced[TraceData](private[gui] val extractTrace: Model=>TraceData)
                extends ViewFrame[Model](initialModel, ticksPerSecond, title, initialDelay, terminateOnClose, closeWhenDone, refreshPolicy)
                with mutable.TraceGeneratingDefaults[Model,TraceData] {
      private[gui] val source = ViewFrame.this
    }
  }

  private[mutable] trait ControlDefaults[Model] extends Controls[Model] with TooltipDefaults with HasModelField[Model] {

    private[gui] type Traced[TraceData] <: GeneratesTrace[Model,TraceData]
    private[gui] def tracedWith[TraceData](extractTrace: Model=>TraceData): Traced[TraceData]


    /** Returns a `View` that stores a pictorial trace of the ticks and GUI events that
      * the `View`’s event handlers process. This is equivalent to calling [[tracedWith]]
      * and passing in the `View`’s `makePic` method. */
    final def tracedPics: Traced[Pic] = this.tracedWith(this.makePic)


    /** Returns a `View` that stores a trace of the ticks and GUI events that its event handlers
      * process. This parameterless method stores, at each event, the `toString` description of
      * the `View`’s (mutable) model. This is equivalent to calling [[tracedWith]] and passing in
      * that `toString` method. */
    final def traced: Traced[String] = this.tracedWith( _.toString )


    /** Indicates whether the view is paused. By default, always returns `false`.
      * @see [[o1.gui.View.HasPauseToggle HasPauseToggle]] */
    override def isPaused = super.isPaused


    /** Returns a [[Pic]] that graphically represents the current state of the view’s `model`
      * object. This method is automatically invoked by the view after GUI events and clock ticks.
      * Left abstract by this class so any concrete view needs to add a custom implementation.
      *
      * For best results, all invocations of this method on a single view object should return
      * `Pic`s of equal dimensions. */
    def makePic: Pic


    /** Determines if the given state is a “done state” for the view. By default, this is never
      * the case, but that behavior can be overridden.
      *
      *  Once done, the view stops reacting to events and updating its graphics and may close
      *  its GUI window, depending on the constructor parameters of the view. */
    def isDone = super.isDone(this.model)


    /** Determines whether the view should play a sound, given the current state of its model.
      * By default, no sounds are played.
      * @return a [[Sound]] that the view should play; `None` if no sound is appropriate for
      *         the current state */
    def sound = super.sound(this.model)


    /** Causes an additional effect when the view is stopped (with `stop()`).
      * By default, this method does nothing. */
    override def onStop() = super.onStop()


    /** Programmatically requests an update to the graphics of the view (even though no
      * clock tick or triggering GUI event occurred). */
    def refresh(): Unit



    //////////////////////////      SIMPLE HANDLERS       //////////////////////////

    /** Causes an effect whenever the view’s internal clock ticks.
      * Does nothing by default but can be overridden. */
    def onTick(): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever the mouse cursor moves above the view.
      * Does nothing by default but can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param position  the position of the mouse cursor relative to the view’s top left-hand corner */
    def onMouseMove(position: Pos): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever the mouse cursor is dragged above the view.
      * Does nothing by default but can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param position  the position of the mouse cursor relative to the view’s top left-hand corner */
    def onMouseDrag(position: Pos): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever the mouse wheel is rotated above the view.
      * Does nothing by default but can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param rotation  the number of steps the wheel rotated (negative means up, positive down) */
    def onWheel(rotation: Int): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever a mouse button is clicked (pressed+released, possibly multiple
      * times in sequence) above the view. Does nothing by default but can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param position  the position of the mouse cursor relative to the view’s top left-hand corner */
    def onClick(position: Pos): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever a mouse button is pressed down above the view.
      * Does nothing by default but can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param position  the position of the mouse cursor relative to the view’s top left-hand corner */
    def onMouseDown(position: Pos): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever a mouse button is released above the view.
      * Does nothing by default but can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param position  the position of the mouse cursor relative to the view’s top left-hand corner */
    def onMouseUp(position: Pos): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever a key on the keyboard is pressed down while the view
      * has the keyboard focus. Does nothing by default but can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param key  the key that was pressed down  */
    def onKeyDown(key: Key): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever a key on the keyboard is released while the view
      * has the keyboard focus. Does nothing by default but can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param key  the key that was released  */
    def onKeyUp(key: Key): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever a key on the keyboard is typed (pressed+released) while
      * the view has the keyboard focus. Does nothing by default but can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param character  the key that was typed */
    def onType(character: Char): Unit = unimplementedDefaultHandler





    //////////////////////////     FULL HANDLERS       //////////////////////////


    /** Causes an effect whenever the view’s internal clock ticks.
      * Does nothing by default but can be overridden.
      *
      * If you don’t need the number of the clock tick, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param time  the running number of the clock tick (the first tick being number 1, the second 2, etc.) */
    def onTick(time: Long): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever the mouse cursor moves above the view.
      * Does nothing by default but can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param event  the GUI event that caused this handler to be called */
    def onMouseMove(event: MouseMoved): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever the mouse cursor is dragged above the view.
      * Does nothing by default but can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param event  the GUI event that caused this handler to be called */
    def onMouseDrag(event: MouseDragged): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever the mouse wheel is rotated above the view.
      * Does nothing by default but can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param event  the GUI event that caused this handler to be called */
    def onWheel(event: MouseWheelMoved): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever a mouse button is clicked (pressed+released, possibly multiple
      * times in sequence) above the view. Does nothing by default but can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param event  the GUI event that caused this handler to be called */
    def onClick(event: MouseClicked): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever a mouse button is pressed down above the view.
      * Does nothing by default but can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param event  the GUI event that caused this handler to be called */
    def onMouseDown(event: MousePressed): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever a mouse button is released above the view.
      * Does nothing by default but can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param event  the GUI event that caused this handler to be called */
    def onMouseUp(event: MouseReleased): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever the mouse cursor enters the view.
      * Does nothing by default but can be overridden.
      * @param event  the GUI event that caused this handler to be called */
    def onMouseEnter(event: MouseEntered): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever the mouse cursor exits the view.
      * Does nothing by default but can be overridden.
      * @param event  the GUI event that caused this handler to be called */
    def onMouseExit(event: MouseExited): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever a key on the keyboard is pressed down while the view
      * has the keyboard focus. Does nothing by default but can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param event  the GUI event that caused this handler to be called */
    def onKeyDown(event: KeyPressed): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever a key on the keyboard is released while the view
      * has the keyboard focus. Does nothing by default but can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param event  the GUI event that caused this handler to be called */
    def onKeyUp(event: KeyReleased): Unit = unimplementedDefaultHandler


    /** Causes an effect whenever a key on the keyboard is typed (pressed+released) while
      * the view has the keyboard focus. Does nothing by default but can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param event  the GUI event that caused this handler to be called */
    def onType(event: KeyTyped): Unit = unimplementedDefaultHandler



    final override private[gui] def makePic (model: Model) = this.makePic
    final override private[gui] def isDone  (model: Model) = this.isDone
    final override private[gui] def sound   (model: Model) = this.sound

    final override private[gui] def onTick(model: Model) = { this.onTick(); this.model }
    final override private[gui] def onTick(model: Model, time: Long) = { this.onTick(time); this.model }

    final override private[gui] def onMouseMove(model: Model, position: Pos)   = { this.onMouseMove(position); this.model }
    final override private[gui] def onMouseDrag(model: Model, position: Pos)   = { this.onMouseDrag(position); this.model }
    final override private[gui] def onWheel    (model: Model, rotation: Int)   = { this.onWheel(rotation)    ; this.model }
    final override private[gui] def onClick    (model: Model, position: Pos)   = { this.onClick(position)    ; this.model }
    final override private[gui] def onMouseDown(model: Model, position: Pos)   = { this.onMouseDown(position); this.model }
    final override private[gui] def onMouseUp  (model: Model, position: Pos)   = { this.onMouseUp(position)  ; this.model }
    final override private[gui] def onKeyDown  (model: Model, key: Key)        = { this.onKeyDown(key)       ; this.model }
    final override private[gui] def onKeyUp    (model: Model, key: Key)        = { this.onKeyUp(key)         ; this.model }
    final override private[gui] def onType     (model: Model, character: Char) = { this.onType(character)    ; this.model }

    final override private[gui] def onMouseMove (model: Model, event: MouseMoved     ) = { this.onMouseMove(event) ; this.model }
    final override private[gui] def onMouseDrag (model: Model, event: MouseDragged   ) = { this.onMouseDrag(event) ; this.model }
    final override private[gui] def onMouseEnter(model: Model, event: MouseEntered   ) = { this.onMouseEnter(event); this.model }
    final override private[gui] def onMouseExit (model: Model, event: MouseExited    ) = { this.onMouseExit(event) ; this.model }
    final override private[gui] def onMouseUp   (model: Model, event: MouseReleased  ) = { this.onMouseUp(event)   ; this.model }
    final override private[gui] def onMouseDown (model: Model, event: MousePressed   ) = { this.onMouseDown(event) ; this.model }
    final override private[gui] def onWheel     (model: Model, event: MouseWheelMoved) = { this.onWheel(event)     ; this.model }
    final override private[gui] def onClick     (model: Model, event: MouseClicked   ) = { this.onClick(event)     ; this.model }
    final override private[gui] def onKeyDown   (model: Model, event: KeyPressed     ) = { this.onKeyDown(event)   ; this.model }
    final override private[gui] def onKeyUp     (model: Model, event: KeyReleased    ) = { this.onKeyUp(event)     ; this.model }
    final override private[gui] def onType      (model: Model, event: KeyTyped       ) = { this.onType(event)      ; this.model }
  }


  private[mutable] trait TraceGeneratingDefaults[Model,TraceData] extends ControlDefaults[Model] with GeneratesTrace[Model,TraceData] {
    private[gui] val source: ControlDefaults[Model]
    def makePic: Pic = source.makePic
    override def isDone: Boolean = source.isDone
    override def sound: Option[Sound] = source.sound


    import TraceEvent._
    override def onTick(): Unit                          = { source.onTick(); log(this.model, Tick()) }
    override def onTick(time: Long): Unit                = { source.onTick(time); log(this.model, Tick(time)) }
    override def onMouseMove(position: Pos): Unit        = { source.onMouseMove(position); log(this.model, Input("mouse-move", position)) }
    override def onMouseDrag(position: Pos): Unit        = { source.onMouseDrag(position); log(this.model, Input("mouse-drag", position)) }
    override def onWheel(rotation: Int): Unit            = { source.onWheel(rotation); log(this.model, Input("wheel", rotation)) }
    override def onClick(position: Pos): Unit            = { source.onClick(position); log(this.model, Input("click", position)) }
    override def onMouseDown(position: Pos): Unit        = { source.onMouseDown(position); log(this.model, Input("mouse-down", position)) }
    override def onMouseUp(position: Pos): Unit          = { source.onMouseUp(position); log(this.model, Input("mouse-up", position)) }
    override def onKeyDown(key: Key): Unit               = { source.onKeyDown(key); log(this.model, Input("key-down", key)) }
    override def onKeyUp(key: Key): Unit                 = { source.onKeyUp(key); log(this.model, Input("key-up", key)) }
    override def onType(character: Char): Unit           = { source.onType(character); log(this.model, Input("type", character)) }
    override def onMouseMove(event: MouseMoved): Unit    = { source.onMouseMove(event); log(this.model, Input("mouse-move", event)) }
    override def onMouseDrag(event: MouseDragged): Unit  = { source.onMouseDrag(event); log(this.model, Input("mouse-drag", event)) }
    override def onMouseEnter(event: MouseEntered): Unit = { source.onMouseEnter(event); log(this.model, Input("mouse-enter", event)) }
    override def onMouseExit(event: MouseExited): Unit   = { source.onMouseExit(event); log(this.model, Input("mouse-exit", event)) }
    override def onMouseUp(event: MouseReleased): Unit   = { source.onMouseUp(event); log(this.model, Input("mouse-up", event)) }
    override def onMouseDown(event: MousePressed): Unit  = { source.onMouseDown(event); log(this.model, Input("mouse-down", event)) }
    override def onWheel(event: MouseWheelMoved): Unit   = { source.onWheel(event); log(this.model, Input("wheel", event)) }
    override def onClick(event: MouseClicked): Unit      = { source.onClick(event); log(this.model, Input("click", event)) }
    override def onKeyDown(event: KeyPressed): Unit      = { source.onKeyDown(event); log(this.model, Input("key-down", event)) }
    override def onKeyUp(event: KeyReleased): Unit       = { source.onKeyUp(event); log(this.model, Input("key-up", event)) }
    override def onType(event: KeyTyped): Unit           = { source.onType(event); log(this.model, Input("type", event)) }

  }

}


///////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////
/////////////////// IMMUTABLE VARIANT /////////////////////////////
///////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////


/** This package contains a version of `View`s that is not much used in O1: views to ''immutable''
  * domain models. In O1, the other implementation in [[o1.gui.mutable]] is more relevant. */
object immutable {

  /** An alias for [[ViewFrame]], which is the default sort of `View`. (Cf. the alternative, [[ViewComponent]].)  */
  type View[Model] = ViewFrame[Model]

  private[immutable] type Controls[Model] = o1.gui.View.Controls[Model]


  /** A Swing-embeddable view (complete with a picture, a ticking clock, event handlers, etc.).
    * It works like a [[ViewFrame]] except that it’s a Swing component, not a standalone GUI frame.
    * See [[ViewFrame]] for an overview.
    *
    * @param initialState      the initial state of the model to be displayed in the view (the only
    *                          required parameter). This class has been designed to work conveniently
    *                          with immutable model objects (cf. [[o1.gui.mutable.ViewComponent]]).
    * @param tickRate          the clock of the view will tick roughly this many times per second
    *                          (optional; defaults to 24)
    * @param initialDelay      an additional delay in milliseconds between calling [[start]] and the
    *                          clock starting (optional; defaults to 600)
    * @param refreshPolicy     a policy for how eagerly the view should try to update the graphical
    *                          representation of its model (optional; changing this may improve
    *                          efficiency in some circumstances)
    * @tparam Model  the type of the states of the model */
  abstract class ViewComponent[Model](initialState: Model, tickRate: Double = TicksPerSecondDefault, initialDelay: Int = 600, refreshPolicy: RefreshPolicy = Always)
           extends ViewImpl.ViewComponent(initialState, tickRate, initialDelay, refreshPolicy)
           with immutable.ControlDefaults[Model] {

    /** Returns a view that collects of the ticks and GUI events that the `View`’s event handlers
      * process, using the given function to generate that trace. That trace-collecting view, which
      * an instance of the [[Traced]] subclass, delegates the actual event handling to this original
      * view but provides an additional interface for tracing.
      * @param  extractTrace  a function that determines how to describe a model state in the generated trace
      * @tparam TraceData     the type of the model-state descriptions in the trace
      * @see [[traced]], [[tracedPics]] */
    final def tracedWith[TraceData](extractTrace: Model=>TraceData): Traced[TraceData] =
      new Traced(extractTrace)

    /** A view that wraps around another, collecting a log or ''trace'' of events while delegating
      * its actual event-handling to the wrapped view. Provides additional methods for accessing
      * such traces: [[trace]], [[simulateAndGet]], and [[startAndGet]]. A few examples of using
      * these methods are given below.
      *
      * `simulate` 500 clock ticks on the trace-collecing view and print the trace of clock ticks
      * accompanied by descriptions of the view’äs `model`.
      *
      * {{{
      *   for((traceItem, traceEvent) <- myTracedView.simulateAndGet(500)) {
      *     println(traceEvent + ": " + traceItem)
      *   }
      * }}}
      *
      * Or, equivalently:
      *
      * {{{
      *   myTracedView.simulate(500)
      *   for((traceItem, traceEvent) <- myTracedView.trace) {
      *     println(traceEvent + ": " + traceItem)
      *   }
      * }}}
      *
      *  Alternatively, start the trace-collecting view and run it interactively until it is done or
      * a tick limit is reached. Then print the trace of ticks and GUI events accompanied with
      * descriptions of the view’s `model`.
      *
      * {{{
      *   val futureTrace = myTracedView.startAndGet(tickLimit=100) recover {
      *     case Aborted(message, partialTrace) => partialTrace
      *   }
      *   for(trace <- futureTrace; (traceItem, traceEvent) <- trace) {
      *     println(traceEvent + ": " + traceItem)
      *   }
      * }}}
      *
      * @param  extractTrace  a function that determines how to describe a model state in the generated trace
      * @tparam TraceData     the type of the model-state descriptions in the trace */
    final class Traced[TraceData](private[gui] val extractTrace: Model=>TraceData)
                extends ViewComponent[Model](initialState, ticksPerSecond, initialDelay, refreshPolicy)
                with immutable.TraceGeneratingDefaults[Model,TraceData] {
      private[gui] val source = ViewComponent.this
    }

  }


  /** This class provides a framework for building simple GUIs. Each instance of the class is a graphical
    * view to objects that represent the states of a domain model; those states can be (but are not required
    * to be) immutable objects. A `ViewFrame` displays the model as graphics within a GUI frame.
    *
    * '''Note to students: this is not the view class that we commonly use in O1 but an alternative
    * implementation. For the usual `View`, see [[o1.gui.mutable.ViewFrame here]].'''
    *
    * The key method in the class is [[makePic]], which the view calls automatically and repeatedly to
    * determine which [[Pic]] to display in the frame at each moment in time. Concrete view objects must
    * add an implementation for this abstract method.
    *
    * A view listens to GUI events within the frame, but it doesn’t really do anything when notified
    * of an event; concrete instances of the class can override this behavior by overriding one of the
    * “on methods” (`onClick`, `onMouseMove`, etc.). The view also runs an internal clock and can react
    * to the passing of time (`onTick`).
    *
    * Just creating a view object is not enough to display it onscreen and start the clock; see the
    * [[start]] method.
    *
    * Please note that even though this class is designed to work with immutable model states, the
    * actual `ViewFrame` is not itself immutable.
    *
    * @param initialState      the initial state of the model to be displayed in the view (the only
    *                          required parameter). This class has been designed to work conveniently
    *                          with immutable model objects (cf. [[o1.gui.mutable.ViewFrame]]).
    * @param tickRate          the clock of the view will tick roughly this many times per second
    *                          (optional; defaults to 24)
    * @param title             a string to be displayed in the frame’s title bar (optional)
    * @param initialDelay      an additional delay in milliseconds between calling [[start]] and the
    *                          clock starting (optional; defaults to 600)
    * @param terminateOnClose  whether the entire application should exit when the `ViewFrame` is closed
    *                          (optional; defaults to `true`)
    * @param closeWhenDone     whether the `ViewFrame` should be hidden and its clock stopped once the view
    *                          has reached a “done state” (as per [[isDone]]) (optional; defaults to `false`)
    * @param refreshPolicy     a policy for how eagerly the view should try to update the graphical
    *                          representation of its model (optional; changing this may improve
    *                          efficiency in some circumstances)
    * @tparam Model  the type of the states of the model */
  abstract class ViewFrame[Model](initialState: Model, tickRate: Double = TicksPerSecondDefault, title: String = "", initialDelay: Int = 600,
                                  terminateOnClose: Boolean = true, closeWhenDone: Boolean = false, refreshPolicy: RefreshPolicy = Always)
           extends ViewImpl.ViewFrame(initialState, tickRate, title, initialDelay, terminateOnClose, closeWhenDone, refreshPolicy)
           with immutable.ControlDefaults[Model] {

    /** An alternative constructor. Takes in just the initial state and the title; uses the defaults
      * for all the other parameters. Please see the multi-parameter constructor for details.
      * @param initialState      the initial state of the model to be displayed in the view
      * @param title             a string to be displayed in the frame’s title bar */
    def this(initialState: Model, title: String) = this(initialState, TicksPerSecondDefault, title)

    /** An alternative constructor. Takes in just the initial state and the tick rate; uses the defaults
      * for all the other parameters. Please see the multi-parameter constructor for details.
      * @param initialState      the initial state of the model to be displayed in the view
      * @param tickRate          the clock of the view will tick roughly this many times per second */
    def this(initialState: Model, tickRate: Double) = this(initialState, tickRate, "")

    /** Returns a view that collects of the ticks and GUI events that the `View`’s event handlers
      * process, using the given function to generate that trace. That trace-collecting view, which
      * an instance of the [[Traced]] subclass, delegates the actual event handling to this original
      * view but provides an additional interface for tracing.
      * @param  extractTrace  a function that determines how to describe a model state in the generated trace
      * @tparam TraceData     the type of the model-state descriptions in the trace
      * @see [[traced]], [[tracedPics]] */
    final def tracedWith[TraceData](extractTrace: Model=>TraceData): Traced[TraceData] =
      new Traced(extractTrace)

    /** A view that wraps around another, collecting a log or ''trace'' of events while delegating
      * its actual event-handling to the wrapped view. Provides additional methods for accessing
      * such traces: [[trace]], [[simulateAndGet]], and [[startAndGet]]. A few examples of using
      * these methods are given below.
      *
      * `simulate` 500 clock ticks on the trace-collecing view and print the trace of clock ticks
      * accompanied by descriptions of the view’äs `model`.
      *
      * {{{
      *   for((traceItem, traceEvent) <- myTracedView.simulateAndGet(500)) {
      *     println(traceEvent + ": " + traceItem)
      *   }
      * }}}
      *
      * Or, equivalently:
      *
      * {{{
      *   myTracedView.simulate(500)
      *   for((traceItem, traceEvent) <- myTracedView.trace) {
      *     println(traceEvent + ": " + traceItem)
      *   }
      * }}}
      *
      *  Alternatively, start the trace-collecting view and run it interactively until it is done or
      * a tick limit is reached. Then print the trace of ticks and GUI events accompanied with
      * descriptions of the view’s `model`.
      *
      * {{{
      *   val futureTrace = myTracedView.startAndGet(tickLimit=100) recover {
      *     case Aborted(message, partialTrace) => partialTrace
      *   }
      *   for(trace <- futureTrace; (traceItem, traceEvent) <- trace) {
      *     println(traceEvent + ": " + traceItem)
      *   }
      * }}}
      *
      * @param  extractTrace  a function that determines how to describe a model state in the generated trace
      * @tparam TraceData     the type of the model-state descriptions in the trace */
    final class Traced[TraceData](private[gui] val extractTrace: Model=>TraceData)
                extends ViewFrame[Model](initialState, ticksPerSecond, title, initialDelay, terminateOnClose, closeWhenDone, refreshPolicy)
                with immutable.TraceGeneratingDefaults[Model,TraceData] {
      private[gui] val source = ViewFrame.this
    }

  }


  private[immutable] trait ControlDefaults[Model] extends Controls[Model] with TooltipDefaults {

    private[gui] def tracedWith[TraceData](traceData: Model=>TraceData): Traced[TraceData]
    private[gui] type Traced[TraceData] <: GeneratesTrace[Model,TraceData]

    /** Returns a `View` that stores a pictorial trace of the ticks and GUI events that
      * the `View`’s event handlers process. This is equivalent to calling [[tracedWith]]
      * and passing in the `View`’s `makePic` method. */
    final def tracedPics: Traced[Pic] = this.tracedWith(this.makePic)


    /** Returns a `View` that stores a trace of the ticks and GUI events that its event handlers
      * process. This parameterless method stores, at each event, the (immutable) state of the
      * `View`’s model. This is equivalent to calling [[tracedWith]] and passing in `identity`. */
    final def traced: Traced[Model] = this.tracedWith(identity)


    /** Returns a [[Pic]] that graphically represents the given state of the view’s model.
      * This method is automatically invoked by the view after GUI events and clock ticks.
      * Left abstract by this class so any concrete view needs to add a custom implementation.
      *
      * For best results, all invocations of this method on a single view object should return
      * `Pic`s of equal dimensions.
      *
      * @param state  a state of the model to be displayed */
    def makePic(state: Model): Pic


    /** Determines if the given state is a “done state” for the view. By default, this is never
      * the case, but that behavior can be overridden.
      *
      *  Once done, the view stops reacting to events and updating its graphics and may close
      *  its GUi window, depending on the constructor parameters of the view.
      *
      * @param state  a state of the model (possibly a done state) */
    override def isDone(state: Model) = super.isDone(state)


    /** Indicates whether the view is paused. By default, always returns `false`.
      * @see [[o1.gui.View.HasPauseToggle HasPauseToggle]] */
    override def isPaused = super.isPaused


    /** Determines whether the view should play a sound, given a state of its model.
      * By default, no sounds are played.
      * @param state  a state of the model
      * @return a [[Sound]] that the view should play; `None` if no sound is appropriate
      *         for the given state */
    override def sound(state: Model) = super.sound(state)


    /** Causes an additional effect when the view is stopped (with `stop()`).
      * By default, this method does nothing. */
    override def onStop() = super.onStop()



    //////////////////////////      SIMPLE HANDLERS       //////////////////////////

    /** Determines what state should follow the given one on a tick of the view’s internal
      * clock. By default, just returns the unchanged state, but this can be overridden.
      * @param previousState  the state of the model before the clock tick */
    def onTick(previousState: Model): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when the mouse cursor moves above
      * the view. By default, just returns the unchanged state, but this can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param state     the state of the model at the time of the move event
      * @param position  the position of the mouse cursor relative to the view’s top left-hand corner */
    def onMouseMove(state: Model, position: Pos): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when the mouse cursor is dragged above
      * the view. By default, just returns the unchanged state, but this can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param state     the state of the model at the time of the drag event
      * @param position  the position of the mouse cursor relative to the view’s top left-hand corner */
    def onMouseDrag(state: Model, position: Pos): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when the mouse wheel is rotated above
      * the view. By default, just returns the unchanged state, but this can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param state     the state of the model at the time of the wheel event
      * @param rotation  the number of steps the wheel rotated (negative means up, positive down) */
    def onWheel(state: Model, rotation: Int): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when a mouse button is clicked
      * (pressed+relesed, possibly multiple times in sequence) above the view. By default,
      * just returns the unchanged state, but this can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param state     the state of the model at the time of the click event
      * @param position  the position of the mouse cursor relative to the view’s top left-hand corner */
    def onClick(state: Model, position: Pos): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when a mouse button is pressed down
      * above the view. By default, just returns the unchanged state, but this can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param state     the state of the model at the time of the mouse event
      * @param position  the position of the mouse cursor relative to the view’s top left-hand corner */
    def onMouseDown(state: Model, position: Pos): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when a mouse button is released above
      * the view. By default, just returns the unchanged state, but this can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param state     the state of the model at the time of the mouse event
      * @param position  the position of the mouse cursor relative to the view’s top left-hand corner */
    def onMouseUp(state: Model, position: Pos): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when a key on the keyboard is pressed
      * down while the view has the keyboard focus. By default, just returns the unchanged state,
      * but this can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param state  the state of the model at the time of the keyboard event
      * @param key    the key that was pressed down */
    def onKeyDown(state: Model, key: Key): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when a key on the keyboard is released
      * while the view has the keyboard focus. By default, just returns the unchanged state, but
      * this can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param state  the state of the model at the time of the keyboard event
      * @param key    the key that was released */
    def onKeyUp(state: Model, key: Key): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when a key on the keyboard is typed
      * (pressed+released) while the view has the keyboard focus. By default, just returns the
      * unchanged state, but this can be overridden.
      *
      * If the desired behavior depends on detailed information about the GUI event, you
      * may want to implement the other method of the same name instead of this one.
      *
      * @param state      the state of the model at the time of the keyboard event
      * @param character  the key that was typed */
    def onType(state: Model, character: Char): Model = unimplementedDefaultHandler





    //////////////////////////     FULL HANDLERS       //////////////////////////


    /** Determines what state should follow the given one on a tick of the view’s internal
      * clock. By default, just returns the unchanged state, but this can be overridden.
      *
      * If you don’t need the number of the clock tick, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param previousState  the state of the model before the clock tick
      * @param time           the running number of the clock tick (the first tick being number 1, the second 2, etc.) */
    def onTick(previousState: Model, time: Long): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when the mouse cursor moves above
      * the view. By default, just returns the unchanged state, but this can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param state  the state of the model at the time of the move event
      * @param event  the GUI event that caused this handler to be called */
    def onMouseMove(state: Model, event: MouseMoved): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when the mouse cursor is dragged above
      * the view. By default, just returns the unchanged state, but this can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param state  the state of the model at the time of the drag event
      * @param event  the GUI event that caused this handler to be called */
    def onMouseDrag(state: Model, event: MouseDragged): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when the mouse wheel is rotated above
      * the view. By default, just returns the unchanged state, but this can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param state  the state of the model at the time of the wheel event
      * @param event  the GUI event that caused this handler to be called */
    def onWheel(state: Model, event: MouseWheelMoved): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when a mouse button is clicked
      * (pressed+relesed, possibly multiple times in sequence) above the view. By default,
      * just returns the unchanged state, but this can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param state  the state of the model at the time of the click event
      * @param event  the GUI event that caused this handler to be called */
    def onClick(state: Model, event: MouseClicked): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when a mouse button is pressed down
      * above the view. By default, just returns the unchanged state, but this can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param state  the state of the model at the time of the mouse event
      * @param event  the GUI event that caused this handler to be called */
    def onMouseDown(state: Model, event: MousePressed): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when a mouse button is released above
      * the view. By default, just returns the unchanged state, but this can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param state  the state of the model at the time of the mouse event
      * @param event  the GUI event that caused this handler to be called */
    def onMouseUp(state: Model, event: MouseReleased): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when the mouse cursor enters the
      * view. By default, just returns the unchanged state, but this can be overridden.
      * @param state  the state of the model at the time of the mouse event
      * @param event  the GUI event that caused this handler to be called */
    def onMouseEnter(state: Model, event: MouseEntered): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when the mouse cursor exits the
      * view. By default, just returns the unchanged state, but this can be overridden.
      * @param state  the state of the model at the time of the mouse event
      * @param event  the GUI event that caused this handler to be called */
    def onMouseExit(state: Model, event: MouseExited): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when a key on the keyboard is pressed
      * down while the view has the keyboard focus. By default, just returns the unchanged state,
      * but this can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param state  the state of the model at the time of the keyboard event
      * @param event  the GUI event that caused this handler to be called */
    def onKeyDown(state: Model, event: KeyPressed): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when a key on the keyboard is released
      * while the view has the keyboard focus. By default, just returns the unchanged state, but
      * this can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param state  the state of the model at the time of the keyboard event
      * @param event  the GUI event that caused this handler to be called */
    def onKeyUp(state: Model, event: KeyReleased): Model = unimplementedDefaultHandler


    /** Determines what state should follow the given one when a key on the keyboard is typed
      * (pressed+released) while the view has the keyboard focus. By default, just returns the
      * unchanged state, but this can be overridden.
      *
      * If you don’t need much information about the GUI event, you may find it simpler
      * to implement the other method of the same name instead of this one.
      *
      * @param state  the state of the model at the time of the keyboard event
      * @param event  the GUI event that caused this handler to be called */
    def onType(state: Model, event: KeyTyped): Model = unimplementedDefaultHandler

  }



  private[immutable] trait TraceGeneratingDefaults[Model,TraceData] extends ControlDefaults[Model] with GeneratesTrace[Model,TraceData] {

    /** Returns a [[Pic]] that graphically represents the current state of the view’s `model` object.
      * This implementation delegates to the underlying `View` that is being traced.
      * @param state  a state of the model to be displayed */
    def makePic(state: Model): Pic = source.makePic(state)

    /** Determines if the given state is a “done state” for the view.
      * This implementation delegates to the underlying `View` that is being traced.
      * @param state  a state of the model (possibly a done state) */
    override def isDone(state: Model): Boolean = source.isDone(state)

    /** Determines whether the view should play a sound, given a state of its model.
      * This implementation delegates to the underlying underlying `View` that is being traced.
     *  @param state  a state of the model */
    override def sound(state: Model): Option[Sound] = source.sound(state)

    import TraceEvent._
    /** Handles a clock tick and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onTick(previousState: Model): Model                    = log(source.onTick(previousState), Tick())
    /** Handles a clock tick and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onTick(previousState: Model, time: Long): Model        = log(source.onTick(previousState, time), Tick(time))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onMouseMove(state: Model, position: Pos): Model        = log(source.onMouseMove(state, position), Input("mouse-move", position))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onMouseDrag(state: Model, position: Pos): Model        = log(source.onMouseDrag(state, position), Input("mouse-drag", position))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onWheel(state: Model, rotation: Int): Model            = log(source.onWheel(state, rotation), Input("wheel", rotation))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onClick(state: Model, position: Pos): Model            = log(source.onClick(state, position), Input("click", position))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onMouseDown(state: Model, position: Pos): Model        = log(source.onMouseDown(state, position), Input("mouse-down", position))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onMouseUp(state: Model, position: Pos): Model          = log(source.onMouseUp(state, position), Input("mouse-up", position))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onKeyDown(state: Model, key: Key): Model               = log(source.onKeyDown(state, key), Input("key-down", key))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onKeyUp(state: Model, key: Key): Model                 = log(source.onKeyUp(state, key), Input("key-up", key))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onType(state: Model, character: Char): Model           = log(source.onType(state, character), Input("type", character))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onMouseMove(state: Model, event: MouseMoved): Model    = log(source.onMouseMove(state, event), Input("mouse-move", event))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onMouseDrag(state: Model, event: MouseDragged): Model  = log(source.onMouseDrag(state, event), Input("mouse-drag", event))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onMouseEnter(state: Model, event: MouseEntered): Model = log(source.onMouseEnter(state, event), Input("mouse-enter", event))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onMouseExit(state: Model, event: MouseExited): Model   = log(source.onMouseExit(state, event), Input("mouse-exit", event))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onMouseUp(state: Model, event: MouseReleased): Model   = log(source.onMouseUp(state, event), Input("mouse-up", event))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onMouseDown(state: Model, event: MousePressed): Model  = log(source.onMouseDown(state, event), Input("mouse-down", event))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onWheel(state: Model, event: MouseWheelMoved): Model   = log(source.onWheel(state, event), Input("wheel", event))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onClick(state: Model, event: MouseClicked): Model      = log(source.onClick(state, event), Input("click", event))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onKeyDown(state: Model, event: KeyPressed): Model      = log(source.onKeyDown(state, event), Input("key-down", event))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onKeyUp(state: Model, event: KeyReleased): Model       = log(source.onKeyUp(state, event), Input("key-up", event))
    /** Handles a GUI event and adds it to the view’s [[trace]]. Delegates the actual event-handling
      * to the corresponding method on the underlying `View` that is being traced. */
    override def onType(state: Model, event: KeyTyped): Model           = log(source.onType(state, event), Input("type", event))
  }

}


