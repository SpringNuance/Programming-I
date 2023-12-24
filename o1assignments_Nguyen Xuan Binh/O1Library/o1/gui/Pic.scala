package o1.gui


import java.awt.image.BufferedImage

import smcl.colors.rgb.{Color => SMCLColor}
import smcl.infrastructure.jvmawt.SMCLBitmapWrapper
import smcl.pictures.{Bitmap => SMCLBitmap, Picture => SMCLPicture, PictureElement => SMCLPictureElement}
import smcl.settings._

import o1.gui.Anchor._
import o1.gui.PicHistory.op.{Create,Transform,AdjustViewport,Miscellaneous}
import o1.util._
import o1.world._
import o1.gui.colors.Transparent

import javax.swing.Icon






/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
//////
//////        PIC COMPANION OBJECT
//////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////

/** The primary purpose of this companion object of [[Pic class `Pic`]] is to provide methods for
  * creating new [[Pic]] instances: ([[apply(pathOrURL:String)* apply]], [[generate]], [[circle]], etc.
  * There is also a small selection of related utility methods.
  *
  * This object has an alias in the top-level package [[o1]], so it’s accessible to students simply
  * via `import o1._`. The shape-creating methods of this object (such as `circle`) are also available
  * as functions in package [[o1]]. */
object Pic extends ShapeAPI {

  o1.util.smclInit()


  /** Takes an image file path or URL as a string, loads image data from that location, and constructs
    * a [[Pic]] from that data. Anchors the `Pic` at its center. Throws an error if the file
    * doesn’t exist or could not be accessed (cf. [[asTry]], [[asOption]]).
    * @param pathOrURL  a classpath-relative path or a URL string that starts with "http://" or "https://".
    *                   Note that the O1Library project exports its `pics` folder, so the example images
    *                   in that folder can be loaded using just the file name.
    * @return the loaded image (a bitmap) */
  @inline def apply(pathOrURL: String): Pic = this(pathOrURL, Center)


  /** Takes an image file path or URL as a string, loads image data from that location, and constructs
    *  an anchored [[Pic]] from that data. Throws an error if the file doesn’t exist or could not
    *  be accessed (cf. [[asTry]], [[asOption]]).
    * @param pathOrURL  a classpath-relative path or a URL string that starts with "http://" or "https://".
    *                   Note that the O1Library project exports its `pics` folder, so the example images
    *                   in that folder can be loaded using just the file name.
    * @param anchor     an anchor for the new `Pic`
    * @return the loaded image (a bitmap) */
    // * @throws AccessDeniedByServerError                   for HTTP status codes 401, 402, 403, 407, and 451
    // * @throws EmptyFileError                              if the given path points to an empty file
    // * @throws FileAttributeRetrievalFailedError           if the attributes of the file that the given path points to could not be retrieved
    // * @throws FileNotFoundError                           if the given path points to a file that does not seem to exist
    // * @throws ImageInputStreamNotCreatedError             if a cache file is needed but could not be created
    // * @throws ImageNotFoundError                          for HTTP status codes 204, 205, 404, and 410, and if the requested resource could not be found
    // * @throws ImageReaderNotRetrievedError                if the first suitable [[ImageReader]] cannot be retrieved
    // * @throws MaximumBitmapSizeExceededError              if a bitmap is larger than the maximum allowed bitmap size
    // * @throws MinimumBitmapSizeNotMetError                if a bitmap is smaller than the minimum allowed bitmap size
    // * @throws OperationPreventedBySecurityManagerError    if retrieval of file attributes was prevented by a security manager
    // * @throws PathDoesNotPointToRegularFileError          if the given path does not point to a regular file
    // * @throws PathIsEmptyOrOnlyWhitespaceError            if the given path is empty or contains only whitespace
    // * @throws PathIsNullError                             if the given path was actually null
    // * @throws PathPointsToFolderError                     if the given path points to a folder
    // * @throws PathPointsToSymbolicLinkError               if the given path poins to a symbolic link
    // * @throws RedirectionRequestedError                   for HTTP status codes 301, 302, 307, and 308
    // * @throws RequestedURITooLongError                    for HTTP status code 414
    // * @throws ServerError                                 for all HTTP status codes beginning with 5
    // * @throws SuitableImageReaderNotFoundError            if no suitable [[ImageReader]] is found
    // * @throws SuitableImageStreamProviderNotFoundError    if [[ImageIO]] did not find a suitable image stream service provider instance
    // * @throws TooManyRequestsToServerError                for HTTP status code 429
    // * @throws UnknownFileExtensionError                   if the file extension is unknown
    // * @throws UnknownHTTPResponseError                    for all HTTP status codes other than 200 that are not reported with other exceptions
    // * @throws UnknownMIMETypeError                        if the MIME type sent by the server is not supported
    // * @throws UnableToRetrieveDataOverHTTPConnectionError if an I/O error occurs while creating an [[InputStream]] or if the protocol to be used does not support input
    // * @throws UnableToOpenHTTPConnectionError             if an [[HttpURLConnection]] instance could not be created; if the HTTP request method cannot be reset; if the request method is not valid; if the connection timeout expires before a connection has been established; or if an I/O error occurs during establishing the connection/
  @inline def apply(pathOrURL: String, anchor: Anchor): Pic = {
    val loadedImage = SMCLBitmap(pathOrURL)
    val newHistory = PicHistory(Create(method = "Pic", simpleDescription = pathOrURL))
    apply(smclContent = loadedImage, anchor, newHistory)
  }


  @inline private[gui] final def apply(smclContent: SMCLPictureElement, o1Anchor: Anchor, history: PicHistory): Pic =
    apply(Option(smclContent), o1Anchor, history)


  @inline private[gui] final def apply(smclContent: Option[SMCLPictureElement], o1Anchor: Anchor, history: PicHistory): Pic = {
    val wrappedContent = smclContent.fold(SMCLPicture())(_.toPicture)
    new Pic(wrappedContent, o1Anchor, history)
  }


  /** Takes an image file path or URL as a string and attempts to load image data from that location and
    *  construct a [[Pic]] from that data.
    * @param pathOrURL  a classpath-relative path or a URL string that starts with "http://" or "https://".
    * @param anchor     an anchor for the new `Pic`
    * @return the loaded [[Pic]] (a bitmap) or the error that caused the attempt to fail
    * @see [[asOption]] */
  def asTry(pathOrURL: String, anchor: Anchor = Center): Try[Pic] = Try(Pic(pathOrURL, anchor))


  /** Takes an image file path or URL as a string and attempts to load image data from that location and
    *  construct a [[Pic]] from that data.
    * @param pathOrURL  a classpath-relative path or a URL string that starts with "http://" or "https://".
    * @param anchor     an anchor for the new `Pic`
    * @return the loaded [[Pic]] (a bitmap); `None` in case the attempt failed for any reason
    * @see [[asTry]] */
  def asOption(pathOrURL: String, anchor: Anchor = Center): Option[Pic] = Pic.asTry(pathOrURL, anchor).toOption


  /** Takes an image file path or URL as a string and attempts to load image data from that location and
    *  construct a [[https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html BufferedImage]]
    *  from that data. Note that this method does not construct a [[Pic]] at all.
    * @param pathOrURL  a classpath-relative path or a URL string that starts with "http://" or "https://".
    * @return the loaded image; `None` in case the attempt failed for any reason */
  def asImage(pathOrURL: String): Option[BufferedImage] = Pic.asOption(pathOrURL).flatMap( _.toImage )


  /** Displays the given [[Pic]] in a minimalistic GUI window. Calling this method repeatedly on the
    * same [[Pic]] doesn’t display multiple windows but reuses the existing one. This method is meant
    * only for experimentation and debugging; not for GUI construction (cf. [[o1.gui.mutable.ViewFrame views]]).
    * @param pic              the image to display; should have a height and width of at least one pixel
    * @param background        the color to appear behind the `Pic` in the frame (where the `Pic` is transparent)
    * @param border            the width of the simple black window frame, in pixels
    * @see [[hide]]
    * @see [[Pic.show]] */
 def show(pic: Pic, background: Color, border: Int): Unit = {
    val reasonNotToShow = pic.dimensions match  {
      case (w, h) if w < 1 && h < 1 => Some(s"The picture is too small to show (width=$w < 1, height=$h < 1).")
      case (w, h) if w < 1          => Some(s"The picture is too narrow to show (width=$w < 1).")
      case (w, h) if h < 1          => Some(s"The picture is too flat to show (height=$h < 1).")
      case pictureLargeEnoughToShow => if (o1.gui.isInTestMode) Some("Not actually showing the picture because in text-based test mode.") else None
    }
    reasonNotToShow match {
      case Some(reason) => println(reason)
      case None         => PicFrame.show(pic, background, border)
    }
  }


  /** Hides the window that has been created (with [[show]]) to display the given [[Pic]].
    * If there is no such window, does nothing.
    * @see [[show]], [[hideAll]] */
  def hide(pic: Pic): Unit = {
    PicFrame.hide(pic)
  }

