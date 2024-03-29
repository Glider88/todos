package todos

import cats.implicits.*
import eu.timepit.refined.types.string.NonEmptyString
import eu.timepit.refined.auto.*
import eu.timepit.refined.cats.*
import ciris.*
import ciris.refined.*

object Configuration:
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

  def config(): ConfigValue[Effect, AppConfig] =
    (
      env("POSTGRES_USERNAME").as[NonEmptyString],
      env("POSTGRES_PASSWORD").as[NonEmptyString].secret,
      env("JWT_SECRET_KEY").as[NonEmptyString].secret
    ).parMapN { (username, password, jwt_secret) =>
      AppConfig(
        PostgresConfig(
          driver = NonEmptyString.unsafeFrom("org.postgresql.Driver"),
          url = NonEmptyString.unsafeFrom("jdbc:postgresql:todo"),
          user = username,
          pass = password
        ),
        NonEmptyString.unsafeFrom("migrations"),
        NonEmptyString.unsafeFrom("src/main/resources"),
        jwt_secret
      )
    }
