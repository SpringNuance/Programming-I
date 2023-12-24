package o1.carsim
import o1.Pos
import scala.math._


class Car(val fuelConsumption: Double, val tankSize: Double, initialFuel: Double, initialLocation: Pos) {
  private var amountFuel:Double = initialFuel
  private var currentFuel:Double = 0
  private var currentPos:Pos = initialLocation
  private var counterPos:Pos = Pos(0,0)
  private var currentMeters: Double = 0
  private var counterMeters: Double = 0
  private var x:Double = 0
  def location: Pos = currentPos

  def fuel(toBeAdded: Double): Double = {
     if (amountFuel + toBeAdded > tankSize) {
       var y:Double = tankSize - amountFuel
       amountFuel = tankSize
           y
     }
     else {
       amountFuel += toBeAdded
       toBeAdded
     }
  }

  def fuel(): Double = {
    var refillFuel:Double = tankSize - amountFuel
    amountFuel = tankSize
    refillFuel
  }

  def fuelRatio(): Double = (amountFuel/tankSize)*100

  def fuelRange(): Double = (amountFuel/fuelConsumption)*100000

  def drive(destination: Pos, metersToDestination: Double): Unit = {
        currentFuel = amountFuel - (metersToDestination/100000) * fuelConsumption
    if (currentFuel <= 0) {
       x = 0
    }
    else if (currentFuel > amountFuel) {
       x = 0
       } else {
       x = currentFuel
       }
    /////////////////////////////////////////
    if (amountFuel > 0 && fuelRange() > metersToDestination) {
      currentMeters = metersToDestination
    } else {
      currentMeters = fuelRange()
    }
      counterMeters += currentMeters
      amountFuel = x
    //////////////////////////////////////////
    var ratio = currentMeters/metersToDestination
    counterPos = (destination - currentPos)*ratio
    currentPos += counterPos
  }
    //////////////////////////////////////////
  def metersDriven(): Double = counterMeters
}
// Note the units: distances in meters, fuel consumption in liters per hundred kilometers.
//
//The trickiest bit may be to compute the car’s new location when fuel runs out. Compute the ratio between the distance the car
// actually drives and the distance it was supposed to drive (the method’s second parameter). Since we’re modeling the car’s movement as
// a straight line towards the destination, the same ratio applies to each of the car’s two coordinates.



//Stick to the specified interface, please
//
//Do not add public members to the class beyond those requested. On the other hand, you’re free to add private members as you see fit.
//
//This guideline applies to all of O1’s programming assignments that specify public interfaces for you to implement.
//An optional hint about instance variables

//Use instance variables to track information that needs to persist between method calls. This includes each car’s fuel consumption rate and fuel-tank size, both of which are already defined as instance variables in the given code.
//
//Several methods operate on the car’s current fuel level. Store that information in an instance variable so that it’s not lost between method calls.
//
//The methods location and drive access the car’s current location, which you’ll also need to store as part of the car object.
//
//metersDriven and drive need to know how far the car has been driven in total.
//
//You don’t need additional instance variables beyond those. If you want to store some information temporarily while a method runs, use a local variable.
//
//Step 2 of 6: The easier(?) methods
//Ignore the most complex method, drive, for now. Implement the other methods first.
//
//Two methods named fuel?
//
//Did you notice that there are two different methods named fuel, with different parameter lists?
//
//Even though these are two entirely separate methods, you can use one of them to implement the other.
//
//Further down in this chapter, there is some additional information about methods that share a name.
//
//
//An optional hint for location and metersDriven
//
//
//Hide the hint
//
//As long as you have the car’s location and total meters stored in instance variables, these corresponding methods should be extremely simple to implement.
//
//An additional hint for the fuel methods
//
//
//Hide the hint
//
//The single-parameter fuel method is very similar to some methods that you’ve written in earlier assignments (cf. account withdrawals). Carefully note what the method should return. Use a local variable as you compute the return value.
//
//The parameterless fuel is easy to implement: call the other fuel and pass a large enough number as a parameter.
//
//
//An optional hint for fuelRatio and fuelRange
//
//
//Hide the hint
//
//Simple arithmetic will do the trick. Just make sure you use the right units: the percentage should be between zero and a hundred; the car’s “range” is measured in meters (not hundreds of kilometers).
//Step 3 of 6: Test your class as a separate component
//Try running the test program CarTest that uses Car separately from the rest of the CarSim app. Apart from driving, the test program should now produce output that makes sense.
//
//You can edit the test program to improve coverage. You can also experiment with Car in the REPL.
//
//Step 4 of 6: Test your class as part of CarSim
//Launch CarSim via the app object o1.carsim.gui.CarSim. Try adding cars and fueling them through the GUI. There are some usage instructions at the bottom of the GUI window.
//
//Step 5 of 6: Implement drive
//As the documentation says: in this assignment, you can assume that each car moves in a straight line, “as the crow flies”. Sometimes, a car will fail to reach the destination as its fuel runs out.
//
//In case you have trouble with the math required to determine where a car stops, this may help:
//
//Despite the fact that CarSim uses the x and y of a Pos to represent latitude and longitude on the Earth’s round surface, this assignment has been set up for you so that you can continue to think about Pos objects in terms of ordinary, flat, two-dimensional coordinates.
//Forget programming for a while. Draw a diagram of the car’s movement, and solve the math problem first. Then consider how to express the solution as Scala.
//You don’t have to model the car’s velocity. Just take care of the things described in the Scaladocs.
//An optional hint about the math
//
//
//Hide
//
//You won’t need any trigonometry. Basic arithmetic will do: multiplication, addition, division, and subtraction.
//
//You may also draw on the methods of class Pos. The already-familiar distance method will be of use, and you may find other methods described in the docs helpful as well.
//
//
//Further hints
//
//
//Hide
//
//Note the units: distances in meters, fuel consumption in liters per hundred kilometers.
//
//The trickiest bit may be to compute the car’s new location when fuel runs out. Compute the ratio between the distance the car actually drives and the distance it was supposed to drive (the method’s second parameter). Since we’re modeling the car’s movement as a straight line towards the destination, the same ratio applies to each of the car’s two coordinates.