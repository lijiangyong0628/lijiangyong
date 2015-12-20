package com.ljy.mychat.ui;

import com.ljy.ljychat.R;
import com.ljy.mychat.utils.LiLog;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PayActivity extends Activity {
	
	private TextView title;
	private TextView name;
	private TextView price;
	private EditText password_et;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ljy_activity_pay);
		title = (TextView) findViewById(R.id.pay_title);
		name = (TextView) findViewById(R.id.pay_uname_pay);
		price = (TextView) findViewById(R.id.pay_number_pay);
		title.setText(getIntent().getStringExtra("title"));
		name.setText(getIntent().getStringExtra("name"));
		price.setText(getIntent().getStringExtra("price"));
		password_et = (EditText) findViewById(R.id.pay_password);
		password_et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				LiLog.d("start:"+start);
				if(s.length() == 6){
					Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	
	public void back(View v){
		finish();
	}
}
