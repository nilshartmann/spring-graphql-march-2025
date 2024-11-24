package nh.springgraphql.graphqlservice.gqljava;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import nh.springgraphql.graphqlservice.domain.StoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Diese Klasse wird nur als Beispiel für die Verwendung von graphql-java benötigt.
 * <p>
 * In unserer Spring Anwendung werden wir sie nicht weiterverwenden, weil das erzeugen
 * der GraphQL Instanz dann von Spring übernommen wird.
 */
public class GraphQLProvider {

    private static final Logger log = LoggerFactory.getLogger(GraphQLProvider.class);
    private final StoryRepository storyRepository;

    public GraphQLProvider() {
        this.storyRepository = new StoryRepository();
    }

    public GraphQL getGraphQL() {
        SchemaParser schemaParser = new SchemaParser();
        InputStream schemaStream = getClass().getResourceAsStream("/graphql/schema.graphqls");
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schemaStream);

        // TODO:
        //  1. Implementiere die DataFetcher:
        //      - QueryStoriesDataFetcher (für Query.stories)
        //      - StoryByIdDataFetcher (für Query.story)
        //      - ExcerptDataFetcher( für Story.excerpt)
        // TODO:
        //  2. Registriere die DataFetcher hier am RuntimeWiring

        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
            .type("Query", builder -> {
                // todo: DataFetcher registrieren
                return builder;
            })
            .type("Story", builder -> {
                    // todo: DataFetcher registrieren
                    return builder;
                }
            )
            .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, wiring);

        GraphQL graphql = GraphQL.newGraphQL(graphQLSchema).build();

        return graphql;
    }


}
