package com.ljy.mychat.ui;

import com.ljy.ljychat.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MoneyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ljy_activity_money);
	}
	
	public void back(View v){
		finish();
	}
	
	public void onPayClick(View v){
		View view = LayoutInflater.from(this).inflate(R.layout.ljy_dialog_inputnumber, null);
		final Button but1 = (Button) view.findViewById(R.id.jiner_1);
		final Button but2 = (Button)view.findViewById(R.id.jiner_2);
		final Button but5 = (Button)view.findViewById(R.id.jiner_5);
		final Button but10 = (Button)view.findViewById(R.id.jiner_10);
		final Button but50 = (Button)view.findViewById(R.id.jiner_50);
		final TextView choose_price = (TextView) view.findViewById(R.id.choose_price);
		but1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				choose_price.setText("选择："+but1.getText());
			}
		});
		but2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				choose_price.setText("选择："+but2.getText());
			}
		});
		but5.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				choose_price.setText("选择："+but5.getText());
			}
		});
		but10.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				choose_price.setText("选择："+but10.getText());
			}
		});
		but50.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				choose_price.setText("选择："+but50.getText());
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择充值金额");
		builder.setView(view);
		builder.setCancelable(false);
		builder.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String price = choose_price.getText().toString();
				String result = price.replace("选择：", "").replace("元", "");
				Intent intent = new Intent(MoneyActivity.this,AlipayActivity.class);
				intent.putExtra("price",result);
				startActivity(intent);
			}
		});
		builder.setNegativeButton("No", null);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
}
