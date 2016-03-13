package input

import java.io.File
import java.time.LocalDate

import com.typesafe.config.{ConfigFactory, Config}
import input.AsanaConfig.{ProjectNameKey, AccessTokenKey}

import scala.collection.mutable.ListBuffer

object Validators {
  def getConfigurationFromFile(file: File): Either[List[String], Config] = {
    if (!file.exists()) {
      return Left(List(s"${file.getAbsolutePath} does not exists"))
    }

    val config: Config = ConfigFactory.parseFile(file)
    val errors: ListBuffer[String] = ListBuffer[String]()

    if (!config.hasPath(AccessTokenKey)) {
      errors += "access_token is missing"
    }

    if (!config.hasPath(ProjectNameKey)) {
      errors += "project_name is missing"
    }

    if (!config.hasPath("email.username")) {
      errors += "email.username is missing"
    }

    if (!config.hasPath("email.password")) {
      errors += "email.password is missing"
    }

    if (!config.hasPath("email.recipients")) {
      errors += "email.recipients is missing"
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
