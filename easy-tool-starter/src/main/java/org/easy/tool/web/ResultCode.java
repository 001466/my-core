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
package org.easy.tool.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

/**
 * 业务代码枚举
 *
 * @author Chill
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {

	/**
	 * 操作成功
	 */
	SUCCESS(HttpServletResponse.SC_OK, "操作成功"),

	/**
	 * 业务异常
	 */
	FAILURE(HttpServletResponse.SC_BAD_REQUEST, "业务异常"),

	/**
	 * 请求未授权
	 */
	UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "请求未授权"),

	/**
	 * 客户端请求未授权
	 */
	CLIENT_UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "客户端请求未授权"),

	/**
	 * 404 没找到请求
	 */
	NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "404 没找到请求"),

	/**
	 * 消息不能读取
	 */
	MSG_NOT_READABLE(HttpServletResponse.SC_BAD_REQUEST, "消息不能读取"),

	/**
	 * 不支持当前请求方法
	 */
	METHOD_NOT_SUPPORTED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "不支持当前请求方法"),

	/**
	 * 不支持当前媒体类型
	 */
	MEDIA_TYPE_NOT_SUPPORTED(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "不支持当前媒体类型"),

	/**
	 * 请求被拒绝
	 */
	REQ_REJECT(HttpServletResponse.SC_FORBIDDEN, "请求被拒绝"),

	/**
	 * 服务器异常
	 */
	INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器异常"),

	/**
	 * 缺少必要的请求参数
	 */
	PARAM_MISS(HttpServletResponse.SC_BAD_REQUEST, "缺少必要的请求参数"),

	/**
	 * 请求参数类型错误
	 */
	PARAM_TYPE_ERROR(HttpServletResponse.SC_BAD_REQUEST, "请求参数类型错误"),

	/**
	 * 请求参数绑定错误
	 */
	PARAM_BIND_ERROR(HttpServletResponse.SC_BAD_REQUEST, "请求参数绑定错误"),

	/**
	 * 参数校验失败
	 */
	PARAM_VALID_ERROR(HttpServletResponse.SC_BAD_REQUEST, "参数校验失败"),


	UNDEFINED_ERROR(10000,"错误未定义，请联系管理员"),
	NO_DEVICE_PERM(10001, "没有设备权限"),
	NO_HOST_ID(10002, "请选择家庭"),
	HOST_CTL_FAIL(10003, "主机执行失败"),
	NO_DEVICE(10004, "未查询到设备"),
	NO_CHILD_DEVICE(10005, "未查询到子设备"),
	ERROR_GET_DEVICE_STATE(10006, "查询设备状态错误"),
	NO_BEAN_FOR_COMMAND(10007,"不支持该指令"),
	GET_INFO_FROM_HOST_FAIL(10008,"从主机获取信息失败"),
	REPEAT_CTL_SCENES(10009,"不要重复执行场景"),
	USER_NOT_FOUND(10010,"用户未注册, 该账号未注册，请注册后再登录"),
	DEVICE_ADD_FAIL(10011,"设备添加失败"),
	HOST_NOT_FOUND(10012,"未查询到该主机"),
	DEVICE_NOT_FOUND_IN_HOST(10013,"主机不存在该设备"),
	DEVICE_CTL_FAIL(10014,"主机控制设备失败"),
	HOST_OFFLINE(10015,"主机已离线"),
	SCENE_NOT_FOUND_IN_HOST(10016,"未在主机中找到该场景")
	;

	/**
	 * code编码
	 */
	final int code;
	/**
	 * 中文信息描述
	 */
	final String message;


	public static ResultCode getResultCodeByCode(int code){
		for (ResultCode resultCode : ResultCode.values()){
			if (code==resultCode.getCode()){
				return resultCode;
			}
		}
		return null;
	}

}
