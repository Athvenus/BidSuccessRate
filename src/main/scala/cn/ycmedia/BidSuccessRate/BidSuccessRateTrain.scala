package cn.ycmedia.BidSuccessRate


import org.apache.spark.SparkConf
import org.apache.spark.SparkContext


object BidSuccessRateTrain {
  def main(args:Array[String]) {
    val conf = new SparkConf().set("spark.driver.maxResultSize","5G")
    val sc=new SparkContext(conf)
    val documentPath = "/opt/dmp/mspace/bsr/160522/part-00000"
    val data = sc.textFile(documentPath).randomSplit(Array(0.5, 0.5), seed = 11L)
    val training = data(0)
    val trainer = new SimpleNuralNetworkTrainer(sc,training,10,2,500000,0.01,0.01,50000,0.005)
    //sc:SparkContext,data:RDD[String],iterations:10,hiddenum:2,binarylen:1000000,initialWeight:0.01,rate:0.01,batchSize:10000,convergence:0.005
    val model = trainer.model   
    println("Model Training Finished.model ",model)
  }
}