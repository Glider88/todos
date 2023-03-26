package todos

import eu.timepit.refined.auto.*
import todos.auth.*
import todos.todo.*
import todos.user.*
import sttp.tapir.*
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*

import java.util.UUID
import io.circe.generic.auto.*
import scala.language.unsafeNulls

object Tapir extends TapirCodecRefined:
  private val baseEndpoint =
    endpoint
      .errorOut(stringBody)
      .in("todos")

  private val limitParameter =
    query[Option[Long]]("limit")
      .description("Maximum number of books to retrieve")

  val token: PublicEndpoint[AuthRequest, String, AuthResponse, Any] =
    endpoint
      .post
      .in("auth")
      .in(
        jsonBody[AuthRequest]
          .description("getting auth token")
          .example(
            AuthRequest(UUID.fromString("2c0902c8-2314-41a1-8351-5f74eb473f96").nn, "pass1")
          )
      )
      .out(jsonBody[AuthResponse])
      .errorOut(stringBody)

  val listing: PublicEndpoint[Option[Long], String, List[Todo], Any] =
    baseEndpoint
      .get
      .in("list" / "all")
      .in(limitParameter)
      .out(jsonBody[List[Todo]])

  val find: PublicEndpoint[UUID, String, Todo, Any] =
    baseEndpoint
      .get
      .in("find")
      .in(path[UUID]("id"))
      .out(jsonBody[Todo])

  val authAdd: Endpoint[JwtToken, NewTodo, String, NewTodoResponse, Any] =
    endpoint
      .post
      .in("todos")
      .in("add")
      .securityIn(auth.bearer[JwtToken]())
      .in(
        jsonBody[NewTodo]
          .description("new todo")
          .example(
            NewTodo("First todo")
          )
      )
      .errorOut(stringBody)
      .out(jsonBody[NewTodoResponse])
