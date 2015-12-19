package com.ljy.mychat.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpRequestCommonUtils {
	
	public static String sendRegisterRequestByPost(String uri,String name,String password){
		
		HttpClient httpClient = new DefaultHttpClient();
		String result = "-1";
		
		try {
			HttpPost post = new HttpPost(uri);
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("username", name));
			list.add(new BasicNameValuePair("password", password));
			post.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
			HttpResponse resp = httpClient.execute(post);
			result = EntityUtils.toString(resp.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String sendPostResquest(String uri,String name,String flag){
		
		HttpClient httpClient = new DefaultHttpClient();
		String result = "-1";
		
		try {
			HttpPost post = new HttpPost(uri);
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("accept", name));
			list.add(new BasicNameValuePair("flag", flag));
			post.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
			HttpResponse resp = httpClient.execute(post);
			result = EntityUtils.toString(resp.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String sendInviteRequest(String uri,String flag,String inviteman,String acceptman,String time){
		
		HttpClient httpClient = new DefaultHttpClient();
		String result = "-1";
		
		try {
			
			HttpPost post = new HttpPost(uri);
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("invite", inviteman));
			list.add(new BasicNameValuePair("accept", acceptman));
			list.add(new BasicNameValuePair("time", time));
			list.add(new BasicNameValuePair("flag", flag));
			post.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
			HttpResponse resp = httpClient.execute(post);
			result = EntityUtils.toString(resp.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}
