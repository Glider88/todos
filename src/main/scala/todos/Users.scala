package todos

import cats.effect._
import doobie._
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.refined.implicits._
import eu.timepit.refined.auto._
import todos.user.EncryptedPassword
import todos.user.User
import todos.user.UserId
import todos.user.UserName
import java.util.UUID

class Users[F[_]: Async](postgres: Transactor[F]) {
  implicit val userRead: Read[User] =
    Read[(UUID, String, String)].map {
      case (id, name, pass) =>
        new User(id, name, pass)
    }

  def findUserById(id: UUID): F[Option[User]] = {
    sql"SELECT id, name, password FROM users WHERE id = $id"
      .query[User]
      .option
      .transact(postgres)
  }
}
