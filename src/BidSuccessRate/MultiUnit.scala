package BidSuccessRate

import scala.collection.parallel.mutable.ParArray


class MultiUnit(hiddenum:Int) {
  
  val Units = Array[Any](hiddenum+2)
  val Numeric = new Array[Double](100)
  val Binary = Array[Int](1000000)
  def create(hiddenum:Int):Array[Any] = {
    for(i <- Iterator.range(1,hiddenum))
      Units.update(i,Binary)
    Units.update(hiddenum+1,Numeric)
    Units  
  }  
  
}