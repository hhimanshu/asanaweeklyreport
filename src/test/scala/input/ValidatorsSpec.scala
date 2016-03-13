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
    errors.head should endWith("does not exists")
  }

  "getConfigurationFromFile" should "return error when file does not contain access_token" in {
    val tempFile: File = File.createTempFile("tempFile", "conf")
    writeToFile(tempFile, getConfigurationExcluding(List("access_token")))

    val configOrError: Either[List[String], Config] = getConfigurationFromFile(tempFile)
    val errors: List[String] = configOrError.left.get
    errors should have size 1
    errors.head should include("access_token is missing")
  }

  "getConfigurationFromFile" should "return error when file does not contain project_name" in {
    val tempFile: File = File.createTempFile("tempFile", "conf")
    writeToFile(tempFile, getConfigurationExcluding(List("project_name")))

    val configOrError: Either[List[String], Config] = getConfigurationFromFile(tempFile)
    val errors: List[String] = configOrError.left.get
    errors should have size 1
    errors.head should include("project_name is missing")
  }

  "getConfigurationFromFile" should "return error when email.username, email.password, email.recipients are missing" in {
    val tempFile: File = File.createTempFile("tempFile", "conf")
    writeToFile(tempFile, getConfigurationExcluding(List("email.username", "email.password", "email.recipients")))

    val configOrError: Either[List[String], Config] = getConfigurationFromFile(tempFile)
    val errors: List[String] = configOrError.left.get
    errors should have size 3
    errors shouldBe List("email.username is missing", "email.password is missing", "email.recipients is missing")
  }

  "getConfigurationFromFile" should "return no error when file contains all required" in {
    val tempFile: File = File.createTempFile("tempFile", "conf")
    writeToFile(tempFile, getConfigurationExcluding(List.empty))

    val configOrError: Either[List[String], Config] = getConfigurationFromFile(tempFile)
    val config: Config = configOrError.right.get
    config.getString(AccessTokenKey) shouldBe "some_access_token"
  }

  "getDateForReportOrToday" should "return error on date that could not be parsed" in {
    val dateOrError: Either[String, LocalDate] = getDateForReportOrToday(Array("config", "invalidDateFormat"))
    dateOrError.left.get should include("could not be parsed")
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
    new PrintWriter(file) {
      write(contents.mkString("\n"));
      close()
    }
  }

  def getConfigurationExcluding(keys: List[String]): List[String] = {
    val configuration: Map[String, String] = Map("access_token" -> "some_access_token",
      "project_name" -> "myDaily",
      "email.username" -> "\"email@gmail.com\"",
      "email.password" -> "password",
      "email.recipients" -> "\"a@b.com,c.d@e.com\"") -- keys

    configuration.map { case (k, v) => k + "=" + v }.toList
  }
}