  /** Hides any and all windows that have been created (with [[show]]) to display [[Pic]]s.
    * @see [[show]], [[hide]] */
  def hideAll(): Unit = {
    PicFrame.hideAll()
  }


  /** Creates a new [[Pic]] by applying the given pixel-generating function to each pair coordinates
    * within the new image.
    * @param width      the width, in pixels, of the new `Pic`
    * @param height     the height, in pixels, of the new `Pic`
    * @param makeColor  a function that `generate` calls on every pixel location of
    *                   the new `Pic` to produce the color at that location
    * @return the generated [[Pic]] (a bitmap) */
  def generate(width: Int, height: Int, makeColor: (Int, Int) => Color): Pic = {
    def callPasser: (Int, Int) => SMCLColor = makeColor(_, _).smclColor
    val smclContent = smcl.pictures.Bitmap(width, height, callPasser)
    val anchor = Center
    val newHistory = PicHistory(Create(method = "Pic.generate", simpleDescription = "generated pic"))
    Pic(smclContent, anchor, newHistory)
  }


  /** Creates a new [[Pic]] by setting colors of each individual pixel according to the `colors` list.
    * @param width  the width, in pixels, of the new `Pic`
    * @param height the height, in pixels, of the new `Pic`
    * @param colors a sequence of colors, one per pixel, starting from the upper-left corner and
    *               continuing to the right and line-by-line towards the bottom
    * @return the new [[Pic]] (a bitmap) */
  def fromColors(width: Int, height: Int, colors: Seq[Color]): Pic = {
    if (colors == null) throw new IllegalArgumentException("The sequence of pixel colors cannot be null")
    val smclContent = smcl.pictures.Bitmap(width, height, colors.map( _.smclColor ))
    val anchor = Center
    val newHistory = PicHistory(Create(method = "Pic.fromColors", simpleDescription = "composed pic"))
    Pic(smclContent, anchor, newHistory)
  }


  private[gui] def leftToRight(pics: Seq[Pic]) = pics.reduceLeft( _ leftOf _ )
  private[gui] def topToBottom(pics: Seq[Pic]) = pics.reduceLeft( _ above _ )

}






/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
//////
//////        CLASS PIC
//////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////

/** Each instance of this class represents a picture: an immutable two-dimensional image.
  *
  * You don’t instantiate `Pic` directly; instead, you create `Pic`s with the methods on the
  * [[Pic$ `Pic` companion object]]. For instance, you can:
  *
  *  - load existing images: `Pic("mypic.png")` or
  *    `Pic("http://address.of/mypic.png")`),
  *  - create `Pic`s of shapes (e.g., `circle(150, Red)` or
  *    `star(100, Black)`); or
  *  - generate the pixels of a `Pic` with a function.
  *
  * Moreover, many of the methods of a `Pic` object create and return new `Pic`s that
  * represent combinations or transformations of existing `Pic`s.
  *
  * This class has an alias in the top-level package [[o1]], so it’s accessible to students
  * simply via `import o1._`.
  *
  * The examples below illustrate a few of the methods (assuming the above import):
  * {{{
  * val background = rectangle(400, 300, Red)
  * val rotatedSquare = square(50, Blue).clockwise(30)
  * val squareAgainstBg = background.place(rotatedSquare, Pos(100, 100))
  * squareAgainstBg.show()
  *
  * val ladybug = Pic("ladybug.png").scaleTo(100).flipHorizontal
  * val combination = circle(100, Red).leftOf(ladybug)
  * val part = combination.crop(Pos(10, 10), 180, 80)
  * val negative = part.transformColors( _.negative )
  * negative.show()
  * }}}
  *
  * Some of the methods of a `Pic` that use [[Pos]] objects or plain x and y coordinates to
  * indicate positions within the `Pic` (e.g., `place` and `crop` above). All these methods
  * consider the origo to be at the top left-hand corner or the `Pic`, with x values increasing
  * downwards and y values rightwards. The coordinates are in pixels.
  *
  * Each image has an [[anchor]] that defines where it connects to other `Pic`s. By default,
  * the anchor is [[Center]]; for example, the `place` method call above puts the center of
  * the square at `Pos(100, 100)` within the background.
  *
  * Here is a of the main types of operations as methods on a `Pic`, and examples of each type:
  *
  *  - Combining `Pic`s by positioning them relative
  *    to each other: `above`, `below`, `leftOf`,
  *    `rightOf`, `onto`, `against`, `place`.
  *  - Rotations (*): `clockwise`, `counterclockwise`
  *  - Changing size (*): `scaleBy`, `scaleTo`
  *  - Selecting and moving pixels (**): `crop`,
  *    `shift`, `flipHOrizontal`, `flipVertical`
  *  - Examining and manipulating individual pixels (**):
  *    `pixelColor`, `transformXY`, `transformColors`,
  *    `combine`
  *  - Convenience methods for experimentation and
  *    debugging: `show`, `hide`.
  *
  * ==Notes on implementation and efficiency:==
  *
  * Internally, a `Pic` stores its contents either as vector-based graphics, as a bitmap (raster),
  * or as a combination of the two. By design, that internal representation is meant to be largely
  * opaque to the user of the `Pic` class: students in O1 working on O1’s standard assignments
  * generally shouldn’t need to know or care about it. Nevertheless, whether a particular `Pic`
  * is stored in vector or bitmap form does have very substantial effect on efficiency in some
  * contexts; `Pic`, like the rest of O1Library, is not designed for high-performance graphics.
  *
  * Some users of this class may wish to know the following:
  *
  *  - `Pic`s start out in either vector form or bitmap form,
  *    depending on which method created them. Specifically, the
  *    shape-creating methods (like `rectangle` and `circle`).
  *    produce `Pic`s in vector form.
  *  - No operation on a `Pic` ever changes an already rasterized
  *    bitmap into vector graphics. (An operation such as `leftOf`
  *    can produce an `Pic` that is stored as a combination of
  *    a vector graphic and a bitmap.)
  *  - Some operations always produce a rasterized `Pic`. These
  *    are marked with a double asterisk (**) in the list above.
  *    Some operations sometimes produce rasterized `Pic`s but
  *    may retain the vector-based representation in simple cases.
  *    These are marked with a single asterisk (*).
  *  - You can call [[freeze]] to force rasterization.
  *
  * @param anchor  the anchor of the image */
final class Pic private[gui](private[gui] val smclPic: SMCLPicture, val anchor: Anchor, private[o1] val historyDetails: PicHistory) extends HasAnchor {

  /** A list of method names used for creating this `Pic`. Only the most recently applied methods
    * are listed (up to [[historyLength]]. This method may be useful for demonstrative purposes,
    * debugging, and/or automatic assessment. */
  lazy val history: List[String] = this.historyDetails.methodList


  /** A limit on the number of historical method calls that are tracked by the `Pic` and available
    * through [[history]]. Defaults to 10. Can be changed via [[withHistoryLength]]. */
  val historyLength: Int = this.historyDetails.maximumLength


  /** Sets a new limit to the history length on the `Pic`.
    * @param maxLength  a new [[historyLength]] (>= 0) */
  def withHistoryLength(maxLength: Int): Pic =
    Pic(this.smclPic, this.anchor, this.historyDetails.setHistoryLength(maxLength))


  /** Returns a very simple string description of this `Pic`. Examples:
    *  - `"ladybug.png"`
    *  - `circle-shape`
    *  - `"ladybug.png" (transformed)`
    *  - `circle-shape (transformed)`
    *  - `combined pic` */
  @inline override def toString: String = {
    val s = new StringBuilder
    s ++= this.historyDetails.creationOp.simpleDescription
    if (this.historyDetails.containsTransformations) {
      s ++= " (transformed)"
    }
    s.toString()
  }


  /** Forces rasterization of any vector graphics in this `Pic`s internal representation into
    * bitmap form. See the [[Pic introduction to this class]] for more information.
    * @return the same picture, stored entirely in bitmap form
    * @see [[isBitmap]] */
  @inline def freeze: Pic =
    Pic(this.toSMCLBitmap, this.anchor, Miscellaneous("freeze") :: this.historyDetails)



  @inline private def toSMCLBitmap: SMCLBitmap =
    if (this.isBitmap)
      this.smclPic.elements.head.asInstanceOf[SMCLBitmap]
    else
      this.smclPic.toBitmap


  /** Indicates whether this `Pic` is internally stored as a bitmap raster.
    * See the [[Pic introduction to this class]] for more information.
    * @return `true` if the entire `Pic` is rasterized as a bitmap;
    *         `false` if the `Pic`’s internal representation uses any vector graphics
    * @see [[freeze]] */
  @inline def isBitmap: Boolean =
    this.smclPic.elements.sizeIs == 1 && this.smclPic.elements.head.isBitmap


  /** Displays this `Pic` in a minimalistic GUI window. Calling this method repeatedly on the same
    * `Pic` doesn’t display multiple windows but reuses the existing one. This method is meant only for
    * experimentation and debugging; not for GUI construction (cf. [[o1.gui.mutable.ViewFrame views]]).
    *
    * Students may alternatively use the `show(pic)` variant in the top-level package [[o1]].
    *
    * @param background        the color to appear behind the `Pic` in the frame (where the `Pic` is transparent)
    * @param border            the width of the simple black window frame, in pixels
    * @see [[hide]]
    * @see [[Pic$.show Pic.show]] */
  @inline def show(background: Color = colors.White, border: Int = 1): Unit = {
    Pic.show(this, background, border)
  }


