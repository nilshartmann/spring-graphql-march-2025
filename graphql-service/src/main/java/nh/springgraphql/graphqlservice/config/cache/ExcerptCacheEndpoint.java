package nh.springgraphql.graphqlservice.config.cache;

import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Displays the CONTENT of our "Excerpt" cache via an Actuator endpoint.
 * <p>
 * This would NOT be done in a real application:
 * - The endpoint is (for simplicity) only built for the "excerpt-cache"
 * - There is already a general Actuator endpoint for the cache in Spring Boot
 * - However, the built-in endpoint does not show cache CONTENT (for good security reasons!)
 * - Additionally, my implementation only works with the default in-memory cache
 */
//@Component
//@Endpoint(id = "excerpt-cache") // Endpoint-id muss in den application.properties enabled werden
class ExcerptCacheEndpoint {

    /**
     * Muss zum @Cacheable im StoryRepository passen
     */
    private final static String excerptCacheName = "excerpt-cache";

    private final CacheManager cacheManager;

    ExcerptCacheEndpoint(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * List all available cache names.
     */
    @ReadOperation
    public Map<String, Object> listContent() {
        var content = getCacheContent();
        return Map.of(excerptCacheName, content);
    }

    @DeleteOperation
    public Map<String, Object> deleteExcerpt(@Selector String key) {
        Cache cache = cacheManager.getCache(excerptCacheName);
        if (cache == null) {
            return Map.of("error", "Cache not found");
        }

        cache.evict(key);
        return Map.of("message", "Key '%s' removed from cache '%s'".formatted(
            key, excerptCacheName
        ));
    }

    private Map<String, Object> getCacheContent() {
        var cacheName = excerptCacheName;
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            return Map.of("error", "Cache '%s' not found".formatted(cacheName));
        }

        if (cache.getNativeCache() instanceof Map<?, ?> nativeCache) {
            return Map.of("content", nativeCache);
        }

        return Map.of("error", "Native content of cache '%s' is not a map".formatted(cacheName));
    }
}
