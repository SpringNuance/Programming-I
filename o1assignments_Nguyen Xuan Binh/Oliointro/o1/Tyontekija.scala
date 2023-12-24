package o1

// Alla on kakkoskierrokselta tuttu Tyontekija-luokka, johon on lisätty dokumentaatiokommentteja.
// Seuraavan määrittelyn perusteella voidaan automaattisesti tuottaa tämän moduulin doc-kansiosta
// löytyvä työntekijäluokan dokumentaatiosivu.

/** Luokka `Tyontekija` kuvaa työntekijöitä (kuvitteellisessa kirjanpitojärjestelmässä).
  * Kustakin työntekijästä kirjataan ominaisuuksia kuten nimi ja kuukausipalkka.
  * Useimpien ominaisuuksien arvoja voi myös muuttaa.
  *
  * @param nimi      työntekijän nimi
  * @param syntynyt  työntekijän syntymävuosi
  * @param kkpalkka  työntekijän kuukausipalkka euroissa (ilman työnantajalle koituvia sivukuluja) */
class Tyontekija(var nimi: String, val syntynyt: Int, var kkpalkka: Double) {

  /** suhteellinen työaika. Esimerkiksi 1.0 tarkoittaa täyspäiväisyyttä ja 0.5 puolipäiväisyyttä. */
  var tyoaika = 1.0


  /** Ilmoittaa palautusarvollaan, kuinka monta vuotta työntekijä täyttää annettuna vuonna. */
  def ikaVuonna(vuosi: Int) = vuosi - this.syntynyt


  /** Palauttaa työntekijän kuukausittaisen hinnan työnantajalleen. Tämä saadaan
    * kuukausipalkan (esim. 4000), työajan (esim. 0.6) ja sivukulukertoimen (esim. 1.3) tulona.
    *
    * @param kulukerroin  sivukulukerroin, työpaikkakohtainen luku, jota käytetään
    *                     työntekijälle aiheutuvien kulujen määrittämisessä */
  def kuukausikulut(kulukerroin: Double) = this.kkpalkka * this.tyoaika * kulukerroin


  /** Muuttaa työntekijän palkkaa kertoen sen annetulla kertoimella.
    * Esimerkiksi parametriarvo 1.2 tuottaa 20 %:n palkankorotuksen. */
  def korotaPalkkaa(kerroin: Double) = {
    this.kkpalkka = this.kkpalkka * kerroin
  }


  /** Palauttaa merkkijonokuvauksen eräistä työntekijäolion keskeisistä tiedoista. */
  def kuvaus =
    this.nimi + " (s. " + this.syntynyt + "), palkka " + this.tyoaika + " * " + this.kkpalkka + " euroa"

}

