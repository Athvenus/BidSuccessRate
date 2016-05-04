package BidSuccessRate

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.SparseVector
import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.mllib.linalg.DenseMatrix
import org.apache.spark.mllib.linalg.Matrix

//import org.apache.spark.util.Vector

import org.apache.spark.mllib.linalg.distributed.BlockMatrix
import org.apache.spark.mllib.linalg.distributed.CoordinateMatrix
import org.apache.spark.mllib.linalg.distributed.DistributedMatrix
import org.apache.spark.mllib.linalg.distributed.RowMatrix


import scala.collection.parallel.mutable.ParArray
import scala.collection.parallel.immutable.ParVector
import scala.collection.{Iterator,BufferedIterator}
import scala.collection.immutable.{StreamIterator,VectorIterator}
import scala.xml.pull.ProducerConsumerIterator

import scala.math.exp

class SimpleNuralNetwork (data:RDD[Vector],eta:Double,hiddenum:Int,hiddenlen:Int) {
  
  
  val units=new MultiUnit(3,100)
  
  class Data{
    
  }
  
  class Weight{
    def initialize
    def update

    
  }
  
  def initialize(data:RDD[Vector]):Vector = {
    //Initialize weights to 0.05
    val length=data.take(1)(0).size
    val ar=new Array[Double](length)
    val weight=new DenseVector(ar.map(_ +0.05))
    weight
  }
    
  class ExampleSpliter(example:Vector,slipter:Seq[Int]){
    val trafficratiox1 = new DenseVector(example.toArray.slice(slipter(0),slipter(1)))
    val trafficratiox2 = new DenseVector(example.toArray.slice(slipter(0),slipter(1)))
    val bidprice = new DenseVector(example.toArray.slice(slipter(1),slipter(2)))
    val output = new DenseVector(example.toArray.slice(slipter(2),slipter(3)))
    
  }
  
  def update(example:Vector,weight:Vector,gradient:Vector){
    val outerror=example(99)*(1-example(99))*(1-example(99))
    val latenterror=example(98)*outerror
  }
  
  class model {
    def unitf(weight:Vector,example:Vector):Double = {
      //DistributedMatrix & ParArray While Needed
      val size=weight.size
      val inner=new Array[Double](size)
      for(i <- Iterator.range(0,size))
        inner.update(i,weight(i)*example(i))
      inner.sum
    }  
    
    //Rewrite this part
    def sigmoid(x:Double):Double=1/(1+exp(-x))
    def tanh(x:Double):Double=(exp(x)-exp(-x))/(exp(x)+exp(-x))
    
    def snnf(alpha:Vector,beta:Vector,gamma:Vector,ig1:Vector,ig2:Vector,ig3:Vector):Double = {
       val t1=sigmoid(unitf(alpha,ig1))
       val t2=sigmoid(unitf(beta,ig2))
       val t3=sigmoid(unitf(gamma,ig3))
       t3*(t1+t2)     
      
    }
    
  }
  
  class Perceptron {
    //Two Perception Functions 
    def sigmoid(x:Double):Double=1/(1+exp(-x))
    def tanh(x:Double):Double=(exp(x)-exp(-x))/(exp(x)+exp(-x))
  }
  
  
}