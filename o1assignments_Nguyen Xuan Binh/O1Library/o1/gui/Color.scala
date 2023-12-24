package o1.gui


import smcl.colors.rgb.{Color => SMCLColor}

import o1.util.ConvenientDouble


/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
//////
//////        COLOR COMPANION OBJECT
//////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////




/** This companion object of [[Color class `Color`]] provides methods for creating new `Color` objects.
  * There is also a small selection of related utility methods and constants.
  *
  * For many constants of type [[Color]] that represent different preset colors, see [[o1.gui.colors]].
  *
  * This object has an alias in the top-level package [[o1]], so it’s accessible to students simply
  * via `import o1._`. */
object Color {

  /** The minimum value of a [[Color]]’s RGB and opacity components.
    *  (They range between 0 and 255, so this equals zero. */
  val Min: Int = 0

  /** The maximum value of a [[Color]]’s RGB and opacity components.
    *  (They range between 0 and 255, so this equals 255. */
  val Max: Int = 255

  /** Creates a new fully opaque [[Color]] with the given RGB components.
    * @param red   the amount of red   in the color; values outside the 0--255 range are clamped to fit it
    * @param green the amount of green in the color; values outside the 0--255 range are clamped to fit it
    * @param blue  the amount of blue  in the color; values outside the 0--255 range are clamped to fit it
    * @return the color */
  def apply(red: Double, green: Double, blue: Double): Color = apply(red, green, blue, opacity = Max)


  /** Creates a new [[Color]] with the given RGB components and opacity.
    * @param red       the amount of red   in the color; values outside the 0--255 range are clamped to fit it
    * @param green     the amount of green in the color; values outside the 0--255 range are clamped to fit it
    * @param blue      the amount of blue  in the color; values outside the 0--255 range are clamped to fit it
    * @param opacity   the opacity of the color; values outside the 0--255 range are clamped to fit it
    * @return the color */
  def apply(red: Double, green: Double, blue: Double, opacity: Double): Color =
    apply(red, green, blue, opacity, name = "")


  private[gui] def apply(red: Double, green: Double, blue: Double, opacity: Double, name: String): Color = {
    val redInt = clampComponent(red).toInt
    val greenInt = clampComponent(green).toInt
    val blueInt = clampComponent(blue).toInt
    val opacityInt = clampComponent(opacity).toInt
    val nameOption = Option[String](name).map( _.trim ).filterNot( _.isEmpty )
    val smclColor = nameOption match {
      case Some(name) => SMCLColor(redInt, greenInt, blueInt, opacityInt, name)
      case nameless   => SMCLColor(redInt, greenInt, blueInt, opacityInt)
    }
    new Color(redInt, greenInt, blueInt, opacityInt, nameOption, smclColor)
  }

  private[gui] def apply(smclColor: SMCLColor): Color = apply(smclColor, name = None)

  private[gui] def apply(smclColor: SMCLColor, name: String): Color =
    apply(smclColor, Option(name))

  private def apply( smclColor: SMCLColor, name: Option[String]): Color = {
    def defaultName = if (smclColor.isPreset) Some(smclColor.toString) else smclColor.canonicalName
    val effectiveName = name.map( _.trim ).filterNot( _.isEmpty ).orElse(defaultName)
    new Color(smclColor.red, smclColor.green, smclColor.blue, smclColor.opacity, effectiveName, smclColor)
  }

  /** This method deconstructs a [[Color]] to its components and returns them as a tuple.
    * Its presence here in the companion object enables us to write things like:
    * {{{
    * myColor match {
    *   case Color(r, g, b, 255)          => "opaque color: " + r + "," + g + "," + b
    *   case Color(r, _, _, _) if r > 200 => "high in red"
    *   case _                            => "other color"
    * }
    * }}}
    * @param color  any [[Color]]
    * @return a `Some` that contains a tuple with all the RGB components of the given color and its opacity
    */
  def unapply(color: Color): Option[(Int, Int, Int, Int)] = Some((color.red, color.green, color.blue, color.opacity))



  /** Creates a [[Color]] specified in terms of the HSI (hue--saturation--intensity) color scheme.
    * @param hue         the hue (“main observable color”) component of color, in degrees around the color wheel (0--360)
    * @param saturation  the saturation (“richness”) component of the color; values outside the 0--255 range are clamped to fit it
    * @param value       the intensity (“brightness”) component of the color; values outside the 0--255 range are clamped to fit it
    * @return the color (represented internally as RGB nonetheless) */
  def fromHSI(hue: Double, saturation: Double, value: Double, opacity: Double = Max): Color = {
    val normSat = saturation.clamp(0, 1)
    val normIntensity = clampComponent(value)
    val normOpacity = clampComponent(opacity).toInt
    SMCLColor.fromHSI(hue, normSat, normIntensity, normOpacity).toO1Color
  }

