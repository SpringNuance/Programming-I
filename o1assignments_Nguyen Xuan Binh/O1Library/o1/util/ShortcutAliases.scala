
package o1.util

import o1.gui.{event => GE}
import o1.sound.{midi => SM, sampled => SS}
import o1.{grid => R, gui => G, world => W}
import scala.language.implicitConversions

// This trait can be mixed into a package object to provide aliases in that package for
// O1Library’s most frequently used features. This is done in the top-level package o1.
private[o1] trait ShortcutAliases {

  o1.util.smclInit()

  //////////////////////////////////////////////////////////////////////////////////////////////
  //////////
  //////////            SHORTCUTS TO THE STANDARD SCALA API
  //////////
  //////////////////////////////////////////////////////////////////////////////////////////////

  /** This is a shortcut alias to Scala’s standard `Buffer` type, which we use a lot early in O1. */
  type Buffer[Element] = scala.collection.mutable.Buffer[Element]
  /** This is a shortcut alias to Scala’s standard `Buffer` type, which we use a lot early in O1. */
  val Buffer: scala.collection.mutable.Buffer.type = scala.collection.mutable.Buffer




  //////////////////////////////////////////////////////////////////////////////////////////////
  //////////
  //////////            O1.SOUND
  //////////
  //////////////////////////////////////////////////////////////////////////////////////////////


  /** Plays the music written in the given `String` (as described under [[o1.sound.midi]]) on
    * a MIDI synthesizer; this is a shortcut alias to [[o1.sound.midi.play(music:String)* o1.sound.midi]]. */
  def play(song: String): Unit = {
    SM.play(song)
  }

  /** The `Sound` type represents recorded sound samples; this is a shortcut alias to `o1.sound.sampled`. */
  type Sound = SS.Sound
  /** The `Sound` type represents recorded sound samples; this is a shortcut alias to `o1.sound.sampled`. */
  val Sound: SS.Sound.type = SS.Sound

  /** a constant that you can use to make the sound repeat indefinitely; this is a shortcut alias to [[o1.sound.sampled]] */
  val KeepRepeating: Int = SS.KeepRepeating
  /** a constant that you can use to mute a sound; this is a shortcut alias to [[o1.sound.sampled]] */
  val Mute: Float = SS.Mute





  //////////////////////////////////////////////////////////////////////////////////////////////
  //////////
  //////////            O1.GUI
  //////////
  //////////////////////////////////////////////////////////////////////////////////////////////


  /** The `Color` type represents colors; this is a shortcut alias to `o1.gui`. */
  type Color = G.Color
  /** The `Color` type represents colors; this is a shortcut alias to `o1.gui`. */
  val Color: G.Color.type = G.Color


  /** The `Pic` type represents images; this is a shortcut alias to `o1.gui`. */
  type Pic = G.Pic
  /** The `Pic` type represents images; this is a shortcut alias to `o1.gui`. */
  val Pic: G.Pic.type = G.Pic


  /** Displays the given picture in a minimalistic window; this is a shortcut alias to [[o1.gui.Pic.show `o1.gui`]]. */
  def show(pic: Pic): Unit = {
    pic.show()
  }


  /** The `Animation` type represents sequences of pictures shown in a GUI window; this is a shortcut alias to `o1.gui`. */
  type Animation = G.Animation
  /** The `Animation` type represents sequences of pictures shown in a GUI window; this is a shortcut alias to `o1.gui`. */
  val Animation: G.Animation.type = G.Animation


  /** Displays the given pictures in sequence in a minimalistic window; this is a shortcut alias to [[o1.gui.Animation.show `o1.gui`]]. */
  def animate(pics: Iterable[Pic], picsPerSecond: Double): Unit = {
    Animation.show(frames = pics, frameRate = picsPerSecond)
  }

  /** Generates pictures with the given function and displays them in sequence in a minimalistic window; this is a shortcut alias to [[o1.gui.Animation.generate `o1.gui`]]. */
  def animateWithFunction(picGeneratingFunction: Int => Pic, numberOfPics: Int, picsPerSecond: Double): Unit = {
    Animation.generate(picGeneratingFunction, numberOfPics, picsPerSecond)
  }


  /** The `View` type represents GUI windows that graphically represent a given domain model; this is a shortcut alias to class `ViewFrame` in `o1.gui.mutable`. */
  type View[Model <: AnyRef] = G.mutable.View[Model]


  /** The `Key` type represents keys on a computer keyboard; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  type Key = G.Key
  /** The `Key` type represents keys on a computer keyboard; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  val Key: G.Key.type = G.Key



  /** Creates a picture of a filled rectangle with the given parameters; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def rectangle(bounds: Bounds, color: Color): G.Pic = G.Pic.rectangle(bounds, color)
  /** Creates a picture of a filled rectangle with the given parameters; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def rectangle(width: Double, height: Double, color: Color): G.Pic = G.Pic.rectangle(width, height, color)
  /** Creates a picture of a filled rectangle with the given parameters; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def rectangle(width: Double, height: Double, color: Color, anchor: Anchor): G.Pic = G.Pic.rectangle(width, height, color, anchor)
  /** Creates a picture of a filled rectangle with the given parameters, anchored at the top left-hand corner; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def emptyCanvas(width: Double, height: Double, color: Color = G.colors.White): G.Pic = G.Pic.emptyCanvas(width, height, color)
  /** Creates a picture of a filled square with the given parameters; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def square(side: Double, color: Color): G.Pic = G.Pic.square(side, color)
  /** Creates a picture of a filled square with the given parameters; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def square(side: Double, color: Color, anchor: Anchor): G.Pic = G.Pic.square(side, color, anchor)
  /** Creates a picture of a filled circle with the given parameters; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def circle(diameter: Double, color: Color): G.Pic = G.Pic.circle(diameter, color)
  /** Creates a picture of a filled circle with the given parameters; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def circle(diameter: Double, color: Color, anchor: Anchor): G.Pic = G.Pic.circle(diameter, color, anchor)
  /** Creates a picture of a five-pointed star with the given parameters; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def star(width: Double, color: Color): G.Pic = G.Pic.star(width, color)
  /** Creates a picture of a five-pointed star with the given parameters; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def star(width: Double, color: Color, anchor: Anchor): G.Pic = G.Pic.star(width, color, anchor)
  /** Creates a picture of an isosceles triangle with the given parameters; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def triangle(width: Double, height: Double, color: Color): G.Pic = G.Pic.triangle(width, height, color)
  /** Creates a picture of an isosceles triangle with the given parameters; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def triangle(width: Double, height: Double, color: Color, anchor: Anchor): G.Pic = G.Pic.triangle(width, height, color, anchor)
  /** Creates a picture of an ellipse triangle with the given parameters; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def ellipse(width: Double, height: Double, color: Color): G.Pic = G.Pic.ellipse(width, height, color)
  /** Creates a picture of an ellipse triangle with the given parameters; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def ellipse(width: Double, height: Double, color: Color, anchor: Anchor): G.Pic = G.Pic.ellipse(width, height, color, anchor)
  /** Creates a picture of a thin line defined in terms of two locations on a plane; this is a shortcut alias to [[o1.gui.Pic$ `o1.gui.Pic`]]. */
  def line(from: Pos, to: Pos, color: Color): G.Pic = G.Pic.line(from, to, color)



  //////////////////////////////////////////////////////////////////////////////////////////////
  //////////
  //////////            O1.GUI.EVENT
  //////////
  //////////////////////////////////////////////////////////////////////////////////////////////


  /** The `MouseMoved` type represents mouse-movement events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  type MouseMoved = GE.MouseMoved
  /** The `MouseMoved` type represents mouse-movement events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  val MouseMoved: GE.MouseMoved.type = GE.MouseMoved

  /** The `MouseDragged` type represents mouse-dragging events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  type MouseDragged = GE.MouseDragged
  /** The `MouseDragged` type represents mouse-dragging events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  val MouseDragged: GE.MouseDragged.type = GE.MouseDragged

  /** The `MouseExited` type represents cursor-exiting events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  type MouseExited = GE.MouseExited
  /** The `MouseExited` type represents cursor-exiting events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  val MouseExited: GE.MouseExited.type = GE.MouseExited

  /** The `MouseEntered` type represents cursor-entering events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  type MouseEntered = GE.MouseEntered
  /** The `MouseEntered` type represents cursor-entering events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  val MouseEntered: GE.MouseEntered.type = GE.MouseEntered

  /** The `MouseWheelMoved` type represents mouse-wheel events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  type MouseWheelMoved = GE.MouseWheelMoved
  /** The `MouseWheelMoved` type represents mouse-wheel events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  val MouseWheelMoved: GE.MouseWheelMoved.type = GE.MouseWheelMoved

  /** The `MouseReleased` type represents button-release events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  type MouseReleased = GE.MouseReleased
  /** The `MouseReleased` type represents button-release events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  val MouseReleased: GE.MouseReleased.type = GE.MouseReleased

  /** The `MousePressed` type represents button-press in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  type MousePressed = GE.MousePressed
  /** The `MousePressed` type represents button-press in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  val MousePressed: GE.MousePressed.type = GE.MousePressed

  /** The `MouseClicked` type represents mouse-click events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  type MouseClicked = GE.MouseClicked
  /** The `MouseClicked` type represents mouse-click events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  val MouseClicked: GE.MouseClicked.type = GE.MouseClicked

  /** The `KeyPressed` type represents key-press events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  type KeyPressed = GE.KeyPressed
  /** The `KeyPressed` type represents key-press events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  val KeyPressed: GE.KeyPressed.type = GE.KeyPressed

  /** The `KeyReleased` type represents key-release events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  type KeyReleased = GE.KeyReleased
  /** The `KeyReleased` type represents key-release events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  val KeyReleased: GE.KeyReleased.type = GE.KeyReleased

  /** The `KeyTyped` type represents keyboard-typing events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  type KeyTyped = GE.KeyTyped
  /** The `KeyTyped` type represents keyboard-typing events in a GUI; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  val KeyTyped: GE.KeyTyped.type = GE.KeyTyped

  /** The `InputEvent` type represents GUI events that generate user input; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  type InputEvent = GE.InputEvent

  /** The `KeyEvent` type represents keyboard-related GUI events; this is a shortcut alias to `scala.swing.event` via `o1.gui`. */
  type KeyEvent = GE.KeyEvent


  import scala.language.implicitConversions
  /** This implicit conversion adds some methods to Swing’s `InputEvent` class; the methods are described under [[o1.gui.event.ConvenientInputEvent]]. */
  implicit def addMethodsToInputEvent(event: InputEvent): GE.ConvenientInputEvent =
    new GE.ConvenientInputEvent(event)


  //////////////////////////////////////////////////////////////////////////////////////////////
  //////////
  //////////            O1.WORLD  AND  O1.WORLD.OBJECTS
  //////////
  //////////////////////////////////////////////////////////////////////////////////////////////



  /** The `Pos` type represents coordinates on a two-dimensional surface; this is a shortcut alias to `o1.world`. */
  type Pos = W.Pos
  /** The `Pos` type represents coordinates on a two-dimensional surface; this is a shortcut alias to `o1.world`. */
  val Pos: W.Pos.type = W.Pos


  /** The `Bounds` type represents the boundaries of rectangular areas in two-dimensional space; this is a shortcut alias to `o1.world`. */
  type Bounds = W.Bounds
  /** The `Bounds` type represents the boundaries of rectangular areas in two-dimensional space; this is a shortcut alias to `o1.world`. */
  val Bounds: W.Bounds.type = W.Bounds


  /** The `Velocity` type represents directed movement in two-dimensional space; this is a shortcut alias to `o1.world`. */
  type Velocity = W.Velocity
  /** The `Velocity` type represents directed movement in two-dimensional space; this is a shortcut alias to `o1.world`. */
  val Velocity: W.Velocity.type = W.Velocity


  /** The `Velocity` type represents directions in two-dimensional space; this is a shortcut alias to `o1.world`. */
  type Direction = W.Direction
  /** The `Velocity` type represents directions in two-dimensional space; this is a shortcut alias to `o1.world`. */
  val Direction: W.Direction.type = W.Direction


  /** The `Anchor` type  represents points of attachment in two-dimensional elements; this is a shortcut alias to `o1.world.objects`. */
  type Anchor = W.objects.Anchor
  /** The `Anchor` type  represents points of attachment in two-dimensional elements; this is a shortcut alias to `o1.world.objects`. */
  val Anchor: W.objects.Anchor.type = W.objects.Anchor



  /** An anchor at the top left-hand corner; this is a shortcut alias to [[o1.world.objects.Anchor$ `o1.world.objects.Anchor`]]. */
  val TopLeft: G.Anchor = G.Anchor.TopLeft
  /** An anchor at the middle of the top edge; this is a shortcut alias to [[o1.world.objects.Anchor$ `o1.world.objects.Anchor`]]. */
  val TopCenter: G.Anchor = G.Anchor.TopCenter
  /** An anchor at the top right-hand corner; this is a shortcut alias to [[o1.world.objects.Anchor$ `o1.world.objects.Anchor`]]. */
  val TopRight: G.Anchor = G.Anchor.TopRight
  /** An anchor at the middle of the left edge; this is a shortcut alias to [[o1.world.objects.Anchor$ `o1.world.objects.Anchor`]]. */
  val CenterLeft: G.Anchor = G.Anchor.CenterLeft
  /** An anchor at the middle; this is a shortcut alias to [[o1.world.objects.Anchor$ `o1.world.objects.Anchor`]]. */
  val Center: G.Anchor = G.Anchor.Center
  /** An anchor at the middle of the right edge; this is a shortcut alias to [[o1.world.objects.Anchor$ `o1.world.objects.Anchor`]]. */
  val CenterRight: G.Anchor = G.Anchor.CenterRight
  /** An anchor at the bottom left-hand corner; this is a shortcut alias to [[o1.world.objects.Anchor$ `o1.world.objects.Anchor`]]. */
  val BottomLeft: G.Anchor = G.Anchor.BottomLeft
  /** An anchor at the middle of the bottom edge; this is a shortcut alias to [[o1.world.objects.Anchor$ `o1.world.objects.Anchor`]]. */
  val BottomCenter: G.Anchor = G.Anchor.BottomCenter
  /** An anchor at the bottom right-hand corner; this is a shortcut alias to [[o1.world.objects.Anchor$ `o1.world.objects.Anchor`]]. */
  val BottomRight: G.Anchor = G.Anchor.BottomRight


  /** A supertype for objects that have a position and a velocity in two-dimensional space; this is a shortcut alias to `o1.world.objects`. */
  type HasVelocity = W.objects.HasVelocity
  /** A supertype for objects that have a location in two-dimensional space; this is a shortcut alias to `o1.world.objects`. */
  type HasPos = W.objects.HasPos
  /** A supertype for objects that have a width and a height; this is a shortcut alias to `o1.world.objects`. */
  type HasSize = W.objects.HasSize
  /** A supertype for objects that have an anchoring point; this is a shortcut alias to `o1.world.objects`. */
  type HasAnchor = W.objects.HasAnchor
  /** A supertype for objects that take up a rectangular area and have an anchoring point; this is a shortcut alias to `o1.world.objects`. */
  type HasEdges = W.objects.HasEdges
  /** A supertype for objects that have a mutable position and a velocity in two-dimensional space; this is a shortcut alias to `o1.world.objects.mutable`. */
  type MovingObject = W.objects.mutable.MovingObject
  /** A supertype for objects that take up a rectangular area and have a velocity as well as a mutable position that may be constrained by a larger object around them; this is a shortcut alias to `o1.world.objects.mutable`. */
  type MovingObjectInContainer = W.objects.mutable.MovingObjectInContainer








  //////////////////////////////////////////////////////////////////////////////////////////////
  //////////
  //////////            O1.GRID
  //////////
  //////////////////////////////////////////////////////////////////////////////////////////////


  /** The `Grid` type represents rectangular grids; this is a shortcut alias to `o1.grid`. */
  type Grid[Elem] = R.Grid[Elem]

  /** The `GridPos` type represents coordinates in a grid-like structure; this is a shortcut alias to `o1.grid`. */
  type GridPos = R.GridPos
  /** The `GridPos` type represents coordinates in a grid-like structure; this is a shortcut alias to `o1.grid`. */
  val GridPos: R.GridPos.type = R.GridPos


  /** The `CompassDir` type represents the four main directions in a grid-like structure; this is a shortcut alias to `o1.grid`. */
  type CompassDir = R.CompassDir
  /** The `CompassDir` type represents the four main directions in a grid-like structure; this is a shortcut alias to `o1.grid`. */
  val CompassDir: R.CompassDir.type = R.CompassDir


  /** The northwardly compass direction in a grid; this is a shortcut alias to [[o1.grid.CompassDir$ `o1.grid.CompassDir`]]. */
  val North: CompassDir = R.CompassDir.North
  /** The northwardly compass direction in a grid; this is a shortcut alias to [[o1.grid.CompassDir$ `o1.grid.CompassDir`]]. */
  val East: CompassDir = R.CompassDir.East
  /** The northwardly compass direction in a grid; this is a shortcut alias to [[o1.grid.CompassDir$ `o1.grid.CompassDir`]]. */
  val South: CompassDir = R.CompassDir.South
  /** The westwardly compass direction in a grid; this is a shortcut alias to [[o1.grid.CompassDir$ `o1.grid.CompassDir`]]. */
  val West: CompassDir = R.CompassDir.West







  //////////////////////////////////////////////////////////////////////////////////////////////
  //////////
  //////////            O1.GUI.COLORS
  //////////
  //////////////////////////////////////////////////////////////////////////////////////////////




  /** Represents a fully transparent (white) color. This is a shortcut alias to [[o1.gui.colors]]. */
  val Transparent: G.Color = G.colors.Transparent


  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val AliceBlue: G.Color = G.colors.AliceBlue
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val AlizarinCrimson: Color = G.colors.AlizarinCrimson
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Amethyst: G.Color = G.colors.Amethyst
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val AntiqueWhite: G.Color = G.colors.AntiqueWhite
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Aqua: G.Color = G.colors.Aqua
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Aquamarine: G.Color = G.colors.Aquamarine
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Azure: G.Color = G.colors.Azure
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Beige: G.Color = G.colors.Beige
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Bisque: G.Color = G.colors.Bisque
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Black: G.Color = G.colors.Black
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val BlanchedAlmond: G.Color = G.colors.BlanchedAlmond
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Blue: G.Color = G.colors.Blue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val BlueViolet: G.Color = G.colors.BlueViolet
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val BrightRed: Color = G.colors.BrightRed
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Brown: G.Color = G.colors.Brown
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val BurlyWood: G.Color = G.colors.BurlyWood
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val CadetBlue: G.Color = G.colors.CadetBlue
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val CadmiumYellow: Color = G.colors.CadmiumYellow
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Chartreuse: G.Color = G.colors.Chartreuse
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Chocolate: G.Color = G.colors.Chocolate
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Coral: G.Color = G.colors.Coral
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val CornflowerBlue: G.Color = G.colors.CornflowerBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val CornSilk: G.Color = G.colors.CornSilk
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Crimson: G.Color = G.colors.Crimson
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Cyan: G.Color = G.colors.Cyan
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkBlue: G.Color = G.colors.DarkBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkCyan: G.Color = G.colors.DarkCyan
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkGoldenrod: G.Color = G.colors.DarkGoldenrod
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkGray: G.Color = G.colors.DarkGray
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkGreen: G.Color = G.colors.DarkGreen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkGrey: G.Color = G.colors.DarkGrey
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkKhaki: G.Color = G.colors.DarkKhaki
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkMagenta: G.Color = G.colors.DarkMagenta
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkOliveGreen: G.Color = G.colors.DarkOliveGreen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkOrange: G.Color = G.colors.DarkOrange
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkOrchid: G.Color = G.colors.DarkOrchid
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkRed: G.Color = G.colors.DarkRed
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkSalmon: G.Color = G.colors.DarkSalmon
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkSeaGreen: G.Color = G.colors.DarkSeaGreen
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkSienna: Color = G.colors.DarkSienna
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkSlateBlue: G.Color = G.colors.DarkSlateBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkSlateGray: G.Color = G.colors.DarkSlateGray
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkSlateGrey: G.Color = G.colors.DarkSlateGrey
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkTurquoise: G.Color = G.colors.DarkTurquoise
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DarkViolet: G.Color = G.colors.DarkViolet
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DeepPink: G.Color = G.colors.DeepPink
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DeepSkyBlue: G.Color = G.colors.DeepSkyBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DimGray: G.Color = G.colors.DimGray
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DimGrey: G.Color = G.colors.DimGrey
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val DodgerBlue: G.Color = G.colors.DodgerBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val FireBrick: G.Color = G.colors.FireBrick
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val FloralWhite: G.Color = G.colors.FloralWhite
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val ForestGreen: G.Color = G.colors.ForestGreen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Fuchsia: G.Color = G.colors.Fuchsia
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Gainsboro: G.Color = G.colors.Gainsboro
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val GhostWhite: G.Color = G.colors.GhostWhite
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Gold: G.Color = G.colors.Gold
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Goldenrod: G.Color = G.colors.Goldenrod
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Gray: G.Color = G.colors.Gray
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Green: G.Color = G.colors.Green
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val GreenYellow: G.Color = G.colors.GreenYellow
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Grey: G.Color = G.colors.Grey
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Honeydew: G.Color = G.colors.Honeydew
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val HotPink: G.Color = G.colors.HotPink
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val IndianRed: G.Color = G.colors.IndianRed
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val IndianYellow: Color = G.colors.IndianYellow
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Indigo: G.Color = G.colors.Indigo
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Ivory: G.Color = G.colors.Ivory
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Khaki: G.Color = G.colors.Khaki
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Lavender: G.Color = G.colors.Lavender
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LavenderBlush: G.Color = G.colors.LavenderBlush
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LawnGreen: G.Color = G.colors.LawnGreen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LemonChiffon: G.Color = G.colors.LemonChiffon
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightBlue: G.Color = G.colors.LightBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightCoral: G.Color = G.colors.LightCoral
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightCyan: G.Color = G.colors.LightCyan
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightGoldenrodYellow: G.Color = G.colors.LightGoldenrodYellow
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightGray: G.Color = G.colors.LightGray
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightGreen: G.Color = G.colors.LightGreen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightGrey: G.Color = G.colors.LightGrey
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightPink: G.Color = G.colors.LightPink
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightSalmon: G.Color = G.colors.LightSalmon
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightSeaGreen: G.Color = G.colors.LightSeaGreen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightSkyBlue: G.Color = G.colors.LightSkyBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightSlateGray: G.Color = G.colors.LightSlateGray
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightSlateGrey: G.Color = G.colors.LightSlateGrey
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightSteelBlue: G.Color = G.colors.LightSteelBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LightYellow: G.Color = G.colors.LightYellow
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Lime: G.Color = G.colors.Lime
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val LimeGreen: G.Color = G.colors.LimeGreen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Linen: G.Color = G.colors.Linen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Magenta: G.Color = G.colors.Magenta
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Maroon: G.Color = G.colors.Maroon
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val MediumAquamarine: G.Color = G.colors.MediumAquamarine
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val MediumBlue: G.Color = G.colors.MediumBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val MediumOrchid: G.Color = G.colors.MediumOrchid
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val MediumPurple: G.Color = G.colors.MediumPurple
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val MediumSeaGreen: G.Color = G.colors.MediumSeaGreen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val MediumSlateBlue: G.Color = G.colors.MediumSlateBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val MediumSpringGreen: G.Color = G.colors.MediumSpringGreen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val MediumTurquoise: G.Color = G.colors.MediumTurquoise
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val MediumVioletRed: G.Color = G.colors.MediumVioletRed
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val MidnightBlack: Color = G.colors.MidnightBlack
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val MidnightBlue: G.Color = G.colors.MidnightBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val MintCream: G.Color = G.colors.MintCream
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val MistyRose: G.Color = G.colors.MistyRose
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Moccasin: G.Color = G.colors.Moccasin
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val NavajoWhite: G.Color = G.colors.NavajoWhite
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Navy: G.Color = G.colors.Navy
  /** A named color as per [[http://montypython.50webs.com/scripts/Series_1/53.htm  the  MP  standard]]. This is a shortcut alias to [[o1.gui.colors]]. */
  lazy val NorwegianBlue: G.Color = G.colors.NorwegianBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val OldLace: G.Color = G.colors.OldLace
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Olive: G.Color = G.colors.Olive
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val OliveDrab: G.Color = G.colors.OliveDrab
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Orange: G.Color = G.colors.Orange
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val OrangeRed: G.Color = G.colors.OrangeRed
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Orchid: G.Color = G.colors.Orchid
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val PaleGoldenrod: G.Color = G.colors.PaleGoldenrod
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val PaleGreen: G.Color = G.colors.PaleGreen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val PaleTurquoise: G.Color = G.colors.PaleTurquoise
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val PaleVioletRed: G.Color = G.colors.PaleVioletRed
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val PapayaWhip: G.Color = G.colors.PapayaWhip
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val PeachPuff: G.Color = G.colors.PeachPuff
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Peru: G.Color = G.colors.Peru
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val PhthaloBlue: Color = G.colors.PhthaloBlue
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val PhthaloGreen: Color = G.colors.PhthaloGreen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Pink: G.Color = G.colors.Pink
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Plum: G.Color = G.colors.Plum
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val PowderBlue: G.Color = G.colors.PowderBlue
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val PrussianBlue: Color = G.colors.PrussianBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Purple: G.Color = G.colors.Purple
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Red: G.Color = G.colors.Red
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val RosyBrown: G.Color = G.colors.RosyBrown
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val RoyalBlue: G.Color = G.colors.RoyalBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val SaddleBrown: G.Color = G.colors.SaddleBrown
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Salmon: G.Color = G.colors.Salmon
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val SandyBrown: G.Color = G.colors.SandyBrown
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val SapGreen: Color = G.colors.SapGreen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val SeaGreen: G.Color = G.colors.SeaGreen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val SeaShell: G.Color = G.colors.SeaShell
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Sienna: G.Color = G.colors.Sienna
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Silver: G.Color = G.colors.Silver
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val SkyBlue: G.Color = G.colors.SkyBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val SlateBlue: G.Color = G.colors.SlateBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val SlateGray: G.Color = G.colors.SlateGray
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val SlateGrey: G.Color = G.colors.SlateGrey
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Snow: G.Color = G.colors.Snow
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val SpringGreen: G.Color = G.colors.SpringGreen
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val SteelBlue: G.Color = G.colors.SteelBlue
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Tan: G.Color = G.colors.Tan
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Teal: G.Color = G.colors.Teal
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Thistle: G.Color = G.colors.Thistle
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val TitaniumHwite: Color = G.colors.TitaniumHwite
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Tomato: G.Color = G.colors.Tomato
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Turquoise: G.Color = G.colors.Turquoise
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val VanDykeBrown: Color = G.colors.VanDykeBrown
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Violet: G.Color = G.colors.Violet
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Wheat: G.Color = G.colors.Wheat
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val White: G.Color = G.colors.White
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val WhiteSmoke: G.Color = G.colors.WhiteSmoke
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val Yellow: G.Color = G.colors.Yellow
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val YellowGreen: G.Color = G.colors.YellowGreen
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. This is a shortcut alias to [[o1.gui.colors]]. */
  val YellowOchre: Color = G.colors.YellowOchre


  /** A grayscale color that contains 10% black. This is a shortcut alias to [[o1.gui.colors]]. */
  val Black10: G.Color = G.colors.Black10
  /** A grayscale color that contains 20% black. This is a shortcut alias to [[o1.gui.colors]]. */
  val Black20: G.Color = G.colors.Black20
  /** A grayscale color that contains 30% black. This is a shortcut alias to [[o1.gui.colors]]. */
  val Black30: G.Color = G.colors.Black30
  /** A grayscale color that contains 40% black. This is a shortcut alias to [[o1.gui.colors]]. */
  val Black40: G.Color = G.colors.Black40
  /** A grayscale color that contains 50% black. This is a shortcut alias to [[o1.gui.colors]]. */
  val Black50: G.Color = G.colors.Black50
  /** A grayscale color that contains 60% black. This is a shortcut alias to [[o1.gui.colors]]. */
  val Black60: G.Color = G.colors.Black60
  /** A grayscale color that contains 70% black. This is a shortcut alias to [[o1.gui.colors]]. */
  val Black70: G.Color = G.colors.Black70
  /** A grayscale color that contains 80% black. This is a shortcut alias to [[o1.gui.colors]]. */
  val Black80: G.Color = G.colors.Black80
  /** A grayscale color that contains 90% black. This is a shortcut alias to [[o1.gui.colors]]. */
  val Black90: G.Color = G.colors.Black90
  /** A grayscale color that contains 10% white. This is a shortcut alias to [[o1.gui.colors]]. */
  val White10: G.Color = G.colors.White10
  /** A grayscale color that contains 20% white. This is a shortcut alias to [[o1.gui.colors]]. */
  val White20: G.Color = G.colors.White20
  /** A grayscale color that contains 30% white. This is a shortcut alias to [[o1.gui.colors]]. */
  val White30: G.Color = G.colors.White30
  /** A grayscale color that contains 40% white. This is a shortcut alias to [[o1.gui.colors]]. */
  val White40: G.Color = G.colors.White40
  /** A grayscale color that contains 50% white. This is a shortcut alias to [[o1.gui.colors]]. */
  val White50: G.Color = G.colors.White50
  /** A grayscale color that contains 60% white. This is a shortcut alias to [[o1.gui.colors]]. */
  val White60: G.Color = G.colors.White60
  /** A grayscale color that contains 70% white. This is a shortcut alias to [[o1.gui.colors]]. */
  val White70: G.Color = G.colors.White70
  /** A grayscale color that contains 80% white. This is a shortcut alias to [[o1.gui.colors]]. */
  val White80: G.Color = G.colors.White80
  /** A grayscale color that contains 90% white. This is a shortcut alias to [[o1.gui.colors]]. */
  val White90: G.Color = G.colors.White90

}

