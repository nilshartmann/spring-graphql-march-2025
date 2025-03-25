package nh.springgraphql.graphqlservice.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

/**
 * Emits a OnNewCommentEvent for each received application event
 * CommentAddedEvent
 */
@Component
public class CommentEventPublisher {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final StoryRepository storyRepository;
  private final Sinks.Many<CommentCreatedEvent> sink;

  public CommentEventPublisher(StoryRepository storyRepository) {
    this.storyRepository = storyRepository;
    this.sink = Sinks.many()
      .multicast()
      .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
  }

  @EventListener
  public void onNewComment(CommentCreatedEvent event) {
    logger.info("onNewComment received event for comment {}, currentSubscriberCount {}",
      event,
      this.sink.currentSubscriberCount()
    );

    this.sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
  }

  public Flux<CommentCreatedEvent> getEvents(String storyId) {
    logger.info("Subscribe {}", storyId);
    return this.sink
      .asFlux()
      .filter(event -> {
        logger.info("EVENT {}", event);
        var s = storyRepository.findStoryForComment(event.newComment().id());
        if (s.isPresent()) {
          var story = s.get();
          return story.id().equals(storyId);
        }

        return false;
      });
  }
}