package com.qingyuan.activity.userdata;

import org.json.JSONObject;

import com.qingyuan.R;
import com.qingyuan.util.HttpUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MS_PurseActivity extends Activity {
	String tag = "MS_PurseAcitivity";
	// xml布局变量
	private TextView money, score;
	private LinearLayout purse_ui;
	// 用户数据,搜索唯一条件
	public static String home_uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ms_purseactivity);
		// init
		SharedPreferences sp = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		home_uid = sp.getString("uid", "");
		money = (TextView) findViewById(R.id.money);
		score = (TextView) findViewById(R.id.score);
		purse_ui = (LinearLayout) findViewById(R.id.purse_ui);

		loadData();

	}

	private void loadData() {
		MS_PurseActivity.this.runOnUiThread(new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.getRequest(HttpUtil.BASE_URL
							+ "&toType=json&f=purse&android_uid=" + home_uid);
					JSONObject js = new JSONObject(url);
					int code = js.getInt("code");
					if (code == 1) {
						money.setText("余额："
								+ js.getJSONObject("result").getString("money")
								+ " 元");
						score.setText("积分： "
								+ js.getJSONObject("result")
										.getString("points") + " 分");
					} else if (code == 10000) {
						Toast.makeText(getApplicationContext(),
								js.getString("message"), Toast.LENGTH_SHORT);
					} else {
						Toast.makeText(getApplicationContext(), "服务器异常，请稍候再试",
								Toast.LENGTH_SHORT).show();
						Log.e(tag, "取不到值或者断网");
						MS_PurseActivity.this.finish();
					}

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "服务器异常，请稍候再试",
							Toast.LENGTH_SHORT).show();
					MS_PurseActivity.this.finish();
					e.printStackTrace();
				}
			}
		}));
	}
}
