package cn.ycmedia.FeatureExtract

import org.apache.spark.SparkContext
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SQLContext



class BidRelevant (data:DataFrame,sqlContext:SQLContext,sc:SparkContext) {
  
  val bidRelevantDataFrame=data.select("ex_id","mininum_cpm","rtb_price","max_cpm")
  
    
  def correlative (d:DataFrame):DataFrame = {
    
    val columns = d.columns
    val done = d.withColumn("one",d("mininum_cpm")-d("mininum_cpm")+1)
    val dfloor_second = done.withColumn("floor_second",done("one")-done("mininum_cpm")/done("rtb_price"))
    val dfloor_highest = dfloor_second.withColumn("floor_highest",dfloor_second("one")-dfloor_second("mininum_cpm")/dfloor_second("max_cpm"))
    val dsecond_highest = dfloor_highest.withColumn("second_highest",dfloor_highest("one")-dfloor_highest("rtb_price")/dfloor_highest("max_cpm"))
    val correlative = dsecond_highest.drop("one").drop("mininum_cpm").drop("rtb_price").drop("max_cpm")
    correlative
  }
  
  
  def collect (d:DataFrame):DataFrame = {
    /***
     * Join original distribution and correlative DataFrames
     */
    
    val singleAnalyzer = new SingleVariableAnalyzer(d,sqlContext,sc)
    
    //Three Distributions
    val mininum_cpm_cdf=singleAnalyzer.cdf("mininum_cpm")
    val win_price_cdf=singleAnalyzer.cdf("rtb_price")
    val max_cpm_cdf=singleAnalyzer.cdf("max_cpm")
    
    val distribution=d.join(mininum_cpm_cdf,Seq("mininum_cpm"),"left_outer").join(win_price_cdf,Seq("rtb_price"),"left_outer").join(max_cpm_cdf,Seq("max_cpm"),"left_outer")
    val correlive=correlative(d)
    
    val dd=distribution.join(correlive,Seq("ex_id"),"left_outer")
    dd
  }
  
  val bidRelevant=collect(bidRelevantDataFrame)
}