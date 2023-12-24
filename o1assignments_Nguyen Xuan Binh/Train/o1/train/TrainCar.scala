package o1.train

/** This trait represents the passenger cars of a train in a train ticket reservation system.
  * It describes the common operations of all passenger car types. Different classes representing
  * different kinds of train cars will have different implementations for these operations.
  *
  * In this simple implementation, no data is stored about who has reserved which places (seats, beds,
  * or the like) in the cars. A car object only keeps track of whether a place is reserved or not.
  *
  * A train car's state is mutable; it changes as reservations are made. */
trait TrainCar {

  /** Returns the number of places (seats, beds, or the like) this car has for passengers.
    * This is a positive number. */
  def numberOfPlaces: Int


  /** Returns the number of free, unreserved places (seats, beds, or the like) in this car. */
  def numberOfFreePlaces: Int


  /** Returns a figure between 0 and 100 that indicates how many percent of the car's
    * passenger places have been reserved. */
  def fullness = 100 * (this.numberOfPlaces - this.numberOfFreePlaces) / this.numberOfPlaces


  /** Reserves places (seats, beds, or the like) in the car for a group of people whose size is
    * indicated by the parameter. The whole group should be able to get places that are reasonably
    * close to each other (what exactly this means is left for concrete classes to decide). If it
    * is not possible to reserve suitable places for all members of the group, no places are
    * reserved at all. The return value indicates whether the places were reserved or not. */
  def reservePlaces(numberOfPeople: Int): Boolean

}
