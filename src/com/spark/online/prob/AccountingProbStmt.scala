package com.spark.online.prob

import java.time.ZonedDateTime
import java.util.Date
import scala.collection.JavaConverters._
import org.apache.spark.sql.SparkSession

// An item, a type of 'thing' that we sell.
case class Item(id: Int, price: BigDecimal)

// A site 'store or distribution center'
case class Site(id: Int, name: String)

// A message indicating a sale has occurred. inventory leaves the store, sales increases
case class Sale(id: Int, itemsSold: Set[SaleLineItem], siteId: Int, dateTime: ZonedDateTime)
// A given sale can have a multitude of sales line items. Each one contains an item and a count.
case class SaleLineItem(item: Item, count: Int)

// For a given site, the inventory system can send the following messages...
// Inventory reset means that the inventory has been counted and is the defined quantity
case class InventoryReset(item: Item, count: Int, siteId: Int, dateTime: ZonedDateTime)
// Inventory Received indicates that the count of a given item has been received at a given site
case class InventoryReceived(item: Item, count: Int, siteId: Int, dateTime: ZonedDateTime)
// Inventory Sent indicates that the count of a given item has left the site and going somewhere else
case class InventorySent(item: Item, count: Int, siteId: Int, dateTime: ZonedDateTime)

// A total for a given site/date/account.
case class AccountingTotal(siteId: Int, date: Date, accountNumber: Int, amount: BigDecimal)


// The following traits represent the various edge systems that our system will interact with
trait ItemService {
  def getItemDetail(id: Int): Item
}

trait LocationService {
  def getSiteDetail(id: Int): Site
}

trait SaleService {
  def getStream: Stream[Sale]
}

trait InventoryService {
  def getResetStream: Stream[InventoryReset]
  def getReceivedStream: Stream[InventoryReceived]
  def getSentStream: Stream[InventorySent]
}

trait AccountingService {
  def postAccountingTotal(total: AccountingTotal): Boolean
}

class AccountingSRVImpl extends ItemService with LocationService with SaleService with InventoryService with AccountingService{
  
    def getItemDetail(id: Int): Item={
      null
    }
    
    def getSiteDetail(id: Int): Site = {
      null
    }
    
    def getStream: Stream[Sale]={
      null
    }
  
   def getResetStream: Stream[InventoryReset] ={
     null
   }
   def getReceivedStream: Stream[InventoryReceived] ={
     null
   }
   def getSentStream: Stream[InventorySent] ={
     null
   }
    def postAccountingTotal(total: AccountingTotal): Boolean ={
    false 
   }
    
}

object AccountingProbStmt {
  
  def main(args: Array[String]): Unit={
    
   val r = scala.util.Random
   
   var items = scala.collection.mutable.ArrayBuffer[Item]()
   for(i <- 1 to 1000)
     items += Item(i, math.round((r.nextDouble()*10000)))
     
   var sites = scala.collection.mutable.ArrayBuffer[Site]()
   for(i <- 1 to 50)
     sites += Site(i, "Site-"+i)
     
   var receivedInv = scala.collection.mutable.ArrayBuffer[InventoryReceived]()
   for(i<- 1 to 500){
     val it = items(i%100)
     receivedInv += InventoryReceived(it, r.nextInt(500), sites(r.nextInt(50)).id, ZonedDateTime.now().minusDays(r.nextInt(45)))     
   }
   
   var sentInv = scala.collection.mutable.ArrayBuffer[InventorySent]()
   for(i<- 1 to 500){
     val it = items(i%100)
     sentInv += InventorySent(it, r.nextInt(100), sites(r.nextInt(50)).id, ZonedDateTime.now().minusDays(r.nextInt(45)))    
   }   
     
   var sales = scala.collection.mutable.ArrayBuffer[Sale]()
   for(i <- 1 to 100){
     var saleLineItems = Set[SaleLineItem]()
     val noOfItems = r.nextInt(10) // Considering MAX 10 Diff items in same order
     for( it <- 1 to noOfItems){
       val it = items(i%100)
       val saleLineItem  = SaleLineItem(it, r.nextInt(10)) // Considering MAX 10 QTY of same item.
       saleLineItems +=(saleLineItem)
     }
     sales += Sale(i, saleLineItems, sites(i%50).id, ZonedDateTime.now().minusDays(r.nextInt(30)))
   }
    
   val invSentMap = sentInv.map(f => (f.item.id, f))
   val invSentGrp = invSentMap.groupBy(f => f._1)
  // val red = invSentMap.reduce((a,b) => a.)
   invSentGrp.foreach(f => {
     f._2.foreach(p => println(p._2.item.id +"\t"+ p._2.count))
   })
  
  //invSentMap.reduce( (a,b) => a._2.count + b._2.count )

  }
}
