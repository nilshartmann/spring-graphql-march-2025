package nh.springgraphql.graphqlservice.graphql;

import graphql.schema.DataFetchingEnvironment;
import nh.springgraphql.graphqlservice.domain.Comment;
import nh.springgraphql.graphqlservice.domain.Story;
import nh.springgraphql.graphqlservice.domain.StoryRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

// Annotated Controller
@Controller
public class QueryController {

    private final StoryRepository storyRepository;

    public QueryController(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    // Resolver Function
    // Mapping Functions
    // Handler Functions
    //  DATA FETCHERS
    @QueryMapping
    List<Story> stories() {
        return storyRepository.findAllStories();
    }

//    @SchemaMapping(typeName = "Query")
    @QueryMapping
    Optional<Story> story(@Argument String storyId) {
        return storyRepository.findStory(storyId);
    }

    @SchemaMapping
    String excerpt(Story story, @Argument int maxLength) {
        return story.body().substring(0, maxLength);
    }

}
