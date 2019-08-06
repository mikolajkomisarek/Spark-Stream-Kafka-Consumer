package pl.com.itti.utils


import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import pl.com.itti.model.NetFlowFrame
import pl.com.itti.producer.KafkaProducer

object NetFlowsUtils {

  def prepareData(rdd: Array[NetFlowFrame]): RDD[Array[String]] = {
    val sqlContext = SparkSession
      .builder()
      .getOrCreate()

    sqlContext.createDataFrame(rdd)
      .createOrReplaceTempView("netflows")

    val sql = sqlContext.sql("SELECT count(*) as flcnt, " +
      "count(distinct proto) as dprot, " +
      "count(distinct sPort) as dsport, " +
      "count(distinct dstAddr) as ddadr, " +
      "count(distinct dPort) as ddport, " +
      "sum(totPkts) as totp, " +
      "sum(totBytes) as totbytes, " +
      "max(Label), " +
      "srcAddr, " +
      "label " +
      "FROM netflows group by label,srcAddr")

    val results = sql.rdd.map(x => x.mkString(",").split(',')).map(x => (x(7), x(8), x(9)) -> x).map {
      case (((windid: String, srcAddr: String, label: String), (y: Array[String]))) => y
    }

    println(sql.toDF().show())

    KafkaProducer.produceProcessingNetFlowData(results)

    results
  }

}
