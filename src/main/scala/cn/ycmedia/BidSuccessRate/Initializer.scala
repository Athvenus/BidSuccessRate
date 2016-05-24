package main.scala.BidSuccessRate


import scala.collection.mutable.ArrayBuffer
import scala.util.hashing.MurmurHash3
import scala.collection.Iterator
import scala.math.abs


class Initializer(data:String,hiddenum:Int,binarylen:Int,initialweight:Double) extends Serializable{
  
  val blocks = data.split("\\|")
  val numericblock = blocks(3).split("\t").map(x => toDouble(x))
  
  def initd(str:String):Array[Array[Double]] = {
    //Initialize Information From Data
    val blocks = str.split("\\|")
    val numericblock = blocks(3).split("\t").map(x => toDouble(x))
    //1.Get Label
    val example = ArrayBuffer(Array(toDouble(blocks(1))))
    //2.Get Binary Feature
    val binaryblock=blocks(2).split("\t")
    example+=new Array[Double](binarylen)
    for{i <- Iterator.range(0,binaryblock.length)}{
      val pos=hash(i.toString+binaryblock(i))
      example(1).update(pos,1.0)
    }
    for{j <- Iterator.range(1,hiddenum)}{
      example+=example(1)
    }
    //3.Get Numeric Feature
    example+=numericblock
    //4.Get Hidden Values
    example+=new Array[Double](hiddenum+1)
    
    example.toArray
  }
  
  def hash(str:String):Int = {
    var pos = abs(MurmurHash3.stringHash(str))
    while(pos > binarylen){ pos %= binarylen }
    pos
  }
  
  def toDouble(str:String):Double = {
    val num = 0.0
    try{
      val num = str.trim.toDouble
    }catch{
      case ex : NumberFormatException => {
        val num = 0.0
      }
    }
    num
  }
  
  def initw:Array[Array[Double]] = {
    //Initialize Model Weight
    val numericlen = numericblock.length
    val units = new MultiUnits(hiddenum,binarylen,numericlen).Units
    val weight = ArrayBuffer(Array(0.0))
    for{i <- Iterator.range(1,units.length)}{
      weight+=units(i).map(_ +initialweight)
    }
    weight.toArray
  }
}

//Every Independent function is OK,but once put them together problem comes out.