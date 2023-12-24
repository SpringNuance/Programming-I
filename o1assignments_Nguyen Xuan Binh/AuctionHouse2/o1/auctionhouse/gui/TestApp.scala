
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.auctionhouse.gui

import scala.swing._
import scala.swing.event._
import o1.auctionhouse._
import FlowPanel.Alignment
import o1.gui.layout._
import o1.gui.Dialog._
import o1.gui.O1AppDefaults
import java.util.Locale.US
import scala.util.Try


/** The singleton object `TestApp` represents a simple application that a programmer can use
  * to experiment with the main functionality of the classes from package `o1.auctionhouse`.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this object works
  * or can be used, apart from the fact that you can use this file to start the program.''' */
object TestApp extends SimpleSwingApplication with O1AppDefaults {

  def top = new MainFrame {

    title = """A Simple UI for Playing Around with an "Auction House""""
    location = new Point(100, 100)

    val house = new AuctionHouse("ReBay")

    val itemTabs = new TabbedPane

    val statusLine = new Label
    val advanceButton = new Button("Next day")
    val addFPS = new Button("Add a fixed-price item")
    val addDA  = new Button("Add a Dutch-style auction")
    val addEA  = new Button("Add an English-style auction")
    val buttonRow = new FlowPanel(FlowPanel.Alignment.Center)(addFPS, addDA, addEA, advanceButton)
    listenTo(addFPS, addDA, addEA, advanceButton)

    listenTo(itemTabs.selection)
    reactions += {
      case ButtonClicked(source) =>
        if (source == advanceButton) {
          this.house.nextDay()
          this.itemTabs.pages.foreach( _.content.asInstanceOf[ItemPanel[_]].refresh() )
        } else {
          val newPanel = if (source == addFPS) makeFPS() else if (source == addDA) makeDA() else if (source == addEA) makeEA() else None
          newPanel.foreach(this.addTab)
          updateStatus()
        }
      case SelectionChanged(_) =>
        defaultButton = itemTabs.selection.page.content.asInstanceOf[ItemPanel[_]].defaultButton
    }

    this.addTab(new FPSPanel(new FixedPriceSale("Example item", 10000, 3)))
    this.addTab(new DAPanel(new DutchAuction("Dutch auction example", 100000, 1000, 70000)))
    this.addTab(new EAPanel(new EnglishAuction("English auction example", 50000, 10)))

    contents = new EasyPanel {
      placeNW(statusLine, (0, 0), OneSlot, Slight,          (5, 5,  5, 5))
      placeNW(buttonRow,  (0, 1), OneSlot, Slight,          NoBorder)
      placeNW(itemTabs,   (0, 2), OneSlot, FillBoth(1, 1), NoBorder)
    }

    this.resizable = false
    this.updateStatus()
    this.pack()


    def updateStatus() = {
      val line = s"""${house.name}. Total: %.2f €. Average: %.2f €. Open items: ${house.numberOfOpenItems}. Priciest: ${house.priciest.map( _.description ).getOrElse("(none)")}."""
      this.statusLine.text = line.formatLocal(US, (house.totalPrice / 100.0), (house.averagePrice / 100.0))
    }

    def addTab(panel: ItemPanel[ItemForSale]) = {
      this.house.addItem(panel.item)
      val itemCount = this.itemTabs.pages.size
      this.itemTabs.pages += new TabbedPane.Page("Item #" + (itemCount + 1), panel, panel.tooltipText)
      this.itemTabs.selection.index = itemCount
      panel.requestFocus()
      this.pack()
    }

    def makeFPS() = {
       for {
         description <- descriptionParam()
         price       <- intParam("Price (in cents)")
         duration    <- intParam("Duration (in days)")
       } yield new FPSPanel(new FixedPriceSale(description, price, duration))
    }

    def makeDA() = {
       for {
         description <- descriptionParam()
         start       <- intParam("Starting price (in cents)")
         decrement   <- intParam("Decrement (in cents)")
         minimum     <- intParam("Minimum price (in cents)")
       } yield new DAPanel(new DutchAuction(description, start, decrement, minimum))
    }

    def makeEA() = {
       for {
         description <- descriptionParam()
         start       <- intParam("Starting price (in cents)")
         duration    <- intParam("Duration (in days)")
       } yield new EAPanel(new EnglishAuction(description, start, duration))
    }

    def descriptionParam() = {
      requestNonEmptyLine("Item description: ", "Please enter a non-empty description.", RelativeTo(buttonRow))
    }

    def intParam(prompt: String) = {
      requestInt(prompt + ": ", _ >= 0, "Please enter a reasonable integer value.", RelativeTo(buttonRow)).map(Integer.valueOf)
    }

    abstract class ItemPanel[+SaleType <: ItemForSale](val item: SaleType) extends EasyPanel {
      val tooltipText = item.description

      def defaultButton: Button

      val open, expired, buyer = new Label
      val title = new Label(s"<html><b>${item.description}</b> (${item.getClass.getSimpleName})</html>")
      val customerSelector = new ComboBox(Seq("Melchior", "Caspar", "Balthazar"))
      // customerSelector.makeEditable()
      val buyAction = new FlowPanel(Alignment.Left)(new Label("Select customer: "), customerSelector)

      placeNW(title,     (0, 0), ThreeWide, NoFill(1, 0), (20, 20, 8,  8))
      placeNW(open,      (0, 1), OneSlot,   NoFill(1, 0), (0,  20, 0,  0))
      placeNW(expired,   (1, 1), OneSlot,   NoFill(1, 0), (0,  0,  0,  0))
      placeNW(buyer,     (0, 4), ThreeWide, NoFill(1, 0), (0,  20, 0,  0))
      placeNW(buyAction, (0, 5), ThreeWide, NoFill(1, 0), (5,  20, 10, 0))

      def refresh() = {
        this.open.text    = s"<html>Open: ${if (this.item.isOpen) "yes" else "<font color=\"red\">no</font>"}</html>"
        this.buyer.text   = s"<html>Buyer: ${this.item.buyer.map( name => s"""<font color="green">$name</font>""" ).getOrElse("(none)")}</html>"
        this.expired.text = s"<html>Expired: ${if (this.item.isExpired) "<font color=\"red\">yes</font>" else "no"}</html>"
        updateStatus()
      }

    }


    private abstract class IPPanel(item: InstantPurchase) extends ItemPanel(item) {
      val buyButton = new Button("Buy") {
        listenTo(this)
        reactions += {
          case ButtonClicked(_) =>
            val result = item.buy(customerSelector.selection.item)
            if (!result) {
              display("Failed to buy the item.", RelativeTo(this))
            }
            refresh()
        }
      }
      buyAction.contents += buyButton

      def defaultButton = this.buyButton
    }



    private class FPSPanel(item: FixedPriceSale) extends IPPanel(item) {
      val daysRemaining = new Label
      val price = new Label("Price: %.2f €".formatLocal(US, this.item.price / 100.0))
      refresh()
      placeNW(price,         (0, 2), OneSlot, NoFill(1, 0), (0,  20, 0, 0))
      placeNW(daysRemaining, (1, 2), OneSlot, NoFill(1, 0), (0,  0,  0, 0))

      override def refresh() = {
        super.refresh()
        this.daysRemaining.text = s"Days remaining: ${this.item.daysLeft}"
      }
    }

    private class DAPanel(item: DutchAuction) extends IPPanel(item) {
      val priceRatio, currentPrice = new Label
      val startingPrice = new Label("Starting price: %.2f €".formatLocal(US, this.item.startingPrice / 100.0))
      val minimumPrice = new Label("Minimum price: %.2f €".formatLocal(US, this.item.minimumPrice / 100.0))
      refresh()
      placeNW(startingPrice, (0, 2), OneSlot, Slight, (0,  20, 0, 0))
      placeNW(minimumPrice,  (1, 2), OneSlot, Slight, (0,  0,  0, 0))
      placeNW(currentPrice,  (0, 3), OneSlot, Slight, (0,  20, 0, 0))
      placeNW(priceRatio,    (1, 3), OneSlot, Slight, (0,  0,  0, 0))

      override def refresh() = {
        super.refresh()
        this.currentPrice.text = "Price now: %.2f €".formatLocal(US, this.item.price / 100.0)
        this.priceRatio.text   = "Price/starting: %.2f".formatLocal(US, this.item.priceRatio)
      }
    }

    private class EAPanel(item: EnglishAuction) extends ItemPanel(item) {
      val requiredBid, currentPrice, daysRemaining = new Label
      val startingPrice = new Label("Starting price: %.2f €".formatLocal(US, this.item.startingPrice / 100.0))
      val amountField = new TextField((this.item.price * 1.1).toInt.toString)
      amountField.columns = 8
      val bidButton = new Button("Place bid") {
        listenTo(this)
        reactions += {
          case ButtonClicked(_) =>
            val result = EAPanel.this.item.bid(customerSelector.selection.item, amountField.text.toIntOption.getOrElse(0))
            if (!result) {
              display("Didn't become the highest bidder.", RelativeTo(this))
            }
            refresh()
        }
      }
      buyAction.contents += new Label("Amount (in cents): ") += amountField += bidButton
      refresh()
      placeNW(startingPrice, (0, 2), OneSlot, Slight, (0,  20, 0, 0))
      placeNW(daysRemaining, (1, 2), OneSlot, Slight, (0,  0,  0, 0))
      placeNW(currentPrice,  (0, 3), OneSlot, Slight, (0,  20, 0, 0))
      placeNW(requiredBid,   (1, 3), OneSlot, Slight, (0,  0,  0, 0))

      override def refresh() = {
        super.refresh()
        this.currentPrice.text =   "Price now: %.2f €".formatLocal(US, this.item.price / 100.0)
        this.requiredBid.text =    "Bid at least: %.2f €".formatLocal(US, this.item.requiredBid / 100.0)
        this.daysRemaining.text = s"Days remaining: ${this.item.daysLeft}"
      }

      def defaultButton = this.bidButton

      override def requestFocus() = {
        this.amountField.requestFocus()
      }
    }



  }

}
