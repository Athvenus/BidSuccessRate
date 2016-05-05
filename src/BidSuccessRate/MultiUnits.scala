package BidSuccessRate

import scala.collection.parallel.mutable.ParArray
import scala.collection.mutable.ArrayBuffer

class MultiUnits (hiddenum:Int,binarylen:Int) {
  
  val UnitsBuffer = ArrayBuffer[Any](0.0)
  //Array(out,binary_hidden_unit,numeric_hidden_unit,out_unit)
  val Numeric = new Array[Double](100)
  val Binary = Array[Int](binarylen)
  val Out = Array[Double](hiddenum+1)
 
  for(i <- Iterator.range(1,hiddenum-1))
    UnitsBuffer+=Binary
  UnitsBuffer+=Numeric
  UnitsBuffer+=Out
  
  val Units=UnitsBuffer.toArray
  
}