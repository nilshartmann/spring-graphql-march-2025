# Build a Mutation to Create Comments

* Extend the schema:
    * We need a `CreateCommentInput` input type that contains the necessary information to create a comment:
        * `storyId`, `text`, `rating`
    * We need a `createComment` mutation that uses the input type and returns the new comment.
* Write a mutation handler method for this
    * **Create a new controller class `MutationController`**
    * Here, you will also use Spring Dependency Injection to get the `StoryRepository`
    * You can use the `createComment` method on the `StoryRepository` to add the comment to our "database"
* You can test your mutation in GraphiQL
    * Can you execute the mutation in GraphiQL and pass the input object as a **variable**?

## Documentation
* https://graphql.org/learn/queries/#mutations
* https://graphql.org/learn/schema/#input-object-types
* https://graphql.org/learn/queries/#variables
