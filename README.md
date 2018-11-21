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


TODO: lint on test/dev 
TODO: add gitlab ci (ng test --browsers FirefoxHeadless,ChromeHeadlessNoSandbox)

