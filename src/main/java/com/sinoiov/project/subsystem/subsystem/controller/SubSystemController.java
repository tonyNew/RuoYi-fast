package com.sinoiov.project.subsystem.subsystem.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sinoiov.common.controller.AbstractController;
import com.sinoiov.project.subsystem.subsystem.domain.SubSystem;
import com.sinoiov.project.subsystem.subsystem.service.ISubSystemService;

import io.swagger.annotations.Api;

/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月12日
 * 描    述：
*/
@Api(description="子系统管理")
@RestController
@RequestMapping("/sino/subSystem/subSystem")
public class SubSystemController  extends AbstractController<ISubSystemService, SubSystem>{
}
