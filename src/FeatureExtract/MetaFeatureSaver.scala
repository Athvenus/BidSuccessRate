package FeatureExtract



import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.DataFrameWriter


class MetaFeatureSaver (A:DataFrame,B:DataFrame,savePath:String){
  
  //metaPath="/opt/dmp/mspace/"
  val metaFeature=A.join(B,Seq("ex_id"),"left_outer")
  
  A.write.saveAsTable(savePath)
  B.write.text(savePath)
  
  metaFeature.write.save(savePath)
  
}