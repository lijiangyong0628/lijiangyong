package com.ljy.mychat.utils;

import org.json.JSONObject;

public class JSONParseUtils {

	public static String getStateOrCode(String object,String key){
		try {
			JSONObject jsonObject = new JSONObject(object);
			return jsonObject.getString(key);
		} catch (Exception e) {
			LiLog.d("json format error");
			e.printStackTrace();
		}
		return "";
	}
}
