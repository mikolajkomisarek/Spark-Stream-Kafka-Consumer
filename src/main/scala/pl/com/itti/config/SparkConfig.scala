package pl.com.itti.config

import com.typesafe.config.ConfigFactory
import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object SparkConfig {

  private final val configuration = ConfigFactory.load("application")
  private final var sparkContext: SparkContext = _
  private final var sparkStreamingContext: StreamingContext = _

  def runSparkEngine(): StreamingContext = {
    Logger.getLogger("org")
      .setLevel(Level.OFF);
    Logger.getLogger("akka")
      .setLevel(Level.OFF);

    val sparkConfig = new SparkConf()
      .setMaster(configuration.getString("spark.master"))
      .setAppName(configuration.getString("spark.applicationName"))

    sparkContext = new SparkContext(sparkConfig)
    sparkStreamingContext = new StreamingContext(sparkContext, Minutes(configuration.getInt("spark.stream.duration")))
    sparkStreamingContext
  }


}
