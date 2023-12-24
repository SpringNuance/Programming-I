
package o1.sound.midi

/** A [[MusicElem]] that sets the instrument for the upcoming notes. The 128 main instruments
 *  of [[http://www.midi.org/techspecs/gm1sound.php the standard MIDI list]] are supported.
 *  The [[Instrument$ companion object of this class]] provides constants that match those instruments.
  * @param number  the number of a MIDI instrument between 0 and 127, inclusive */
case class Instrument(val number: Int) extends MusicElem {
  if (number < 1 || number > 128) throw new IllegalArgumentException(s"The instrument number $number is not between 1 and 128 as expected.")
  /** an `Instrument` element is a meta directive; it has no duration, so its length is zero */
  val length = 0
}


/** This object provides a selection of constants that correspond to the 128 main instruments of
  * [[http://www.midi.org/techspecs/gm1sound.php the standard MIDI list]]. They are grouped in
  * nested objects ([[Piano]], [[Strings]], etc.). The object [[PercussionChannel]] contains
  * constants that correspond to the special sounds available on the MIDI percussion channel. */
object Instrument {

  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object Piano {
    val AcousticGrandPiano  = 1
    val BrightAcousticPiano = 2
    val ElectricGrandPiano  = 3
    val HonkyTonkPiano      = 4
    val ElectricPiano1      = 5
    val ElectricPiano2      = 6
    val Harpsichord         = 7
    val Clavi               = 8
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object ChromaticPercussion {
    val Celesta      = 9
    val Glockenspiel = 10
    val MusicBox     = 11
    val Vibraphone   = 12
    val Marimba      = 13
    val Xylophone    = 14
    val TubularBells = 15
    val Dulcimer     = 16
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object Organ {
    val DrawbarOrgan    = 17
    val PercussiveOrgan = 18
    val RockOrgan       = 19
    val ChurchOrgan     = 20
    val ReedOrgan       = 21
    val Accordion       = 22
    val Harmonica       = 23
    val TangoAccordion  = 24
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object Guitar {
    val AcousticGuitarNylon = 25
    val AcousticGuitarSteel = 26
    val ElectricGuitarJazz  = 27
    val ElectricGuitarClean = 28
    val ElectricGuitarMuted = 29
    val OverdrivenGuitar    = 30
    val DistortionGuitar    = 31
    val Guitarharmonics     = 32
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object Bass {
    val AcousticBass       = 33
    val ElectricBassFinger = 34
    val ElectricBassPick   = 35
    val FretlessBass       = 36
    val SlapBass1          = 37
    val SlapBass2          = 38
    val SynthBass1         = 39
    val SynthBass2         = 40
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object Strings {
    val Violin           = 41
    val Viola            = 42
    val Cello            = 43
    val Contrabass       = 44
    val TremoloStrings   = 45
    val PizzicatoStrings = 46
    val OrchestralHarp   = 47
    val Timpani          = 48
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object Ensemble {
    val StringEnsemble1 = 49
    val StringEnsemble2 = 50
    val SynthStrings1   = 51
    val SynthStrings2   = 52
    val ChoirAahs       = 53
    val VoiceOohs       = 54
    val SynthVoice      = 55
    val OrchestraHit    = 56
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object Brass {
    val Trumpet      = 57
    val Trombone     = 58
    val Tuba         = 59
    val MutedTrumpet = 60
    val FrenchHorn   = 61
    val BrassSection = 62
    val SynthBrass1  = 63
    val SynthBrass2  = 64
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object Reed {
    val SopranoSax  = 65
    val AltoSax     = 66
    val TenorSax    = 67
    val BaritoneSax = 68
    val Oboe        = 69
    val EnglishHorn = 70
    val Bassoon     = 71
    val Clarinet    = 72
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object Pipe {
    val Piccolo     = 73
    val Flute       = 74
    val Recorder    = 75
    val PanFlute    = 76
    val BlownBottle = 77
    val Shakuhachi  = 78
    val Whistle     = 79
    val Ocarina     = 80
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object SynthLead {
    val Square       = 81
    val Sawtooth     = 82
    val Calliope     = 83
    val Chiff        = 84
    val Charang      = 85
    val Voice        = 86
    val Fifths       = 87
    val BassPlusLead = 88
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object SynthPad {
    val NewAge    = 89
    val Warm      = 90
    val Polysynth = 91
    val Choir     = 92
    val Bowed     = 93
    val Metallic  = 94
    val Halo      = 95
    val Sweep     = 96
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object SynthEffects {
    val Rain       = 97
    val Soundtrack = 98
    val Crystal    = 99
    val Atmosphere = 100
    val Brightness = 101
    val Goblins    = 102
    val Echoes     = 103
    val SciFi      = 104
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object Ethnic {
    val Sitar    = 105
    val Banjo    = 106
    val Shamisen = 107
    val Koto     = 108
    val Kalimba  = 109
    val Bagpipe  = 110
    val Fiddle   = 111
    val Shanai   = 112
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object Percussion {
    val TinkleBell    = 113
    val Agogo         = 114
    val SteelDrums    = 115
    val Woodblock     = 116
    val TaikoDrum     = 117
    val MelodicTom    = 118
    val SynthDrum     = 119
    val ReverseCymbal = 120
  }
  /** Constants that correspond to instruments in [[http://www.midi.org/techspecs/gm1sound.php the MIDI standard]]. */
  object SoundEffects {
    val GuitarFretNoise = 121
    val BreathNoise     = 122
    val Seashore        = 123
    val BirdTweet       = 124
    val TelephoneRing   = 125
    val Helicopter      = 126
    val Applause        = 127
    val Gunshot         = 128
  }


  /** Constants that evaluate to the notes (`String`s) that correspond to different special sounds on the
    * [[http://www.midi.org/techspecs/gm1sound.php MIDI standard]]’s percussion channel. For example,
    * `BassDrum` is the note `"C3"` and `HandClap` the note `"D#3". See [[o1.sound.midi the package overview]]
    * for an explanation of the note syntax. */
  object PercussionChannel {
    val AcousticBassDrum = "H2"
    val BassDrum         = "C3"
    val SideStick        = "C#3"
    val AcousticSnare    = "D3"
    val HandClap         = "D#3"
    val ElectricSnare    = "E3"
    val LowFloorTom      = "F3"
    val ClosedHiHat      = "F#3"
    val HighFloorTom     = "G3"
    val PedalHiHat       = "G#3"
    val LowTom           = "A3"
    val OpenHiHat        = "A#3"
    val LowMidTom        = "H3"
    val HiMidTom         = "C4"
    val CrashCymbal1     = "C#4"
    val HighTom          = "D4"
    val RideCymbal1      = "D#4"
    val ChineseCymbal    = "E4"
    val RideBell         = "F4"
    val Tambourine       = "F#4"
    val SplashCymbal     = "G4"
    val Cowbell          = "G#4"
    val CrashCymbal2     = "A4"
    val Vibraslap        = "A#4"
    val RideCymbal2      = "H4"
    val HiBongo          = "C5"
    val LowBongo         = "C#5"
    val MuteHiConga      = "D5"
    val OpenHiConga      = "D#5"
    val LowConga         = "E5"
    val HighTimbale      = "F5"
    val LowTimbale       = "F#5"
    val HighAgogo        = "G5"
    val LowAgogo         = "G#5"
    val Cabasa           = "A5"
    val Maracas          = "A#5"
    val ShortWhistle     = "H5"
    val LongWhistle      = "C6"
    val ShortGuiro       = "C#6"
    val LongGuiro        = "D6"
    val Claves           = "D#6"
    val HiWoodBlock      = "E6"
    val LowWoodBlock     = "F6"
    val MuteCuica        = "F#6"
    val OpenCuica        = "G6"
    val MuteTriangle     = "G#6"
    val OpenTriangle     = "A6"
  }

}
