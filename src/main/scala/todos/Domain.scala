package todos

import derevo.Derivation
import derevo.NewTypeDerivation
import derevo.cats._
import derevo.circe.magnolia.decoder
import derevo.circe.magnolia.encoder
import derevo.derive
import io.estatico.newtype.macros.newtype
import monocle.Iso

import java.util.UUID
import scala.annotation.implicitNotFound

object user {
  @derive(decoder, encoder, eqv, show, uuid)
  @newtype
  case class UserId(value: UUID)

  // type Rgx = "^[A-Z]"
  // type UserNamePred = String Refined MatchesRegex[Rgx]

  @derive(decoder, encoder, eqv, show)
  @newtype
  case class UserName(value: String)

  @derive(decoder, encoder, eqv, show)
  @newtype
  case class Password(value: String)

  @derive(decoder, encoder, eqv, show)
  @newtype
  case class EncryptedPassword(value: String)

  @derive(decoder, encoder, show)
  case class User(id: UserId, name: UserName, password: EncryptedPassword)
}

object todo {
  @derive(decoder, encoder, eqv, show, uuid)
  @newtype
  case class TodoId(value: UUID)

  @derive(decoder, encoder, eqv, show)
  @newtype
  case class TodoTitle(value: String)

  @derive(decoder, encoder, show)
  case class Todo(id: TodoId, title: TodoTitle)

  @derive(decoder, encoder, show)
  case class NewTodo(title: TodoTitle)

  @derive(decoder, encoder, show)
  case class NewTodoResponse(newUuid: UUID)
}

object auth {
  import user.{UserName, Password}

  @derive(decoder, encoder, eqv, show)
  @newtype
  case class JwtToken(value: String)

  //type JwtToken = String
  case class AuthRequest(username: UserName, password: Password)
  case class AuthResponse(token: JwtToken)
}

trait IsUUID[A] {
  def _UUID: Iso[UUID, A]
}

object IsUUID {
  def apply[A: IsUUID]: IsUUID[A] = implicitly

  implicit val identityUUID: IsUUID[UUID] = new IsUUID[UUID] {
    val _UUID = Iso[UUID, UUID](identity)(identity)
  }
}

trait Derive[F[_]] extends Derivation[F] with NewTypeDerivation[F] {
  def instance(implicit ev: OnlyNewtypes): Nothing = ev.absurd

  @implicitNotFound("Only newtypes instances can be derived")
  abstract final class OnlyNewtypes {
    def absurd: Nothing = ???
  }
}

object uuid extends Derive[IsUUID]
