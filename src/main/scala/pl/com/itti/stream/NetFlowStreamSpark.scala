package pl.com.itti.stream

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.sql.{Encoders, SQLContext, SparkSession}
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import pl.com.itti.config.KafkaConfig
import pl.com.itti.kafkaproducer.model.NetFlowFrameAvro

object NetFlowStreamSpark {

  def groupByLabelAndCountSize(kafkaTopics: Array[String], spark: StreamingContext): Unit = {
    val kafkaRawStream: InputDStream[ConsumerRecord[String, NetFlowFrameAvro]] =
      KafkaUtils.createDirectStream[String, NetFlowFrameAvro](
        spark,
        LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe[
          String,
          NetFlowFrameAvro](kafkaTopics, KafkaConfig.propertiesMap)
      )


    kafkaRawStream.foreachRDD(RDDS => {
      val collection = RDDS.map(data => data.value())
        .collect()

      val sqlContext = SparkSession.builder().getOrCreate()

      sqlContext.createDataFrame(collection)
        .createOrReplaceTempView("netflows")

      val results = sqlContext.sql("SELECT count(*) as flcnt, count(distinct proto) as dprot, count(distinct sPort) as dsport, count(distinct dstAddr) as ddadr, count(distinct dPort) as ddport, " +
        "sum(totPkts) as totp, sum(totBytes) as totbytes, max(Label), srcAddr, label FROM netflows group by label,srcAddr")

      val out = results.rdd.map(x=>x.mkString(",").split(',')).map(x=>(x(7),x(8),x(9))->x ).map{
        case ( ((windid: String, srcAddr: String, label: String), (y:Array[String])) ) => {
          y
        }
      }

      out.foreach(it=> {
        println("-------------------")
        it.foreach(text=> println(text))
        println("-------------------")
      })


    })
  }

}
