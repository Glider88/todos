import sbt._

object Dependencies {
  object com {
    object eed3si9n {
      object expecty {
        val expecty = "com.eed3si9n.expecty" %% "expecty" % "0.16.0"
      }
    }

    object softwaremill {
      object sttp {
        object tapir {
          private val tapir = "1.2.11"
          val `tapir-core` = "com.softwaremill.sttp.tapir" %% "tapir-core" % tapir
          val `tapir-json-circe` = "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapir
          val `tapir-http4s-server` = "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapir
          val `tapir-swagger-ui-bundle` = "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapir
          val `tapir-openapi-docs` = "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapir
          val `tapir-refined` = "com.softwaremill.sttp.tapir" %% "tapir-refined" % tapir
        }
      }
    }

    object github {
      object `jwt-scala` {
        private val jwt = "9.2.0"
        val `jwt-core` = "com.github.jwt-scala" %% "jwt-core" % jwt
        val `jwt-circe` = "com.github.jwt-scala" %% "jwt-circe" % jwt
      }
    }
  }

  object org {
    object typelevel {
      val `cats-effect` = "org.typelevel" %% "cats-effect" % "3.4.8"
      val `cats-core` = "org.typelevel" %% "cats-core" % "2.9.0"

      private val log4cats = "2.5.0"
      val `log4cats-core` = "org.typelevel" %% "log4cats-core" % log4cats
      val `log4cats-slf4j` = "org.typelevel" %% "log4cats-slf4j" % log4cats
    }

    object tpolecat {
      private val doobie = "1.0.0-RC2"
      val `doobie-core` = "org.tpolecat" %% "doobie-core" % doobie
      val `doobie-postgres` = "org.tpolecat" %% "doobie-postgres" % doobie
      val `doobie-refined` = "org.tpolecat" %% "doobie-refined" % doobie
    }

    object http4s {
      private val http4s = "0.23.19-RC3"
      val `http4s-ember-server` = "org.http4s" %% "http4s-ember-server" % http4s
      val `http4s-ember-client` = "org.http4s" %% "http4s-ember-client" % http4s
      val `http4s-circe` = "org.http4s" %% "http4s-circe" % http4s
      val `http4s-dsl` = "org.http4s" %% "http4s-dsl" % http4s
    }

    object scalatest {
      val scalatest = "org.scalatest" %% "scalatest" % "3.2.15"
    }

    object scalatestplus {
      val `scalacheck-1-17` = "org.scalatestplus" %% "scalacheck-1-17" % "3.2.15.0"
    }
  }

  object co {
    object fs2 {
      val `fs2-core` = "co.fs2" %% "fs2-core" % "3.6.1"
    }
  }

  object `io.circe` {
    private val cir = "0.14.5"
    val `circe-generic` = "io.circe" %% "circe-generic" % cir
    val `circe-refined` = "io.circe" %% "circe-refined" % cir
  }

  object dev {
    object optics {
      private val monocle = "3.2.0"
      val `monocle-core` = "dev.optics" %% "monocle-core" % monocle
      val `monocle-macro` = "dev.optics" %% "monocle-macro" % monocle
    }
  }

  object is {
    object cir {
      private val cir = "3.1.0"
      val ciris = "is.cir" %% "ciris" % cir
      val `ciris-refined` = "is.cir" %% "ciris-refined" % cir
    }
  }

  object eu {
    object timepit {
      private val ref = "0.10.3"
      val refined = "eu.timepit" %% "refined" % ref
      val `refined-cats` = "eu.timepit" %% "refined-cats" % ref
    }
  }

  object ch {
    object qos {
      object logback {
        val `logback-classic` = "ch.qos.logback" % "logback-classic" % "1.4.6"
      }
    }
  }

  object de {
    object mkammerer {
      val `argon2-jvm` = "de.mkammerer" % "argon2-jvm" % "2.11"
    }
  }
}
