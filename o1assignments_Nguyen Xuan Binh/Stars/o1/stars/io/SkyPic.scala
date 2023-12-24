package o1.stars.io

import o1._
import o1.stars._


/** This singleton object provides utilities for creating and manipulating images that
  * represent views of a night sky. */
object SkyPic {

  /** Given an image of the sky and a star, returns a version of the original image with an
    * image of the star placed on top. That is, forms an image of the star and places it
    * against the given (larger) image in a `Pos` that corresponds to the star's [[StarCoords]].
    *
    * The star is depicted as a `White` circle whose diameter is `12.0 / (M + 2)`,
    * where `M` is the star’s magnitude. Its position within the resulting image is
    * determined by [[Star.posIn]]. The given star must have a magnitude greater than -2.
    *
    * For example, say the background image is 400 by 400 pixels, and the given star has
    * the coords (0.5,0.0) and a magnitude of 0.5. The returned image will then consist of
    * the given background image with white circle of radius 4 placed upon it at (300,200).
    *
    * @param skyPic  an image to place the star upon
    * @param star    a star (of magnitude > -2) that should be depicted against the given image */
  def placeStar(skyPic: Pic, star: Star): Pic = {
     skyPic.place(circle(12/(star.magnitude+2),White), star.posIn(skyPic))
  }


  /** Given a [[StarMap]] that details what is visible in the sky, produces a [[Pic]]
    * that represents that information as an image. The background of the image is a
    * `Black` square of the given size. Each star and constellation in the sky appear
    * against that background.
    *
    * @param skyData  the contents of the night sky that are to be represented as an image
    * @param bgSize   the width and height, in pixels, of the desired square image */
  def create(skyData: StarMap, bgSize: Int) = {
    val darkSky = rectangle(bgSize, bgSize, Black)
    var y = skyData.stars.foldLeft(darkSky)(placeStar)
    skyData.constellations.foldLeft(y)(placeConstellation)
  }
  // Given a StarMap that details what is visible in the sky, produces a Pic that represents that information as an image.
  // The background of the image is a Black square of the given size. Each star and constellation in the sky appear
  // against that background.

  private def placeDots(skyPic: Pic, starDot: Star):Pic = {
     skyPic.place(circle(4,Red),starDot.posIn(skyPic))
  }

  private def placeLines(skyPic: Pic, starLine: (Star, Star)):Pic = {
     skyPic.place(line(starLine._1.posIn(skyPic), starLine._2.posIn(skyPic), Yellow),starLine._1.posIn(skyPic))
  }

  def placeConstellation(skyPic: o1.Pic, constellation: Constellation): o1.Pic = {
    var y = constellation.stars.foldLeft(skyPic)(placeDots)
    var x = constellation.lines.toSet.foldLeft(y)(placeLines)
        x
     // skyPic.place(constellation.lines.line(new Pos(0, 0), new Pos(150, 100), Yellow)
    //myBackground.place(pic1 -> pos1, pic2 -> pos2, pic3 -> pos3)
  }
   // Given an image of the sky and a star, returns a version of the original image with a constellation drawn on top.
  // A constellation is depicted using a combination of red circles and yellow lines.
  // At each star that belongs to the constellation, a red circle with a diameter of 4 pixels is added.
  // A yellow line is then drawn between each pair of connected stars.
  // The positions of the stars within the image are determined as in placeStar.


   // Implement placeConstellation on SkyPic.
  // Edit SkyPic’s create method so that it adds the given StarMap’s constellations to


  // Recommended steps and other hints

  // Implement SkyPic.placeConstellation.
  //
  // You can do this by calling the place method of class Pic that you know from earlier assignments.
  // Or if you prefer, you can use a version of place that takes in any number of pairs as illustrated here:
  //
  // myBackground.place(pic1 -> pos1, pic2 -> pos2, pic3 -> pos3)
  // The line function for forming a Pic of a line previously came up in Chapter 3.1’s optional material.
  // It’s easy to use; here’s a simple example:
  //
  // val myLine = line(new Pos(0, 0), new Pos(150, 100), Red)
  // myLine: Pic = line-shape
  // For a neat implementation, try using foldLeft (Chapter 6.4) to compose the final image. Or just go with
  // a for loop, if you prefer.
  //
  // Edit SkyPic.create so that it adds the constellations.
  //
  // The constellations should now show up when you run the app.
  //
  // If they don’t, check that you’ve selected "northern", not ´"test", in `:file:`StarryApp.scala.
}