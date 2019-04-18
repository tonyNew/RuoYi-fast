package com.sinoiov.project.system.menu.controller;

import java.util.List;
import java.util.Map;
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

import com.sinoiov.common.domain.Result;
import com.sinoiov.framework.aspectj.lang.annotation.Log;
import com.sinoiov.framework.aspectj.lang.enums.BusinessType;
import com.sinoiov.framework.web.controller.BaseController;
import com.sinoiov.framework.web.domain.AjaxResult;
import com.sinoiov.project.system.menu.domain.Menu;
import com.sinoiov.project.system.menu.service.IMenuService;
import com.sinoiov.project.system.role.domain.Role;
import com.sinoiov.utils.ResultUtils;

import io.swagger.annotations.Api;

/**
 * 菜单信息
 * 
 * @author tony
 */
@Api(description="菜单管理")
@RequestMapping("/system/menu")
@RestController
public class MenuController extends BaseController
{
    private String prefix = "system/menu";

    @Autowired
    private IMenuService menuService;


    @RequiresPermissions("system:menu:list")
    @GetMapping("/list")
    public Result<List<Menu>>  list(Menu menu)
    {
        List<Menu> menuList = menuService.selectMenuList(menu);
        return ResultUtils.WrapSuccess(menuList);
    }

    /**
     * 删除菜单
     */
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:menu:remove")
    @PostMapping("/del")
    public AjaxResult del( Long id)
    {
        if (menuService.selectCountMenuByParentId(id) > 0)
        {
            return error(1, "存在子菜单,不允许删除");
        }
        if (menuService.selectCountRoleMenuByMenuId(id) > 0)
        {
            return error(1, "菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(id));
    }


    /**
     * 新增保存菜单
     */
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:menu:add")
    @PostMapping("/add")
    @Transactional
    public AjaxResult addSave(Menu menu)
    {
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改保存菜单
     */
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:menu:edit")
    @PostMapping("/update")
    public AjaxResult editSave(Menu menu)
    {
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 校验菜单名称
     */
    @PostMapping("/checkMenuNameUnique")
    public Result<String> checkMenuNameUnique(Menu menu)
    {
        return ResultUtils.WrapSuccess(menuService.checkMenuNameUnique(menu));
    }

    /**
     * 加载角色菜单列表树
     */
    @GetMapping("/roleMenuTreeData")
    @ResponseBody
    public List<Map<String, Object>> roleMenuTreeData(Role role)
    {
        List<Map<String, Object>> tree = menuService.roleMenuTreeData(role);
        return tree;
    }

    /**
     * 加载所有菜单列表树
     */
    @GetMapping("/menuTreeData")
    @ResponseBody
    public List<Map<String, Object>> menuTreeData(Role role)
    {
        List<Map<String, Object>> tree = menuService.menuTreeData();
        return tree;
    }

}