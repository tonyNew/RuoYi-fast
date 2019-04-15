package com.ruoyi.sso;

import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作    者： niuyi@missfresh.cn
 * 创建于：2019年4月9日
 * 描    述：抽像sso认证中心
*/
public abstract class AbstractSsoServer implements SsoAbility {
	/**获取ssoClient列表*/
	abstract Collection<SsoRemoteClient> SsoClients();
	/**登录认证*/
	abstract boolean logInAuthValid(HttpServletRequest request);
	abstract boolean authValid(LogInAuth logInAut);
	
	@Override
	public String logIn(HttpServletRequest request, HttpServletResponse response) {
		//获取登录信息
		SSOUtils.getLogInAuth(request);
		if(logInAuthValid(request)) {//登录信息校验
			return doLogIn(request);//登录信息操作
		}
		return null;
	}

	@Override
	public void logOut(HttpServletRequest request) {
		doLogOut(request);
		Collection<SsoRemoteClient> ssoClients = SsoClients();
		if(ssoClients!=null) {
			for (SsoRemoteClient ssoClient : ssoClients) {
				ssoClient.logOut(request);
			}
		}
	}

	@Override
	public boolean authenticate(LogInAuth logInAuth,HttpServletResponse response) {
		return authValid(logInAuth);
	}
	
	/**
	 * 登录并返回令牌
	 * @param request
	 * @return 令牌
	 */
	private String doLogIn(ServletRequest request) {
		return null;
	}
	
	/**
	 * 注销并返回令牌
	 * @param request
	 * @return 令牌
	 */
	private void doLogOut(HttpServletRequest request) {
		
	}
	
	public String getLogInUrl() {
		return null;
	}
}
