# Exercise: DataFetcher with graphql-java

* Implement all necessary DataFetchers for our schema
* As a starting point, you will find prepared classes for this in the package `nh.springgraphql.graphqlservice.gqljava`
* In most cases, you need at least the DataFetchers for fields in the root types `Query`, `Mutation`, and `Subscription`
* As is the case here: we need two DataFetchers for `Query.stories` and `Query.story`
    * In the DataFetcher for `Query.story`, you need to retrieve the argument `storyId` via the `DataFetchingEnvironment`
* You can instantiate and use the `StoryRepository` in the DataFetchers to fetch stories
* Additionally, we need a DataFetcher for `Story.excerpt`. Why is this necessary?
    * In the DataFetcher, you need to retrieve the `Source` object, which must be an instance of `Story`
* Once you've implemented the three DataFetchers, you need to register them in the `RuntimeWiring` in the class `nh.springgraphql.graphqlservice.gqljava.GraphQLProvider`.
    * You will find further information in the class
* If you have implemented the DataFetchers correctly, the test `nh.springgraphql.graphqlservice.gqljava.QueryTest` should pass without errors

## Documentation

* https://www.graphql-java.com/documentation/data-fetching
* https://www.graphql-java.com/documentation/schema#creating-a-schema-using-the-sdl
* https://javadoc.io/doc/com.graphql-java/graphql-java/22.0/graphql/schema/DataFetchingEnvironment.html
