package com.reader.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static String getMatchedStr(String expStr, String pattern) {

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(expStr);
		while (m.find()) {
			// System.out.println(m.group(0));
			return m.group(1);
		}
		return null;
	}

}
