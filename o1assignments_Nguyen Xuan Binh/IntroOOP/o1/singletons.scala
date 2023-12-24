

// At this stage, you don’t have to worry about the definition immediately below this text.
package object o1 extends o1.util.ShortcutAliases {


  // CHAPTER 2.2: Define the account object below.

  import scala.math._   // math functions will come in handy in this assignment
// Write your code here.
  object account {
    var number = "15903000000776FI00"
    var balance = 0
    def deposit(x:Int) = {
      this.balance = this.balance + max(x,0)
    }
    def withdraw(y:Int) = {
      var actualdecrease = min(this.balance,y)
      this.balance = this.balance - actualdecrease
      actualdecrease
    }
  }
  var Smith = new PhoneBill("Smith")
   Smith.addCall(new PhoneCall(0.99,0.47,7.5))
   Smith.addCall(new PhoneCall(1.2,0.4,30))
   println(Smith.totalPrice)
   println(Smith.breakdown)














  // This example is introduced in Chapter 2.2.

  object employee {

    var name = "Edelweiss Fume"
    val yearOfBirth = 1965
    var monthlySalary = 5000.0
    var workingTime = 1.0

    def ageInYear(year: Int) = year - this.yearOfBirth

    def monthlyCost(multiplier: Double) = this.monthlySalary * this.workingTime * multiplier

    def raiseSalary(multiplier: Double) = {
      this.monthlySalary = this.monthlySalary * multiplier
    }

    def description =
      this.name + " (s. " + this.yearOfBirth + "), salary " + this.workingTime + " * " + this.monthlySalary + " e/month"

  }



  // THE FOLLOWING EXAMPLES ARE INTRODUCED IN CHAPTER 2.1. IT IS NOT NECESSARY TO
  // UNDERSTAND THEIR INTERNAL WORKINGS. SOME OF THE CODE THAT FOLLOWS IS NOT WRITTEN
  // IN A BEGINNER-FRIENDLY STYLE.

  object parrot {

    private var repertoire = List("Yo-Yo Ma and a bottle of rum", "Polly wants a cracker")

    def respond(heardPhrase: String) = {
      def cleanse(phrase: String) = phrase.replaceAll(raw"[^\w åäöÅÄÖ]+", "")
      def toWords(phrase: String) = cleanse(phrase).toLowerCase.split(" ").filter( _.length > 2 )
      val heardWords = toWords(heardPhrase)
      def isSimilar(word: String, another: String) = o1.util.editDistance(word, another, 1) <= 1
      def hasFamiliarSoundingWord(phrase: String) =
        toWords(phrase).exists( familiarWord => heardWords.exists( isSimilar(_, familiarWord)) )
      val knownReply = this.repertoire.find(hasFamiliarSoundingWord)
      val response = knownReply.getOrElse(cleanse(heardPhrase).takeWhile( _ != ' ' ))
      if (response.nonEmpty) response + "!" else ""
    }

    def learnPhrase(newPhrase: String) = {
      this.repertoire = newPhrase :: this.repertoire
    }

  }



  object radio {

    private val presets = Vector(87900, 94000, 94900, 98500)
    private val stations = Map(
      98500  -> ("Radio Helsinki", // with apologies to Claire Boucher:
                 "[39]<<" + "CC  CC  CC  CC  " * 2 + "&[33]<<<" + "CC  CC  CC  CC  " * 2 + "&P:<<                  D   D   D   D &[39]<" + "cc>c<gccc>c<|cc>c<gc>c<gg" * 2 + "/150"),
      94000  -> ("Radio Suomi",    // with apologies to Fucked Up:
                 "[41]>g-g-gg-g-g#-g---f-f--gfd#-d#-----c-c-cc<hb-----hb>ddd--dd#-d#---/180"),
      87900  -> ("YLE 1",          // with apologies to Chairlift:
                 "[17]edc<a>c-d<a-------hageg-ad-c<a----/150"),
      94900  -> ("Radio Rock",     // with apologies to the old Ludwig Van:
                 "[2](<G>G)(<G>G)(<G>G)(<Eb>Eb)---------- (<F>F)(<F>F)(<F>F)(<D>D)-------------- &[2]<<(<G>G)(<G>G)(<G>G)(<Eb>Eb)-----------(<F>F)(<F>F)(<F>F)(<D>D)---------------/180"),
      106200 -> ("Radio Nova",     // with apologies to Fiona Apple:
                 "[33]<<c# c#e# e#d de# e#c# c#e# e#c#&[46]<   g# g#   g# g#   g# g#>[113]  >E---"),
      91900  -> ("YleX",           // with apologies to Heikki Kuula:
                 "[110]>E-E-EDC-<A-G->E-E-EDC-<A->C-<A-G-    >E-E-EGE-D-C-E-E-EGE-D-C-[112]e-d-/150")
    ).withDefaultValue(("just static", ""))

    val notchKHz = 100
    private var frequency = presets(1)

    def frequencyKHz_=(newFrequency: Int) = {
      this.frequency = newFrequency
      o1.play(this.nowPlaying)
    }

    def frequencyKHz = this.frequency

    private def currentStation = this.stations(this.frequencyKHz)._1
    private def nowPlaying = this.stations(this.frequencyKHz)._2

    def tune(steps: Int) = {
      this.frequencyKHz += this.notchKHz * steps
      this.description
    }

    def select(presetNumber: Int) = {
      for (chosen <- this.presets.lift(presetNumber - 1)) {
        this.frequencyKHz = chosen
      }
      this.description
    }

    private def megaHz = this.frequencyKHz / 1000.0
    private def description = f"$megaHz%.1f MHz: $currentStation"

  }


}


