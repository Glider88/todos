package todos

import java.util.Calendar
import java.text.SimpleDateFormat
import java.nio.file.{Paths, Files}
import cats.effect.*
import cats.implicits.*
import doobie.*
import doobie.implicits.*
import scala.io.Source
import eu.timepit.refined.auto.*


object Migrations extends IOApp:
  private def generate[F[_]: Sync](config: Configuration.AppConfig): F[Unit] =
    Sync[F].delay {
      val date = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Calendar.getInstance().nn.getTime())
      val filename = s"$date.sql"
      val path = Paths.get(config.resourcesDir, config.migrationDir, filename)
      Files.createFile(path)
      ()
    }

  private def filenames[F[_]: Sync](config: Configuration.AppConfig): F[List[String]] =
    Sync[F].delay {
      val files = Source.fromResource(config.migrationDir).getLines().toList
      val sorted = files.filter(_.endsWith(".sql")).sorted
      sorted
    }

  private def fileContent[F[_]: Sync](config: Configuration.AppConfig, filename: String): F[String] =
    Sync[F].delay {
      val path = Paths.get(config.migrationDir, filename)
      Source.fromResource(path.toString).mkString
    }

  private def createTableStorage[F[_]: Async](transactor: Transactor[F]): F[Int] =
    sql"CREATE TABLE IF NOT EXISTS migration_version(version varchar(25) PRIMARY KEY)"
      .update
      .run
      .transact(transactor)

  private def tableVersions[F[_]: Async](transactor: Transactor[F]): F[List[String]] =
    sql"SELECT version FROM migration_version ORDER BY version"
      .query[String]
      .to[List]
      .transact(transactor)

  private def migrateOne[F[_]: Async](transactor: Transactor[F], name: String, content: String): F[Int] =
    val migration = Update0(content, None).run
    val version = sql"INSERT INTO migration_version(version) VALUES($name)".update.run
    (migration, version).mapN(_ + _).transact(transactor)

  private def migrateAll[F[_]: Async](transactor: Transactor[F], config: Configuration.AppConfig): F[Unit] =
    for {
      fs <- filenames[F](config)
      vs <- tableVersions[F](transactor)
       _ <- fs.diff(vs)
             .traverse(v => fileContent[F](config, v)
             .flatMap(c => migrateOne(transactor, v, c)))
    } yield ()

  def run(args: List[String]): IO[ExitCode] =
    for {
      cfg <- Configuration.config().load[IO]
             transactor = Doobie.transactor[IO](cfg.postgres)
        _ <- createTableStorage[IO](transactor)
        _ <- args.head match
          case "generate" => generate[IO](cfg)
          case "migrate" => migrateAll[IO](transactor, cfg)
          case i => IO.println(s"incorrect $i").as(ExitCode.Error)
    } yield ExitCode.Success
