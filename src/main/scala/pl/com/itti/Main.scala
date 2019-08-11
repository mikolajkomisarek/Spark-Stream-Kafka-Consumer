package pl.com.itti

import com.typesafe.config.ConfigFactory
import org.apache.avro.generic.GenericRecord
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import pl.com.itti.config.SparkConfig
import pl.com.itti.stream.KafkaStream
import pl.com.itti.utils.GenericRecordUtils

import scala.io.Source


object Main extends App {

  private val configuration = ConfigFactory.load("application")
  Logger.getLogger("org").setLevel(Level.ERROR)

  val queryFile = Source.fromFile(configuration.getString("data.query.file.localization"))
  private final val query = queryFile.mkString
  queryFile.close()

  val spark = SparkConfig.runSparkEngine()

  val sqlContext = SparkSession
    .builder()
    .getOrCreate()


  val kafkaStream = new KafkaStream[GenericRecord]

  kafkaStream
    .createStream(
      Array(configuration.getString("data.topic.raw")), spark)
    .foreachRDD(RDDS => {

      val schema = RDDS.map(it => GenericRecordUtils.getSchemaTypeFromGenericRecord(it.value()))
      val genericRecord = RDDS.map(it => GenericRecordUtils.convertGenericRecordToRow(it.value()))

      if (!genericRecord.isEmpty()) {
        val df = sqlContext.createDataFrame(
          genericRecord,
          schema.first())

        df.createOrReplaceTempView("data")

        val sql = sqlContext.sql(query)

        println(sql.toDF().show())
      }


    })


  spark.start()
  spark.awaitTermination()


}