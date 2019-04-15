package com.sinoiov.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作    者： niuyi@missfresh.cn
 * 创建于：2019年4月9日
 * 描    述：
*/
public class SsoRemoteServer implements SsoAbility {

	@Override
	public String logIn(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void logOut(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean authenticate(LogInAuth logInAuth, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getLogInUrl() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
