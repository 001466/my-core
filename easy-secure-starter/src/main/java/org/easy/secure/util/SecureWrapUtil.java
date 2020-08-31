/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.easy.secure.util;

import lombok.SneakyThrows;
import org.easy.secure.BladeUser;
import org.easy.secure.constant.TokenConstant;
import org.easy.tool.util.StringPool;
import org.easy.tool.util.WebUtil;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * Secure工具类
 *
 * @author Chill
 */
public class SecureWrapUtil extends SecureRawUtil {

	/**
	 * 获取用户信息
	 *
	 * @return BladeUser
	 */

//	public static BladeUser getUser() {
//
//		HttpServletRequest request = WebUtil.getRequest();
//
//		if (request == null) {
//			return null;
//		}
//		// 优先从 request 中获取
//		Object bladeUser = request.getAttribute(BLADE_USER_REQUEST_ATTR);
//		if (bladeUser == null) {
//			bladeUser = getUser(request);
//			if (bladeUser != null) {
//				// 设置到 request 中
//				request.setAttribute(BLADE_USER_REQUEST_ATTR, bladeUser);
//			}
//		}
//		return (BladeUser) bladeUser;
//	}

	public static BladeUser getUser() {
		HttpServletRequest request = WebUtil.getRequest();
		if (request == null) {
			return null;
		}
 		Object bladeUser = getUser(request);
		return (BladeUser) bladeUser;
	}

	/**
	 * 获取用户id
	 *
	 * @return userId
	 */
	public static Long getUserId() {
		BladeUser user = getUser();
		return (null == user) ? -1 : user.getUserId();
	}

	/**
	 * 获取用户账号
	 *
	 * @return userAccount
	 */
	public static String getUserAccount() {
		BladeUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getAccount();
	}



	/**
	 * 获取用户名
	 *
	 * @return userName
	 */
	public static String getUserName() {
		BladeUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getUserName();
	}


	/**
	 * 获取用户角色
	 *
	 * @return userName
	 */
	public static String getUserRole() {
		BladeUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getRoleName();
	}


	public static Set<String> getUserPermissions() {
		BladeUser user = getUser();
		return (null == user) ? null : user.getPermissions();
	}


	/**
	 * 获取租户编号
	 *
	 * @return tenantCode
	 */
	public static String getTenantCode() {
		BladeUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getTenantCode();
	}


	/**
	 * 获取客户端id
	 *
	 * @return tenantCode
	 */
	public static String getClientId() {
		BladeUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getClientId();
	}


	/**
	 * 获取请求头
	 *
	 * @return header
	 */
	public static String getHeader() {
		return WebUtil.getRequest().getHeader(TokenConstant.AUTHORIZATION);
	}


	/**
	 * 创建令牌
	 *
	 * @param user     user
	 * @param audience audience
	 * @param issuer   issuer
	 * @param isExpire isExpire
	 * @return jwt
	 */

	public static String createJWT(Map<String, String> user, String audience, String issuer, boolean isExpire) {
		HttpServletRequest request = WebUtil.getRequest();
		return createJWT(user,audience,issuer,isExpire,request);
	}


	/**
	 * 客户端信息解码
	 */
	@SneakyThrows
	public static String[] extractAndDecodeHeader() {
		HttpServletRequest request = WebUtil.getRequest();
		return extractAndDecodeHeader(request);
	}

	/**
	 * 获取请求头中的客户端id
	 */
	public static String getClientIdFromHeader() {
		HttpServletRequest request = WebUtil.getRequest();
		return getClientIdFromHeader(request);
	}

	public SecureWrapUtil(RestTemplate restTemplate){
		super(restTemplate);
	}

}
