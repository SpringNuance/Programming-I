
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.football2


/** The singleton object `ExampleLeague` provides constants that can be used
  * to experiment with the other classes of this package. More specifically,
  * a bunch of `Club` and `Player` objects are provided.
  *
  * Usage examples (which assume the command `import ExampleLeague._` has been given):
  *
  *  - `new Player("T. Teekkari", ARS)`
  *
  *  - `new Match(LIV, SOT)`
  *
  */
object ExampleLeague {

  val CHE = new Club("Chelsea",              "CHE", "Stamford Bridge")
  val EVE = new Club("Everton",              "EVE", "Goodison Park")
  val CAR = new Club("Cardiff",              "CAR", "Cardiff City Stadium")
  val WH  = new Club("West Ham United",      "WH",  "Upton Park")
  val CP  = new Club("Crystal Palace",       "CP",  "Selhurst Park")
  val MU  = new Club("Manchester United",    "MU",  "Old Trafford")
  val STO = new Club("Stoke City",           "STO", "Britannia Stadium")
  val HUL = new Club("Hull City",            "HUL", "KC Stadium")
  val NEW = new Club("Newcastle United",     "NEW", "St. James' Park")
  val LIV = new Club("Liverpool",            "LIV", "Anfield")
  val AV  = new Club("Aston Villa",          "AV",  "Villa Park")
  val SOT = new Club("Southampton",          "SOT", "St. Mary's Stadium")
  val TOT = new Club("Tottenham Hotspur",    "TOT", "White Hart Lane")
  val MC  = new Club("Manchester City",      "MC",  "Etihad Stadium")
  val WBA = new Club("West Bromwich Albion", "WBA", "The Hawthorns")
  val SWA = new Club("Swansea City",         "SWA", "Liberty Stadium")
  val NOR = new Club("Norwich City",         "NOR", "Carrow Road")
  val ARS = new Club("Arsenal",              "ARS", "Emirates Stadium")
  val FUL = new Club("Fulham",               "FUL", "Craven Cottage")
  val SUN = new Club("Sunderland",           "SUN", "Stadium of Light")
  val KH  = new Club("Kainalniemen Hiki",    "KH",  "Kainalniemi")

  val Clubs = Vector(ARS, CHE, EVE, CAR, WH, CP, MU, STO, HUL, NEW, LIV, AV, SOT, TOT, MC, WBA, SWA, NOR, FUL, SUN, KH).sortBy( _.name )

  private def playersForNames(club: Club, names: Vector[String]) =
    club -> names.map( name => new Player(name, club) )

