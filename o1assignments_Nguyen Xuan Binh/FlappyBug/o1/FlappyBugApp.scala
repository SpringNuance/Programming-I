package o1

import constants._
// This class is introduced in Chapter 2.7.

// This class is introduced in Chapter 3.1.



object FlappyBugApp extends App {

  val sky        = rectangle(ViewWidth, ViewHeight,  LightBlue)
  val ground     = rectangle(ViewWidth, GroundDepth, SandyBrown)
  val trunk      = rectangle(30, 250, SaddleBrown)
  val foliage    = circle(200, ForestGreen)
  val tree       = trunk.onto(foliage, TopCenter, Center)
  val rootedTree = tree.onto(ground, BottomCenter, new Pos(ViewWidth / 2, 30))
  val scenery    = sky.place(rootedTree, BottomLeft, BottomLeft)
  val bugPic = Pic("ladybug.png")
  def rockPic(obstacle: Obstacle) = circle(obstacle.radius * 2, Black)

  // INSERT YOUR OWN CODE BELOW.
  val game = new Game
  val gui = new View(game,"FlappyBug"){
    var background = scenery
    def makePic = {
      var Pic: Pic = background
      //for (currentObstacle <- game.obstacles) {
     // Pic = Pic.place(rockPic(currentObstacle),currentObstacle.pos)
      //}
      Pic = game.obstacles.foldLeft(Pic)((pic, current) => pic.place(rockPic(current),current.pos))
      Pic.place(bugPic, game.bug.pos)
    }
    // Another solution
    //def makePic = {
    //
    //  def addObstacle(pic: Pic, obstacle: Obstacle) =
    //    pic.place(rockPic(obstacle), obstacle.pos)
    //
    //  val withObstacles = game.obstacles.foldLeft(this.background)(addObstacle)
    //  withObstacles.place(bugPic, game.bug.pos)
    //}
    override def onTick() = {
       game.timePasses()
       this.background = this.background.shiftLeft(2)
    }
    override def onKeyDown(pressedKey: Key) = {
      if (pressedKey == Key.Space) {
       game.activateBug()
      }
    }
    override def isDone = game.isLost
    }
   gui.start()
}


