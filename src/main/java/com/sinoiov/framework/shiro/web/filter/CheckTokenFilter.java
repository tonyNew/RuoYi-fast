package com.sinoiov.framework.shiro.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sinoiov.common.constant.Constants;
import com.sinoiov.common.utils.MessageUtils;
import com.sinoiov.common.utils.StringUtils;
import com.sinoiov.common.utils.security.ShiroUtils;
import com.sinoiov.common.utils.spring.SpringUtils;
import com.sinoiov.framework.manager.AsyncManager;
import com.sinoiov.framework.manager.factory.AsyncFactory;
import com.sinoiov.framework.shiro.SinoiovToken;
import com.sinoiov.framework.sso.service.ISSOTokenService;
import com.sinoiov.project.system.user.domain.User;

/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月18日
 * 描    述：
*/
public class CheckTokenFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {
    private static final Logger log = LoggerFactory.getLogger(CheckTokenFilter.class);
    
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception
    {
        try
        {
            Subject subject = getSubject(request, response);
            try
            {
            	String token = WebUtils.toHttp(request).getHeader(Constants.SSO_AUTHORIZATION_TOKEN);

                User user = ShiroUtils.getSysUser();
                if (StringUtils.isNull(user))
                {
            		SinoiovToken sinoiovToken = new SinoiovToken(null, null, token);
            		subject.login(sinoiovToken);
            		return true;
                }
            }
            catch (SessionException ise)
            {
                log.error("logout fail.", ise);
            }
            issueRedirect(request, response, "");
        }
        catch (Exception e)
        {
            log.error("Encountered session exception during logout.  This can generally safely be ignored.", e);
        }
        return false;
    }

}
