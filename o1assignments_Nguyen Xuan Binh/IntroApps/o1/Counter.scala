package o1

// This class is introduced in Chapter 3.1.

class Counter(var value: Int) {

  def advance() = {
    this.value = this.value + 1
  }

  override def toString = "value " + this.value

}
