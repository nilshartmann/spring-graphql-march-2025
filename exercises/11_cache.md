# Exercise: Caching

* **Add caching to the application**

* Enable Spring Boot cache support
    * Add the annotation `@EnableCaching` to the class `nh.springgraphql.graphqlservice.GraphqlServiceApplication`

* Where do you think caching makes sense in our application?
    * Add caches at those points

* As an "artificial" example to try it out, you can use the `Story.excerpt` field
    * In the `StoryRepository`, you'll find a method called `generateExcerpt`
    * The schema-mapping method for `Story.excerpt` can use this method to "generate" the excerpt
    * In the `generateExcerpt` method, you can use the `sleep` method to artificially slow it down

* Add the `@Cacheable` annotation
    * You can view the caches via the Actuator endpoints at `http://localhost:8008/actuator/caches`
    * If you use a cache named `excerpt-cache`, you can use the endpoint `http://localhost:8008/actuator/excerpt-cache` to see the cache content as well
    * Tip: IntelliJ has an **Actuator** view that allows you to call these endpoints

* Can you set the key to be a string consisting of `Story.id` and `maxLength`?

## Documentation

* Cache support in Spring: https://docs.spring.io/spring-framework/reference/integration/cache/annotations.html
* Background: "Spring Expression Language (SpEL)": https://docs.spring.io/spring-framework/reference/core/expressions.html
