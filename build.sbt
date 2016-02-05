name := """play-mongo"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.mongodb.morphia" % "morphia-logging-slf4j" % "1.0.1" exclude("org.slf4j", "slf4j-log4j12"),
  "org.mongodb.morphia" % "morphia" % "1.0.1" exclude("org.slf4j", "slf4j-log4j12")
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
