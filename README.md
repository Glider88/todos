Training application, simple todo rest api.

## How to start

Start postrges:

    docker run -it \
    -e POSTGRES_USER=pavel \
    -e POSTGRES_PASSWORD=pass \
    -e POSTGRES_DB=test \
    -p 5432:5432 \
    postgres

Initialize database schema:

    sbt "run-db-migrations migrate"

Start webserver:

    sbt "runMain todos.Main"

Examples of api requests in the file **polygon.http**

