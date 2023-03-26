import complete.DefaultParsers._

// Migration Task
lazy val runMigrate = inputKey[Unit]("Migrates the database schema.")

runMigrate := Def.inputTaskDyn {
  runTask(Compile, "todos.Migrations", migrationParser.parsed)
}.evaluated

lazy val migrationParser = Space ~> (migrate | generate)
lazy val migrate = token("migrate")
lazy val generate = token("generate")
// Migration Task

addCommandAlias("run-db-migrations", "runMigrate")
fork / runMigrate := true
