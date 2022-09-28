# Passport Status API

Passport Status API is a (for now) bare-bones API for retrieving Canadian
passport application statuses.

## Introduction

The Passport Status API fetches and returns Canadian passport application
statuses by searching a database using a set of shared datapoints. Software
services can query the database by providing various pieces of data such as
`firstName`, `lastName`, `dateOfBirth`, and other data that is volunteered by
end users.

## Requirements

The Passport Status API requires the following toolchain to build and run:

- Java 17
- Maven 3.x
- (optional) Docker, podman, Cloud Native Buildpacks, or compatible container runtime

## Recommended software

The Passport Status API development team recommends the following:

- Visual Studio Code w/ Java extension, or Eclipse w/ JBoss APT plugin
- Docker CE, Podman, or Cloud Native Buildpacks
- Coffee<sup>[*](#errata)</sup>; dark roast during code reviews, light roast for development

## Installation

The Passport Status API requires no installation process to run. 🙏

## Building

- To build/run the application: `mvn clean spring-boot:run`
- To build a container image: `mvn clean spring-boot:build-image`

## Configuration

The Passport Status API is built using Spring Boot 2.7.x and inherits it's
[externalized configration](https://docs.spring.io/spring-boot/docs/2.7.4/reference/htmlsingle/#features.external-config)
mechanisms.

### Application specific configration

The Passport Status API comes preconfigured with some default values that can be
tweaked as needed via environment variables, or by placing an
`application-local.yaml` file on the classpath (ie: in `src/main/resources`).

Below you will find the current set of configuration keys, as well as their
default values.

``` yaml
application:
  dev:
    data-initializer:
      duplicate-statuses-number: 10     # number of duplicate (ie: same data) statuses to generate on startup
      generated-statuses-number: 10_000 # number of random status to generate on startup
  http-trace-repository:
    page-size: 100                      # number of http request/response trace requests to return from /actuator/httptrace
```

### `application-local.yaml`

When running in an IDE (ie: VSCode, Eclipse), a developer can configure the
Passport Status API by creating an `application-local.yaml` file anywhere on the
classpath (ex: `src/main/resources`). This configuration file **will be ignored
by Git** so you can put all kinds of secrets in there!! 🍞

## Maintainers

If you have questions or need help running the Passport Status API, feel free to contact:

- Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca) — Lead developer  
  - Flashlight enthusiast, lawn care expert, headbanger
- Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca) — `README.md` author;
  - Java apologist, Kubernetes `${latestBuzzword}`, headbanger

## Advanced help

This section exists only because some *README best practices* page said it should.

## Errata

- Coffee is not actually software.
