# Our Application Needs to Be Tested 🫣

* **Write tests to ensure the functionality of the API**

* Familiarize yourself with the testing possibilities of Spring GraphQL
* You can test, for example:
    * Reading a single story
    * Reading a story by its ID (what happens if the queried ID is not present in the DB?)
    * Creating a comment
        * Can you use the `AddCommentInput` as a variable in the test?

## Types of Tests

* You can test either a "slice" or the entire application
* With a **slice test**, the test runner only starts selected parts of your application
    * Which parts those are depends on the annotation used, e.g., all repositories or all controllers
    * If there are beans that are not started, you will need to mock them or explicitly list them to be started
    * To write a "GraphQL slice" test, annotate your test class with `@GraphQlTest`. This will start all GraphQL-related "infrastructure" beans and all `@Controller` beans
        * You can limit the selection of `@Controller` beans by passing the class names to `@GraphQlTest`
        * This can be useful so that you don’t have to mock or add too many beans that your controllers need. It also makes the startup faster.
        * ```java
      // Start only the QueryController + all Spring-GraphQL beans
      @GraphQlTest(controllers = QueryController.class)
      // Also start the StoryRepository
      @Import(StoryRepository.class)
      class QueryControllerTest {
      // ...
      }
      ```
* If you want to start the entire application, you can use the `@SpringBootTest` annotation
    * This will create and start the entire application context
    * By default, no web server is started, and communication between the test and the application happens through a mocked layer by Spring. If you want to start the application fully with a web server, you need to use `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)`
    * To get the `GraphqlTester` in your test, you must annotate your test class with `@AutoConfigureGraphQlTester`

## The GraphqlTester

* The `GraphQlTester` class allows you to execute GraphQL queries against your API in the test and verify the result.
    * There are different implementations of this interface.
    * **Regardless of the test type, testing with the `GraphQlTester` works the same**
    * You can execute a working test in different ways (as a slice test or a SpringBootTest)
* With the `GraphqlTester`, you configure your request by providing the query document and possibly setting other parameters like variables.
* Then, you execute the request with `execute()`.
    * If the query is successful, the method returns a `Response` object that you can use to verify the result.
    * Primarily, you use `path(String)` to select which fields to verify from the result using a JSON path expression. The paths start with the `data` node of the response (exclusive).
    * Example response:
    * ```json
  { "data": { "story": { "id": "1" } } }
    ```
    * With `path("story.id")`, you can select and verify the ID of the story
    * To verify a value, you need to convert it to a Java type using `entity` and then compare it, e.g.:
        * `path("story.id").entity(String.class).isEqualTo("1");`
    * If you expect errors in the query (`errors` node), you can check these with `errors`

## GraphQL Queries in Tests
* You can pass the tests to the `GraphqlTester` either by `document` or `documentName`
    * With `document`, you write the query directly as a string
    * With `documentName`, you specify the name of a "document" that the GraphQLTester will search for in the filesystem.
        * By default, the associated file must be in the directory `src/test/resources/graphql-test/` and have the extension `.graphql` or `.gql`. So, for a file named `get-story.graphql`, you would pass "get-story" as the `documentName`.

## Documentation

* JsonPath: https://github.com/json-path/JsonPath?tab=readme-ov-file

* `SpringBootTest`: https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html#testing.spring-boot-applications.with-running-server
* https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html#testing.spring-boot-applications.with-mock-environment
* https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html#testing.spring-boot-applications.spring-graphql-tests
* Testing GraphQL Requests with `GraphqlTester`: https://docs.spring.io/spring-graphql/reference/testing.html
    * Especially: https://docs.spring.io/spring-graphql/reference/testing.html#testing.requests
* `@GraphQlTest`: https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html#testing.spring-boot-applications.spring-graphql-tests
    * You’ll find a list of beans included in the application context with `@GraphQlTest`

## Tip: Use the GraphQL Plugin in IntelliJ
* Install: https://plugins.jetbrains.com/plugin/8097-graphql
* With this, you get code completion and syntax highlighting for queries written in the editor
* If you write queries directly in your (Java) code, you can set the language to "GraphQL" in IntelliJ with a comment `language=GraphQL`. This enables "inline" code completion, etc. It's best to use a Java text block for this:
    * ```java
      class MyTest {
      
        
        // language=GraphQL
        String myQuery = """
           { stories { id } }
        """;
      
      }
      ```
    * If you want to store your queries in files in the (test) directory, they should end with `.graphql`. IntelliJ will automatically treat them as GraphQL files.
