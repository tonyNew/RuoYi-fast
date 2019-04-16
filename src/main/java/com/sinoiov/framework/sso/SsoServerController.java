package com.sinoiov.framework.sso;

import java.util.Objects;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sinoiov.common.domain.Result;
import com.sinoiov.common.utils.StringUtils;
import com.sinoiov.common.utils.security.ShiroUtils;
import com.sinoiov.framework.sso.service.ISSOTokenService;
import com.sinoiov.project.system.user.domain.User;
import com.sinoiov.project.system.user.service.IUserService;
import com.sinoiov.utils.ResultUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 作    者： niuyi@missfresh.cn
 * 创建于：2019年4月9日
 * 描    述：
*/
@Api(description="单点登录")
@RestController
@RequestMapping("/sso")
public class SsoServerController {
	
	@Autowired
	ISSOTokenService sSOTokenService;
	
	@Autowired
	IUserService userService;
	
	@ApiOperation("登陆")
	@RequestMapping(value="/login",method= {RequestMethod.GET,RequestMethod.POST})
    public Result<String> login(String username, String password, Boolean rememberMe)
    {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try
        {
            subject.login(token);
            User user = ShiroUtils.getSysUser();
            String ssoToken = sSOTokenService.selectTokenByUser(user.getLoginName());
            return ResultUtils.WrapSuccess(ssoToken);
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return ResultUtils.WrapSuccess(msg);
        }
    }
	@ApiOperation("校验")
	@RequestMapping(value="/check",method= {RequestMethod.GET,RequestMethod.POST})
	public Result<String> check(@RequestParam String token) {
		User user = sSOTokenService.selectUserByToken(token);
		if(user==null) {
			return ResultUtils.WrapError("token失效");
		}
		return ResultUtils.WrapSuccess(user.getLoginName());
	}
}
