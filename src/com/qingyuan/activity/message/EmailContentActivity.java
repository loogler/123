package com.qingyuan.activity.message;

import java.text.SimpleDateFormat;

import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qingyuan.R;
import com.qingyuan.util.HttpUtil;

public class EmailContentActivity extends Activity implements
		android.view.View.OnClickListener {

	String tag = "EmailContentActivity";
	String messageId, home_uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_msg_email_content);
		SharedPreferences sp = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		home_uid = sp.getString("uid", null);
		messageId=(String) getIntent().getSerializableExtra("messageId");

		TextView email_title = (TextView) findViewById(R.id.email_content_title);
		TextView email_content = (TextView) findViewById(R.id.email_content);
		TextView email_content_usernick = (TextView) findViewById(R.id.email_content_usernick);
		TextView email_content_sendtime = (TextView) findViewById(R.id.email_content_sendtime);

		Button replybtn = (Button) findViewById(R.id.email_content_replybtn);
		Button returnbtn = (Button) findViewById(R.id.email_content_returnbtn);
		Button deletebtn = (Button) findViewById(R.id.email_content_deletebtn);
		replybtn.setOnClickListener(this);
		returnbtn.setOnClickListener(this);
		deletebtn.setOnClickListener(this);

		try {
			String res = null;
			JSONObject json;
			res = HttpUtil.getRequest(HttpUtil.BASE_URL
					+ "&f=email&toType=json&id=" + messageId + "&android_uid="
					+ home_uid);

			if (res != null) {
				json = new JSONObject(res);
				if (json.getInt("code") == 1) {
					String content = json.getJSONObject("result").getString("content");
					Log.i(tag, "content+++++" + content);
					email_content.setText(content);
					email_title.setText(json.getJSONObject("result").getString("title"));
					if (json.getJSONObject("result").getJSONObject("user")
							.getString("nickname") != null) {

						email_content_usernick.setText(json.getJSONObject("result")
								.getJSONObject("user").getString("nickname")
								+ "发来的邮件");
					} else {
						email_content_usernick.setText(json.getJSONObject("result")
								.getString("fuid") + "发来的邮件");
					}
					long time = json.getJSONObject("result").getInt("cdate");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					email_content_sendtime.setText(sdf.format(time * 1000));

				} else if (json.getInt("code") == 10000) {
					Toast.makeText(EmailContentActivity.this,
							json.getString("message"), Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(EmailContentActivity.this, "服务器异常，请稍后。。",
							Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			Toast.makeText(EmailContentActivity.this, "服务器异常，请稍后。。",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

		

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.email_content_replybtn:

			break;
		case R.id.email_content_returnbtn:

			break;
		case R.id.email_content_deletebtn:

			break;

		default:
			break;
		}
	}

}
