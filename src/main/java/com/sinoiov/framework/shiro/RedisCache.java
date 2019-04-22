
package com.sinoiov.framework.shiro;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
/**
 * 
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月19日
 * 描    述：redis缓存
 *
 * @param <K>
 * @param <V>
 */
public class RedisCache<K, V> implements Cache<K, V> {

	private long expireTime = 120;// 缓存的超时时间，单位为s

	private RedisTemplate<K, V> redisTemplate;// 通过构造方法注入该对象

	public RedisCache() {
		super();
	}

	public RedisCache(long expireTime, RedisTemplate<K, V> redisTemplate) {
		super();
		this.expireTime = expireTime;
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 通过key来获取对应的缓存对象 通过源码我们可以发现，shiro需要的key的类型为Object，V的类型为AuthorizationInfo对象
	 */
	@Override
	public V get(K key) throws CacheException {
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * 将权限信息加入缓存中
	 */
	@Override
	public V put(K key, V value) throws CacheException {
		redisTemplate.opsForValue().set(key, value, this.expireTime, TimeUnit.SECONDS);
		return value;
	}

	/**
	 * 将权限信息从缓存中删除
	 */
	@Override
	public V remove(K key) throws CacheException {
		V v = redisTemplate.opsForValue().get(key);
		redisTemplate.opsForValue().getOperations().delete(key);
		return v;
	}

	@Override
	public void clear() throws CacheException {

	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public Set<K> keys() {
		return null;
	}

	@Override
	public Collection<V> values() {
		return null;
	}

}
