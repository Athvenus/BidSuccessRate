package Utils

import scala.io.Source

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.hive.ql.io.orc.OrcInputFormat
import org.apache.hadoop.hive.ql.io.orc.OrcStruct

import org.apache.spark.rdd.RDD


class DataFrameLoader (Context:Int,FileType:Int,FilePath:String) {
  //Get DataFrame or RDD
  val conf = new SparkConf()
  val sc = new SparkContext()
  
  
  def readconf(){}
  
  def read(t:Seq[Int]): match {
    val 
    
    
  }
  
  
  
}

/***
 * Context={
 * SparkContext:1
 * SqlContext:2
 * HiveContext:3}
 * 
 * FileType={
 * text:1
 * orc:2
 * parquet:3
 * } 
 * 
 */
 