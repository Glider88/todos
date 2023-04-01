import Dependencies._

ThisBuild / scalaVersion     := "3.2.2"
ThisBuild / organization     := "com.todos"
ThisBuild / organizationName := "todos"

enablePlugins(
  DockerPlugin,
)

ThisBuild / scalacOptions ++=
  Seq(
    "-deprecation",
    "-explain",
    "-feature",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Yexplicit-nulls", // experimental (I've seen it cause issues with circe)
    "-Ykind-projector",
    "-Ysafe-init", // experimental (I've seen it cause issues with circe)
  ) ++ Seq("-rewrite", "-indent") ++ Seq("-source", "future-migration")

lazy val root =
  project
    .in(file("."))
    .settings(name := "polygon")
    .settings(commonSettings)
    .settings(
      assembly / mainClass := Some("todos.Main"),
      assembly / assemblyJarName := "polygon.jar",
      ThisBuild / assemblyMergeStrategy := {
        // fix swagger-tapir: https://tapir.softwaremill.com/en/latest/docs/openapi.html#using-swaggerui-with-sbt-assembly
        case PathList("META-INF", "maven", "org.webjars", "swagger-ui", "pom.properties") =>
          MergeStrategy.singleOrError
        case PathList(ps @ _*) if ps.last endsWith ".class" => MergeStrategy.first
        case x =>
          val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
          oldStrategy(x)
      }
    )
    .settings(
      docker / dockerfile := {
        // The assembly task generates a fat JAR file
        val artifact: File = assembly.value
        val artifactTargetPath = s"/app/${artifact.name}"

        new Dockerfile {
          from("openjdk:19")
          add(artifact, artifactTargetPath)
          entryPoint("java", "-jar", artifactTargetPath)
        }
      },
      docker / buildOptions := BuildOptions(
        cache = false,
        // removeIntermediateContainers = BuildOptions.Remove.Always,
        // pullBaseImage = BuildOptions.Pull.Always,
      )
    )
    .settings(dependencies)

lazy val commonSettings = {
  lazy val commonScalacOptions = Seq(
    Compile / console / scalacOptions --= Seq(
      "-Wunused:_",
      "-Xfatal-warnings",
    ),
    Test / console / scalacOptions :=
      (Compile / console / scalacOptions).value,
  )

  lazy val otherCommonSettings = Seq(
    update / evictionWarningOptions := EvictionWarningOptions.empty,
    Compile / run / fork := true,
  )

  Seq(
    commonScalacOptions,
    otherCommonSettings,
  ).reduceLeft(_ ++ _)
}

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    org.typelevel.`cats-effect`,
    org.typelevel.`cats-core`,
    org.tpolecat.`doobie-core`,
    org.tpolecat.`doobie-postgres`,
    is.cir.ciris,
    is.cir.`ciris-refined`,
    org.http4s.`http4s-ember-server`,
    org.http4s.`http4s-ember-client`,
    org.http4s.`http4s-circe`,
    org.http4s.`http4s-dsl`,
    co.fs2.`fs2-core`,
    com.softwaremill.sttp.tapir.`tapir-core`,
    com.softwaremill.sttp.tapir.`tapir-json-circe`,
    com.softwaremill.sttp.tapir.`tapir-http4s-server`,
    com.softwaremill.sttp.tapir.`tapir-swagger-ui-bundle`,
    com.softwaremill.sttp.tapir.`tapir-openapi-docs`,
    com.github.`jwt-scala`.`jwt-core`,
    com.github.`jwt-scala`.`jwt-circe`,
    de.mkammerer.`argon2-jvm`,
    org.typelevel.`log4cats-core`,
    org.typelevel.`log4cats-slf4j`,
    ch.qos.logback.`logback-classic`,
    eu.timepit.refined,
    eu.timepit.`refined-cats`,
    `io.circe`.`circe-refined`,
    `io.circe`.`circe-generic`,
    org.tpolecat.`doobie-refined`,
    dev.optics.`monocle-core`,
    dev.optics.`monocle-macro`,
    com.softwaremill.sttp.tapir.`tapir-refined`,
  ),
  libraryDependencies ++= Seq(
    com.eed3si9n.expecty.expecty,
    org.scalatest.scalatest,
    org.scalatestplus.`scalacheck-1-17`,
  ).map(_ % Test),
)
