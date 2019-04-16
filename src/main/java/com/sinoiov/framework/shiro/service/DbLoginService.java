package com.sinoiov.framework.shiro.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.sinoiov.project.system.user.domain.User;

/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月16日
 * 描    述：
*/
@ConditionalOnProperty(prefix = "sinoiov.ldap", name = "enabled", havingValue = "false")
@Configuration
public class DbLoginService extends LoginService {

	@Override
	boolean validatePassword(User user, String password) {
		passwordService.validate(user, password);
		return false;
	}

}
