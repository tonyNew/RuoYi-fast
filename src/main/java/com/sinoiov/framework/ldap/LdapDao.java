package com.sinoiov.framework.ldap;

import org.springframework.stereotype.Repository;

import com.sinoiov.project.system.user.domain.User;

/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月16日
 * 描    述：
*/
@Repository
public class LdapDao {
	
	public boolean validatePassword(User user, String password) {
		return true;
	}
	
	public boolean addUser(User user) {
		return true;
	}
	
	public boolean resetUserPwd(User user) {
		return true;
	}
	
}
