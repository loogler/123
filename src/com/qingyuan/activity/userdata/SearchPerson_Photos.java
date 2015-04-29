package com.qingyuan.activity.userdata;

import org.json.JSONArray;
import org.json.JSONObject;

import com.qingyuan.R;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.HttpUtil;





import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

@SuppressWarnings("deprecation")
public class SearchPerson_Photos extends Activity  {
	private ImageView is;

	public static int imagePosition;//得到用户点击的那个图片序号
	public static String fuid;//获得用户id
	String home_uid;
	String imgUrl[] = new String[20];
	AsyncImageLoader2 asyncImageLoader2 = new AsyncImageLoader2();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchperson_photos);
		SharedPreferences s = getSharedPreferences("userinfo",
				Activity.MODE_PRIVATE);
		home_uid = s.getString("uid", "");

		Log.i("imagePosition", imagePosition + "");
		is = (ImageView) findViewById(R.id.imageView1);
		is.setScaleType(ImageView.ScaleType.FIT_CENTER);
		
		loadData();

	}

	private void loadData() {
		String url = HttpUtil.BASE_URL + "&toType=json&f=space&fuid=" + fuid
				+ "&android_uid=" + home_uid;
		try {
			String res = HttpUtil.getRequest(url);
			JSONObject jsonData = new JSONObject(res);
			if (jsonData.getInt("code") == 1) {
				JSONObject user = jsonData.getJSONObject("result")
						.getJSONObject("fuser");
				JSONArray arr = user.getJSONArray("pics");
				for (int i = 0; i < arr.length(); i++) {
					imgUrl[i] = arr.optJSONObject(i).getString("imgurl");
					Log.i("src", imgUrl[i]);
				}
				
				
				loadImage(HttpUtil.URL + "/" + imgUrl[imagePosition],
						R.id.imageView1);
				Log.i("src", imagePosition+"");
				
				
			}else if (jsonData.getInt("code")==10000) {
				new AlertDialog.Builder(SearchPerson_Photos.this)
				.setTitle("警告")
				.setMessage(jsonData.getString("message"))
				.setPositiveButton("确定", null).show();
			}else {
				Log.e("ERROR", jsonData.getString("message"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadImage(final String url, final int id) {
		// 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
		Drawable cacheImage = asyncImageLoader2.loadDrawable(url,
				new AsyncImageLoader2.ImageCallback() {
					// 请参见实现：如果第一次加载url时下面方法会执行
					public void imageLoaded(Drawable imageDrawable) {
						((ImageView) findViewById(id))
								.setImageDrawable(imageDrawable);
					}
				});
		if (cacheImage != null) {
			((ImageView) findViewById(id)).setImageDrawable(cacheImage);
		}
	}

	

}
