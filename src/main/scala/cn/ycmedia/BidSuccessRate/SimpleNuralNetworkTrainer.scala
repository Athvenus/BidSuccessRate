package cn.ycmedia.BidSuccessRate

import cn.ycmedia.BidSuccessRate._
import scala.collection.Iterator
import scala.math.pow
import scala.math.sqrt
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.AccumulatorParam

import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics


class SimpleNuralNetworkTrainer (sc:SparkContext,data:RDD[String],iterations:Int,binarynum:Int,binarylen:Int,initialWeight:Double,rate:Double,batchSize:Int,convergence:Double) extends Serializable {
  
  //Initialize Overall Model
  var rmse = 1.0
  var dataExample = data.take(1)(0)
  var iteration = 0
  
  val initializer = new Initializer(dataExample,binarynum,binarylen,initialWeight)
  var example = initializer.initd(dataExample)
  var weight = initializer.initw
  val snn = new SimpleNuralNetwork(binarynum,binarylen,rate)
 
  object MultiUnitsAccumulatorParam extends AccumulatorParam[Array[Array[Double]]]{
    def zero(initialValue:Array[Array[Double]]):Array[Array[Double]] = {
      weight 
    }
    def addInPlace(A:Array[Array[Double]],B:Array[Array[Double]]):Array[Array[Double]] = {
      val sum = new Array[Array[Double]](A.length)
      for(i <- Iterator.range(0,A.length)){
        sum.update(i,new Array[Double](A(i).length))
        for(j <- Iterator.range(0,A(i).length)){
          sum(i).update(j,A(i)(j)+B(i)(j))
        }
      }
      sum
    }
  }
  
  var Weight = sc.accumulator(weight)(MultiUnitsAccumulatorParam)
  var MSE = sc.accumulator(pow(convergence,2))
  var NUM = sc.accumulator(1)
  var RMSE_HISTORY = new Array[Double](iterations)
  
  //Initialize Batch Model
  val numExample = data.count().toInt
  val numPartition = numExample/batchSize
  val examples = data.repartition(numPartition).map(s  => initializer.initd(s))
  
  println("These are basic initial parameters: rmse ",rmse,"dataExample ",dataExample,"example ",example,"weight ",weight)
  println("These are some basic statistics: numExample ",numExample,"numPartition ",numPartition,"MSE ",MSE,"NUM ",NUM)
  
  //Algorithm Body
  while(rmse >= convergence && iteration < iterations){
    
    var w0 = Weight.value 
    
    println("In this iteration,start weight is ",w0)
    
    def minus(A:Array[Array[Double]],B:Array[Array[Double]]):Array[Array[Double]] = {
      val difference = new Array[Array[Double]](A.length)
      for(i <- Iterator.range(0,A.length)){
        difference.update(i,new Array[Double](A(i).length))
        for(j <- Iterator.range(0,A(i).length)){
          difference(i).update(j,A(i)(j)-B(i)(j))
        }
      }
      difference
    }
    
    def updateBatchModel(iter:Iterator[Array[Array[Double]]]):Iterator[(Int,Double,Array[Array[Double]])] = {
      var Result = List[(Int,Double,Array[Array[Double]])]()
      var d = example
      var w = w0
      var L = 0.0
      var i = 0
      while(iter.hasNext){
        val eg = iter.next
        println("iter unit is",i,eg)
        w = snn.update(w,eg)
        d = new snn.predictor(w,eg).update
        //weight and data
        L += pow(new snn.predictor(w,eg).predict - eg(0)(0),2)
        i += 1
      }
      println("In this partition,Length is ",i,"MSE is",MSE)
      Result.::((i,L,minus(w,w0))).iterator
      //minus(w0,w) Gradient Descend
    }
    
    val examples = data.repartition(numPartition).map(s  => initializer.initd(s))
    var updates = examples.mapPartitions(updateBatchModel).collect
          
    //Update Model Errors
    for(i <- Iterator.range(0,updates.length)){
      NUM += updates(i).productElement(0).asInstanceOf[Int]
      MSE += updates(i).productElement(1).asInstanceOf[Double]
      Weight += updates(i).productElement(2).asInstanceOf[Array[Array[Double]]]
    }
    
    rmse = sqrt(MSE.value/NUM.value)
    
    RMSE_HISTORY.update(iteration,rmse)
    
    println("This is iteration ",iteration,"MSE is",MSE.value,"NUM is",NUM.value,"rmse is",rmse)
    
    //Update Iteration Numbers
    MSE.add(-MSE.value+pow(convergence,2))
    
    NUM.add(-NUM.value+1)
    
    iteration+=1
  
  }
  
  
 //OutputArrangement 
  val model = Weight.value
  val simi_examples = examples.map(eg => new snn.predictor(model,eg).update)
  val scoreAndLabels = examples.map(eg => (new snn.predictor(model,eg).predict,eg(0)(0)))
  val auc = new BinaryClassificationMetrics(scoreAndLabels).areaUnderROC()
  
  //println("Some Simi_examples Is",simi_examples.take(1))
  //println("Some Predict Examples Is",scoreAndLabels.take(10))
  val pres = scoreAndLabels.take(10)
  for(i <- Iterator.range(0,10)){
    println(i,pres(i))
  }
  println("Training Auc Is",auc)
  println("RMSE_HISTORY is",RMSE_HISTORY)
  for(i <- Iterator.range(0,10)){
    println(i,RMSE_HISTORY(i))
  }
  
  
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
  //write style like flow
  /***
   * var updates = examples.mapPartitions{ egs => {
      
      var Result = List[(Int,Double,Array[Array[Double]])]()
      
      var d = example
      var w = w0
      var L = 0.0
      var i = 0
      
      while(egs.hasNext){
        val eg = egs.next
        println("iter unit is",i,eg)
        w = snn.update(w,eg)
        d = new snn.predictor(w,eg).update
        //weight and data
        L += pow(new snn.predictor(w,eg).predict - eg(0)(0),2)
        i += 1
      }
      
      Result.::(i,L,minus(w,w0)).iterator
      
      }
    }
   */
  //one kind of wrong
  /***
   * def updateBatchModel(iter:Iterator[Array[Array[Double]]]):Iterator[Seq[Array[Array[Double]]]] = {
      //Update weight,predict and hidden example values
      var w = w0
      var d = example
      var M = Iterator(Seq(w,d)) 
      var L = 0.0
      var i = 0
      M.next
      while(iter.hasNext){
        val eg = iter.next
        println("iter unit is",i,eg)
        w = snn.update(w,eg)
        d = new snn.predictor(w,eg).update
        //weight and data
        M ++= Iterator(Seq(w,d))
        L += pow(new snn.predictor(w,eg).predict - eg(0)(0),2)
        i += 1
      }
      //Update The Overall Model,Maybe RMSE
      println(minus(w,w0))
      Weight.add(minus(w,w0))
      MSE += L
      NUM.add(i)
      println("In this partition,loss sum",L,"iterator length is",i)
      M
    }
   */
}