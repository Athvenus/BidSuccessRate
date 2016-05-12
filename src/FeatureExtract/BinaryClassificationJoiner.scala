package FeatureExtract

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

class BinaryClassificationJoiner (Denominator:DataFrame,Numerator:DataFrame) {
  //Fraction=Numerator/Denominator
  val LabeledDenominator = Denominator.withColumn("label",Denominator("mininum_cpm")-Denominator("mininum_cpm")+1)
  val JoinedData = Numerator.join(LabeledDenominator,Seq("ex_id"),"right_outer")
  val schema = JoinedData.schema 
  
}