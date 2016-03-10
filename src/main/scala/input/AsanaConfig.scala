package input

import java.time.LocalDate

case object AsanaConfig {
  val AccessTokenKey: String = "access_token"
}

case class AsanaConfig(access_token: String, reportDate: LocalDate)
