package BidSuccessRate


import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD




object BidSuccessRateTrainer extends App {
  
  class Spliter(data:RDD[String]){
    
    val feat=
      data.map(str => str.split("\\|"))
      //map(arr => 
        //for{i <- Iterator.range(1,arr.length)}{
          //arr.update(i,arr(i).split("\t"))
        //}
      //)    
 
  }
  
}