package main.scala.FeatureExtract

import org.apache.spark.sql.DataFrame


class BidIrrelevant(data:DataFrame) {
   
   val bidIrrelevant=selectBinary(data)
   
   def selectBinary(data:DataFrame):DataFrame={
     val bidIrrelevant = data.select("ex_id","label","ip","user_agent","user_id","user_id_version","user_category","gender",
        "detected_language","url","referer","site_category","site_quality","page_type","adslot_type","adsize",
        "slot_visibility","ask_creative_type","advertiser_id","creative_type","channel_id","request_type",
        "request_time","client_user_browser","client_user_os","client_user_screen","adspace_id","adspace_website_id",
        "ad_schedule_id","ad_project_id","adplan_id","page_title","ad_showhours","ad_showhours_weight")
     
     bidIrrelevant
   }
   
   
}