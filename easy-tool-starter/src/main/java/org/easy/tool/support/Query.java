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
package org.easy.tool.support;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

/**
 * 分页工具
 *
 * @author Chill
 */
@ApiModel(description = "查询条件")
@NoArgsConstructor
public class Query<CLI_EXP> extends Page {

	@ApiModelProperty(value = "偏移位置", required = false, hidden = true)
	Integer offset;

	@ApiModelProperty(value = "分页长度", required = false, hidden = true)
	Integer rows;


	@ApiModelProperty(value = "顺序字段", required = false, hidden = false)
	String ascs;

	@ApiModelProperty(value = "倒序字段", required = false, hidden = false)
	String descs;

	@ApiModelProperty(value = "排序字段", required = false, hidden = true)
	String orderByClause;

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}


	CLI_EXP example;
	public CLI_EXP getExample() {
		return example;
	}
	public Query<CLI_EXP> setExample(CLI_EXP example) {
		this.example = example;
		return this;
	}


//	@JsonIgnore
//	@ApiModelProperty(value = "登陆用户", required = false, hidden = true)
//	BladeUser user;

	public Query(Integer page, Integer size) {
		super(page, size);
	}


	public Integer getOffset() {
		if(offset!=null) {
			return offset;
		}else if(page!=null&&size!=null){
			return (page-1)*size;
		}
		return null;
	}

	public void setOffset(Integer start) {
		this.offset = offset;
	}

	public Integer getRows() {
		if(rows!=null) {
			return rows;
		}else if(size!=null){
			return size;
		}
		return null;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getAscs() {
		return ascs;
	}

	public Query<CLI_EXP> setAscs(String ascs) {
		this.ascs = ascs;return this;
	}

	public String getDescs() {
		return descs;
	}

	public Query<CLI_EXP> setDescs(String descs) {
		this.descs = descs;return this;
	}



//	@JsonIgnore
//	@ApiModelProperty(value = "登陆用户", required = false, hidden = true)
//	public BladeUser getUser() {
//		return user;
//	}
//
//	public Query setUser(BladeUser user) {
//		this.user = user;
//		return this;
//	}



}
