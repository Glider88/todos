package todos

import pdi.jwt.{JwtCirce, JwtAlgorithm, JwtClaim}
import java.time.Instant
import Configuration.AppConfig
import io.circe.Json
import java.util.UUID
import eu.timepit.refined.auto.*
import todos.auth.JwtToken
import todos.todo.*
import todos.user.*
import cats.syntax.all.*

class Jwt(cfg: AppConfig) {
  private val secret = cfg.jwtSecretKey

  private def json(token: JwtToken): Either[Throwable, Json] =
    JwtCirce
      .decodeJson(token, secret.value, Seq(JwtAlgorithm.HS256))
      .toEither

  def userId(token: JwtToken): Either[Throwable, UUID] =
    json(token)
      .flatMap(_.hcursor.get[UUID]("sub"))

  def token(userId: UserId, ttl: Long = 300L): JwtToken =
    val claim = JwtClaim(
      expiration = Some(Instant.now.nn.plusSeconds(ttl).nn.getEpochSecond),
      issuedAt = Some(Instant.now.nn.getEpochSecond),
      subject = Some(userId.toString)
    )

    JwtCirce.encode(claim, cfg.jwtSecretKey.value, JwtAlgorithm.HS256)
}