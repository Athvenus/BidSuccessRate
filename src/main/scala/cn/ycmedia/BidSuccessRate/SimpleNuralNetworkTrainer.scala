package cn.ycmedia.BidSuccessRate


import cn.ycmedia.BidSuccessRate._
import scala.collection.Iterator
import scala.math.pow
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.AccumulatorParam



class SimpleNuralNetworkTrainer (sc:SparkContext,data:RDD[String],iterations:Int,hiddenum:Int,binarylen:Int,initialWeight:Double,rate:Double,batchSize:Int,convergence:Double) extends Serializable {
  
  //Initialize Overall Model
  var rmse = 1.0
  var dataExample = data.take(1)(0)
  val initializer = new Initializer(dataExample,hiddenum,binarylen,initialWeight)
  var example = initializer.initd(dataExample)
  var weight = initializer.initw
  var iteration = 0
  
  object MultiUnitsAccumulatorParam extends AccumulatorParam[Array[Array[Double]]]{
    def zero(initialValue:Array[Array[Double]]):Array[Array[Double]] = {
      weight 
    }
    def addInPlace(A:Array[Array[Double]],B:Array[Array[Double]]):Array[Array[Double]] = {
      val sum = A.toBuffer.toArray
      for(i <- Iterator.range(0,A.length)){
        for(j <- Iterator.range(0,A(0).length)){
          sum(i).update(j,A(i)(j)+B(i)(j))
        }
      }
      sum
    }
  }
  
  var Weight = sc.accumulator(weight)(MultiUnitsAccumulatorParam)
  var MSE = sc.accumulator(0.0)
  var NUM = sc.accumulator(1)
  
  //Initialize Batch Model
  val numExample = data.count().toInt
  val numPartition = numExample/batchSize
  
  println("These are basic initial parameters: rmse ",rmse,"dataExample ",dataExample,"example ",example,"weight ",weight)
  println("These are some basic statistics: numExample ",numExample,"numPartition ",numPartition,"MSE ",MSE,"NUM ",NUM)
  
  //Algorithm Body
  while(rmse > convergence && iteration < iterations){
    
    val snn = new SimpleNuralNetwork(hiddenum,binarylen,rate)
    
    def updateBatchModel(iter:Iterator[Array[Array[Double]]]):Iterator[Seq[Array[Array[Double]]]] = {
      //Update weight,predict and hidden example values
      var w = Weight.value
      var d = example
      var p = new snn.predictor(w,d)
      var M = Iterator(Seq(w,d)) 
      val len = iter.length
      var L = 0.0
      M.next
      for(i <- Iterator.range(0,len-1)){
        val eg = iter.next
        println("iter unit is",i,eg)
        w = snn.update(w,eg)
        p = new snn.predictor(w,eg)
        d = p.update
        M ++= Iterator(Seq(w,d))
        L += pow(p.predict - eg(0)(0),2)
      }
      Weight.add(minus(w,Weight.value))
      MSE.add(L)
      NUM.add(len)
      println("In this partition,loss sum",L,"iterator length is",len)
      M
      //Do Something To Update The Overall Model,Maybe RMSE
    }
    
    def minus(A:Array[Array[Double]],B:Array[Array[Double]]):Array[Array[Double]] = {
      val difference = A.toBuffer.toArray
      for(i <- Iterator.range(0,A.length)){
        for(j <- Iterator.range(0,A(0).length)){
          difference(i).update(j,A(i)(j)-B(i)(j))
        }
      }
      difference
    }  
    
    val examples = data.repartition(numPartition).map(s  => initializer.initd(s))
    
    var models = examples.mapPartitions(updateBatchModel)
    
    var predicts = models.map(m => m(0))
    
    val simi_examples = models.map(m => m(1))
    
    val losses = models.map(m => minus(m(1),m(0)))
        
    rmse = MSE.value/NUM.value
    
    iteration+=1
    
    println("This is iteration ",iteration,"rmse is",rmse)
   
  }
  
  
 //OutputArrangement 
  val model = Weight.value
  
  println("Model Training Finished.")
  
  
  
  //This part output some evaluation metrics
  //Include hidden loss changing curve,out loss changing curve,
  //Include training AUC FScore ROC etc.
  //Include hidden predict values
  
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