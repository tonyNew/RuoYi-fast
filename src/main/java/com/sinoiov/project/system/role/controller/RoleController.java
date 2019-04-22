package com.sinoiov.project.system.role.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sinoiov.common.domain.Result;
import com.sinoiov.common.utils.poi.ExcelUtil;
import com.sinoiov.framework.aspectj.lang.annotation.Log;
import com.sinoiov.framework.aspectj.lang.enums.BusinessType;
import com.sinoiov.framework.web.controller.BaseController;
import com.sinoiov.framework.web.domain.AjaxResult;
import com.sinoiov.framework.web.page.TableDataInfo;
import com.sinoiov.project.system.role.domain.Role;
import com.sinoiov.project.system.role.service.IRoleService;
import com.sinoiov.utils.ResultUtils;

import io.swagger.annotations.Api;

/**
 * 角色信息
 * 
 * @author tony
 */
@Api(description="角色管理")
@RequestMapping("/system/role")
@RestController
public class RoleController extends BaseController
{
    private String prefix = "system/role";

    @Autowired
    private IRoleService roleService;

    @RequiresPermissions("system:role:list")
    @GetMapping("/list")
    @ResponseBody
    public Result<TableDataInfo> list(Role role)
    {
        startPage();
        List<Role> list = roleService.selectRoleList(role);
        return ResultUtils.WrapSuccess(getDataTable(list));
    }

    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:role:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Role role)
    {
        List<Role> list = roleService.selectRoleList(role);
        ExcelUtil<Role> util = new ExcelUtil<Role>(Role.class);
        return util.exportExcel(list, "role");
    }


    /**
     * 新增保存角色
     */
    @RequiresPermissions("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addSave( @RequestBody @Validated Role role)
    {
        return toAjax(roleService.insertRole(role));

    }


    /**
     * 修改保存角色
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult editSave( @RequestBody @Validated Role role)
    {
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 修改保存数据权限
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/rule")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult ruleSave(@RequestBody Role role)
    {
        return toAjax(roleService.updateRule(role));
    }

    @RequiresPermissions("system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("/del")
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(roleService.deleteRoleByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 校验角色名称
     */
    @PostMapping("/checkRoleNameUnique")
    public Result<String> checkRoleNameUnique(Role role)
    {
        return ResultUtils.WrapSuccess(roleService.checkRoleNameUnique(role));
    }

    /**
     * 校验角色权限
     */
    @PostMapping("/checkRoleKeyUnique")
    @ResponseBody
    public Result<String> checkRoleKeyUnique(Role role)
    {
        return ResultUtils.WrapSuccess(roleService.checkRoleKeyUnique(role));
    }
    

    /**
     * 修改角色
     */
    @GetMapping("/detail")
    public Result<Role> detail(Long roleId)
    {
    	return ResultUtils.WrapSuccess( roleService.selectRoleById(roleId));
    }

}