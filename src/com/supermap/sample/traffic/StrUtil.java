package com.supermap.sample.traffic;

import net.sf.json.JSONObject;

public class StrUtil {

	private static final String BLANK = "";
	private static final String NULL_C = "NULL";
	private static final String NULL_L = "null";
	
	public final static String FULL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public final static String SHORT_DATE_PATTERN = "yyyy-MM-dd";
	
	public static boolean isEmpty(String str) {
		return str == null || BLANK.compareTo(str) == 0 || NULL_C.compareTo(str) == 0 || NULL_L.compareTo(str) == 0;
	}
	
	public static boolean isEmpty(Object str) {
		return str == null || BLANK.equals(str);
	}
	/**
	 * 将实体对象转换为json字符串
	 * @param object
	 * @return
	 */
	public static String objectToJsonString(Object object) {
		JSONObject jsonObject = JSONObject.fromObject(object);
		return jsonObject.toString();
	}

	/**
	 * 不够位数的在前面补0，保留numStr的长度位数字
	 * @param numStr
	 * @return
	 */
	public static String leftAppend(int num, String numStr) {
		String result = "";
		result = String.format("%0" + num + "d", Integer.parseInt(numStr));

		return result;
	}

	public static void main(String[] args) {
		String b = "bb";
		System.out.println(b);
		StrUtil test = new StrUtil();
		System.out.println("字符串"+test.leftAppend(5, "2"));
		System.out.println("正负数字符串是否相等："+("123").equals("-123"));
		System.out.println("数字符串是否相等："+("123").equals("123"));




	}
}
