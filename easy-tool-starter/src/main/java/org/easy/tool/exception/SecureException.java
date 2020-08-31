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
package org.easy.tool.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.easy.tool.web.ResultCode;


/**
 * Secure异常
 *
 * @author Chill
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SecureException extends CustomException {
	private static final long serialVersionUID = 2359767895161832954L;

	public SecureException(String message) {
		super(message);
	}

	public SecureException(ResultCode code, String message) {
		super(message);
		this.code = code;
	}

//	public SecureException(  Throwable cause) {
//		super(cause);
//	}

	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
