package nh.springgraphql.graphqlservice.graphql;

import nh.springgraphql.graphqlservice.domain.CommentCreatedEvent;
import nh.springgraphql.graphqlservice.domain.CommentEventPublisher;
import nh.springgraphql.graphqlservice.domain.StoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class SubscriptionController {

    private final StoryRepository storyRepository;

    public SubscriptionController(StoryRepository storyRepository, CommentEventPublisher publisher) {
        this.storyRepository = storyRepository;
        this.publisher = publisher;
    }

    private final CommentEventPublisher publisher;
    private static final Logger log = LoggerFactory.getLogger(SubscriptionController.class);

    @SubscriptionMapping
    Flux<CommentCreatedEvent> onNewComment(@Argument String storyId) {
        log.info("onNewComment {}", storyId);
        return publisher.getEvents(storyId);
    }

}
