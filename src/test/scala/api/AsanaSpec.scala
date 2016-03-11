package api

import java.io.IOException
import java.util.Collections.singletonList
import java.util.{Collections, UUID}

import com.asana.Client
import com.asana.models.{Project, Task}
import com.asana.requests.CollectionRequest
import com.asana.resources.{Projects, Tasks}
import harness.UnitSpec
import org.mockito.Matchers.anyString
import org.mockito.Mockito.when
import org.scalatest.mock.MockitoSugar

/**
  * // (todo: harit) This is not beautiful code.
  * Please learn Scala better and refactor this
  */
class AsanaSpec extends UnitSpec
  with MockitoSugar {

  "getAllAsanaTasksForProject" should "return a valid task when all information is correct" in {
    val projectId: String = UUID.randomUUID().toString
    val projectName: String = "myProject"

    val mockProjects: Projects = mock[Projects]
    val mockTasks: Tasks = mock[Tasks]

    val mockClient: Client = mock[Client]
    mockClient.projects = mockProjects
    mockClient.tasks = mockTasks

    val mockProject: Project = mock[Project]
    mockProject.id = projectId
    mockProject.name = projectName

    val mockTask: Task = mock[Task]
    mockTask.name = "Test this test"

    val mockCollectionRequestProject: CollectionRequest[Project] = mock[CollectionRequest[Project]]
    val mockCollectionRequestTask: CollectionRequest[Task] = mock[CollectionRequest[Task]]

    when(mockClient.projects.findAll()).thenReturn(mockCollectionRequestProject)
    when(mockClient.projects.findAll().execute()).thenReturn(singletonList(mockProject))

    when(mockClient.tasks.findByProject(projectId)).thenReturn(mockCollectionRequestTask)
    when(mockClient.tasks.findByProject(projectId)
      .query(anyString(), anyString())).thenReturn(mockCollectionRequestTask)
    when(mockClient.tasks.findByProject(projectId)
      .query("opt_fields", "completed, name, notes, due_on, tags")
      .execute())
      .thenReturn(singletonList(mockTask))


    val tasksOrError: Either[String, List[Task]] = Asana.getAllAsanaTasksForProject(mockClient, projectName)
    val tasks: List[Task] = tasksOrError.right.value

    tasks should have size 1
    tasks.head.name should equal(mockTask.name)
  }

  "getAllAsanaTasksForProject" should "return error when project is not found" in {
    val projectId: String = UUID.randomUUID().toString
    val projectName: String = "myProject"

    val mockProjects: Projects = mock[Projects]
    val mockTasks: Tasks = mock[Tasks]

    val mockClient: Client = mock[Client]
    mockClient.projects = mockProjects
    mockClient.tasks = mockTasks

    val mockProject: Project = mock[Project]
    mockProject.id = projectId

    val mockTask: Task = mock[Task]
    mockTask.name = "Test this test"

    val mockCollectionRequestProject: CollectionRequest[Project] = mock[CollectionRequest[Project]]

    when(mockClient.projects.findAll()).thenReturn(mockCollectionRequestProject)
    when(mockClient.projects.findAll().execute()).thenReturn(singletonList(mockProject))

    val tasksOrError: Either[String, List[Task]] = Asana.getAllAsanaTasksForProject(mockClient, projectName)
    tasksOrError.left.value should startWith("no project found with name")
  }

  "getAllAsanaTasksForProject" should "return error when no tasks were found for the project" in {
    val projectId: String = UUID.randomUUID().toString
    val projectName: String = "myProject"

    val mockProjects: Projects = mock[Projects]
    val mockTasks: Tasks = mock[Tasks]

    val mockClient: Client = mock[Client]
    mockClient.projects = mockProjects
    mockClient.tasks = mockTasks

    val mockProject: Project = mock[Project]
    mockProject.id = projectId
    mockProject.name = projectName

    val mockTask: Task = mock[Task]
    mockTask.name = "Test this test"

    val mockCollectionRequestProject: CollectionRequest[Project] = mock[CollectionRequest[Project]]
    val mockCollectionRequestTask: CollectionRequest[Task] = mock[CollectionRequest[Task]]

    when(mockClient.projects.findAll()).thenReturn(mockCollectionRequestProject)
    when(mockClient.projects.findAll().execute()).thenReturn(singletonList(mockProject))

    when(mockClient.tasks.findByProject(projectId)).thenReturn(mockCollectionRequestTask)
    when(mockClient.tasks.findByProject(projectId).query(anyString(), anyString()))
      .thenReturn(mockCollectionRequestTask)
    when(mockClient.tasks.findByProject(projectId).query(anyString(), anyString()).execute())
      .thenReturn(Collections.emptyList[Task]())


    val tasksOrError: Either[String, List[Task]] = Asana.getAllAsanaTasksForProject(mockClient, projectName)
    tasksOrError.left.value should startWith("no tasks found for the project")
  }

  "getAllAsanaTasksForProject" should "return error when IOException is thrown" in {
    val projectName: String = "myProject"
    val mockProjects: Projects = mock[Projects]

    val mockClient: Client = mock[Client]
    mockClient.projects = mockProjects

    val mockCollectionRequestProject: CollectionRequest[Project] = mock[CollectionRequest[Project]]

    when(mockClient.projects.findAll()).thenReturn(mockCollectionRequestProject)
    when(mockClient.projects.findAll().execute()).thenThrow(new IOException("fail fail"))

    val tasksOrError: Either[String, List[Task]] = Asana.getAllAsanaTasksForProject(mockClient, projectName)
    tasksOrError.left.value should startWith("fail fail")
  }
}
