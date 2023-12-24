package o1.people

class Passenger(val name: String, val card: Option[TravelCard]) {
  def hasCard = {
  card match {
     case Some(card) => true
     case None => false
    }
  }
  def canTravel = {
   card match {
     case Some(card) => {
       var x = card
       if (card.isValid) true else false
     }
     case None => false
    }
  }
}