  /** Hides the window that has been created (with [[show]]) to display the `Pic`.
    * If there is no such window, does nothing.
    *
    * Students may alternatively use the `hide(pic)` variant in the top-level package [[o1]].
    *
    * @see [[show]], [[Pic$.hide Pic.hide]] */
  @inline def hide(): Unit = Pic.hide(this)



  //-----------------------------------------------------------------------------------------------
  //
  //
  // Dimensions and locations
  //
  //
  //-----------------------------------------------------------------------------------------------

  private[gui] lazy val boundary: Bounds = this.smclPic.viewport.map( _.boundary ).getOrElse(this.smclPic.boundary).toO1Bounds


  /** The width of the `Pic` in pixels. */
  lazy val width: Double = this.smclPic.viewport.map( _.width.inPixels ).getOrElse(this.smclPic.width.inPixels)


  /** The height of the `Pic` in pixels. */
  lazy val height: Double = this.smclPic.viewport.map( _.height.inPixels ).getOrElse(this.smclPic.height.inPixels)


  /** The [[width]] and [[height]] of the `Pic` as a tuple. */
  lazy val dimensions: (Double, Double) = (this.width, this.height)


  /** Determines if the given [[Pos]] is within this `Pic`’s borders. For that to be the case, the
    * `Pos` object’s `x` needs to be between zero (inclusive) and this `Pic`’s `width` (exclusive)
    * and its `y` needs to be betewen zero (inclusive) and this `Pic`’s `height` (exclusive). */
  def contains(pixel: Pos): Boolean = pixel.x.isBetween(0, this.width) && pixel.y.isBetween(0, this.height)


  /** Returns the colors of the pixels in this `Pic` that are “near” a given [[Pos]]. A pixel
    * counts as being near if it’s located at a `Pos` whose distance from the given `Pos` is less
    * than or equal to the given `range`. This includes the pixel at the given `Pos` (if there is one).
    * @param pos    a position whose nearby pixels in this `Pic` will be returned
    * @param range  the maximum distance of a returned pixel from `pos` (must be non-negative)
    * @return a collection of the pixel colors, in arbitrary order */
  def pixelsNear(pos: Pos, range: Double): Seq[Color] = {
    val maxDelta = range.abs.round
    def candidatesAround(center: Double) = BigDecimal(center - maxDelta) to BigDecimal(center + maxDelta) by 1.0
    val possNear = for {
      y <- candidatesAround(pos.y)
      x <- candidatesAround(pos.x)
      candidate = Pos(x.toDouble, y.toDouble)
      if pos.distance(candidate) <= range && this.contains(candidate)
    } yield candidate
    possNear.map(this.pixelColor).toSeq
  }






  //-----------------------------------------------------------------------------------------------
  //
  //
  // Anchors
  //
  //
  //-----------------------------------------------------------------------------------------------

  /** An [[Anchor.Absolute]] that points to the same spot in the `Pic` that the `Pic`’s current anchor does. */
  @inline def absoluteAnchor: Anchor = this.anchor.toAbsoluteWithin(this)


  /** Anchors the `Pic` at the given position.
    * @param anchor  a new anchoring position
    * @return a new `Pic` that’s identical to the original but anchored at the given position */
  @inline def anchorAt(anchor: Anchor): Pic =
    Pic(this.smclPic, anchor, Miscellaneous("anchorAt") :: this.historyDetails)



  //-----------------------------------------------------------------------------------------------
  //
  //
  // Viewports
  //
  //
  //-----------------------------------------------------------------------------------------------

  // Viewport-related operations are not public in O1Library at the present time. This may change later.

  private[gui] lazy val viewport: Viewport = this.smclPic.viewport.map( _.toO1Viewport ).getOrElse(Viewport.NotSet)

  @inline private[gui] def setViewportFrom(picture: Pic): Pic = setViewport(picture.viewport)

  @inline private[gui] def setViewportToContentBoundary: Pic =
    setViewportToContentBoundaryOf(this)

  @inline private[gui] def setViewportToContentBoundaryOf(picture: Pic): Pic =
    if (picture.smclPic.boundary.isUndefined)
      this
    else
      setViewport(picture.smclPic.boundary.toO1Bounds)

  @inline private[gui] def setViewport(topLeft: Pos, bottomRight: Pos): Pic =
    setViewport(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y)

  @inline private[gui] def setViewport(left: Double, top: Double, right: Double, bottom: Double): Pic = {
    val bounds = Bounds(left, top, right - left + 1, bottom - top + 1)
    setViewport(bounds)
  }

  @inline private[gui] def setViewport(boundary: Bounds): Pic = setViewport(Viewport(boundary))

  @inline private[gui] def setViewport(viewport: Viewport): Pic =
    this.setViewportInternal(viewport, AdjustViewport("setViewport") :: this.historyDetails)

  @inline private[gui] def setViewportWithoutHistory(viewport: Viewport): Pic =
    this.setViewportInternal(viewport, this.historyDetails)


  @inline private def setViewportInternal(viewport: Viewport, newHistory: PicHistory): Pic = {
    val possibleViewport = Option(viewport).flatMap( _.toSMCLViewport )
    val smclContent = possibleViewport.map(this.smclPic.setViewport).getOrElse(this.smclPic.removeViewport)
    Pic(smclContent, this.anchor, newHistory)
  }

  @inline private[gui] def hasViewport: Boolean = this.smclPic.hasViewport

  @inline private[gui] def removeViewport: Pic =
    removeViewportInternal(viewport, AdjustViewport("removeViewport") :: this.historyDetails)

  @inline private[gui] def removeViewportWithoutHistory(viewport: Viewport): Pic =
    removeViewportInternal(viewport, this.historyDetails)

  @inline private def removeViewportInternal(viewport: Viewport, newHistory: PicHistory): Pic = {
    val smclContent = this.smclPic.removeViewport
    Pic(smclContent, this.anchor, newHistory)
  }



  //-----------------------------------------------------------------------------------------------
  //
  //
  // Exporting images
  //
  //
  //-----------------------------------------------------------------------------------------------

  /** This `Pic` converted to a [[https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html
    * Java AWT `BufferedImage`]] (for use in Swing GUIs). */
  lazy val toImage: Option[BufferedImage] = toSMCLBitmap.toAWTImage     // Unless this pic is already in bitmap form, this will be less efficient as the pic will first be rasterized.


  /** This `Pic` converted to a [[https://docs.oracle.com/javase/8/docs/api/javax/swing/Icon.html
    * Swing `Icon`]]). */
  lazy val toIcon: Option[Icon] = toSMCLBitmap.toSwingIcon


  /** Saves the `Pic` in the given file in PNG format.
    * @param path  an absolute local path or a path relative to the working directory
    * @return `true` if the saving was successful and `false` if no file was created because there was no bitmap to save */
  //  * @throws FileOverwritingIsDeniedBySMCLError       if given path points to an existing file (not folder)
  //  * @throws ImageWriterNotRetrievedError             if the first suitable [[ImageWriter]] cannot be retrieved
  //  * @throws ImageWritingFailedError                  if an [[IOException]] occurred while writing to the file represented by the given path
  //  * @throws OperationPreventedBySecurityManagerError if an existing security manager prevents access to the file represented by the given path
  //  * @throws PathIsNullError                          if given path is null
  //  * @throws PathIsEmptyOrOnlyWhitespaceError         if given path is an empty string or contains only whitespace
  //  * @throws PathPointsToFolderError                  if given path points to an existing folder
  //  * @throws SuitableImageWriterNotFoundError         if no suitable [[ImageWriter]] is found
  //  * @throws UnableToOpenFileForWritingError          if the file represented by the given path cannot be opened
  @inline def save(path: String): Boolean = toSMCLBitmap.saveAsPngTo(path)



  //-----------------------------------------------------------------------------------------------
  //
  //
  // Color Transformations
  //
  //
  //-----------------------------------------------------------------------------------------------

  /** Returns the color of the pixel at the given position within the image.
    *
    * ''N.B.'' This is inefficient on a `Pic` that’s not fully rasterized as a bitmap.
    * If you have a vector graphic and need to call this method many times, consider
    * [[freeze freezing]] the `Pic` first. */
  @inline def pixelColor(pos: Pos): Color = pos.turnInto(this.pixelColor)


  /** Returns the color of the pixel at the given position within the image.
    *
    * ''N.B.'' This is inefficient on a `Pic` that’s not fully rasterized as a bitmap.
    * If you have a vector graphic and need to call this method many times, consider
    * [[freeze freezing]] the `Pic` first. */
  @inline def pixelColor(x: Int, y: Int): Color =
    toSMCLBitmap.colorAt(x, y).map( _.toO1Color ).getOrElse(Transparent)


