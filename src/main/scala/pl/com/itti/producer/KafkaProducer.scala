package pl.com.itti.producer

import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.rdd.RDD
import pl.com.itti.config.KafkaConfig
import pl.com.itti.model.NetFlowProcessed
import com.typesafe.scalalogging.Logger

object KafkaProducer {
  private final val logger = Logger("Root")
  private final val configuration = ConfigFactory.load("application")
  private final val DELIMITER = ","

  def produceProcessingNetFlowData(data: RDD[Array[String]]): Unit = {
    val props = KafkaConfig.properties
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer")

    val producer = new KafkaProducer[String, NetFlowProcessed](props)

    val TOPIC = configuration.getString("data.topic.processed")

    data
      .collect()
      .map(it => convertFromArrayStringToNetFlowProcessed(it.mkString(DELIMITER).split(DELIMITER)))
      .foreach(netFlowProcessed => {
        producer.send(new ProducerRecord(TOPIC, netFlowProcessed))
        logger.info(s"Send into kafka topic: ${TOPIC} message: ${netFlowProcessed.toString}")
      })


  }

  private def convertFromArrayStringToNetFlowProcessed(data: Array[String]) = new NetFlowProcessed(data(0).toInt, data(1).toInt, data(2).toInt, data(3).toInt, data(4).toInt, data(5).toFloat, data(6).toFloat, data(8), data(9))


}
