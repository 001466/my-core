package org.easy.tool.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.extern.slf4j.Slf4j;
import org.easy.tool.util.DateUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * @author winall
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@Slf4j
public class BaseEntity implements Serializable {
	static ObjectMapper mapper = new ObjectMapper();
	static {
		//序列化时忽略值为null的属性
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		//忽略值为默认值的属性
		mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
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
