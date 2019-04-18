package com.sinoiov.framework.shiro;
/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月17日
 * 描    述：
*/
public class SinoiovToken  extends org.apache.shiro.authc.UsernamePasswordToken{
	
	private String token;
	
	public SinoiovToken(String username,String password,String token) {
		super(username, password);
		this.token=token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
