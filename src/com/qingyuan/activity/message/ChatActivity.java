package com.qingyuan.activity.message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingyuan.R;
import com.qingyuan.service.adapter.MSG_ChatAdapter;
import com.qingyuan.service.adapter.MSG_ChatAdapter.ChatEntity;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.HttpUtil;

/**
 * 聊天主界面
 * 
 * @author Administrator
 * @NOTES 必须传入自己的 id
 * 
 *
 */
@SuppressLint("SimpleDateFormat")
public class ChatActivity extends Activity implements View.OnClickListener {
	private static final String tag = "ChatActivity";
	private static final int MSG_FROM_FUSER = 0;
	private static final int MSG_TO_FUSER = 1;

	public static String talk_fuid;// 传过来的对方uid
	public static String talk_nickname;// 传过来的对方昵称
	public static String fuid, fUser_nick, fUser_Pic;// 聊天对象信息，
	// 需要使用到的几个变量
	private static String send_str, send_time;
	public static String home_uid, home_nick, home_pic;
	private String jsonMsg;
	private int emo_id;
	// 初始化界面
	private ListView lv_content;
	private MSG_ChatAdapter adapter;
	private List<ChatEntity> list_infos = new ArrayList<ChatEntity>();
	// 原始layout布局
	private TextView title;
	private Button btn_emoj, btn_send;
	private EditText edt_input;
	// 底部表情
	private GridView grid_emo;
	private List<String> mKeyList;// 表情list String
	private List<Integer> emo_ListView;//
	private boolean isShowEmo = false;// 判断是否展示表情
	// private Map<String, Integer> mFaceMap;
	private Map<String, Integer> mFaceMap = new HashMap<String, Integer>();
	// 功能型 类
	private AsyncImageLoader2 async2 = new AsyncImageLoader2();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	private InputMethodManager im;

