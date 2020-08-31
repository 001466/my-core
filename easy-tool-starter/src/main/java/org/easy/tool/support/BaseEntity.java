package org.easy.tool.support;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.easy.tool.util.DateUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author winall
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@Slf4j
@Data
public class BaseEntity implements Serializable {

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private Long createUser;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private Long updateUser;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updateTime;


	static ObjectMapper mapper = new ObjectMapper();
	static {
		//序列化时忽略值为null的属性
		mapper.setSerializationInclusion(Include.NON_NULL);
		//忽略值为默认值的属性
		mapper.setDefaultPropertyInclusion(Include.NON_NULL);
		//在序列化时日期格式默认为 yyyy-MM-dd HH:mm:ss 简体中文
		mapper.setDateFormat(new SimpleDateFormat(DateUtil.PATTERN_DATETIME, Locale.CHINA));

	}


	@Override
	public String toString() {

		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(),e);
		}

		return null;
	}

}
