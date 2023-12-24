package o1.robots.tribal

import scala.util.Random

/** The trait `Instruction` represents instructions within RoboSpeak programs.
  * The program of each [[Tribe]] consists of instruction objects; a [[TribalBot]]
  * is able to execute sequences of instructions.
  *
  * A single `Instruction` object represents an instruction for a tribal robot to
  * do something. By way of example, here are two RoboSpeak instructions in textual
  * form, each of which can also be represented by an `Instruction` object:
  * {{{
  * turn
  * goto 1
  * }}}
  *
  * See the documentation of class [[Tribe]] for more details and examples of RoboSpeak.
  *
  * There are various kinds of instruction objects representing the various kinds of
  * instructions that can appear in RoboSpeak programs. However, all instructions share
  * the common interface described by this trait. Through this interface, instructions
  * can be examined and executed. Each instruction object, irrespective of what is does,
  * provides the following:
  *
  *  - Methods for determining basic information about the instruction:
  *    Does executing the instruction end a tribal robot's turn? On which
  *    line in a RoboSpeak program does the instruction appear?
  *
  *  - A method for executing the instruction and determining which instruction
  *    to execute next.
  *
  * An instruction object is immutable.
  *
  * @see [[Tribe]]
  * @see [[TribalBot]] */
trait Instruction {


  /** Causes the given tribal bot to execute the instruction. What happens as a result
    * depends on the instruction. In the case of an action instruction such as ''move'' or
    * ''hack'', the tribal bot does something visible. In the case of a logic instruction
    * such as ''ifempty'', ''goto'' or ''callsub'', nothing visible happens immediately,
    * but the bot does something which affects its future behavior.
    *
    * This method also determines the next instruction that is to be executed by the
    * acting robot next after this instruction is done with. The next instruction may be
    * either the instruction on the following line of the RoboSpeak program (e.g., after a
    * ''move'' instruction, or after an ''ifempty'' when the square ahead is not empty),
    * or it may be an instruction somewhere else in the program (e.g., after a ''goto'', or
    * after an ''ifempty'' when the square ahead is empty).
    *
    * @param actor  a tribal bot whose turn it is and who is to execute the instruction
    * @return the next instruction that is to be executed by the actor after this one
    *         (possibly only during a future turn) */
  def execute(actor: TribalBot): Instruction


  /** Determines if this instruction is of a type which ends the acting robot's turn
    * when executed. (''move'' does, for instance, while ''ifempty'' does not.) */
  def endsTurn: Boolean


  /** Returns the number of the line (1 or higher) that the instruction appears on
    * the RoboSpeak program that it is a part of. */
  def lineNumber: Int


}

