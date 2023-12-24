
package o1.shapes

import scala.math.Pi

// This class is introduced in Chapter 7.2.

class Circle(val radius: Double) extends Shape {

  def area = Pi * this.radius * this.radius
  def perimeter = Pi * 2 * this.radius

}

