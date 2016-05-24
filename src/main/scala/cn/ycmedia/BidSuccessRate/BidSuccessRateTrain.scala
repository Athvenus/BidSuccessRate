package cn.ycmedia.BidSuccessRate



import org.apache.spark.SparkContext


object BidSuccessRateTrain {
  def main(args:Array[String]) {
    val sc=new SparkContext()
    val documentPath = "/opt/dmp/mspace/bsr/160517"
    val data = sc.textFile(documentPath)
    val trainer = new SimpleNuralNetworkTrainer(sc,data,10,2,1000000,0.01,0.01,10000,0.005)
    //sc:SparkContext,data:RDD[String],iterations:10,hiddenum:2,binarylen:1000000,initialWeight:0.01,rate:0.01,batchSize:10000,convergence:0.005
    val model = trainer.model   
    println("Model Training Finished.")
  }
}