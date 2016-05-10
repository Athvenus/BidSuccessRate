package BidSuccessRate

import org.apache.spark.mllib.linalg.Vector
import scala.math._

class LinearPerceptron (weight:Array[Double],example:Array[Double],delta:String) {
  
  def unitf:Double = {
      //DistributedMatrix & ParArray While Needed
      val size=weight.size
      val inner=new Array[Double](size)
      for(i <- Iterator.range(0,size))
        inner.update(i,weight(i)*example(i))
      inner.sum
    }
  
  def squash:Double = {
    delta match {
      case "sigmoid" => sigmoid(unitf)
      case "tanh" => tanh(unitf)
    }
  }
  
  def sigmoid(x:Double):Double=1/(1+exp(-x))
  def tanh(x:Double):Double=(exp(x)-exp(-x))/(exp(x)+exp(-x))
  
  val perception = squash
  
}