package o1

// This class is gradually developed between Chapters 2.4 and 3.4.

class Odds(val wont: Int, val will: Int) {

  def probability = 1.0 * this.will / (this.wont + this.will)
  override def toString = this.wont + "/" + this.will
  def fractional = s"$wont/$will"
  def moneyline = {
    if (this.probability <= 0.5) (this.wont*100)/this.will else (this.will* -100)/this.wont
  }
  def decimal = (wont+will).toDouble/will.toDouble
  def winnings(bet:Double) = decimal*bet
  def isLikely() = {
    var x = this.will > this.wont
        x
  }
  def isLikelierThan(another:Odds) = {
     var x = this.probability > another.probability
         x
  }
  def not = {
    var x = new Odds(this.will, this.wont)
        x
  }
  def both(odd1: Odds) = {
     var y1 = this.wont * odd1.wont + this.wont * odd1.will + this.will * odd1.wont
     var y2 = this.will * odd1.will
     var y3 = new Odds(y1,y2)
         y3
  }
  def either(odd2: Odds) = {
     var z1 = this.wont * odd2.wont
     var z2 = this.wont * odd2.will + this.will * odd2.wont + this.will * odd2.will
     var z3 = new Odds(z1,z2)
         z3
  }
  // TODO: other methods missing

}