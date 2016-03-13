name := "asanaweeklyreport"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.asana" % "asana" % "0.4.1",
  "com.typesafe" % "config" % "1.3.0",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
  "javax.mail" % "mail" % "1.4.7",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.mockito" % "mockito-core" % "1.10.19" % "test"
)

// sbt pack
packAutoSettings