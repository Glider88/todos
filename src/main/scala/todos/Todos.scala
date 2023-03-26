package todos

import cats.effect.*
import cats.implicits.*
import doobie.*
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.refined.implicits.*
import todos.todo.*

import java.util.UUID

class Todos[F[_]: Async](postgres: Transactor[F]):

  implicit val todoRead: Read[Todo] =
    Read[(UUID, String)].map {
      case (id, title) => new Todo(id, title)
    }

  def findAll(limit: Option[Long]): F[List[Todo]] =
    val todos = sql"SELECT id, title FROM todo".query[Todo].stream
    val limitedTodos = limit match
      case Some(l) => todos.take(l)
      case None => todos
    limitedTodos.compile.toList.transact(postgres)

  def find(id: UUID): F[Option[Todo]] =
    sql"SELECT id, title FROM todo WHERE id = $id"
      .query[Todo]
      .option
      .transact(postgres)

  def add(todo: NewTodo): F[UUID] =
    def insert(uuid: UUID): F[Unit] =
      sql"INSERT INTO todo(id, title) VALUES($uuid, $todo)"
        .update
        .run
        .void
        .transact(postgres)

    for {
      newUuid <- ID.mk[F]()
      _ <- insert(newUuid)
    } yield newUuid

object ID {
  def mk[F[_]: Sync](): F[UUID] =
    Sync[F].delay(UUID.randomUUID().nn)
}