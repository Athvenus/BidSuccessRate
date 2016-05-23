package BidSuccessRate.main.scala


import BidSuccessRate._
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD


object BidSuccessRateTrain {
  def main(args:Array[String]) {
    val sc=new SparkContext()
    val documentPath = ""
    val data = sc.textFile(documentPath)
    val trainer = new SimpleNuralNetworkTrainer(sc,data,10,2,1000000,0.01,0.01,10000,0.005)
    //sc:SparkContext,data:RDD[String],iterations:10,hiddenum:2,binarylen:1000000,initialWeight:0.01,rate:0.01,batchSize:10000,convergence:0.005
    val model = trainer.model   
    println("Model Training Finished.")
  }
}