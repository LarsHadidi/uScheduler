name := "uScheduler3"

version := "0.1.0"

scalaVersion := "2.10.4"

logLevel := Level.Info

fork in test:= true

fork in run := true

scalacOptions += "-deprecation"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

libraryDependencies += "org.apache.shiro" % "shiro-all" % "1.2.3"

libraryDependencies += "commons-beanutils" % "commons-beanutils" % "1.9.1"

libraryDependencies += "com.h2database" % "h2" % "1.3.176"

libraryDependencies += "jline" % "jline" % "2.11"