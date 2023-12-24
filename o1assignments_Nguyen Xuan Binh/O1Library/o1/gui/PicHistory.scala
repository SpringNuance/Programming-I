package o1.gui

////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

import scala.collection.mutable.ListBuffer
import o1.gui.PicHistory._
import o1.util._

private[o1] object PicHistory {

  private val MinimumLength: Int = 1 // Has to be at least one operation (the creation)

  // The default total length of a new PicHistory; includes creation and processing.
  private val DefaultLength: Int = 10


  private[gui] def apply(creationOp: op.Create): PicHistory =
    apply(creationOp, DefaultLength)

  private[gui] def apply(creationOp: op.Create, maximumHistoryLength: Int): PicHistory =
    apply(creationOp, List(), maximumHistoryLength)

  private[gui] def apply(creationOp: op.Create, processingOps: List[op.Process], maximumHistoryLength: Int): PicHistory =
    new PicHistory(creationOp, processingOps, maximumHistoryLength atLeast MinimumLength)


  private[o1] object op {

    sealed abstract class AbstractOperation private[gui](private val method: String, val metadata: Option[Map[String, Boolean]]) {

      val calledMethod: String = {
        val trimmed = method.trim
        if (trimmed.isEmpty) throw new IllegalArgumentException("Method name cannot be empty or contain only whitespace")
        trimmed
      }

      override def toString = {
        val b = new StringBuilder
        b ++= calledMethod
        if (metadata.isDefined) {
          b ++= " (" ++= metadata.toString ++= ")"
        }
        b.toString()
      }

      def booleanValue(key: String, defaultValue: Boolean): Boolean =
        this.metadata.flatMap( _.get(key) ).getOrElse(defaultValue)

    }

    final case class Create private[gui](private val method: String, simpleDescription: String, override val metadata: Option[Map[String, Boolean]] = None)
      extends AbstractOperation(method, metadata) {
    }


    sealed abstract class Process private[gui](private val method: String, metadata: Option[Map[String, Boolean]])
        extends AbstractOperation(method, metadata) {
      // Whether this operation is expected to represent color or geometry transformation
      val isTransformation: Boolean = false
    }


    final case class AdjustViewport private[gui](private val method: String, override val metadata: Option[Map[String, Boolean]] = None)
        extends Process(method, metadata) {
    }
    private[gui] object AdjustViewport {
      def apply(method: String) = new AdjustViewport(method = method)
    }


    final case class Transform private[gui](private val method: String, override val metadata: Option[Map[String, Boolean]] = None)
        extends Process(method, metadata) {
      override val isTransformation: Boolean = true
    }
    private[gui] object Transform {
      def apply(method: String) = new Transform(method = method)
    }


    final case class Miscellaneous private[gui](private val method: String, override val metadata: Option[Map[String, Boolean]] = None)
        extends Process(method, metadata)
    private[gui] object Miscellaneous {
      def apply(method: String) = new Miscellaneous(method = method)
    }
  }

}



private[o1] class PicHistory private(val creationOp: op.Create, val processingOps: List[op.Process], val maximumLength: Int) {

  override def toString = methodList.mkString(", ")

  private[gui] def copy(newCreationOp: op.Create = creationOp, newProcessingOpList: List[op.Process] = processingOps): PicHistory =
    PicHistory(newCreationOp, newProcessingOpList, maximumLength)


  private[gui] def setHistoryLength(newMaximumLength: Int): PicHistory = {
    val length = newMaximumLength atLeast PicHistory.MinimumLength
    val newOpList = shortenedProcessingOpList(targetLength = length, forAddition = false)
    PicHistory(this.creationOp, newOpList, length)
  }


  private[gui] def ::(processingOp: op.Process): PicHistory = {
    if (maximumLength <= PicHistory.MinimumLength)
      return this.copy(newProcessingOpList = List())
    val normalizedOpList = shortenedProcessingOpList(targetLength = maximumLength, forAddition = true)
    val newOpList = processingOp :: normalizedOpList
    this.copy(newProcessingOpList = newOpList)
  }

  private def shortenedProcessingOpList(targetLength: Int, forAddition: Boolean): List[op.Process] = {
    val oneForCreationOp = 1
    val possiblyOneForAddition = if (forAddition) 1 else 0
    val numberOfOpsToDrop = this.processingOps.length - (targetLength - oneForCreationOp - possiblyOneForAddition)
    this.processingOps.dropRight(numberOfOpsToDrop)
  }


  def methodList: List[String] = {
    val b = ListBuffer[String]()
    this.processingOps foreach {
      b += _.calledMethod
    }
    b += creationOp.calledMethod
    b.toList
  }

  private[gui] def containsTransformations: Boolean = this.processingOps.exists( _.isTransformation )

}
