package o1

import scala.languageFeature.higherKinds

/** The package `o1.util` contains miscellaneous tools that are used internally by some of the
  * given programs in O1 for added convenience.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this package works
  * or can be used.'''
  *
  * This documentation lists only some of the tools in the package. The listed tools are largely
  * a mix of:
  *
  *  - functions for simple I/O from files and URLs;
  *  - aliases for easy access (via `import o1.util._`)
  *    to some of the tools from `scala.util`; and
  *  - implicit classes that add a few convenience methods
  *    to selected types from the Scala API.  */
package object util extends iofuncs {

  ///////////////////////////////////
  ///// ALIASES FOR CONVENIENCE
  ///////////////////////////////////

  /** An alias for convenient use of `scala.util.Try` via `import o1.util._`. */
  type Try[T] = scala.util.Try[T]
  /** An alias for convenient use of `scala.util.Try` via `import o1.util._`. */
  val Try = scala.util.Try


  /** An alias for convenient use of `scala.util.Try` via `import o1.util._`. */
  type Success[T] = scala.util.Success[T]
  /** An alias for convenient use of `scala.util.Try` via `import o1.util._`. */
  val Success = scala.util.Success


  /** An alias for convenient use of `scala.util.Try` via `import o1.util._`. */
  type Failure[T] = scala.util.Failure[T]
  /** An alias for convenient use of `scala.util.Try` via `import o1.util._`. */
  val Failure = scala.util.Failure


  /** An alias for convenient use of `scala.util.Random` via `import o1.util._`. */
  type Random = scala.util.Random
  /** An alias for convenient use of `scala.util.Random` via `import o1.util._`. */
  val Random = scala.util.Random


  /** An alias for convenient use of `scala.math.Ordering.Double.TotalOrdering` via `import o1.util._`. */
  val DoubleOrdering = scala.math.Ordering.Double.TotalOrdering


  /** An alias for convenient use of `java.net.URL` via `import o1.util._`. */
  type URL = java.net.URL

  /** An alias for convenient use of `java.nio.file.Path` via `import o1.util._`. */
  type Path = java.nio.file.Path

  /** An alias for convenient use of `scala.io.Source` via `import o1.util._`. */
  type Source = scala.io.Source
  /** An alias for convenient use of `scala.io.Source` via `import o1.util._`. */
  val Source = scala.io.Source


  /** An alias for convenient access to the macros in `scala.reflect.runtime.universe` via `import o1.util._`. */
  val reflect = scala.reflect.runtime.universe


  /** An alias for convenient access to `java.lang.System.getProperty("user.dir")` via `import o1.util._`.*/
  def workingDir = System.getProperty("user.dir")


  /////////////////////////////////////////////
  // CONVENIENCE EXTENSIONS FOR API CLASSES
  /////////////////////////////////////////////


  /** This class extends the interface of `scala.Iterable` with convenience methods. */
  implicit class ConvenientCollection[Element, Collection[Element] <: Iterable[Element]](private val self: Collection[Element]) extends AnyVal {

    /** Constructs and returns a new map by applying a key-generating function as well as
      * a value-generating function to each element of the collection. The respective
      * outputs of the functions are paired to form the key--value pairs of the `Map`.
      * @param formKey    a function called on each element of the collection to obtain the keys
      * @param formValue  a function called on each element of the collection to obtain the values */
    def mapify[Key, Value](formKey: Element => Key)(formValue: Element => Value): Map[Key, Value] =
      self.view.map(elem => formKey(elem) -> formValue(elem)).to(Map)


    /** Constructs and returns a new map by applying an ID-generating function to each
      * element of the collection and using the collection’s elements as values. This is
      * equivalent to calling `mapify(formID)(identity)`.
      * @param formID    a function called on each element of the collection to obtain the keys */
    def mapFromID[ID](formID: Element => ID): Map[ID, Element] =
      self.mapify(formID)(identity)


    /** Constructs and returns a new map by applying a value-generating function
      * to each element of the collection and using the collection’s elements as keys.
      * This is equivalent to calling `mapify(identity)(formValue)`.
      * @param formValue  a function called on each element of the collection to obtain the keys */
    def mapTo[Value](formValue: Element => Value): Map[Element, Value] =
      self.mapify(identity)(formValue)


    /** Constructs and returns a new map by using one given function to group the elements of the
      * collection (as per `groupBy`) and then transforming each of the groups using another given
      * function (as per `mapValues`).
      * @param formKey         a function called on each element of the collection to generate groups of values
      * @param transformGroup  a function called on each group to obtain the values for the resulting collection*/
    def mapGroups[Key, Value](formKey: Element => Key)(transformGroup: Iterable[Element] => Value): Map[Key, Value] =
      self.groupBy(formKey).view.mapValues(transformGroup).to(Map)


    /** Constructs and returns a new map with the collection’s elements as keys and each element’s
      * occurrence counts as the corresponding values. This is equivalent to `mapGroups(identity)( _.size )`. */
    def frequencies: Map[Element, Int] = self.mapGroups(identity)( _.size )


    /** Performs a given side effect at each element of the collection. Returns the unmodified collection. */
    def tap(effect: Element => Unit): Collection[Element] = {
      self.foreach(effect)
      self
    }

    /** Prints out the collection, then returns the unmodified collection.
      * @param format  a function from the collection to the desired printout; defaults to `_.toString` */
    def log(format: Iterable[Element] => String = _.toString ): Collection[Element] = {
      println(format(self))
      self
    }

    /** Returns a `LazyList` containing the collection’s elements. This is equivalent to `to(LazyList)`. */
    def toLazy: LazyList[Element] = self.to(LazyList)


  }

  /** This class extends the interface of `scala.Seq` with convenience methods. */
  implicit class ConvenientSeq[Element](private val self: Seq[Element]) extends AnyVal {

    /** Return a random element from the collection. Uses the standard `scala.util.Random` singleton. */
    def randomElement() = {
      val randomIndex = scala.util.Random.nextInt(self.size)
      self(randomIndex)
    }

  }

  /** This class extends the interface of `scala.Map` with convenience methods. */
  implicit class ConvenientMap[Key, Value](private val self: Map[Key, Value]) extends AnyVal {

    /** Constructs and returns a new map by transforming each value with a given function.
      * @param transform the value-transforming function */
    def mapValuesOnly[Result](transform: Value => Result): Map[Key, Result] =
      self.map( keyValue => keyValue._1 -> transform(keyValue._2) )

  }


  /** This class extends the interface of `Option` with convenience methods. */
  implicit class ConvenientOption[Content](private val self: Option[Content]) extends AnyVal {

    /** Performs a given side effect on the `Option`’s contents (if any). Returns the unmodified `Option`. */
    def tap(effect: Content => Unit): Option[Content] = {
      self.foreach(effect)
      self
    }

    /** Prints out a report of the `Option`’s contents (if any). Returns the unmodified `Option`.
      * @param format           a function that takes in a description of an `Option` and produces a report of it; defaults to `identity`
      * @param describeContent  a function that takes in an `Option`’s contents and produces a description of it; defaults to `_.toString`
      * @param describeNone     a description of an empty `Option`; defaults to `"None"` */
    def log(format: String => String = identity, describeContent: Content => String = _.toString, describeNone: =>String = "None"): Option[Content] = {
      if (self.isDefined) {
        this.tap( describeContent andThen format andThen println )
      } else {
        println(format(describeNone))
      }
      self
    }

  }

  /** This class extends the interface of `Int` with convenience methods. */
  implicit class ConvenientInt(private val value: Int) extends AnyVal {
    /** `num atLeast limit` is equivalent to `num.max(limit)`. */
    def atLeast(minimum: Int): Int = this.value.max(minimum)
    /** `num atMost limit` is equivalent to `num.min(limit)`. */
    def atMost(maximum: Int): Int = this.value.min(maximum)
    /** `num.clamp(low, high)` is equivalent to `num.max(low).min(high)`. */
    def clamp(low: Int, high: Int): Int = this.value atLeast low atMost high
    /** Determines if the at least as large as `low` and less than `high`. Note that the lower bound is inclusive and the upper bound exclusive.  */
    def isBetween(low: Int, high: Int): Boolean = this.value >= low && this.value < high
    /** Determines if the integer isn’t divisible by two. */
    def isOdd: Boolean = this.value % 2 != 0
    /** Determines if the integer is divisible by two. */
    def isEven: Boolean = this.value % 2 == 0
  }


  /** This class extends the interface of `Double` with convenience methods. */
  implicit class ConvenientDouble(private val value: Double) extends AnyVal {
    /** `num atLeast limit` is equivalent to `num.max(limit)`. */
    def atLeast(minimum: Double): Double = this.value.max(minimum)
    /** `num atMost limit` is equivalent to `num.min(limit)`. */
    def atMost(maximum: Double): Double = this.value.min(maximum)
    /** `num.clamp(low, high)` is equivalent to `num.max(low).min(high)`. */
    def clamp(low: Int, high: Int): Double = this.value atLeast low atMost high
    /** Determines if the at least as large as `low` and less than `high`. Note that the lower bound is inclusive and the upper bound exclusive.  */
    def isBetween(low: Double, high: Double): Boolean = this.value >= low && this.value < high
  }


  /** This class extends the interface of `Float` with convenience methods. */
  implicit class ConvenientFloat(private val value: Float) extends AnyVal {
    /** `num atLeast limit` is equivalent to `num.max(limit)`. */
    def atLeast(minimum: Float): Float = this.value.max(minimum)
    /** `num atMost limit` is equivalent to `num.min(limit)`. */
    def atMost(maximum: Float): Float = this.value.min(maximum)
    /** `num.clamp(low, high)` is equivalent to `num.max(low).min(high)`. */
    def clamp(low: Int, high: Int): Float = this.value atLeast low atMost high
    /** Determines if the at least as large as `low` and less than `high`. Note that the lower bound is inclusive and the upper bound exclusive.  */
    def isBetween(low: Float, high: Float): Boolean = this.value >= low && this.value < high
  }


  /** This class extends the interface of `Long` with convenience methods. */
  implicit class ConvenientLong(private val value: Long) extends AnyVal {
    /** `num atLeast limit` is equivalent to `num.max(limit)`. */
    def atLeast(minimum: Long): Long = this.value.max(minimum)
    /** `num atMost limit` is equivalent to `num.min(limit)`. */
    def atMost(maximum: Long): Long = this.value.min(maximum)
    /** `num.clamp(low, high)` is equivalent to `num.max(low).min(high)`. */
    def clamp(low: Int, high: Int): Long = this.value atLeast low atMost high
    /** Determines if the at least as large as `low` and less than `high`. Note that the lower bound is inclusive and the upper bound exclusive.  */
    def isBetween(low: Long, high: Long): Boolean = this.value >= low && this.value < high
    /** Determines if the integer isn’t divisible by two. */
    def isOdd: Boolean = this.value % 2 != 0
    /** Determines if the integer is divisible by two. */
    def isEven: Boolean = this.value % 2 == 0
  }


  private[o1] implicit class RegexContext(private val interpolated: StringContext) extends AnyVal {
    import scala.util.matching.Regex
    def r = new Regex(interpolated.parts.mkString, interpolated.parts.tail.map( _ => "unnamedGroup" ): _*)
  }


  ////////////////////////
  ///// TAGGED TYPES
  ////////////////////////

  // based on http://etorreborre.blogspot.fi/2011/11/practical-uses-for-unboxed-tagged-types.html
  private[o1] type Tagged[TagType] = {type Tag = TagType}
  private[o1] type @@[BaseType, TagType] = BaseType with Tagged[TagType]


  private[o1] trait MSTag


  private[o1] type Millisec = Int @@ MSTag

  private[o1] def ms(timeInMilliseconds: Int) = timeInMilliseconds.asInstanceOf[Millisec]



  ////////////////////////////////
  ///// MISCELLANEOUS
  ////////////////////////////////

  /** Performs a given computation and checks to see if it crashes with a `NotImplementedError`.
    * Returns the result of the computation in an `Option`; returns `None` only in the
    * computation wasn’t implemented. Any other exception is thrown.
    * @param  computation  a computation that may or may not be implemented
    * @tparam Result       the type of the given computation  */
  def ifImplemented[Result](computation: =>Result): Option[Result] =
    Try(computation) match {
      case Success(result)                       => Some(result)
      case Failure(missing: NotImplementedError) => None
      case Failure(miscProblem)                  => throw miscProblem
    }

  /** Performs a given computation and determines whether it crashes with a `NotImplementedError`.
    * Returns `true` if it did and `false` otherwise.
    * @param  computation  a computation that may or may not be implemented
    * @tparam Result       the type of the given computation  */
  def isImplemented[Result](computation: =>Result): Boolean =
    ifImplemented(computation).isDefined


  private[o1] def editDistance(text1: String, text2: String, threshold: Int): Int =
    if (text1.isEmpty)
      if (text2.length <= threshold) text2.length else Int.MaxValue
    else if (text2.isEmpty)
      if (text1.length <= threshold) text1.length else Int.MaxValue
    else if (text1.head == text2.head)
      editDistance(text1.tail, text2.tail, threshold)
    else if (threshold == 0)
      Int.MaxValue
    else {
      val deletion = editDistance(text1.tail, text2, threshold - 1)
      val insertion = editDistance(text1, text2.tail, threshold - 1)
      val substitution = editDistance(text1.tail, text2.tail, threshold - 1)
      val shortest = Seq(deletion, insertion, substitution).min
      if (shortest == Int.MaxValue) Int.MaxValue else shortest + 1
    }


  private[o1] def repeatEvery(interval: Int)(timedBlock: => Unit): Ticker = {
    val ticker = new Ticker(interval, Ticker.TickEvery(interval))(timedBlock)
    ticker.start()
    ticker
  }


  private[o1] object Program {

    // not entirely reliable, but good enough for some purposes
    lazy val isRunningInScalaREPL: Boolean =
      Try(Class.forName("scala.tools.nsc.interpreter.IMain")).isSuccess

    lazy val isRunningInGTK: Boolean =
      javax.swing.UIManager.getSystemLookAndFeelClassName.endsWith("GTKLookAndFeel")

  }


  private[o1] class FirstTimeOnly(effect: => Unit) {

    private lazy val storedEffect = effect

    def apply(): Unit = storedEffect

  }


  private[o1] object firstTimeOnly {

    def apply(effect: => Unit): FirstTimeOnly = new FirstTimeOnly(effect)

  }


  /////////////////////////////////////
  // INTERNAL: SMCL INITIALIZATION
  /////////////////////////////////////

  private val smclInitialization = new DoneStatus()

  private class DoneStatus {
    private var hasNotBeenDone = true

    def setDone(): Unit = {
      if (hasNotBeenDone) {
        synchronized {
          if (hasNotBeenDone) {
            hasNotBeenDone = false
          }
        }
      }
    }
    def isNotDone: Boolean = hasNotBeenDone
  }


  private[o1] def smclInit(): Unit = {
    if (smclInitialization.isNotDone) {
      synchronized {
        if (smclInitialization.isNotDone) {
          smcl.infrastructure.jvmawt.Initializer()

          // Effects of setting the DefaultBackgroundColor include the background color for bitmap rotations
          smcl.settings.DefaultBackgroundColor = smcl.colors.rgb.Transparent

          // This affects to all affine transformations including scaling, rotations, and flips
          smcl.settings.jvmawt.AffineTransformationInterpolationMethod = smcl.settings.jvmawt.Bicubic

          // smcl.settings.jvmawt.DrawingIsAntialiased = false

          smclInitialization.setDone()
        }
      }
    }
  }

  smclInit()


}
