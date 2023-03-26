package todos

import cats.effect.*
import com.comcast.ip4s.*
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder

object Server:
  def ember(httpApp: HttpApp[IO]): IO[ExitCode] =
    EmberServerBuilder.default[IO]
      .withHttpApp(httpApp)
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
