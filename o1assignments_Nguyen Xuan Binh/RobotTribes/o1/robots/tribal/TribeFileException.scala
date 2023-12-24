
package o1.robots.tribal

/** This exception type represents situations where a robot tribe file is either
  * unreadable or does not contain a valid RoboSpeak program. */
class TribeFileException(message: String) extends RuntimeException(message)
