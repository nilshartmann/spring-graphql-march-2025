# Step 1: Create Schema

* Create the foundation for our schema in the file `src/main/resources/graphql/schema.graphqls`:
* There should be a `Story` type with the following fields (all non-nullable):
    * `id`, `title`, `body`, `excerpt`, `state`, and `comments`
    * `title`, `body`, and `excerpt` contain text
    * `id` is an identifier.
    * `state` should be an enum-type `PublicationState` with three values: `draft`, `in_review`, `published`
    * `comments` is a list of `Comment` objects
* The `Comment` type has an `id`, a `text`, and a `rating` field
    * `rating` is a number
* There should be two `Query` fields:
    * `story` with an argument `storyId`. This field should return a `Story` object
    * Add some documentation to the `story` field (you can use Markdown syntax for this)
    * `stories` returns a list of `Story` objects
* You can run the test class `nh.springgraphql.graphqlservice.gqljava.SchemaTest` to check if your schema is correct
* All fields except for `Query.stories` are non-nullable!
    * Is this really a good idea?
    * What could be a drawback (even if the fields **always** have a value from a business perspective)?

## Documentation

* https://graphql.org/learn/schema/
* https://www.graphql-java.com/documentation/schema#creating-a-schema-using-the-sdl
