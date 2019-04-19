package com.sinoiov.framework.sso.service;

import com.sinoiov.common.constant.Constants;
import com.sinoiov.project.system.user.domain.User;

/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月16日
 * 描    述：
*/
public interface ISSOTokenService {
	
	String REDIS_USER_PRE=Constants.USER_PRE;
	
	void saveToken(User user);
	
	void clearToken(User user);
	
	User selectUserByToken(String token);
	
	String selectTokenByUser(String loginName);
	
	User checkToken(String token);
	
	
	default String getKey(String loginName ) {
		return String.format(REDIS_USER_PRE,loginName);
	}
	
}
