package nh.springgraphql.graphqlservice.gqljava;

import graphql.ExecutionResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryTest {

  GraphQLProvider graphQLProvider = new GraphQLProvider();

  @Test
  void storiesQueryWorks() {

    ExecutionResult executionResult = graphQLProvider.
      getGraphQL()
      .execute("query { stories { id title excerpt } }");

    assertThat(executionResult.getErrors())
      .isEmpty();

    Object data = executionResult.getData();
    assertThat(data).isInstanceOf(Map.class);
    Map<?, ?> map = (Map<?, ?>) data;
    assertThat(map.containsKey("stories")).isTrue();
    List<?> stories = (List<?>)map.get("stories");
    assertThat(stories).hasSize(8);

    assertStory((Map<?, ?>)stories.get(0), "1", "The");
    assertStory((Map<?, ?>)stories.get(1), "2", "As ");
    assertStory((Map<?, ?>)stories.get(2), "3", "Wit");
  }

  @Test
  void storyByIdQueryWorks() {

    ExecutionResult executionResult = graphQLProvider.
      getGraphQL()
      .execute("query { story(storyId: 2) { id title excerpt } }");

    assertThat(executionResult.getErrors())
      .isEmpty();

    Object data = executionResult.getData();
    assertThat(data).isInstanceOf(Map.class);
    Map<?, ?> map = (Map<?, ?>) data;
    assertThat(map).hasSize(1);
    assertThat(map.containsKey("story")).isTrue();
    Map<?, ?> story = (Map<?, ?>)map.get("story");


    assertStory(story, "2", "As ");
  }

  private void assertStory(Map<?, ?> story, String id, String excerpt) {
    assertThat(story.get("id")).isEqualTo(id);
    assertThat(story.get("excerpt")).isEqualTo(excerpt);
  }

}
