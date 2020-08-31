package org.easy.tool.convert;

import org.easy.tool.util.DateUtil;
import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Date2StringConverter implements Converter<Date, String> {

    @Override
    public String convert(Date source) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_DATETIME);
        return sdf.format(source);
    }
}
