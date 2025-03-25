package nh.springgraphql.graphqlservice.graphql;

import nh.springgraphql.graphqlservice.domain.Story;
import nh.springgraphql.graphqlservice.domain.StoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

// Annotated Controller
@Controller
public class QueryController {

    private static final Logger log = LoggerFactory.getLogger(QueryController.class);

    private final StoryRepository storyRepository;

    public QueryController(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    // Resolver Function
    // Mapping Functions
    // Handler Functions
    //  DATA FETCHERS
    @QueryMapping
//    @PostFilter()
    List<Story> stories() {
        return storyRepository.findAllStories();
    }

    //    @SchemaMapping(typeName = "Query")
    @QueryMapping
    @PostAuthorize("returnObject.isEmpty() || !returnObject.get().state().isDraft() || hasRole('ROLE_ADMIN')")
    Optional<Story> story(@Argument String storyId) {
        // 
        return storyRepository.findStory(storyId);
    }

    // alternative:
    //   - CompletableFuture
    //   - Mono (Spring Reactor)
    //   - Flux (Sprint Reactor)

//    @SchemaMapping
//    Callable<String> excerpt(Story story, @Argument int maxLength) {
//        return () -> {
//            log.info("Excerpt for Story with id {}", story.id());
//            return storyRepository.generateExcerpt(story, maxLength);
//        };

    /// /        return story.body().substring(0, maxLength);
//    }
    @SchemaMapping
    String excerpt(Story story, @Argument int maxLength) {
        log.info("Excerpt for Story with id {}", story.id());
        return storyRepository.generateExcerpt(story, maxLength);
//        return story.body().substring(0, maxLength);
    }

}
