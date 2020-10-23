package com.spark.scala.hive

import scala.collection.mutable.ArrayBuffer
import scala.collection.SortedSet
import scala.collection.BitSet
import scala.collection.immutable.ListSet

abstract class A(a:Int){
  //var a:Int;
  def run()                           // Abstract method  
  def performance(){                  // Non-abstract method  
      println("Performance awesome")  
  }  
}
class B(a:Int) extends A(a){
  //a = 10;
  def run(){
    println(a)
  }
}

class Student(a:Int,b:Int){
  var c:Int = 0;
  def show(){
    println(a,b)
  }
  def this(a:Int,b:Int,c:Int){
    this(a,b)
    this.c =c
    
  }
}

trait SuperTrait  
case class CaseClass1(a:Int,b:Int) extends SuperTrait  
case class CaseClass2(a:Int) extends SuperTrait         // Case class  
case object CaseObject extends SuperTrait               // Case object  

object ScalaTest {
  def main(args: Array[String]): Unit = {
    val aobj = new B(10);
    println(aobj.run())
    
    new Student(1,2).show()
  
    callCase(CaseClass1(10,20))
    callCase(CaseClass2(100))
    callCase(CaseObject)
    
    
    println("Scala Array")
    var arr = Array(1,2,3,4,5,6)
    val d = arr.foreach(e => e*2) // wrong implementation
    val dm = arr.map(e => (e*2,1)) // Right way to map or transform
    val df = arr.filter( e => {
      e%2 == 0
    })
    for(i <- arr)
      println(i)
      
    dm.foreach({ case (k,v) =>
      println(k+ "-"+v)
    })
      
    dm.filter(e =>{
      e._1>5
    }).foreach(println)
    
    dm.filter({case (k,v) =>
      k>5
    }).foreach(println)
    
    df.foreach(println)
    
    println("------")
    var aryBuf = new ArrayBuffer[Int]
    dm.foreach({ case (k,v) =>
      aryBuf.+=:(k)  
    })
    aryBuf.foreach(println)
    
    println("------")
    val input = Array(7,1,5,3,6,4)
    val minPriceIndex = input.indexOf(input.min)
    var restPrice = new ArrayBuffer[Int]
    for(i <- minPriceIndex+1 to input.length-1){
      restPrice += input(i)
    }
    val highestSalePrice = restPrice.max
    val profit = highestSalePrice-input.min
    println(profit)
    
    println("------")
    var pi = 3.14567  
    print(f"$pi%2.3f")
    
    try {
      
    }
    catch{
      case e: ArithmeticException => {e.printStackTrace()}
      case e: Exception => e.printStackTrace()
    }
    
    var tupleValues = (1,2.5,"India")  
    tupleValues.productIterator.foreach(e => println(e))
    
    val lang = Set("Java","Scala","Python")
    val ver = Set("8","2.3","3")
    
    lang.+("ShellScriptin")
    
    println(lang.head)
    println(lang.tail)
    println(lang.isEmpty)
    val leng_ver = lang++ver
    leng_ver.foreach(println)
    println(lang.contains("Python"))
    println(lang.size)
    
    lang.union(ver).foreach(println)
    
    println("------------")
    var numbers: SortedSet[Int] = SortedSet(5,8,1,2,9,6,4,7,2)  
    numbers.foreach(println)
    
    var numbersBit = BitSet(1,5,8,6,9,0)  
    numbersBit.foreach((element:Int) => println(element+" "))  
    
    var listset = ListSet(4,2,8,0,6,3,45)  
    listset.foreach((element:Int) => println(element+" "))
    
    println("----------")
    val sq:Seq[Int] = Seq(10,11,12,56,23,130)
    println(sq.isEmpty)
    println(sq.contains(10))
    println(sq.endsWith(Seq(23,130)))
    println(sq.lastIndexOf(12))
    println(sq.reverse)
    println(sq.sortBy((f => f)))
    println(sq.sortWith(_>_))
   
    
      
  }
  
 def callCase(f:SuperTrait) = f match{  
      case CaseClass1(f,g)=>println("a = "+f+" b ="+g)  
      case CaseClass2(f)=>println("a = "+f)  
      case CaseObject=>println("No Argument")  
  }  
   
}