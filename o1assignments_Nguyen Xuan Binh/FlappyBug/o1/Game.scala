package o1

import constants._

class Game {

  // Your code goes here. Please add only what is requested by the ebook. To avoid
  // confusing our automatic assessment system, please don't invent additions of your own
  // here (at least not until you're done with the ebook’s official FlappyBug assignments).
   val bug = new Bug(Pos(100,40))
   val obstacles = Vector(new Obstacle(70), new Obstacle(30), new Obstacle(20))
   def timePasses() = {
     this.bug.fall()
     obstacles.foreach(_.approach())
   }

   def activateBug() = {
     this.bug.flap(15)
   }
   def isLost = {
     //var touches = false
     //for (currentObstacle <- obstacles){
     //  if (currentObstacle.touches(this.bug)) touches = true
     //}
     if (obstacles.exists(_.touches(this.bug)) || !this.bug.isInBounds) true else false
   }
}
