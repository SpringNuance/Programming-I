package o1.grid

import scala.reflect.ClassTag
import o1.util.ConvenientInt
import scala.util.{Try,Success,Failure}

/** The class `Grid` represents rectangular grids that contain elements of a particular
  * kind. Each element in a grid is located at a unique pair of coordinates, represented
  * as a [[GridPos]].
  *
  * X coordinates run from 0 to `width-1`, y coordinates from 0 to `height-1`.
  * (0,0) corresponds to the upper left corner of the grid.
  *
  * There are different kinds of grids: the type of element that a grid contains is
  * defined by a type parameter. For instance `Grid[Square]` is a grid where each pair
  * of x and y coordinates contains a `Square` object, and `Grid[House]` is a grid
  * containing `House` objects.
  *
  * A `Grid` is mutable: it is possible to replace an element at a particular `GridPos`
  * with another. (Depending on the element type, the elements may be individually
  * mutable, too.) The width and height of a grid never change, however.
  *
  * Upon creation, a `Grid` initializes itself by calling [[initialElements]], which
  * produces an initial state for the grid.
  *
  * This class has an alias in the top-level package [[o1]], so it’s accessible to students
  * simply via `import o1._`.
  *
  * @param width   the number of elements in each row of the grid
  * @param height  the number of elements in each column of the grid */
abstract class Grid[Element: ClassTag](val width: Int, val height: Int) {

  /** the number of elements in this grid, in total. (Equals `width` times `height`.) */
  val size = width * height


  private val contents: Array[Array[Element]] = {
    val elems = try { this.initialElements } catch { case npe: NullPointerException => throw new RuntimeException("Grid initialization failed with a NullPointerException.\nPossible cause: trying to access an element’s (still nonexistent) neighbors or other parts of the unready grid while initializing.", npe) }
    require(elems.sizeIs == this.size, s"The number of elements returned by initialElements (${elems.size}) did not equal width times height (${this.size}).")
    elems.toArray.grouped(this.width).toArray.transpose
  }


  /** Returns the element at the given pair of coordinates. (This does the same as `apply`.)
    * @param location  a location on the grid (must be within range) */
  def elementAt(location: GridPos) = {
    require(this.contains(location), s"Attempted to access a non-existent location $location of a ${width}-by-${height} grid.")
    this.contents(location.x)(location.y)
  }


  /** Modifies the grid by replacing the existing element at the given location with the new element.
    * @param location    a location on the grid (must be within range)
    * @param newElement  the new element that replaces the old one at `location` */
  def update(location: GridPos, newElement: Element) = {
    require(this.contains(location), s"Attempted to update element at a non-existent location $location of a ${width}-by-${height} grid.")
    this.contents(location.x)(location.y) = newElement
  }


  /** Checks whether the grid contains the given x and y coordinates. */
  private def contains(x: Int, y: Int) = x.isBetween(0, this.width) && y.isBetween(0, this.height)


  /** Determines whether the grid contains the given pair of coordinates.
    * For instance,a grid with a width and height of 5 will contain (0, 0)
    * and (4, 4) but not (-1, -1), (4, 5) or (5, 4). */
  def contains(location: GridPos): Boolean = this.contains(location.x, location.y)


  /** Generates the elements that initially occupy the grid. This method is automatically
    * invoked by every `Grid` object upon creation. Subclasses should implement this method
    * as appropriate for the particular sort of grid desired.
    *
    * Note that since this method produces the initial contents of the `Grid`, it gets
    * called during initialization before the `Grid` actually has any elements as content.
    * Therefore, subclass implementations of this method must not depend on the state of the
    * new `Grid` (by calling `neighbors` or `elementAt`, for instance) or attempt to modify
    * the `Grid` (with `update`, for instance).
    *
    * This method is meant for initialization purposes only. To access all the elements of
    * an already-initialized `Grid`, call [[allElements]] instead.
    *
    * @return a collection of size `width` times `height` that contains the initial grid elements.
    *         The first element in the collection will appear at `GridPos` (0,0), the second at (1,0),
    *         and so on, filling in the first row before continuing on the second row at (1,0). */
  def initialElements: Seq[Element]


  /** Returns the element at the given pair of coordinates. (This does the same as `elementAt`.)
    * @param location  a location on the grid (must be within range) */
  def apply(location: GridPos) = this.elementAt(location)



  private def possibleElementAt(location: GridPos) =
    if (this.contains(location)) Some(this(location)) else None


  /** Returns a vector of all the neighboring elements of the indicated element. Depending on
    * the value of the second parameter, either only the four neighbors in cardinal compass
    * directions (north, east, south, west) are considered, or the four diagonals as well.
    *
    * Note that an element that is at the edge of the grid has fewer neighbors than one
    * in the middle. For instance, the element at (0, 0) of a 5-by-5 grid has only three
    * neighbors, diagonals included.
    *
    * @param middleLoc         the location between the neighbors
    * @param includeDiagonals  `true` if diagonal neighbors also count (resulting in up to eight neighbors),
    *                          `false` if only cardinal directions count (resulting in up to four) */
  def neighbors(middleLoc: GridPos, includeDiagonals: Boolean): Vector[Element] = {
    def neighborsOfLoc(dir: CompassDir) = {
      val cardinal = middleLoc.neighbor(dir)
      val candidates = if (includeDiagonals) Vector(cardinal, cardinal.neighbor(dir.clockwise)) else Vector(cardinal)
      candidates.flatMap(this.possibleElementAt)
    }
    CompassDir.Clockwise.flatMap(neighborsOfLoc)
  }


  /** Returns a collection of all the locations on the grid. */
  def allPositions: Vector[GridPos] =
    (0 until this.size).map( n => GridPos(n % this.width, n / this.width) ).toVector


  /** Returns a collection of all the elements currently in the grid. */
  def allElements: Vector[Element] =
    for (pos <- this.allPositions; elem <- Option(this(pos))) yield elem


  /** Swaps the elements at two given locations on the grid, which must be within range. */
  def swap(location1: GridPos, location2: GridPos) = {
    val temporary = this(location1)
    this(location1) = this(location2)
    this(location2) = temporary
  }

}


