package com.sinoiov.framework.ldap;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

import com.sinoiov.project.system.user.domain.User;
import com.sinoiov.utils.StringUtils;

/**
 * 作 者： niuyi@sinoiov.com
 * 创建于：2019年4月16日 
 * 描 述：
 * 
 * 用户名称，cn,ou,dc 分别：用户，组，域
 */
@Repository
public class LdapDao {
	private static final String COMMON_FILTE_STR = "ou=People,dc=trafficguide,dc=com,dc=cn";
	private static final String USER_COMMON_FILTE_STR = "uid=%s,"+COMMON_FILTE_STR;

	private final Logger logger = LoggerFactory.getLogger(LdapDao.class);

	@Autowired
	LdapTemplate template;

	public boolean checkUser(User user, String password) {
		boolean authenticate = false;
		try {
			authenticate = template.authenticate("", "(uid=" + user.getLoginName() + ")", password);

//			String filter = "(&(objectclass=inetOrgPerson)(uid=" + user.getLoginName() + "))";
//			List<User> list = template.search("ou=People,dc=trafficguide,dc=com,dc=cn", filter, new AttributesMapper() {
//				@Override
//				public Object mapFromAttributes(Attributes attributes) throws NamingException {
//					User user = new User();
//					user.setLoginName((attributes.get("cn").get().toString()));
//					user.setPassword(attributes.get("userPassword").get().toString());
//					return user;
//				}
//			});
//			if(password.equals(user.getPassword())) {
//				return true;
//			}
			return authenticate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean addUser(User vo) {
		try {
			if (checkUser(vo.getLoginName())) {
				return false;
			}
			// 基类设置
			BasicAttribute ocattr = new BasicAttribute("objectClass");
			ocattr.add("top");
			ocattr.add("person");
			ocattr.add("uidObject");
			ocattr.add("inetOrgPerson");
			ocattr.add("organizationalPerson");
			// 用户属性
			Attributes attrs = new BasicAttributes();
			attrs.put(ocattr);
			attrs.put("cn", StringUtils.trimToEmpty(vo.getLoginName()));
			attrs.put("sn", StringUtils.trimToEmpty(vo.getUserName()));
			attrs.put("displayName", StringUtils.trimToEmpty(vo.getUserName()));
			attrs.put("mail", StringUtils.trimToEmpty(vo.getEmail()));
			attrs.put("telephoneNumber", StringUtils.trimToEmpty(vo.getPhonenumber()));
//		attrs.put("title", StringUtils.trimToEmpty(vo.getTitle()));
			attrs.put("userPassword", StringUtils.trimToEmpty(vo.getPassword()));
			template.bind("uid=" + vo.getLoginName(), null, attrs);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public boolean resetUserPwd(User user) {
		try {
			template.modifyAttributes("uid=" + user.getLoginName(),
					new ModificationItem[] { new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
							new BasicAttribute("userPassword", user.getPassword())), });
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

	}

	public boolean updateUser(User user) {
		try {
			if (checkUser(user.getLoginName())) {
				return false;
			}
			template.modifyAttributes("uid=" + user.getLoginName(), new ModificationItem[] {
					new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("cn", user.getLoginName())),
					new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
							new BasicAttribute("displayName", user.getUserName())),
					new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("sn", user.getUserName())),
					new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("mail", user.getEmail())),
					new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
							new BasicAttribute("telephoneNumber", user.getPhonenumber())),
					new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
							new BasicAttribute("userpassword", user.getPassword()))
			});
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

	}

	public boolean delUseer(User user) {
		boolean flag=false;
		try {
			if(checkUser(user.getLoginName())){
				template.unbind(String.format(USER_COMMON_FILTE_STR, user.getLoginName()));
				flag = true;
			};
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return flag;

	}

	private boolean checkUser(String loginName) {
		String filter = "(&(objectclass=inetOrgPerson)(uid=" + loginName + "))";
		List<User> list = template.search(COMMON_FILTE_STR, filter,
				new AttributesMapper<User>() {
					@Override
					public User mapFromAttributes(Attributes attributes) throws NamingException {
						User user = new User();
						user.setLoginName((attributes.get("cn").get().toString()));
						return user;
					}
				});
		return CollectionUtils.isEmpty(list);
	}

}
