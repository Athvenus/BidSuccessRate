package FeatureExtract

import FeatureExtract.SingleVariableAnalyzer

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SQLContext

import org.apache.spark.mllib.stat.{MultivariateStatisticalSummary,Statistics}
import org.apache.spark.sql.DataFrameStatFunctions
import org.apache.spark.sql.ColumnName
import org.apache.spark.mllib.random.ExponentialGenerator

class bidRelevant (data:DataFrame) {
  
  val bidRelevantDataFrame=data.select("ex_id","mininum_cpm","win_price","max_cpm")
  
  def singleVariable (column:DataFrame):DataFrame = {
    val distribution = new SingleVariableAnalyzer(column).distribution
    distribution
  }
  
  def correlative (d:DataFrame):DataFrame = {
    val columns=d.columns
    val one=d.withColumn("one",d("mininum_cpm")-d("mininum_cpm")+1)
    val correlative=d.select(d("ex_id"),d("one")-d("mininum_cpm")/d("win_price"),d("one")-d("mininum_cpm")/d("max_cpm"),d("one")-d("win_price")/d("max_cpm"))
    correlative
  }
  
  def collect(d:DataFrame):DataFrame = {
    /***
     * Join original distribution and correlative DataFrames
     */
    //Three Distributions
    val mininum_cpm_dis=singleVariable(d.select(d("mininum_cpm")))
    val win_price_dis=singleVariable(d.select(d("win_price")))
    val max_cpm_dis=singleVariable(d.select(d("max_cpm")))
    
    val distribution=d.join(mininum_cpm_dis,Seq("mininum_cpm"),"lefter_out").join(win_price_dis,Seq("win_price"),"lefter_out").join(max_cpm_dis,Seq("max_cpm"),"lefter_out")
    val correlive=correlative(d)
    
    val dd=distribution.join(correlive,Seq("ex_id"),"lefter_out")
    dd
  }
  
  val bidRelevant=collect(bidRelevantDataFrame)
}