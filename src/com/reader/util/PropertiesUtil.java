package com.reader.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil {

	Map<String, String> map = null;

	public PropertiesUtil(String fileName) throws FileNotFoundException, IOException {
		map = new HashMap<>();
		Properties pps = new Properties();
		pps.load(new FileInputStream(fileName));
		Enumeration<?> enum1 = pps.propertyNames();
		while (enum1.hasMoreElements()) {
			String strKey = (String) enum1.nextElement();
			String strValue = pps.getProperty(strKey);
			map.put(strKey.trim().toLowerCase(), strValue.trim());
		}
	}

	public String getValue(String key) {
		return map.get(key.toLowerCase());
	}

	public ArrayList<String> getValuesWithStart(String start) {
		ArrayList<String> arrayList = new ArrayList<>();
		Set<String> keyset = map.keySet();
		for (String key : keyset) {
			if (key.startsWith(start.toLowerCase().trim())) {
				arrayList.add(map.get(key));
			}
		}
		return arrayList;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		PropertiesUtil propertiesUtil = new PropertiesUtil("runtime.properties");
		System.err.println(propertiesUtil.getValue("username"));
	}
}
