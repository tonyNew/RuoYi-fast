package com.sinoiov.project.system.user.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sinoiov.common.domain.Result;
import com.sinoiov.framework.config.SinoiovConfig;
import com.sinoiov.framework.web.controller.BaseController;
import com.sinoiov.project.system.menu.domain.Menu;
import com.sinoiov.project.system.menu.service.IMenuService;
import com.sinoiov.project.system.user.domain.User;
import com.sinoiov.utils.ResultUtils;

/**
 * 首页 业务处理
 * 
 * @author tony
 */
@RestController
public class IndexController extends BaseController
{
    @Autowired
    private IMenuService menuService;

    @Autowired
    private SinoiovConfig ruoYiConfig;

    // 系统首页
    @GetMapping("/index")
    public Result<List<Menu>> index(ModelMap mmap)
    {
        // 取身份信息
        User user = getSysUser();
        // 根据用户id取出菜单
        List<Menu> menus = menuService.selectMenusByUser(user,null);
//        mmap.put("menus", menus);
//        mmap.put("user", user);
//        mmap.put("copyrightYear", ruoYiConfig.getCopyrightYear());
        return ResultUtils.WrapSuccess(menus);
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
        mmap.put("version", ruoYiConfig.getVersion());
        return "main";
    }
}
