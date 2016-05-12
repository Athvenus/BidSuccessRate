package BidSuccessRate


import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import scala.collection.mutable.ArrayBuffer



class SimpleNuralNetworkTrainer (data:RDD[String],iterations:Int,hiddenum:Int,binarylen:Int,initialweight:Double,rate:Double,convergence:Double) {
  
  
  val sc = new SparkContext()
  val OutputLoss=ArrayBuffer(1.0)
  val HiddenLoss=ArrayBuffer(ArrayBuffer(1.0))
  for{i <- Iterator.range(1,hiddenum)}{
    HiddenLoss+=HiddenLoss(0)
  }
  
  while(convergence < 0.3){
     val f = new Initializer(hiddenum,binarylen,initialweight)
     val d = data.map(x => f.initd(x))
     
     var convergence = sc.accumulator(0)
     
  }
  
  def toDouble(x:String):Double = {
    val a=0.1
    a
  }
  
  
  //data.map(x => toDouble(x)).foreachPartition(x => convergence+=x)

  def loss(real:Double,predict:Double):Double = {
    predict
    
  }
  
}