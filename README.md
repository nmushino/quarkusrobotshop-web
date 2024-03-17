# Docs
Please see the Github Pages Site for complete documentation: [quarkusrobotshop.github.io](https://quarkusrobotshop.github.io)

# Quarkus

If you have not used Quarkus before: https://quarkus.io/

# quarkuscofeeshop-web

This is the web frontend for the Quarkus robotshop Application

Orders can be placed through the web UI or a REST endpoint "/order"

## Local Development

You will need to start the supporting services, Kafka and PostgreSQL, from the [quarkusrobotshop-support](https://github.com/quarkusrobotshop/quarkusrobotshop-support.git) project:

```shell
git clone https://github.com/quarkusrobotshop/quarkusrobotshop-support.git
```

The services can be started with Docker compose from within the quarkusrobotshop-support directory:

```shell
docker compose up
```

### Environment Variables

This services uses the following environment variables, all of which are set in development mode:  

NOTE: _Quarkus has a development mode that automatically listens for a debugger, watches for code changes and reloads your application immediately, and allows you to set application properties specifically for development.  You can learn more in Quarkus' getting started guide: https://quarkus.io/get-started_

* KAFKA_BOOTSTRAP_SERVERS
* STREAM_URL
* CORS_ORIGINS

If you wish to override these you can set them with the following (on Linux or a Mac):

```shell script
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092 STREAM_URL=http://localhost:8080/dashboard/stream CORS_ORIGINS=http://localhost:8080 STORE_ID=ATLANTA
```

### Starting the app

To start the app in Quarkus dev mode with:

```shell script
./mvnw clean compile quarkus:dev
```

### Attaching a debugger

By default Quarkus listens on port 5005 for a debugger.  You can change this by appending the flag, "-Ddebug<<PORT NUMBER>>" as in the below examples.  The parameter is optional, of course:

```shell script
./mvnw clean compile quarkus:dev -Ddebug=5006
```

### pgAdmin

The docker-compose file starts an instance of pgAdmin4.  You can login with:
* quarkus.cafe@redhat.com/redhat-20

You will need to create a connection to the Crunchy PostgreSQL database.  Use the following values:
* General 
** Name: pg10
* Connection
** Host: crunchy
** Port: 5432
** Maintenance database: postgres
** Username: postgres
** Password: redhat-20

The settings are not currently persisted across restarts so they will have to be recreated each time "docker-compose up" is run

## Containerizing the application (OPTIONAL)
  
Quarkus applications contain all the files needed to containerize the application yourself (you don't have to do this to run the app):

```shell
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <<DOCKER_HUB_ID>>/quarkusrobotshop-web .
export KAFKA_BOOTSTRAP_URLS=localhost:9092 STREAM_URL=http://localhost:8080/dashboard/stream CORS_ORIGINS=http://localhost:8080
docker run -i --network="host" -e KAFKA_BOOTSTRAP_URLS=${KAFKA_BOOTSTRAP_URLS} -e STREAM_URL=${STREAM_URL} -e CORS_ORIGINS=${CORS_ORIGINS} <<DOCKER_HUB_ID>>/quarkusrobotshop-counter:latest
docker images -a | grep web
docker tag <<RESULT>> <<DOCKER_HUB_ID>>/quarkusrobotshop-web:<<VERSION>>
```

### Containerizing the application in Native Mode (OPTIONAL)

Quarkus can also compile your Java application into a native binary (you don't have to do this to run the app):
  
 ```shell
export KAFKA_BOOTSTRAP_URLS=localhost:9092 STREAM_URL=http://localhost:8080/dashboard/stream CORS_ORIGINS=http://localhost:8080
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <<DOCKER_HUB_ID>>/quarkusrobotshop-web .
docker run -i --network="host" -e STREAM_URL=${STREAM_URL} -e CORS_ORIGINS=${CORS_ORIGINS} -e KAFKA_BOOTSTRAP_URLS=${KAFKA_BOOTSTRAP_URLS} <<DOCKER_ID>>/quarkusrobotshop-web:latest
docker images -a | grep web
docker tag <<RESULT>> <<DOCKER_HUB_ID>>/quarkus-cafe-web:<<VERSION>>
```