  val Players = Seq(
    CHE -> Vector("P Cech", "M Schwarzer", "C Azpilicueta", "A Cole", "B Ivanovic", "G Cahill", "D Luiz", "J Terry", "E Hazard", "F Lampard", "N Matic", "J Mikel", "Oscar", "Ramires", "M Salah", "A Schurrle", "M van Ginkel", "Willian", "D Ba", "S Eto'o", "F Torres"),
    EVE -> Vector("T Howard", "J Robles", "L Baines", "S Coleman", "T Hibbert", "B Oviedo", "A Alcaraz", "S Distin", "P Jagielka", "J Stones", "R Barkley", "G Barry", "G Deulofeu", "D Gibson", "M Gueye", "J McCarthy", "A McGeady", "K Mirallas", "S Naismith", "L Osman", "S Pienaar", "A Kone", "R Lukaku", "L Traore", "A Vellios"),
    CAR -> Vector("J Lewis", "D Marshall", "M Connolly", "Fabio", "D John", "K McNaughton", "A Taylor", "K Theophile-Catherine", "J Cala", "S Caulker", "M Hudson", "B Turner", "C Bellamy", "K Bo-Kyung", "D Cowie", "M Daehli", "M Eikrem", "A Gunnarsson", "K Harris", "F Kiss", "G Medel", "J Mutch", "C Noone", "T Smith", "P Whittingham", "W Zaha", "J Berget", "F Campbell", "K Jones", "E Velikonja"),
    WH  -> Vector("Adrian", "S Henderson", "J Jaaskelainen", "P Armero", "G Demel", "G McCartney", "J O'Brien", "D Potts", "J Collins", "R Johnson", "W Reid", "J Tomkins", "J Cole", "J Collison", "M Diame", "A Diarra", "S Downing", "M Jarvis", "M Noble", "A Nocerino", "K Nolan", "M Taylor", "M Borriello", "A Carroll", "C Cole", "R Vaz Te"),
    CP  -> Vector("W Hennessey", "L Price", "J Speroni", "D Moxey", "J Parr", "J Ward", "S Dann", "D Delaney", "D Gabbidon", "A Mariappa", "P McCarthy", "B Bannan", "Y Bolasie", "K De Silva", "K Dikgacoi", "A Guedioura", "T Ince", "M Jedinak", "J Ledley", "S O'Keefe", "J Puncheon", "J Thomas", "K Appiah", "M Chamakh", "D Gayle", "C Jerome", "G Murray", "A Wilbraham"),
    MU  -> Vector("D De Gea", "A Lindegaard", "A Buttner", "P Evra", "Rafael", "G Varela", "J Evans", "R Ferdinand", "P Jones", "C Smalling", "N Vidic", "M Carrick", "T Cleverley", "M Fellaini", "D Fletcher", "R Giggs", "A Januzaj", "S Kagawa", "J Mata", "Nani", "A Valencia", "A Young", "J Hernandez", "W Rooney", "R van Persie", "D Welbeck"),
    STO -> Vector("A Begovic", "T Sorensen", "G Cameron", "E Pieters", "A Wilkinson", "M Wilson", "R Huth", "M Muniesa", "R Shawcross", "C Adam", "M Arnautovic", "O Assaidi", "M Etherington", "S Ireland", "S N'Zonzi", "W Palacios", "G Whelan", "P Crouch", "J Guidetti", "P Odemwingie", "J Walters"),
    HUL -> Vector("S Harper", "E Jakupovic", "A McGregor", "J Dudgeon", "M Figueroa", "L Rosenior", "A Bruce", "J Chester", "C Davies", "A Faye", "P McShane", "S Aluko", "G Boyd", "R Brady", "A Elmohamady", "T Huddlestone", "R Koren", "J Livermore", "D Meyler", "S Quinn", "M Fryatt", "Gedo", "N Jelavic", "S Long", "Y Sagbo"),
    NEW -> Vector("R Elliot", "T Krul", "M Debuchy", "P Dummett", "M Haidara", "D Santon", "F Coloccini", "S Taylor", "M Williamson", "M Yanga-Mbiwa", "V Anita", "H Ben Arfa", "G Bigirimana", "D Gosling", "S Marveaux", "G Obertan", "M Sissoko", "R Taylor", "C Tiote", "H Vuckic", "S Ameobi", "S Ameobi", "P Cisse", "L de Jong", "Y Gouffran", "L Remy"),
    LIV -> Vector("B Jones", "S Mignolet", "A Cissokho", "J Enrique", "J Flanagan", "G Johnson", "M Kelly", "D Agger", "S Coates", "M Sakho", "M Skrtel", "K Toure", "L Alberto", "J Allen", "P Coutinho", "S Gerrard", "J Henderson", "Lucas", "V Moses", "R Sterling", "I Aspas", "D Sturridge", "L Suarez"),
    AV  -> Vector("S Given", "B Guzan", "J Bennett", "R Bertrand", "M Lowton", "A Luna", "N Baker", "C Clark", "J Okore", "R Vlaar", "M Albrighton", "L Bacuna", "F Delph", "K El Ahmadi", "C Herd", "C N'Zogbia", "Y Sylla", "A Tonev", "A Westwood", "G Agbonlahor", "C Benteke", "N Helenius", "G Holt", "L Kozak", "A Weimann"),
    SOT -> Vector("A Boruc", "K Davis", "P Gazzaniga", "C Chambers", "N Clyne", "L Shaw", "J Fonte", "J Hooiveld", "D Lovren", "M Yoshida", "J Cork", "S Davis", "G do Prado", "A Lallana", "G Ramirez", "M Schneiderlin", "V Wanyama", "J Ward-Prowse", "S Gallagher", "R Lambert", "J Rodriguez"),
    TOT -> Vector("B Friedel", "H Lloris", "K Naughton", "D Rose", "K Walker", "V Chiriches", "M Dawson", "Y Kaboul", "J Vertonghen", "N Bentaleb", "E Capoue", "N Chadli", "M Dembele", "C Eriksen", "E Lamela", "A Lennon", "Paulinho", "Sandro", "G Sigurdsson", "A Townsend", "E Adebayor", "R Soldado"),
    MC  -> Vector("J Hart", "C Pantilimon", "G Clichy", "A Kolarov", "M Richards", "P Zabaleta", "M Demichelis", "V Kompany", "J Lescott", "M Nastasic", "Fernandinho", "J Garcia", "J Milner", "S Nasri", "J Navas", "J Rodwell", "D Silva", "Y Toure", "S Aguero", "E Dzeko", "S Jovetic", "A Negredo"),
    WBA -> Vector("L Daniels", "B Foster", "B Myhill", "B Jones", "G Popov", "S Reid", "L Ridgewell", "C Dawson", "D Lugano", "G McAuley", "J Olsson", "M Amalfitano", "C Brunt", "G Dorrans", "Z Gera", "J Morrison", "Y Mulumbu", "S Sessegnon", "S Sinclair", "C Yacob", "N Anelka", "V Anichebe", "S Berahino", "T Bifouma", "M Vydra"),
    SWA -> Vector("G Tremmel", "M Vorm", "B Davies", "A Rangel", "A Richards", "N Taylor", "D Tiendalli", "J Amat", "Chico", "G Monk", "A Williams", "L Britton", "J Canas", "J De Guzman", "N Dyer", "P Hernandez", "R Lamah", "Michu", "A Pozuelo", "W Routledge", "J Shelvey", "W Bony", "M Emnes", "D Ngog", "A Vazquez"),
    NOR -> Vector("M Bunn", "J Ruddy", "J Garrido", "R Martin", "M Olsson", "S Whittaker", "S Bassong", "R Bennett", "M Turner", "J Yobo", "E Bennett", "L Fer", "D Fox", "J Gutierrez", "W Hoolahan", "J Howson", "B Johnson", "A Pilkington", "N Redmond", "R Snodgrass", "A Tettey", "L Becchio", "J Elmander", "G Hooper", "R v Wolfswinkel"),
    ARS -> Vector("L Fabianski", "W Szczesny", "E Viviano", "K Gibbs", "C Jenkinson", "N Monreal", "B Sagna", "L Koscielny", "P Mertesacker", "T Vermaelen", "M Arteta", "S Cazorla", "A Chamberlain", "A Diaby", "M Flamini", "S Gnabry", "K Kallstrom", "R Miyaichi", "M Ozil", "A Ramsey", "T Rosicky", "T Walcott", "J Wilshere", "N Bendtner", "O Giroud", "L Podolski", "Y Sanogo"),
    FUL -> Vector("M Stekelenburg", "D Stockdale", "S Riether", "J Riise", "E Zverotic", "F Amorebieta", "D Burn", "B Hangeland", "J Heitinga", "D Boateng", "L Cole", "A Dejagah", "D Duff", "L Holtby", "A Kacaniklic", "G Karagounis", "P Kasami", "W Kvist", "S Parker", "K Richardson", "S Sidwell", "D Bent", "K Mitroglou", "H Rodallega"),
    SUN -> Vector("V Mannone", "O Ustari", "K Westwood", "M Alonso", "P Bardsley", "O Celustka", "A Dossena", "J O'Shea", "W Brown", "C Cuellar", "V Roberge", "S Vergini", "L Bridcutt", "L Cattermole", "J Colback", "C Gardner", "E Giaccherini", "A Johnson", "S Ki", "S Larsson", "J Altidore", "F Borini", "S Fletcher", "I Scocco"),
    KH  -> Vector("Elmo")
  ).map(Function.tupled(playersForNames)).toMap

}

