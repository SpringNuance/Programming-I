package o1.looptest

// This program is associated with Chapter 5.6.

object Task3 extends App {

  def gcContent(dna: String) = {
    var gcCount = 0
    var atCount = 0
    for (base <- dna) {
      if (base == 'G' || base == 'C') {
        gcCount += 1
      } else if (base == 'A' || base == 'T') {
        atCount += 1
      }
    }
    100.0 * gcCount / (atCount + gcCount)
  }


  // The program code below reads in some DNA data from a text file
  // indicated by the user, passes that data to gcContent (defined above),
  // and prints out the return value.

  import scala.io.StdIn._
  import o1.util.readTextFile

  val speciesName = readLine("Enter a species file name (without .mtdna extension): ")
  val fileName = s"mtDNA_examples/$speciesName.mtdna"
  readTextFile(fileName) match {
    case Some(dnaData) =>
      println("The GC content is " + gcContent(dnaData.toUpperCase) + "%.")
    case None =>
      println("Failed to read the file.")
  }

}

