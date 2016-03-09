name := "asanaweeklyreport"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.asana" % "asana" % "0.4.1"
)

// sbt pack
packAutoSettings