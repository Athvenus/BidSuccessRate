package FeatureExtract

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql._
import org.apache.spark.sql.types._
import org.apache.spark.sql.types.{StructType,StructField,StringType,IntegerType,DoubleType}

class BidDataSchema {
  
  val userStruct=
    StructType(
        StructField("ex_id",StringType,true)::
        StructField("ip",StringType,true)::
        StructField("user_agent",StringType,true)::
        StructField("user_id",StringType,true)::
        StructField("user_id_ex",StringType,true)::
        StructField("user_id_version",StringType,true)::
        StructField("user_category",StringType,true)::
        StructField("gender",StringType,true)::
        StructField("detected_language",StringType,true)::Nil)
  
  val siteStruct=
    StructType(
        StructField("url",StringType,true)::
        StructField("referer",StringType,true)::
        StructField("site_category",StringType,true)::
        StructField("site_quality",IntegerType,true)::
        StructField("page_type",IntegerType,true)::
        StructField("page_keyword",StringType,true)::
        StructField("page_quality",IntegerType,true)::
        StructField("page_vertical",StringType,true)::Nil)
                
  val adStruct=
    StructType(
        StructField("excluded_product_category",StringType,true)::
        StructField("adblock_key",StringType,true)::
        StructField("sequence_id",IntegerType,true)::
        StructField("adslot_type",IntegerType,true)::
        StructField("adsize",StringType,true)::
        StructField("slot_visibility",IntegerType,true)::
        StructField("ask_creative_type",IntegerType,true)::
        StructField("excluded_landing_page_url",StringType,true)::
        StructField("mininum_cpm",IntegerType,true)::
        StructField("is_test",StringType,true)::
        StructField("is_ping",StringType,true)::
        StructField("creative_id",IntegerType,true)::
        StructField("max_cpm",IntegerType,true)::
        StructField("extdata",StringType,true)::
        StructField("is_cookie_matching",StringType,true)::
        StructField("html_snippet",StringType,true)::
        StructField("advertiser_id",StringType,true)::
        StructField("adcategory",StringType,true)::
        StructField("creative_type",IntegerType,true)::
        StructField("landing_page",StringType,true)::
        StructField("target_url",StringType,true)::
        StructField("processing_time_ms",IntegerType,true)::
        StructField("rtb_price",IntegerType,true)::
        StructField("rtb_success",StringType,true)::
        StructField("channel_id",IntegerType,true)::
        StructField("request_type",IntegerType,true)::
        StructField("request_time",StringType,true)::
        StructField("client_user_browser",StringType,true)::
        StructField("client_user_os",StringType,true)::
        StructField("client_user_screen",StringType,true)::
        StructField("adspace_userid",IntegerType,true)::
        StructField("adspace_id",IntegerType,true)::
        StructField("adspace_website_id",IntegerType,true)::
        StructField("ad_schedule_id",IntegerType,true)::
        StructField("ad_project_id",IntegerType,true)::
        StructField("adplan_id",IntegerType,true)::
        StructField("page_title",StringType,true)::
        StructField("ad_showhours",StringType,true)::
        StructField("ad_showhours_weight",StringType,true)::
        StructField("ad_is_fashshow",StringType,true)::
        StructField("ad_allow_website_category",StringType,true)::
        StructField("ad_deny_website_category",StringType,true)::
        StructField("ad_allow_domain",StringType,true)::
        StructField("ad_allow_zone",StringType,true)::
        StructField("ad_deny_zone",StringType,true)::
        StructField("ad_allow_tag",StringType,true)::
        StructField("ad_deny_tag",StringType,true)::
        StructField("adspace_allow_adcategory",StringType,true)::
        StructField("adspace_allow_ad_projectid",StringType,true)::
        StructField("adspace_deny_ad_projectid",StringType,true)::
        StructField("adspace_allow_ad_planid",StringType,true)::
        StructField("adspace_deny_ad_planid",StringType,true)::
        StructField("cookies",StringType,true)::
        StructField("adspace_total_type",IntegerType,true)::
        StructField("adspace_total_prices",DoubleType,true)::
        StructField("adspace_cost",DoubleType,true)::
        StructField("adspace_type",IntegerType,true)::
        StructField("ad_total_prices",DoubleType,true)::
        StructField("ad_total_money",DoubleType,true)::
        StructField("exdata",StringType,true)::
        StructField("uid",StringType,true)::
        StructField("ad_project_label_id",StringType,true)::
        StructField("type",IntegerType,true)::
        StructField("pricesn",StringType,true)::
        StructField("usersn",StringType,true)::
        StructField("match_kws",StringType,true)::Nil)
        
