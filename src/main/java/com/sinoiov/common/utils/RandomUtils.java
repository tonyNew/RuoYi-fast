package com.sinoiov.common.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;

/**
 * 作 者： niuyi@sinoiov.com 创建于：2019年4月16日 描 述：
 */
public class RandomUtils {
	public static final SecureRandomNumberGenerator RANDOM=new SecureRandomNumberGenerator();

	public static String gen() {
		String hex = RANDOM.nextBytes(15).toBase64();
		return hex;
	}
	
}
