name := """citizen"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.github.fge" % "json-schema-validator" % "2.2.6",
  "org.mongodb.morphia" % "morphia" % "1.2.1",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.mongodb" % "mongo-java-driver" % "3.3.0"
)
