package input

import java.time.LocalDate

import api.Asana.{getAllAsanaTasksForProject, getClient}
import com.asana.Client
import com.asana.models.Task

import scala.collection.mutable

object AsanaWeekly {

  // (todo: harit) exclude tags (ex. Personal)
  def getTasksForWeek(asanaConfig: AsanaConfig): Either[String, mutable.LinkedHashMap[LocalDate, List[Task]]] = {
    val client: Client = getClient(asanaConfig.access_token)
    val firstDateForWeek: LocalDate = getFirstDateForWeek(asanaConfig.reportDate)
    val weekDateByTasks: mutable.LinkedHashMap[LocalDate, List[Task]] = mutable.LinkedHashMap.empty

    getAllAsanaTasksForProject(client, asanaConfig.projectName) match {
      case Left(error) => return Left(error)
      case Right(tasks) =>
        for {
          task <- tasks
          if task.dueOn != null
          taskDueOn = LocalDate.parse(task.dueOn.toString)
          if isValidTaskForWeek(firstDateForWeek, taskDueOn)
        } weekDateByTasks.get(taskDueOn) match {
          case None => weekDateByTasks.put(taskDueOn, List(task))
          case Some(existingTasks) => weekDateByTasks.put(taskDueOn, task :: existingTasks)
        }
    }

    Right(weekDateByTasks)
  }

  def isValidTaskForWeek(firstDateForWeek: LocalDate, taskDueOn: LocalDate): Boolean = {
    (taskDueOn.isEqual(firstDateForWeek) || taskDueOn.isAfter(firstDateForWeek)) &&
      taskDueOn.isBefore(firstDateForWeek.plusWeeks(1))
  }

  def getFirstDateForWeek(date: LocalDate): LocalDate = {
    date.minusDays(date.getDayOfWeek.getValue - 1)
  }
}
