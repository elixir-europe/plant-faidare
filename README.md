# FAIDARE: FAIR Data-finder for Agronomic Research
FAIDARE is a application that provides web services (based on the BrAPI standard) and a user-friendly web interface to access plant datasets from a federation of sources.

[[_TOC_]]

## Overview
- Purpose: Facilitate access to federated plant datasets for agronomic research.
- Key Features:

    - BrAPI-compliant web services.
    - Intuitive filters for dataset exploration.
    - Support for Elasticsearch and Kibana.

## How to Contribute

Look at the [contribution guide](CONTRIBUTING.md).

## Data loading

For loading data in the FAIDARE Elasticsearch, see [HOW-TO-LOAD-DATA.md](HOW-TO-LOAD-DATA.md). 

## Setting Up the Development Environment

### Prerequisites

1. Node.js and Pnpm
Install Node.js and Pnpm. Using nvm or volta is advised for version control: https://github.com/creationix/nvm.
Check out the versions in the `package.json` file.

2. Java JDK17
Install the latest JDK17 version for your operating system.

3. Docker
Required to run Elasticsearch and Kibana locally. Ensure Docker and Docker Compose are installed.

### Installation Steps

1. Install JavaScript Dependencies
Navigate to the web directory and install dependencies:
```sh
cd web
pnpm install
```

2. Start Elasticsearch and Kibana
Launch using Docker Compose:
```sh
docker compose up
```

- Elasticsearch available at http://127.0.0.1:9200

- Kibana available at http://127.0.0.1:5601

    Note: Prepare your Elasticsearch indices before proceeding.

## Running the Backend Server

### Basic API Server

Run the backend server with:

```sh
./gradlew bootRun
```

### Development Server

If you are working on frontend assets, start the backend with the dev profile:

```sh
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Complete Backend + Web Interface

For the full application:

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
pnpm watch
```

`pnpm watch` automatically picks up the changes in any files,
and rebuild the resulting assets (thanks to Webpack).
Make sure the backend is running with the `dev` profile if you do so (see above),
otherwise the changes won't be shown in the browser.

`pnpm watch:prod` is also available to use production settings,
while `pnpm build` and `pnpm build:prod` do the same but without watching the changes. 

## GitLab CI

When creating merge requests on the ForgeMIA GitLab, the GitLab CI will 
automatically run the tests of the project (no need to do anything).

If you want to run the GitLab CI locally, you have to follow this steps:

### Important: 
The `gitlab-runner exec` command was fully removed in GitLab Runner 16.0 and is no longer available in version 17.0 (released May 2024). This command was deprecated in GitLab 15.7 (December 2022), and its removal was part of the breaking changes introduced in GitLab Runner 16.0.

For more information, see the official [deprecation notice](https://docs.gitlab.com/ee/update/deprecations.html?utm_source=chatgpt.com#the-gitlab-runner-exec-command-is-deprecated).

### Alternatives to gitlab-runner exec:
1.  Use the Validate option on GitLab
GitLab provides an integrated Pipeline Editor that allows you to validate and simulate the execution of your .gitlab-ci.yml file before actually running the pipeline.
To use this feature, go to CI/CD > Pipelines > Editor in your GitLab project. Here, you can paste your .gitlab-ci.yml file and click the Validate button to check the syntax and simulate its execution.

2.  Emulators
While gitlab-runner exec is deprecated, third-party tools and emulators can help simulate GitLab CI pipelines locally. These tools may be useful for testing and troubleshooting, though they may not replicate the GitLab CI environment exactly.


## Spring Cloud config

On bootstrap, the application will try to connect to a remote Spring Cloud config server
to fetch its configuration.
The details of this remote server are filled in the `application.yml` file.
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

