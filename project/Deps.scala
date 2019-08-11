import sbt._

object Deps {
  val avroVersion = "1.9.0"
  val kafkaVersion = "2.3.0"
  val kafkaStreamsScalaVersion = "2.3.0"
  val avroSerializerVersion = "5.2.1"
  val sparkVersion  = "2.4.0"
  val jacksonVersion = "2.9.4"
  val configTypeSafeVersion = "1.3.4"
  val logsVersion = "3.9.2"

  lazy val jacksonCore = "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion
  lazy val jacksonScala = "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion

  lazy val avro = "org.apache.avro" % "avro" % avroVersion
  lazy val avroSerializer = "io.confluent" % "kafka-avro-serializer" % avroSerializerVersion

  lazy val kafkaClient = "org.apache.kafka" % "kafka-clients" % kafkaVersion
  lazy val kafkaStreams = "org.apache.kafka" % "kafka-streams" % kafkaVersion
  lazy val kafkaStreamsScala = "org.apache.kafka" %% "kafka-streams-scala" % kafkaStreamsScalaVersion

  lazy val sparkStreamingKafka = "org.apache.spark" %% "spark-streaming-kafka-0-10" %sparkVersion
  lazy val sparkStreaming = "org.apache.spark" %% "spark-streaming" %sparkVersion
  lazy val spark = "org.apache.spark" %% "spark-core" % sparkVersion
  lazy val sparkSQL =  "org.apache.spark" %% "spark-sql" % sparkVersion
  lazy val sparkSQLKafka = "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion
  lazy val sparkAvro =  "org.apache.spark" %% "spark-avro" % sparkVersion


  lazy val configTypeSafe = "com.typesafe" % "config" % configTypeSafeVersion
  lazy val logs =  "com.typesafe.scala-logging" %% "scala-logging" % logsVersion
}
