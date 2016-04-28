package com.ljy.mychat.ui;

import org.jivesoftware.smack.XMPPConnection;

import com.ljy.ljychat.R;
import com.ljy.mychat.utils.HttpRequestCommonUtils;
import com.ljy.mychat.utils.JSONParseUtils;
import com.ljy.mychat.utils.LiLog;
import com.ljy.mychat.utils.MustCommonUtils;
import com.ljy.mychat.xmpp.XmppConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText username;
	private EditText password;
	private static final int requestCodes=0;
	private boolean progressShow;
	private XMPPConnection conn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ljy_activity_login);
		
		username = (EditText) findViewById(R.id.login_username);
		password = (EditText) findViewById(R.id.login_password);

	}
	
	public void register(View v){
		//跳转注册界面
		Intent intent = new Intent(this,RegisterActivity.class);
		startActivityForResult(intent, requestCodes);
	}
	
	public void login(View v){
		//发送登录请求到服务器
		final String u = username.getText().toString();
		final String p = password.getText().toString();
		
		//登录前检查是否有网络，此处要设置uses-permission，否则报错
		if(!MustCommonUtils.getInstance().isNetworkConnected(LoginActivity.this)){
			Toast.makeText(LoginActivity.this, "无网络",Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(u)){
			Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(p)){
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
//		progressShow = true;
//		final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
//		pd.setCanceledOnTouchOutside(false);
//		pd.setOnCancelListener(new OnCancelListener() {
//			
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				progressShow = false;
//			}
//		});
//		pd.setMessage("登录中。。");
//		pd.show();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
//					String uri = "http://10.20.73.17:8080/LijiangyongChatServer/login";
//					String result = HttpRequestCommonUtils.sendRegisterRequestByPost(uri, u, p);
//					LiLog.d(result);
//					
//					String state = JSONParseUtils.getStateOrCode(result, "state");
//					String code = JSONParseUtils.getStateOrCode(result, "code");
//					
//					if("".equals(state) || "".equals(code)){
//						hander.sendEmptyMessage(1);
//						return;
//					}
//					if("401".equals(code)){
//						hander.sendEmptyMessage(2);
//					}else if("402".equals(code)){
//						hander.sendEmptyMessage(3);
//					}else if("200".equals(code)){
//						MustCommonUtils.getInstance().setCurrentUserName(u);
//						hander.sendEmptyMessage(4);
//					}else{
//						hander.sendEmptyMessage(5);
//					}
					if(conn == null){
						conn = XmppConnection.getInstance().getConnection();
					}
					
					boolean b = XmppConnection.getInstance().loginAccount(u, p);
					if(b){
						MustCommonUtils.getInstance().setCurrentUserName(u);
						hander.sendEmptyMessage(4);
					}else{
						hander.sendEmptyMessage(2);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}

	Handler hander = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1){
				Toast.makeText(LoginActivity.this, "服务端返回json格式错误", Toast.LENGTH_SHORT).show();
			}
			if(msg.what == 2){
				Toast.makeText(LoginActivity.this, "密码错误或者用户名不存在", Toast.LENGTH_SHORT).show();
			}
			if(msg.what == 3){
				Toast.makeText(LoginActivity.this, "用户名不存在，请先注册", Toast.LENGTH_SHORT).show();
			}
			if(msg.what == 4){
				Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
			if(msg.what == 5){
				Toast.makeText(LoginActivity.this, "服务器异常错误，登录失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case requestCodes:
			if(resultCode == RESULT_OK){
				String returnName=data.getStringExtra("registername");
				username.setText(returnName);
				LiLog.d(returnName);
			}
			break;

		default:
			break;
		}
	}
	
	
}
