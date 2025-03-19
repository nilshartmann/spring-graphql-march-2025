# GraphQL Training Workspace

## Prerequisites

* You need **Java 17** at your computer
* You need an IDE (like IntelliJ)
* Your computer needs to download and install packages using **Gradle**
* You need to clone a **Git repository** from github.com

> **Note!**
>
> We will update the workspace during the workshop with `git pull` and we will also add some more gradle dependencies that you need to install.
>
> So please make sure, that your internet connection (including VPN, Firewall, Proxy, ...) also works during the workshop!


## The Workspace

- The repository is a multi-module Gradle project
- You can open the entire repository in your IDE
  - IntelliJ should correctly recognize the modules.
- There are three Gradle modules:
  - `root` (empty)
  - `graphql-service`
  - `remote-service`
  - We will only work in the **graphql-service**

## Starting the Application

- We will develop the Spring Boot application `graphql-service` step by step
- This module contains a few classes, particularly the "domain layer" is already completed. There are also some configuration files and a few classes as a starting point for our own code.

**To make sure everything works**, please:

1. Import the workspace into your IDE
	- Gradle should download and install all packages
2. To ensure everything is setup correctly, you can make a build from the command line:
    - Open a terminal in the root folder of this repository
    - Execute: `./gradlew clean bootJar -x test`
       - Note that the tests in the repository are failing because we do not have implemented the business logic yet, so we exclude them here
3. You can **start the application** in your IDE by running the class `nh.springgraphql.graphqlservice.GraphqlServiceApplication` in your IDE (it contains a `main` method)
	- The Spring Boot server with the GraphQL API will then listen on port `8080`
  	- You can adjust the port in the `application.properties` file if needed (Property `server.port`)
4. You can **start the "remote service"** in your IDE by running the class `nh.springgraphql.remoteservice.RemoteServiceApplication`
    - This spring boot service listens on port `8090`
    - If this port is already in use on your machine, you can adjust it:
      - In the file `remote-service/src/main/resources/application.properties`, set the `server.port` property to your desired port
      - In the GraphQL application (`graphql-service`), you will need to adjust the URL in `graphql-service/src/main/resources/application.properties` with the `publisher.service.base-url` property
5. When both services have been started correctly, you're ready for the workshop 🥳
    - You can now stop the services and start them again during our workshop.
    - If you have problems or question, please feel free to reach me via email.

**Good luck!!!**