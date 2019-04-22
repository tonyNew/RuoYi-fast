package com.sinoiov.framework.shiro;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月19日
 * 描    述：
*/
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.sinoiov.common.constant.Constants;

public class RedisCacheManager implements CacheManager{

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return new RedisCache<>(Constants.EXPIRED_TIME*60, redisTemplate);
	}

}
