# The "Remote Service"

* The `remote-service` module contains a standalone Spring Boot application that provides the "Publisher Service" (HTTP API) and the "Media GraphQL Service" (GraphQL API).
* These two services serve as examples of external APIs that we want to consume in our own application.
* In "real life," they would not be part of our workspace, and we would not have access to their source code.
  * They would likely be two separate services

## Starting the RemoteServiceApplication

* We will need the Remote-Service later in the workshop.
  - I will let you know when you need to start it.

* **To start:**
  * Run the class `nh.springgraphql.remoteservice.RemoteServiceApplication`
  * The application will run on port `8090`.
  * If this port is already in use, you can adjust it:
    * In the file `remote-service/src/main/resources/application.properties`, set the `server.port` property to the desired port
    * In the GraphQL application, adjust the URL in `graphql-service/src/main/resources/application.properties` with the `publisher.service.base-url` property
  * Once the service is running, you can **test** it by querying a publisher, for example, by calling http://localhost:8090/api/publishers/1 in your browser (or using curl, wget, etc.)
