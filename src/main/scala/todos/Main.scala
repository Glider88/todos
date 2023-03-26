package todos

import cats.effect.IOApp
import cats.effect.*

object Main extends IOApp:
  def run(args: List[String]): IO[ExitCode] =
    Configuration.config().load[IO].flatMap(cfg => {
      val transactor = Doobie.transactor[IO](cfg.postgres)
      val users = new Users(transactor)
      val todos = new Todos(transactor)
      val jwt = new Jwt(cfg)
      val httpApp = Http4s.mkHttpApp[IO](todos, users, jwt)
      Server.ember(httpApp)
    }).as(ExitCode.Success)
