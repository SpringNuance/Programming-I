package o1.grid


/** The class `CompassDir` represents the cardinal compass directions in a grid-like coordinate system.
  * There are exactly four instances of this class: `North`, `East`, `South` and `West`, which are
  * also defined in this package.
  *
  * All the `CompassDir` objects are immutable.
  *
  * This class and its instances have aliases in the top-level package [[o1]], so they are accessible
  * to students simply via `import o1._`.
  *
  * @see [[GridPos]]
  * @param xStep  the change in x coordinate if one moves one step in this direction. For instance, `West` has an `xStep` of -1 and `North` has an `xStep` of 0.
  * @param yStep  the change in y coordinate if one moves one step in this direction. For instance, `North` has an `yStep` of -1 and `West` has an `yStep` of 0. */
sealed abstract class CompassDir(val xStep: Int, val yStep: Int) extends Product with Serializable {
  // The word sealed means that you can't directly inherit from this class except within this file.
  // Consequently, there are only four objects of type CompassDir, as defined below.

  /** Returns the next of the four compass directions, moving clockwise from this one.
    * For instance, calling this method on `North` returns `East`. */
  def clockwise = CompassDir.next(this)


  /** Returns the next of the four compass directions, moving counterclockwise from this
    * one. For instance, calling this method on `North` returns `West`. */
  def counterClockwise = CompassDir.previous(this)


  /** Returns a textual description of this direction, which is the English name of the direction. */
  override def toString = this.getClass.getSimpleName.replaceAll("\\$", "")

}



/** This companion object of [[CompassDir class `CompassDir`]] provides a selection of related
  * constants and utility methods.
  *
  * This object has an alias in the top-level package [[o1]], so it’s accessible  to students
  * simply via `import o1._`. */
object CompassDir {

  /** This immutable singleton object represents the northwardly compass direction. It’s one of the four predefined instances of
    * class `CompassDir`. It has an alias in the top-level package [[o1]], so it’s accessible to students simply via `import o1._`. */
  case object North extends CompassDir( 0,-1)
  /** This immutable singleton object represents the eastwardly compass direction. It’s one of the four predefined instances of
    * class `CompassDir`. It has an alias in the top-level package [[o1]], so it’s accessible to students simply via `import o1._`. */
  case object East  extends CompassDir( 1, 0)
  /** This immutable singleton object represents the southwardly compass direction. It’s one of the four predefined instances of
    * class `CompassDir`. It has an alias in the top-level package [[o1]], so it’s accessible to students simply via `import o1._`. */
  case object South extends CompassDir( 0, 1)
  /** This immutable singleton object represents the westwardly compass direction. It’s one of the four predefined instances of
    * class `CompassDir`. It has an alias in the top-level package [[o1]], so it’s accessible to students simply via `import o1._`. */
  case object West  extends CompassDir(-1, 0)


  /** a collection of all the four directions, in clockwise order starting with `North` */
  val Clockwise = Vector[CompassDir](North, East, South, West)

  /** The number of the compass directions represented by class `CompassDir`. Four, that is. */
  val Count = Clockwise.size

  private val next = Clockwise.zip(Clockwise.tail ++ Clockwise.init).toMap
  private val previous = this.next.map( _.swap )

  private type Key = scala.swing.event.Key.Value
  private val  Key = scala.swing.event.Key
  private val ArrowToDir = Map(Key.Up -> North, Key.Left -> West, Key.Down-> South, Key.Right-> East)
  private val WASDToDir  = Map(Key.W  -> North, Key.A    -> West, Key.S   -> South, Key.D    -> East)
  private val KeyToDir   = ArrowToDir ++ WASDToDir

  /** Returns the [[CompassDir]] that corresponds to the given arrow key. For example, the right arrow corresponds to `East`.
    * @param key  any key on the keyboard
    * @return one of the four `CompassDir`s; `None` if the given key was not one of the four arrow keys
    * @see [[fromWASD]], [[fromKey]] */
  def fromArrowKey(key: Key) = ArrowToDir.get(key)

  /** Returns the [[CompassDir]] that corresponds to the given WASD key. For example, the D key corresponds to `East`.
    * @param key  any key on the keyboard
    * @return one of the four `CompassDir`s; `None` if the given key was not one of the four WASD keys
    * @see [[fromArrowKey]], [[fromKey]] */
  def fromWASD(key: Key)     = WASDToDir.get(key)

  /** Returns the [[CompassDir]] that corresponds to the given arrow key. For example, the right arrow or the D key corresponds to `East`.
    * @param key  any key on the keyboard
    * @return one of the four `CompassDir`s; `None` if the given key was not one of the four arrow keys or one of the four WASD keys
    * @see [[fromArrowKey]], [[fromWASD]] */
  def fromKey(key: Key)      = KeyToDir.get(key)

}

