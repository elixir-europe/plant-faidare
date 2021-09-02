# FAIDARE: FAIR Data-finder for Agronomic Research
This application provides web services (based on the BrAPI standard) and a web interface with easy to use filters to facilitates the access to plant datasets from a federation of sources.

[[_TOC_]]

## How to contribute

Look at the [contribution guide](CONTRIBUTING.md).

## Install development environment

- Install `node` and `yarn`

Installation via `nvm` is recommended for easier control of installed version:
https://github.com/creationix/nvm

```sh
nvm install 10.13.0
nvm use v10.13.0
```

- Install JS dependencies

```sh
cd web
yarn
```

- Install latest Java JDK8

See latest instructions for your operating system.

- (Optional) Install `docker`

If you want to run an Elasticsearch and Kibana instance on your machine.
You can use your favorite package manager for that


## Run backend development server

First make sure you have access to an Elasticsearch HTTP API server on `http://127.0.0.1:9200` (either via ssh tunneling or by running a local server).

If you want to run an Elasticsearch server on your development machine you can use the `docker`/`docker-compose` configuration like so:

```sh
docker compose up
```

> This will launch an Elasticsearch server (with port forwarding `9200`) and a Kibana server (with port forwarding `5601`)

> **Warning**: This repository does not automatically index data into Elasticsearch, you need to prepare your indices beforehand.


If you just need access to API, you can run:

```sh
./gradlew bootRun
```

If you are developing and need to work on the `web` assets (scripts, styles, etc),
you'll need to run the application with the `dev` profile:

```sh
./gradlew bootRun --args='--spring.profiles.active=dev'
```

Otherwise, for the complete server (backend APIs + web interface), you can run:

```sh
./gradlew assemble && java -jar backend/build/libs/faidare.jar
```

The server should then be accessible at http://localhost:8380/faidare-dev

## Build the JS/CSS assets for the Web module

The `web` directory contains the scripts and styles used by the thymeleaf templates
when a Germplasm, Study or Site page is rendered.

The build process for these assets can be run with the following command:

```sh
cd web
yarn watch
```

`yarn watch` automatically picks up the changes in any files,
and rebuild the resulting assets (thanks to Webpack).
Make sure the backend is running with the `dev` profile if you do so (see above),
otherwise the changes won't be shown in the browser.

`yarn watch:prod` is also available to use production settings,
while `yarn build` and `yarn build:prod` do the same but without watching the changes. 

## Harvest

Before all, take care to get data locally before running any indexing script.

### TL;DR

Data indexing to your local Elasticsearch is done using the following command (take care to change the path to local data). Note that your local Elasticsearch instance should be already runing using `docker-compose up`:

```sh
docker run -t --volume /path/to/local/data:/opt/data/ --network=container:elasticsearch-faidare registry.forgemia.inra.fr/urgi-is/docker-rare/faidare-loader:latest -jsonDir /opt/data/ --help
```
Remove the `--help` parameter to run the loading with default params.

### Portability

#### Docker

[TL;DR](#TLDR) section above expects to have an available docker image on the forgemia docker registry. The Gitlab CI rebuil it when needed, but you can update or push such an image using the following commands:

```sh
# build the image
docker build -t registry.forgemia.inra.fr/urgi-is/docker-rare/faidare-loader:latest .

# Login before pushing the image
docker login registry.forgemia.inra.fr/urgi-is/docker-rare -u <your ForgeMIA username>

# push the built image
docker push registry.forgemia.inra.fr/urgi-is/docker-rare/faidare-loader:latest
```

That should ease the indexing of data without having to craft a dedicated environment.

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

