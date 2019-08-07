package pl.com.itti.stream

import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import pl.com.itti.config.KafkaConfig
import pl.com.itti.model.NetFlowFrame
import pl.com.itti.producer.KafkaProducer
import pl.com.itti.utils.NetFlowsUtils

object NetFlowStreamSpark {

  private final val configuration = ConfigFactory.load("application")

  def prepareNetFlowDataAndProduceToKafkaTopic( spark: StreamingContext): Unit = {
    createStreamNetFlowRawData(Array( configuration.getString("data.topic.netflow.raw")), spark)
      .foreachRDD(RDDS => {
        KafkaProducer
          .produceProcessingNetFlowData(
            NetFlowsUtils.prepareData(
              RDDS
                .map(data => data.value())
                .collect())
          )

      })
  }


  private def createStreamNetFlowRawData(kafkaTopics: Array[String], spark: StreamingContext): InputDStream[ConsumerRecord[String, NetFlowFrame]] = {
    KafkaUtils.createDirectStream[String, NetFlowFrame](
      spark,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[
        String,
        NetFlowFrame](kafkaTopics, KafkaConfig.propertiesMap)
    )
  }

}
