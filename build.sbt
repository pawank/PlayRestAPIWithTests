name := """PlayRestAPIWithTests"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "net.sourceforge.htmlunit" % "htmlunit" % "2.19" % "test",
  "joda-time" % "joda-time" % "2.5",
  "org.xerial" % "sqlite-jdbc" % "3.8.6",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  "com.typesafe.play" %% "play-slick" % "1.1.0",
  "com.emotioncity" %% "soriento" % "0.1.0-SNAPSHOT",
  "org.scalatestplus" %% "play" % "1.4.0-M3" % "test"
)

routesImport += "helpers.AppPathBinders._"

libraryDependencies += evolutions

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += "Local ivy2 Repository" at "file:///Users/pawan/.ivy2/local"



// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := ".*index.*;.*main.*"

parallelExecution in Test := false
