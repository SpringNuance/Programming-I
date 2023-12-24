package o1.hofuncs

// This example is introduced in Chapter 6.1.

object Example6 extends App {

  def multiplicationTableEntry(row: Int, column: Int) = (row + 1) * (column + 1)

  val vectorOfRows = Vector.tabulate(10, 10)(multiplicationTableEntry)
  println(vectorOfRows)

  for (numbersOnRow <- vectorOfRows) {
    println(numbersOnRow.mkString("\t"))
  }

}

