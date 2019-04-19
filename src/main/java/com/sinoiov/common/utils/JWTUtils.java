package com.sinoiov.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sinoiov.common.constant.Constants;
import com.sinoiov.project.system.user.domain.User;

/**
 * 作 者： niuyi@sinoiov.com 创建于：2019年4月18日 描 述：
 */
public class JWTUtils {

	/**
	 * 获得token中的信息无需secret解密也能获得
	 * 
	 * @return token中包含的签发时间
	 */
	public static Date getIssuedAt(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getIssuedAt();
		} catch (JWTDecodeException e) {
			return null;
		}
	}

	/**
	 * 获得token中的信息无需secret解密也能获得
	 * 
	 * @return token中包含的用户名
	 */
	public static String getLoginName(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("loginName").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}

	/**
	 * 生成签名,expireTime后过期
	 * 
	 * @param username 用户名
	 * @param time     过期时间s
	 * @return 加密的token
	 */
	public static String sign(String loginName, String salt, long time) {
		try {
			Date date = new Date(System.currentTimeMillis() + time * 1000);
			Algorithm algorithm = Algorithm.HMAC256(salt);
			// 附带loginName信息
			return JWT.create().withClaim("loginName", loginName).withExpiresAt(date).withIssuedAt(new Date())
					.sign(algorithm);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * token是否过期
	 * 
	 * @return true：过期
	 */
	public static boolean isTokenExpired(String token) {
		Date now = Calendar.getInstance().getTime();
		DecodedJWT jwt = JWT.decode(token);
		return jwt.getExpiresAt().before(now);
	}

	/**
	 * 生成随机盐,长度32位
	 * 
	 * @return
	 */
	public static String generateSalt() {
		SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
		String hex = secureRandom.nextBytes(16).toHex();
		return hex;
	}
	
	/**
	 * 生成签名,expireTime后过期
	 * 
	 * @param username 用户名
	 * @param time     过期时间s
	 * @return 加密的token
	 */
	public static String sign(@NotNull User user) {
		try {
			Date date =DateUtils.addMinutes(new Date(), Constants.EXPIRED_TIME);
			Algorithm algorithm = Algorithm.HMAC256(StringUtils.defaultString(user.getSalt()));
			// 附带loginName信息
			return JWT.create().withClaim("loginName", user.getLoginName()).withExpiresAt(date).withIssuedAt(new Date())
					.sign(algorithm);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

}
