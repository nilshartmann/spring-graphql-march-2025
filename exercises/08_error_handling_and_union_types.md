# Build an (Improved) Mutation to Add Comments

* Extend the schema:
    * The `createComment` mutation should now be marked as `Deprecated`
    * Instead, create a new `addComment` mutation
    * It should have the same input type as `createComment`.
    * Unlike `createComment`, the new `addComment` mutation should have improved error handling:
        * The return type `AddCommentPayload` should be a union type that can either be `AddCommentSuccess` or `AddCommentError`
        * `AddCommentSuccess` should have a `newComment` field containing the newly created comment
        * `AddCommentFailed` should have a field for an error message (`errorMsg`)
* Implement the mutation handler method for this in the `MutationController` class
    * If an exception is thrown when calling `StoryRepository.createComment`, return the error type
    * You can test the error case by passing a non-existent story ID when creating a story (e.g., `1000`)
* Execute the mutation in GraphiQL and query the return values

## Documentation
* https://graphql.org/learn/schema/#union-types
* https://docs.spring.io/spring-graphql/reference/controllers.html#controllers.exception-handler
* `GraphQLError`: https://javadoc.io/static/com.graphql-java/graphql-java/22.0/graphql/GraphQLError.html
* `@GraphQlExceptionHandler`: https://docs.spring.io/spring-graphql/docs/current/api/org/springframework/graphql/data/method/annotation/GraphQlExceptionHandler.html
* `@ControllerAdvice`: https://docs.spring.io/spring-framework/reference/web/webflux/controller/ann-advice.html
