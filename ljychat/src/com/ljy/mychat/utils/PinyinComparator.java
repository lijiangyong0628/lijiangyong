package com.ljy.mychat.utils;

import java.util.Comparator;

import com.ljy.mychat.model.Friend;



public class PinyinComparator implements Comparator{

//	@Override
//	public int compare(Object o1, Object o2) {
//		 String str1 = PinyinUtils.getPingYin((String) o1);
//	     String str2 = PinyinUtils.getPingYin((String) o2);
//	     return str1.compareTo(str2);
//	}
//	
	@Override
	public int compare(Object o1, Object o2) {
		 String str1 = PinyinUtils.getPingYin(((Friend)o1).username);
	     String str2 = PinyinUtils.getPingYin(((Friend)o2).username);
	     return str1.compareToIgnoreCase(str2);
//	     return str1.compareTo(str2);
	}

}
