package harness

import org.scalatest.{EitherValues, FlatSpec, ShouldMatchers}

abstract class UnitSpec extends FlatSpec
  with ShouldMatchers
  with EitherValues