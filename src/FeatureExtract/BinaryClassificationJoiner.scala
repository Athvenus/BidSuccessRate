package FeatureExtract

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

class BinaryClassificationJoiner (Denominator:DataFrame,Numerator:DataFrame) {
  //Fraction=Numerator/Denominator
  val LabeledNumerator=Numerator.withColumn("label",Numerator("type")-Numerator("type")+1)
  val JoinedData = Denominator.join(LabeledNumerator,Seq("ex_id"),"left_outer")
  
}