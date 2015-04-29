package com.qingyuan.activity.message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingyuan.R;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.HttpUtil;

/**
 * 聊天主界面
 * 
 * @author Administrator
 * @NOTES 必须传入自己的
 *
 */
@SuppressLint("SimpleDateFormat")
public class ChatActivity extends Activity implements View.OnClickListener {
	private static final String tag = "ChatActivity";
	private static final int MSG_FROM_FUSER = 0;
	private static final int MSG_TO_FUSER = 1;

	private Message msg;
	public static String talk_fuid;// 传过来的对方uid
	public static String talk_nickname;// 传过来的对方昵称

	private String send_str, send_time;
	private String home_uid, home_nick, home_pic;

	private TextView title;
	private Button btn_emoj, btn_send;
	private EditText edt_input;
	private ViewPager vp_emoj;// 表情

	private ListView lv_content;
	private Chat_Adapter adapter;
	private List<ChatEntity> list_infos = new ArrayList<ChatActivity.ChatEntity>();
	private List<ChatEntity> list_info = null;

	private AsyncImageLoader2 async2 = new AsyncImageLoader2();

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
		vp_emoj = (ViewPager) findViewById(R.id.vp_chat_emo);
		lv_content = (ListView) findViewById(R.id.lv_chat_content);

		btn_emoj.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		if (talk_nickname != null) {
			title.setText("与 " + talk_nickname + " 聊天中");

		} else {
			title.setText("与 " + talk_fuid + " 聊天中");
		}

		new Chat_GetThread().start();
		adapter = new Chat_Adapter(this, list_infos);
		lv_content.setAdapter(adapter);

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_FROM_FUSER:
				if (msg.obj != null) {
					List<ChatEntity> abc = (List<ChatEntity>) msg.obj;
					abc.addAll(list_infos);
				}

				break;
			case MSG_TO_FUSER:

				break;

