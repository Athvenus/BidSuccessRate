package cn.ycmedia.FeatureExtract

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import scala.collection.Iterator
import org.apache.spark.sql.types.{StructType, StructField, IntegerType, DoubleType}
import org.apache.spark.sql.Row

class SingleVariableAnalyzer (data:DataFrame,sqlContext:SQLContext,sc:SparkContext) {
  
  def distribute(colName:String):DataFrame = {
    val sum = data.count()
    val hist = data.groupBy(data.col(colName)).count().sort(asc(colName))
    val dist = hist.withColumn("count",hist.col("count")/sum).withColumnRenamed("count",colName+"_dist")
    val distribution = dist.select(dist(colName),dist(colName+"_dist").cast("double"))
    //distribution is a DataFrame
    distribution
  }
  
  def cdf(colName:String):DataFrame = {
    //Cumulant Distribution Function
    val dist = distribute(colName)
    var s = 0.0
    val col = dist.map(t => t(0).asInstanceOf[Int]).collect()
    val prob = dist.map(t => t(1).asInstanceOf[Double]).collect()
    val len = col.length
    val cumul = new Array[Double](len)
    for(i <- Iterator.range(0,len)){
      if(i < 1){
        cumul.update(i,prob(i))
      }else{
        cumul.update(i,cumul(i-1)+prob(i))
      }
    }
    var rdd = Seq(Row(col(0),cumul(0)))
    for(i <- Iterator.range(1,len)){
      rdd = rdd:+Row(col(i),cumul(i))
    }
    val struct =
      StructType(
        StructField(colName,IntegerType,true)::
        StructField(colName+"_cdf",DoubleType,true)::Nil)  
    val cdf = sqlContext.createDataFrame(sc.parallelize(rdd),struct)
    
    //Forget This Method:dist.registerTempTable("dist")
    //sqlContext.sql("select colName,sum(dist) as cdf from dist group by colName having colName <= $colName")  
    cdf
  }
  
  def density(x:Int,colName:String):Double={
    val Precision = 0.01
    val dis = distribute(colName)
    val density = dis.filter("colName <= x + Precision and colName >= x - Precision").select(colName+"_dist").map(t => t(0).asInstanceOf[Double]).collect()
    density.sum
  }
  
  def cumulant(x:Int,colName:String):Double={
    val dis = distribute(colName)  
    val cumulant = dis.filter("colName <= x").select(colName+"_dist").map(t => t(0).asInstanceOf[Double]).collect()
    cumulant.sum
  }
  

}