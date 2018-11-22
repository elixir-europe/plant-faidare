# GnpIS Plant Data Search

GnpIS Plant Data Search *aka* legacy Unified Interface.

## How to contribute

Look at the [contribution guide](CONTRIBUTING.md).

## Install development environment

### Install node and npm
install nvm

go to :
https://github.com/creationix/nvm

```sh
nvm install 10.13.0
nvm use v10.13.0
```

###Install Angular CLI

```sh
npm install -g @angular/cli@7.0.6
```

### Install JS dependencies

```sh
cd frontend
npm install
```

## Run frontend development server

The frontend requests are redirected to the production server API of gnpis core 
(https://urgi.versailles.inra.fr/gnpis-core-srv/swagger-ui.html#) via the 
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
