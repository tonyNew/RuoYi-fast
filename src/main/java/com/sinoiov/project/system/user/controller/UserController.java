package com.sinoiov.project.system.user.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.jdbc.util.ResultSetUtil;
import com.sinoiov.common.domain.Result;
import com.sinoiov.common.utils.StringUtils;
import com.sinoiov.common.utils.poi.ExcelUtil;
import com.sinoiov.common.utils.security.ShiroUtils;
import com.sinoiov.framework.aspectj.lang.annotation.Log;
import com.sinoiov.framework.aspectj.lang.enums.BusinessType;
import com.sinoiov.framework.web.controller.BaseController;
import com.sinoiov.framework.web.domain.AjaxResult;
import com.sinoiov.framework.web.page.TableDataInfo;
import com.sinoiov.project.system.role.service.IRoleService;
import com.sinoiov.project.system.user.domain.User;
import com.sinoiov.project.system.user.service.IUserService;
import com.sinoiov.utils.ResultUtils;

import io.swagger.annotations.Api;

/**
 * 用户信息
 * 
 * @author tony
 */
@Api(description="用户管理")
@RestController
@RequestMapping("/system/user")
public class UserController extends BaseController
{
    private String prefix = "system/user";

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

//    @RequiresPermissions("system:user:list")
    @GetMapping("/list")
    public Result<TableDataInfo> list(User user)
    {
        startPage();
        List<User> list = userService.selectUserList(user);
        return ResultUtils.WrapSuccess(getDataTable(list));
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:user:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(User user)
    {
        List<User> list = userService.selectUserList(user);
        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
        return util.exportExcel(list, "user");
    }

    /**
     * 新增用户
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("roles", roleService.selectRoleAll());
        return prefix + "/add";
    }

    /**
     * 新增保存用户
     */
//    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addSave(User user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && User.isAdmin(user.getUserId()))
        {
            return error("不允许修改超级管理员用户");
        }
        return toAjax(userService.insertUser(user));
    }

    
    /**
     * 修改用户
     */
    @GetMapping("/detail")
    public Result<User> detail(long id)
    {
    	User user = userService.selectUserById(id);
    	return ResultUtils.WrapSuccess(user);
    }

    /**
     * 修改保存用户
     */
//    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public AjaxResult editSave(User user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && User.isAdmin(user.getUserId()))
        {
            return error("不允许修改超级管理员用户");
        }
        return toAjax(userService.updateUser(user));
    }

//    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/resetPwd";
    }

//    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwd(User user)
    {
        return toAjax(userService.resetUserPwd(user));
    }

//    @RequiresPermissions("system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping("/del")
    public AjaxResult del(String ids)
    {
        try
        {
            return toAjax(userService.deleteUserByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 校验用户名
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public String checkLoginNameUnique(User user)
    {
        return userService.checkLoginNameUnique(user.getLoginName());
    }

    /**
     * 校验手机号码
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(User user)
    {
        return userService.checkPhoneUnique(user);
    }

    /**
     * 校验email邮箱
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(User user)
    {
        return userService.checkEmailUnique(user);
    }
}