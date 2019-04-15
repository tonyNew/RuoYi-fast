package com.ruoyi.sso;

import java.io.Serializable;

/**
 * 作    者： niuyi@missfresh.cn
 * 创建于：2019年4月9日
 * 描    述：
*/
public class LogInAuth implements Serializable{
	private static final long serialVersionUID = 3841194866174936388L;
	private String userName;
	private String passWord;
	private String token;
}
