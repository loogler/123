package com.qingyuan;

import java.io.IOException;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qingyuan.activity.message.ChatActivity;
import com.qingyuan.activity.message.CommissionActivity;
import com.qingyuan.activity.message.EmailActivity;
import com.qingyuan.activity.message.EmailQingYuanActivity;
import com.qingyuan.activity.message.GiftActivity;
import com.qingyuan.activity.message.LeerActivity;
import com.qingyuan.activity.message.LikerActivity;
import com.qingyuan.activity.message.VisitedActivity;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.CustomProgressDialog;
import com.qingyuan.util.HttpUtil;

public class TabNewsActivity extends Activity implements
		OnRefreshListener<ListView> {
	private static final String tag = "TabNewsActivity";

	private PullToRefreshListView pullListView;
	private ListDataAdapter_News adapter_ListNews;
	private List<UserInfo_News> userInfoList_News = new ArrayList<UserInfo_News>();

	private String home_uid;
	private Message msg;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_tabnews);
		SharedPreferences sp = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		home_uid = sp.getString("uid", null);

		pullListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		adapter_ListNews = new ListDataAdapter_News(this, userInfoList_News);

		loadData(0);
		pullListView.setAdapter(adapter_ListNews);
		pullListView.setOnRefreshListener(TabNewsActivity.this);

	}

	/**
	 * 内容适配器====>news
	 * 
	 * @author Administrator
	 *
	 */
	class ListDataAdapter_News extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private AsyncImageLoader2 imageLoader2 = new AsyncImageLoader2();
		private List<UserInfo_News> userinfos;

		public ListDataAdapter_News(Context context,
				List<UserInfo_News> userInfos) {
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
			TextView tv_recent_unread;
		}

		@SuppressLint({ "ViewHolder", "InflateParams" })
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			String url = HttpUtil.URL + "/"
					+ userinfos.get(position).getFuser_pic();
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
			viewHoler.tv_recent_unread = (TextView) view
					.findViewById(R.id.tv_recent_unread);

			loadImage(url, R.id.iv_recent_avatar, view);
			viewHoler.tv_recent_msg.setText(userInfoList_News.get(position)
					.getContent());
			viewHoler.tv_recent_name.setText(userInfoList_News.get(position)
					.getTitle());
			viewHoler.tv_recent_time.setText(userInfoList_News.get(position)
					.getTime());
			viewHoler.tv_recent_unread.setText(userInfoList_News.get(position)
					.getMsgNum());
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					CustomProgressDialog.createDialog(TabNewsActivity.this,
							"加载中，请稍后。。。", 500).show();

					if (userInfoList_News.get(position).getMsgTag()
							.equals("会员邮件")) {
						Intent intent = new Intent(TabNewsActivity.this,
								EmailActivity.class);
						startActivity(intent);
					} else if (userInfoList_News.get(position).getMsgTag()
							.equals("红娘邮件")) {
						Intent intent = new Intent(TabNewsActivity.this,
								EmailQingYuanActivity.class);
						startActivity(intent);

					} else if (userInfoList_News.get(position).getMsgTag()
							.equals("访问")) {
						Intent intent = new Intent(TabNewsActivity.this,
								VisitedActivity.class);
						startActivity(intent);
					} else if (userInfoList_News.get(position).getMsgTag()
							.equals("聊天")) {

						ChatActivity.talk_fuid = userInfoList_News.get(
								position).getFuid();
						ChatActivity.talk_nickname = userInfoList_News.get(
								position).getFuser_nickname();

						Intent intent = new Intent(TabNewsActivity.this,
								ChatActivity.class);
						startActivity(intent);
					} else if (userInfoList_News.get(position).getMsgTag()
							.equals("委托")) {
						Intent intent = new Intent(TabNewsActivity.this,
								CommissionActivity.class);
						startActivity(intent);
					} else if (userInfoList_News.get(position).getMsgTag()
							.equals("秋波")) {
						Intent intent = new Intent(TabNewsActivity.this,
								LeerActivity.class);
						startActivity(intent);
					} else if (userInfoList_News.get(position).getMsgTag()
							.equals("礼物")) {
						Intent intent = new Intent(TabNewsActivity.this,
								GiftActivity.class);
						startActivity(intent);
					} else if (userInfoList_News.get(position).getMsgTag()
							.equals("意中人")) {
						Intent intent = new Intent(TabNewsActivity.this,
								LikerActivity.class);
						startActivity(intent);
					}

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
	class UserInfo_News {
		private String fuid;// 对方id
		private String uid;// 自己id
		private String time;// 发送时间
		private String id;// 消息id
		private String fuser_nickname;// 对方信息 nick
		private String fuser_pic;// 聊天头像
		private String title;// 标题
		private String content;// 消息内荣
		private String msgTag;// biaoqian

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

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getFuser_nickname() {
			return fuser_nickname;
		}

		public void setFuser_nickname(String fuser_nickname) {
			this.fuser_nickname = fuser_nickname;
		}

		public String getFuser_pic() {
			return fuser_pic;
		}

		public void setFuser_pic(String fuser_pic) {
			this.fuser_pic = fuser_pic;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getMsgNum() {
			return msgNum;
		}

		public void setMsgNum(String msgNum) {
			this.msgNum = msgNum;
		}

		public String getMsgTag() {
			return msgTag;
		}

		public void setMsgTag(String msgTag) {
			this.msgTag = msgTag;
		}

		private String msgNum;// 消息数量

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
		@SuppressWarnings("null")
		@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
		public List<UserInfo_News> parserLiker(String res) {
			List<UserInfo_News> items = new ArrayList<TabNewsActivity.UserInfo_News>();
			UserInfo_News item = null;
			JSONObject json = null;
			items = new ArrayList<UserInfo_News>();
			try {
				json = new JSONObject(res);
				JSONObject js = json.getJSONObject("result");
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd  HH:mm:ss");
				// email
				if (js.getJSONObject("email").getInt("num") != 0) {
					JSONArray arr = js.getJSONObject("email").getJSONArray(
							"rows");
					for (int i = 0; i < arr.length(); i++) {

						if (arr.optJSONObject(i).getString("fuid").equals("0")) {// hongniang
							item = new UserInfo_News();
							item.setTitle("红娘来信");
							item.setContent(arr.optJSONObject(i).getString(
									"title"));
							item.setMsgNum("新");
							long time = arr.optJSONObject(i).getInt("time");
							item.setTime(sdf.format(time * 1000) + "");
							item.setMsgTag("红娘邮件");
							// item.setFuser_pic(); pic 为默认
							items.add(item);
						}

						if (!arr.optJSONObject(i).getString("fuid").equals("0")) {
							item = new UserInfo_News();
							item.setTitle("会员来信");
							item.setContent(arr.optJSONObject(i).getString(
									"title"));
							item.setMsgNum("新");
							item.setMsgTag("会员邮件");
							long time = arr.optJSONObject(i).getInt("time");
							item.setTime(sdf.format(time * 1000) + "");
							items.add(item);

						}

					}

				} else {
					// no message!!
				}
				// visited
				if (js.getJSONObject("visited").getInt("num") != 0) {
					JSONArray arr = js.getJSONObject("visited").getJSONArray(
							"rows");
					item = new UserInfo_News();

					item.setContent("快去看看谁来过");
					item.setTitle("访问记录");
					long time = arr.optJSONObject(0).getInt("time");
					item.setTime(sdf.format(time * 1000) + "");
					item.setMsgNum(arr.length() + "");
					item.setMsgTag("访问");
					items.add(item);

				} else {
					// no new visited
				}
				// commission
				if (js.getJSONObject("commission").getInt("num") != 0) {
					JSONArray arr = js.getJSONObject("commission")
							.getJSONArray("rows");
					item = new UserInfo_News();

					item.setContent("委托红娘获得幸福");
					item.setTitle("委托记录");
					item.setMsgNum(arr.length() + "");
					long time = arr.optJSONObject(0).getInt("time");
					item.setTime(sdf.format(time * 1000) + "");
					item.setMsgTag("委托");
					items.add(item);
				} else {
					// no commission
				}
				// leer
				if (js.getJSONObject("leer").getInt("num") != 0) {
					JSONArray arr = js.getJSONObject("leer").getJSONArray(
							"rows");
					item = new UserInfo_News();

					item.setContent("新的秋波");
					item.setTitle("秋波记录");
					item.setMsgNum(arr.length() + "");
					long time = arr.optJSONObject(0).getInt("time");
					item.setTime(sdf.format(time * 1000) + "");
					item.setMsgTag("秋波");
					items.add(item);
				} else {
					// no leer !!
				}
				// gift
				if (js.getJSONObject("gift").getInt("num") != 0) {
					JSONArray arr = js.getJSONObject("gift").getJSONArray(
							"rows");
					item = new UserInfo_News();

					item.setContent("新的礼物");
					item.setTitle("礼物记录");
					item.setMsgNum(arr.length() + "");
					long time = arr.optJSONObject(0).getInt("time");
					item.setTime(sdf.format(time * 1000) + "");
					item.setMsgTag("礼物");
					items.add(item);
				} else {
					// no gift !!
				}

				// friend

				if (js.getJSONObject("friend").getInt("num") != 0) {
					JSONArray arr = js.getJSONObject("friend").getJSONArray(
							"rows");
					item = new UserInfo_News();

					item.setContent("新的意中人");
					item.setTitle("意中人记录");
					item.setMsgNum(arr.length() + "");
					long time = arr.optJSONObject(0).getInt("time");
					item.setTime(sdf.format(time * 1000) + "");
					item.setMsgTag("意中人");
					items.add(item);
				} else {
					// no friend!!
				}
				if (js.getJSONObject("chat").getInt("num") != 0) {
					JSONArray arr = js.getJSONObject("chat").getJSONArray(
							"rows");

					for (int i = 0; i < arr.length(); i++) {

						item = new UserInfo_News();
						item.setTitle(arr.optJSONObject(i)
								.getJSONObject("user").getString("nickname"));
						item.setFuid(arr.optJSONObject(i).getString("fuid"));
						item.setContent(arr.optJSONObject(i).getString(
								"content"));
						item.setMsgNum("新");
						item.setMsgTag("聊天");
						item.setFuser_pic(arr.optJSONObject(i)
								.getJSONObject("user").getString("pic"));
						long time = arr.optJSONObject(i).getInt("time");
						item.setFuser_nickname(arr.optJSONObject(i).getString("nickname"));
						
						item.setTime(sdf.format(time * 1000));
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
			List<UserInfo_News> mData = (List<UserInfo_News>) msg.obj;
			userInfoList_News.addAll(mData);
			adapter_ListNews.notifyDataSetChanged();
			pullListView.onRefreshComplete();

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

		List<UserInfo_News> list_info = new ArrayList<UserInfo_News>();
		List<UserInfo_News> posters = null;
		XML_Parse_liker parser = new XML_Parse_liker();

		try {
			res = HttpUtil.getRequest(HttpUtil.BASE_URL
					+ "&f=message&toType=json&android_uid=" + home_uid);
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
					for (UserInfo_News userInfo_Liker : posters) {
						list_info.add(userInfo_Liker);
					}
				} else if (json.getInt("code") == 10000) {
					TabNewsActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(TabNewsActivity.this, jsonMsg,
									Toast.LENGTH_SHORT).show();
						}
					});

				} else {
					Log.e(tag, "XML_Parser");
					TabNewsActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(TabNewsActivity.this,
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

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		userInfoList_News.clear();
		loadData(0);
	}

}
