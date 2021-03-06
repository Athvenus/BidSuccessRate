name := "BidSuccessRate Project"

version := "1.0"


scalaVersion := "2.10.5"

libraryDependencies ++= Seq(
  "org.apache.spark"%"spark-core_2.10"%"1.6.1",
  "org.apache.spark"%"spark-mllib_2.10"%"1.6.1",
  "org.apache.hadoop"%"hadoop-common"%"2.6.1",
  "org.apache.hive"%"hive-exec"%"1.2.1",
  "org.apache.spark"%"spark-sql_2.10"%"1.6.1",
  "org.apache.spark"%"spark-hive_2.10"%"1.6.1"
  )
  
resolvers ++=Seq(
  "Maven Repository" at "http://repo.maven.apache.org/maven2",
  "Apache Repository" at "https://repository.apache.org/content/repositories/releases",
  "JBoss Repository" at "https://repository.jboss.org/nexus/content/repositories/releases/",
  "Conjars" at "https://conjars.org/repo",
  "Clojars" at "https://clojars.org/repo",
  "Plugins" at "http://repo.spring.io/plugins-release",
  "Sonatype" at "http://oss.sonatype.org/content/group/public/"
   )