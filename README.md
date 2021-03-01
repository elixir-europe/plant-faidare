# FAIDARE: FAIR Data-finder for Agronomic Research
This application provides web services (based on the BrAPI standard) and a web interface with easy to use filters to facilitates the access to plant datasets from a federation of sources.

[[_TOC_]]

## How to contribute

Look at the [contribution guide](CONTRIBUTING.md).

## Install development environment

- Install `node` and `npm`

Installation via `nvm` is recommended for easier control of installed version:
https://github.com/creationix/nvm

```sh
nvm install 10.13.0
nvm use v10.13.0
```

- Install Angular CLI

```sh
npm install -g @angular/cli@7.0.6
```

- Install JS dependencies

```sh
cd frontend
npm install
```

- Install latest Java JDK8

See latest instructions for your operating system.

- (Optional) Install `docker` and `docker-compose`

If you want to run an Elasticsearch and Kibana instance on your machine.
You can use your favorite package manager for that


## Run backend development server

First make sure you have access to an Elasticsearch HTTP API server on `http://127.0.0.1:9200` (either via ssh tunneling or by running a local server).

If you want to run an Elasticsearch server on your development machine you can use the `docker`/`docker-compose` configuration like so:

```sh
docker-compose up
```

> This will launch an Elasticsearch server (with port forwarding `9200`) and a Kibana server (with port forwarding `5601`)

> **Warning**: This repository does not automatically index data into Elasticsearch, you need to prepare your indices beforehand.


If you just need access to API (to run the `ng serve` on top of it), you can run:

```sh
./gradlew bootRun
```

Otherwise, for the complete server (backend APIs + frontend interface), you can run:

```sh
./gradlew assemble && java -jar backend/build/libs/faidare.jar
```

The server should then be accessible at http://localhost:8380/faidare-dev

## Run frontend development server

The frontend requests are redirected to the local backend API server (see instructions above to launch) via the
Angular proxy.

You can run the development server with the following command:

```sh
cd frontend
ng serve
```

## GitLab CI

When creating merge requests on the ForgeMIA GitLab, the GitLab CI will 
automatically run the tests of the project (no need to do anything).

If you want to run the GitLab CI locally, you have to follow this steps:

1. [Install gitlab-runner](https://docs.gitlab.com/runner/install/)
2. Run the following command (with the correct GnpIS security token):

```sh
gitlab-runner exec docker test 
```


## Spring Cloud config

On bootstrap, the application will try to connect to a remote Spring Cloud config server
to fetch its configuration.
The details of this remote server are filled in the `bootstrap.yml` file.
By default, it tries to connect to the remote server on http://localhost:8888
but it can of course be changed, or even configured via the `SPRING_CONFIG_URI` environment variable.

It will try to fetch the configuration for the application name `faidare`, and the default profile.
If such a configuration is not found, it will then fallback to the local `application.yml` properties.
To avoid running the Spring Cloud config server every time when developing the application,
all the properties are still available in `application.yml` even if they are configured on the remote Spring Cloud server as well.

The configuration is currently only read on startup,
meaning the application has to be rebooted if the configuration is changed on the Spring Cloud server.
For a dynamic reload without restarting the application,
see http://cloud.spring.io/spring-cloud-static/Finchley.SR1/single/spring-cloud.html#refresh-scope
to check what has to be changed.

If you want to use a Spring Cloud configuration server, please refer to
https://spring.io/guides/gs/centralized-configuration/

