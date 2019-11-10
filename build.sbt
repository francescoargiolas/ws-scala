name := "webService"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(  "com.typesafe.akka" %% "akka-http" % "10.1.10")
libraryDependencies ++= Seq(  "com.typesafe.akka" %% "akka-stream" % "2.5.23")

libraryDependencies ++= Seq( "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.10")