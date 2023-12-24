
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.gui


import scala.swing.Dialog._
import scala.swing._

import o1.util._


/** This object provides convenience methods for displaying messages and reading user
  * input via simple Swing dialogs. */
object Dialog {

  /** Displays a textual message in a Swing dialog.
    * @param message   a message to the end user
    * @param position  the location of the dialog onscreen */
  def display(message: String, position: Position): Unit = {
    showMessage(position.javaParent, message, "", Message.Plain, Swing.EmptyIcon)
  }


  /** Asks the user to input a line of text via a Swing dialog and interprets the input with the given
    * functions. Keeps prompting the user with new dialogs until it receives a valid input or the user
    * cancels. An input is invalid if `isOK(convert(input))` returns `false` or fails with an exception.
    * @param prompt        a message that prompts the user for input
    * @param convert       a function applied to the input string to produce the actual return value
    * @param isOK          a function applied to the input string to check if it’s valid
    * @param errorMessage  a prefix added to `prompt` on each request except the first
    * @param position      the location of the dialog(s) onscreen
    * @tparam Input        the type of the user input (converted from a string to this type)
    * @return a valid user input (converted from a string); `None` if the user cancelled */
  def requestInput[Input](prompt: String, convert: String => Input, isOK: Input => Boolean, errorMessage: String, position: Position): Option[Input] = {
    def firstRequest()   = showInput(position.javaParent, prompt,                       prompt, Message.Question, Swing.EmptyIcon, Nil, "")
    def furtherRequest() = showInput(position.javaParent, errorMessage + "\n" + prompt, prompt, Message.Error,    Swing.EmptyIcon, Nil, "")
    def inputs = firstRequest() #:: LazyList.continually( furtherRequest() )
    def isInvalid(inputLine: String) = Try( !isOK(convert(inputLine)) ).getOrElse(true)
    val firstAcceptableInput = inputs.dropWhile( _.exists(isInvalid) ).head
    firstAcceptableInput.map(convert)
  }


  /** Asks the user to input a line of text via a Swing dialog. Keeps prompting the user with new dialogs
    * until it receives a valid input or the user cancels. An input is invalid if `isOK(input.trim)` returns
    * `false` or fails with an exception.
    * @param prompt        a message that prompts the user for input
    * @param isOK          a function applied to the input string to check if it’s valid
    * @param errorMessage  a prefix added to `prompt` on each request except the first
    * @param position      the location of the dialog(s) onscreen
    * @return a valid user input, trimmed for whitespce; `None` if the user cancelled */
  def requestString(prompt: String, isOK: String => Boolean, errorMessage: String, position: Position): Option[String] = {
    requestInput(prompt, _.trim, isOK, errorMessage, position)
  }


  /** Asks the user to input a line of text via a Swing dialog; accepts any string as input.
    * @param prompt        a message that prompts the user for input
    * @param position      the location of the dialog onscreen
    * @return the string entered by the user; `None` if they cancelled */
  def requestAnyLine(prompt: String, position: Position): Option[String] = {
    requestString(prompt, AnythingGoes, "", position)
  }


  /** Asks the user to input a non-empty line of text via a Swing dialog. Keeps prompting the user with new dialogs
    * until it receives a non-empty string as input or the user cancels.
    * @param prompt        a message that prompts the user for input
    * @param errorMessage  a prefix added to `prompt` on each request except the first
    * @param position      the location of the dialog(s) onscreen
    * @return the user input, a non-empty string; `None` if the user cancelled */
  def requestNonEmptyLine(prompt: String, errorMessage: String, position: Position): Option[String] = {
    requestString(prompt, !_.isEmpty, errorMessage, position)
  }

  /** Asks the user to input an integer by typing it in a Swing dialog. Keeps prompting the user
    * with new dialogs until it receives a valid input or the user cancels. An input is invalid
    * if `isOK(inputString.toInt)` returns `false` or fails with an exception.
    * @param prompt        a message that prompts the user for input
    * @param isOK          a function applied to any actual integer input to check if it’s valid
    * @param errorMessage  a prefix added to `prompt` on each request except the first
    * @param position      the location of the dialog(s) onscreen
    * @return the user input, converted from the textual input with `toInt`; `None` if the user cancelled */
  def requestInt(prompt: String, isOK: Int => Boolean, errorMessage: String, position: Position): Option[Int] = {
    requestInput(prompt, _.toInt, isOK, errorMessage, position)
  }

