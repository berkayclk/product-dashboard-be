# PRODUCT DASHBOARD BE
Backend Server of the Product Dashboard Project

<p align="center">
    <a alt="Java">
        <img src="https://img.shields.io/badge/Java-v1.8-orange.svg" />
    </a>
    <a alt="Spring Boot">
        <img src="https://img.shields.io/badge/Spring%20Boot-v2.1.3-brightgreen.svg" />
    </a>
    <a alt="Dependencies">
        <img src="https://img.shields.io/badge/dependencies-up%20to%20date-brightgreen.svg" />
    </a>
    <a alt="License">
        <img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg" />
    </a>
</p>

This project is developed as [Spring Boot](http://projects.spring.io/spring-boot/) Web Application to manage products on a dashboard.   

Application is availablle on the [HEROKU (swagger-doc)](https://product-dashboard-be.herokuapp.com/swagger-ui.html)

 
## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.paydaybank.dashboard.ProductDashboardApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Running the application with DOCKER

Build Dockerfile with following command. This creates and deploy jar file to docker image.
```
    docker build -t dashboard .
```
Run docker image with following command.
```
    docker run -d -p 8080:8080 dashboard
```

And api is available on 8080 port.

## ENV 
Some variables should be set in application.properties
 ```application.properties
    security.jwt.uri : application login path (default: "/auth/login")
    security.jwt.header : header key that keeps token (default: "Authorization")
    security.jwt.prefix : prefix of the jwt token (default: "Bearer ")
    security.jwt.expiration : expiration time in seconds (default: 24*60*60)
    security.jwt.secret : jwt secret key to sign tokens (default: "JwtSecretKey")
    security.jwt.rolesKey : field key that keeps roles array in token body (default: "roles")
    security.cors.domains: ',' seperated cors domains string (default: http:/localhost:3000)   
```

## TO DO  
- Model documentation for swagger  

## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/codecentric/springboot-sample-app/blob/master/LICENSE) file.