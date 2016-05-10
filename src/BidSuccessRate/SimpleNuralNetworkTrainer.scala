package BidSuccessRate



import org.apache.spark.rdd.RDD
import scala.collection.mutable.ArrayBuffer



class SimpleNuralNetworkTrainer (data:RDD[String],iterations:Int,hiddenum:Int,binarylen:Int,initialweight:Double,rate:Double,convergence:Double) {
  
  val OutputLoss=ArrayBuffer(1.0)
  val HiddenLoss=ArrayBuffer(ArrayBuffer(1.0))
  for{i <- Iterator.range(1,hiddenum)}{
    HiddenLoss+=HiddenLoss(0)
  }
  
  while(convergence < 0.3){
     val f = new Initializer(hiddenum,binarylen,initialweight)
     val d = data.map(x => f.initd(x))
     
  }
  
}