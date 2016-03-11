package output

import java.time.LocalDate

import com.asana.models.Task

import scala.collection.mutable

case object WeeklyReport {
  def generate(tasks: mutable.LinkedHashMap[LocalDate, List[Task]]): WeeklyReport = {
    val allTasks: List[Task] = tasks.values.flatten.toList
    WeeklyReport(allTasks.size, allTasks.count(_.completed), allTasks.size - allTasks.count(_.completed), tasks)
  }
}

case class WeeklyReport(totalTaskCount: Int, completedTaskCount: Int,
                        inCompletedTaskCount: Int, tasksByDay: mutable.LinkedHashMap[LocalDate, List[Task]])