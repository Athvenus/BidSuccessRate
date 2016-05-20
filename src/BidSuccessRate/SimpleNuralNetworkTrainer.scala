package BidSuccessRate


import scala.collection.Iterator
import scala.collection.mutable.ArrayBuffer

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.Accumulator
import org.apache.spark.AccumulatorParam

import org.apache.spark.mllib.evaluation.RegressionMetrics
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics

import org.apache.spark.TaskContext
import org.apache.spark.scheduler.SparkListenerTaskStart
import org.apache.spark.scheduler.SparkListenerTaskEnd
import org.apache.spark.scheduler.TaskInfo
import org.apache.spark.status.api.v1.TaskData
import org.apache.spark.status.api.v1.TaskMetricDistributions
import org.apache.spark.status.api.v1.TaskMetrics
import org.apache.spark.status.api.v1.TaskSorting
import org.apache.spark.util.TaskCompletionListener




class SimpleNuralNetworkTrainer (sc:SparkContext,data:RDD[String],iterations:Int,hiddenum:Int,binarylen:Int,initialWeight:Double,rate:Double,batchSize:Int,convergence:Double) {
  
  //Initialize Overall Model
  var rmse = 1.0
  var dataExample = data.take(1)(0)
  val initializer = new Initializer(dataExample,hiddenum,binarylen,initialWeight)
  var example = initializer.initd(dataExample)
  var weight = initializer.initw
  var iteration = 0
  
  //Initialize Batch Model
  val numExample = data.count().toInt
  val numPartition = numExample/batchSize
  
  //Algorithm Body
  while(rmse > convergence && iteration > iterations){
    
    val snn = new SimpleNuralNetwork(hiddenum,binarylen,rate)
    
    var examples = data.repartition(numPartition).map(s  => initializer.initd(s))
    
    var models = examples.mapPartitions(updateBatchModel)
    
    var predicts = models.map(m => m(0))
    
    val simi_examples = models.map(m => m(1))
    
    val losses = models.map(m => minus(m(1),m(0)))
    
    def updateBatchModel(iter:Iterator[Array[Array[Double]]]):Iterator[Seq[Array[Array[Double]]]] = {
      //Update weight,predict and hidden example values
      var w = weight
      var d = example
      var M = Iterator(Seq(w,d)) 
      M.next
      for(i <- Iterator.range(0,iter.length)){
        val eg = iter.next
        w = snn.update(w,eg)
        d = new snn.predictor(w,eg).update
        M ++= Iterator(Seq(w,d))
      }
      M
      //Do Something To Update The Overall Model,Maybe RMSE
    }
    
    
    def getModelUpdate(iter:Iterator[Array[Array[Double]]]):Array[Array[Double]] = {weight}
    def getModelError(iter:Iterator[Array[Array[Double]]]):Array[Array[Double]] = {weight}
    
    def minus(A:Array[Array[Double]],B:Array[Array[Double]]):Array[Array[Double]] = {
      val difference = A.toBuffer.toArray
      for(i <- Iterator.range(0,A.length)){
        for(j <- Iterator.range(0,A(0).length)){
          difference(i).update(j,A(i)(j)-B(i)(j))
        }
      }
      difference
    }
    
    rmse+=0
    iteration+=1
    
  }
  


  object MultiUnitsAccumulatorParam extends AccumulatorParam[Array[Array[Double]]]{
    def zero:Array[Array[Double]] = {
      initializer.initw
    }
    def addInPlace(A:Array[Array[Double]],B:Array[Array[Double]]):Array[Array[Double]] = {
      minus(A,B)
    }
  }
  
  val model = sc.accumulator(initializer.initw)(MultiUnitsAccumulatorParam)
  //every partition update will be executed in this model
  
  val init_weight = sc.broadcast()
  
  //Broadcast the init weight and example to all of the machines
  


  val snn = new SimpleNuralNetwork (2,1000,0.05)
  val examples = data.map(s => initializer.initd(s))
  
  
    
    val updatedWeight = initialWeight
    
    //This function will be execute total update
    
    def minus(A:Array[Array[Double]],B:Array[Array[Double]]):Array[Array[Double]] = {
      val difference = A.toBuffer.toArray
      for(i <- Iterator.range(0,A.length)){
        for(j <- Iterator.range(0,A(0).length)){
          difference(i).update(j,A(i)(j)-B(i)(j))
        }
      }
      difference
    }
    
  
  class OutputArrangement
  //This part output some evaluation metrics
  //Include hidden loss changing curve,out loss changing curve,
  //Include training AUC FScore ROC etc.
  //Include hidden predict values
  
  val examples_num = data.count() 
  val iteration_nums = 100
  val patitions_nums = (examples_num/iteration_nums).toInt
  val d = data.repartition(patitions_nums)
  
  val OutputLoss = ArrayBuffer(1.0)
  val HiddenLoss = ArrayBuffer(ArrayBuffer(1.0))
  for{i <- Iterator.range(1,hiddenum)}{
    HiddenLoss += HiddenLoss(0)
  }
  data.repartition(45)
  
  //val weight = sc.accumulator(0)
  
  //data.map(x => toDouble(x)).foreachPartition(x => convergence+=x)


  
  //Accumulator need SparkContext
  //If we decide to write an independent class then we want to reuse which part 
  //We want to reuse data this part,and actually we need to re write initializer 
  //If we want to write an new parallel method we can write an new class
  //As the data is defined it seems that we have no need to write this parallel method in a class
  //If we write this parallel method in a class
  //But Evaluator could be reused but it is really simple as if we have no need to class it but test and train will use it either
  //So we do not to write it twice so it is better that we write a new class
  //Accumulator is in the external part of iterator and convergence and partitions
  //This is a real algorithm
  //Once a class need to be used in spark we need SparkContext this parameter,once you do not use spark you have not need for this parameter
  //So if number of parameter could be change is OK 
  
  //iter.next means an example
  //this function will be execute several times in a partition
  //convergence will be checked once in one iteration
  //model will be updated once in one iteration
  //every iterator batch weight will update once 
  //every batch could be executed several times which depends on iteration number
  //iterator number means this updating will be executed how many times
  //every example will use SNN core method to execute gradient descend
  //so we need a accumulator to update main model in the cluster
  //Iterator could be used in one batch or in all batches or in a mixed way
  //The meaning of batch is save updated result
  //How to update the convergence condition
  //Just update the data bricks
  //error need to collect from every partition
  
}