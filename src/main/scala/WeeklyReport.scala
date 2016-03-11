import java.io.File
import java.time.LocalDate

import com.asana.models.Task
import input.AsanaWeekly.getTasksForWeek
import input.{AsanaWeekly, AsanaConfig}
import input.AsanaConfig.{ProjectNameKey, AccessTokenKey}
import input.Validators._

import scala.collection.mutable

object WeeklyReport {


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
          case Right(date) =>
            val tasksForWeek: Either[String, mutable.LinkedHashMap[LocalDate, List[Task]]] =
              getTasksForWeek(AsanaConfig(config.getString(AccessTokenKey),config.getString(ProjectNameKey), date))
            println(tasksForWeek)
        }
    }
  }
}
