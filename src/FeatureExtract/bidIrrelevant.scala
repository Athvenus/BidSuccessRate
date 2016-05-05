package FeatureExtract

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

import org.apache.spark.mllib.linalg.SparseVector
import org.apache.spark.mllib.linalg.distributed.IndexedRow
import org.apache.spark.mllib.linalg.distributed.MatrixEntry
import org.apache.spark.ml.feature.Bucketizer
import org.apache.spark.ml.feature.CountVectorizer
import org.apache.spark.ml.feature.CountVectorizerModel
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.feature.VectorIndexerModel
import org.apache.spark.ml.feature.VectorSlicer
import org.apache.spark.mllib.feature.VectorTransformer
import org.apache.spark.util.Vector

class bidIrrelevant (data:DataFrame) {
  
  
  
}