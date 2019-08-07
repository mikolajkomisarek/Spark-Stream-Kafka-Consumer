package pl.com.itti.config

import java.util.Properties

import com.typesafe.config.ConfigFactory
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

object KafkaConfig {
  private val configuration = ConfigFactory.load("application")

  val propertiesMap: Map[String, Object] = Map[String, Object](
    "bootstrap.servers" -> configuration.getString("kafka-configuration.bootstrap.servers"),
      "schema.registry.url" -> configuration.getString("kafka-configuration.schema.registry.url"),
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[KafkaAvroDeserializer],
      "group.id" -> configuration.getString("kafka-configuration.group.id"),
      "auto.offset.reset" -> configuration.getString("kafka-configuration.auto.offset.reset"),
      "specific.avro.reader" -> (true: java.lang.Boolean),
    )


  def properties: Properties = {
    val consumerProps: Properties = new Properties()
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer])
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getString("kafka-configuration.bootstrap.servers"))
    consumerProps.put("schema.registry.url", configuration.getString("kafka-configuration.schema.registry.url"))
    consumerProps.put("group.id", configuration.getString("kafka-configuration.group.id"))
    consumerProps.put("specific.avro.reader", (true: java.lang.Boolean))
    consumerProps
  }

}
