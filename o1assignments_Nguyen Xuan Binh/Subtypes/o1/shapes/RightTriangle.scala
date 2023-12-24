package o1.shapes
import scala.math.sqrt

class RightTriangle(val base: Double, val height: Double) extends Shape {

   def area = base*height/2
   def hypotenuse = sqrt(base*base + height*height)
   def perimeter = base + height + hypotenuse
}