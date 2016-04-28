package com.ljy.mychat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyUtils {
	
	public static String getFormatTime(long second){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date(second);
		return simpleDateFormat.format(d);
	}
	
}
