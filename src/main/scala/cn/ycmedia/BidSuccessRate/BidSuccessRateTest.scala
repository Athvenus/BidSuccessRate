package cn.ycmedia.BidSuccessRate


import org.apache.spark.SparkContext


object BidSuccessRateTest {
  def main(args:Array[String]) {
     val sc=new SparkContext()
     val documentPath = "/opt/dmp/mspace/bsr/160517"
     val modelPath = ""
     val data = sc.textFile(documentPath).randomSplit(Array(0.7, 0.3), seed = 11L)
     val testing = data(1)
     val model = Array(Array(0.0))
     //val model = sc.textFile(modelPath).collect()(0)
     val test = new SimpleNuralNetworkTester(testing,model)
   }
  
  
  def modelRead(s:String):Array[Array[Double]] = {
    val a = Array(Array(0.0))
    a
  }
  
}