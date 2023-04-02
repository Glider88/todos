Training application, simple todo rest api.

## How to start

Start postrges:

    docker run -it \
    -e POSTGRES_USER=pavel \
    -e POSTGRES_PASSWORD=pass \
    -e POSTGRES_DB=todo \
    -p 5432:5432 \
    postgres

Start webserver:

    sbt "runMain todos.Main"

Make fat jar:

    sbt assembly

Build docker image:

    sbt docker

Run app in docker:

    docker run -it \
    -e POSTGRES_USERNAME=pavel \
    -e POSTGRES_PASSWORD=pass \
    -e JWT_SECRET_KEY=secret-key \
    com.todos/polygon

Examples of api requests in the file **polygon.http**
