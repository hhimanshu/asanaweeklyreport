package input

import java.io.File
import java.time.LocalDate

import com.typesafe.config.{ConfigFactory, Config}

import scala.collection.mutable.ListBuffer

object Validators {
  def getConfigurationFromFile(file: File): Either[List[String], Config] = {
    if (!file.exists()) {
      return Left(List(s"${file.getAbsolutePath} does not exists"))
    }

    val config: Config = ConfigFactory.parseFile(file)
    val errors: ListBuffer[String] = ListBuffer[String]()

    if (!config.hasPath("access_token")) {
      errors += "access_token is missing"
    }

    if (errors.nonEmpty) Left(errors.toList) else Right(config)
  }

  def getDateForReportOrToday(args: Array[String]): Either[String, LocalDate] = {
    if (args.isDefinedAt(1)) {
      try {
        Right(LocalDate.parse(args(1)))
      } catch {
        case e: Exception => Left(e.getMessage)
      }
    } else Right(LocalDate.now())
  }
}
