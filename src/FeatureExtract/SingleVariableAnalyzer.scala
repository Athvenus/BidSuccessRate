package FeatureExtract

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._


class SingleVariableAnalyzer (data:DataFrame) {
  
  def distribute(colName:String):DataFrame = {
    val sum=data.count()
    val hist=data.groupBy(data.col(colName)).count().sort(asc(colName))
    val distribution=hist.withColumn("count",hist.col("count")/sum).withColumnRenamed("count","dist")
    //distribution is a DataFrame
    distribution
  }
  
  def density(x:Int,colName:String):Array[Any]={
    val Precision = 0.01
    val dis = distribute(colName)
    val density = dis.filter("colName <= x + Precision and colName >= x - Precision").select("dist").map(t => t(0)).collect()
    density
  }
  
  def cumulant(x:Int,colName:String):Array[Any]={
    val dis = distribute(colName)  
    val cumulant = dis.filter("colName <= x").select("dist").map(t => t(0)).collect()
    cumulant
  }
}