package input

import java.time.{DayOfWeek, LocalDate}

import harness.UnitSpec
import input.AsanaWeekly.getFirstDateForWeek

class AsanaWeeklySpec extends UnitSpec {

  "getFirstDayOfWeek" should "return March 7, 2016 as first day of week given March 9, 2016 as input" in {
    val firstDateForWeek: LocalDate = getFirstDateForWeek(LocalDate.of(2016, 3, 9))
    firstDateForWeek should equal(LocalDate.of(2016, 3, 7))
    firstDateForWeek.getDayOfWeek shouldBe DayOfWeek.MONDAY
  }

  "getFirstDayOfWeek" should "return Dec 28, 2015 as first day of week given Jan 1, 2016 as input" in {
    val firstDateForWeek: LocalDate = getFirstDateForWeek(LocalDate.of(2016, 1, 1))
    firstDateForWeek should equal(LocalDate.of(2015, 12, 28))
    firstDateForWeek.getDayOfWeek shouldBe DayOfWeek.MONDAY
  }

  "getFirstDayOfWeek" should "return Dec 26, 2016 as first day of week given Dec 31, 2016 as input" in {
    val firstDateForWeek: LocalDate = getFirstDateForWeek(LocalDate.of(2016, 12, 31))
    firstDateForWeek should equal(LocalDate.of(2016, 12, 26))
    firstDateForWeek.getDayOfWeek shouldBe DayOfWeek.MONDAY
  }

  "getFirstDayOfWeek" should "return Feb 29, 2016 as first day of week given Feb 29, 2016 as input (leap year)" in {
    val firstDateForWeek: LocalDate = getFirstDateForWeek(LocalDate.of(2016, 2, 29))
    firstDateForWeek should equal(LocalDate.of(2016, 2, 29))
    firstDateForWeek.getDayOfWeek shouldBe DayOfWeek.MONDAY
  }
}
