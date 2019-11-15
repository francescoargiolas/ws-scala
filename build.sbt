name := "webService"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(  "com.typesafe.akka" %% "akka-http" % "10.1.10")
libraryDependencies ++= Seq(  "com.typesafe.akka" %% "akka-stream" % "2.5.23")

libraryDependencies ++= Seq( "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.10")
libraryDependencies += "org.squeryl" %% "squeryl" % "0.9.14"
libraryDependencies += "c3p0" % "c3p0" % "0.9.1.2"
libraryDependencies += "postgresql" % "postgresql" % "9.0-801.jdbc4"
libraryDependencies += "com.h2database" % "h2" % "1.2.127"

lazy val logback = "ch.qos.logback" % "logback-classic" % "1.0.0" % "runtime"

