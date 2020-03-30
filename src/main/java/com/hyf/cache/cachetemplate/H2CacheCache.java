package com.hyf.cache.cachetemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.concurrent.Callable;

/**
 * @author Howinfun
 * @desc H2CacheCache, L2 cache implementer
 * @date 2020/3/25
 * @email 876237770@qq.com
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class H2CacheCache implements Cache {

    private EhCacheCacheManager ehCacheCacheManager;
    private RedisCacheManager redisCacheManager;
    private String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public ValueWrapper get(Object key) {

        Cache ehCache = ehCacheCacheManager.getCache(this.name);
        if(null != ehCache && null != ehCache.get(key)){
            log.info("select from ehcache,key:{}",key);
            return ehCache.get(key);
        }

        Cache redisCache = redisCacheManager.getCache(this.name);
        if(null != redisCache && null != redisCache.get(key)){
            log.info("select from redis,key:{}",key);
            ehCache.put(key,redisCache.get(key).get());
            return redisCache.get(key);
        }

        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {

        Cache ehCache = ehCacheCacheManager.getCache(this.name);
        if(null != ehCache && null != ehCache.get(key,type)){
            log.info("select from ehcache,key:{},type:{}",key,type);
            return ehCache.get(key,type);
        }

        Cache redisCache = redisCacheManager.getCache(this.name);
        if(null != redisCache && null != redisCache.get(key,type)){
            log.info("select from redis,key:{},type:{}",key,type);
            ehCache.put(key,redisCache.get(key).get());
            return redisCache.get(key,type);
        }

        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {

        Cache ehCache = ehCacheCacheManager.getCache(this.name);
        if(null != ehCache && null != ehCache.get(key,valueLoader)){
            log.info("select from ehcache,key:{},valueLoader:{}",key,valueLoader);
            return ehCache.get(key,valueLoader);
        }

        Cache redisCache = redisCacheManager.getCache(this.name);
        if(null != redisCache && null != redisCache.get(key,valueLoader)){
            log.info("select from redis,key:{},valueLoader:{}",key,valueLoader);
            ehCache.put(key,redisCache.get(key).get());
            return redisCache.get(key,valueLoader);
        }

        return null;
    }

    @Override
    public void put(Object key, Object value) {

        Cache ehCache = ehCacheCacheManager.getCache(this.name);
        if (null != ehCache){
            log.info("insert into ehcache,key:{},value:{}",key,value);
            ehCache.put(key,value);
        }

        Cache redisCache = redisCacheManager.getCache(this.name);
        if (null != redisCache){
            log.info("insert into redis,key:{},value:{}",key,value);
            redisCache.put(key,value);
        }

    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {

        ValueWrapper valueWrapper = null;
        Cache ehCache = ehCacheCacheManager.getCache(this.name);
        if (null != ehCache){
            log.info("insert into ehcache,key:{},value:{}",key,value);
            valueWrapper = ehCache.putIfAbsent(key,value);
        }

        Cache redisCache = redisCacheManager.getCache(this.name);
        if (null != redisCache){
            log.info("insert into redis,key:{},value:{}",key,value);
            valueWrapper = redisCache.putIfAbsent(key,value);
        }
        return valueWrapper;
    }

    @Override
    public void evict(Object key) {

        Cache ehCache = ehCacheCacheManager.getCache(this.name);
        if (null != ehCache) {
            log.info("delete from ehcache,key:{}", key);
            ehCache.evict(key);
        }

        Cache redisCache = redisCacheManager.getCache(this.name);
        if (null != redisCache) {
            log.info("delete from ehcache,key:{}", key);
            redisCache.evict(key);
        }
    }

    @Override
    public void clear() {
        Cache ehCache = ehCacheCacheManager.getCache(this.name);
        if (null != ehCache) {
            log.info("clear ehcache");
            ehCache.clear();
        }

        Cache redisCache = redisCacheManager.getCache(this.name);
        if (null != redisCache) {
            log.info("clear redis");
            redisCache.clear();
        }
    }
}