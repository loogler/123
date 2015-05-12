package com.qingyuan.service.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qingyuan.R;
import com.qingyuan.activity.message.ChatActivity;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.AsyncImageLoader2.ImageCallback;
import com.qingyuan.util.HttpUtil;

public class MSG_ChatAdapter extends BaseAdapter {
	public static final int MSG_TYPE_SEND = 0;
	public static final int MSG_TYPE_GET = 1;

	private AsyncImageLoader2 asyncImageLoader2 = new AsyncImageLoader2();
	ViewHoleder holeder;
	Message msg;

	private static String home_pic;
	private Context context;
	private List<ChatEntity> listinfos;
	LayoutInflater inflater;

	public MSG_ChatAdapter(Context context, List<ChatEntity> listinfos) {

		this.context = context;
		this.listinfos = listinfos;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listinfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listinfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	static class ViewHoleder {
		ImageView iv_head;
		ProgressBar pro_loading;
		TextView tv_content;
		TextView tv_time;

	}


	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ChatEntity chatEntity = listinfos.get(position);
		String pic = null;
		String pic_urlSend = HttpUtil.URL + "/" + ChatActivity.home_pic;
		String pic_urlGet = HttpUtil.URL + "/" + ChatActivity.fUser_Pic;

		// 初始化view
		switch (chatEntity.getMsg_type()) {

		case MSG_TYPE_GET: {

			holeder = new ViewHoleder();
			view = inflater.inflate(R.layout.item_msg_chatadapter_get, null);
			holeder.iv_head = (ImageView) view
					.findViewById(R.id.iv_chatadapter_photo);
			holeder.tv_content = (TextView) view
					.findViewById(R.id.tv_chatadapter_content);
			holeder.tv_time = (TextView) view
					.findViewById(R.id.tv_chatadapter_time);
			holeder.pro_loading = (ProgressBar) view
					.findViewById(R.id.pro_chatadapter_loading);

			break;
		}

		case MSG_TYPE_SEND:

		{
			holeder = new ViewHoleder();
			view = inflater.inflate(R.layout.item_msg_chatadapter_sent, null);
			holeder.iv_head = (ImageView) view
					.findViewById(R.id.iv_chatadapter_photos);
			holeder.tv_content = (TextView) view
					.findViewById(R.id.tv_chatadapter_contents);
			holeder.tv_time = (TextView) view
					.findViewById(R.id.tv_chatadapter_times);
			holeder.pro_loading = (ProgressBar) view
					.findViewById(R.id.pro_chatadapter_loadings);

			break;
		}
		default:
			break;
		}

		// 设置view内容

		switch (chatEntity.getMsg_type()) {
		case MSG_TYPE_GET:
			pic = pic_urlGet;
			break;
		case MSG_TYPE_SEND:
			pic = pic_urlSend;
			break;

		default:
			break;
		}
		holeder.iv_head.setImageDrawable(asyncImageLoader2.loadDrawable(pic,
				new ImageCallback() {

					@Override
					public void imageLoaded(Drawable imageDrawable) {
						holeder.iv_head.setImageDrawable(imageDrawable);
					}
				}));

		/*
		 * CharSequence sequence = Html.fromHtml(chatEntity.getMsg_content(),
		 * new Html.ImageGetter() {
		 * 
		 * @Override public Drawable getDrawable(String source) { URL url =
		 * null; Drawable drawable = null; try { url = new URL(source); drawable
		 * = Drawable.createFromStream( url.openStream(), null);
		 * drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
		 * drawable.getIntrinsicHeight());
		 * 
		 * } catch (MalformedURLException e) { e.printStackTrace(); } catch
		 * (IOException e) { e.printStackTrace(); }
		 * 
		 * return drawable; } }, null);
		 */
		// TODO setbounds 无法在正确的位置显示图片；
		/*
		 * 在这里存在一个问题，就是图片的加载过程不受textView初始化时候View大小的限定，即加载前，当前的
		 * textview已经设定好了布局的大小，如果在此时异步加载了图片，该图片是没有被分配空间去存放的，所以导致
		 * 图片的显示会占用其他的组件位置，如果使用matchcontent 显示一行是可以的
		 * 
		 * 如果有时间,在更改当前bug,设置为可以加载gif的形式.
		 */
		URLImageParser p = new URLImageParser(holeder.tv_content, context);
		CharSequence sequence = Html.fromHtml(chatEntity.getMsg_content(), p,
				null);
		holeder.tv_content.setText(sequence);
		holeder.tv_time.setText(chatEntity.getMsg_time());

		return view;

	}

	class URLImageParser implements ImageGetter {

		Context c;
		TextView tv_image;

		public URLImageParser(TextView t, Context c) {
			this.tv_image = t;
			this.c = c;
		}

		@Override
		public Drawable getDrawable(String source) {
			URLDrawable urlDrawable = new URLDrawable();
			ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(
					urlDrawable);
			asyncTask.execute(source);
			return urlDrawable;
		}

		public class ImageGetterAsyncTask extends
				AsyncTask<String, Void, Drawable> {
			URLDrawable urlDrawable;

			public ImageGetterAsyncTask(URLDrawable d) {
				this.urlDrawable = d;
			}

			@Override
			protected void onPostExecute(Drawable result) {
				if (result != null) {
					urlDrawable.setBounds(0, 0, result.getIntrinsicWidth(),
							result.getIntrinsicHeight());
					urlDrawable.drawable = result;
					URLImageParser.this.tv_image.invalidate();
				}
			}

			@Override
			protected Drawable doInBackground(String... params) {
				String source = params[0];// URL
				return fetchDrawable(source);
			}

			//
			public Drawable fetchDrawable(String urlString) {
				try {
					InputStream is = fetch(urlString);
					Drawable drawable = Drawable.createFromResourceStream(
							context.getResources(), null, is, "src", null);
					drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight());
					return drawable;
				} catch (Exception e) {
					return null;
				}
			}

			private InputStream fetch(String urlString)
					throws MalformedURLException, IOException {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet request = new HttpGet(urlString);
				HttpResponse response = httpClient.execute(request);
				return response.getEntity().getContent();
			}

		}

	}

	class URLDrawable extends BitmapDrawable {

		protected Drawable drawable;

		@Override
		public void draw(Canvas canvas) {

			if (drawable != null) {
				drawable.draw(canvas);
			}
		}
	}

	/**
	 * 
	 * @author Administrator 聊天信息与用户信息类
	 */
	public static class ChatEntity {
		private String msg_content;
		private String msg_time;
		private int msg_type;

		public String getMsg_content() {
			return msg_content;
		}

		public void setMsg_content(String msg_content) {
			this.msg_content = msg_content;
		}

		public String getMsg_time() {
			return msg_time;
		}

		public void setMsg_time(String msg_time) {
			this.msg_time = msg_time;
		}

		public int getMsg_type() {
			return msg_type;
		}

		public void setMsg_type(int msg_type) {
			this.msg_type = msg_type;
		}

	}

}
