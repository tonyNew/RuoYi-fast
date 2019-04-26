package com.sinoiov.project.system.user.domain;

import java.io.Serializable;
import java.util.Collection;

import lombok.Builder;
import lombok.Data;

/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月26日
 * 描    述：sso用户认证bean
*/
@Data
@Builder
public class UserAuth implements Serializable{
	private long userId;
	private String username;
	private Collection<String> perms;
}
