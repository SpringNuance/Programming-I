package o1
object misc { // These definitions at the top are discussed in Chapter 5.2.

  // Various small assignments across several chapters will ask you to define functions in this file.
  // Please enter your code below this comment.
  def together(melody: Vector[String], tempo:Int) = {
    var newMelody = melody.mkString("&") + s"/$tempo"
    newMelody
  }

  def tempo(melody: String) = {
    if (melody.indexOf("/") < 0) 120 else melody.substring(melody.indexOf("/")+1).toInt
  }

  def toMinsAndSecs(num: Int) ={
    var mins = num/60
    var secs = num%60
    (mins,secs)
  }

  def isInOrder(pairOfNumbers: (Int, Int)) = pairOfNumbers._1 <= pairOfNumbers._2    // This example function is introduced in Chapter 8.4. You can ignore it until then.

  def isAscending(list: Vector[Int]) = list.zip(list.tail).forall(isInOrder(_))

}
