package com.sinoiov.framework.sso;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sinoiov.common.constant.Constants;
import com.sinoiov.common.domain.Result;
import com.sinoiov.common.sso.UserAuthRequest;
import com.sinoiov.common.utils.StringUtils;
import com.sinoiov.common.utils.TreeUtils;
import com.sinoiov.common.utils.security.ShiroUtils;
import com.sinoiov.framework.shiro.SinoiovToken;
import com.sinoiov.framework.sso.service.ISSOTokenService;
import com.sinoiov.project.subsystem.subsystem.domain.SubSystem;
import com.sinoiov.project.subsystem.subsystem.service.ISubSystemService;
import com.sinoiov.project.system.menu.domain.Menu;
import com.sinoiov.project.system.menu.service.IMenuService;
import com.sinoiov.project.system.user.domain.User;
import com.sinoiov.project.system.user.domain.UserAuth;
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
	 private IMenuService menuService;

	@Autowired
	ISSOTokenService sSOTokenService;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	ISubSystemService subSystemService;
	private LoadingCache<String, List<SubSystem>>  PERMISSIONCACHE=null;
	@PostConstruct
	private void init() {
		PERMISSIONCACHE=CacheBuilder.newBuilder().maximumSize(300).expireAfterAccess(3, TimeUnit.MINUTES).build(new CacheLoader<String, List<SubSystem>>() {
			@Override
			public List<SubSystem> load(String key) throws Exception {
				List<SubSystem> subsystem = subSystemService.selectSubsystemByUserId(Integer.valueOf(key));
				return subsystem;
			}
		});
	}
	
	
	@ApiOperation("登陆")
	@RequestMapping(value="/login",method= {RequestMethod.POST})
    public Result<User> login(@RequestBody SinoiovToken token,HttpServletRequest request,HttpServletResponse response)
    {
		token.setToken(WebUtils.toHttp(request).getHeader(Constants.SSO_AUTHORIZATION_TOKEN));
        Subject subject = SecurityUtils.getSubject();
        try
        {
            subject.login(token);
            User user = ShiroUtils.getSysUser();
            response.addHeader(Constants.SSO_AUTHORIZATION_TOKEN, user.getToken());
            return ResultUtils.WrapSuccess(user);
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return ResultUtils.WrapError(msg);
        }
    }
	@ApiOperation("校验")
	@RequestMapping(value="/user/auth",method= {RequestMethod.POST})
	public Result<UserAuth> check(@RequestBody UserAuthRequest request) throws ExecutionException {
		User user = sSOTokenService.checkToken(request.getToken());
		if(user!=null) {
			//List<SubSystem> subsystem = subSystemService.selectSubsystemByUserId(user.getUserId());
			List<SubSystem> subsystem = getBykey(user.getUserId()+"");
			Set<String> perms=Sets.newHashSet();
			if(!CollectionUtils.isEmpty(subsystem)) {
				perms= subsystem.stream().map(e->String.valueOf(e.getId())).collect(Collectors.toSet());
			}
			UserAuth userAuth = UserAuth.builder()
			.userId(user.getUserId())
			.username(user.getLoginName())
			.perms(perms)
			.build();
			return new Result<>(20000, userAuth, "success");
		}
		return new Result<>(20000, null, "success");
	}
	
    // 系统首页
    @GetMapping("/user/index")
    public Result<JSONObject> index(Integer subSystemId)
    {
        // 取身份信息
        User user = ShiroUtils.getSysUser();
        // 根据用户id取出菜单
        List<Menu> menus = menuService.selectMenusByUser(user,subSystemId);
        List<Menu> buttonList = Lists.newArrayList();
        List<Menu> buildMenu = TreeUtils.buildMenu(menus, buttonList);
        JSONObject result = new JSONObject();
        result.put("menus", buildMenu);
        result.put("buttons", buttonList);
        return ResultUtils.WrapSuccess(result);
    }
    
    
   public List<SubSystem> getBykey(String key){
        try {
        	List<SubSystem> subsystem = PERMISSIONCACHE.get(key);
            return subsystem;
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }
}
