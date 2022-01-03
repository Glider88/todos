package todos

import cats.effect._
import cats.syntax.all._
import eu.timepit.refined.auto._
import org.http4s._
import org.http4s.server.middleware.{Logger => LoggerMiddleware}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import todos.auth.AuthRequest
import todos.auth.AuthResponse
import todos.auth.JwtToken
import todos.todo._
import todos.user._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

import java.util.UUID

object Http4s {
  def mkHttpApp[F[_]](todos: Todos[F], users: Users[F], jwt: Jwt)(implicit F: Async[F]): HttpApp[F] = {
    implicit def unsafeLogger[G[_]: Sync] = Slf4jLogger.getLogger[G]

    val listing: HttpRoutes[F] =
      Http4sServerInterpreter[F]().toRoutes(
        Tapir.listing.serverLogic(
          todos
            .findAll(_)
            .map(Either.right[String, List[Todo]](_))
        )
      )

    def auth(token: JwtToken): F[Either[String, User]] =
      Logger[F].info(s"Authentication for token: $token") *>
      {
        jwt.userId(token) match {
          case Right(uuid) => users.findUserById(uuid).map(_ match {
            case Some(u) => Right(u)
            case None => Left("no user")
          })
          case Left(error) => F.pure(Left(error.toString))
        }
      }

    val authAdd: HttpRoutes[F] =
      Http4sServerInterpreter[F]().toRoutes(
        Tapir
          .authAdd
          .serverSecurityLogic[User, F](auth(_))
          .serverLogic (
            (_: User) => (todo: NewTodo) =>
              todos
                .add(todo)
                .map(newUuid => Either.right[String, NewTodoResponse](NewTodoResponse(newUuid)))
          )
      )

    def verify(request: AuthRequest, user: User): Either[String, AuthResponse] = {
      if (Security.verify(user.password, request.password)) {
        Right(AuthResponse(jwt.token(user.id)))
      } else {
        Left("Incorrect token")
      }
    }

    val token: HttpRoutes[F] =
      Http4sServerInterpreter[F]().toRoutes(
        Tapir
          .token
          .serverLogic((req: AuthRequest) => {
            users.findUserById(UUID.fromString(req.username.show)).map {
              case Some(u) => verify(req, u) match {
                case r @ Right(_) => r
                case l @ Left(_) => l
              }
              case None => Left("no user")
            }
          })
      )

    val find: HttpRoutes[F] =
      Http4sServerInterpreter[F]().toRoutes(
        Tapir.find.serverLogic(todos.find(_).map {
          case Some(t) => Right(t)
          case None => Left("No user")
        })
      )

    val swagger: HttpRoutes[F] =
      Http4sServerInterpreter[F]().toRoutes(
        SwaggerInterpreter().fromEndpoints[F](
          List(Tapir.token, Tapir.listing, Tapir.authAdd, Tapir.find),
          "The tapir library",
          "1.0.0"
        )
      )

    val logger: HttpApp[F] => HttpApp[F] =
      (http: HttpApp[F]) => LoggerMiddleware.httpApp(true, true)(http)

    val routes = (listing <+> find <+> swagger <+> token <+> authAdd).orNotFound
    logger(routes)
  }
}
