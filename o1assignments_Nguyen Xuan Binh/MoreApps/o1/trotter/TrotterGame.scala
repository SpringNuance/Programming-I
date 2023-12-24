package o1.trotter

import o1._

// This program is introduced in Chapter 3.6.

/** Represents a “game” in which a horse trots about a two-dimensional surface.
  * @param sizeInSteps  the size of the visible game area, counted in movement steps
  * @param horseStep    the number of coordinate units (pixels) the horse moves per step */
class TrotterGame(val sizeInSteps: Int, val horseStep: Int) {

  /** the horse’s current coordinates */
  var horsePos = new Pos((sizeInSteps / 5 + 0.5) * horseStep,
                         (sizeInSteps / 2 + 0.5) * horseStep)

  /** the direction the horse is currently trotting in */
  var horseHeading: Direction = Direction.Right

  /** Moves the horse one step in its current heading. */
  def advance() = {
    this.horsePos = this.horsePos.add(horseHeading.dx * horseStep,
                                      horseHeading.dy * horseStep)
  }

  /** Returns a brief textual description of the trotter’s world. */
  override def toString = s"a $sizeInSteps-by-$sizeInSteps grid"

}


