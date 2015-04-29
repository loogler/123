package com.qingyuan.activity.userdata;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.qingyuan.R;
import com.qingyuan.util.CustomProgressDialog;
import com.qingyuan.util.HttpUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MS_IntroduceActivity extends Activity {
	String tag = "MS_IntroduceActivity";

	private String home_uid;// 用户id
	private String introduce, introduce_pass, introduce_check;
	private EditText edit_introduce;// //独白内容
	private TextView txt_save, txt_prompt;// 保存按钮/独白状态检查

	private CustomProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ms_introduceactivity);
		SharedPreferences sp = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		home_uid = sp.getString("uid", null);

		edit_introduce = (EditText) findViewById(R.id.introduceText);
		txt_save = (TextView) findViewById(R.id.txt_saveintroduce);
		txt_prompt = (TextView) findViewById(R.id.txt_introduceprompt);
		txt_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (edit_introduce.getText().toString().trim().length() < 30
						|| edit_introduce.getText().toString().trim().length() > 5000) {
					Toast.makeText(getApplicationContext(),
							"内心独白长度应在30-5000字之间", Toast.LENGTH_SHORT).show();
				} else {
					new Thread(saveIntroduce).start();
				}
			}
		});
		progressDialog = CustomProgressDialog.createDialog(MS_IntroduceActivity.this, "载入中...");
		progressDialog.show();
		new Thread(checkIntroduce).start();
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				Log.i(tag, "msg.what==1");
				try {
					JSONObject js = new JSONObject(msg.obj.toString());
					if (js.getInt("code") == 1) {
						Toast.makeText(getApplicationContext(), "提交成功，等待审核。",
								Toast.LENGTH_LONG).show();
						MS_IntroduceActivity.this.finish();
						
					} else if (js.getInt("code") == 10000) {
						Toast.makeText(getApplicationContext(),
								"提交失败" + js.getString("message"),
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(), "服务器异常，请稍后。",
								Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else if (msg.what == 2) {
				Log.i(tag, "msg.what==2");
				try {
					JSONObject js= new JSONObject(msg.obj.toString());
					if (js.getInt("code")==1) {
						JSONObject res=js.getJSONObject("result");
						introduce_pass= res.getString("introduce_pass");
						introduce=res.getString("introduce");
						introduce_check = res.getString("introduce_check");
						if(introduce_pass.equals("0")){
							edit_introduce.setText(introduce_check);
							edit_introduce.setEnabled(true);
							txt_prompt.setText("内心独白正在审核中...");
							txt_prompt.setTextColor(Color.parseColor("#999999"));
							txt_save.setClickable(true);
						}else if(introduce_pass.equals("1")){
							edit_introduce.setText(introduce);
							txt_prompt.setText("内心独白审核通过");
							txt_prompt.setTextColor(Color.parseColor("#000000"));
						}else if(introduce_pass.equals("2")){
							edit_introduce.setText(introduce_check);
							txt_prompt.setText("内心独白审核未通过");
							txt_prompt.setTextColor(Color.parseColor("#FF0000"));
						}
					} else if (js.getInt("code") == 10000) {
						Toast.makeText(getApplicationContext(), js.getString("message"), Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(getApplicationContext(), "服务器异常，请稍后再试", Toast.LENGTH_SHORT).show();
					}
						
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				
			}

		}

	};

	Thread saveIntroduce = new Thread() {
		@Override
		public void run() {
			super.run();
			Message msg = new Message();
			Map<String, String> params = new HashMap<String, String>();
			params.put("introduce", edit_introduce.getText().toString());
			try {
				String url = HttpUtil.postRequest(HttpUtil.BASE_URL
						+ "&f=update&toType=json&android_uid=" + home_uid,
						params);
				msg.what = 1;
				msg.obj = url;
				progressDialog.dismiss();
				handler.sendMessage(msg);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	};
	Thread checkIntroduce = new Thread() {
		@Override
		public void run() {
			super.run();
			Message msg = new Message();
			try {
				String url = HttpUtil.getRequest(HttpUtil.BASE_URL
						+ "&f=user&toType=json&android_uid=" + home_uid);
				msg.what = 2;
				msg.obj = url;
				progressDialog.dismiss();
				handler.sendMessage(msg);
			} catch (Exception e) {
			}
		}

	};

}
