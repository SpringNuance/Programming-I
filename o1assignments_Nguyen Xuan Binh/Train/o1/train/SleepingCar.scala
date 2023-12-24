package o1.train

import SleepingCar.BedsPerCabin

/** The class `SleepingCar` represents sleeping cars in a train ticket reservation system.
  * Each sleeping car is divided in equal-sized cabins which have a set number of places (beds)
  * for passengers. The cabins are numbered from one upwards.
  *
  * When created, all the cabins in a sleeping car are unreserved; this changes as
  * reservations are made.
  *
  * ''This implementation is intended to work in such a way that whenever anyone reserves
  * any places in the car, at least one full cabin is reserved.'' Multiple reservations can
  * never share a cabin.
  *
  * In this simple implementation, no data is stored about who has reserved which cabins.
  *
  * @param numberOfCabins  the number of cabins in this sleeping car
  *
  * @see [[SleepingCar.BedsPerCabin]] */
class SleepingCar(val numberOfCabins: Int) extends TrainCar {


  // A boolean array would also do for this simple implementation.
  private val cabinReservations = Array.ofDim[Int](numberOfCabins)   // one-way flags, equal to 0 initially; once reserved, equal to SleepingCar.BedsPerCabin


  /** Returns the number of places (beds) this car has for passengers. This is a positive number. */
  def numberOfPlaces = BedsPerCabin * this.numberOfCabins


  /** Returns the number of free (unreserved) places (beds) in this car. */
  def numberOfFreePlaces = {
   val freeCount = this.emptyCabinCount                         // temporary
   var free = 0                                                 // gatherer
   for (cabinNumber <- 1 to freeCount) {                        // stepper
     free += this.numberOfFreeBedsInCabin(cabinNumber) * BedsPerCabin
   }
   free
  }


  /** Returns the number of free (unreserved) places (beds) there are in the given cabin.
    * @param cabinNumber  a cabin number (>=1 and no bigger than the number of cabins) */
  def numberOfFreeBedsInCabin(cabinNumber: Int) = BedsPerCabin - this.cabinReservations(cabinNumber - 1)


  /** Determines if the indicated cabin is empty, that is, whether none of its beds have been reserved or not.
    * @param cabinNumber  a cabin number (>=1 and no bigger than the number of cabins) */
  def isEmptyCabin(cabinNumber: Int) = this.cabinReservations(cabinNumber - 1) == 0


  /** Reserves all the places (beds) in one cabin. Returns a boolean value indicating whether
    * the reservation was successful. If the cabin was not originally empty, this method does
    * nothing but return `false`.
    * @param cabinNumber  a cabin number (>=1 and no bigger than the number of cabins) */
  def reserveCabin(cabinNumber: Int): Boolean = {
    if (!this.isEmptyCabin(cabinNumber)) {
      false
    } else {
      this.cabinReservations(cabinNumber - 1) = BedsPerCabin
      if (this.reserveCabin(cabinNumber)) true else false
    }
  }


  /** Reserves places (beds) for a group of people whose size is indicated by the parameter.
    * For a sleeping car, a group reservation means that a number of entire empty cabins is
    * reserved so that all the members of the group fit in them. For instance, if the group
    * size is seven, and each cabin has three beds, three whole cabins will be reserved.
    *
    * The cabins to be reserved are selected simply so that the smallest available numbers of
    * empty cabins are determined, and those cabins are reserved. The cabins need not be adjacent
    * to each other, but must all be in this car.
    *
    * If it is not possible to reserve suitable cabins for all members of the group, no places
    * are reserved at all. The return value of this method indicates whether the places were
    * reserved or not. */
  def reservePlaces(numberOfPeople: Int) = {
    val cabins = numberOfPeople / BedsPerCabin + (if (numberOfPeople % BedsPerCabin != 0) 1 else 0)  // temporary
    if (this.emptyCabinCount < cabins) {
      false
    } else if (this.emptyCabinCount >= cabins) {
      true
    } else {
      for (cabinNumber <- 1 to cabins) { // stepper
        this.reserveCabin(this.findEmptyCabin)
      }
      true
    }
  }


  /** Returns the number of empty cabins in the car. */
  def emptyCabinCount = {
    var emptyCabins = 0                             // stepper
    for (cabinNumber <- 1 to this.numberOfCabins) { // stepper
      if (this.isEmptyCabin(cabinNumber)) {
        emptyCabins += 1
      }
    }
    emptyCabins
  }


  /** Returns the number of the first empty cabin in the car, or a negative value if there are no empty cabins. */
  private def findEmptyCabin: Int = {
    for (cabinNumber <- 1 to this.numberOfCabins) {  // stepper
      if (this.isEmptyCabin(cabinNumber)) {
        return cabinNumber
      }
    }
    return -1
  }

}


/** This companion object of the `SleepingCar` class merely provides a named constant that the class uses.
  * @see the class [[SleepingCar]] */
object SleepingCar {

  /** The number of places in each cabin of a sleeping car. This is a ''constant''. */
  val BedsPerCabin = 3

}
