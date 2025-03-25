package nh.springgraphql.graphqlservice.graphql;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import jakarta.annotation.security.RolesAllowed;
import nh.springgraphql.graphqlservice.domain.Comment;
import nh.springgraphql.graphqlservice.domain.ResourceNotFoundException;
import nh.springgraphql.graphqlservice.domain.StoryRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class MutationController {

    private final StoryRepository storyRepository;

    public MutationController(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    @GraphQlExceptionHandler
    GraphQLError handleResourceNotFoundException(
        ResourceNotFoundException resourceNotFoundException,
        DataFetchingEnvironment env) {

        return GraphQLError.newError()
            .message("Resource not found!")
            .errorType(ErrorType.OperationNotSupported)
            .location(env.getField().getSourceLocation())
            .build();
    }

    record CreateCommentInput(
        String storyId,
        String text,
        int rating
    ) {
    }

    @MutationMapping
    Comment createComment(@Argument CreateCommentInput input) {
        return storyRepository.addComment(
            input.storyId(),
            input.text(),
            input.rating()
        );
    }


    interface AddCommentPayload {
    }

    record AddCommentInput(
        String storyId,
        String text,
        int rating
    ) {
    }

    record AddCommentSuccess(Comment comment) implements AddCommentPayload {
    }

    record AddCommentError(String errorMsg) implements AddCommentPayload {
    }

    @RolesAllowed("USER")
    @MutationMapping
    AddCommentPayload addComment(@Argument CreateCommentInput input) {
        try {
            return new AddCommentSuccess(storyRepository.addComment(
                input.storyId(),
                input.text(),
                input.rating()
            ));
        } catch (Exception ex) {
            return new AddCommentError(ex.getMessage());
        }
    }


}
