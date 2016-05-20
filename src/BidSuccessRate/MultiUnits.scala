package BidSuccessRate

import scala.collection.parallel.mutable.ParArray
import scala.collection.mutable.ArrayBuffer
import org.apache.spark.Accumulator
import org.apache.spark.AccumulatorParam

class MultiUnits (hiddenum:Int,binarylen:Int,numericlen:Int) {
  
  val UnitsBuffer = ArrayBuffer(Array(0.0))
  //Array(out,binary_hidden_unit,numeric_hidden_unit,out_unit)
  val Numeric = new Array[Double](numericlen)
  val Binary = new Array[Double](binarylen)
  val Out = new Array[Double](hiddenum+1)
 
  for(i <- Iterator.range(0,hiddenum-1))
    UnitsBuffer+=Binary
  UnitsBuffer+=Numeric
  UnitsBuffer+=Out
  
  val Units=UnitsBuffer.toArray
  
}