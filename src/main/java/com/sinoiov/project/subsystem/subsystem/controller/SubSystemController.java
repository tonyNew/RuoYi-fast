package com.sinoiov.project.subsystem.subsystem.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sinoiov.common.controller.AbstractController;
import com.sinoiov.common.domain.Result;
import com.sinoiov.common.utils.bean.BeanUtils;
import com.sinoiov.common.utils.security.ShiroUtils;
import com.sinoiov.framework.aspectj.lang.annotation.Log;
import com.sinoiov.framework.aspectj.lang.enums.BusinessType;
import com.sinoiov.project.common.enums.MenuTypeEnum;
import com.sinoiov.project.subsystem.subsystem.domain.SubSystem;
import com.sinoiov.project.subsystem.subsystem.service.ISubSystemService;
import com.sinoiov.project.system.menu.domain.Menu;
import com.sinoiov.project.system.menu.service.IMenuService;
import com.sinoiov.project.system.menu.service.MenuServiceImpl;
import com.sinoiov.utils.ResultUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月12日
 * 描    述：
*/
@Api(description="子系统管理")
@RestController
@RequestMapping("/sino/subSystem/subSystem")
public class SubSystemController  extends AbstractController<ISubSystemService, SubSystem>{
	
	@Autowired
	IMenuService menuService;
	
    @Log(title = "子系统-新增", businessType = BusinessType.INSERT)
    @Transactional
	@ApiOperation("新增")
	@RequestMapping(value="/add",method= {RequestMethod.POST})
	public Result<Boolean> add(@Validated @RequestBody SubSystem domain) {
    	Long userId = ShiroUtils.getUserId();
    	domain.setCreateBy(userId);
    	domain.setCreateTime(new Date());
    	Result<Boolean> result = super.add(domain);
    	if(result.getData()) {
    		Menu menu =subSystem2Menu(domain);
    		menu.setSubsystemId(domain.getId());
    		menu.setParentId(0L);
    		menuService.insertMenu(menu);
    	}
		return result;
	}
	
    
    @Log(title = "子系统-修改", businessType = BusinessType.UPDATE)
    @Transactional
	@ApiOperation("更新")
	@RequestMapping(value="/update",method= {RequestMethod.POST})
	public Result<Boolean> update(@Validated @RequestBody SubSystem domain) {
    	Long userId = ShiroUtils.getUserId();
    	domain.setUpdateBy(userId);
    	domain.setUpdateTime(new Date());
    	Result<Boolean> result = super.update(domain);
    	/*if(result.getData()) {
    		Menu menu = new Menu();
    		menu.setMenuName(domain.getName());
    		menuService.updateMenu(menu);
    	}*/
		return result;
	}
	
    @Log(title = "子系统-删除", businessType = BusinessType.DELETE)
    @Transactional
	@ApiOperation("删除")
	@RequestMapping(value="/del",method= {RequestMethod.POST})
	public Result<Boolean> delById(long id) {
		boolean success = service.delById(id);
		return ResultUtils.WrapSuccess(success);
	}
    

	@ApiOperation("用户展示")
	@RequestMapping(value="/index",method= {RequestMethod.GET})
	public Result<List<SubSystem>> index() {
		SubSystem domain=new SubSystem();
		domain.setVisible(0);
		return ResultUtils.WrapSuccess(service.selectSubsystemByUserId(ShiroUtils.getUserId()));
	}
	
	
	private Menu subSystem2Menu(SubSystem subSystem) {
		Menu menu = new Menu();
		BeanUtils.copyBeanProp(menu, subSystem);
		menu.setMenuType(MenuTypeEnum.S.name());
		menu.setMenuName(subSystem.getName());
		menu.setParentId(0L);
		return menu;
	}
}
