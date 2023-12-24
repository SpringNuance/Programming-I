package o1.gui

import smcl.colors.{rgb => SCR}
import SCR.PresetColor



/** This package contains [[Color]] constants. They cover all the colors listed in the W3C’s
  * [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ CSS Color Module specification (July
  * 5th, 2017)]]; there are some additional ones as well.
  *
  * All the colors are fully opaque, except [[colors.Transparent Transparent]], which is fully
  * transparent.
  *
  * The contents of this package are aliased in the top-level package [[o1]] so that
  * they are accessible to students simply via `import o1._`. */
package object colors {

  o1.util.smclInit()
  /** Represents a fully transparent (white) color. */
  val Transparent: Color = Color(SCR.Transparent)

  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val AliceBlue: Color = Color(SCR.AliceBlue)
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. */
  val AlizarinCrimson: Color = Color(PresetColor(0xff4e1500, "alizarin crimson", "alizarincrimson"))
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Amethyst: Color = Color(SCR.Amethyst)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val AntiqueWhite: Color = Color(SCR.AntiqueWhite)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Aqua: Color = Color(SCR.Aqua)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Aquamarine: Color = Color(SCR.Aquamarine)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Azure: Color = Color(SCR.Azure)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Beige: Color = Color(SCR.Beige)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Bisque: Color = Color(SCR.Bisque)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Black: Color = Color(SCR.Black)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val BlanchedAlmond: Color = Color(SCR.BlanchedAlmond)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Blue: Color = Color(SCR.Blue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val BlueViolet: Color = Color(SCR.BlueViolet)
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. */
  val BrightRed: Color = Color(PresetColor(0xffdb0000, "bright red", "brightred"))
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Brown: Color = Color(SCR.Brown)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val BurlyWood: Color = Color(SCR.BurlyWood)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val CadetBlue: Color = Color(SCR.CadetBlue)
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. */
  val CadmiumYellow: Color = Color(PresetColor(0xffffec00, "cadmium yellow", "cadmiumyellow"))
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Chartreuse: Color = Color(SCR.Chartreuse)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Chocolate: Color = Color(SCR.Chocolate)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Coral: Color = Color(SCR.Coral)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val CornflowerBlue: Color = Color(SCR.CornflowerBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val CornSilk: Color = Color(SCR.CornSilk)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Crimson: Color = Color(SCR.Crimson)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Cyan: Color = Color(SCR.Cyan)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkBlue: Color = Color(SCR.DarkBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkCyan: Color = Color(SCR.DarkCyan)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkGoldenrod: Color = Color(SCR.DarkGoldenrod)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkGray: Color = Color(SCR.DarkGray)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkGreen: Color = Color(SCR.DarkGreen)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkGrey: Color = Color(SCR.DarkGrey)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkKhaki: Color = Color(SCR.DarkKhaki)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkMagenta: Color = Color(SCR.DarkMagenta)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkOliveGreen: Color = Color(SCR.DarkOliveGreen)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkOrange: Color = Color(SCR.DarkOrange)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkOrchid: Color = Color(SCR.DarkOrchid)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkRed: Color = Color(SCR.DarkRed)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkSalmon: Color = Color(SCR.DarkSalmon)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkSeaGreen: Color = Color(SCR.DarkSeaGreen)
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. */
  val DarkSienna: Color = Color(PresetColor(0xff5f2e1f, "dark sienna", "darksienna"))
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkSlateBlue: Color = Color(SCR.DarkSlateBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkSlateGray: Color = Color(SCR.DarkSlateGray)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkSlateGrey: Color = Color(SCR.DarkSlateGrey)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkTurquoise: Color = Color(SCR.DarkTurquoise)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DarkViolet: Color = Color(SCR.DarkViolet)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DeepPink: Color = Color(SCR.DeepPink)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DeepSkyBlue: Color = Color(SCR.DeepSkyBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DimGray: Color = Color(SCR.DimGray)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DimGrey: Color = Color(SCR.DimGrey)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val DodgerBlue: Color = Color(SCR.DodgerBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val FireBrick: Color = Color(SCR.FireBrick)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val FloralWhite: Color = Color(SCR.FloralWhite)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val ForestGreen: Color = Color(SCR.ForestGreen)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Fuchsia: Color = Color(SCR.Fuchsia)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Gainsboro: Color = Color(SCR.Gainsboro)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val GhostWhite: Color = Color(SCR.GhostWhite)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Gold: Color = Color(SCR.Gold)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Goldenrod: Color = Color(SCR.Goldenrod)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Gray: Color = Color(SCR.Gray)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Green: Color = Color(SCR.Green)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val GreenYellow: Color = Color(SCR.GreenYellow)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Grey: Color = Color(SCR.Grey)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Honeydew: Color = Color(SCR.Honeydew)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val HotPink: Color = Color(SCR.HotPink)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val IndianRed: Color = Color(SCR.IndianRed)
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. */
  val IndianYellow: Color = Color(PresetColor(0xffffb800, "indian yellow", "indianyellow"))
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Indigo: Color = Color(SCR.Indigo)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Ivory: Color = Color(SCR.Ivory)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Khaki: Color = Color(SCR.Khaki)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Lavender: Color = Color(SCR.Lavender)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LavenderBlush: Color = Color(SCR.LavenderBlush)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LawnGreen: Color = Color(SCR.LawnGreen)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LemonChiffon: Color = Color(SCR.LemonChiffon)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightBlue: Color = Color(SCR.LightBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightCoral: Color = Color(SCR.LightCoral)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightCyan: Color = Color(SCR.LightCyan)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightGoldenrodYellow: Color = Color(SCR.LightGoldenrodYellow)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightGray: Color = Color(SCR.LightGray)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightGreen: Color = Color(SCR.LightGreen)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightGrey: Color = Color(SCR.LightGrey)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightPink: Color = Color(SCR.LightPink)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightSalmon: Color = Color(SCR.LightSalmon)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightSeaGreen: Color = Color(SCR.LightSeaGreen)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightSkyBlue: Color = Color(SCR.LightSkyBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightSlateGray: Color = Color(SCR.LightSlateGray)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightSlateGrey: Color = Color(SCR.LightSlateGrey)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightSteelBlue: Color = Color(SCR.LightSteelBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LightYellow: Color = Color(SCR.LightYellow)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Lime: Color = Color(SCR.Lime)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val LimeGreen: Color = Color(SCR.LimeGreen)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Linen: Color = Color(SCR.Linen)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Magenta: Color = Color(SCR.Magenta)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Maroon: Color = Color(SCR.Maroon)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val MediumAquamarine: Color = Color(SCR.MediumAquamarine)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val MediumBlue: Color = Color(SCR.MediumBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val MediumOrchid: Color = Color(SCR.MediumOrchid)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val MediumPurple: Color = Color(SCR.MediumPurple)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val MediumSeaGreen: Color = Color(SCR.MediumSeaGreen)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val MediumSlateBlue: Color = Color(SCR.MediumSlateBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val MediumSpringGreen: Color = Color(SCR.MediumSpringGreen)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val MediumTurquoise: Color = Color(SCR.MediumTurquoise)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val MediumVioletRed: Color = Color(SCR.MediumVioletRed)
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. */
  val MidnightBlack: Color = Color(PresetColor(0xff000000, "midnight black", "midnightblack"))
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val MidnightBlue: Color = Color(SCR.MidnightBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val MintCream: Color = Color(SCR.MintCream)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val MistyRose: Color = Color(SCR.MistyRose)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Moccasin: Color = Color(SCR.Moccasin)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val NavajoWhite: Color = Color(SCR.NavajoWhite)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Navy: Color = Color(SCR.Navy)
  /** A named color as per [[http://montypython.50webs.com/scripts/Series_1/53.htm  the  MP  standard]]. */
  lazy val NorwegianBlue: Color = throw new NotImplementedError("This color has expired.")
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val OldLace: Color = Color(SCR.OldLace)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Olive: Color = Color(SCR.Olive)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val OliveDrab: Color = Color(SCR.OliveDrab)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Orange: Color = Color(SCR.Orange)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val OrangeRed: Color = Color(SCR.OrangeRed)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Orchid: Color = Color(SCR.Orchid)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val PaleGoldenrod: Color = Color(SCR.PaleGoldenrod)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val PaleGreen: Color = Color(SCR.PaleGreen)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val PaleTurquoise: Color = Color(SCR.PaleTurquoise)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val PaleVioletRed: Color = Color(SCR.PaleVioletRed)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val PapayaWhip: Color = Color(SCR.PapayaWhip)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val PeachPuff: Color = Color(SCR.PeachPuff)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Peru: Color = Color(SCR.Peru)
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. */
  val PhthaloBlue: Color = Color(PresetColor(0xff0c0040, "phthalo blue", "phthaloblue"))
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. */
  val PhthaloGreen: Color = Color(PresetColor(0xff102e3c, "phthalo green", "phthalogreen"))
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Pink: Color = Color(SCR.Pink)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Plum: Color = Color(SCR.Plum)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val PowderBlue: Color = Color(SCR.PowderBlue)
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. */
  val PrussianBlue: Color = Color(PresetColor(0xff021e44, "Prussian blue", "prussianblue"))
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Purple: Color = Color(SCR.Purple)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Red: Color = Color(SCR.Red)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val RosyBrown: Color = Color(SCR.RosyBrown)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val RoyalBlue: Color = Color(SCR.RoyalBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val SaddleBrown: Color = Color(SCR.SaddleBrown)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Salmon: Color = Color(SCR.Salmon)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val SandyBrown: Color = Color(SCR.SandyBrown)
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. */
  val SapGreen: Color = Color(PresetColor(0xff0a3410, "sap green", "sapgreen"))
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val SeaGreen: Color = Color(SCR.SeaGreen)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val SeaShell: Color = Color(SCR.SeaShell)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Sienna: Color = Color(SCR.Sienna)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Silver: Color = Color(SCR.Silver)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val SkyBlue: Color = Color(SCR.SkyBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val SlateBlue: Color = Color(SCR.SlateBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val SlateGray: Color = Color(SCR.SlateGray)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val SlateGrey: Color = Color(SCR.SlateGrey)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Snow: Color = Color(SCR.Snow)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val SpringGreen: Color = Color(SCR.SpringGreen)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val SteelBlue: Color = Color(SCR.SteelBlue)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Tan: Color = Color(SCR.Tan)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Teal: Color = Color(SCR.Teal)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Thistle: Color = Color(SCR.Thistle)
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. */
  val TitaniumHwite: Color = Color(PresetColor(0xffffffff, "titanium hwite", "titaniumhwite"))
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Tomato: Color = Color(SCR.Tomato)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Turquoise: Color = Color(SCR.Turquoise)
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. */
  val VanDykeBrown: Color = Color(PresetColor(0xff221b15, "Van Dyke brown", "vandykebrown"))
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Violet: Color = Color(SCR.Violet)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Wheat: Color = Color(SCR.Wheat)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val White: Color = Color(SCR.White)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val WhiteSmoke: Color = Color(SCR.WhiteSmoke)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val Yellow: Color = Color(SCR.Yellow)
  /** A named color as per [[https://www.w3.org/TR/2016/WD-css-color-4-20160705/ W3C’s list of colors]]. */
  val YellowGreen: Color = Color(SCR.YellowGreen)
  /** A named color as per [[https://thomaspark.co/2015/11/bob-ross-color-palette-in-css/   Bob  Ross]]. */
  val YellowOchre: Color = Color(PresetColor(0xffc79b00, "yellow ochre", "yellowochre"))


  /** A grayscale color that contains 10% black. */
  val Black10: Color = Color(SCR.Black10)
  /** A grayscale color that contains 20% black. */
  val Black20: Color = Color(SCR.Black20)
  /** A grayscale color that contains 30% black. */
  val Black30: Color = Color(SCR.Black30)
  /** A grayscale color that contains 40% black. */
  val Black40: Color = Color(SCR.Black40)
  /** A grayscale color that contains 50% black. */
  val Black50: Color = Color(SCR.Black50)
  /** A grayscale color that contains 60% black. */
  val Black60: Color = Color(SCR.Black60)
  /** A grayscale color that contains 70% black. */
  val Black70: Color = Color(SCR.Black70)
  /** A grayscale color that contains 80% black. */
  val Black80: Color = Color(SCR.Black80)
  /** A grayscale color that contains 90% black. */
  val Black90: Color = Color(SCR.Black90)
  /** A grayscale color that contains 10% white. */
  val White10: Color = Color(SCR.White10)
  /** A grayscale color that contains 20% white. */
  val White20: Color = Color(SCR.White20)
  /** A grayscale color that contains 30% white. */
  val White30: Color = Color(SCR.White30)
  /** A grayscale color that contains 40% white. */
  val White40: Color = Color(SCR.White40)
  /** A grayscale color that contains 50% white. */
  val White50: Color = Color(SCR.White50)
  /** A grayscale color that contains 60% white. */
  val White60: Color = Color(SCR.White60)
  /** A grayscale color that contains 70% white. */
  val White70: Color = Color(SCR.White70)
  /** A grayscale color that contains 80% white. */
  val White80: Color = Color(SCR.White80)
  /** A grayscale color that contains 90% white. */
  val White90: Color = Color(SCR.White90)



  object O_o   // Apparently Scaladoc doesn’t (7/2018) generate a page for a package object unless it contains at least one object or class.

}
