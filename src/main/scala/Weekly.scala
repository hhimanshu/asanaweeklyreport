import java.io.File
import java.time.LocalDate

import api.Email
import com.typesafe.config.Config
import input.AsanaConfig
import input.AsanaConfig.{AccessTokenKey, ProjectNameKey}
import input.AsanaWeekly.getTasksForWeek
import input.Validators._
import output.{Html, WeeklyReport}

object Weekly {
  def main(args: Array[String]) {
    if (args.isEmpty || args.length != 1) {
      println("usage: weekly-report <configuration> <date>(optional)")
      return
    }

    getConfigurationFromFile(new File(args(0))) match {
      case Left(errors) => println(errors.mkString("\n"))
      case Right(config) => getDateForReportOrToday(args) match {
        case Left(error) => println(error)
        case Right(date) => generateHtmlReport(config, date) match {
          case Left(error) => println(error)
          case Right(htmlReport) =>
            val (userName, password, recipients) = (config.getString("email.username"),
              config.getString("email.password"), config.getString("email.recipients").split(",").toList)
            Email(userName, password).send(recipients, "Weekly Highlights", htmlReport) match {
              case Left(error) => println(s"Email send failure: $error")
              case Right(_) => println("Email sent successfully")
            }
        }
      }
    }

    def generateHtmlReport(config: Config, date: LocalDate): Either[String, String] = {
      getTasksForWeek(AsanaConfig(config.getString(AccessTokenKey), config.getString(ProjectNameKey), date)) match {
        case Left(error) => Left(error)
        case Right(tasksForWeek) => Right(Html.get(WeeklyReport.generate(tasksForWeek)))
      }
    }
  }
}
