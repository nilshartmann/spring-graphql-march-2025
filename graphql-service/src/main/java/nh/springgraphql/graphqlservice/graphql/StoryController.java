package nh.springgraphql.graphqlservice.graphql;

import nh.springgraphql.graphqlservice.domain.PublisherServiceClient;
import nh.springgraphql.graphqlservice.domain.Story;
import org.dataloader.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class StoryController {

    private static final Logger log = LoggerFactory.getLogger( StoryController.class );
    private final PublisherServiceClient publisherServiceClient;

    public StoryController(PublisherServiceClient publisherServiceClient, BatchLoaderRegistry registry) {
        this.publisherServiceClient = publisherServiceClient;

//        registry.forTypePair(String.class, Publisher.class)
//            .registerMappedBatchLoader(
//
//                (publisherIds, env) -> {
//                    log.info("Loading Publishers with ids {}", publisherIds);
//                    var publisherIdToPublisherObject = publisherIds.stream()
//                        .map(publisherServiceClient::fetchPublisher)
//                        .map(Publisher::fromJson)
//                        .collect(Collectors.toMap(Publisher::id, Function.identity()));
//
//                    return Mono.just(publisherIdToPublisherObject);
//                }
//            );
    }

    enum ContactType {phone, email}

    record Contact(ContactType type, String value) {
    }

    record Publisher(String id, String name, Contact contact) {

        public static Publisher fromJson(Optional<Map<String, String>> jsonMap) {
            return jsonMap.map(publisherMap -> new Publisher(
                publisherMap.get("id"),
                publisherMap.get("name"),
                new Contact(
                    ContactType.valueOf(publisherMap.get("contact_type")),
                    publisherMap.get("contact_value")
                ))).orElse(null);
        }

    }

    // "Optimize" performance by lazy initializing Contact
    record BetterPublisher(String id, String name, Map<String, String> rawValues) {
        Contact getContact() {
            return new Contact(
                ContactType.valueOf(rawValues.get("contact_type")),
                rawValues.get("contact_value")
            );
        }
    }

    // APPROACH #2: BatchMapping
    //   Return Mono<Map<...>>
    @BatchMapping
    Map<Story, Publisher> publisher(List<Story> stories) {
        var publisherIds = stories.stream()
            .map(Story::publisherId)
            .distinct()
            .toList();

        // gra

        // In real life:
        //   - use reactive APIs for parallel requests
        //   - if that's possible use apis that can return multiple entities/Resources in ONE call
        var publisherIdToPublisherObject = publisherIds.stream()
                        .map( (id) -> {
                            try {
                                return publisherServiceClient.fetchPublisher(id);
                            } catch (Exception ex) {
                                return null;
                            }
                        })
                        .map(Publisher::fromJson)
                        .collect(Collectors.toMap(Publisher::id, Function.identity()));

        var publishers = stories.stream().collect(Collectors.toMap(
            Function.identity(), s -> publisherIdToPublisherObject.get(s.publisherId())
        ));

        return publishers;
    }

    // APPROACH #1: DataLoader
//    @SchemaMapping
//    CompletableFuture<Publisher> publisher(Story story, DataLoader<String, Publisher> dataLoader) {
//        String publisherId = story.publisherId();
//        return dataLoader.load(publisherId);
////        var result = publisherServiceClient.fetchPublisher(publisherId); // ⚠️ ⚠️ ⚠️ ⚠️ ⚠️ ⚠️
////        return Publisher.fromJson(result);
//    }

//    @SchemaMapping
//    Optional<Publisher> publisher(Story story, DataFetchingEnvironment env) {
//        String publisherId = story.publisherId();
//        var result = publisherServiceClient.fetchPublisher(publisherId); // ⚠️ ⚠️ ⚠️ ⚠️ ⚠️ ⚠️
//        if (result.isEmpty()) {
//            return Optional.empty();
//        }
//
//        var publisherMap = result.get();
//
//        // "Optimization" by only returning fields that are actually requested in the _current_ query
//        var selectionSet = env.getSelectionSet();
//        boolean hasContactFieldSelected = selectionSet.contains("contact/**");
//
//        if (!hasContactFieldSelected) {
//            return Optional.of(new Publisher(
//                publisherMap.get("id"),
//                publisherMap.get("name"),
//                null));
//        }
//
//        return Optional.of(new Publisher(
//            publisherMap.get("id"),
//            publisherMap.get("name"),
//            new Contact(
//                ContactType.valueOf(publisherMap.get("contact_type")),
//                publisherMap.get("contact_value")
//            )));
//
//    }
}
