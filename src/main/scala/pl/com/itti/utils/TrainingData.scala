package pl.com.itti.utils

import java.io.{File, FileNotFoundException, IOException}

import pl.com.itti.model.NetFlowFrame

import scala.collection.mutable
import scala.io.Source


object TrainingData {

  private final val DELIMITER = ","

  def readLoadingFilesByLineAndConvertToModel(): List[NetFlowFrame] = {
    val models: mutable.MutableList[NetFlowFrame] = mutable.MutableList()
    getListOfFiles("data", List("2format")).foreach(file => {
      try {
        val source = Source.fromFile(file)
        for (line <- source.getLines()) {
          models += getNetFlowFrameFromLine(line.split(DELIMITER))
        }
        source.close()
      } catch {
        case _: FileNotFoundException => println("Couldn't find that file.")
        case _: IOException => println("Got an IOException!")
      }
    })

    models.toList
  }

  private def getNetFlowFrameFromLine(netFlowLine: Array[String]) = new NetFlowFrame(netFlowLine(0), netFlowLine(1), netFlowLine(2), netFlowLine(3), netFlowLine(4), netFlowLine(5), netFlowLine(6), netFlowLine(7), netFlowLine(8), netFlowLine(9), netFlowLine(10), netFlowLine(11), netFlowLine(12), netFlowLine(13), netFlowLine(14), netFlowLine(15), netFlowLine(16), netFlowLine(17), netFlowLine(18), netFlowLine(19), netFlowLine(20), netFlowLine(21), netFlowLine(22), netFlowLine(23), netFlowLine(24), netFlowLine(25), netFlowLine(26), netFlowLine(27), netFlowLine(28), netFlowLine(29), netFlowLine(30), netFlowLine(31), netFlowLine(32))

  private def getListOfFiles(dir: String, extensions: List[String]) = {
    val files = new File(dir)
    files.listFiles.filter(_.isFile).toList.filter { file =>
      extensions.exists(file.getName.endsWith(_))
    }
  }

}
