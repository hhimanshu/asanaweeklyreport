import java.io.File
import java.time.LocalDate

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
      case Right(config) =>
        getDateForReportOrToday(args) match {
          case Left(error) => println(error)
          case Right(date) => generateReport(config, date)

        }
    }

    def generateReport(config: Config, date: LocalDate): Unit = {
      getTasksForWeek(AsanaConfig(config.getString(AccessTokenKey), config.getString(ProjectNameKey), date)) match {
        case Left(error) => println(error)
        case Right(tasksForWeek) => println(Html.get(WeeklyReport.generate(tasksForWeek)))
      }
    }
  }
}
