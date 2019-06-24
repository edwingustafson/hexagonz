scalaVersion := "2.12.8"

name := "ps"
organization := "com.edwingustafson"
version := "1.0"

resolvers += "mvnrepository" at "http://mvnrepository.com/artifact/"

// https://mvnrepository.com/artifact/org.processing/core
libraryDependencies += "org.processing" % "core" % "3.3.7"