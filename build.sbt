name := """natebot"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

coverageExcludedPackages := "" +
  "Module;" +
  ".*\\.Reverse.*;" +
  "router\\..*;" +
  "models.Forms;" +
  "models.JsonFormats"

libraryDependencies ++= Seq(
  ws,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.12.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test,
  "org.mockito" % "mockito-all" % "1.10.19" % Test
)

