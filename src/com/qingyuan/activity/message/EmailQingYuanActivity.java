package com.qingyuan.activity.message;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qingyuan.R;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.CustomProgressDialog;
import com.qingyuan.util.HttpUtil;

public class EmailQingYuanActivity extends Activity implements
		OnRefreshListener2<ListView> {
	private PullToRefreshListView pullListView_left;

	ListDataAdapter_Get adapter_ListGet;

	private List<UserInfo_Liker> userInfoList_Get = new ArrayList<UserInfo_Liker>();

	private String home_uid;
	// private String url;

	private Message msg;
	String tag = "EmailQingYuanActivity";
	private int pageIndex = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_msg_email_qingyuan);
		SharedPreferences sp = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		home_uid = sp.getString("uid", null);

		

		pullListView_left = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		adapter_ListGet = new ListDataAdapter_Get(this, userInfoList_Get);

		pullListView_left.setAdapter(adapter_ListGet);
		pullListView_left.setOnRefreshListener(EmailQingYuanActivity.this);
		pullListView_left.setMode(Mode.BOTH);

		loadData(0);

	}

	/**
	 * 内容适配器====>get
	 * 
	 * @author Administrator
	 *
	 */
	class ListDataAdapter_Get extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private AsyncImageLoader2 imageLoader2 = new AsyncImageLoader2();
		private List<UserInfo_Liker> userinfos;

		public ListDataAdapter_Get(Context context,
				List<UserInfo_Liker> userInfos) {
			this.context = context;
			inflater = LayoutInflater.from(this.context);
			this.userinfos = userInfos;
		}

		@Override
		public int getCount() {
			return userinfos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		class ViewHoler {
			ImageView iv_recent_avatar;
			TextView tv_recent_name;
			TextView tv_recent_msg;
			TextView tv_recent_time;
		}

		@SuppressLint({ "ViewHolder", "InflateParams" })
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			String url = HttpUtil.URL + "/"
					+ userinfos.get(position).getUser_pic();
			Log.d(tag, "url==========" + url);
			View view = null;

			view = inflater.inflate(R.layout.item_newsadapter, null);
			ViewHoler viewHoler = new ViewHoler();
			viewHoler.iv_recent_avatar = (ImageView) view
					.findViewById(R.id.iv_recent_avatar);
			viewHoler.tv_recent_name = (TextView) view
					.findViewById(R.id.tv_recent_name);
			viewHoler.tv_recent_msg = (TextView) view
					.findViewById(R.id.tv_recent_msg);
			viewHoler.tv_recent_time = (TextView) view
					.findViewById(R.id.tv_recent_time);

			loadImage(url, R.id.iv_recent_avatar, view);
			viewHoler.tv_recent_msg.setText(userInfoList_Get.get(position).getTitle());
			viewHoler.tv_recent_name.setText("情缘来信");
			viewHoler.tv_recent_time
					.setText(userInfoList_Get.get(position).getCdate());

			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					CustomProgressDialog.createDialog(EmailQingYuanActivity.this,
							"加载中。。。", 2000).show();
					String messageId = userInfoList_Get.get(position).getId();
					Intent i = new Intent(EmailQingYuanActivity.this, EmailContentActivity.class);
					
					Bundle bundle = new Bundle();
					bundle.putSerializable("messageId", (Serializable) messageId);
					i.putExtras(bundle);
					startActivity(i);
				}
			});
			return view;
		}

		/**
		 * 
		 * @param url
		 *            ==>img路径
		 * @param id
		 *            res路径，将img摆放于id位置，
		 * @param view
		 *            父容器
		 */
		private void loadImage(final String url, final int id, final View view) {
			// 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
			Drawable cacheImage = imageLoader2.loadDrawable(url,
					new AsyncImageLoader2.ImageCallback() {
						// 请参见实现：如果第一次加载url时下面方法会执行
						public void imageLoaded(Drawable imageDrawable) {
							((ImageView) view.findViewById(id))
									.setImageDrawable(imageDrawable);
						}
					});
			if (cacheImage != null) {
				((ImageView) view.findViewById(id))
						.setImageDrawable(cacheImage);
			}
		}

	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	class UserInfo_Liker {
		private String fuid;
		private String uid;
		private String cdate;
		private String id;
		private String is_read;
		private String user_nickname;
		private String user_pic;
		private String user_info;
		private String title;

		public String getFuid() {
			return fuid;
		}

		public void setFuid(String fuid) {
			this.fuid = fuid;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getCdate() {
			return cdate;
		}

		public void setCdate(String cdate) {
			this.cdate = cdate;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getIs_read() {
			return is_read;
		}

		public void setIs_read(String is_read) {
			this.is_read = is_read;
		}

		public String getUser_nickname() {
			return user_nickname;
		}

		public void setUser_nickname(String user_nickname) {
			this.user_nickname = user_nickname;
		}

		public String getUser_pic() {
			return user_pic;
		}

		public void setUser_pic(String user_pic) {
			this.user_pic = user_pic;
		}

		public String getUser_info() {
			return user_info;
		}

		public void setUser_info(String user_info) {
			this.user_info = user_info;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

	/**
	 * 
	 * @author Administrator 解析器
	 */
	class XML_Parse_liker {
		public XML_Parse_liker() {

		}

		public String getXmlFromUrl(String url) {

			String xml = null;
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				xml = EntityUtils.toString(httpEntity, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return xml;
		}

		public String getStrFromUrl(String url) {
			String str = null;
			try {
				str = HttpUtil.getRequest(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return str;
		}

		/**
		 * type !=1; 取收到的
		 * 
		 * @param res
		 * @return
		 */
		@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
		public List<UserInfo_Liker> parserLiker(String res) {
			List<UserInfo_Liker> items = null;
			UserInfo_Liker item = null;
			JSONObject json = null;
			items = new ArrayList<UserInfo_Liker>();
			try {
				json = new JSONObject(res);
				JSONObject jsonData = json.getJSONObject("result");
				int num = jsonData.getInt("total");
				JSONArray arr = jsonData.getJSONArray("list");
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd  HH:mm:ss");
				if (num > 0) {
					for (int i = 0; i < num; i++) {
						item = new UserInfo_Liker();
						item.setFuid(arr.optJSONObject(i).getString("fuid")); // 自己的id
						item.setId(arr.optJSONObject(i).getString("id"));
						item.setIs_read(arr.optJSONObject(i).getString(
								"status"));
						item.setUid(arr.optJSONObject(i).getString("uid"));// 对方的id
						long time = arr.optJSONObject(i).getInt("cdate");
						item.setCdate(sdf.format(time * 1000));
						item.setTitle(arr.optJSONObject(i).getString("title"));
						
						items.add(item);
					}
				}

			} catch (JSONException jsonException) {
				jsonException.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return items;

		}

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			@SuppressWarnings("unchecked")
			List<UserInfo_Liker> mData = (List<UserInfo_Liker>) msg.obj;
			userInfoList_Get.addAll(mData);
			adapter_ListGet.notifyDataSetChanged();
			pullListView_left.onRefreshComplete();

		};
	};

	private String res;
	private String jsonMsg;

	/**
	 * 
	 * @param dataType
	 *            数据类型 get/sent
	 */
	public void loadData(int dataType) {

		List<UserInfo_Liker> list_info = new ArrayList<UserInfo_Liker>();
		List<UserInfo_Liker> posters = null;
		XML_Parse_liker parser = new XML_Parse_liker();

		try {
			res = HttpUtil.getRequest(HttpUtil.BASE_URL
					+ "&f=email&toType=json&type=2" + "&page_size=8" + "&page="
					+ pageIndex + "&android_uid=" + home_uid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		msg = handler.obtainMessage(0);

		try {

			if (res != null) {

				JSONObject json = new JSONObject(res);
				jsonMsg = json.getString("message");
				if (json.getInt("code") == 1) {
					posters = parser.parserLiker(res);
					for (UserInfo_Liker userInfo_Liker : posters) {
						list_info.add(userInfo_Liker);
					}
				} else if (json.getInt("code") == 10000) {
					EmailQingYuanActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(EmailQingYuanActivity.this, jsonMsg,
									Toast.LENGTH_SHORT).show();
						}
					});

				} else {
					Log.e(tag, "XML_Parser");
					EmailQingYuanActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(EmailQingYuanActivity.this,
									jsonMsg + ",尝试下拉刷新", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
			} else {
				Log.e(tag, "res+++++ > null");
			}

			msg.obj = list_info;
			msg.sendToTarget();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 实现下拉刷新
	 * 
	 * @args Listview
	 * @param refreshView
	 */

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

		pageIndex = 1;
		userInfoList_Get.clear();
		loadData(0);

	}

	/**
	 * 上拉加载
	 * 
	 * @args Listview
	 * @param refreshView
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

		pageIndex++;
		loadData(0);

	}

	

}
