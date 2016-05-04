package BidSuccessRate


import org.apache.spark.mllib.linalg.Vector
import scala.math._


class Perceptron(weight:Vector,example:Vector,delta:String) {
  
  def unitf(weight:Vector,example:Vector):Double = {
      //DistributedMatrix & ParArray While Needed
      val size=weight.size
      val inner=new Array[Double](size)
      for(i <- Iterator.range(0,size))
        inner.update(i,weight(i)*example(i))
      inner.sum
    }
  
  def squash(delta:String){
    delta match {
      case "sigmoid" => sigmoid(unitf(weight,example))
      case "tanh" => tanh(unitf(weight,example))
    }
  }
  
  def sigmoid(x:Double):Double=1/(1+exp(-x))
  def tanh(x:Double):Double=(exp(x)-exp(-x))/(exp(x)+exp(-x))
  
  val perception = squash(delta)
}