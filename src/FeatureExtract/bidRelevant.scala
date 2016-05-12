package FeatureExtract


import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SQLContext

import org.apache.spark.mllib.stat.{MultivariateStatisticalSummary,Statistics}
import org.apache.spark.sql.DataFrameStatFunctions
import org.apache.spark.sql.ColumnName
import org.apache.spark.mllib.random.ExponentialGenerator

class BidRelevant (data:DataFrame) {
  
  val bidRelevantDataFrame=data.select("ex_id","mininum_cpm","rtb_price","max_cpm")
  
    
  
  def correlative (d:DataFrame):DataFrame = {
    val columns=d.columns
    val one=d.withColumn("one",d("mininum_cpm")-d("mininum_cpm")+1)
    val correlative=d.select(d("ex_id"),d("one")-d("mininum_cpm")/d("rtb_price"),d("one")-d("mininum_cpm")/d("max_cpm"),d("one")-d("rtb_price")/d("max_cpm"))
    correlative
  }
  
  def collect(d:DataFrame):DataFrame = {
    /***
     * Join original distribution and correlative DataFrames
     */
    //Three Distributions
    val singleAnalyzer = new SingleVariableAnalyzer(d)
    
    val mininum_cpm_dis=singleAnalyzer.distribute("mininum_cpm")
    val win_price_dis=singleAnalyzer.distribute("rtb_price")
    val max_cpm_dis=singleAnalyzer.distribute("max_cpm")
    
    val distribution=d.join(mininum_cpm_dis,Seq("mininum_cpm"),"left_outer").join(win_price_dis,Seq("rtb_price"),"left_outer").join(max_cpm_dis,Seq("max_cpm"),"left_outer")
    val correlive=correlative(d)
    
    val dd=distribution.join(correlive,Seq("ex_id"),"left_outer")
    dd
  }
  
  val bidRelevant=collect(bidRelevantDataFrame)
}