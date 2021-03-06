package cn.ycmedia.BidSuccessRate

import scala.collection.mutable.ArrayBuffer

class MultiUnits (binarynum:Int,binarylen:Int,numericlen:Int) {
  
  val UnitsBuffer = ArrayBuffer(Array(0.0))
  //Array(out,binary_hidden_unit,numeric_hidden_unit,out_unit)
  val Numeric = new Array[Double](numericlen)
  val Binary = new Array[Double](binarylen)
  val Out = new Array[Double](binarynum+1)
 
  for(i <- Iterator.range(0,binarynum))
    UnitsBuffer+=Binary
  UnitsBuffer+=Numeric
  UnitsBuffer+=Out
  
  val Units=UnitsBuffer.toArray
  
}