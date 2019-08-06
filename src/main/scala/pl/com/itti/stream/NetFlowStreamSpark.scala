package pl.com.itti.stream

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import pl.com.itti.config.KafkaConfig
import pl.com.itti.model.NetFlowFrame
import pl.com.itti.producer.KafkaProducer
import pl.com.itti.utils.NetFlowsUtils

object NetFlowStreamSpark {

  def prepareNetFlowDataAndProduceToKafkaTopic(kafkaTopics: Array[String], spark: StreamingContext): Unit = {
    createStreamNetFlowRawData(kafkaTopics, spark)
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
