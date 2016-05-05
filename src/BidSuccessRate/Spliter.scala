package BidSuccessRate

import org.apache.spark.rdd.RDD

class Spliter(data:RDD[String]) {
    
    //Been used in trainer and tester
    val feat=
      data.map(str => str.split("\\|"))
      //map(arr => 
        //for{i <- Iterator.range(1,arr.length)}{
          //arr.update(i,arr(i).split("\t"))
        //}
      //)    
 
}