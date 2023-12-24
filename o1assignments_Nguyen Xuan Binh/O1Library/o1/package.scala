
/** '''O1Library''' is a toolkit designed for the course [[https://plus.cs.aalto.fi/o1/
 *  Programming 1 (a.k.a. O1)]] at Aalto University. It contains an assortment of tools;
 *  most prominently, it provides a framework for simple graphical programming and utilities
 *  for playing sound.
  *
  * This is the front page of O1Library’s documentation. However, this is probably not the best
  * place to start learning about O1Library as a student. That’s because the relevant content
  * of this library is introduced bit by bit in the chapters of O1’s custom ebook alongside the
  * associated programming concepts and assignments.
  *
  * You may still find this documentation useful as a reference. You can also find some
  * optional content here that you may wish to try.
  *
  * This front page lists the content available in the top-level package called simply `o1`.
  * These tools are available with the simple command `import o1._` in your Scala programs.
  * Some of them you’ll use a lot; some of them you won’t necessarily need at all.
  *
  * The tools listed here are actually implemented in a number of subpackages (`o1.gui`,
  * `o1.sound`, etc.); what you see here are just “shortcut aliases” to those actual
  * implementations. The aliases are here to make that convenient `import` command work and to
  * provide you with this list of links to some of the more commonly used tools in O1Library.
  * The subpackages also contain additional content not listed here.
  *
  * O1Library has been developed by Aleksi Lukkarinen and Juha Sorva. Several
  * of the key components in `o1.gui` and `o1.world` are built upon Aleksi’s
  * [[https://github.com/Aalto-LeTech/Scala-Media-Computation Scala Media Computation Library]].
  * Some parts of O1Library draw inspiration from the “teachpacks” of the Racket
  * programming language.
  *
  * We are grateful to Riku Autio, Joonatan Honkamaa, Juhani Numminen, Leo Varis, Veera Kahva,
  * and anonymous students for bug reports and fixes. We thank Otto Seppälä for helpful discussions.
  *
  * @author Juha Sorva (juha.sorva@aalto.fi)
  * @author Aleksi Lukkarinen (aleksi.lukkarinen@aalto.fi) */
package object o1 extends o1.util.ShortcutAliases