package api

import java.io.IOException

import com.asana.Client
import com.asana.models.Task

object Asana {

  def getClient(accessToken: String): Client = Client.accessToken(accessToken)

  /**
    * The current Asana API does not support asking for tasks for a given date.
    * They recommend fetching all the tasks and perform client-side filtering
    * see http://stackoverflow.com/a/35808942/379235
    *
    * @param client
    * @param projectName
    * @return All the Tasks for the project
    */
  def getAllAsanaTasksForProject(client: Client, projectName: String): Either[String, List[Task]] = {
    try {
      import scala.collection.JavaConversions._

      client.projects.findAll().execute().toList.find(_.name == projectName) match {
        case None => Left(s"no project found with name $projectName")
        case Some(project) =>
          val tasks: List[Task] = client.tasks.findByProject(project.id)
            .query("opt_fields", "completed, name, notes, due_on, tags").execute().toList
          if (tasks.isEmpty) Left("no tasks found for the project") else Right(tasks)
      }
    } catch {
      case e: IOException => Left(e.getMessage)
    }
  }
}
