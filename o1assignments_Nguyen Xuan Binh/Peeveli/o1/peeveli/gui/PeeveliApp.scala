package o1.peeveli.gui

import scala.swing._
import o1.peeveli.GameState
import o1.util.ConvenientSeq
import o1.gui.Dialog._
import o1.gui.layout._
import o1.gui.O1AppDefaults
import o1.util.readFileLines
import java.awt.Font
import java.awt.font.TextAttribute
import scala.swing.Swing._
import java.awt.Color
import scala.swing.event._

////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////


/** The singleton object `PeeveliApp` serves as an entry point for the Peeveli game. You can
  * run it to start up the user interface.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this object works
  * or can be used, apart from the fact that you can use this file to start the program.''' */
object PeeveliApp extends SimpleSwingApplication with O1AppDefaults {

  def top: MainFrame = new MainFrame {

    type Vocabulary = Map[Int, Vector[String]]

    this.title = "Hangman"
    this.location = new Point(100, 100)
    this.resizable = false

    var debugPrintoutOn = false
    val vocabularies = Map("Finnish" -> "vocabularies/kotus/suomen_sanoja.txt",
                           "test: AAVA, AIKA, AIVO, ALLI, KULU, RIMA, RÄKÄ, SOLA, SUMU, TAAS" -> "vocabularies/kotus/testisanat.txt",
                           "English" -> "vocabularies/dict/english_words.txt",
                           "test: ALSO, AREA, AUNT, GURU, HAWK, IDEA, JUJU, PLAY, TUNA, ZERO" -> "vocabularies/dict/test_words.txt")
    val DefaultVocabulary = "Finnish"

    var vocabulary = loadVocabulary(DefaultVocabulary)
    var currentState = new GameState(5, 10, vocabulary(10))

    def loadVocabulary(vocabularyName: String) = {
      val words = readFileLines(vocabularies(vocabularyName)).getOrElse(throw new RuntimeException("Failed to load vocabulary file."))
      words.groupBy( _.length )
    }


    def newState(vocabulary: Vocabulary) = {
      for {
        length <- if (vocabulary.size > 1) requestInt("How many letters per word (longer words are easier)?", vocabulary.contains(_), "I don't know any words with that number of letters.", RelativeTo(this))
                  else                     vocabulary.keys.headOption
        misses <- requestInt("How many missed guesses are allowed (the usual number is five)?", _ >= 0, "Please enter a non-negative integer.", RelativeTo(this))
      } yield new GameState(misses, length, vocabulary(length))
    }


    val targetWord = new Label {
      val attrib = new java.util.HashMap[TextAttribute, Double]
      attrib.put(TextAttribute.TRACKING, 0.5)
      this.font = new Font("Monospaced", Font.PLAIN, 25).deriveFont(attrib)
      border = EmptyBorder(25, 25, 25, 25)
    }

    val prompt = new Label
    val feedback = new Label
    val previousGuesses = new TextField(50) {
      this.editable = false
      this.focusable = false
      this.border = EmptyBorder
    }
    val newGame = Action("New Game...") {
      for (game <- newState(vocabulary)) {
        currentState = game
        initializeView(game)
      }
    }
    val exitGame = Action("Exit") { this.dispose() }


    val newGameButton = new Button(newGame)
    val gallows = new PeeveliDisplay
    this.defaultButton = newGameButton

    this.contents = new EasyPanel {
      this.border = EmptyBorder(20, 20, 20, 20)
      placeC(targetWord,                  (0, 0), OneSlot, Slight, (0, 0, 0, 0))
      placeW(prompt,                      (0, 1), OneSlot, Slight, (2, 2, 2, 2))
      placeNW(feedback,                   (0, 2), OneSlot, Slight, (0, 0, 0, 0))
      placeNW(new Label("Your guesses:"), (0, 3), OneSlot, Slight, (0, 0, 0, 0))
      placeNW(previousGuesses,            (0, 4), OneSlot, Slight, (0, 0, 0, 0))
      placeC(Swing.VStrut(140),           (0, 5), TwoHigh, Slight, (0, 0, 0, 0))
      placeC(gallows,                     (0, 5), OneSlot, Slight, (20, 0, 0, 0))
      placeC(newGameButton,               (0, 6), OneSlot, Slight, (20, 0, 0, 0))

      focusable = true
      listenTo(this.keys)
      reactions += {
        case KeyPressed(_, Key.Escape, _, _) =>
          exitGame()
        case KeyTyped(_, char, _, _) =>
          if (isRunning) {
            makeAGuess(char)
          }
      }
    }

    this.menuBar = new MenuBar {
      contents += new Menu("Game") {
        contents += new MenuItem(newGame)
        contents += new MenuItem(exitGame)
      }
      contents += new Menu("Vocabulary") {
        val vocabularyItems = for (vocabName <- vocabularies.keys.toSeq.sorted) yield {
          new RadioMenuItem(vocabName) {
            selected = (vocabName == DefaultVocabulary)
            action = Action(vocabName) {
              vocabulary = loadVocabulary(vocabName)
              newGame()
            }
          }
        }
        val vocabularyryhma = new ButtonGroup(vocabularyItems: _*)
        contents ++= vocabularyryhma.buttons
      }
      contents += new Menu("Testing") {
        contents += new CheckMenuItem("") {
          action = Action("Print word lists in the console") {
            debugPrintoutOn = this.selected
            describe(currentState)
          }
        }
      }
    }



    private def describe(state: GameState) = {
      if (this.debugPrintoutOn) {
        println("There are " + state.numberOfSolutions + " words that fit: " + state.possibleSolutions.mkString(", ").take(5000))
      }
    }

    this.initializeView(currentState)

    def initializeView(initialState: GameState): Unit = {
      this.prompt.text = "Which " + initialState.wordLength + "-letter word am I thinking about? Guess a letter with your keyboard."
      this.feedback.text = "Any correct guesses will appear above."
      this.feedback.foreground = Color.black
      this.previousGuesses.text = "(no guesses yet)"
      this.targetWord.text = initialState.visibleWord
      this.gallows.updateTo(initialState)
      this.newGameButton.visible = !this.isRunning
      this.describe(this.currentState)
      this.pack()
    }


    def isRunning = !this.currentState.isLost && !this.currentState.isWon


    def generateFeedback(oldState: GameState, newState: GameState) = {
      val goodColor = Color.green.darker.darker.darker
      val badColor = Color.red.darker
      val lives = newState.missesAllowed
      if (newState.isWon) {
        this.feedback.text = "You got it! The word is: " + correctSolution() + "!"
        this.feedback.foreground = goodColor
      } else if (lives >= oldState.missesAllowed) {
        this.feedback.text = "It's a hit! See above for the updated word."
        this.feedback.foreground = goodColor
      } else if (newState.isLost) {
        this.feedback.text = "You lost. The word I was thinking about is: " + correctSolution() + "."
        this.feedback.foreground = badColor
      } else if (this.gallows.visible) {
        val threat = "You'll soon hang. "
        val report = if (lives > 0) "Only " + newState.missesAllowed + " more misses allowed." else "The next miss will be your last."
        this.feedback.text = threat + report
        this.feedback.foreground = badColor
      } else {
        this.feedback.text = "It's a miss! You are still allowed to miss " + newState.missesAllowed + " times."
        this.feedback.foreground = badColor
      }
    }


    def correctSolution() = {
      import scala.util.Random
      val credibles = this.currentState.possibleSolutions
      credibles.randomElement()
    }


    def makeAGuess(guess: Char): Unit = {
      val oldState = this.currentState
      this.currentState = this.currentState.guessLetter(guess)
      this.previousGuesses.text = this.currentState.previousGuesses.mkString(" ")
      this.targetWord.text = this.currentState.visibleWord
      this.gallows.updateTo(this.currentState)
      this.generateFeedback(oldState, this.currentState)
      this.describe(this.currentState)
      this.newGameButton.visible = !this.isRunning
      this.pack()
    }


    private class PeeveliDisplay extends Label(" ") {

      this.border = EmptyBorder(10, 10, 10, 10)

      this.font = new Font("Monospaced", Font.BOLD, 40)
      this.foreground = Color.red
      this.background = Color.black
      this.opaque = true

      val wholeText = "PEEVELI!"
      val revealOrder = "!PLVIE"

      def updateTo(state: GameState) = {
        val charsRevealed = revealOrder.length - state.missesAllowed - 1
        this.visible = charsRevealed > 0
        val visibleNow = this.revealOrder.take(charsRevealed)
        this.text = wholeText.map( char => if (visibleNow.contains(char)) char else ' ' )
      }

    }

  }


}


