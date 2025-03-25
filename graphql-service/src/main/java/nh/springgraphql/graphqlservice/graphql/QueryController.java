package nh.springgraphql.graphqlservice.graphql;

import graphql.schema.DataFetchingEnvironment;
import nh.springgraphql.graphqlservice.domain.Comment;
import nh.springgraphql.graphqlservice.domain.Story;
import nh.springgraphql.graphqlservice.domain.StoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

// Annotated Controller
@Controller
public class QueryController {

    private static final Logger log = LoggerFactory.getLogger( QueryController.class );

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

    // alternative:
    //   - CompletableFuture
    //   - Mono (Spring Reactor)
    //   - Flux (Sprint Reactor)

    @SchemaMapping
    Callable<String> excerpt(Story story, @Argument int maxLength) {
        return () -> {
            log.info("Excerpt for Story with id {}", story.id());
            return storyRepository.generateExcerpt(story, maxLength);
        };
//        return story.body().substring(0, maxLength);
    }

}
