package pl.com.itti.producer

import com.typesafe.config.ConfigFactory
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.log4j.Logger
import org.apache.spark.sql.Row
import pl.com.itti.config.KafkaConfig
import pl.com.itti.utils.GenericRecordUtils

object KafkaProducer {

  private final val logger = Logger.getLogger(getClass.getName)

  private final val configuration = ConfigFactory.load("application")

  def produceProcessingNetFlowData(data: Array[Row]): Unit = {
    val props = KafkaConfig.properties
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer")

    val producer = new KafkaProducer[String, GenericRecord](props)

    val TOPIC = configuration.getString("data.topic.processed")

    data.foreach(row => {
      val record = new ProducerRecord[String, GenericRecord](TOPIC, GenericRecordUtils.rowToGenericRecord(row))
      producer.send(record)
      logger.info(String.format("#### -> Producing message -> %s", record.toString))
    })
  }


}