			default:
				break;

			}

			adapter.notifyDataSetChanged();
		};
	};

	class Chat_GetThread extends Thread {
		List<ChatEntity> items = new ArrayList<ChatActivity.ChatEntity>();
		ChatEntity item = null;
		String jsonMsg;

		@Override
		public void run() {

			try {
				String res = HttpUtil.getRequest(HttpUtil.BASE_URL
						+ "&f=chat&toType=json&&action=READ&android_uid="
						+ home_uid + "&fuid=" + talk_fuid);
				JSONObject js = new JSONObject(res);
				jsonMsg = js.getString("message");
				if (js.getInt("code") == 1) {
					JSONArray arr = js.getJSONObject("result")
							.getJSONObject("hisMsg").getJSONArray("list");
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd  HH:mm:ss");
					for (int i = 0; i < arr.length(); i++) {
						item = new ChatEntity();
						item.setMSG_Come(true);
						item.setFuser_content(arr.optJSONObject(i).getString(
								"content"));
						item.setFuser_nick(talk_nickname);
						item.setFuser_pic(js.getJSONObject("result")
								.getJSONObject("fUser").getString("pic"));
						long time = arr.optJSONObject(i).getInt("cdate");
						item.setFuser_time(sdf.format(time * 1000));
						items.add(item);
					}
					msg = handler.obtainMessage(MSG_FROM_FUSER);
					msg.obj = items;
					msg.sendToTarget();

				} else if (js.getInt("code") == 10000) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(ChatActivity.this, jsonMsg,
									Toast.LENGTH_SHORT).show();
						}
					});

				} else if (js.getInt("code") == 10001) {
					Log.e(tag, "code>10000");
				} else {
					Log.e(tag, "code无返回值");
				}

			} catch (Exception e) {
			}

		}
	}

	/**
	 * 
	 * @author Administrator adapter 适配器
	 */
	protected class Chat_Adapter extends BaseAdapter {
		Context context;
		List<ChatEntity> listInfos;
		LayoutInflater inflater;

		public Chat_Adapter(Context context, List<ChatEntity> listInfos) {
			this.context = context;
			this.listInfos = listInfos;
			inflater = LayoutInflater.from(this.context);
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getCount() {
			return listInfos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		private class ViewHolder {
			private ImageView iv_photo;
			private TextView tv_time;
			private TextView tv_content;

		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ChatEntity entity = listInfos.get(position);

			String url_get = HttpUtil.URL + "/"
					+ listInfos.get(position).getFuser_pic();
			String url_send = HttpUtil.URL + "/" + home_pic;
			ViewHolder vh = null;
			if (view == null) {
				if (entity.isMSG_Come()) {
					// come msg
					view = inflater.inflate(R.layout.item_msg_chatadapter_get,
							null);
					vh = new ViewHolder();
					vh.iv_photo = (ImageView) view
							.findViewById(R.id.iv_chatadapter_photo);
					vh.tv_content = (TextView) view
							.findViewById(R.id.tv_chatadapter_content);
					vh.tv_time = (TextView) view
							.findViewById(R.id.tv_chatadapter_time);

					loadImage(url_get, R.id.iv_chatadapter_photo, view);
					vh.tv_content.setText(listInfos.get(position)
							.getFuser_content());
					vh.tv_time.setText(listInfos.get(position).getFuser_time());
					view.setTag(vh);

				} else {
					// send msg
					view = inflater.inflate(R.layout.item_msg_chatadapter_sent,
							null);
					vh = new ViewHolder();
					vh.iv_photo = (ImageView) view
							.findViewById(R.id.iv_chatadapter_photo);
					vh.tv_content = (TextView) view
							.findViewById(R.id.tv_chatadapter_content);
					vh.tv_time = (TextView) view
							.findViewById(R.id.tv_chatadapter_time);
					
					loadImage(url_send, R.id.iv_chatadapter_photo, view);
					vh.tv_content.setText(listInfos.get(position)
							.getSend_content());
					vh.tv_time.setText(listInfos.get(position).getSend_time());
					view.setTag(vh);
				}

			} else {
				vh = (ViewHolder) view.getTag();
			}

			return view;

		}

		private void loadImage(final String url, final int id, final View view) {
			// 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
			Drawable cacheImage = async2.loadDrawable(url,
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
	 * @author Administrator 聊天信息与用户信息类
	 */
	protected class ChatEntity {
		private String fuid;
		private String fuser_nick;
		private String fuser_pic;

		private String fuser_time;
		private String fuser_content;

		private String send_time;
		private String send_content;
		private String user_pic;

		private String MSG_Id;
		private boolean MSG_Come;

		public String getFuid() {
			return fuid;
		}

		public void setFuid(String fuid) {
			this.fuid = fuid;
		}

		public String getFuser_nick() {
			return fuser_nick;
		}

		public void setFuser_nick(String fuser_nick) {
			this.fuser_nick = fuser_nick;
		}

		public String getFuser_pic() {
			return fuser_pic;
		}

		public void setFuser_pic(String fuser_pic) {
			this.fuser_pic = fuser_pic;
		}

		public String getFuser_time() {
			return fuser_time;
		}

		public void setFuser_time(String fuser_time) {
			this.fuser_time = fuser_time;
		}

		public String getFuser_content() {
			return fuser_content;
		}

		public void setFuser_content(String fuser_content) {
			this.fuser_content = fuser_content;
		}

		public String getSend_time() {
			return send_time;
		}

		public void setSend_time(String send_time) {
			this.send_time = send_time;
		}

		public String getSend_content() {
			return send_content;
		}

		public void setSend_content(String send_content) {
			this.send_content = send_content;
		}

		public String getUser_pic() {
			return user_pic;
		}

		public void setUser_pic(String user_pic) {
			this.user_pic = user_pic;
		}

		public String getMSG_Id() {
			return MSG_Id;
		}

		public void setMSG_Id(String mSG_Id) {
			MSG_Id = mSG_Id;
		}

		public boolean isMSG_Come() {
			return MSG_Come;
		}

		public void setMSG_Come(boolean mSG_Type) {
			MSG_Come = mSG_Type;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_chat_emo:

			break;
		case R.id.btn_chat_send:

			break;

		default:
			break;
		}

	}

}
