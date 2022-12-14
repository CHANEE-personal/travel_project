package com.travel.travel_project.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.List.of;

//@Configuration
//@EnableCaching
public class CacheConfiguration {
    /**
     * <pre>
     * 1. MethodName : cacheManager
     * 2. ClassName  : CacheConfiguration.java
     * 3. Comment    : api cache 설정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 02. 09.
     * </pre>
     */
//    @Bean
//    public CacheManager cacheManager() {
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        cacheManager.setCaches(of(
//                new ConcurrentMapCache("user"),
//                new ConcurrentMapCache("travel"),
//                new ConcurrentMapCache("group"),
//                new ConcurrentMapCache("group_user"),
//                new ConcurrentMapCache("post"),
//                new ConcurrentMapCache("review"),
//                new ConcurrentMapCache("notice"),
//                new ConcurrentMapCache("faq"),
//                new ConcurrentMapCache("common")));
//        return cacheManager;
//    }
}
