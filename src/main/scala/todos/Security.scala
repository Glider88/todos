package todos

import de.mkammerer.argon2.Argon2Factory
import todos.user.{Password, EncryptedPassword}
import eu.timepit.refined.auto._

object Security {
  val argon2 = Argon2Factory.create()

  def encrypt(password: Password): EncryptedPassword =
    EncryptedPassword(
      argon2.hash(10, 65536, 1, password.value.toCharArray)
    )

  def verify(hash: EncryptedPassword, password: Password): Boolean =
    argon2.verify(hash.value, password.value.toCharArray)
}
