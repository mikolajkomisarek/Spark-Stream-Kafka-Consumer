import Deps._
import sbt.Keys.scalaVersion

lazy val root = (project in file(".")).
  aggregate(streamConsumerService).
  settings(
    inThisBuild(List(
      organization := "pl.com.itti",
      scalaVersion := "2.13.0",
      resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      resolvers += "io.confluent" at "http://packages.confluent.io/maven/"
    )),
    name := "stream-consumer-service"
  )


lazy val streamConsumerService = (project in file (".")).
  settings(
    name := "stream-consumer-service",
    sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue,
    mainClass := Some("pl.com.itti.Main"),
    libraryDependencies ++= Seq(
      jacksonCore,
      jacksonScala,
      avro,
      avroSerializer,

      kafkaClient,
      kafkaStreams,
      kafkaStreamsScala,

      spark,
      sparkStreamingKafka,
      sparkStreaming,
      sparkSQL,
      sparkSQLKafka,
      configTypeSafe
    )
  )