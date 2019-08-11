package pl.com.itti.utils

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

import scala.collection.JavaConverters._

object SchemaParser {

  val schema = StructType(ConfigFactory.load("schema")
    .getConfigList(ConfigFactory.load("application").getString("data.topic.schema"))
    .asScala
    .map(it => StructField(
      it.getString("name"),
      getDataTypeFromString(it.getString("dataType")),
      it.getBoolean("nullable")
    )).toList)

  def row(frame: List[String]): Row = Row.fromSeq(
    frame.zipWithIndex.map {
      case (element, index) =>
        if(!(index >= schema.size)){
          parseValueByDataType(schema(index).dataType, element)
        }
    }
  )

  private def getDataTypeFromString(dataType: String): DataType = {
    dataType match {
      case "IntegerType" => IntegerType
      case "FloatType" => FloatType
      case "LongType" => LongType
      case _ => StringType
    }
  }


  private def parseValueByDataType(dataType: DataType, value: String): Any = {
    dataType match {
      case IntegerType => value.toInt
      case LongType => value.toLong
      case FloatType => value.toFloat
      case DoubleType => value.toDouble
      case TimestampType => value.toLong
      case StringType => value
      case _ => value
    }
  }



}