  /** Returns the color of the pixel at the given coordinates within the image.
    *
    * ''N.B.'' This is inefficient on a `Pic` that’s not fully rasterized as a bitmap.
    * If you have a vector graphic and need to call this method many times, consider
    * [[freeze freezing]] the `Pic` first. */
  @inline def pixelColor(x: Double, y: Double): Color =
    toSMCLBitmap.colorAt(x.floor.toInt, y.floor.toInt).map( _.toO1Color ).getOrElse(Transparent)


  /** Returns the color of the pixel at the given coordinates within the image.
    * (This is equivalent to calling `pixelColor`.)
    *
    * ''N.B.'' This is inefficient on a `Pic` that’s not fully rasterized as a bitmap.
    * If you have a vector graphic and need to call this method many times, consider
    * [[freeze freezing]] the `Pic` first. */
  @inline def apply(pixelX: Int, pixelY: Int): Color = this.pixelColor(pixelX, pixelY)


  /** Returns the color of the pixel at the given coordinates within the image.
    * (This is equivalent to calling `pixelColor`.)
    *
    * ''N.B.'' This is inefficient on a `Pic` that’s not fully rasterized as a bitmap.
    * If you have a vector graphic and need to call this method many times, consider
    * [[freeze freezing]] the `Pic` first. */
  @inline def apply(pixelPos: Pos): Color = this.pixelColor(pixelPos)


  /** Applies the given operation to each pixel in the `Pic` to generate a different `Pic`.
    * That is, every color in the image will be replaced by another color in the generated image,
    * as specified by the given function.
    * @param transformer  a function that `transformColors` calls on every pixel in the image to map its color to a new one
    * @return the new (bitmap) image obtained by calling `transformer` and putting together the outputs */
  @inline def transformColors(transformer: Color => Color): Pic = {
    def callPasser(c: SMCLColor): SMCLColor = transformer(Color(c)).smclColor
    val smclContent = toSMCLBitmap.transformColorToColor(callPasser _)
    Pic(smclContent, this.anchor, Transform("transformColors") :: this.historyDetails)
  }


  /** Applies the given operation to each position in the `Pic` to generate a different `Pic`.
    * @param newColorAt  a function that `transformXY` calls repeatedly to determine the pixels of the transformed `Pic`
    * @return the new (bitmap) image obtained by calling `newColorAt` and putting together the outputs */
  @inline def transformXY(newColorAt: (Int, Int) => Color): Pic = {
    def callPasser: (Int, Int) => SMCLColor = newColorAt(_, _).smclColor
    val smclContent = toSMCLBitmap.setColorsByLocation(callPasser)
    Pic(smclContent, this.anchor, Transform("transformXY") :: this.historyDetails)
  }



  //-----------------------------------------------------------------------------------------------
  //
  //
  // Geometrical Transformations
  //
  //
  //-----------------------------------------------------------------------------------------------

  /** Returns a partial `Pic` of a rectangular area within this `Pic`.
    * @param boundary  the cropping frame within this `Pic`
    * @return a new `Pic` (a bitmap) that contains a part of the original */
  @inline def crop(boundary: Bounds): Pic = {
    val ulCorner = Pos(boundary.left, boundary.top).toSMCLPos
    val smclContent = this.smclPic.crop(ulCorner, boundary.width atLeast 0, boundary.height atLeast 0).toPicture
    val smclContentWithViewport =
      if (this.smclPic.hasViewport) smclContent.setViewportToContentBoundary(name = None) else smclContent
    Pic(smclContentWithViewport, this.anchor, Transform("crop") :: this.historyDetails)
  }


  /** Returns a partial `Pic` of a rectangular area within this `Pic`.
    * @param topLeft      the top-left corner of the cropping frame within this `Pic`
    * @param bottomRight  the bottom-right corner of the cropping frame within this `Pic`
    * @return a new `Pic` (a bitmap) that contains a part of the original */
  @inline def crop(topLeft: Pos, bottomRight: Pos): Pic = {
    val smclContent = this.smclPic.crop(
        upperLeftCornerXInPixels = topLeft.x,
        upperLeftCornerYInPixels = topLeft.y,
        lowerRightCornerXInPixels = bottomRight.x,
        lowerRightCornerYInPixels = bottomRight.y).toPicture
    val smclContentWithViewport =
      if (this.smclPic.hasViewport) smclContent.setViewportToContentBoundary(name = None) else smclContent
    Pic(smclContentWithViewport, this.anchor, Transform("crop") :: this.historyDetails)
  }


  /** Returns a partial `Pic` of a rectangular area within this `Pic`.
    * @param topLeft  the top-left corner of the cropping frame within this `Pic`
    * @param width    the width of the cropping frame
    * @param height   the height of the cropping frame
    * @return a new `Pic` (a bitmap) that contains a part of the original */
  @inline def crop(topLeft: Pos, width: Double, height: Double): Pic = {
    val smclContent = this.smclPic.crop(topLeft.toSMCLPos, width atLeast 0, height atLeast 0).toPicture
    val smclContentWithViewport =
      if (this.smclPic.hasViewport) smclContent.setViewportToContentBoundary(name = None) else smclContent
    Pic(smclContentWithViewport, this.anchor, Transform("crop") :: this.historyDetails)
  }


  /** Returns a partial `Pic` of a rectangular area within this `Pic`.
    * @param x        the x coordinate of left edge of the cropping frame within this `Pic`
    * @param y        the y coordinate of top edge of the cropping frame within this `Pic`
    * @param width    the width of the cropping frame
    * @param height   the height of the cropping frame
    * @return a new `Pic` (a bitmap) that contains a part of the original */
  @inline def crop(x: Double, y: Double, width: Double, height: Double): Pic = {
    val ulCorner = Pos(x, y).toSMCLPos
    val smclContent = this.smclPic.crop(ulCorner, width atLeast 0, height atLeast 0).toPicture
    val smclContentWithViewport =
      if (this.smclPic.hasViewport) smclContent.setViewportToContentBoundary(name = None) else smclContent
    Pic(smclContentWithViewport, this.anchor, Transform("crop") :: this.historyDetails)
  }


  /** Creates a version of this `Pic` that is larger or smaller as per the given multiplier.
    * A value between 0 and 1 produces a smaller `Pic`, a value above 1 a larger one.
    * Retains the aspect ratio of the original image.
    * @param factor  the ratio between the sizes of the result and the original
    * @return a new `Pic` that is a scaled version of this one; it will usually be
    *         a bitmap, but some simple vector-based `Pic`s may retain their vector form */
  @inline def scaleBy(factor: Double): Pic = {
    val smclContent = this.smclPic.scaleBy(factor)
    Pic(smclContent, this.anchor, Transform("scaleBy") :: this.historyDetails)
  }


  /** Creates a scaled version of this `Pic` that has the given dimensions. The aspect ratio
    * of the resulting image may be different from the original’s.
    * @param targetWidth   the width of the output image
    * @param targetHeight  the height of the output image
    * @return a new `Pic` that is a scaled version of this one; it will usually be
    *         a bitmap, but some simple vector-based `Pic`s may retain their vector form */
  @inline def scaleTo(targetWidth: Double, targetHeight: Double): Pic = {
    val smclContent = this.smclPic.scaleTo(targetWidth, targetHeight)
    Pic(smclContent, this.anchor, Transform("scaleTo") :: this.historyDetails)
  }


  /** Creates a scaled version of this `Pic` that has the given size and is rectangular. If
    * the original `Pic` isn’t rectangular, the aspect ratio of the result will be different.
    * @param targetSize  the width and height of the output image
    * @return a new `Pic` that is a scaled version of this one; it will usually be
    *         a bitmap, but some simple vector-based `Pic`s may retain their vector form */
  @inline def scaleTo(targetSize: Double): Pic = {
    val smclContent = this.smclPic.scaleTo(targetSize, targetSize)
    Pic(smclContent, this.anchor, Transform("scaleTo") :: this.historyDetails)
  }


  /** Creates a scaled version of this `Pic` that has the dimensions of the given `Pic`.
    * The aspect ratio of the resulting image may be different from the original’s.
    * @param sizingPic  a `Pic` whose width and height are given to the scaled `Pic`
    * @return a new `Pic` that is a scaled version of this one; it will usually be
    *         a bitmap, but some simple vector-based `Pic`s may retain their vector form */
  @inline def scaleTo(sizingPic: Pic): Pic = this.scaleTo(sizingPic.width, sizingPic.height)


  /** Creates mirrored version of this `Pic`: what’s on the left in the original is on the right
    * in the resulting `Pic` and vice versa.
    * @return a new `Pic` (a bitmap) that is a flipped version of this one */
  @inline def flipHorizontal: Pic = {
    val smclContent = this.toSMCLBitmap.flipHorizontally.toPicture
    val smclContentWithViewport = this.smclPic.viewport.map(smclContent.setViewport).getOrElse(smclContent)
    Pic(smclContentWithViewport, this.anchor, Transform("flipHorizontal") :: this.historyDetails)
  }


