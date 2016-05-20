package FeatureExtract

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

class BinaryClassificationJoiner (Denominator:DataFrame,Numerator:DataFrame) {
  //Fraction=Numerator分子/Denominator分母
  val LabeledNumerator = Numerator.withColumn("label",Numerator("rtb_price")-Numerator("rtb_price")+1)
  val JoinedData = Denominator.join(LabeledNumerator,Seq("ex_id"),"left_outer")
  val schema = JoinedData.schema 
  
}