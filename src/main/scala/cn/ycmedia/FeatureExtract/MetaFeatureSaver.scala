package cn.ycmedia.FeatureExtract


import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.Row

class MetaFeatureSaver (A:DataFrame,B:DataFrame,savePath:String) extends Serializable {
  
  
  //metaPath="/opt/dmp/mspace/"
  
  val metaFeature = A.join(B,Seq("ex_id"),"left_outer")
  val len = metaFeature.schema.length
  val meta = metaFeature.map(r => text(r))
  
  meta.saveAsTextFile(savePath)
  //metaFeature.write.save(savePath)
  
  def text(r:Row):String = {
    var s = ""
    val len = r.length - 1
    val field = Array(0,1,len-9,len)
    for(i <- Iterator.range(0,len)){
      if(r(i).isInstanceOf[NotNull]){
        s+=r(i).toString+"\t"
      }else{
        s+="null"+"\t"
      }
      if(field.contains(i)){
        s+="|"
      }
    }
    s
  }
}