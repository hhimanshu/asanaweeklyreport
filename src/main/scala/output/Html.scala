package output

import java.time.LocalDate

// (todo: harit) Add Notes
object Html {
  def get(weeklyReport: WeeklyReport): String = {
    val firstDateOfWeek: LocalDate = weeklyReport.tasksByDay.keys.last
    val page =
      <html>
        <body>
          <h1>Weekly Report for [{firstDateOfWeek} - {firstDateOfWeek.plusWeeks(1).minusDays(1)}]</h1>
          <h2>Total Tasks: {weeklyReport.totalTaskCount}</h2>
          <h2>Completed Tasks: {weeklyReport.completedTaskCount}</h2>
          <h2>In-Complete Tasks: {weeklyReport.inCompletedTaskCount}</h2>

          {
            for (entry <- weeklyReport.tasksByDay) yield
              <div>
                <h4>{entry._1}</h4>
                {
                  for(task <- entry._2) yield
                    <p>[{if(task.completed) "Completed" else "InComplete"}] {task.name}</p>
                }
              </div>
              <br/>
          }
        </body>
      </html>

    page.toString
  }
}
