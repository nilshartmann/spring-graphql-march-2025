package nh.springgraphql.graphqlservice.gqljava;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import nh.springgraphql.graphqlservice.domain.Story;
import nh.springgraphql.graphqlservice.domain.StoryRepository;

public class StoryByIdDataFetcher {

    // TODO:
    //  Implement this DataFetcher
    //   - It should return a single Story based on its ID (speficied as 'storyId' argument)
    //     from the "StoryRepository" (or null if there is no such story)

    private final StoryRepository storyRepository = new StoryRepository();
}
