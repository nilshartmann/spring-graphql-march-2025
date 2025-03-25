package nh.springgraphql.graphqlservice.graphql;

import graphql.schema.DataFetchingEnvironment;
import nh.springgraphql.graphqlservice.domain.PublisherServiceClient;
import nh.springgraphql.graphqlservice.domain.Story;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Optional;

@Controller
public class StoryController {

    private final PublisherServiceClient publisherServiceClient;

    public StoryController(PublisherServiceClient publisherServiceClient) {
        this.publisherServiceClient = publisherServiceClient;
    }

    enum ContactType {phone, email}

    record Contact(ContactType type, String value) {
    }

    record Publisher(String id, String name, Contact contact) {
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

    @SchemaMapping
    Optional<Publisher> publisher(Story story, DataFetchingEnvironment env) {
        String publisherId = story.publisherId();
        var result = publisherServiceClient.fetchPublisher(publisherId); // ⚠️ ⚠️ ⚠️ ⚠️ ⚠️ ⚠️
        if (result.isEmpty()) {
            return Optional.empty();
        }

        var publisherMap = result.get();

        // "Optimization" by only returning fields that are actually requested in the _current_ query
        var selectionSet = env.getSelectionSet();
        boolean hasContactFieldSelected = selectionSet.contains("contact/**");

        if (!hasContactFieldSelected) {
            return Optional.of(new Publisher(
                publisherMap.get("id"),
                publisherMap.get("name"),
                null));
        }

        return Optional.of(new Publisher(
            publisherMap.get("id"),
            publisherMap.get("name"),
            new Contact(
                ContactType.valueOf(publisherMap.get("contact_type")),
                publisherMap.get("contact_value")
            )));

    }
}