  /** Creates mirrored version of this `Pic`: what’s at the top in the original is at the bottom
    * in the resulting `Pic` and vice versa.
    * @return a new `Pic` (a bitmap) that is a flipped version of this one */
  @inline def flipVertical: Pic = {
    val smclContent = this.toSMCLBitmap.flipVertically.toPicture
    val smclContentWithViewport = this.smclPic.viewport.map(smclContent.setViewport).getOrElse(smclContent)
    Pic(smclContentWithViewport, this.anchor, Transform("flipVertical") :: this.historyDetails)
  }


  /** Creates mirrored version of this `Pic`: what’s in each corner of the original is in the
    * opposite corner of the resulting `Pic`.
    * @return a new `Pic` (a bitmap) that is a flipped version of this one */
  @inline def flipDiagonal: Pic = {
    val smclContent = this.toSMCLBitmap.flipDiagonally.toPicture
    val smclContentWithViewport = this.smclPic.viewport.map(smclContent.setViewport).getOrElse(smclContent)
    Pic(smclContentWithViewport, this.anchor, Transform("flipDiagonal") :: this.historyDetails)
  }


  /** Creates version of this `Pic` that’s rotated clockwise around its center. The resulting image
    * is sized so that the entire contents of the original `Pic` are visible; any empty space in the
    * corners is [[Transparent]].
    * @param  degrees the amount of clockwise rotation, in degrees
    * @return a new `Pic` that is a rotated version of this one; it will usually be a
    *         bitmap, but some simple vector-based `Pic`s may retain their vector form */
  @inline def clockwise(degrees: Double = 90.0): Pic = {
    val smclContent =
      if (degrees == 90.0) this.smclPic.rotateBy90DegsCW(centerOfRotation)
      else                 this.smclPic.rotateBy(-degrees, centerOfRotation)
    Pic(Some(smclContent), this.anchor, Transform("clockwise") :: this.historyDetails)
  }


  /** Creates version of this `Pic` that’s rotated counterclockwise around its center. The resulting
    * image is sized so that the entire contents of the original `Pic` are visible; any empty space
    * in the corners is [[Transparent]].
    * @param  degrees the amount of counterclockwise rotation, in degrees
    * @return a new `Pic` that is a rotated version of this one; it will usually be a
    *         bitmap, but some simple vector-based `Pic`s may retain their vector form */
  @inline def counterclockwise(degrees: Double = 90.0): Pic = {
    val smclContent =
      if (degrees == 90.0) this.smclPic.rotateBy90DegsCCW(centerOfRotation)
      else                 this.smclPic.rotateBy(degrees, centerOfRotation)
    Pic(Some(smclContent), this.anchor, Transform("counterclockwise") :: this.historyDetails)
  }


  private def centerOfRotation: smcl.modeling.d2.Pos = {
    val anchorLocation = this.anchor.internalPosWithin(this)

    //------------------------------------------------------------
    // 28.6.2016 AL:
    //
    // The code below replaces the following expression:
    //
    //   this.smclPic.boundary.upperLeftCorner + (anchorLocation.x, anchorLocation.y)
    //
    // The expression above caused the following error in Scala IDE 4.7.0 bundle:
    //
    //    error: Error while emitting
    //   assertion failed:
    //     ClassBType for class being compiled was already created from a classfile: scala.Tuple2$mcDD$sp
    //        while compiling: <console>
    //           during phase: jvm
    //        library version: version 2.12.3
    //       compiler version: version 2.12.3
    //   . . .
    //   <Cannot read source file>
    //
    // Root cause for the error is unknown at the moment.
    // Version information of the Scala IDE bundle is below:
    //
    //   Scala IDE build of Eclipse SDK
    //   Build id: 4.7.0-vfinal-2017-09-29T14:34:02Z-Typesafe
    //   Eclipse 4.7.1 (Oxygen)
    //   Scala IDE for Eclipse: 4.7.0.v-2_12-201709291352-71a28d0
    //   Scala 2.12.3
    //   Java 1.8.0
    //
    val ulCorner = this.smclPic.boundary.upperLeftCorner
    val centerX = ulCorner.xInPixels + anchorLocation.x
    val centerY = ulCorner.yInPixels + anchorLocation.y
    smcl.modeling.d2.Pos(centerX, centerY)
  }


  /** Shifts each pixel within the `Pic` to the right by the given amount. Retains the
    * size of the image: any pixels that would go beyond the right edge of the `Pic`
    * instead wrap around to the left-hand side.
    * @param offset  the number of pixels the image shifts to the right
    * @return a new `Pic` (a bitmap) that contains a shifted version of the original */
  @inline def shiftRight(offset: Double): Pic = {
    val normalizedOffset = offset % this.width
    if (normalizedOffset == 0) {
      return this
    }
    val actualOffset = if (normalizedOffset > 0) normalizedOffset else (this.width + normalizedOffset) % this.width
    val leftSMCLContent = this.smclPic.crop(Pos(0, 0).toSMCLPos, this.width - actualOffset, this.height)
    val rightSMCLContent = this.smclPic.crop(Pos(this.width - actualOffset, 0).toSMCLPos, actualOffset, this.height)
    val smclContent = SMCLPicture(leftSMCLContent.addToLeft(content = rightSMCLContent, paddingInPixels = 0, alignment = VAMiddle))
    Pic(this.shiftedContentInViewport(smclContent), this.anchor, Transform("shiftRight") :: this.historyDetails)
  }


  /** Shifts each pixel within the `Pic` to the left by the given amount. Retains the
    * size of the image: any pixels that would go beyond the left edge of the `Pic`
    * instead wrap around to the right-hand side.
    * @param offset  the number of pixels the image shifts to the left
    * @return a new `Pic` (a bitmap) that contains a shifted version of the original */
  @inline def shiftLeft(offset: Double): Pic = {
    val normalizedOffset = offset % this.width
    if (normalizedOffset == 0) {
      return this
    }
    val actualOffset = if (normalizedOffset >= 0) normalizedOffset else (this.width + normalizedOffset) % this.width
    val leftSMCLContent = this.smclPic.crop(Pos(0, 0).toSMCLPos, actualOffset, this.height)
    val rightSMCLContent = this.smclPic.crop(Pos(actualOffset, 0).toSMCLPos, this.width - actualOffset, this.height)
    val smclContent = SMCLPicture(rightSMCLContent.addToRight(content = leftSMCLContent, paddingInPixels = 0, alignment = VAMiddle))
    Pic(this.shiftedContentInViewport(smclContent), this.anchor, Transform("shiftLeft") :: this.historyDetails)
  }


  /** Shifts each pixel within the `Pic` downwards by the given amount. Retains the
    * size of the image: any pixels that would go beyond the bottom edge of the `Pic`
    * instead wrap around to the top.
    * @param offset  the number of pixels the image shifts down
    * @return a new `Pic` (a bitmap) that contains a shifted version of the original */
  @inline def shiftDown(offset: Double): Pic = {
    val normalizedOffset = offset % this.height
    if (normalizedOffset == 0) {
      return this
    }
    val actualOffset = if (normalizedOffset > 0) normalizedOffset else (this.height + normalizedOffset) % this.height
    val topSMCLContent = this.smclPic.crop(Pos(0, 0).toSMCLPos, this.width, this.height - actualOffset)
    val bottomSMCLContent = this.smclPic.crop(Pos(0, this.height - actualOffset).toSMCLPos, this.width, actualOffset)
    val smclContent = SMCLPicture(topSMCLContent.addToTop(content = bottomSMCLContent, paddingInPixels = 0, alignment = HACenter))
    Pic(this.shiftedContentInViewport(smclContent), this.anchor, Transform("shiftDown") :: this.historyDetails)
  }


  /** Shifts each pixel within the `Pic` upwards by the given amount. Retains the
    * size of the image: any pixels that would go beyond the top edge of the `Pic`
    * instead wrap around to the bottom.
    * @param offset  the number of pixels the image shifts up
    * @return a new `Pic` (a bitmap) that contains a shifted version of the original */
  @inline def shiftUp(offset: Double): Pic = {
    val normalizedOffset = offset % this.height
    if (normalizedOffset == 0) {
      return this
    }
    val actualOffset = if (normalizedOffset > 0) normalizedOffset else (this.height + normalizedOffset) % this.height
    val topSMCLContent = this.smclPic.crop(Pos(0, 0).toSMCLPos, this.width, actualOffset)
    val bottomSMCLContent = this.smclPic.crop(Pos(0, actualOffset).toSMCLPos, this.width, this.height - actualOffset)
    val smclContent = SMCLPicture(bottomSMCLContent.addToBottom(content = topSMCLContent, paddingInPixels = 0, alignment = HACenter))
    Pic(this.shiftedContentInViewport(smclContent), this.anchor, Transform("shiftUp") :: this.historyDetails)
  }

  @inline private def shiftedContentInViewport(smclContent: SMCLPicture) =
    if (this.smclPic.hasViewport) smclContent.setViewportToContentBoundary(name = None) else smclContent



  //-----------------------------------------------------------------------------------------------
  //
  //
  // Combining Pictures
  //
  //
  //-----------------------------------------------------------------------------------------------

