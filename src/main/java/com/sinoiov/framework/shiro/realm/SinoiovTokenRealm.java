package com.sinoiov.framework.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinoiov.framework.shiro.SinoiovToken;
import com.sinoiov.framework.sso.service.ISSOTokenService;
import com.sinoiov.project.system.user.domain.User;
import com.sinoiov.utils.StringUtils;

/**
 * 
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月18日
 * 描    述：
 *
 */
public class SinoiovTokenRealm extends UserRealm {
	private static final Logger log = LoggerFactory.getLogger(SinoiovTokenRealm.class);

	@Autowired
	private ISSOTokenService sSOTokenService;

	/**
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		if(token instanceof SinoiovToken) {
			SinoiovToken upToken = (SinoiovToken) token;
			User user = null;
			if (StringUtils.isNotEmpty(upToken.getToken())) {
				user = sSOTokenService.selectUserByToken(upToken.getToken());
				if (user == null) {
					throw new AuthenticationException("token" + upToken.getToken() + "校验不通过");
				}
				return new SimpleAuthenticationInfo(user, user, getName());
			}
		}
		return super.doGetAuthenticationInfo(token);
	}

	@Override
	protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info)
			throws AuthenticationException {
		if (token instanceof SinoiovToken) {
			return;
		}
		super.assertCredentialsMatch(token, info);
	}
}
