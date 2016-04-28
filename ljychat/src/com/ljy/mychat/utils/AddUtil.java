package com.ljy.mychat.utils;

public class AddUtil {
	
	String from;
	String to ;
	private static AddUtil instance;
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	public static AddUtil getInstance(){
		if(null == instance){
			instance = new AddUtil();
		}
		return instance;
	}
}
