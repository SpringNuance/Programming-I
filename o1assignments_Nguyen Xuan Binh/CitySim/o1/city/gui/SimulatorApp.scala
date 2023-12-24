
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.city.gui

import o1.util._
import o1.city._
import o1.gui._
import o1.grid.GridPos
import o1.gui.mutable._
import o1.gui.layout._
import scala.swing._
import scala.swing.event.ButtonClicked


/** The singleton object `SimulatorApp` represents an application that runs
  * simulations based on Schelling’s model of emergent social segregation. The
  * application appears in a GUI window and provides controls for adjusting
  * simulation settings.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this object works
  * or can be used, apart from the fact that you can use this file to start the program.''' */
object SimulatorApp extends App {

  val simulator = new Simulator
  val supportsMultipleDemographics = isImplemented(simulator.residents)

  private val AllDemographics = ColorScheme.DemographicColors.map( new Occupied(_) )
  private def residentsOfTwoDGs = AllDemographics.take(2).map( dg => (dg, simulator.findDemographic(dg)) )
  private def residents = if (supportsMultipleDemographics) simulator.residents else residentsOfTwoDGs

  val PixelsPerSquare = 20
  val SizeInSquares = 40
  val WindowSizeInPixels = 600
  private val Background = Pic.emptyCanvas(WindowSizeInPixels, WindowSizeInPixels)
  val gui = new SimpleFrame("City Simulation") with O1WindowDefaults with Escapable with TerminatesOnClose {


  private val screen = new ViewComponent(simulator) with View.HasPauseToggle {

      override def startsPaused = true

      def makePic = {
        val squareInPixels = WindowSizeInPixels / simulator.squaresPerSide
        def pic(dg: Demographic) = Pic.square(squareInPixels, ColorScheme(dg)).anchorAt(Anchor.TopLeft)

        def toPixelPos(address: GridPos) = Pos(address.x * squareInPixels, address.y * squareInPixels)

        def placePopulation(bg: Pic, population: (Demographic, Vector[GridPos])) =
          bg.placeCopies(pic(population._1), population._2.map(toPixelPos))

        residents.foldLeft(Background)(placePopulation)
      }

      override def onTick() = {
        stepForward()
      }
    }

    val stepButton  = new Button("Single Step") { preferredSize = preferredSize.withWidth(100) }
    val runButton   = new Button("Run")         { preferredSize = preferredSize.withWidth(100) }
    val startButton = new Button("Start Over")  { preferredSize = preferredSize.withWidth(100) }
    val buttonRow   = new FlowPanel(stepButton, runButton, startButton)
    this.listenTo(stepButton, runButton, startButton)
    this.reactions += {
      case ButtonClicked(`startButton`) => startNewSimulation()
      case ButtonClicked(`stepButton`)  => stepForward(); screen.refresh()
      case ButtonClicked(`runButton`)   => togglePause()
    }


    private def stepForward() = {
      simulator.moveResidents()
      satisfactionPercentLabel.text = satisfactionText
    }

    private def startNewSimulation() = {
      simulator.startNew(sizeSlider.value, 100 - vacancySlider.value,
                         AllDemographics.take(populationCountSlider.value),
                         proportionSlider.value, similaritySlider.value)
      satisfactionPercentLabel.text = satisfactionText
      screen.refresh()
    }



    trait RestartsSim extends Setting {
      override def onAdjust() = {
        super.onAdjust()
        startNewSimulation()
      }
    }

    def satisfactionText = "Satisfied: " + (simulator.satisfactionLevel * 100).formatted("%.1f") + "%"
    val satisfactionPercentLabel = new Label
    val similaritySlider               = new Setting("Threshold:",              0,  70, 100, _.toString + "% similarity desired." ) with RestartsSim
    val populationCountSlider: Setting = new Setting("Number of demographics:", 2,  2,   10, n => s"$n populations.")      with RestartsSim {
      this.enabled = supportsMultipleDemographics
      override def onAdjust() = { super.onAdjust(); proportionSlider.updateLabel();  }

    }
    def nonRedDescription: String = if (populationCountSlider.value > 2) "others" else "Blue"
    val proportionSlider     = new Setting("Relative sizes:", 0,  50,  100, ratio   => s"$ratio% Red, ${100 - ratio}% $nonRedDescription.") with RestartsSim
    val vacancySlider        = new Setting("Vacancy:",        0,  10,  100, vacancy => s"$vacancy% vacant locations." ) with RestartsSim
    val sizeSlider           = new Setting("Grid size:",      1,  20,   70, side    => s"$side by $side squares.") with RestartsSim
    val speedSlider: Setting = new Setting("Speed:",        -85,   0,   85, value   => "Trying for ~" + tickRate(value).formatted("%.1f") + " steps/s.") {
      override def onAdjust() = { updateSpeed() }
    }

    private def tickRate(sliderValue: Int) = math.pow(10, sliderValue / 100.0)
    private def updateSpeed() = {
      screen.adjustSpeed(tickRate(this.speedSlider.value))
    }

    contents = new EasyPanel {
      placeN(screen,                   (0, 0), TwoWide, FillBoth(1, 1),    (2, 2, 2, 2))
      placeN(satisfactionPercentLabel, (0, 1), TwoWide, Slight,            NoBorder)
      placeN(buttonRow,                (0, 2), TwoWide, Slight,            NoBorder)
      placeNW(similaritySlider,        (0, 3), OneSlot, FillHorizontal(1), NoBorder)
      placeNW(vacancySlider,           (0, 4), OneSlot, FillHorizontal(1), NoBorder)
      placeNW(speedSlider,             (0, 5), OneSlot, FillHorizontal(1), NoBorder)
      placeNE(populationCountSlider,   (1, 3), OneSlot, FillHorizontal(1), NoBorder)
      placeNE(proportionSlider,        (1, 4), OneSlot, FillHorizontal(1), NoBorder)
      placeNE(sizeSlider,              (1, 5), OneSlot, FillHorizontal(1), NoBorder)
    }

    this.updateSpeed()
    startNewSimulation()
    screen.start()
    this.pack()

    private def togglePause() = {
      screen.togglePause()
      runButton.text = if (screen.isPaused) "Run" else "Pause"
      stepButton.enabled = screen.isPaused
    }

    override def closeOperation() =  {
      screen.stop()
      super.closeOperation()
    }

  }
  gui.visible = true
}

