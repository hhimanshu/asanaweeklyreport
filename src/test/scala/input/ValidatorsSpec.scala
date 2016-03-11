package input

import java.io.{PrintWriter, File}
import java.time.LocalDate

import com.typesafe.config.Config
import harness.UnitSpec
import input.AsanaConfig.AccessTokenKey
import input.Validators._

class ValidatorsSpec extends UnitSpec {

  "getConfigurationFromFile" should "return error when file does not exists" in {
    val configOrError: Either[List[String], Config] = getConfigurationFromFile(new File("nonExistent"))
    val errors: List[String] = configOrError.left.get
    errors should have size 1
    errors.head should endWith ("does not exists")
  }

  "getConfigurationFromFile" should "return error when file does not contain access_token" in {
    val tempFile: File = File.createTempFile("tempFile", "conf")
    writeToFile(tempFile, List("project_name=someProjectName"))

    val configOrError: Either[List[String], Config] = getConfigurationFromFile(tempFile)
    val errors: List[String] = configOrError.left.get
    errors should have size 1
    errors.head should include ("access_token is missing")
  }

  "getConfigurationFromFile" should "return error when file does not contain project_name" in {
    val tempFile: File = File.createTempFile("tempFile", "conf")
    writeToFile(tempFile, List("access_token=some_access_token"))

    val configOrError: Either[List[String], Config] = getConfigurationFromFile(tempFile)
    val errors: List[String] = configOrError.left.get
    errors should have size 1
    errors.head should include ("project_name is missing")
  }

  "getConfigurationFromFile" should "return no error when file contains access_token and project_name" in {
    val tempFile: File = File.createTempFile("tempFile", "conf")
    writeToFile(tempFile, List("access_token=some_access_token, project_name=someProject"))

    val configOrError: Either[List[String], Config] = getConfigurationFromFile(tempFile)
    val config: Config = configOrError.right.get
    config.getString(AccessTokenKey) shouldBe "some_access_token"
  }

  "getDateForReportOrToday" should "return error on date that could not be parsed" in {
    val dateOrError: Either[String, LocalDate] = getDateForReportOrToday(Array("config", "invalidDateFormat"))
    dateOrError.left.get should include ("could not be parsed")
  }

  "getDateForReportOrToday" should "return today on date which is not provided" in {
    val dateOrError: Either[String, LocalDate] = getDateForReportOrToday(Array())
    dateOrError.right.get should equal(LocalDate.now())
  }

  "getDateForReportOrToday" should "return date for text which is provided" in {
    val dateOrError: Either[String, LocalDate] = getDateForReportOrToday(Array("", "2016-03-09"))
    dateOrError.right.get should equal(LocalDate.of(2016, 3, 9))
  }

  def writeToFile(file: File, contents: List[String]): Unit = {
    new PrintWriter(file) { write(contents.mkString("\n")); close() }
  }
}
