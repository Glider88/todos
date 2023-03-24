import Dependencies._
import complete.DefaultParsers._

ThisBuild / scalaVersion     := "2.13.10" // 3.2.2
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.todos"
ThisBuild / organizationName := "todos"

val doobieVersion = "1.0.0-RC2"
val http4sVersion = "0.23.19-RC3"
val circeVersion = "0.14.5"
val tapirVersion = "1.2.11"
val jwtScalaVersion = "9.2.0"
val jwtHttp4sAuthVersion = "1.0.0"
val derevoVersion = "0.13.0"

// Migration Task
lazy val runMigrate = inputKey[Unit]("Migrates the database schema.")

runMigrate := Def.inputTaskDyn {
  runTask(Compile, "todos.Migrations", migrationParser.parsed)
}.evaluated

lazy val migrationParser = Space ~> (migrate | generate)
lazy val migrate = token("migrate")
lazy val generate = token("generate")
// Migration Task

addCommandAlias("run-db-migrations", "runMigrate")
fork / runMigrate := true

lazy val root = (project in file("."))
  .settings(
    name := "polygon",
    scalacOptions ++= List("-Ymacro-annotations"),
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.15" % Test,
      "org.typelevel" %% "cats-effect" % "3.4.8",
      "org.typelevel" %% "cats-core"   % "2.9.0",
      "org.tpolecat"  %% "doobie-core"     % doobieVersion,
      "org.tpolecat"  %% "doobie-postgres" % doobieVersion,
      "is.cir"          %% "ciris" % "3.1.0",
      "org.http4s"      %% "http4s-ember-server" % http4sVersion,
      "org.http4s"      %% "http4s-ember-client" % http4sVersion,
      "org.http4s"      %% "http4s-circe"        % http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % http4sVersion,
      "io.circe"        %% "circe-generic"       % circeVersion,
      "co.fs2" %% "fs2-core" % "3.6.1",
      "com.softwaremill.sttp.tapir" %% "tapir-core"               % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe"         % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"      % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle"  % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % tapirVersion,
      // "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion,
      "com.github.jwt-scala" %% "jwt-core" % jwtScalaVersion,
      "com.github.jwt-scala" %% "jwt-circe" % jwtScalaVersion,
      "de.mkammerer" % "argon2-jvm" % "2.11",
      "org.typelevel" %% "log4cats-core"    % "2.5.0",
      "org.typelevel" %% "log4cats-slf4j"   % "2.5.0",
      "ch.qos.logback" % "logback-classic" % "1.4.6",
      "io.estatico" %% "newtype" % "0.4.4",
      "eu.timepit" %% "refined"          % "0.10.3",
      "eu.timepit" %% "refined-cats"     % "0.10.3",
      "io.circe"   %% "circe-refined"    % circeVersion,
      "is.cir"     %% "ciris-refined"    % "3.1.0",
      "org.tpolecat"  %% "doobie-refined" % doobieVersion,
      "tf.tofu" %% "derevo-core" % derevoVersion,
      "tf.tofu" %% "derevo-cats" % derevoVersion,
      "tf.tofu" %% "derevo-circe" % derevoVersion excludeAll(
        ExclusionRule("io.circe", "circe-derivation_2.13")
      ),
      "io.circe" %% "circe-derivation" % "0.13.0-M5" excludeAll(
        ExclusionRule("io.circe", "circe-core_2.13")
      ),
      "dev.optics" %% "monocle-core"  % "3.2.0",
      "dev.optics" %% "monocle-macro" % "3.2.0",
      "com.softwaremill.sttp.tapir" %% "tapir-refined" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-newtype" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-derevo" % tapirVersion,
    )
  )
