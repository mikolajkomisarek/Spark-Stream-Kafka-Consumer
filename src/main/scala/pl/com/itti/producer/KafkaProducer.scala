package pl.com.itti.producer

import com.typesafe.config.ConfigFactory
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.spark.rdd.RDD
import pl.com.itti.config.KafkaConfig

object KafkaProducer {

  private final val configuration = ConfigFactory.load("application")

  def produceProcessingNetFlowData(data: RDD[Array[String]]): Unit = {
    val props = KafkaConfig.properties
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer")

    val producer = new KafkaProducer[String, GenericRecord](props)

    val TOPIC = configuration.getString("data.topic.processed")



  }



}
