package BidSuccessRate

import scala.collection.mutable.ArrayBuffer
import scala.util.hashing.MurmurHash3
import scala.collection.Iterator
import scala.math._


class Initializer(hiddenum:Int,binarylen:Int,initialweight:Double) {
  
  def initd(data:String):Array[Array[Double]] = {
    //Initialize Information From Data
    val blocks = data.split("\\|")
    //Get Label
    val example = ArrayBuffer(Array(blocks(1).toDouble))
    //Get Binary Feature
    val binaryblock=blocks(2).split("\t")
    example+=Array[Double](binarylen)
    for{i <- Iterator.range(0,binaryblock.length)}{
      val pos=hash(binaryblock(i))
      example(1).update(pos,1.0)
    }
    for{j <- Iterator.range(1,hiddenum)}{
      example+=example(1)
    }
    //Get Numeric Feature
    val numericblock=blocks(3).split("\t").map(x => x.toDouble)
    example+=numericblock
    
    example.toArray
  }
  
  def hash(str:String):Int = {
    var pos = abs(MurmurHash3.stringHash(str))
    while(pos > binarylen){ pos %= binarylen }
    pos
  }
  
  def initw:Array[Array[Double]] = {
    //Initialize Model Weight
    val units = new MultiUnits(hiddenum,binarylen).Units
    val weight = ArrayBuffer(Array(0.0))
    for{i <- Iterator.range(1,hiddenum+1)}{
      weight+=units(i).map(_ +initialweight)
    }
    weight.toArray
  }
}