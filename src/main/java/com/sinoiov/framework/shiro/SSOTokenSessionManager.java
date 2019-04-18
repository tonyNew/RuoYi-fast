package com.sinoiov.framework.shiro;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import com.sinoiov.utils.StringUtils;

/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月17日
 * 描    述：
*/
public class SSOTokenSessionManager  extends DefaultWebSessionManager {
    private static final String AUTHORIZATION = "token";
    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";


	
	
	@Override
	protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
		String token = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
		if(StringUtils.isNotEmpty(token)) {//SSO单点登陆已TOKEN作为SESSIONID
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID,token);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID,Boolean.TRUE);
            return token;
		}
		return super.getSessionId(request, response);
	}
}
