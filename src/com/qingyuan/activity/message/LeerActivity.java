package com.qingyuan.activity.message;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qingyuan.R;
import com.qingyuan.activity.userdata.SearchPersonActivity;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.HttpUtil;

/**
 * 滑动列表的上拉下啦，左右侧滑功能
 * 
 * @see 意中人
 * @author Administrator
 *
 */
@SuppressLint({ "InflateParams", "HandlerLeak" })
public class LeerActivity extends Activity implements
		OnRefreshListener2<ListView> {

	int pageIndex_get = 1, pageIndex_sent = 1;
	private String home_uid;
	private static final String tag = "LikerActivity";
	Message msg;

	private ViewPager mViewPage;
	private ViewGroup btnLines;

	private Button btn_right, btn_left;
	private Button[] btns;

	private ArrayList<View> pageViews;
	private View leftView, rightView;

	private PullToRefreshListView pullListView_left, pullListView_right;

	ListDataAdapter_Get adapter_ListGet;
	ListDataAdapter_Sent adapter_ListSent;

	private List<UserInfo_Liker> userInfoList_Get = new ArrayList<LeerActivity.UserInfo_Liker>();
	private List<UserInfo_Liker> userInfoList_Sent = new ArrayList<LeerActivity.UserInfo_Liker>();

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.likeractivity);

		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		leftView = inflater.inflate(R.layout.message_left_aty, null);
		rightView = inflater.inflate(R.layout.message_right_aty, null);
		pageViews.add(leftView);
		pageViews.add(rightView);
		pullListView_left = (PullToRefreshListView) leftView
				.findViewById(R.id.pull_refresh_list);
		pullListView_right = (PullToRefreshListView) rightView
				.findViewById(R.id.pull_refresh_list);
		adapter_ListGet = new ListDataAdapter_Get(this, userInfoList_Get);
		adapter_ListSent = new ListDataAdapter_Sent(this, userInfoList_Sent);

		pullListView_left.setAdapter(adapter_ListGet);
		pullListView_right.setAdapter(adapter_ListSent);
		pullListView_left.setOnRefreshListener(LeerActivity.this);
		pullListView_right.setOnRefreshListener(LeerActivity.this);
		pullListView_left.setMode(Mode.BOTH);
		pullListView_right.setMode(Mode.BOTH);

		btnLines = (ViewGroup) inflater.inflate(R.layout.leeractivity, null);
		setContentView(btnLines);

		btns = new Button[pageViews.size()];
		btn_left = (Button) btnLines.findViewById(R.id.btn_left_likeraty);
		btn_right = (Button) btnLines.findViewById(R.id.btn_right_likeraty);
		btn_right.setText("右侧按钮");
		btn_left.setText("左侧按钮");

		btns[0] = btn_left;
		btns[1] = btn_right;
		btn_left.setOnClickListener(new GuideButtonClickListener(0));
		btn_right.setOnClickListener(new GuideButtonClickListener(1));

		mViewPage = (ViewPager) btnLines.findViewById(R.id.viewpager_leer);
		mViewPage.setAdapter(new ListViewPager());
		mViewPage.setOnPageChangeListener(new GuidePageChangeListener());

		SharedPreferences sp = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		home_uid = sp.getString("uid", null);
		loadData(0);
		loadData(1);
	}

	/**
	 * viewpage 切换监听， 留着美化用，切换不同的button换不同颜色
	 * 
	 * @author Administrator
	 *
	 */
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			/*
			 * for (int i = 0; i < btns.length; i++) {
			 * btns[arg0].setBackgroundResource(R.drawable.ic_launcher); if
			 * (arg0 != i) { btns[i]
			 * .setBackgroundResource(android.R.color.holo_red_dark); } }
			 */
		}

	}

	/**
	 * 监听点击事件tab切换 默认 index=0 加载第0页；
	 * 
	 * @author Administrator
	 *
	 */
	class GuideButtonClickListener implements OnClickListener {

		int index = 0;// 默认第一个

		/* the constructor */
		GuideButtonClickListener(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View arg0) {
			mViewPage.setCurrentItem(index, true);
		}

	}

	/**
	 * 翻页适配器
	 * 
	 * @author Administrator
	 *
	 */
	class ListViewPager extends PagerAdapter {

		@Override
		public int getCount() {
			return btns.length;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {

			((ViewPager) container).addView((pageViews).get(position));
			return pageViews.get(position);
		}

		@Override
		public int getItemPosition(Object object) {

			return super.getItemPosition(object);
		}
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
			viewHoler.tv_recent_msg.setText(userinfos.get(position)
					.getUser_info());
			viewHoler.tv_recent_name.setText(userinfos.get(position)
					.getUser_nickname());
			viewHoler.tv_recent_time
					.setText(userinfos.get(position).getCdate());

			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String fuid = userInfoList_Get.get(position).getUid();
					SearchPersonActivity.search_person_fuid = fuid;
					Intent i = new Intent(LeerActivity.this,
							SearchPersonActivity.class);
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
	 * 内容适配器====>sent
	 * 
	 * @author Administrator
	 *
	 */
	class ListDataAdapter_Sent extends BaseAdapter {
		Context context;
		LayoutInflater inflater;
		AsyncImageLoader2 imageLoader2 = new AsyncImageLoader2();
		List<UserInfo_Liker> userinfos;

		public ListDataAdapter_Sent(Context context,
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
			ViewHoler viewHoler = null;
			String url = HttpUtil.URL + "/"
					+ userinfos.get(position).getUser_pic();
			View view = null;

			viewHoler = new ViewHoler();
			view = inflater.inflate(R.layout.item_newsadapter, null);
			viewHoler.iv_recent_avatar = (ImageView) view
					.findViewById(R.id.iv_recent_avatar);
			viewHoler.tv_recent_name = (TextView) view
					.findViewById(R.id.tv_recent_name);
			viewHoler.tv_recent_msg = (TextView) view
					.findViewById(R.id.tv_recent_msg);
			viewHoler.tv_recent_time = (TextView) view
					.findViewById(R.id.tv_recent_time);

			loadImage(url, R.id.iv_recent_avatar, view);
			viewHoler.tv_recent_msg.setText(userinfos.get(position)
					.getUser_info());
			viewHoler.tv_recent_name.setText(userinfos.get(position)
					.getUser_nickname());
			viewHoler.tv_recent_time
					.setText(userinfos.get(position).getCdate());

			view.setTag(viewHoler);
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String fuid = userInfoList_Sent.get(position).getUid();
					SearchPersonActivity.search_person_fuid = fuid;
					Intent i = new Intent(LeerActivity.this,
							SearchPersonActivity.class);
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
			items = new ArrayList<LeerActivity.UserInfo_Liker>();
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

						JSONObject user = arr.optJSONObject(i).getJSONObject(
								"user");
						item.setUser_nickname(user.getString("nickname"));
						item.setUser_pic(user.getString("pic"));

						String province = null, city = null, age;
						String pr = user.getString("province");
						String ci = user.getString("city");
						String[] arr_pro = getResources().getStringArray(
								R.array.province);
						String[] arr_city = getResources().getStringArray(
								R.array.city);
						if (!pr.equals("0") && pr != null) {
							for (int j = 0; j < arr_pro.length; j++) {
								if (arr_pro[j].contains(pr)) {
									province = arr_pro[j].substring(0,
											arr_pro[j].indexOf(","));
									break;
								}
							}
						} else {
							province = " ";
						}
						if (!ci.equals("0") && ci != null) {
							for (int j = 0; j < arr_city.length; j++) {
								if (arr_city[j].contains(ci)) {
									city = arr_city[j].substring(0,
											arr_city[j].indexOf(","));
								}
							}
						} else {
							city = " ";
						}
						int yearnow = Calendar.getInstance().get(Calendar.YEAR);
						int birthyear = user.getInt("birthyear");
						age = (yearnow - birthyear) + "";

						item.setUser_info("来自 " + province + city + "，今年" + age
								+ "岁");
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

	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0: {
				List<UserInfo_Liker> mData = (List<UserInfo_Liker>) msg.obj;
				userInfoList_Get.addAll(mData);
				adapter_ListGet.notifyDataSetChanged();
				pullListView_left.onRefreshComplete();
			}
				break;
			case 1: {
				List<UserInfo_Liker> mData = (List<UserInfo_Liker>) msg.obj;
				userInfoList_Sent.addAll(mData);
				adapter_ListSent.notifyDataSetChanged();
				pullListView_right.onRefreshComplete();
			}

				break;

			default:
				break;
			}

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

		List<UserInfo_Liker> list_info = new ArrayList<LeerActivity.UserInfo_Liker>();
		List<UserInfo_Liker> posters = null;
		XML_Parse_liker parser = new XML_Parse_liker();

		switch (dataType) {

		case 0:
			try {
				res = HttpUtil
						.getRequest(HttpUtil.BASE_URL
								+ "&f=leer&toType=json&type=1" + "&page_size=8"
								+ "&page=" + pageIndex_get + "&android_uid="
								+ home_uid);
			} catch (Exception e) {
				e.printStackTrace();
			}
			msg = handler.obtainMessage(0);
			break;
		case 1:
			try {
				res = HttpUtil.getRequest(HttpUtil.BASE_URL
						+ "&f=leer&toType=json&type=0" + "&page_size=8"
						+ "&page=" + pageIndex_sent + "&android_uid="
						+ home_uid);
			} catch (Exception e) {
				e.printStackTrace();
			}
			msg = handler.obtainMessage(1);
			break;
		default:
			break;
		}
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
					LeerActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(LeerActivity.this, jsonMsg,
									Toast.LENGTH_SHORT).show();
						}
					});

				} else {
					Log.e(tag, "XML_Parser");
					LeerActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(LeerActivity.this,
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

		if (refreshView.equals(pullListView_left)) {

			pageIndex_get = 1;
			userInfoList_Get.clear();
			loadData(0);

		} else {
			// pageViews.remove(rightView);
			pageIndex_sent = 1;
			userInfoList_Sent.clear();
			loadData(1);
		}
		// new DoActionTask(refreshView).execute();

	}

	/**
	 * 上拉加载
	 * 
	 * @args Listview
	 * @param refreshView
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

		if (refreshView.equals(pullListView_left)) {

			pageIndex_get++;
			loadData(0);

		} else {
			pageIndex_sent++;
			loadData(1);

		}

	}

}