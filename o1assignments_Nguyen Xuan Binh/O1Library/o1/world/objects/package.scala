
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.world

import o1.util.ConvenientDouble

import smcl.modeling.d2.{RatioAnchor => SMCLAnchor}


/** This package contains tools for representing objects that exist in two-dimensional space.
  * In particular, it defines:
  *
  *  - the [[Anchor]] type: an anchor is the point where an
  *    object connects to its environment; and
  *  - a number of traits ([[HasPos]], [[HasVelocity]], etc.)
  *    the classes of an application can mix in to gain access
  *    to various convenience methods.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this package works or can be used.'''
  * That being said, some students may wish to experiment with some of its contents. Many of the traits in this
  * package have aliases in the top-level package [[o1]], so they are accessible to students simply via `import o1._`. */
package object objects {

  o1.util.smclInit()


  private[o1] implicit class ConvertableSMCLAnchor(val self: SMCLAnchor) extends AnyVal {
    def toO1Anchor: Option[Anchor] = (self.widthRatio, self.heightRatio) match {
      case (0.0, 0.0) => Some(Anchor.TopLeft)
      case (0.0, 0.5) => Some(Anchor.CenterLeft)
      case (0.0, 1.0) => Some(Anchor.BottomLeft)
      case (0.5, 0.0) => Some(Anchor.TopCenter)
      case (0.5, 0.5) => Some(Anchor.Center)
      case (0.5, 1.0) => Some(Anchor.BottomCenter)
      case (1.0, 0.0) => Some(Anchor.TopRight)
      case (1.0, 0.5) => Some(Anchor.CenterRight)
      case (1.0, 1.0) => Some(Anchor.BottomRight)
      case _          => None
    }
  }



  /** A supertype for objects that have a size: a width and a height. */
  trait HasSize {
    /** this object’s width: the distance from its left edge to its right edge */
    def width: Double
    /** this object’s height: the distance from its top edge to its bottom edge */
    def height: Double
    /** the location of this object’s center: the average point between its edges,
      * expressed in coordinates relative to the object’s top left-hand corner */
    def centerFromTopLeft = Pos(this.width / 2, this.height / 2)
  }



  /** A supertype for objects that have a location in two-dimensional space. */
  trait HasPos {
    /** the object’s location */
    def pos: Pos
    /** Determines the distance (“as the crow flies”) between this object’s `pos` and that of the given object. */
    def distance(another: HasPos) = this.pos.distance(another.pos)
  }



  /** A supertype for objects that have an anchoring point. */
  trait HasAnchor extends HasSize {
    /** the object’s anchoring point */
    def anchor: Anchor
    /** Returns the [[Pos]] of the anchoring point within this object, expressed as coordinates
      * relative to the object’s top left-hand corner. */
    def internalAnchorPos = this.anchor.internalPosWithin(this)
    /** Returns the x coordinate of the anchoring point within this object, relative to the object’s left edge. */
    def internalAnchorX = this.anchor.internalXWithin(this)
    /** Returns the y coordinate of the anchoring point within this object, relative to the object’s top edge. */
    def internalAnchorY = this.anchor.internalYWithin(this)
  }



  /** A supertype for objects that have a position and a velocity in two-dimensional space. */
  trait HasVelocity extends HasPos {

    /** the object's velocity */
    def velocity: Velocity

    /** Returns the `Pos` that this object, moving from its current location at its current velocity,
      * reaches in one unit of time. That is, adds the `dx` and `dy` components of the velocity to
      * the current `Pos` and returns the result. */
    def nextPos = this.pos.nextPos(this.velocity)

    /** Determines whether this objecthas a rightward velocity, as per [[Direction.isRightward]]. */
    def isRightbound = this.velocity.direction.isRightward
    /** Determines whether this objecthas a leftward velocity, as per [[Direction.isLeftward]]. */
    def isLeftbound = this.velocity.direction.isLeftward
    /** Determines whether this objecthas an upward velocity, as per [[Direction.isUpward]]. */
    def isUpbound = this.velocity.direction.isUpward
    /** Determines whether this objecthas a downward velocity, as per [[Direction.isDownward]]. */
    def isDownbound = this.velocity.direction.isDownward
    /** Determines whether this objectis bound for roughly the given direction, as per [[Direction.sharesQuadrant]]. */
    def isBoundFor(direction: Direction) = this.velocity.direction.sharesQuadrant(direction)
  }


  /** A supertype for objects that take up a rectangular area and have an anchoring point.
    * The object’s `pos` indicates the position of the anchoring point; the locations of its
    * edges are determined relative to the anchor. */
  trait HasEdges extends HasPos with HasAnchor {

    /** Returns the object’s anchoring point, which is [[Anchor.Center]] unless overridden. */
    def anchor: Anchor = Anchor.Center

    /** Returns the x coordinate of this object’s left edge. */
    def left: Double = this.pos.x - this.internalAnchorX
    /** Returns the y coordinate of the top edge. */
    def top: Double = this.pos.y - this.internalAnchorY
    /** Returns the x coordinate of the right edge. */
    def right: Double = this.left + this.width
    /** Returns the y coordinate of the bottom edge. */
    def bottom: Double = this.top  + this.height
    /** Returns the coordinates of this object’s top left-hand corner. */
    def topLeft: Pos = Pos(this.left, this.top)
    /** Returns the coordinates of this object’s bottom left-hand corner. */
    def bottomLeft: Pos = Pos(this.left, this.bottom)
    /** Returns the coordinates of this object’s top right-hand corner. */
    def topRight: Pos = Pos(this.right, this.top)
    /** Returns the coordinates of this object’s bottom right-hand corner. */
    def bottomRight: Pos = Pos(this.right, this.bottom)
    /** Returns the average position between this object’s edges. */
    def center: Pos = this.topLeft + this.centerFromTopLeft
    /** Determines whether the given `Pos`’s x coordinate is between this object’s left edge (inclusive)
      * and its right edge (exclusive) and if it’s y coordinate is similarly between the top and bottom edges. */
    def containsBetweenEdges(candidate: Pos): Boolean = candidate.x.isBetween(this.left, this.right) &&
                                                        candidate.y.isBetween(this.top,  this.bottom)

    /** Looks for a position for this object that is as close to possible to a particular
      * target position but keeps the left and right edges of the object within the bounds
      * of the given other object.
      * @param container        another object that sets the boundaries for the return value
      * @param desiredPosition  the ideal position for this object (defaults to its current [[pos]])
      * @return the location closest to `desiredPosition` where this object’s [[anchor]] can
      *         be placed without its left or right edge extending beyond `container` */
    def clampXWhollyInside(container: HasEdges, desiredPosition: Pos = this.pos): Pos =
      desiredPosition.clampX(container.left + this.internalAnchorX, container.right + this.internalAnchorX - this.width)

    /** Looks for a position for this object that is as close to possible to a particular
      * target position but keeps the top and bottom edges of the object within the bounds
      * of the given other object.
      * @param container        another object that sets the boundaries for the return value
      * @param desiredPosition  the ideal position for this object (defaults to its current [[pos]])
      * @return the location closest to `desiredPosition` where this object’s [[anchor]] can
      *         be placed without its top or bottom edge extending beyond `container` */
    def clampYWhollyInside(container: HasEdges, desiredPosition: Pos = this.pos): Pos =
      desiredPosition.clampY(container.top + this.internalAnchorY, container.bottom + this.internalAnchorY - this.height)

    /** Looks for a position for this object that is as close to possible to a particular target
      * position but keeps the object's edges within the bounds of the given other object.
      * @param container        another object that sets the boundaries for the return value
      * @param desiredPosition  the ideal position for this object (defaults to its current [[pos]])
      * @return the location closest to `desiredPosition` where this object’s [[anchor]] can
      *         be placed without its edges extending beyond `container` */
    def clampWhollyInside(container: HasEdges, desiredPosition: Pos = this.pos): Pos = {
      val anchorPos = this.internalAnchorPos
      desiredPosition.clamp(xMin = container.left + anchorPos.x, xMax = container.right  + anchorPos.x - this.width,
                            yMin = container.top  + anchorPos.y, yMax = container.bottom + anchorPos.y - this.height)
    }

    /** Returns the position within this object’s edges that is as close as possible to the given position. */
    def closestPosTo(target: Pos): Pos = target.clamp(this.left, this.right, this.top, this.bottom)

  }


  /** A supertype for objects that take up a rectangular area and whose position may be constrained
    * by a larger object around them. */
  trait HasContainer extends HasEdges {

    /** another object that [[clampX]], [[clampY]], and [[clamp]] use for constraining this
      * object’s position */
    val container: HasEdges

    /** Looks for a position for this object that is as close to possible to the object's
      * current [[pos]] but keeps the left and right edges of the object within the bounds
      * of the object's [[container]]. Returns that `Pos`. */
    def clampX(): Pos = this.clampXWhollyInside(container)

    /** Looks for a position for this object that is as close to possible to the object's
      * current [[pos]] but keeps the top and bottom edges of the object within the bounds
      * of the object's [[container]]. Returns that `Pos`. */
    def clampY(): Pos = this.clampYWhollyInside(container)

    /** Looks for a position for this object that is as close to possible to the object's
      * current [[pos]] but keeps the object's edges within the bounds of the object's
      * [[container]]. Returns that `Pos`. */
    def clamp(): Pos = this.clampWhollyInside(container)

  }


  /** This subpackage defines a few additional traits that extend the ones in of [[o1.world.objects]]. These
    * traits provide additional convenience methods for working with mutable objects in two-dimensional space.
    *
    * '''NOTE TO STUDENTS: In this course, you don't need to understand how this package works or can be used.'''
    * That being said, some students may wish to experiment with some of its contents. Many of the traits in this
    * package have aliases in the top-level package [[o1]], so they are accessible to students simply via `import o1._`. */
  object mutable {

    /** A supertype for objects that have a mutable location in two-dimensional space. */
    trait MutablePos extends HasPos {
    /** the object’s location */
      var pos: Pos
    }


    /** A supertype for objects that take up a rectangular area and have a mutable position that may
      * be constrained by a larger object around them. */
    trait ContainedObject extends MutablePos with HasContainer {

      /** Causes this object’s [[pos]] to change so that the object is within its container, as per [[HasContainer.clamp `clamp`]]. */
      def bringToContainer() = {
        this.pos = this.clamp()
      }

      /** Causes this object’s [[pos]] to change so that its left and right edges don’t extend beyond its container,
        * as per [[HasContainer.clampX `clampX`]]. */
      def bringXToContainer() = {
        this.pos = this.clampX()
      }

      /** Causes this object’s [[pos]] to change so that its top and bottom edges don’t extend beyond its container,
        * as per [[HasContainer.clampY `clampY`]]. */
      def bringYToContainer() = {
        this.pos = this.clampY()
      }

    }

    /** A supertype for objects that have a mutable position and a velocity in two-dimensional space. */
    trait MovingObject extends HasVelocity with MutablePos {

    /** Causes the object’s [[pos]] to change: the object moves from its current location at its
      * current velocity for one unit of time. That is, the `dx` and `dy` components of the
      * velocity are added to the object’s [[pos]]. */
      def moveAlong(): Unit = {
        this.pos = this.nextPos
      }

    /** Causes the object’s [[pos]] to change: the object moves from its current location at its
      * current velocity for one unit of time (as per [[moveAlong]]) and is then immediately
      * clamped to fit within the bounds of the given object (as per [[Pos.clamp]]). */
      def moveWithin(container: HasEdges): Unit = {
        this.moveAlong()
        this.pos = this.pos.clamp(container.left, container.right, container.top, container.bottom)
      }

    }


    /** A supertype for objects that take up a rectangular area and have a velocity as well as a
      * mutable position that may be constrained by a larger object around them. */
    trait MovingObjectInContainer extends ContainedObject with HasVelocity  {

    /** Causes the object’s [[pos]] to change: the object moves from its current location at its
      * current velocity for one unit of time. That is, the `dx` and `dy` components of the
      * velocity are added to the object’s [[pos]]. */
      def moveFreely(): Unit = {
        this.pos = this.nextPos
      }

    /** Causes the object’s [[pos]] to change: the object moves from its current location at its
      * current velocity for one unit of time (as per [[moveFreely]]) and is then immediately
      * clamped to the object’s container (as per [[ContainedObject.bringToContainer `bringToContainer`]]). */
      def moveWithinContainer(): Unit = {
        this.moveFreely()
        this.bringToContainer()
      }

    }


  }


}

