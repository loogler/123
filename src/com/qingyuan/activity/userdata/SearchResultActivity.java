package com.qingyuan.activity.userdata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.qingyuan.R;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.CustomProgressDialog;
import com.qingyuan.util.HttpUtil;

public class SearchResultActivity extends Activity {
	private String searchUrlStr, home_uid;
	private PullToRefreshGridView gridView;
	private AsyncImageLoader2 async2 = new AsyncImageLoader2();
	private List<SearchInfo> lists;
	private SearchResultAdapter adapter;
	Message msg;
	int pageIndex = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_searchperson_result);
		SharedPreferences sp = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		searchUrlStr = sp.getString("search_url", null);
		home_uid = sp.getString("uid", null);

		gridView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
		lists = new ArrayList<SearchResultActivity.SearchInfo>();
		new GetInfoThread().start();
		adapter = new SearchResultAdapter(SearchResultActivity.this, lists);
		gridView.setAdapter(adapter);

		gridView.setMode(Mode.PULL_FROM_END);
		gridView.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				pageIndex++;
				new GetInfoThread().start();
			}

		});

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.obj != null) {
				List<SearchInfo> abc = (List<SearchInfo>) msg.obj;
				lists.addAll(abc);
				adapter.notifyDataSetChanged();
				gridView.onRefreshComplete();

			}

		}
	};

	class GetInfoThread extends Thread {
		private SearchInfo info;
		private List<SearchInfo> infos = new ArrayList<SearchResultActivity.SearchInfo>();

		@Override
		public void run() {
			try {
				String url = HttpUtil.getRequest(HttpUtil.BASE_URL
						+ "&f=search&toType=json&android_uid=" + home_uid
						+ searchUrlStr + "&page=" + pageIndex);
				JSONObject js = new JSONObject(url);
				if (js.getInt("code") == 1) {
					JSONObject json = js.getJSONObject("result");
					JSONArray arr = json.getJSONArray("users");
					for (int i = 0; i < arr.length(); i++) {
						info = new SearchInfo();
						String nick = arr.optJSONObject(i)
								.getString("nickname");
						String uid = arr.optJSONObject(i).getString("uid");
						String pro = arr.optJSONObject(i).getString("province");
						String city = arr.optJSONObject(i).getString("city");
						String birthyear = arr.optJSONObject(i).getString(
								"birthyear");
						String pic = arr.optJSONObject(i).getString("pic");
						int year = Integer.parseInt(birthyear);
						Calendar c = Calendar.getInstance();
						int age = c.get(Calendar.YEAR) - year;
						String[] pr = getResources().getStringArray(
								R.array.province);
						String[] ci = getResources().getStringArray(
								R.array.city);
						String a = null, b = null;
						;
						if (pr != null && !pr.equals("0")) {
							for (int j = 0; j < pr.length; j++) {
								if (pr[j].contains(pro)) {
									a = pr[j].substring(0, pr[j].indexOf(","));
								}

							}
						} else {
							a = "未知";
						}
						if (ci != null && !ci.equals("0")) {
							for (int j = 0; j < ci.length; j++) {
								if (ci[j].contains(city)) {
									b = ci[j].substring(0, ci[j].indexOf(","));
									if (b.equals("其他地区")) {
										b = " ";
									}
								}

							}
						} else {
							b = " ";
						}

						info.setfNick(nick);
						info.setfAge(age + "");
						info.setfPic(HttpUtil.URL + "/" + pic);
						info.setfPlace(a + b);
						info.setfUid(uid);
						infos.add(info);

					}
					msg = handler.obtainMessage();
					msg.obj = infos;
					msg.sendToTarget();

				} else if (js.getInt("code") == 10000) {

				} else {
					Log.e("getinfothread ===", "json出错");
				}

			} catch (Exception e) {
			}

		}
	}

	class SearchResultAdapter extends BaseAdapter {
		private Context context;
		private List<SearchInfo> lists;
		private LayoutInflater inflater;

		public SearchResultAdapter(Context context, List<SearchInfo> lists) {
			this.context = context;
			this.lists = lists;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int arg0) {
			return lists.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		String imageUrl;

		@Override
		public View getView(final int position, View view, ViewGroup arg2) {
			imageUrl = lists.get(position).getfPic();
			view = inflater.inflate(R.layout.item_search_result, null);
			ViewHoleder vh = new ViewHoleder();
			vh.head = (ImageView) view.findViewById(R.id.iv_recent_avatar);
			vh.nick = (TextView) view.findViewById(R.id.tv_recent_name);
			vh.info = (TextView) view.findViewById(R.id.tv_recent_msg);
			loadImage(imageUrl, R.id.iv_recent_avatar, view);
			vh.nick.setText(lists.get(position).getfNick() + "(id: "
					+ lists.get(position).getfUid() + ")");
			vh.info.setText(lists.get(position).getfPlace() + " 今年"
					+ lists.get(position).getfAge() + "岁");
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					runOnUiThread(new Runnable() {
						public void run() {
							CustomProgressDialog.createDialog(
									SearchResultActivity.this, "加载中。。。", 2000)
									.show();
						}
					});

					String fuids = lists.get(position).getfUid();
					try {

						if (fuids != null) {

							// 设置fuid
							SearchPersonActivity.search_person_fuid = fuids;
							// 切换页面
							Intent intent = new Intent(
									SearchResultActivity.this,
									SearchPersonActivity.class);
							startActivity(intent);
						} else {
							// fuid传入错误
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(SearchResultActivity.this,
											"对方id无法获取", Toast.LENGTH_SHORT)
											.show();
								}
							});
						}

					} catch (Exception e1) {
						Toast.makeText(SearchResultActivity.this,
								"服务器异常，请稍后再试！", Toast.LENGTH_SHORT).show();
						e1.printStackTrace();
					}

				}
			});

			return view;
		}

		class ViewHoleder {
			ImageView head;
			TextView nick;
			TextView info;

		}

	}

	class SearchInfo {
		private String fNick;
		private String fUid;
		private String fPic;
		private String fAge;
		private String fPlace;

		public String getfNick() {
			return fNick;
		}

		public void setfNick(String fNick) {
			this.fNick = fNick;
		}

		public String getfUid() {
			return fUid;
		}

		public void setfUid(String fUid) {
			this.fUid = fUid;
		}

		public String getfPic() {
			return fPic;
		}

		public void setfPic(String fPic) {
			this.fPic = fPic;
		}

		public String getfAge() {
			return fAge;
		}

		public void setfAge(String fAge) {
			this.fAge = fAge;
		}

		public String getfPlace() {
			return fPlace;
		}

		public void setfPlace(String fPlace) {
			this.fPlace = fPlace;
		}
	}

	/**
	 * 加载图片的方法 （异步）
	 * 
	 * @param url
	 *            传入url
	 * @param id
	 *            控件id
	 * @param view
	 *            View 当前view
	 */
	private void loadImage(final String url, final int id, final View view) {
		Drawable cacheImage = async2.loadDrawable(url,
				new AsyncImageLoader2.ImageCallback() {
					public void imageLoaded(Drawable imageDrawable) {
						((ImageView) view.findViewById(id))
								.setImageDrawable(imageDrawable);
					}
				});
		if (cacheImage != null) {
			((ImageView) view.findViewById(id)).setImageDrawable(cacheImage);
		}
	}

}
