package nh.springgraphql.remoteservice.publisher;

import java.util.List;
import java.util.Optional;

class Publishers {

    private final List<Publisher> publisherList;

    Publishers() {
        this.publisherList = List.of(
            new Publisher("1", "Tech Times", "email", "contact@techtimes.com"),
            new Publisher("2", "Daily News", "phone", "+1234567890"),
            new Publisher("3", "World Journal", "email", "info@worldjournal.com"),
            new Publisher("4", "Science Digest", "phone", "+9876543210"),
            new Publisher("5", "Art Weekly", "email", "support@artweekly.com")
        );
    }

    Optional<Publisher> findPublisherWithId(String id) {
        return this.publisherList.stream().filter(
            u -> u.id().equals(id)
        ).findFirst();
    }
}
