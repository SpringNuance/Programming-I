package o1.train

/** The class `Train` represents trains which consist of a number of passenger cars.
  * The cars are numbered from one upwards (starting from the head of the train).
  *
  * A train object is mutable. New cars can be added to it with the `addCar` method,
  * but initially a train has no cars. Trains have a maximum length (in cars) which
  * they must not exceed.
  *
  * A train may contain cars of various types, that is, instances of any class that
  * mixes in the `TrainCar` trait.
  *
  * @param description  a description of the train, e.g. "From Helsinki to Oulu, leaves at 8:13" */
class Train(val description: String) {

  private val cars = Array.ofDim[TrainCar](Train.MaximumLength)      // container
  private var numberOfCars = 0                                       // stepper


  /** Adds the given car to the end of the train, if possible.
    * @param car  the car to be added
    * @return `true` if the car was added, `false` if the length would have become excessive
    * @see [[Train.MaximumLength]] */
  def addCar(car: TrainCar) = {
    if (this.numberOfCars < this.cars.length) {
      this.numberOfCars += 1
      this.cars(this.numberOfCars) = car
      true
    } else {
      false
    }
  }


  /** Returns the car indicated by the given number, wrapped in an `Option`.
    * Returns `None` if the given number is too low or too high.
    * @param numberOfCar  the number of the car that should be returned
    *                     (must be between 1 and the number of cars in
    *                     the train or the result will be None) */
  def car(numberOfCar: Int) = this.cars.take(this.numberOfCars).lift(numberOfCar - 1)


  /** Returns the number of (passenger) cars there are in this train. */
  def length = this.numberOfCars

}


/** This companion object of the `Train` class merely provides a named constant that the class uses.
  * @see the class [[Train]] */
object Train {

  /** The maximum length, in cars, of a train. (This is a ''constant''.) */
  val MaximumLength = 30

}
