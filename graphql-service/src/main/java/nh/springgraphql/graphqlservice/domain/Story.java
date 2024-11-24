package nh.springgraphql.graphqlservice.domain;

import java.time.OffsetDateTime;
import java.util.List;

public record Story(String id,
                    OffsetDateTime date,
                    String title,
                    String body,
                    String publisherId,
                    PublicationState state,
                    List<Comment> comments) {
}
