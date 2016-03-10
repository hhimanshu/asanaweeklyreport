import java.io.File

import input.AsanaConfig
import input.AsanaConfig.AccessTokenKey
import input.Validators._

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
            print("Running Weekly")
            AsanaConfig(config.getString(AccessTokenKey), date)
        }
    }
  }
}
