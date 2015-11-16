name := "play-handlebars-demo"

organization := "com.lauraluiz"

version := "1.0-SNAPSHOT"


lazy val `play-handlebars-demo` = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(Seq (
    scalaVersion := "2.10.5",
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    resolvers ++= Seq (Resolver.mavenLocal),
    libraryDependencies ++= Seq (
      "com.lauraluiz" % "handlebars-webjars-demo" % "0.0.1-SNAPSHOT",
      "org.webjars" % "webjars-play_2.10" % "2.4.0-1",
      "com.github.jknack" % "handlebars" % "2.2.3",
      "org.assertj" % "assertj-core" % "3.0.0" % "test"
    ),
    dependencyOverrides ++= Set (
      "com.google.guava" % "guava" % "18.0",
      "junit" % "junit" % "4.12" % "test"
    )
  ))