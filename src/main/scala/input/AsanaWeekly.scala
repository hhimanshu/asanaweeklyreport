package input

import java.time.LocalDate

import api.Asana.{getAllAsanaTasksForProject, getClient}
import com.asana.Client
import com.asana.models.Task

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object AsanaWeekly {

  // {LocalDate -> [Tasks]}
  def getTasksForWeek(asanaConfig: AsanaConfig): Either[String, mutable.LinkedHashMap[LocalDate, List[Task]]] = {
    /**
      * filter by the weekdates
      * add to LinkedHashMap
      * return
      */
    val firstDateForWeek: LocalDate = getFirstDateForWeek(asanaConfig.reportDate)
    val client: Client = getClient(asanaConfig.access_token)

    getAllAsanaTasksForProject(client, asanaConfig.projectName) match {
      case Left(error) =>
        println(error)
        return Left(error)
      case Right(tasks) => println(tasks.mkString(","))
    }
    Right(mutable.LinkedHashMap.empty)
  }

  def getWeekDatesFor(date: LocalDate): List[LocalDate] = {
    val firstDateForWeek: LocalDate = getFirstDateForWeek(date)
    val weekDates: ListBuffer[LocalDate] = ListBuffer(firstDateForWeek)
    weekDates.toList
  }

  def getFirstDateForWeek(date: LocalDate): LocalDate = {
    date.minusDays(date.getDayOfWeek.getValue - 1)
  }
}
