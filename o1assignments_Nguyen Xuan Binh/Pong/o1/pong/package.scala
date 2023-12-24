package o1

package object pong {

  val BallRadius = 50

  object Screen extends HasEdges {
    val width  = 1000.0
    val height = 500.0
    val pos    = Pos(width / 2, height / 2)
  }

  val SideMargin    = 40
  val WallThickness = 20

  object Court extends HasEdges {
    val width  = Screen.width  - 2 * SideMargin
    val height = Screen.height - 2 * WallThickness
    val pos    = Pos(SideMargin, WallThickness)
    override val anchor = TopLeft
  }

  private val InitialPaddleY   = Screen.center.y - (Paddles.Size / 2)
  val InitialLeftPaddleCenter  = Pos(Court.left  + Paddles.Thickness / 2, InitialPaddleY)
  val InitialRightPaddleCenter = Pos(Court.right - Paddles.Thickness / 2, InitialPaddleY)

  object Paddles {
    val Size      = 120
    val Thickness = 25
    val Speed     = 8.0
    val MaxLaunchSpeed     = 13
    val InitialLaunchSpeed = 6
    val MinLaunchSpeed     = 5
    val SmashThreshold     = 0.1
    val SmashSpeed         = MaxLaunchSpeed + 1
  }

  val WayOutMargin = 150

  private val MaxLaunchSkew  = 66

  def launchDirection(generalDirection: Direction, skewPercent: Double) = {
    val skewDegrees   = -skewPercent * MaxLaunchSkew
    val launchDegrees = if (generalDirection.isRightward) skewDegrees else 180 - skewDegrees
    Direction.fromDegrees(launchDegrees)
  }

  val TicksPerSecond = 90
}

