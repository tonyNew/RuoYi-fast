package com.sinoiov.framework.sso.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.sinoiov.common.constant.Constants;
import com.sinoiov.common.utils.JWTUtils;
import com.sinoiov.framework.sso.service.ISSOTokenService;
import com.sinoiov.project.system.user.domain.User;
import com.sinoiov.utils.StringUtils;

/**
 * 作 者： niuyi@sinoiov.com 创建于：2019年4月16日 描 述：
 */
@Service
public class SSORedisTokenServiceImpl implements ISSOTokenService {

	@Autowired
	RedisTemplate<String, User> redisTemplate;

	@Override
	public void saveToken(User user) {
		if (StringUtils.isNull(user.getToken())) {
			user.setToken(JWTUtils.sign(user));
		}
		redisTemplate.opsForValue().set(getKey(user.getLoginName()), user, Constants.EXPIRED_TIME, TimeUnit.MINUTES);
	}

	@Override
	public void clearToken(User user) {
		redisTemplate.delete(getKey(user.getLoginName()));
	}

	@Override
	public User selectUserByToken(String token) {
		return checkToken(token);
	}

	@Override
	public String selectTokenByUser(String loginName) {
		User user = redisTemplate.opsForValue().get(getKey(loginName));
		if (user != null) {
			return user.getToken();
		}
		return null;
	}

	@Override
	public User checkToken(String token) {
		if (StringUtils.isNotEmpty(token)) {
			String loginName = JWTUtils.getLoginName(token);
			String key = getKey(loginName);
			User user = redisTemplate.opsForValue().get(key);
			if (user != null && token.equals(user.getToken())) {
				boolean shouldRefresh = shouldTokenRefresh(JWTUtils.getIssuedAt(token));
				if (shouldRefresh) {
					redisTemplate.expire(key, Constants.EXPIRED_TIME, TimeUnit.MINUTES);
				}
				return user;
			}
		}
		return null;
	}

	protected boolean shouldTokenRefresh(Date issueAt) {
		LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
		return LocalDateTime.now().minusSeconds(Constants.TOKEN_REFRESH_INTERVAL_SECONDS).isAfter(issueTime);
	}

}