  /** Asks the user to input an integer by typing it in a Swing dialog. Keeps prompting the user
    * with new dialogs until it receives text interpretable as a `Double` or the user cancels.
    * @param prompt        a message that prompts the user for input
    * @param errorMessage  a prefix added to `prompt` on each request except the first
    * @param position      the location of the dialog(s) onscreen
    * @return the user input, converted from the textual input with `toInt`; `None` if the user cancelled */
  def requestAnyInt(prompt: String, errorMessage: String, position: Position): Option[Int] = {
    requestInt(prompt, AnythingGoes, errorMessage, position)
  }


  /** Asks the user to input a decimal number by typing it in a Swing dialog. Keeps prompting the
    * user with new dialogs until it receives a valid input or the user cancels. An input is invalid
    * if `isOK(inputString.toDouble)` returns `false` or fails with an exception.
    * @param prompt        a message that prompts the user for input
    * @param isOK          a function applied to any actual `Double` input to check if it’s valid
    * @param errorMessage  a prefix added to `prompt` on each request except the first
    * @param position      the location of the dialog(s) onscreen
    * @return the user input, converted from the textual input with `toDouble`; `None` if the user cancelled */
  def requestDouble(prompt: String, isOK: Double => Boolean, errorMessage: String, position: Position): Option[Double] = {
    requestInput(prompt, _.toDouble, isOK, errorMessage, position)
  }

  /** Asks the user to input a decimal number by typing it in a Swing dialog. Keeps prompting the
    * user with new dialogs until it receives text interpretable as a `Double` or the user cancels.
    * @param prompt        a message that prompts the user for input
    * @param position      the location of the dialog(s) onscreen
    * @return the user input, converted from the textual input with `toDouble`; `None` if the user cancelled */
  def requestAnyDouble(prompt: String, position: Position): Option[Double] = {
    requestDouble(prompt, AnythingGoes, "", position)
  }


  /** Asks the user to choose among the given options via a Swing dialog.
    * @param  prompt        a message that prompts the user for input
    * @param  options       any objects that the user will choose one of; their `toString`s will show in the input dialog
    * @param  position      the location of the dialog onscreen
    * @return one of the `options` as chosen by the user; `None` if the user cancelled */
  def requestChoice[Choice](prompt: String, options: Seq[Choice], position: Position): Option[Choice] = {
    showInput(position.javaParent, prompt, prompt, Message.Question, Swing.EmptyIcon, options, options.head)
  }


  private val AnythingGoes = (_: Any) => true


  /** Represents the positioning policy of an input dialog. Used with the methods on
    * [[Dialog$ Dialog]]. @see [[RelativeTo]], [[Centered]] */
  sealed trait Position extends Product with Serializable {
    private[gui] def parent: Option[Component]
    private[gui] def javaParent: Component = this.parent.orNull
  }

  /** A positioning policy that places the dialog near another Swing component.
    * An alternative to [[Centered]].
    * @param locator  the Swing component in front of which the Dialog should appear */
  final case class RelativeTo(val locator: Component) extends Position {
    private[gui] def parent = Some(locator)
  }
  /** A companion object with factory methods. */
  case object RelativeTo {
    /** Constructs a [[RelativeTo]] with the given Swing component as its locator. */
    def apply(component: Component): RelativeTo = new RelativeTo(component)
    /** Constructs a [[RelativeTo]] with the first component contained by the given Swing `Frame` as its locator.
      *  If that fails (because there is no content in the frame, returns [[Centered]] instead. */
    def apply(frame: Frame): Position = frame.contents.headOption.map(new RelativeTo(_)).getOrElse(Centered)
  }


  /** A positioning policy that centers the dialog onscreen. An alternative to [[RelativeTo]]. */
  case object Centered extends Position {
    /** Under the `Centered` policy, no other component affects positioning, so this is `None`. */
    private[gui] val parent = None
  }



}

