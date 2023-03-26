package todos

import monocle.Iso

import java.util.UUID
import scala.annotation.implicitNotFound

object user {
  type UserId = UUID

  // type Rgx = "^[A-Z]"
  // type UserNamePred = String Refined MatchesRegex[Rgx]

  type UserName = String
  type Password = String
  type EncryptedPassword = String

  case class User(id: UserId, name: UserName, password: EncryptedPassword)
}

object todo {
  type TodoId = UUID
  type TodoTitle = String

  case class Todo(id: TodoId, title: TodoTitle)
  case class NewTodo(title: TodoTitle)
  case class NewTodoResponse(newUuid: UUID)
}

object auth {
  import user._

  type JwtToken = String

  case class AuthRequest(userId: UUID, password: Password)
  case class AuthResponse(token: JwtToken)
}
