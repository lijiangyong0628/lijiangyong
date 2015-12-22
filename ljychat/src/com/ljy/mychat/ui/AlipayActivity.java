package com.ljy.mychat.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ljy.ljychat.R;
import com.ljy.mychat.utils.LiLog;
import com.ljy.mychat.utils.MustCommonUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlipayActivity extends Activity implements OnClickListener{
	
	private RelativeLayout alipay_rl;
	private RelativeLayout weixin_rl;
	private RelativeLayout yhcard_rl;
	private RelativeLayout submit_rl;
	private ImageView alipay_iv;
	private ImageView weixin_iv;
	private ImageView yhcard_iv;
	private String my_name;
	private TextView chongzhi_name;
	private TextView order_bootom_price_tx;
	private TextView pay_number;
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ljy_activity_zhifubao);
		my_name = MustCommonUtils.getInstance().getCurrentUserName();
		alipay_rl = (RelativeLayout) findViewById(R.id.order_alipay_kuaijie);
		weixin_rl = (RelativeLayout) findViewById(R.id.weixin_pay);
		yhcard_rl = (RelativeLayout) findViewById(R.id.card_pay);
		submit_rl = (RelativeLayout) findViewById(R.id.ljy_order_bootom_right);
		submit_rl.setOnClickListener(this);
		alipay_rl.setOnClickListener(this);
		weixin_rl.setOnClickListener(this);
		yhcard_rl.setOnClickListener(this);
		alipay_rl.setTag(Boolean.valueOf(true));
		weixin_rl.setTag(Boolean.valueOf(false));
		yhcard_rl.setTag(Boolean.valueOf(false));
		order_bootom_price_tx = (TextView) findViewById(R.id.order_bootom_price_tx);
		order_bootom_price_tx.setText(getIntent().getStringExtra("price"));
		alipay_iv = (ImageView) findViewById(R.id.order_alipay_yimg);
		weixin_iv = (ImageView) findViewById(R.id.order_wx_yimg);
		yhcard_iv = (ImageView) findViewById(R.id.order_yl_yimg);
		chongzhi_name = (TextView) findViewById(R.id.pay_uname);
		chongzhi_name.setText(my_name);
		pay_number = (TextView) findViewById(R.id.pay_number);
		pay_number.setText(getIntent().getStringExtra("price"));
	}
	
	public void back(View v){
		finish();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.order_alipay_kuaijie:
			boolean isAlipay = (Boolean) alipay_rl.getTag();
			if(isAlipay){
				return;
			}else{
				alipay_rl.setTag(Boolean.valueOf(true));
				weixin_rl.setTag(Boolean.valueOf(false));
				yhcard_rl.setTag(Boolean.valueOf(false));
				alipay_iv.setImageResource(R.drawable.ljy_ed);
				weixin_iv.setImageResource(R.drawable.ljy_fu);
				yhcard_iv.setImageResource(R.drawable.ljy_fu);
			}
			break;
		case R.id.weixin_pay:
			boolean isAlipay2 = (Boolean) weixin_rl.getTag();
			if(isAlipay2){
				return;
			}else{
				alipay_rl.setTag(Boolean.valueOf(false));
				weixin_rl.setTag(Boolean.valueOf(true));
				yhcard_rl.setTag(Boolean.valueOf(false));
				alipay_iv.setImageResource(R.drawable.ljy_fu);
				weixin_iv.setImageResource(R.drawable.ljy_ed);
				yhcard_iv.setImageResource(R.drawable.ljy_fu);
			}
			break;
		case R.id.card_pay:
			boolean isAlipay3 = (Boolean) yhcard_rl.getTag();
			if(isAlipay3){
				return;
			}else{
				alipay_rl.setTag(Boolean.valueOf(false));
				weixin_rl.setTag(Boolean.valueOf(false));
				yhcard_rl.setTag(Boolean.valueOf(true));
				alipay_iv.setImageResource(R.drawable.ljy_fu);
				weixin_iv.setImageResource(R.drawable.ljy_fu);
				yhcard_iv.setImageResource(R.drawable.ljy_ed);
			}
			break;
		case R.id.ljy_order_bootom_right:
			try {
				String ordernumber = "ljy"+System.currentTimeMillis()+"c";
				String sureOrdernumber = URLEncoder.encode(ordernumber,"UTF-8");
				String sureMy_name = URLEncoder.encode(my_name, "UTF-8");
				String sureprice = URLEncoder.encode(order_bootom_price_tx.getText().toString(), "UTF-8");
				url = "http://10.20.73.17:8080/LJYchat/orderlist?name="+sureMy_name+
						"&states=init"+"&price="+sureprice+"&ordernumber="+sureOrdernumber+"&payway=";
				final Intent intent1 = new Intent(AlipayActivity.this,PayActivity.class);
				if((Boolean) alipay_rl.getTag()){
					url = url+"alipay";
					intent1.putExtra("title", "支付宝支付");
				}else if((Boolean) weixin_rl.getTag()){
					url = url+"weixin";
					intent1.putExtra("title", "微信支付");
				}else{
					url = url+"bankvc";
					intent1.putExtra("title", "银行卡支付");
				}
				intent1.putExtra("name", my_name);
				intent1.putExtra("price",order_bootom_price_tx.getText());
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							HttpClient httpClient = new DefaultHttpClient();
							HttpGet get = new HttpGet(url);
							HttpResponse resp = httpClient.execute(get);
							HttpEntity entity = resp.getEntity();
							StringBuilder sb=new StringBuilder();
							if(entity!=null){
								BufferedReader br=new BufferedReader(new InputStreamReader
										(entity.getContent()));
								String line=null;
								while((line=br.readLine())!=null){
									sb.append(line);
								}
							}
							if("success".equals(sb.toString())){
								startActivity(intent1);
							}
							else{
								LiLog.d("create order:"+sb.toString());
								handler.sendEmptyMessage(1);
								return;
							}
						} catch (Exception e) {
						}
					}
				}).start();
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1){
				Toast.makeText(AlipayActivity.this, "创建订单失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
}
