package o1

package object sananmuunnos {

  /** merkkijono, joka sisältää kaikkia suomen kielen vokaaleita vastaavat kirjoitusmerkit  */
  val Vokaalit = "\u0061\u0065\u0069\u006F\u0075\u0079\u00E4\u00F6\u00E5"
  /** merkkijono, joka sisältää kaikkia suomen kielen konsonantteja vastaavat kirjoitusmerkit  */
  val Konsonantit = "\u0062\u0063\u0064\u0066\u0067\u0068\u006A\u006B\u006C\u006D\u006E\u0070\u0071\u0072\u0073\u0074\u0076\u0077\u0078\u007A"

  /** merkkijono, joka sisältää kaikkia suomen kielen "muuttuvia takavokaaleita" (a, o, u) vastaavat kirjoitusmerkit  */
  val MuuttuvatTakavokaalit = "\u0061\u006F\u0075"
  /** merkkijono, joka sisältää kaikkia suomen kielen "muuttuvia etuvokaaleita" (ä, ö, y) vastaavat kirjoitusmerkit  */
  val MuuttuvatEtuvokaalit = "\u00E4\u00F6\u0079"


  /** Palauttaa `true`, jos parametriksi annettu merkki kuuluu suomen kielen perusaakkosiin eli on sellainen
    * merkki, jota käytetään suomessa jonkin vokaalin tai konsonantin kuvaamiseen. Palauttaa `false` muutoin. */
  def onKirjain(symboli: Char) = onVokaali(symboli) || onKonsonantti(symboli)


  /** Palauttaa `true`, jos parametriksi annettu merkki vastaa suomen kielen perusaakkosissa jotakin
    * vokaalia, `false` muutoin. */
  def onVokaali(symboli: Char) = Vokaalit.contains(symboli.toLower)


  /** Palauttaa `true`, jos parametriksi annettu merkki vastaa suomen kielen perusaakkosissa jotakin
    * konsonanttia, `false` muutoin. */
  def onKonsonantti(symboli: Char) = Konsonantit.contains(symboli.toLower)


  /** Palauttaa `true`, jos parametriksi annettu merkki vastaa suomen kielen perusaakkosissa jotakin
    * "muuttuvaa takavokaalia" (a, o, u), `false` muutoin. */
  def onMuuttuvaTakavokaali(symboli: Char) = MuuttuvatTakavokaalit.contains(symboli.toLower)


  /** Palauttaa `true`, jos parametriksi annettu merkki vastaa suomen kielen perusaakkosissa jotakin
    * "muuttuvaa etuvokaalia" (ä, ö, y), `false` muutoin. */
  def onMuuttuvaEtuvokaali(symboli: Char) = MuuttuvatEtuvokaalit.contains(symboli.toLower)


  /** Palauttaa `true`, jos parametriksi annettu merkki vastaa suomen kielen perusaakkosissa jotakin
    * "muuttuvaa vokaalia" (a, ä, o, ö, u, y), `false` muutoin. */
  def onMuuttuvaVokaali(symboli: Char) = onMuuttuvaTakavokaali(symboli) || onMuuttuvaEtuvokaali(symboli)


  /** Jos parametriksi annettu merkki vastaa "muuttuvaa etuvokaalia" (ä, ö, y), palauttaa sitä vastaavan
    * takavokaalin (a, o, u). Muussa tapauksessa palauttaa annetun merkin sellaisenaan. */
  def taakse(symboli: Char) = if (onMuuttuvaEtuvokaali(symboli)) MuuttuvatTakavokaalit(MuuttuvatEtuvokaalit.indexOf(symboli)) else symboli


  /** Jos parametriksi annettu merkki vastaa "muuttuvaa takavokaalia" (a, o, u), palauttaa sitä vastaavan
    * etuvokaalin (ä, ö, y). Muussa tapauksessa palauttaa annetun merkin sellaisenaan. */
  def eteen(symboli: Char) = if (onMuuttuvaTakavokaali(symboli)) MuuttuvatEtuvokaalit(MuuttuvatTakavokaalit.indexOf(symboli)) else symboli


  /** Palauttaa ensimmäisen parametrimerkin siirrettynä "taakse" tai "eteen" sen perusteella,
    * millainen jälkimmäinen parametrimerkki on. Ensimmäisestä merkistä siis palautetaan sellainen
    * versio joka muistuttaa ääntämispaikaltaan jälkimmäistä merkkiä.
    *
    * Tarkemmin sanoen:
    *
    * Jos ensimmäinen parametrimerkki vastaa  "muuttuvaa takavokaalia" (a, o, u) ja toinen parametri
    * "muuttuvaa etuvokaalia" (ä, ö, y), niin palautetaan ensimmäinen merkki "etuvokaaliksi vaihdettuna".
    * Esimerkiksi `assimiloi('a', 'y')` palauttaa ä-merkin.
    *
    * Jos ensimmäinen parametrimerkki vastaa  "muuttuvaa etuvokaalia" (ä, ö, y) ja toinen parametri
    * "muuttuvaa takavokaalia" (a, o, u), niin palautetaan ensimmäinen merkki "takavokaaliksi vaihdettuna".
    * Esimerkiksi `assimiloi('ä', 'u')` palauttaa a-merkin.
    *
    * Kaikissa muissa tapauksissa palautetaan ensimmäisenä parametriksi saatu merkki sellaisenaan. */
  def assimiloi(mika: Char, mihin: Char) =
    if (onMuuttuvaTakavokaali(mihin))
      taakse(mika)
    else if (onMuuttuvaEtuvokaali(mihin))
      eteen(mika)
    else
      mika


}
