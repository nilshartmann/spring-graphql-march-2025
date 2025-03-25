package nh.springgraphql.graphqlservice.graphql;

import nh.springgraphql.graphqlservice.domain.Story;
import nh.springgraphql.graphqlservice.domain.StoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@GraphQlTest(QueryController.class)
@Import(StoryRepository.class)
public class QueryControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @Test
    void make_sure_stories_works() {
        graphQlTester
            .document(
                // language=GraphQL
                """
                    query { stories {
                        id title 
                        comments {
                            id                     
                        }                    
                    }}
                    """
            )
            .execute()
            .path("stories[0].id").entity(String.class).isEqualTo("1")
            .path("stories[*]").entityList(Object.class).hasSize(8)
            .path("stories.length()").entity(Integer.class).isEqualTo(8)
            .path("stories[1]").entity(Story.class).satisfies(s -> {
                assertAll(
                    () -> assertThat(s.id()).isEqualTo("2"),
                    () -> assertThat(s.title()).isEqualTo("Global Climate Update")
                );
            })
            .path("stories[1].comments", s -> {
                s.path("[0].id").entity(String.class).isEqualTo("3");
                s.path("[1].id").entity(String.class).isEqualTo("4");
                s.path("[2].id").entity(String.class).isEqualTo("5");

            })
            .path("stories[1].comments[0].id").entity(String.class).isEqualTo("3")
            .path("stories[1].comments[1].id").entity(String.class).isEqualTo("4")
            .path("stories[1].comments[2].id").entity(String.class).isEqualTo("5");

//            .path("stories[1].id").entity(String.class).isEqualTo("2")
//            .path("stories[1].title").entity(String.class).isEqualTo("Global Climate Update")
    }

    @Test
    void make_sure_story_by_id_works() {
//        graphQlTester.documentName("get-story-test")

        graphQlTester.document(
                // language=GraphQL
                """
                    query ($id: ID!){
                        story(storyId: $id) { id } 
                    } 
                    """)
            .variable("id", "1")
            .execute()
            // https://github.com/json-path/JsonPath
            .path("story.id").entity(String.class).isEqualTo("1");

    }
}
