# Passport Status API

Passport Status API is a Spring Boot API for retrieving Canadian passport
application statuses.

## Table of Contents

- [Introduction](#introduction)
- [Requirements](#requirements)
- [Recommended software](#recommended-software)
- [Installation](#installation)
- [Building](#building)
- [Configuration](#configuration)
  - [Application specific configration](#application-specific-configration)
  - [Embedded Artemis](#embedded-artemis)
  - [application-local.yaml](#application-localyaml)
- [Deploying to Kubernetes cluster](#deploying-to-kubernetes-cluster)
- [Builder container image](#builder-container-image)
  - [Listing existing tags](#listing-existing-tags)
- [Obtaining a bearer token to use for authenticated requests](#obtaining-a-bearer-token-to-use-for-authenticated-requests)
- [Maintainers](#maintainers)
- [Advanced help](#advanced-help)
- [Errata](#errata)

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

- To build/run the application:

  ``` sh
  mvn clean spring-boot:run --define spring-boot.run.arguments="--spring.profiles.active=embedded-mongo --application.database-initializer.enabled=true"
  ```

- To build a container image:

  ``` sh
  mvn clean spring-boot:build-image
  ```

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
  database-initializer:
    duplicate-statuses-number: 10     # number of duplicate (ie: same data) statuses to generate on startup
    generated-statuses-number: 10_000 # number of random status to generate on startup
    run-on-startup: false             # enable the data initializer on startup; WARNING -- THIS WILL DESTROY DATA
  endpoint:
    changelog:
      changelog-path: changelog.json  # classpath location of the changelog.json file generated during build
  etag-header-filter:
    write-weak-etag: true             # whether the ETag value written to the response should be weak, as per RFC 7232
  http-request-repository:
    page-size: 100                    # number of http request/response trace requests to return from /actuator/httptrace
  security:
    cors:
      allowed-headers:                # list of headers that a pre-flight request can list as allowed for use during an actual request
        - '*'
      allowed-methods:                # list of HTTP methods to allow, e.g. GET, POST, PUT, etc.
        - '*'
      allowed-origins:                # list of origins for which cross-origin requests are allowed
        - http://localhost
      exposed-headers:                # list of response headers other than simple headers (i.e. Cache-Control, Content-Language, Content-Type, Expires, Last-Modified, or Pragma) that an actual response might have and can be exposed
        - '*'
  request-logging-filter:
    enabled: false                    # whether to enable the request logging filter
    includeUrls:                      # list of URLs to include, in ant path style
      - /**
    excludeUrls:                      # list of URLs to exclude, in ant path style
      - /actuator/health/liveness
      - /actuator/health/readiness
    include-headers: true             # whether the request headers should be included in the log message
    include-payload: true             # whether the request payload (body) should be included in the log message
    include-query-string: true        # whether the query string should be included in the log message
    max-payload-length: 10240         # the maximum length (in bytes) of the payload body to be included in the log message
  swagger:
    application-name: Passport Status API
    contact-name: Digital technology solutions
    contact-url: https://github.com/dts-stn/passport-status-api/
    terms-of-service-url: https://www.canada.ca/en/transparency/terms.html
```

### Embedded Artemis

The application will run an embedded Artemis im-memory message queue by default.
To persist the message queue to disk, you can use the following configuration:

``` yaml
spring:
  artemis:
    embedded:
      persistent: true
      data-directory: target/artemis

```

### `application-local.yaml`

When running in an IDE (ie: VSCode, Eclipse), a developer can configure the
Passport Status API by creating an `application-local.yaml` file anywhere on the
classpath (ex: `src/main/resources`). This configuration file **will be ignored
by Git** so you can put all kinds of secrets in there!! 🍞

## Builder container image

To build the application in TeamCity, a custom builder image must be used that
provides Maven and Java 17. To build and push this container image:

``` sh
az login
az acr login --name dtsdevcontainers --subscription mts
docker build --tag dtsdevcontainers.azurecr.io/passport-status-api-builder:latest - < docker/Dockerfile-MavenBuild
docker tag dtsdevcontainers.azurecr.io/passport-status-api-builder:latest dtsdevcontainers.azurecr.io/passport-status-api-builder:v{version}-maven3.8-java17
docker push dtsdevcontainers.azurecr.io/passport-status-api-builder:latest
docker push dtsdevcontainers.azurecr.io/passport-status-api-builder:v{version}-maven3.8-java17
```

### Listing existing tags

To get a list of existing tags so you can correctly specify the version above:

``` sh
az login
az acr login --name dtsdevcontainers --subscription mts
az acr repository show-tags --name dtsdevcontainers --subscription mts --repository passport-status-api-builder
```

## Obtaining a bearer token to use for authenticated requests

``` sh
curl --silent --request POST \
  --url https://login.microsoftonline.com/9ed55846-8a81-4246-acd8-b1a01abfc0d1/oauth2/v2.0/token \
  --header 'Content-Type: multipart/form-data' \
  --form 'grant_type=client_credentials' \
  --form 'client_id={your-client-id}' \
  --form 'client_secret={your-client-secret}' \
  --form 'scope=api://passport-status.esdc-edsc.gc.ca/.default' | jq --raw-output '.access_token'
```

## Maintainers

If you have questions or need help running the Passport Status API, feel free to
contact:

- Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
- Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)

## Advanced help

This section exists only because some *README best practices* page said it should.

## Errata

- Coffee is not actually software.
