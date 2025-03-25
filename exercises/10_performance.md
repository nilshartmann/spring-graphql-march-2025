# Exercise: Performance Tuning

* **Improve performance when accessing the "Publisher Remote Service"**

* Enable `SimplifiedTracingInstrumentation`
    * To do this, please add the following bean definition in `nh.springgraphql.graphqlservice.config.graphql.GraphQlConfig`
    * ```java
      @Bean
      SimplifiedTracingInstrumentation tracingInstrumentation() {
        return new SimplifiedTracingInstrumentation();
      }
      ```
    * Note: There is a built-in `TracingInstrumentation` in graphql-java that you can use as well
        * "My" `SimplifiedTracingInstrumentation` outputs slightly fewer details and provides the timing information in milliseconds (instead of nanoseconds).
        * I find this simpler for understanding and demonstrating, but of course, you can use the `TracingInstrumentation` from graphql-java, especially in a "real" application.
* Slow down the remote requests
    * You can do this by setting the constant `slowdownRequest` in `PublisherServiceClient` to a (long) value in milliseconds (e.g., 500), so that every request is artificially delayed by the remote service.
* Try different strategies to improve performance:
    * Use a `Callable` as the return type
      * You can configure the thread pool in your `application.properties` file
        * `spring.task.execution.pool.max-size=4`
        * `spring.task.execution.pool.core-size=4`
    * Refactor the schema-mapping method for `Story.publisher` using a `BatchLoader`

## Documentation
* TracingInstrumentation: https://www.graphql-java.com/documentation/instrumentation/#apollo-tracing-instrumentation
* Spring GraphQL automatically collects all `Instrumentation` beans and adds them to the RuntimeWiring: https://docs.spring.io/spring-graphql/reference/request-execution.html#execution.graphqlsource
* Batch Loading in Spring GraphQL: https://docs.spring.io/spring-graphql/reference/controllers.html#controllers.batch-mapping
* DataLoader in graphql-java: https://www.graphql-java.com/documentation/batching/
* Configuring TaskExecutor in Spring Boot: https://docs.spring.io/spring-boot/reference/features/task-execution-and-scheduling.html#features.task-execution-and-scheduling
* Async methods in Spring: https://spring.io/guides/gs/async-method