  /** Combines this `Pic` and the given one so that this `Pic` appears in front of the other
    * `Pic`. The combined `Pic` will be larger than either of the two originals unless this
    * `Pic` fits completely in front of the other one. This version of `onto` uses the given
    * [[Anchor]]s instead of the default `anchor` of either of the original `Pic`s.
    * @param backPic  the `Pic` that is behind this one in the combination
    * @param my       the point within this `Pic` that will be placed at `atIts`
    * @param atIts    the point within `backPic` that this `Pic` is positioned at
    * @return the combined `Pic`
    * @see [[o1.gui.Pic.against(background:o1\.gui\.Pic,my:o1\.gui\.Anchor,atIts:o1\.gui\.Anchor)* against]],
    *      which does the same but leaves out any “hanging” parts of the foreground image */
  @inline def onto(backPic: Pic, my: Anchor, atIts: Anchor): Pic = this.onto(backPic, my, atIts.internalPosWithin(backPic))


  /** Combines this `Pic` and the given one so that this `Pic` appears in front of the other
    * `Pic`. The combined `Pic` will be larger than either of the two originals unless this
    * `Pic` fits completely in front of the other one. This version of `onto` anchors this
    * `Pic` from its default [[anchor]] to an anchor in the other image.
    * @param backPic  the `Pic` that is behind this one in the combination
    * @param atIts    the point within `backPic` that this `Pic`’s [[anchor]] is positioned at
    * @return the combined `Pic`
    * @see [[o1.gui.Pic.against(background:o1\.gui\.Pic,atIts:o1\.gui\.Anchor)* against]],
    *      which does the same but leaves out any “hanging” parts of the foreground image */
  @inline def onto(backPic: Pic, atIts: Anchor = Center): Pic = this.onto(backPic, anchor, atIts)


  /** Combines this `Pic` and the given one so that this `Pic` appears in front of the other
    * `Pic` at specific coordinates. The combined `Pic` will be larger than either of the two
    * originals unless this `Pic` fits completely in front of the other one. This version of
    * `onto` anchors this `Pic` at its default [[anchor]].
    * @param backPic  the `Pic` that is behind this one in the combination
    * @param at       the point within `backPic` that this `Pic`’s [[anchor]] is positioned at
    * @return the combined `Pic`
    * @see [[o1.gui.Pic.against(background:o1\.gui\.Pic,at:o1\.world\.Pos)* against]],
    *      which does the same but leaves out any “hanging” parts of the foreground image */
  @inline def onto(backPic: Pic, at: Pos): Pic = this.onto(backPic, anchor, at)


  /** Combines this `Pic` and the given one so that this `Pic` appears in front of the other `Pic`
    * at specific coordinates. The combined `Pic` will be larger than either of the two originals
    * unless this `Pic` fits completely in front of the other one. This version of `onto` uses the
    * given [[Anchor]] instead of this `Pic`s default `anchor`.
    * @param backPic  the `Pic` that is behind this one in the combination
    * @param my       the point within this `Pic` that will be placed at the given coordinates
    * @param at       the point within `backPic` that this `Pic` is positioned at
    * @return the combined `Pic`
    * @see [[o1.gui.Pic.against(background:o1\.gui\.Pic,my:o1\.gui\.Anchor,at:o1\.world\.Pos)* against]],
    *      which does the same but leaves out any “hanging” parts of the foreground image */
  @inline def onto(backPic: Pic, my: Anchor, at: Pos): Pic = this.onto(backPic, my, at, newViewport = Viewport.NotSet)


  @inline private def onto(backPic: Pic, my: Anchor, at: Pos, newViewport: Viewport): Pic = {
    val bkgUpperLeftCorner = backPic.boundary.pos
    val absoluteAtPosOnBkg = bkgUpperLeftCorner.add(at.x, at.y)

    val myInternalPos = my.internalPosWithin(this)
    val xAbsolute = absoluteAtPosOnBkg.x - myInternalPos.x
    val yAbsolute = absoluteAtPosOnBkg.y - myInternalPos.y

    val newSMCLContent = backPic.smclPic.addAt(this.smclPic, xAbsolute, yAbsolute, PosTypeUpperLeftCorner).toPicture

    val possibleNewViewport = Option(newViewport).flatMap( _.toSMCLViewport )
    val newSMCLContentWithViewport = possibleNewViewport.map(newSMCLContent.setViewport).getOrElse(newSMCLContent.removeViewport)

    Pic(newSMCLContentWithViewport, backPic.anchor, historyForCombinedPicture)
  }


  /** Combines this `Pic` and the given one so that the other `Pic` serves as a background and this
    * `Pic` appears in front of it but not beyond its borders. The visible part of the combined `Pic`
    * is the size of the background `Pic`. In the combination, some or even none of this `Pic` is visible
    * in front of the background, depending on the relative positioning and dimensions of the two images;
    * any parts of this `Pic` that don’t fit against the background are left out. (The left-out parts are
    * still part of the `Pic`’s data but not visible; only the part within the framing background image
    * will be shown when the `Pic` is rendered onscreen.) This version of `against` anchors this `Pic`
    * at its default [[anchor]] to a specific [[Pos]] in the background.
    * @param background  a `Pic` that serves as a background for this one and determines the size of
    *                    the visible part of the resulting image
    * @param at          the point within `background` that this `Pic`’s [[anchor]] is positioned at
    * @return the combined `Pic`
    * @see [[o1.gui.Pic.onto(backPic:o1\.gui\.Pic,at:o1\.world\.Pos)* onto]],
    *      which does the same but doesn’t leave out the “hanging” parts of the foreground image
    * @see [[o1.gui.Pic.place(foreground:o1\.gui\.Pic,at:o1\.world\.Pos)* place]],
    *      which does the same but switches the order of the two `Pic`s */
  @inline def against(background: Pic, at: Pos): Pic = this.against(background, anchor, at)


  /** Combines this `Pic` and the given one so that the other `Pic` serves as a background and this
    * `Pic` appears in front of it but not beyond its borders. The visible part of the combined `Pic`
    * is the size of the background `Pic`. In the combination, some or even none of this `Pic` is visible
    * in front of the background, depending on the relative positioning and dimensions of the two images;
    * any parts of this `Pic` that don’t fit against the background are left out. (The left-out parts are
    * still part of the `Pic`’s data but not visible; only the part within the framing background image
    * will be shown when the `Pic` is rendered onscreen.) This version of `against` uses the given [[Anchor]]
    * instead of this `Pic`s default `anchor` and places it at a specific [[Pos]] in the background.
    * @param background  a `Pic` that serves as a background for this one and determines the size of
    *                    the visible part of the resulting image
    * @param my          the point within this `Pic` that will be placed at the given coordinates
    * @param at          the point within `background` that this `Pic` is positioned at
    * @return the combined `Pic`
    * @see [[o1.gui.Pic.onto(backPic:o1\.gui\.Pic,my:o1\.gui\.Anchor,at:o1\.world\.Pos)* onto]]
    *      which does the same but doesn’t leave out the “hanging” parts of the foreground image
    * @see [[o1.gui.Pic.place(foreground:o1\.gui\.Pic,its:o1\.gui\.Anchor,at:o1\.world\.Pos)* place]]
    *      which does the same but switches the order of the two `Pic`s */
  @inline def against(background: Pic, my: Anchor, at: Pos): Pic = {
    val newViewport = background.smclPic.viewport.map( _.toO1Viewport ).getOrElse(Viewport(background.boundary))
    this.onto(background, my, at, newViewport)
  }


  /** Combines this `Pic` and the given one so that the other `Pic` serves as a background and this
    * `Pic` appears in front of it but not beyond its borders. The visible part of the combined `Pic`
    * is the size of the background `Pic`. In the combination, some or even none of this `Pic` is visible
    * in front of the background, depending on the relative positioning and dimensions of the two images;
    * any parts of this `Pic` that don’t fit against the background are left out. (The left-out parts are
    * still part of the `Pic`’s data but not visible; only the part within the framing background image will
    * be shown when the `Pic` is rendered onscreen.) This version of `against` uses the given [[Anchor]]s
    * instead of the default `anchor` of either of the original `Pic`s.
    * @param background  a `Pic` that serves as a background for this one and determines the size of
    *                    the visible part of the resulting image
    * @param my          the point within this `Pic` that will be placed at the given coordinates
    * @param atIts       the point within `background` that this `Pic` is positioned at
    * @return the combined `Pic`
    * @see [[o1.gui.Pic.onto(backPic:o1\.gui\.Pic,my:o1\.gui\.Anchor,atIts:o1\.gui\.Anchor)* onto]]
    *      which does the same but doesn’t leave out the “hanging” parts of the foreground image
    * @see [[o1.gui.Pic.place(foreground:o1\.gui\.Pic,its:o1\.gui\.Anchor,atMy:o1\.gui\.Anchor)* place]],
    *      which does the same but switches the order of the two `Pic`s */
  @inline def against(background: Pic, my: Anchor, atIts: Anchor): Pic = this.against(background, my, atIts.internalPosWithin(background))


