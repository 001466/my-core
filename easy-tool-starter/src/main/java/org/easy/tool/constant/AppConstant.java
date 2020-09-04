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
package org.easy.tool.constant;

/**
 * 系统常量
 *
 * @author Chill
 */
public interface AppConstant {

	/**
	 * 应用版本
	 */
	String APPLICATION_VERSION = "2.0.0";

	/**
	 * 基础包
	 */
	String BASE_PACKAGES = "org.easy";



	/**
	 * 网关模块名称
	 */
	String APPLICATION_GATEWAY_NAME =  "gateway";
	/**
	 * 授权模块名称
	 */
	String APPLICATION_AUTH_NAME =  "auth";
	/**
	 * 监控模块名称
	 */
	String APPLICATION_ADMIN_NAME =  "admin";
	/**
	 * 首页模块名称
	 */
	String APPLICATION_DESK_NAME =  "desk";
	/**
	 * 系统模块名称
	 */
	String APPLICATION_SYSTEM_NAME =  "system";
	/**
	 * 用户模块名称
	 */
	String APPLICATION_USER_NAME =  "user";
	/**
	 * 日志模块名称
	 */
	String APPLICATION_LOG_NAME =  "log";
	/**
	 * 开发模块名称
	 */
	String APPLICATION_DEVELOP_NAME =  "develop";
	/**
	 * 测试模块名称
	 */
	String APPLICATION_TEST_NAME =  "test";

	/**
	 * 开发环境
	 */
	String DEV_CDOE = "dev";
	/**
	 * 生产环境
	 */
	String PROD_CODE = "prod";
	/**
	 * 测试环境
	 */
	String TEST_CODE = "test";

	/**
	 * 代码部署于 linux 上，工作默认为 mac 和 Windows
	 */
	String OS_NAME_LINUX = "LINUX";

}
