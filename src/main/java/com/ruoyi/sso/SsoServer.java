package com.ruoyi.sso;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

/**
 * 作    者： niuyi@missfresh.cn
 * 创建于：2019年4月9日
 * 描    述：
*/
public class SsoServer extends AbstractSsoServer {

	@Override
	Collection<SsoRemoteClient> SsoClients() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	boolean logInAuthValid(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean authValid(LogInAuth logInAut) {
		// TODO Auto-generated method stub
		return false;
	}

}
