package nh.springgraphql.graphqlservice.gqljava;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import nh.springgraphql.graphqlservice.domain.Story;
import nh.springgraphql.graphqlservice.domain.StoryRepository;

import java.util.Optional;

public class StoryByIdDataFetcher implements DataFetcher<Optional<Story>> {
    private final StoryRepository storyRepository = new StoryRepository();

    @Override
    public Optional<Story> get(DataFetchingEnvironment environment) throws Exception {
        String storyId = environment.getArgument("storyId");
        return storyRepository.findStory(storyId);
    }
}
