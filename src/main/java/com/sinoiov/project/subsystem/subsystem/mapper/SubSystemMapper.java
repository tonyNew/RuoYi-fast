package com.sinoiov.project.subsystem.subsystem.mapper;

import java.util.List;

import com.sinoiov.common.mapper.IMapper;
import com.sinoiov.project.subsystem.subsystem.domain.SubSystem;

/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月12日
 * 描    述：
*/
public interface SubSystemMapper extends IMapper<SubSystem>  {

	List<SubSystem> selectSubsystemByUserId(long userId);

}
