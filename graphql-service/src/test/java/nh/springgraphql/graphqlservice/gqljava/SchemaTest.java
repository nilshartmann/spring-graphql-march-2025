package nh.springgraphql.graphqlservice.gqljava;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SchemaTest {

  private final static String SCHEMA_LOCATION = "/graphql/schema.graphqls";

  private GraphQLSchemaAssertions.GraphQLSchemaAssertion assertGraphQLSchema() {
    return GraphQLSchemaAssertions.assertGraphQLSchema(SCHEMA_LOCATION);
  }

  @Test
  void commentTypeIsCorrect() {
    assertGraphQLSchema()
      .withType("Comment")
          .withField("id").ofType("ID!")
          .and().withField("text").ofType("String!")
          .and().withField("rating").ofType("Int!")
    ;
  }

  @Test
  void storyTypeIsCorrect() {
    assertGraphQLSchema()
      .withType("Story")
        .withField("id").ofType("ID!")
        .and().withField("title").ofType("String!")
        .and().withField("body").ofType("String!")
        .and().withField("excerpt").ofType("String!")
        .and().withField("comments").ofType("[Comment!]!")
        .and().withField("state").ofType("PublicationState!")
    ;
  }

  @Test
  void publicationStateTypeIsCorrect() {
    assertGraphQLSchema()
        .withEnumType("PublicationState")
        .withEnumValue("draft")
        .withEnumValue("in_review")
        .withEnumValue("published")
    ;
  }


  @Test
  void queryTypeIsCorrect() {
    assertGraphQLSchema()
      .withType("Query")
        .withField("story").ofType("Story")
          .withDescription()
          .withArgument("storyId").ofType("ID!")
        .and()
      .and()
        .withField("stories").ofType("[Story!]!")
      ;
  }

}