	private Handler h;
	private Message msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_msg_chat);
		SharedPreferences sp = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		home_uid = sp.getString("uid", null);
		home_nick = sp.getString("nickname", null);
		home_pic = sp.getString("pic", null);

		title = (TextView) findViewById(R.id.tv_chat_title);
		btn_emoj = (Button) findViewById(R.id.btn_chat_emo);
		btn_send = (Button) findViewById(R.id.btn_chat_send);
		edt_input = (EditText) findViewById(R.id.edt_chat_input);
		lv_content = (ListView) findViewById(R.id.lv_chat_content);

		grid_emo = (GridView) findViewById(R.id.grid_msg_chat_emo);
		im = (InputMethodManager) getSystemService(ChatActivity.INPUT_METHOD_SERVICE);

		btn_emoj.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		edt_input.setOnClickListener(this);
		if (talk_nickname != null) {
			title.setText("与 " + talk_nickname + " 聊天中");
		} else {
			title.setText("与 " + talk_fuid + " 聊天中");
		}
		new initThread().start();
		initViewPager();// 表情初始化
		adapter = new MSG_ChatAdapter(this, list_infos);
		lv_content.setAdapter(adapter);
		lv_content.setSelection(adapter.getCount() - 1);
		h = new Handler();
		h.postDelayed(new Runnable() {

			@Override
			public void run() {
				new Chat_GetThread().start();
				Log.d(tag, "chat_getThread ++++++++++++跑了一次");
				h.postDelayed(this, 3000);
			}
		}, 3000);

	}

	/****************************************** Gorgeous split line! ********************************************************/
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_FROM_FUSER:
				if (msg.obj != null) {
					List<ChatEntity> abc = (List<ChatEntity>) msg.obj;
					list_infos.addAll(abc);
					adapter.notifyDataSetChanged();
				} else {
					// do nothing !
				}

				break;
			case MSG_TO_FUSER:
				if (msg.obj != null) {
					List<ChatEntity> abc = (List<ChatEntity>) msg.obj;
					list_infos.addAll(abc);
					adapter.notifyDataSetChanged();
					lv_content.setSelection(adapter.getCount() - 1);
					send_str = "";
					send_time = "";

				}

				break;

			default:
				break;

			}

		};
	};

	/**
	 * 消息接收线程---定时刷新/action=READ/
	 * 
	 * @author Administrator
	 *
	 */
	class Chat_GetThread extends Thread {

		@Override
		public void run() {
			ChatEntity list_info = null;
			List<ChatEntity> lists = new ArrayList<MSG_ChatAdapter.ChatEntity>();
			try {
				String res = HttpUtil.getRequest(HttpUtil.BASE_URL
						+ "&f=chat&action=READ&toType=json&android_uid="
						+ home_uid + "&fuid=" + talk_fuid);
				try {
					JSONObject js = new JSONObject(res);
					jsonMsg = js.getString("message");
					if (js.getInt("code") == 1) {
						JSONObject json = js.getJSONObject("result");
						JSONObject jsonUser = json.getJSONObject("user");// 用户信息
						JSONArray arr = json.getJSONArray("list");
						fuid = jsonUser.getString("uid");
						fUser_nick = jsonUser.getString("nickname");
						fUser_Pic = jsonUser.getString("pic");
						for (int i = 0; i < arr.length(); i++) {
							list_info = new MSG_ChatAdapter.ChatEntity();
							list_info.setMsg_content(arr.optJSONObject(i).getString(
									"content"));
							long t = arr.optJSONObject(i).getInt("cdate");
							list_info.setMsg_time(sdf.format(t * 1000));
							list_info.setMsg_type(MSG_ChatAdapter.MSG_TYPE_GET);
							lists.add(list_info);

						}

						msg = handler.obtainMessage(MSG_FROM_FUSER);
						msg.obj = lists;
						msg.sendToTarget();
					} else if (js.getInt("code") == 10000) {
						// 空消息，无需提示

					} else {
						// 错误了
						Log.e(tag, jsonMsg + "code>10000");

					}

				} catch (Exception e) {
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 初始化界面，1、判断是否可以聊天，2、查询历史聊天记录。
	 */

	class initThread extends Thread {

		@Override
		public void run() {
			ChatEntity list_info = null;
			List<ChatEntity> lists = new ArrayList<MSG_ChatAdapter.ChatEntity>();
			try {
				String res = HttpUtil.getRequest(HttpUtil.BASE_URL
						+ "&f=chat&toType=json&android_uid=" + home_uid
						+ "&fuid=" + talk_fuid);
				try {
					JSONObject js = new JSONObject(res);
					jsonMsg = js.getString("message");
					if (js.getInt("code") == 1) {
						JSONObject json = js.getJSONObject("result");
						JSONObject jsonData = json.getJSONObject("hisMsg");// 消息内容
						JSONObject jsonUser = json.getJSONObject("fUser");// 用户信息
						JSONArray arr = jsonData.getJSONArray("list");
						fuid = jsonUser.getString("uid");
						fUser_nick = jsonUser.getString("nickname");
						fUser_Pic = jsonUser.getString("pic");
						for (int i = 0; i < arr.length(); i++) {
							list_info = new MSG_ChatAdapter.ChatEntity();
							list_info.setMsg_content(arr.optJSONObject(i)
									.getString("content"));
							long t = arr.optJSONObject(i).getInt("cdate");
							list_info.setMsg_time(sdf.format(t * 1000));
							list_info.setMsg_type(MSG_ChatAdapter.MSG_TYPE_GET);
							lists.add(list_info);

						}

						msg = handler.obtainMessage(MSG_FROM_FUSER);
						msg.obj = lists;
						msg.sendToTarget();
					} else if (js.getInt("code") == 10000) {
						// 空消息，无需提示

					} else {
						// 错误了
						Log.e(tag, jsonMsg + "code>10000");

					}

				} catch (Exception e) {
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 消息发送线程，---emo/text
	 * 
	 * @author Administrator
	 *
	 */
	class Chat_SendThread extends Thread {
		String str = null;

		public void run() {
			List<ChatEntity> items = new ArrayList<ChatEntity>();
			ChatEntity item = null;
			Map<String, String> rawParams = new HashMap<String, String>();
			// send_str = edt_input.getText().toString().trim();
			String content = Html.toHtml(edt_input.getText());
			send_time = sdf.format(new java.util.Date());

			String zhengze = "src=\"(\\d+)\"";
			Pattern pattern = Pattern.compile(zhengze);
			Matcher matcher = pattern.matcher(content);
			while (matcher.find()) {
				String keyWord = matcher.group(1);// 获取匹配的id内容R.id.xxx对应的静态值
				int keyWord_Int = Integer.parseInt(keyWord);// 得到int类型的id值

				// 通过values反向取key ，添加http地址，得到该图片在网页上的地址，使其可以在网页和后台被查看。
				Iterator<String> iterator = mFaceMap.keySet().iterator();
				while (iterator.hasNext()) {
					String keyString = iterator.next();
					if (mFaceMap.get(keyString).equals(keyWord_Int)) {

						String content_Url = "http://www.07919.com/module/chat/images/"
								+ keyString;
						// str = content.replaceAll(keyWord,
						// Matcher.quoteReplacement(content_Url));
						str = content.replaceFirst(keyWord,
								Matcher.quoteReplacement(content_Url));
						content = str;
						break;
					}

				}

			}
			send_str = content;
			rawParams.put("fuid", talk_fuid);
			rawParams.put("content", send_str);

			try {
				String res = HttpUtil.postRequest(HttpUtil.BASE_URL
						+ "&f=chat&toType=json&action=SEND&android_uid="
						+ home_uid, rawParams);
				JSONObject js = new JSONObject(res);
				jsonMsg = js.getString("message");
				if (js.getInt("code") == 1) {
					item = new ChatEntity();
					item.setMsg_content(send_str);
					item.setMsg_time(send_time);
					item.setMsg_type(MSG_ChatAdapter.MSG_TYPE_SEND);
					items.add(item);
					send_str = "";
					send_time = "";

					msg = handler.obtainMessage(MSG_TO_FUSER);
					msg.obj = items;
					msg.sendToTarget();

					runOnUiThread(new Runnable() {
						// 完成时 初始化变量。更改界面 。
						public void run() {
							edt_input.setText("");

						}
					});

				} else if (js.getInt("code") == 10000) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(ChatActivity.this, "发送失败" + jsonMsg,
									Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					Log.e(tag, "send code>10000////" + jsonMsg);
					// 消息为空时，直接报code=10001
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(ChatActivity.this, "发送失败" + jsonMsg,
									Toast.LENGTH_SHORT).show();
						}
					});
				}

			} catch (Exception e) {
				Log.e(tag, "json--catch exception");
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(ChatActivity.this, "网络不稳定，请检查网络",
								Toast.LENGTH_SHORT).show();
					}
				});
			}

		}
	}

	/**
	 * 获取接口数据的办法
	 * 
	 * @param resUrl
	 *            接口地址
	 * @param list_info
	 *            数据填充对象 ChantEntity；
	 * @param lists
	 *            arraylist,集合
	 */
	void getData(String resUrl, ChatEntity list_info, List<ChatEntity> lists) {

	}

	/*********************************** Gorgeous split line ****************************************************/

	/**
	 * 加载表情的gridview
	 */
	private void initViewPager() {

		mFaceMap = Chat_EmotionApplication.getInstance().getFaceMap();
		Set<String> keySet = mFaceMap.keySet();
		mKeyList = new ArrayList<String>();
		mKeyList.addAll(keySet);
		Collection<Integer> valueSet = mFaceMap.values();
		emo_ListView = new ArrayList<Integer>();
		emo_ListView.addAll(valueSet);

		grid_emo.setAdapter(new Chat_Emo_Adapter(getApplicationContext(),
				emo_ListView));
		grid_emo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View convertView,
					int position, long ids) {
				emo_id = emo_ListView.get(position);

				Log.d(tag, emo_id + "");
				// 逆向查找map<K,V> 中K！
				Iterator<String> iterator = mFaceMap.keySet().iterator();
				while (iterator.hasNext()) {
					String keyString = iterator.next();
					if (mFaceMap.get(keyString).equals(emo_id)) {
						CharSequence sequence = Html.fromHtml("<img src="
								+ emo_id + ">", new Html.ImageGetter() {

							@Override
							public Drawable getDrawable(String arg0) {
								Drawable drawable = getResources().getDrawable(
										emo_id);
								drawable.setBounds(0, 0,
										drawable.getIntrinsicWidth(),
										drawable.getIntrinsicHeight());
								return drawable;
							}
						}, null);
						edt_input.append(sequence);
					}

				}
			}
		});

	}

	/**
	 * 表情图标适配
	 * 
	 * @author Administrator
	 *
	 */
	class Chat_Emo_Adapter extends BaseAdapter {

		private List<Integer> listViews;
		private Context context;
		private LayoutInflater inflater;

		public Chat_Emo_Adapter(Context context, List<Integer> listViews) {
			this.context = context;
			this.listViews = listViews;
			inflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			return listViews.size();
		}

		@Override
		public Object getItem(int arg0) {
			return listViews.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		class Emo_holder {
			ImageView faceIV;
		}

		Bitmap bm = null;

		@Override
		public View getView(int position, View view, ViewGroup parent) {

			int resId = listViews.get(position);
			bm = BitmapFactory.decodeResource(context.getResources(), resId);
			Emo_holder viewHolder = null;
			if (view == null) {
				viewHolder = new Emo_holder();

				view = inflater.inflate(R.layout.item_msg_chat_emoj, null);
				viewHolder.faceIV = (ImageView) view.findViewById(R.id.face_iv);
				view.setTag(viewHolder);
			} else {
				viewHolder = (Emo_holder) view.getTag();
			}
			viewHolder.faceIV.setImageBitmap(bm);

			return view;
		}

	}

	/*********************************** Gorgeous split line ****************************************************/

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击图片按钮，呈现图片选择。
		case R.id.btn_chat_emo:

			isShowEmo = true;

			if (isShowEmo) {
				// 强制隐藏软键盘
				im.hideSoftInputFromWindow(v.getWindowToken(), 0);
				grid_emo.setVisibility(View.VISIBLE);
				isShowEmo = false;

			} else {
				im.showSoftInput(v, InputMethodManager.SHOW_FORCED);
				grid_emo.setVisibility(View.GONE);
				isShowEmo = true;

			}

			break;
		case R.id.btn_chat_send:

			new Chat_SendThread().start();

			break;
		case R.id.edt_chat_input:
			if (!isShowEmo) {
				im.hideSoftInputFromWindow(v.getWindowToken(), 0);
				grid_emo.setVisibility(View.VISIBLE);
				isShowEmo = true;
			} else {
				im.showSoftInput(v, InputMethodManager.SHOW_FORCED);
				grid_emo.setVisibility(View.GONE);
				isShowEmo = false;
			}

		default:
			break;
		}

	}

	/**
	 * 退出时终止定时操作。禁止刷新接口，
	 */
	@Override
	protected void onDestroy() {
		h.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

}
