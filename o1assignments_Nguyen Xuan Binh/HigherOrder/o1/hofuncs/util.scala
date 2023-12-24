package o1

package object hofuncs {

  private[hofuncs] def launchUnscramblerGUI(scrambled: Pic, solved: Pic) = {
    val gui = new View(scrambled) {
      this.tooltip = "Click to toggle scrambled/revealed pic."
      var showSolved = false

      def makePic = if (this.showSolved) solved else scrambled

      override def onClick(clickPos: Pos) = {
        this.showSolved = !this.showSolved
      }
    }
    gui.start()
  }


}