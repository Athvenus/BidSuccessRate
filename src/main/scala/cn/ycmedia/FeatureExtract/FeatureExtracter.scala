package main.scala.FeatureExtract


import org.apache.spark.sql.DataFrame
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf


object FeatureExtracter {
  
  def main(args:Array[String]) {
  
    val conf = new SparkConf().setAppName("FeatureExtract")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    
    val sucessPath = "/user/hive/warehouse/bdl_dmp/plat=baidu/ds=2016-05-22/rt=1"
    val bidPath = "/user/hive/warehouse/bdl_dmp/plat=baidu/ds=2016-05-22/rt=14"
    val metaPath = "/opt/dmp/mspace/bsr/160522"
  
    //Load Basic DataFrame
    val successDF = selectsuccess(new DataFrameLoader(sc,3,2,sucessPath).reschema)
    val bidDF = selectbid(new DataFrameLoader(sc,3,2,bidPath).reschema)
    println(successDF.printSchema,bidDF.printSchema)
    
    //Join Data Together
    val joinDF = new BinaryClassificationJoiner(bidDF,successDF).JoinedData
    println(joinDF.printSchema)
    
    //Get bidRelevant and bidIrrelevant Feature
    val bidRelevant = new BidRelevant(joinDF,sqlContext,sc).bidRelevant
    val bidIrrelevant = new BidIrrelevant(joinDF).bidIrrelevant
    println(bidRelevant.printSchema,bidIrrelevant.printSchema)
    
    //Score MetaFeature
    val meta = new MetaFeatureSaver(bidIrrelevant,bidRelevant,metaPath)
    
  }
  
  
  def selectsuccess(data:DataFrame):DataFrame = {
    val sucess = data.select("ex_id","rtb_price","max_cpm")
    sucess
  }
  
  
  def selectbid(data:DataFrame):DataFrame = {
    val bid = data.select("ex_id","ip","user_agent","user_id","user_id_version","user_category","gender",
        "detected_language","url","referer","site_category","site_quality","page_type","adslot_type","adsize",
        "slot_visibility","ask_creative_type","advertiser_id","creative_type","channel_id","request_type",
        "request_time","client_user_browser","client_user_os","client_user_screen","adspace_id","adspace_website_id",
        "ad_schedule_id","ad_project_id","adplan_id","page_title","ad_showhours","ad_showhours_weight","mininum_cpm")
    bid
  }
  
}