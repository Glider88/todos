package todos

import cats.implicits._
import eu.timepit.refined.types.string.NonEmptyString
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import ciris._
import ciris.refined._

object Configuration {
  case class PostgresConfig(
    driver: NonEmptyString,
    url: NonEmptyString,
    user: NonEmptyString,
    pass: Secret[NonEmptyString]
  )

  case class AppConfig(
    postgres: PostgresConfig,
    migrationDir: NonEmptyString,
    resourcesDir: NonEmptyString,
    jwtSecretKey: Secret[NonEmptyString]
  )

  def config(): ConfigValue[Effect, AppConfig] = {
    (
      env("POSTGRES_USERNAME").as[NonEmptyString],
      env("POSTGRES_PASSWORD").as[NonEmptyString].secret,
      env("JWT_SECRET_KEY").as[NonEmptyString].secret
    ).parMapN { (username, password, jwt_secret) =>
      AppConfig(
        PostgresConfig(
          driver = "org.postgresql.Driver",
          url = "jdbc:postgresql:test",
          user = username,
          pass = password
        ),
        "migrations",
        "src/main/resources",
        jwt_secret
      )
    }
  }
}
