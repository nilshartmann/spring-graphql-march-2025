# Exercise: QueryMapping Functions with Spring for GraphQL

* **Migrate our DataFetchers to Spring...**
    * ...more specifically: their logic
    * In Spring, use an Annotated Controller with handler methods.
* **Create a `@Controller` class `QueryController`**
    * Use a new package: `nh.springgraphql.graphqlservice.graphql`
    * From now on, we will always work in the `.graphql` package
* You can use Spring Dependency Injection to obtain the `StoryRepository`
* In it, you will need two `QueryMapping` methods and one `SchemaMapping` method
* New requirement:
    * Extend the schema so that the `Story.excerpt` field receives an `Int` argument `maxLength`.
    * This argument is optional and should have a default value of `20` if not set
    * Use the `maxLength` value from a query to return only the first `maxLength` characters of the `body`
        * (instead of the previously hardcoded 3 characters)
* To check your implementation, you can start our `graphql-service` and execute queries in GraphiQL
    * Start the class `nh.springgraphql.graphqlservice.GraphqlServiceApplication`
    * GraphiQL runs on `http://localhost:8080/`
        * If needed, adjust the port in the `application.properties` file or check it (Property `server.port`)

## Documentation

* https://docs.spring.io/spring-graphql/reference/controllers.html
* https://docs.spring.io/spring-graphql/reference/controllers.html#controllers.schema-mapping.argument
* https://docs.spring.io/spring-graphql/reference/graphiql.html
