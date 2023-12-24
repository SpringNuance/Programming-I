
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.util


import java.lang.reflect.InvocationTargetException

import scala.language.dynamics
import scala.reflect.ClassTag
import scala.swing.Component

import o1.gui.Dialog._
import o1.util._


package object assignments {

  o1.util.smclInit()

  // utils for reflection:
  private[o1] trait ReflectionArgumentTag


  private[o1] type Argument = Any @@ ReflectionArgumentTag

  private[o1] def arg(any: Any) = javify(any).asInstanceOf[Argument]

  private def javify(scalaVal: Any) = scalaVal match {
    // Constructors or the type "new Integer(int)" are deprecated since Java 9:
    // https://docs.oracle.com/javase/9/docs/api/java/lang/Integer.html#Integer-int-
    case int: Int         => java.lang.Integer.valueOf(int)
    case double: Double   => java.lang.Double.valueOf(double)
    case char: Char       => java.lang.Character.valueOf(char)
    case boolean: Boolean => java.lang.Boolean.valueOf(boolean)
    case short: Short     => java.lang.Short.valueOf(short)
    case byte: Byte       => java.lang.Byte.valueOf(byte)
    case float: Float     => java.lang.Float.valueOf(float)
    case long: Long       => java.lang.Long.valueOf(long)
    case otherObject      => otherObject.asInstanceOf[AnyRef]
  }


  // Wrappers for the "dynamic" use of evolving and buggy student classes, implemented using Java reflection.
  // Upgrade to Scala reflection later.

  private[o1] class DynamicClass[Supertype <: AnyRef](private val className: String, private val constructorParameterTypes: Seq[Class[_]]) {

    private val actualClass = Try(Class.forName(className).asInstanceOf[Class[Supertype]]) recoverWith {
      case noSuchClass: ClassNotFoundException => println("The class " + className + " was not available."); throw noSuchClass
      case invalidClass: ClassCastException    => invalidClass.printStackTrace(); throw invalidClass
    }

    val isUsable = this.actualClass.isSuccess

    def instantiate(parameters: Argument*): Supertype =
      this.actualClass match {
        case Failure(_)           =>
          throw new InvalidSignature("The class " + this.className + " is not available and wasn't successfully instantiated.")
        case Success(actualClass) =>
          try {
            val constructor = actualClass.getConstructor(this.constructorParameterTypes: _*)
            constructor.newInstance(parameters: _*)
          } catch {
            case problemWithinImplementation: InvocationTargetException =>
              println("The instantiation of an object of the class " + this.className + " failed to complete.")
              throw problemWithinImplementation
            case instantiationProblem: Exception                        =>
              throw new InvalidSignature("The class " + this.className + " wasn't successfully instantiated.")
          }
      }

  }


  private[o1] class DynamicObject[StaticType](private val wrapped: StaticType) extends Dynamic {

    def applyDynamic[ResultType: ClassTag](methodName: String)(parameters: (Class[_], Argument)*): ResultType = {
      val returnValue = try {
        val method = this.wrapped.getClass.getMethod(methodName, parameters.map( _._1 ): _*)
        method.invoke(this.wrapped, parameters.map( _._2 ): _*)
      } catch {
        case problemWithinImplementation: InvocationTargetException =>
          println("A call to the method " + methodName + " failed to complete.")
          throw problemWithinImplementation
        case otherProblem: Exception =>
          throw new InvalidSignature("The method or variable " + methodName + " was not successfully accessed.")
      }

      val boxings = Map[Class[_], Class[_]](classOf[Boolean] -> classOf[java.lang.Boolean], classOf[Int] -> classOf[java.lang.Integer], classOf[Double] -> classOf[java.lang.Double], classOf[Char] -> classOf[java.lang.Character], classOf[Short] -> classOf[java.lang.Short], classOf[Long] -> classOf[java.lang.Long], classOf[Byte] -> classOf[java.lang.Byte], classOf[Float] -> classOf[java.lang.Float])
      val expectedClassTag = implicitly[ClassTag[ResultType]]
      val expectedClass = expectedClassTag.runtimeClass
      if (expectedClassTag == ClassTag(classOf[Unit]) || expectedClass.isInstance(returnValue) || boxings.get(expectedClass).exists( _.isInstance(returnValue) ))
        returnValue.asInstanceOf[ResultType]
      else
        throw new InvalidSignature("The return value of " + methodName + " was not of the expected type.")
    }

    def selectDynamic[ResultType](methodName: String)(implicit expectedClassTag: ClassTag[ResultType]): ResultType = {
      this.applyDynamic[ResultType](methodName)()
    }

    def get[StaticType] = this.wrapped

  }


  private[o1] class InvalidSignature(val message: String) extends Exception(message)


  private[o1] trait RequestArguments[T <: AnyRef] {
    this: DynamicClass[T] =>

    protected val argumentRequesters: Seq[() => Option[Any]]

    def requestInstance(): Option[T] = {
      val responses: LazyList[Any] = this.argumentRequesters.to(LazyList).map( _() ).takeWhile( _.isDefined ).flatten
      if (responses.size < argumentRequesters.size) None else Some(this.instantiate(responses.map(arg): _*))
    }

  }


  private[o1] def withStudentSolution(owner: Component)(task: => Unit): Unit =
    try {
      task
    } catch {
      case studentCodeProducedException: InvocationTargetException =>
        throw studentCodeProducedException
      case solutionDoesntMeetSpecification: InvalidSignature       =>
        println(solutionDoesntMeetSpecification.message)
        display("A part of the implementation was missing or invalid: " + solutionDoesntMeetSpecification.message, RelativeTo(owner))
    }

}