  val struct=
    StructType(
        StructField("ex_id",StringType,true)::
        StructField("ip",StringType,true)::
        StructField("user_agent",StringType,true)::
        StructField("user_id",StringType,true)::
        StructField("user_id_ex",StringType,true)::
        StructField("user_id_version",StringType,true)::
        StructField("user_category",StringType,true)::
        StructField("gender",StringType,true)::
        StructField("detected_language",StringType,true)::
        StructField("url",StringType,true)::
        StructField("referer",StringType,true)::
        StructField("site_category",StringType,true)::
        StructField("site_quality",IntegerType,true)::
        StructField("page_type",IntegerType,true)::
        StructField("page_keyword",StringType,true)::
        StructField("page_quality",IntegerType,true)::
        StructField("page_vertical",StringType,true)::
        StructField("excluded_product_category",StringType,true)::
        StructField("adblock_key",StringType,true)::
        StructField("sequence_id",IntegerType,true)::
        StructField("adslot_type",IntegerType,true)::
        StructField("adsize",StringType,true)::
        StructField("slot_visibility",IntegerType,true)::
        StructField("ask_creative_type",IntegerType,true)::
        StructField("excluded_landing_page_url",StringType,true)::
        StructField("mininum_cpm",IntegerType,true)::
        StructField("is_test",StringType,true)::
        StructField("is_ping",StringType,true)::
        StructField("creative_id",IntegerType,true)::
        StructField("max_cpm",IntegerType,true)::
        StructField("extdata",StringType,true)::
        StructField("is_cookie_matching",StringType,true)::
        StructField("html_snippet",StringType,true)::
        StructField("advertiser_id",StringType,true)::
        StructField("adcategory",StringType,true)::
        StructField("creative_type",IntegerType,true)::
        StructField("landing_page",StringType,true)::
        StructField("target_url",StringType,true)::
        StructField("processing_time_ms",IntegerType,true)::
        StructField("rtb_price",IntegerType,true)::
        StructField("rtb_success",StringType,true)::
        StructField("channel_id",IntegerType,true)::
        StructField("request_type",IntegerType,true)::
        StructField("request_time",StringType,true)::
        StructField("client_user_browser",StringType,true)::
        StructField("client_user_os",StringType,true)::
        StructField("client_user_screen",StringType,true)::
        StructField("adspace_userid",IntegerType,true)::
        StructField("adspace_id",IntegerType,true)::
        StructField("adspace_website_id",IntegerType,true)::
        StructField("ad_schedule_id",IntegerType,true)::
        StructField("ad_project_id",IntegerType,true)::
        StructField("adplan_id",IntegerType,true)::
        StructField("page_title",StringType,true)::
        StructField("ad_showhours",StringType,true)::
        StructField("ad_showhours_weight",StringType,true)::
        StructField("ad_is_fashshow",StringType,true)::
        StructField("ad_allow_website_category",StringType,true)::
        StructField("ad_deny_website_category",StringType,true)::
        StructField("ad_allow_domain",StringType,true)::
        StructField("ad_allow_zone",StringType,true)::
        StructField("ad_deny_zone",StringType,true)::
        StructField("ad_allow_tag",StringType,true)::
        StructField("ad_deny_tag",StringType,true)::
        StructField("adspace_allow_adcategory",StringType,true)::
        StructField("adspace_allow_ad_projectid",StringType,true)::
        StructField("adspace_deny_ad_projectid",StringType,true)::
        StructField("adspace_allow_ad_planid",StringType,true)::
        StructField("adspace_deny_ad_planid",StringType,true)::
        StructField("cookies",StringType,true)::
        StructField("adspace_total_type",IntegerType,true)::
        StructField("adspace_total_prices",DoubleType,true)::
        StructField("adspace_cost",DoubleType,true)::
        StructField("adspace_type",IntegerType,true)::
        StructField("ad_total_prices",DoubleType,true)::
        StructField("ad_total_money",DoubleType,true)::
        StructField("exdata",StringType,true)::
        StructField("uid",StringType,true)::
        StructField("ad_project_label_id",StringType,true)::
        StructField("type",IntegerType,true)::
        StructField("pricesn",StringType,true)::
        StructField("usersn",StringType,true)::
        StructField("match_kws",StringType,true)::Nil)
                      
  val comStruct=
   StructType(
       StructField("user",userStruct,true)::
       StructField("site",siteStruct,true)::
       StructField("ad",adStruct,true)::Nil)
  
  
}