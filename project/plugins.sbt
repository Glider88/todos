ThisBuild / autoStartServer := false

update / evictionWarningOptions := EvictionWarningOptions.empty

addDependencyTreePlugin

addSbtPlugin("nl.gn0s1s" % "sbt-dotenv" % "3.0.0")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.4.2")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.10.4")
addSbtPlugin("ch.epfl.scala" % "sbt-bloop" % "1.5.6")
// addSbtPlugin("org.jmotor.sbt" % "sbt-dependency-updates" % "1.2.7")

addSbtPlugin("com.github.sbt" % "sbt-git" % "2.0.0")
// addSbtPlugin("com.timushev.sbt" % "sbt-rewarn" % "0.1.3")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.6.4")
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.0")
