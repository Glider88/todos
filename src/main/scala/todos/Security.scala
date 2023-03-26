package todos

import de.mkammerer.argon2.Argon2Factory
import todos.user.*
import eu.timepit.refined.auto.*

object Security:
  val argon2 = Argon2Factory.create().nn

  def encrypt(password: Password): EncryptedPassword =
      argon2.hash(10, 65536, 1, password.toCharArray).nn

  def verify(hash: EncryptedPassword, password: Password): Boolean =
    argon2.verify(hash, password.toCharArray)
