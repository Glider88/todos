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

addCommandAlias("migrations", "runMigrate")
fork / runMigrate := true

  // This prepends the String you would type into the shell
lazy val startupTransition: State => State = { s: State =>
  "migrations migrate" :: s
}

// onLoad is scoped to Global because there's only one.
Global / onLoad := {
  val old = (Global / onLoad).value
  // compose the new transition on top of the existing one
  // in case your plugins are using this hook.
  startupTransition compose old
}
