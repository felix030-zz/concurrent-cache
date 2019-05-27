package com.fklemke.concurrentcache.config;

import com.fklemke.concurrentcache.domain.Word;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

/**
 * Class to configure the JSR-107 JCache implementation.
 */
@Configuration
public class CacheConfig {

    private static final String WORD_CACHE_NAME = "wordCache";
    private static final String WORD_CACHE_CONFIG = "wordCacheConfig";

    /**
     * Caching provider returns the default provider and grants access to the caching manager.
     * {destroyMethod} is referring to the policy chosen upon closing the application context.
     */
    @Bean(destroyMethod = "close")
    public CachingProvider createCachingProvider() {
        return Caching.getCachingProvider();
    }

    @Bean
    public CacheManager createCacheManager(CachingProvider cachingProvider) {
        return cachingProvider.getCacheManager();
    }

    /**
     * Defines a basic configuration definition.
     */
    @Bean(WORD_CACHE_CONFIG)
    public MutableConfiguration<String, Word> createWordCacheConfig() {
        return new MutableConfiguration<>();
    }

    /**
     * Defines the Cache containing words of text files.
     */
    @Bean(WORD_CACHE_NAME)
    public Cache<String, Word> createWordCache(CacheManager cacheManager,
                                               @Qualifier(WORD_CACHE_CONFIG) MutableConfiguration<String,Word> configuration) {
        return cacheManager.createCache(WORD_CACHE_NAME, configuration);
    }
}
