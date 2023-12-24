package o1.items
import scala.collection.mutable.Buffer
// TODO: complete as instructed in Chapter 7.3.

class Container(names: String) extends Item(names){
  private var theItems = Buffer[Item]()
  override def toString = s"$names containing ${theItems.size} item(s)"

  def addContent(newContent: Item): Unit = {
    theItems += newContent
  }
}