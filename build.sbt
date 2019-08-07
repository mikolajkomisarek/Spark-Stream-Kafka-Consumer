import Deps._
import sbt.Keys.scalaVersion


lazy val root = (project in file(".")).
  aggregate(streamConsumerService).
  settings(
    inThisBuild(List(
      organization := "pl.com.itti",
      scalaVersion := "2.13.0",
      resolvers += "io.confluent" at "http://packages.confluent.io/maven/",
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
        configTypeSafe,
        logs
      )
    )),
    name := "stream-consumer-service"
  )


lazy val streamConsumerService = (project in file(".")).
  settings(
    name := "stream-consumer-service",
    resolvers += "confluent" at "http://packages.confluent.io/maven/",
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
      configTypeSafe,
      logs
    )
  )


assemblyJarName in assembly := s"app-assembly.jar"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

assemblyMergeStrategy in assembly := {
  case PathList("org","aopalliance", xs @ _*) => MergeStrategy.last
  case PathList("javax", "inject", xs @ _*) => MergeStrategy.last
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
  case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}