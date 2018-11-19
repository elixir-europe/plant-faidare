# GnpIS Plant Data Search

GnpIS Plant Data Search *aka* legacy Unified Interface.

## How to contribute

Look at the [contribution guide](CONTRIBUTING.md).

## Install development environment

```sh
# Install Angular CLI
npm install -g @angular/cli@7.0.6

cd frontend

# Install JS dependencies
npm install
```

## Run frontend development server

The frontend requests are redirected to the production server API of gnpis core (https://urgi.versailles.inra.fr/gnpis-core-srv/swagger-ui.html#) via the Angular proxy.

You can run the development server with the following command:

```sh
cd frontend
ng serve
```