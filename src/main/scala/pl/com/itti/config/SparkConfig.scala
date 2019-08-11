package pl.com.itti.config

import com.typesafe.config.ConfigFactory
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}

object SparkConfig {

  private final val configuration = ConfigFactory.load("application")
  private final var sparkStreamingContext: StreamingContext = _

  def runSparkEngine(): StreamingContext = {
    val sparkConfig = new SparkConf()
      .setMaster(configuration.getString("spark.master"))
      .setAppName(configuration.getString("spark.applicationName"))

    val spark = SparkSession
      .builder()
      .config(sparkConfig)
      .getOrCreate()

    sparkStreamingContext = new StreamingContext(spark.sparkContext, Seconds(configuration.getInt("spark.stream.duration")))
    sparkStreamingContext
  }




}