  /** Combines this `Pic` and the given one so that the other `Pic` serves as a background and this
    * `Pic` appears in front of it but not beyond its borders. The visible part of the combined `Pic`
    * is the size of the background `Pic`. In the combination, some or even none of this `Pic` is visible
    * in front of the background, depending on the relative positioning and dimensions of the two images;
    * any parts of this `Pic` that don’t fit against the background are left out. (The left-out parts are
    * still part of the `Pic`’s data but not visible; only the part within the framing background image will
    * be shown when the `Pic` is rendered onscreen.) This version of `against` anchors this `Pic` at its
    * default [[anchor]] to an anchor in the background.
    * @param background  a `Pic` that serves as a background for this one and determines the size of
    *                    the visible part of the resulting image
    * @param atIts       the point within `background` that this `Pic`’s [[anchor]] is positioned at
    * @return the combined `Pic`
    * @see [[o1.gui.Pic.onto(backPic:o1\.gui\.Pic,atIts:o1\.gui\.Anchor)* onto]]
    *      which does the same but doesn’t leave out the “hanging” parts of the foreground image
    * @see [[o1.gui.Pic.place(foreground:o1\.gui\.Pic,atMy:o1\.gui\.Anchor)* place]],
    *      which does the same but switches the order of the two `Pic`s */
  @inline def against(background: Pic, atIts: Anchor = Center): Pic = this.against(background, anchor, atIts)


  /** Combines this `Pic` and the given one so that this `Pic` serves as a background and the other
    * `Pic` appears in front but not beyond this `Pic`’s borders. The visible part of the combined `Pic`
    * is the size of this background `Pic`. Some or even none of the other `Pic` is visible in front of
    * this background, depending on the relative positioning and dimensions of the two images; any parts of
    * the other `Pic` that don’t fit against this background are left out. (The left-out parts are still
    * part of this `Pic`’s data but not visible; only the part within this framing background image will be
    * shown when the `Pic` is rendered onscreen.) This version of `place` uses the given [[Anchor]] instead
    * of the other `Pic`s default `anchor` and places it at a specific [[Pos]] in this background.
    * @param foreground  the `Pic` that will be placed against this background
    * @param its         the point within `foreground` that will be placed at the given coordinates
    * @param at          the point within this `Pic` that `foreground` will be placed at
    * @return the combined `Pic`
    * @see [[o1.gui.Pic.against(background:o1\.gui\.Pic,my:o1\.gui\.Anchor,at:o1\.world\.Pos)* place]]
    *      which does the same but switches the order of the two `Pic`s */
  @inline def place(foreground: Pic, its: Anchor, at: Pos): Pic = {
    val foregroundAnchor = its
    foreground.against(this, foregroundAnchor, at)
  }


  /** Combines this `Pic` and the given one so that this `Pic` serves as a background and the other
    * `Pic` appears in front but not beyond this `Pic`’s borders. The visible part of the combined `Pic`
    * is the size of this background `Pic`. Some or even none of the other `Pic` is visible in front of
    * this background, depending on the relative positioning and dimensions of the two images; any parts
    * of the other `Pic` that don’t fit against this background are left out. (The left-out parts are still
    * part of this `Pic`’s data but not visible; only the part within this framing background image will
    * be shown when the `Pic` is rendered onscreen.) This version of `place` uses the given [[Anchor]]s
    * instead of the default `anchor` of either of the original `Pic`s.
    * @param foreground  the `Pic` that will be placed against this background
    * @param its         the point within `foreground` that will be placed at the given coordinates
    * @param atMy        the point within this `Pic` that `foreground` will be placed at
    * @return the combined `Pic`
    * @see [[o1.gui.Pic.against(background:o1\.gui\.Pic,my:o1\.gui\.Anchor,atIts:o1\.gui\.Anchor)* place]],
    *      which does the same but switches the order of the two `Pic`s */
  @inline def place(foreground: Pic, its: Anchor, atMy: Anchor): Pic = foreground.against(this, its, atMy.internalPosWithin(this))


  /** Combines this `Pic` and the given one so that this `Pic` serves as a background and the other
    * `Pic` appears in front but not beyond this `Pic`’s borders. The visible part of the combined `Pic`
    * is the size of this background `Pic`. Some or even none of the other `Pic` is visible in front of
    * this background, depending on the relative positioning and dimensions of the two images; any parts
    * of the other `Pic` that don’t fit against this background are left out. (The left-out parts are still
    * part of this `Pic`’s data but not visible; only the part within this framing background image will
    * be shown when the `Pic` is rendered onscreen.) This version of `place` anchors the other `Pic` at
    * its default [[anchor]] to a specific [[Pos]] in this background.
    * @param foreground  the `Pic` that will be placed against this background
    * @param at          the point within this `Pic` that `foreground`’s anchor will be placed at
    * @return the combined `Pic`
    * @see [[o1.gui.Pic.against(background:o1\.gui\.Pic,at:o1\.world\.Pos)* place]],
    *      which does the same but switches the order of the two `Pic`s */
  @inline def place(foreground: Pic, at: Pos): Pic = this.place(foreground, foreground.anchor, at)


  /** Combines this `Pic` and the given one so that this `Pic` serves as a background and the other
    * `Pic` appears in front but not beyond this `Pic`’s borders. The visible part of the combined `Pic`
    * is the size of this background `Pic`. Some or even none of the other `Pic` is visible in front of
    * this background, depending on the relative positioning and dimensions of the two images; any parts
    * of the other `Pic` that don’t fit against this background are left out. (The left-out parts are still
    * part of this `Pic`’s data but not visible; only the part within this framing background image will
    * be shown when the `Pic` is rendered onscreen.) This version of `place` anchors the other `Pic` at
    * its default [[anchor]] to a specified anchor in this background.
    * @param foreground  the Pic` that will be placed against this background
    * @param atMy        the point within this `Pic` that `foreground`’s anchor will be placed at
    * @return the combined `Pic`
    * @see [[o1.gui.Pic.against(background:o1\.gui\.Pic,atIts:o1\.gui\.Anchor)* place]],
    *      which does the same but switches the order of the two `Pic`s */
  @inline def place(foreground: Pic, atMy: Anchor): Pic = this.place(foreground, foreground.anchor, atMy)


  /** Combines this `Pic` and the given one so that this `Pic` serves as a background and the other
    * `Pic` appears in front but not beyond this `Pic`’s borders. The visible part of the combined `Pic`
    * is the size of this background `Pic`. Some or even none of the other `Pic` is visible in front of
    * this background, depending on the relative positioning and dimensions of the two images; any parts
    * of the other `Pic` that don’t fit against this background are left out. (The left-out parts are still
    * part of this `Pic`’s data but not visible; only the part within this framing background image will
    * be shown when the `Pic` is rendered onscreen.) This version of `place` anchors the other `Pic` at its
    * default [[anchor]] to a specific [[Pos]] in this background.
    * @param foregroundPicAndPos  a tuple containing: the `Pic` that will be placed against this background
    *                             and the point within this `Pic` that `foreground`’s anchor will be placed at
    * @return the combined `Pic`
    * @see [[o1.gui.Pic.place(foreground:o1\.gui\.Pic,at:o1\.world\.Pos)* place]],
    *      which does the same but without the tuple */
  @inline def place(foregroundPicAndPos: (Pic, Pos)): Pic = this.place(foregroundPicAndPos._1, foregroundPicAndPos._2)


  /** Combines this `Pic` and the given ones so that this `Pic` serves as a background and the
    * other `Pic`s appear in front but not beyond this `Pic`’s borders. The visible part of the
    * combined `Pic` is the size of this background `Pic`. This is equivalent to calling
    * [[o1.gui.Pic.place(foreground:o1\.gui\.Pic,at:o1\.world\.Pos)* place(Pic,Pos)]] repeatedly.
    * @param foregroundPics  tuples containing: the `Pic`s that will be placed against this background
    *                        and the points within this `Pic` where their default [[anchor]]s will be placed
    * @return the combined `Pic` */
  @inline def place(foregroundPics: Iterable[(Pic, Pos)]): Pic = foregroundPics.foldLeft(this)( _.place(_) )


  /** Combines this `Pic` and the given ones so that this `Pic` serves as a background and the
    * other `Pic`s appear in front but not beyond this `Pic`’s borders. The visible part of the
    * combined `Pic` is the size of this background `Pic`. This is equivalent to calling
    * [[o1.gui.Pic.place(foreground:o1\.gui\.Pic,at:o1\.world\.Pos)* place(Pic,Pos)]] repeatedly.
    * @param foregroundPics  tuples containing: the `Pic`s that will be placed against this background
    *                        and the points within this `Pic` where their default [[anchor]]s will be placed
    * @return the combined `Pic` */
  @inline def place(foregroundPics: (Pic, Pos)*): Pic = this.place(foregroundPics)


  /** Combines this `Pic` and copies of the given one so that this `Pic` serves as a background and the copies
    * of the other `Pic`s appear in front but not beyond this `Pic`’s borders. The visible part of the combined
    * `Pic` is the size of this background `Pic`. This is equivalent calling [[o1.gui.Pic.place(foreground:o1\.gui\.Pic,at:o1\.world\.Pos)* place(Pic,Pos)]]
    * repeatedly, passing in the same foreground image each time.
    * @param foregroundPic  the `Pic`s whose copies will be placed against this background
    * @param at             the points within this `Pic` where the default [[anchor]]s of the copies should be placed
    * @return the combined `Pic` */
  @inline def placeCopies(foregroundPic: Pic, at: Iterable[Pos]): Pic = this.place(at.map( _ => foregroundPic ) zip at)


