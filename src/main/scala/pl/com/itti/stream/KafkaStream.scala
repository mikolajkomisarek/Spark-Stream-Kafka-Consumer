package pl.com.itti.stream

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import pl.com.itti.config.KafkaConfig

class KafkaStream[A] {

  def createStream(kafkaTopics: Array[String], spark: StreamingContext): InputDStream[ConsumerRecord[String, A]] = {
    KafkaUtils.createDirectStream[String, A](
      spark,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[
        String,
        A](kafkaTopics, KafkaConfig.propertiesMap)
    )
  }


}
