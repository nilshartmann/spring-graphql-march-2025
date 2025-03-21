package nh.springgraphql.graphqlservice.gqljava;


import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import nh.springgraphql.graphqlservice.domain.Story;
import nh.springgraphql.graphqlservice.domain.StoryRepository;

import java.util.List;

public class QueryStoriesDataFetcher {
  // TODO: Implement this DataFetcher:
  //  it should return ALL stories from the StoryRepository

    private final StoryRepository storyRepository = new StoryRepository();
}
