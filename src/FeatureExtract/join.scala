package data_extract


import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.hadoop.mapred
import org.apache.hadoop.hive.ql.io.orc.{OrcInputFormat,OrcOutputFormat}
import org.apache.hadoop.hive.ql.io.orc.OrcSerde
import org.apache.hadoop.hive.ql.io.orc.RecordReader
import org.apache.hadoop.hive.ql.io.orc.OrcFile
import org.apache.hadoop.hive.ql.io.orc.OrcStruct
import org.apache.hadoop.hive.serde2.objectinspector.StructField
//import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector
import org.apache.hadoop.mapred.FileInputFormat
import org.apache.hadoop.mapred.RecordReader

import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.io.{ArrayWritable,BooleanWritable,ByteWritable,CompressedWritable,DoubleWritable,EnumSetWritable,FloatWritable,GenericWritable,IntWritable,LongWritable,MapWritable,NullWritable,ShortWritable,SortedMapWritable,TwoDArrayWritable,VersionedWritable,VIntWritable}

import org.apache.hadoop.hive.conf.HiveConf
import org.apache.hadoop.mapred.Mapper
import org.apache.hadoop.hive.ql.io
import org.apache.hadoop.tools.proto
import org.apache.hadoop.tools.protocolPB
import org.apache.hadoop.ipc.proto


import org.apache.spark.sql.Row
import org.apache.spark.sql.Column
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types._
import org.apache.spark.sql._

import org.apache.spark.sql.hive.HiveContext


class join(bidPath:String,winPath:String) {
  //bidPath="/user/hive/warehouse/bdl_dmp/plat=baidu/ds=2016-04-18/rt=14"
  //winPath="/user/hive/warehouse/bdl_dmp/plat=baidu/ds=2016-04-18/rt=1"
  val sc= new SparkContext()
  val bidFile=sc.hadoopFile[NullWritable,OrcStruct,OrcInputFormat](bidPath)
  val bidRDD=bidFile.map(pair => pair._2.toString)
  val winFile=sc.hadoopFile[NullWritable,OrcStruct,OrcInputFormat](winPath)
  val winRDD=winFile.map(pair => pair._2.toString)
  
  val sqlContext = new SQLContext(sc)
  import sqlContext.implicits._
  winRDD.toDF()
  
  val hiveContext = new HiveContext(sc)
  val df=hiveContext.read.orc(winPath)
  //DataFrame without Standard Schema
  val schema=df.schema
  //DataFrame Schema Description
  //Do not forget Java subString
  
  val s3=
    StructType(
        StructField("A",IntegerType,true)::
        StructField("F",IntegerType,false)::
        StructField("G",IntegerType,true)::Nil)
        
  val a=sc.parallelize(Seq(Row(1,2,3),Row(4,5,6)))
  
  val adf=sqlContext.createDataFrame(a,s3)

  val windf = sqlContext.createDataFrame(winRDD,s3)
  val biddf = sqlContext.createDataFrame(bidRDD,s3)
  
  //Join Operation
  val joinedf =biddf.join(windf,Seq("A"),"left_outer")
  
  //Add a new column
  val winedf = windf.withColumn("L",windf("A")-windf("A")+1)
 
  //Get Schema of DataFrame
  val winedfschema=winedf.schema
  
  //Group Data
  val gwinedf=winedf.groupBy(winedf.col("L")).count()
  //sql.Row Formated DataFrame Getted
  
  
  //Rename a Column
  val rwinedf=winedf.withColumnRenamed("L","l")
  
  
  val x=bsrfeature.map(x:Row => x.toSeq)
  
  
  def feature_extrct()={}
  
  val bsrfeature=joinedf.map(x => feature_extrct(x))
  
  
  
  
  bsrfeature.toDF().insertInto("bsr_bds_snn")
  hiveContext.sql("insert into bsr_bds_snn patition() select ")
  
}

