package FeatureExtract

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._


class SingleVariableAnalyzer (data:DataFrame) {
  
  val sum=data.count()
  val hist=data.groupBy(data.col("L")).count().sort(asc("L"))
  val distribution=hist.withColumn("count",hist.col("count")/sum).withColumnRenamed("count","distribution")
  //distribution is a DataFrame
  
  def probability(x:Int):Double={
    val Precision=0.01
    distribution.filter($"L" <= x + Precision and $"L" >= x - Precision)
  }
  
  def density(x:Int):Double={
    distribution.filter($"L" <= x)
  }
}