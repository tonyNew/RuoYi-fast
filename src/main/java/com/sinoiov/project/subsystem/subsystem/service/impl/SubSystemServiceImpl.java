package com.sinoiov.project.subsystem.subsystem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sinoiov.common.service.BaseService;
import com.sinoiov.project.subsystem.subsystem.domain.SubSystem;
import com.sinoiov.project.subsystem.subsystem.mapper.SubSystemMapper;
import com.sinoiov.project.subsystem.subsystem.service.ISubSystemService;

/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月12日
 * 描    述：
*/
@Service
public class SubSystemServiceImpl extends BaseService<SubSystemMapper, SubSystem> implements ISubSystemService{
	@Override
	public List<SubSystem> selectSubsystemByUserId(long userId) {
		SubSystemMapper subMapper=(SubSystemMapper) mapper;
		return  subMapper.selectSubsystemByUserId(userId);
	}

}
