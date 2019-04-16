package com.sinoiov.framework.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.sinoiov.common.utils.RandomUtils;
import com.sinoiov.framework.sso.service.ISSOTokenService;
import com.sinoiov.project.system.user.domain.User;
import com.sinoiov.project.system.user.mapper.UserMapper;

/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月16日
 * 描    述：
*/
@Service
public class SSOTokenServiceImpl implements ISSOTokenService {
	@Autowired
	UserMapper userMapper;

	@Override
	public void saveToken(User user) {
		user.setToken(RandomUtils.gen());
		userMapper.updateUser(user);
		
	}

	@Override
	public void clearToken(User user) {
		userMapper.clearToken(user);
	}

	@Override
	public User selectUserByToken(String token) {
		User user = userMapper.selectUserByLoginToken(token);
		return user;
	}

	@Override
	public String selectTokenByUser(String loginName) {
		User user = userMapper.selectUserByLoginName(loginName);
		if(user!=null) {
			return user.getToken();
		}
		return null;
	}


	

}
