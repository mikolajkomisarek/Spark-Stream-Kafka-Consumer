package pl.com.itti.utils

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.spark.sql.Row
import org.apache.spark.sql.avro.SchemaConverters
import org.apache.spark.sql.types._

import scala.collection.JavaConverters._

object GenericRecordUtils {

  def convertGenericRecordToRow(record: GenericRecord): Row = Row.fromSeq(record.getSchema.getFields.asScala.map(field => {
    val obj = record.get(field.pos)
    field.schema.getType match {
      case Schema.Type.BOOLEAN => obj.asInstanceOf[Boolean]
      case Schema.Type.DOUBLE => obj.asInstanceOf[Double]
      case Schema.Type.FLOAT => obj.asInstanceOf[Float]
      case Schema.Type.INT => obj.asInstanceOf[Int]
      case Schema.Type.LONG => obj.asInstanceOf[Long]
      case _ => obj.toString
    }
  }))


  def getSchemaTypeFromGenericRecord(record: GenericRecord): StructType = StructType(
    record.getSchema.getFields.asScala.map(field => {

      val dataType = field.schema().getType match {
        case Schema.Type.BOOLEAN => BooleanType
        case Schema.Type.DOUBLE => DoubleType
        case Schema.Type.FLOAT => FloatType
        case Schema.Type.INT => IntegerType
        case Schema.Type.LONG => LongType
        case _ => StringType
      }
      StructField(
        field.name(),
        dataType
      )
    }).toList)


  def rowToGenericRecord(row: Row): GenericRecord = {
    val avroRecord = new GenericData.Record(SchemaConverters.toAvroType(row.schema))
    row.schema.foreach(it => avroRecord.put(it.name, row.getAs(it.name)))
    avroRecord
  }
}

