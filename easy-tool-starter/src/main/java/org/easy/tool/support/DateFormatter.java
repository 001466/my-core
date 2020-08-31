package org.easy.tool.support;

import org.easy.tool.util.DateUtil;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter implements Formatter<Date> {

    SimpleDateFormat formatter = new SimpleDateFormat(DateUtil.PATTERN_DATETIME);

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        return formatter.parse(text);
    }

    @Override
    public String print(Date date, Locale locale) {

        String formatStr= formatter.format(date);
        System.err.println("formatStr="+formatStr);
        return  formatStr;
    }
}
