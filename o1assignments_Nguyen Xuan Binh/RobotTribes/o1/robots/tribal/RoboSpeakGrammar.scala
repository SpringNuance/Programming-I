
package o1.robots.tribal

import scala.util.parsing.combinator.JavaTokenParsers
import scala.util.Random


// NOTE TO STUDENTS: You are not required to understand the code in this file.
// It is used by the Tribe class to turn Strings that contain RoboSpeak into
// Line and Instruction objects.

private[tribal] class RoboSpeakGrammar(val program: Tribe) extends JavaTokenParsers {
  import program.Instruction._

  def parse(input: String): Line =
    this.parseAll(this.line, input) match {
      case Success  (parsedCode, _) => parsedCode
      case NoSuccess(problem,    _) => throw new TribeFileException(s"Invalid RoboSpeak. Message from parser:\n$problem")
    }

  def line = labelDef | instruction | directive | empty

  // Instructions:
  def instruction = (
    move        | spin        | uturn       | waitInstr   | switch      |
    talk        | ifempty     | ifnempty    | iffriend    | ifnfriend   |
    ifwall      | ifnwall     | ifrandom    | goto        | callsub     |
    returnInstr | ifeq        | ifneq       | ifgt        | iflt        |
    set         | enemiesnear | friendsnear | foddernear  | fodderleft  |
    shout       | score       | add1        | plus        | minus       |
    enemiesdir  | friendsdir  )                    ^^ { Line(_, None, None) }
  def        move = "move"                         ^^ ( _ => new Move )
  def        spin = "spin"                         ^^ ( _ => new Spin )
  def       uturn = "uturn"                        ^^ ( _ => new UTurn )
  def   waitInstr = "wait"                         ^^ ( _ => new Wait )
  def      switch = "switch"                       ^^ ( _ => new Switch )
  def        talk = "talk"~>param                  ^^ ( line => new Talk(line) )
  def     ifempty = "ifempty"~>param               ^^ ( line => new IfEmpty(line) )
  def    ifnempty = "ifnempty"~>param              ^^ ( line => new IfNEmpty(line) )
  def    iffriend = "iffriend"~>param              ^^ ( line => new IfFriend(line) )
  def   ifnfriend = "ifnfriend"~>param             ^^ ( line => new IfNFriend(line) )
  def      ifwall = "ifwall"~>param                ^^ ( line => new IfWall(line) )
  def     ifnwall = "ifnwall"~>param               ^^ ( line => new IfNWall(line) )
  def    ifrandom = "ifrandom"~>param              ^^ ( line => new IfRandom(line) )
  def        goto = "goto"~>param                  ^^ ( line => new Goto(line) )
  def        ifeq = "ifeq"~>param~param~param      ^^ { case n1~n2~line => new IfEq(n1, n2, line) }
  def       ifneq = "ifneq"~>param~param~param     ^^ { case n1~n2~line => new IfNEq(n1, n2, line) }
  def        ifgt = "ifgt"~>param~param~param      ^^ { case n1~n2~line => new IfGT(n1, n2, line) }
  def        iflt = "iflt"~>param~param~param      ^^ { case n1~n2~line => new IfLT(n1, n2, line) }
  def     callsub = "callsub"~>param               ^^ ( line => new CallSub(line) )
  def returnInstr = "return"                       ^^ ( _ => new Return )
  def         set = "set"~>slotParam~param         ^^ { case slot~value => new Set(slot.left.getOrElse(""), value) }
  def enemiesnear = "enemiesnear"                  ^^ ( _ => new EnemiesNear )
  def friendsnear = "friendsnear"                  ^^ ( _ => new FriendsNear )
  def  foddernear = "foddernear"                   ^^ ( _ => new FodderNear )
  def  fodderleft = "fodderleft"                   ^^ ( _ => new FodderLeft )
  def       score = "score"                        ^^ ( _ => new Score )
  def  enemiesdir = "enemiesdir"~>param            ^^ ( relDir => new EnemiesDir(relDir) )
  def  friendsdir = "friendsdir"~>param            ^^ ( relDir => new FriendsDir(relDir) )
  def       shout = "shout"~>param                 ^^ ( line => new Shout(line) )
  def        add1 = "add1"~>slotParam              ^^ ( slot => new Add1(slot.left.getOrElse("")) )
  def        plus = "plus"~>slotParam~param~param  ^^ { case slot1~value1~value2 => new Plus(slot1.left.getOrElse(""), value1, value2) }
  def       minus = "minus"~>slotParam~param~param ^^ { case slot1~value1~value2 => new Minus(slot1.left.getOrElse(""), value1, value2) }

  // Parameters of instructions:
  def      param = intParam | slotParam | labelParam
  def  slotParam = Tribe.Slots.mkString("|").r ^^ ( Left(_) )
  def labelParam = raw"[\S]+".r                ^^ ( Left(_) )
  def   intParam = wholeNumber                 ^^ ( numString => Right(numString.toInt) )

  // Labels, empty lines, and directives
  def       empty = ""                         ^^ ( _ => Line(new NoCommand, None, None) )
  def    labelDef = raw"[\S]+:".r              ^^ ( labelName => Line(new LabelDefinition, Some(labelName.init), None) )
  def   directive = "pacifist"                 ^^ ( directiveName => Line(new Directive, None, Some(directiveName)) )

}


private[tribal] case class Line(val instruction: Instruction, val label: Option[String], val directive: Option[String])
