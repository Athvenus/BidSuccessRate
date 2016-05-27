package cn.ycmedia.BidSuccessRate

import scala.collection.Iterator



class SimpleNuralNetwork (hiddenum:Int,binarylen:Int,rate:Double) extends Serializable{
  
    
  class predictor(weight:Array[Array[Double]],example:Array[Array[Double]]) extends Serializable{
    //Output Layer Predictor
    var predictedvalue = 0.0
    val simi_example = example.toBuffer.toArray
    for{i <- Iterator.range(0,hiddenum)}{
      val outputweight = weight(hiddenum+1)(i)
      val perceivedvalue = new LinearPerceptron (weight(i+1),example(i+1),"sigmoid").perception
      simi_example(hiddenum+1).update(i,perceivedvalue)
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
    val hiddenloss = new Array[Double](hiddenum)
    for{i <- Iterator.range(0,hiddenum)}{
      val perceivedvalue = new LinearPerceptron (weight(i+1),example(i+1),"sigmoid").perception
      val oloss = outputloss(weight,example)
      val hloss = perceivedvalue*(1-perceivedvalue)*(weight(hiddenum+1)(i)*oloss)
      hiddenloss.update(i,hloss)
    }
    hiddenloss
  }
  
  
  def update(weight:Array[Array[Double]],example:Array[Array[Double]]): Array[Array[Double]] = {
    val updated = weight.toBuffer.toArray
    for{i <- Iterator.range(1,hiddenum+1)}{
      for{j <- Iterator.range(0,weight(i).length)}{
        var w_ij=weight(i)(j);
        var delta_w_ij=0
        if(i < hiddenum+1){
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