package com.sinoiov.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
/**
 * 
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月19日
 * 描    述：redis
 *
 */
@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate redisKvTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.afterPropertiesSet();

		return redisTemplate;
	}
//	@Bean
//	public StringRedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//		StringRedisTemplate template = new StringRedisTemplate();
//		template.setConnectionFactory(redisConnectionFactory);
//		template.setEnableTransactionSupport(true);
//		return template;
//	}

}