  /** Combines this `Pic` and the given one so that this `Pic` appears immediately to the left
    * the other `Pic`. The combined `Pic` will be large enough to fit both originals.
    * @param rightPic      the `Pic` that is to the right of this `Pic` in the combination
    * @param retainAnchor  `true` means that the [[anchor]] of the combination should be equally far from the
    *                      top left-hand corner as this `Pic`’s [[anchor]] is; `false` (the default) means that
    *                      the combination should follow the same general anchoring policy has this `Pic` (e.g.,
    *                      to anchor from its center)
    * @return the combined `Pic`
    * @see [[above]], [[below]], [[rightOf]], [[rowOf]] */
  @inline def leftOf(rightPic: Pic, retainAnchor: Boolean = false): Pic = {
    val newSMCLContent = rightPic.smclPic.addToLeft(content = this.smclPic, paddingInPixels = 0, alignment = VAMiddle)
    val newAnchor = if (retainAnchor) this.absoluteAnchor else this.anchor
    Pic(newSMCLContent, newAnchor, historyForCombinedPicture)
  }


  /** Combines this `Pic` and the given one so that this `Pic` appears immediately to the right
    * the other `Pic`. The combined `Pic` will be large enough to fit both originals.
    * @param leftPic       the `Pic` that is to the left of this `Pic` in the combination
    * @param retainAnchor  `true` means that the [[anchor]] of the combination should be equally far from the
    *                      top left-hand corner as this `Pic`’s [[anchor]] is; `false` (the default) means that
    *                      the combination should follow the same general anchoring policy has this `Pic` (e.g.,
    *                      to anchor from its center)
    * @return the combined `Pic`
    * @see [[above]], [[below]], [[leftOf]], [[rowOf]] */
  @inline def rightOf(leftPic: Pic, retainAnchor: Boolean = false): Pic = {
    val newSMCLContent = leftPic.smclPic.addToRight(content = this.smclPic, paddingInPixels = 0, alignment = VAMiddle)
    val newAnchor = if (retainAnchor) this.absoluteAnchor else this.anchor
    Pic(newSMCLContent, newAnchor, historyForCombinedPicture)
  }


  /** Combines this `Pic` and the given one so that this `Pic` appears immediately below the other `Pic`
    * The combined `Pic` will be large enough to fit both originals.
    * @param abovePic      the `Pic` that is above this `Pic` in the combination
    * @param retainAnchor  `true` means that the [[anchor]] of the combination should be equally far from the
    *                      top left-hand corner as this `Pic`’s [[anchor]] is; `false` (the default) means that
    *                      the combination should follow the same general anchoring policy has this `Pic` (e.g.,
    *                      to anchor from its center)
    * @return the combined `Pic`
    * @see [[above]], [[leftOf]], [[rightOf]], [[columnOf]] */
  @inline def below(abovePic: Pic, retainAnchor: Boolean = false): Pic = {
    val newSMCLContent = abovePic.smclPic.addToBottom(content = this.smclPic, paddingInPixels = 0, alignment = HACenter)
    val newAnchor = if (retainAnchor) this.absoluteAnchor else this.anchor
    Pic(newSMCLContent, newAnchor, historyForCombinedPicture)
  }


  /** Combines this `Pic` and the given one so that this `Pic` appears immediately above the other `Pic`
    * The combined `Pic` will be large enough to fit both originals.
    * @param lowerPic      the `Pic` that is below this `Pic` in the combination
    * @param retainAnchor  `true` means that the [[anchor]] of the combination should be equally far from the
    *                      top left-hand corner as this `Pic`’s [[anchor]] is; `false` (the default) means that
    *                      the combination should follow the same general anchoring policy has this `Pic` (e.g.,
    *                      to anchor from its center)
    * @return the combined `Pic`
    * @see [[below]], [[leftOf]], [[rightOf]], [[columnOf]] */
  @inline def above(lowerPic: Pic, retainAnchor: Boolean = false): Pic = {
    val newSMCLContent = lowerPic.smclPic.addToTop(content = this.smclPic, paddingInPixels = 0, alignment = HACenter)
    val newAnchor = if (retainAnchor) this.absoluteAnchor else this.anchor
    Pic(newSMCLContent, newAnchor, historyForCombinedPicture)
  }


  /** Combines copies of this `Pic` so that they appear in a horizontal row.
    * The combined `Pic` will be large enough to fit all the copies.
    * @param number  the number of copies in the row (e.g., 3 means the result has three identical images)
    * @see [[leftOf]], [[rightOf]], [[columnOf]], [[alternatingRow]] */
  @inline def rowOf(number: Int): Pic =
    if (number < 0) {
      throw new IllegalArgumentException("Cannot form a row of negative size.")
    } else if (number == 0) {
      Pic(None, this.anchor, historyForCombinedPicture)
    } else {
      val smclContent = this.smclPic.replicateRightwards(
          numberOfReplicas = number - 1, paddingInPixels = 0, alignment = VAMiddle, transformer = SMCLPictureElement.IdentitySimpleTransformer)
      Pic(smclContent, this.anchor, historyForCombinedPicture)
    }


  /** Combines copies of this `Pic` so that they appear in a vertical column.
    * The combined `Pic` will be large enough to fit all the copies.
    * @param number  the number of copies in the column (e.g., 3 means the result has three identical images)
    * @see [[above]], [[below]], [[rowOf]], [[alternatingColumn]] */
  @inline def columnOf(number: Int): Pic =
    if (number < 0) {
      throw new IllegalArgumentException("Cannot form a column of negative size.")
    } else if (number == 0) {
      Pic(None, this.anchor, historyForCombinedPicture)
    } else {
      val smclContent = this.smclPic.replicateDownwards(
          numberOfReplicas = number - 1, paddingInPixels = 0, alignment = HACenter, transformer = SMCLPictureElement.IdentitySimpleTransformer)
      Pic(smclContent, this.anchor, historyForCombinedPicture)
    }


  /** Combines copies of this `Pic` and another `Pic` so that they alternate in a horizontal row
    * (starting with this `Pic`). The combined `Pic` will be large enough to fit all the copies.
    * @param number  the total number of `Pic`\s in the row (e.g., 6 means the result has three copies of each `Pic`)
    * @see [[leftOf]], [[rightOf]], [[rowOf]], [[alternatingColumn]] */
  @inline def alternatingRow(another: Pic, number: Int): Pic =
    if (number < 0) {
      throw new IllegalArgumentException("Cannot form a row of negative size.")
    } else if (number == 0) {
      Pic(None, this.anchor, historyForCombinedPicture)
    } else {
      val smclContent = this.smclPic.alternateRightwardsWith(
          alternatives = Seq(another.smclPic), numberOfAlternations = number - 1, paddingInPixels = 0, alignment = VAMiddle)
      Pic(smclContent, this.anchor, historyForCombinedPicture)
    }


  /** Combines copies of this `Pic` and another `Pic` so that they alternate in a vertical column
    * (starting with this `Pic`). The combined `Pic` will be large enough to fit all the copies.
    * @param number  the total number of `Pic`\s in the column (e.g., 6 means the result has three copies of each `Pic`)
    * @see [[above]], [[below]], [[columnOf]], [[alternatingRow]] */
  @inline def alternatingColumn(another: Pic, number: Int): Pic =
    if (number < 0) {
      throw new IllegalArgumentException("Cannot form a column of negative size.")
    } else if (number == 0) {
      Pic(None, this.anchor, historyForCombinedPicture)
    } else {
      val smclContent = this.smclPic.alternateDownwardsWith(
          alternatives = Seq(another.smclPic), numberOfAlternations = number - 1, paddingInPixels = 0, alignment = HACenter)
      Pic(smclContent, this.anchor, historyForCombinedPicture)
    }


  /** Creates a new `Pic` by combining this `Pic` with the given one on a pixel-by-pixel basis.
    * For each pair of pixel coordinates in the two original `Pic`s, `combine` calls the given
    * function to compute the color of the corresponding pixel in the new `Pic`. The new `Pic`s
    * width equals the lesser of the two originals’ widths; the same goes for height.
    * @param determinePixel  a function generates a pixel for the combination, given two pixel
    *                        colors from the same location in the originals
    * @return the new (bitmap) image obtained by calling `determinePixel` and putting together the outputs */
  @inline def combine(another: Pic, determinePixel: (Color, Color) => Color): Pic = {
    def callPasser(a: SMCLColor, b: SMCLColor): SMCLColor = determinePixel(Color(a), Color(b)).smclColor
    val smclContent = toSMCLBitmap.mergePixelsWith(another.smclPic, callPasser)
    Pic(smclContent, this.anchor, historyForCombinedPicture)
  }


  private def historyForCombinedPicture: PicHistory = {
    val creation = Create(method = "<combining method>", simpleDescription = "combined pic")
    this.historyDetails.copy(creation, List())
  }


}
