package com.qingyuan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.qingyuan.activity.userdata.SearchPersonActivity;
import com.qingyuan.listadapter_view.L_Search;
import com.qingyuan.service.parser.XMLParser_Search;
import com.qingyuan.util.CustomProgressDialog;
import com.qingyuan.util.HttpUtil;
import com.qingyuan.util.TableItem_Search;
import com.qingyuan.util.pulldown.PullDownView;
import com.qingyuan.util.pulldown.PullDownView.OnPullDownListener;

public class TabRecommendActivity extends Activity implements
		OnPullDownListener, OnItemClickListener {

	private static final int WHAT_DID_LOAD_DATA = 0;// 页面初始化时，加载数据
	// private static final int WHAT_DID_REFRESH = 1;// 下拉刷新数据
	private static final int WHAT_DID_MORE = 2;// 上划加载更多数据

	private ListView mListView;// 资讯栏目列表
	private L_Search mAdapter;// 列表适配器

	private PullDownView mPullDownView;// 自定义下拉，上划view
	private List<TableItem_Search> search_Strings = new ArrayList<TableItem_Search>();// 更新列表的数据集合

	private String xml = "";// 存储XML数据

	private List<TableItem_Search> posters = null;// 解析出的资讯栏目集合
	private int pageIndex = 1;// 加载的页面的页码
	private Message msg;// 向Handler发送的消息
	String httpUrl, home_uid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_tabrecommend);

		SharedPreferences preferences = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		home_uid = preferences.getString("uid", "0");
		httpUrl = preferences.getString("search_url", "");

		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mAdapter = new L_Search(TabRecommendActivity.this, search_Strings,
				mPullDownView);
		mListView.setAdapter(mAdapter);
		mPullDownView.enableAutoFetchMore(true, 1);

		loadData(0);
	}// onCreate(Bundle savedInstanceState)括号的另一边

	/************************* 请×××叫×××我×××分×××割×××线 *************************/
	// 数据加载
	// @param type
	// 判断是上划加载更多type= 1、下拉更新：type=2、数据初始化：type=0

	private void loadData(final int type) {
		final XMLParser_Search parser = new XMLParser_Search();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				List<TableItem_Search> strings = null;
				strings = new ArrayList<TableItem_Search>();
				switch (type) {
				case 0:
					msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
					break;
				case 1:
					msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
					break;

				default:
					break;
				}
				// 获取XML数据
				// url加上页码
				String url = HttpUtil.BASE_URL
						+ "&f=recommend&toType=json&android_uid=" + home_uid
						+ "&page=" + pageIndex;

				xml = parser.getXmlFromUrl(url);
				try {
					JSONObject json = new JSONObject(xml);
					final String message = json.getString("message");
					int code = json.getInt("code");
					if (code == 10000) {
						new AlertDialog.Builder(TabRecommendActivity.this)
								.setTitle("提示").setMessage(message)
								.setPositiveButton("确定", new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										TabRecommendActivity.this.finish();
									}
								}).create().show();
					} else if (code == 1) {

					} else if (code > 10000) {
						Log.e("接口错误", message);
						TabRecommendActivity.this.finish();
					} else {
						Log.i("json模块出问题了", message);
					}
				} catch (JSONException e1) {
					new AlertDialog.Builder(TabRecommendActivity.this)
							.setTitle("提示：").setMessage("服务器异常，请稍候再试！")
							.setPositiveButton("确定", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									TabRecommendActivity.this.finish();
								}
							}).create().show();
					e1.printStackTrace();
				}
				try {
					posters = formatTableItem(xml);

					for (TableItem_Search body : posters) {
						strings.add(body);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				msg.obj = strings;
				msg.sendToTarget();
			}
		}).start();
	}

	// 刷新
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pageIndex = 1;
				loadData(2);
			}
		}).start();
	}

	// 加载更多
	public void onMore() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pageIndex++;
				loadData(1);
			}
		}).start();
	}

	// 更新页面数据（只有加载更多有用）
	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA: {
				if (msg.obj != null) {
					List<TableItem_Search> strings = (List<TableItem_Search>) msg.obj;
					if (!strings.isEmpty()) {
						search_Strings.addAll(strings);
						mAdapter.notifyDataSetChanged();
					}
				}
				mAdapter.notifyDataSetChanged();
				// 诉它数据加载完毕;
				mPullDownView.notifyDidLoad();
				break;
			}

			case WHAT_DID_MORE: {
				List<TableItem_Search> strings = (List<TableItem_Search>) msg.obj;
				if (!strings.isEmpty()) {
					search_Strings.addAll(strings);
					mAdapter.notifyDataSetChanged();
				}
				// 告诉它获取更多完毕
				mPullDownView.notifyDidMore();
				break;
			}
			}
		}
	};

	/************************* 请×××叫×××我×××分×××割×××线 *************************/
	// item被点中的事件
	// 进入TA的个人主页
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final CustomProgressDialog pro;
		pro = CustomProgressDialog.createDialog(TabRecommendActivity.this,
				"加载中....");
		pro.show();

		// uid传递给SearchPersonActivity
		SearchPersonActivity.search_person_fuid = L_Search.search_lists.get(
				position).getTableId();
		// 打开SearchPersonActivity
		Intent intent = new Intent(TabRecommendActivity.this,
				SearchPersonActivity.class);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				pro.dismiss();
			}
		}, 500);
		startActivity(intent);

		// 不关闭这个Activity,一会还要返回

		/*
		 * //显示此人其他消息 new AlertDialog.Builder(TabRecommendActivity.this)
		 * .setTitle("")
		 * .setMessage(ListAdapter_Search.search_lists.get(position
		 * ).getTableId()) .setPositiveButton("确定",null) .show();
		 */
	}

	/************************* 请×××叫×××我×××分×××割×××线 *************************/
	// 真机按键的效果
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// 返回主界面
			TabRecommendActivity.this.finish();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// new AlertDialog.Builder(SearchResultActivity.this)
			// .setTitle("")
			// .setMessage("你点中了MENU，可惜我没写Dialog")
			// .setPositiveButton("确定",null)
			// .show();
		}
		return super.onKeyDown(keyCode, event);
	}

	public List<TableItem_Search> formatTableItem(String str) {
		List<TableItem_Search> items = new ArrayList<TableItem_Search>();
		try {
			JSONObject json = new JSONObject(str);
			if (json.getInt("code") == 1) {
				TableItem_Search item = null;
				JSONArray array = json.getJSONObject("result").getJSONArray(
						"list");
				for (int i = 0; i < array.length(); i++) {
					item = new TableItem_Search();
					String[] arrays = null;
					JSONObject user = array.optJSONObject(i);
					item.setTableIntroduce(user.getString("introduce"));
					item.setTableId(user.getString("uid"));
					item.setTableNickname(user.getString("nickname"));
					item.setTableBirthyear(user.getString("birthyear"));
					item.setTablePic(user.getString("pic"));
					item.setTableHeight(user.getString("height"));
					item.setTableGender(user.getString("gender"));
					if (user.getString("gender").equals("0")) {
						item.setTableGender("男");
					} else {
						item.setTableGender("女");
					}
					if (user.getJSONObject("members_choice")
							.getString("gender").equals("0")) {
						item.setTableChioceGender("男");
					} else {
						item.setTableChioceGender("女");
					}
					item.setTableChioceMinAge(user.getJSONObject(
							"members_choice").getString("age1"));
					item.setTableChioceMaxAge(user.getJSONObject(
							"members_choice").getString("age2"));
					item.setTableLoveStatus(user.getString("love_status"));
					if (user.getString("education").equals("0")) {
						item.setTableEducation("未知");
					} else {
						arrays = getResources().getStringArray(
								R.array.educaction);
						for (int j = 0; j < arrays.length; j++) {
							if (arrays[j].contains(user.getString("education"))) {
								item.setTableEducation(arrays[j].substring(0,
										arrays[j].indexOf(",")));
								break;
							}
						}
					}
					if (user.getString("province").equals("0")) {
						item.setTableProvince("未知");
					} else {
						arrays = getResources()
								.getStringArray(R.array.province);
						for (int j = 0; j < arrays.length; j++) {
							if (arrays[j].contains(user.getString("province"))) {
								item.setTableProvince(arrays[j].substring(0,
										arrays[j].indexOf(",")));
								break;
							}
						}
					}
					if (user.getString("city").equals("0")) {
						item.setTableCity("未知");
					} else {
						arrays = getResources().getStringArray(R.array.city);
						for (int j = 0; j < arrays.length; j++) {
							if (arrays[j].contains(user.getString("city"))) {
								item.setTableCity(arrays[j].substring(0,
										arrays[j].indexOf(",")));
								break;
							}
						}
					}
					if (user.getString("introduce").equals("0")) {
						item.setTableIntroduce("");

					} else {
						item.setTableIntroduce(user.getString("introduce"));
					}
					Log.i("introduce", user.getString("introduce"));
					arrays = null;
					items.add(item);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return items;
	}

}// public class SearchResultActivity extends Activity括号的另一边
