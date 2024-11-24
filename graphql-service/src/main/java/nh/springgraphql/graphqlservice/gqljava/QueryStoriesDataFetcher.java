package nh.springgraphql.graphqlservice.gqljava;


import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import nh.springgraphql.graphqlservice.domain.Story;
import nh.springgraphql.graphqlservice.domain.StoryRepository;

import java.util.List;

public class QueryStoriesDataFetcher {
  // TODO: Implementiere diesen DataFetcher:
  //  er soll ALLE Stories aus einem StoryRepository zurückliefern

    private final StoryRepository storyRepository = new StoryRepository();
}