  private def clampComponent(n: Double) = n.clamp(Min, Max)

}



/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
//////
//////        CLASS COLOR
//////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////





/** Each instance of this class represents a color. The class uses the RGB color scheme: each color
  * is a combination of a red, green, and blue components; there’s also a fourth component of opacity.
  * `Color` objects are immutable.
  *
  * You don’t instantiate `Color` directly; instead, you create `Colors`s with the methods on the
  * [[Color$ `Color` companion object]] (e.g. `Color(200, 150, 255)`) or use one of the named color
  * constants in [[o1.gui.colors]]. There are also a few methods in this class that returns new
  * [[Color]] objects defined in terms of existing ones (e.g., [[lighter]], [[edit]]).
  *
  * This class has an alias in the top-level package [[o1]], so it’s accessible to students simply
  * via `import o1._`.
  *
  * @param red     the amount of red   in the color, between [[Color.Min]] and [[Color.Max]]; that is, 0--255
  * @param green   the amount of green in the color, between [[Color.Min]] and [[Color.Max]]; that is, 0--255
  * @param blue    the amount of blue  in the color, between [[Color.Min]] and [[Color.Max]]; that is, 0--255
  * @param opacity the opacity of the color, between [[Color.Min]] and [[Color.Max]]; that is, 0--255 */
final class Color private(val red: Int, val green: Int, val blue: Int, val opacity: Int, private val presetName: Option[String], private[gui] val smclColor: SMCLColor) {

  private[gui] val name: String = this.presetName.getOrElse("")


  /** the HSI (hue--saturation--intensity) equivalents of this RGB color
    * @return the three HSI components as a tuple; cf. [[hue]], [[saturation]], and [[intensity]] */
  lazy val hsi = this.smclColor.toHSI


  /** the hue (“main observable color”) component of this color when viewed in the HSI color scheme,
    * in degrees around the color wheel (0--360) */
  def hue = this.hsi._1

  /** the saturation (“richness”) component of the color when viewed in the HSI color scheme,
    * between [[Color.Min]] and [[Color.Max]]; that is, 0--255 */
  def saturation = this.hsi._2


  /** the intensity (“brightness”) component of this color when viewed in the HSI color scheme,
    * between [[Color.Min]] and [[Color.Max]]; that is, 0--255 */
  def intensity = this.hsi._3


  /** Determines if this `Color` equals another object. It does if an only if the other object is also
    * a `Color` and its [[red]], [[green]], [[blue]], and [[opacity]] components are equal to this
    * `Color`’s. (Any names the colors may have are irrelevant.) */
  override def equals(other: Any): Boolean = other match {
    case Color(r, g, b, o) => r == this.red && g == this.green && b == this.blue && o == this.opacity
    case _                 => false
  }


  /** Generates a hash code for this `Color` from its four components. */
  override lazy val hashCode: Int = {
    val prime = 31
    var result = 1
    result = prime * result + red
    result = prime * result + green
    result = prime * result + blue
    result = prime * result + opacity
    result
  }


  /** Returns a color with the specified color components. The parameters default to this `Color`
    * object’s component values, so you can specify just the ones you want to change, as in
    * `myColor.edit(red = 255, opacity = 100)`. */
  def edit(red: Int = this.red, green: Int = this.green, blue: Int = this.blue, opacity: Int = this.opacity): Color =
    Color(red, green, blue, opacity)



  /** Returns the color whose R, G, and B components are the complements of this `Color` object’s
    * respective components (255 minus the value). Opacity is retained. */
  def negative = Color(Color.Max - this.red, Color.Max - this.green, Color.Max - this.blue, this.opacity)


  /** Returns a color that is somewhat lighter than this one. */
  def lighter: Color = Color(this.smclColor.lighter, None)


  /** Returns a color that is somewhat darker than this one. */
  def darker: Color = Color(this.smclColor.darker, None)


  /** Returns a string description of this color. Examples:
    *  - `"Red"`, `"CornflowerBlue"` (named preset colors)
    *  - `"Color(200, 100, 200)"` (opaque custom color)
    *  - `"Color(200, 100, 200, 50)"` (non-opaque custom color) */
  override def toString: String = {
    def colorDetails = if (opacity < Color.Max) s"Color($red, $green, $blue, $opacity)" else  s"Color($red, $green, $blue)"
    this.presetName.getOrElse(colorDetails)
  }


  /** Returns the [[https://docs.oracle.com/javase/8/docs/api/java/awt/Color.html java.awt.Color]]
    * equivalent to this color (which is compatible with Swing GUIs). */
  def toSwingColor: java.awt.Color = {
    import smcl.infrastructure.jvmawt.SMCLColorWrapper
    this.smclColor.toAWTColor
  }

}
