package nh.springgraphql.graphqlservice.graphql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureGraphQlTester
public class MutationControllerTest {

    @Autowired
    HttpGraphQlTester graphQlTester;

    @Test
    void test_mutation() {
        graphQlTester
//            .mutate().
//             header("Authorization", "Bearer 1234567")
//            .build()
            .document(
                // language=GraphQL
                """
              mutation($newComment: CreateCommentInput!) {
            createComment(input: $newComment) {
              id
            } }
            """)
            .variable("newComment", Map.of(
                "storyId", "1",
                "text", "Hallo Welt",
                "rating", 23
            ))
            .execute()
            .path("createComment.id").hasValue();
    }

}
