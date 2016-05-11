package FeatureExtract


import scala.io.Source

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.Row
import org.apache.spark.sql.DataFrame

import org.apache.spark.sql.hive.HiveContext
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.hive.ql.io.orc.OrcInputFormat
import org.apache.hadoop.hive.ql.io.orc.OrcStruct

import org.apache.spark.rdd.RDD

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


class DataFrameLoader (Context:Int,fileType:Int,filePath:String) {
  //Get DataFrame or RDD
  val conf = new SparkConf().setAppName("appName").setMaster("master")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)
  val hiveContext = new HiveContext(sc)
  
  import sqlContext.implicits._
  
  def readconf = {
    
  }
  
  def torow(str:String):Row = {
    val s=str.substring(1,str.length-1).split(",")
    for(i <- Iterator.range(0,s.length)){
      
    }
    val a=Row(1,2,3)
    a
    //Need To Write Again
  }
  
  def read:DataFrame = {
    (Context:Int,fileType:Int) match {
      //case (1,1) => val file = sc.textFile(filePath)
      case (1,2) => { 
        val file = sc.hadoopFile[NullWritable,OrcStruct,OrcInputFormat](filePath)
        val rdd = file.map(pair => pair._2.toString).map(str => torow(str))
        val schema = new BidDataSchema().struct
        val df = sqlContext.createDataFrame(rdd,schema)
        df
      }
      //case (1,3) => 
      //case (2,1) => 
      //case (2,2) => 
      //case (2,3) => 
      //case (3,1) => 
      case (3,2) => {
        val df=hiveContext.read.orc(filePath) 
        df
      }
      //case (3,3) =>
    }
  }
  
  
}


 