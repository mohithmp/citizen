name := """citizen"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  filters,
  "com.github.fge" % "json-schema-validator" % "2.2.6",
  "org.mongodb.morphia" % "morphia" % "1.2.1",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.mongodb" % "mongo-java-driver" % "3.3.0",
  "com.google.api-client" % "google-api-client" % "1.26.0",
  "com.google.apis" % "google-api-services-sheets" % "v4-rev516-1.23.0",
  "com.google.oauth-client" % "google-oauth-client-jetty" % "1.23.0",
  "com.mashape.unirest" % "unirest-java" % "1.4.9"
  
)
