package com.ruoyi.sso;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作    者： niuyi@missfresh.cn
 * 创建于：2019年4月9日
 * 描    述：
*/
public abstract class AbstractSsoClient implements SsoAbility {
	
	SsoRemoteServer ssoServer;
	
	@Override
	public String logIn(HttpServletRequest request, HttpServletResponse response){
		jump2LogInUrl(response);
		return null;
	}

	@Override
	public void logOut(HttpServletRequest request) {
		ssoServer.logOut(request);
	}

	@Override
	public boolean authenticate(LogInAuth logInAuth,HttpServletResponse response) {
		boolean pass = ssoServer.authenticate(logInAuth,null);
		if(!pass) {
			jump2LogInUrl(response);
		}
		return true;
	}
	
	private  void  jump2LogInUrl(HttpServletResponse response) {
		String logInUrl = ssoServer.getLogInUrl();
		try {
			response.sendRedirect(logInUrl);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
