package com.sinoiov.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * 作 者： niuyi@sinoiov.com 创建于：2019年4月18日 描 述：
 */
public class LdapConfig {
	@Value("${ldap.url}")
	private String ldapUrl;

	@Value("${ldap.base}")
	private String ldapBase;

	@Value("${ldap.userDn}")
	private String ldapUserDn;

	@Value("${ldap.userPwd}")
	private String ldapUserPwd;

	@Value("${ldap.referral}")
	private String ldapReferral;

	@Bean
	public LdapTemplate ldapTemplate() {
		return new LdapTemplate(contextSourceTarget());
	}

	@Bean
	public LdapContextSource contextSourceTarget() {
		LdapContextSource ldapContextSource = new LdapContextSource();
		ldapContextSource.setUrl(ldapUrl);
		ldapContextSource.setBase(ldapBase);
		ldapContextSource.setUserDn(ldapUserDn);
		ldapContextSource.setPassword(ldapUserPwd);
		ldapContextSource.setReferral(ldapReferral);
		return ldapContextSource;
	}

}
