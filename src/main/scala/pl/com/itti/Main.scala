package pl.com.itti

import com.typesafe.config.ConfigFactory
import pl.com.itti.config.SparkConfig
import pl.com.itti.stream.NetFlowStreamSpark


object Main extends App {

  private final val configuration = ConfigFactory.load("application")

  val spark = SparkConfig.runSparkEngine()
  NetFlowStreamSpark.prepareNetFlowDataAndProduceToKafkaTopic(Array( configuration.getString("data.topic.netflow.raw")), spark)
  spark.start()
  spark.awaitTermination()
}