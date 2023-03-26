package todos

import pdi.jwt.{JwtCirce, JwtAlgorithm, JwtClaim}
import java.time.Instant
import Configuration.AppConfig
import io.circe.Json
import java.util.UUID
import eu.timepit.refined.auto._
import todos.auth.JwtToken
import todos.todo._
import todos.user._
import cats.syntax.all._

class Jwt(cfg: AppConfig) {
  private val secret = cfg.jwtSecretKey

  private def json(token: JwtToken): Either[Throwable, Json] =
    JwtCirce
      .decodeJson(token, secret.value, Seq(JwtAlgorithm.HS256))
      .toEither

  def userId(token: JwtToken): Either[Throwable, UUID] =
    json(token)
      .flatMap(_.hcursor.get[UUID]("sub"))

  def token(userId: UserId, ttl: Long = 300L): JwtToken = {
    val claim = JwtClaim(
      expiration = Some(Instant.now.plusSeconds(ttl).getEpochSecond),
      issuedAt = Some(Instant.now.getEpochSecond),
      subject = Some(userId.toString)
    )

    JwtCirce.encode(claim, cfg.jwtSecretKey.value, JwtAlgorithm.HS256)
  }
}