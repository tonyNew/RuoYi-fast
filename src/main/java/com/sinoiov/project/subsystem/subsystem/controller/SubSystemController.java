package com.sinoiov.project.subsystem.subsystem.controller;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sinoiov.common.controller.AbstractController;
import com.sinoiov.common.domain.Result;
import com.sinoiov.common.utils.security.ShiroUtils;
import com.sinoiov.framework.aspectj.lang.annotation.Log;
import com.sinoiov.framework.aspectj.lang.enums.BusinessType;
import com.sinoiov.project.subsystem.subsystem.domain.SubSystem;
import com.sinoiov.project.subsystem.subsystem.service.ISubSystemService;
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
	
    @Log(title = "子系统-新增", businessType = BusinessType.INSERT)
    @Transactional
	@ApiOperation("新增")
	@RequestMapping(value="/add",method= {RequestMethod.POST})
	public Result<Boolean> add(@RequestBody SubSystem domain) {
    	Long userId = ShiroUtils.getUserId();
    	domain.setCreateBy(userId);
    	domain.setCreateTime(new Date());
		boolean success = service.add(domain);
		return ResultUtils.WrapSuccess(success);
	}
	
    
    @Log(title = "子系统-修改", businessType = BusinessType.UPDATE)
    @Transactional
	@ApiOperation("更新")
	@RequestMapping(value="/update",method= {RequestMethod.POST})
	public Result<Boolean> update(@RequestBody SubSystem domain) {
    	Long userId = ShiroUtils.getUserId();
    	domain.setUpdateBy(userId);
    	domain.setUpdateTime(new Date());
		boolean success = service.update(domain);
		return ResultUtils.WrapSuccess(success);
	}
	
    @Log(title = "子系统-删除", businessType = BusinessType.UPDATE)
    @Transactional
	@ApiOperation("删除")
	@RequestMapping(value="/del",method= {RequestMethod.DELETE})
	public Result<Boolean> delById(long id) {
		boolean success = service.delById(id);
		return ResultUtils.WrapSuccess(success);
	}
    
    
    
    
    
}
