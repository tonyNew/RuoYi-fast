package com.ruoyi.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作    者： niuyi@missfresh.cn
 * 创建于：2019年4月9日
 * 描    述: 单点登录系统能力
*/
public interface SsoAbility {
	/**登录能力*/
	public String logIn(HttpServletRequest request, HttpServletResponse response);
	/**注销能力*/
	public void logOut(HttpServletRequest request);
	/**认证能力*/
	public boolean authenticate(LogInAuth logInAuth,HttpServletResponse response);
}
