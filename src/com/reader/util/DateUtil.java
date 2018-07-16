package com.reader.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String getDateID() {
		return getFormatNowDate("yyyyMMddhhmmss");
	}

	public static String getFormatNowDate(String dateFormat) {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		return df.format(date);
	}
}
