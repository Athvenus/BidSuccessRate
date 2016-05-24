package cn.ycmedia.BidSuccessRate

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics




class SimpleNuralNetworkTester (data:RDD[String],model:Array[Array[Double]]) {
  
   var dataExample = data.take(1)(0)
   val hiddenum = model.length - 2
   val binarylen = model(1).length
   
   val initialWeight = 0.01
   val rate = 0.01
   
   val initializer = new Initializer(dataExample,hiddenum,binarylen,initialWeight)
   val snn = new SimpleNuralNetwork(hiddenum,binarylen,rate)
  
   val examples = data.map(s => initializer.initd(s))
   
   val scoreAndLebels = examples.map(eg => (new snn.predictor(model,eg).predict,eg(0)(0)))
  
   val auc = new BinaryClassificationMetrics(scoreAndLebels).areaUnderROC()
   
   println("Test Auc is ",auc)
  
  
}