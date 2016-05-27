package cn.ycmedia.BidSuccessRate

import scala.collection.Iterator



class SimpleNuralNetwork (binarynum:Int,binarylen:Int,rate:Double) extends Serializable{
  
    
  class predictor(weight:Array[Array[Double]],example:Array[Array[Double]]) extends Serializable{
    //Output Layer Predictor
    var predictedvalue = 0.0
    val simi_example = new Array[Array[Double]](example.length)
    for(i <- Iterator.range(0,example.length)){
      simi_example.update(i,new Array[Double](example(i).length))
    }
    for{i <- Iterator.range(1,binarynum+2)}{
      val outputweight = weight(binarynum+2)(i-1)
      //Signs Come From Input Array
      val perceivedvalue = new LinearPerceptron (weight(i),example(i),"sigmoid").perception
      simi_example(binarynum+2).update(i-1,perceivedvalue)
      predictedvalue += outputweight*perceivedvalue
 
    }
    def predict:Double = {
      weight(0).update(0,predictedvalue)
      predictedvalue
    }
    def update:Array[Array[Double]] = {
      simi_example
    }
  }
  
  
  def outputloss(weight:Array[Array[Double]],example:Array[Array[Double]]):Double = {
    val predictedvalue = new predictor(weight,example).predict
    val outputloss = predictedvalue*(1-predictedvalue)*(predictedvalue-example(0)(0))
    outputloss
  }
 
  
  def hiddenloss(weight:Array[Array[Double]],example:Array[Array[Double]]):Array[Double] = {
    val hiddenloss = new Array[Double](binarynum)
    for{i <- Iterator.range(0,binarynum)}{
      val perceivedvalue = new LinearPerceptron (weight(i+1),example(i+1),"sigmoid").perception
      val oloss = outputloss(weight,example)
      val hloss = perceivedvalue*(1-perceivedvalue)*(weight(binarynum+1)(i)*oloss)
      hiddenloss.update(i,hloss)
    }
    hiddenloss
  }
  
  
  def update(weight:Array[Array[Double]],example:Array[Array[Double]]): Array[Array[Double]] = {
    val updated = weight.toBuffer.toArray
    for{i <- Iterator.range(1,binarynum+1)}{
      for{j <- Iterator.range(0,weight(i).length)}{
        var w_ij=weight(i)(j);
        var delta_w_ij=0
        if(i < binarynum+1){
          var delta_w_ij = hiddenloss(weight,example)
        }else{
          var delta_w_ij = outputloss(weight,example)
        }
        w_ij+=rate*delta_w_ij*example(i)(j)
        updated(i).update(j,w_ij)
      }
    } 
    updated
  }
  
  
}