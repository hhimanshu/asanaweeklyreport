package input

import java.time.LocalDate

case object AsanaConfig {
  val AccessTokenKey: String = "access_token"
  val ProjectNameKey: String = "project_name"
}

case class AsanaConfig(access_token: String, projectName: String, reportDate: LocalDate)
