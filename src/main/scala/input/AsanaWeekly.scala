package input

import java.time.LocalDate

import com.asana.models.Task

import scala.collection.mutable

object AsanaWeekly {

  // {LocalDate -> [Tasks]}
  def getTasksForWeek(asanaConfig: AsanaConfig): mutable.LinkedHashMap[LocalDate, List[Task]] = {
    /**
      * add projectName in properties
      * getFirstDayOfWeek
      * create Asana client
      * fetch all Tasks
      * filter by the weekdates
      * add to LinkedHashMap
      * return
      */
    mutable.LinkedHashMap.empty
  }

  def getFirstDateForWeek(date: LocalDate): LocalDate = {
    date.minusDays(date.getDayOfWeek.getValue - 1)
  }
}
