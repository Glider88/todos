package todos

import doobie.util.transactor.Transactor
import cats.effect.kernel.Async
import todos.Configuration.PostgresConfig
import eu.timepit.refined.auto._

object Doobie {
  def transactor[F[_]: Async](cfg: PostgresConfig): Transactor[F] =
    Transactor.fromDriverManager[F](
      cfg.driver,
      cfg.url,
      cfg.user,
      cfg.pass.value
    )
}
