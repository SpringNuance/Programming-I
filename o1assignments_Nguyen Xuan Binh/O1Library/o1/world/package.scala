package o1


import scala.language.implicitConversions

import smcl.modeling.d2.{Bounds => SMCLBounds, Pos => SMCLPos}


/** This package contains tools for locations and movement in two-dimensional space.
  *
  * The tools in this package have aliases in the top-level package [[o1]], so they are accessible
  * to students simply via `import o1._`.
  *
  * The subpackage [[o1.world.objects]] contains additional tools for representing
  * entities that reside within two-dimensional spaces. */
package object world {

  o1.util.smclInit()

  private[o1] implicit class ConvertableSMCLBounds(val self: SMCLBounds) extends AnyVal {
    def toO1Bounds: world.Bounds =  new world.Bounds(self.upperLeftCorner.xInPixels, self.upperLeftCorner.yInPixels,
                                                     self.width.inPixels, self.height.inPixels)
  }


  private[o1] implicit class ConvertableSMCLPos(val self: SMCLPos) extends AnyVal {
    def toO1Pos: world.Pos = new world.Pos(self.xInPixels, self.yInPixels)
  }


}
