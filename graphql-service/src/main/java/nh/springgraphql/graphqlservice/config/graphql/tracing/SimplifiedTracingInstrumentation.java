package nh.springgraphql.graphqlservice.config.graphql.tracing;

import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.PublicApi;
import graphql.collect.ImmutableKit;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimplePerformantInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationCreateStateParameters;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.execution.instrumentation.parameters.InstrumentationValidationParameters;
import graphql.language.Document;
import graphql.validation.ValidationError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static graphql.execution.instrumentation.InstrumentationState.ofState;
import static graphql.execution.instrumentation.SimpleInstrumentationContext.whenCompleted;

/**
 * This {@link Instrumentation} implementation uses {@link SimplifiedTracingSupport} to
 * capture tracing information and puts it into the {@link ExecutionResult}
 */
@PublicApi
public class SimplifiedTracingInstrumentation extends SimplePerformantInstrumentation {

    public static class Options {
        private final boolean includeTrivialDataFetchers;

        private Options(boolean includeTrivialDataFetchers) {
            this.includeTrivialDataFetchers = includeTrivialDataFetchers;
        }

        public boolean isIncludeTrivialDataFetchers() {
            return includeTrivialDataFetchers;
        }

        /**
         * By default trivial data fetchers (those that simple pull data from an object into field) are included
         * in tracing but you can control this behavior.
         *
         * @param flag the flag on whether to trace trivial data fetchers
         *
         * @return a new options object
         */
        public Options includeTrivialDataFetchers(boolean flag) {
            return new Options(flag);
        }

        public static Options newOptions() {
            return new Options(true);
        }

    }

    public SimplifiedTracingInstrumentation() {
        this(Options.newOptions().includeTrivialDataFetchers(false));
    }

    public SimplifiedTracingInstrumentation(Options options) {
        this.options = options;
    }

    private final Options options;

    @Override
    public @Nullable CompletableFuture<InstrumentationState> createStateAsync(InstrumentationCreateStateParameters parameters) {
        return CompletableFuture.completedFuture(new SimplifiedTracingSupport(options.includeTrivialDataFetchers));
    }

    @Override
    public @NotNull CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult, InstrumentationExecutionParameters parameters, InstrumentationState rawState) {
        Map<Object, Object> currentExt = executionResult.getExtensions();

        SimplifiedTracingSupport simplifiedTracingSupport = ofState(rawState);
        Map<Object, Object> withTracingExt = new LinkedHashMap<>(currentExt == null ? ImmutableKit.emptyMap() : currentExt);
        withTracingExt.put("tracing", simplifiedTracingSupport.snapshotTracingData());

        return CompletableFuture.completedFuture(new ExecutionResultImpl(executionResult.getData(), executionResult.getErrors(), withTracingExt));
    }

    @Override
    public InstrumentationContext<Object> beginFieldFetch(InstrumentationFieldFetchParameters parameters, InstrumentationState rawState) {
        SimplifiedTracingSupport simplifiedTracingSupport = ofState(rawState);
        SimplifiedTracingSupport.TracingContext ctx = simplifiedTracingSupport.beginField(parameters.getEnvironment(), parameters.isTrivialDataFetcher());
        return whenCompleted((result, t) -> ctx.onEnd());
    }

    @Override
    public InstrumentationContext<Document> beginParse(InstrumentationExecutionParameters parameters, InstrumentationState rawState) {
        SimplifiedTracingSupport simplifiedTracingSupport = ofState(rawState);
        SimplifiedTracingSupport.TracingContext ctx = simplifiedTracingSupport.beginParse();
        return whenCompleted((result, t) -> ctx.onEnd());
    }

    @Override
    public InstrumentationContext<List<ValidationError>> beginValidation(InstrumentationValidationParameters parameters, InstrumentationState rawState) {
        SimplifiedTracingSupport simplifiedTracingSupport = ofState(rawState);
        SimplifiedTracingSupport.TracingContext ctx = simplifiedTracingSupport.beginValidation();
        return whenCompleted((result, t) -> ctx.onEnd());
    }
}
