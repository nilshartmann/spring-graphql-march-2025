package nh.springgraphql.graphqlservice.gqljava;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import nh.springgraphql.graphqlservice.domain.Story;
import nh.springgraphql.graphqlservice.domain.StoryRepository;

public class StoryByIdDataFetcher {

    // TODO:
    //  Implementiere diesen DataFetcher
    //   - Er soll eine einzelne Story an Hand ihrer Id (übergeben als Argument)
    //     aus dem StoryRepository zurückliefern (or null)

    private final StoryRepository storyRepository = new StoryRepository();
}
