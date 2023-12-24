package o1

// This is the familiar Employee class from Week 2, but this version has been
// annotated with documentation comments. The class documentation in this module's
// doc folder has been automatically produced from this definition.

/** The class `Employee` represents employees (in an imaginary accounting app).
  * Each employee has a number of attributes such as name and monthly salary.
  * Most of these attributes are mutable.
  *
  * @param name           the name of the employee
  * @param yearOfBirth    the year the employee was born
  * @param monthlySalary  the monthly salary of the employee, in euros and excluding any incidental costs */
class Employee(var name: String, val yearOfBirth: Int, var monthlySalary: Double) {

  /** working time relative to a full week. E.g., 1.0 means a full-time employee and 0.5 a half-timer. */
  var workingTime = 1.0


  /** Returns the number of years the employee turns during the given year. */
  def ageInYear(year: Int) = year - this.yearOfBirth


  /** Returns the monthly cost of the employee to the employer. This figure equals
    * the product of the employee's monthly salary (e.g., 4000), their working time
    * (e.g., 0.6), and a multiplier for incidental costs (e.g., 1.3).
    *
    * @param multiplier  a multiplier used by the employer to estimate the additional
    *                    costs of employing a person, apart from their salary */
  def monthlyCost(multiplier: Double) = this.monthlySalary * this.workingTime * multiplier


  /** Modifies the employee's salary by multiplying it with the given factor.
    * A parameter value of 1.2 will produce a 20% raise, for instance. */
  def raiseSalary(multiplier: Double) = {
    this.monthlySalary = this.monthlySalary * multiplier
  }


  /** Returns a description that contains some basic employee information. */
  def description =
    this.name + " (b. " + this.yearOfBirth + "), salary " + this.workingTime + " * " + this.monthlySalary + " e/month"

}

