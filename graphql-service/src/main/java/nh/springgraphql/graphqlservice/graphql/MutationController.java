package nh.springgraphql.graphqlservice.graphql;

import jakarta.annotation.security.RolesAllowed;
import nh.springgraphql.graphqlservice.domain.Comment;
import nh.springgraphql.graphqlservice.domain.StoryRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MutationController {

    private final StoryRepository storyRepository;

    public MutationController(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    record CreateCommentInput(
        String storyId,
        String text,
        int rating
    ) {}

    @MutationMapping
    Comment createComment(@Argument CreateCommentInput input) {
        return storyRepository.addComment(
            input.storyId(),
            input.text(),
            input.rating()
        );
    }


}
