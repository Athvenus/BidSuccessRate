package data_extract

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object tt {
  val conf=new org.apache.spark.SparkConf().setMaster("FY_LLJ")
  val sc=new org.apache.spark.SparkContext()
  //val dd=sc.parallelize(Array(1,2,3))
  val a=Vector(1,2,3)
  //println(sc)
  println(a)
  //println(dd)
  
}