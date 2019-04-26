package com.sinoiov.framework.shiro.web.filter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sinoiov.common.constant.Constants;
import com.sinoiov.common.utils.JWTUtils;
import com.sinoiov.common.utils.security.ShiroUtils;
import com.sinoiov.framework.shiro.SinoiovToken;
import com.sinoiov.project.system.user.domain.User;

public class JWTAuthFilter extends AuthenticatingFilter {
	private final Logger log = LoggerFactory.getLogger(JWTAuthFilter.class);

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) //对于OPTION请求做拦截，不做token校验
            return false;

        return super.preHandle(request, response);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(this.isLoginRequest(request, response)||getPathWithinApplication(request).equals("/sso/user/auth"))
            return true;
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch(IllegalStateException e){ //not found any token
            log.error("Not found any token");
        }catch (Exception e) {
            log.error("Error occurs when login", e);
        }
        return allowed || super.isPermissive(mappedValue);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        String jwtToken = getAuthzHeader(servletRequest);
//        if(StringUtils.isNotBlank(jwtToken)&&!JWTUtils.isTokenExpired(jwtToken))
        if(StringUtils.isNotBlank(jwtToken))
            return new SinoiovToken(jwtToken);

        return null;
    }


    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        String newToken = null;
        if(token instanceof SinoiovToken){
        	SinoiovToken jwtToken = (SinoiovToken)token;
        	User user = ShiroUtils.getSysUser();
            boolean shouldRefresh = shouldTokenRefresh(JWTUtils.getIssuedAt(jwtToken.getToken()));
            if(shouldRefresh) {
                newToken = JWTUtils.sign(user);
            }
        }
        if(StringUtils.isNotBlank(newToken))
            httpResponse.setHeader(Constants.SSO_AUTHORIZATION_TOKEN, newToken);

        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.error("Validate token fail, token:{}, error:{}", token.toString(), e.getMessage());
        return false;
    }

    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String header = httpRequest.getHeader(Constants.SSO_AUTHORIZATION_TOKEN);
        return header;
    }

    protected boolean shouldTokenRefresh(Date issueAt){
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(Constants.TOKEN_REFRESH_INTERVAL_SECONDS).isAfter(issueTime);
    }


	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
}
