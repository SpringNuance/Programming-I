package o1.cruising
import scala.collection.mutable.Buffer

object Cruising extends App {
  val car = new Car
  car.receivePassenger(new Schoolkid("P. Pupil"))
  car.receivePassenger(new ChemicalEngineer)
  car.receivePassenger(new MechanicalEngineer)
  car.receivePassenger(new ElectricalEngineer)
  car.receivePassenger(new ComputerScientist)
  car.start()
}


class Car {
  private val passengers = Buffer[Passenger]()

  def receivePassenger(passenger: Passenger) = {
    passenger.sitDown()
    this.passengers += passenger
  }

  def start() = {
    println("(The car won't start.)")
    for (passenger <- this.passengers) {
      passenger.remark()
    }
  }
}



abstract class Passenger(val name: String) {
  def sitDown() = {
    println(this.name + " finds a seat.")
  }

  def speak(sentence: String) = {
    println(this.name + ": " + sentence)
  }

  def diagnosis: String

  def remark() = {
    this.speak(this.diagnosis)
  }
}



abstract class Student(name: String) extends Passenger(name) {
  def diagnosis = "No clue what's wrong."
}



class Schoolkid(name: String) extends Student(name)



abstract class TechStudent(name: String) extends Student(name) {
  override def remark() = {
    super.remark()
    this.speak("Clear as day.")
  }
}



class ChemicalEngineer extends TechStudent("C. Chemist") {
  override def diagnosis = "It's the wrong octane. Next time, I'll do the refueling."
}



class MechanicalEngineer extends TechStudent("M. Machine") {

  override def diagnosis = "Nothing wrong with the gas. It must be the pistons."

  override def speak(sentence: String) = {
    super.speak(sentence.replace(".", "!"))
  }
}



class ElectricalEngineer extends TechStudent("E. Electra") {
  override def sitDown() = {
    println(this.name + " claims a front seat.")
  }

  override def diagnosis = "Hogwash. The spark plugs are faulty."
}



class ComputerScientist extends TechStudent("C.S. Student") {
  override def remark() = {
    this.speak("No clue what's wrong.")
    this.speak(this.diagnosis)
  }

  override def diagnosis = "Let's all get out of the car, close the doors, reopen, and try again."
}


