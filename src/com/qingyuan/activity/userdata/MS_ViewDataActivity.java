package com.qingyuan.activity.userdata;

import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import com.qingyuan.R;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.HttpUtil;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("SimpleDateFormat")
public class MS_ViewDataActivity extends Activity implements View.OnClickListener{
	String tag = "MS_ViewDataActivity";
	// layout相关的View
	private TextView username, nickname, place_gender_age, height, school,
			occupation, identity_check, email_check, tel_check, introduce;
	//头像
	private ImageView user_photo;
	//照片墙
	private LinearLayout photos;
	//页卡切换layout
	private FrameLayout framelayout;
	//页卡
	private MS_View_FragmentD mvfd;
	private MS_View_FragmentC mvfc;
	//页卡管理器
	FragmentManager fragmentManager;
	//页卡选择文本，做效果。
	private LinearLayout detail_data,condition_data;
	
	
	ProgressDialog progressDialog;
	private String home_uid;// 用户id
	private String str_nickname, str_uid, str_height, str_school,
			str_occupation, str_identity_check, str_email_check, str_tel_check,
			str_introduce, str_place_gender_age;
	AsyncImageLoader2 asyncImageLoader2 = new AsyncImageLoader2();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ms_viewdataactivity);
		SharedPreferences sp = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		home_uid = sp.getString("uid", null);

		username = (TextView) findViewById(R.id.username);
		nickname = (TextView) findViewById(R.id.nickname);
		place_gender_age = (TextView) findViewById(R.id.place_gender_age);
		height = (TextView) findViewById(R.id.height);
		school = (TextView) findViewById(R.id.school);
		occupation = (TextView) findViewById(R.id.occupation);
		// love_state=(TextView) findViewById(R.id.love_state);
		identity_check = (TextView) findViewById(R.id.identity_check);
		email_check = (TextView) findViewById(R.id.email_check);
		tel_check = (TextView) findViewById(R.id.tel_check);
		introduce = (TextView) findViewById(R.id.introduce);
		user_photo = (ImageView) findViewById(R.id.user_photo);
		
		photos = (LinearLayout) findViewById(R.id.photos);
		
		condition_data=(LinearLayout) findViewById(R.id.condition_data);
		detail_data=(LinearLayout) findViewById(R.id.detail_data);
		framelayout = (FrameLayout) findViewById(R.id.framelayout);
		
		detail_data.setOnClickListener(this);
		condition_data.setOnClickListener(this);
		initView();
		fragmentManager = getFragmentManager();
		  setTabSelection(0);
	}

	/** 加载登录,初始化界面 **/
	void initView() {
		Log.i(tag, "initView()");
		String url = HttpUtil.BASE_URL + "&toType=json&f=user"
				+ "&android_uid=" + home_uid;
		Log.i(tag, "initView+++++++++++++++url" + url);
		try {
			String res = HttpUtil.getRequest(url);
			JSONObject js = new JSONObject(res);
			if (js.getInt("code") == 1) {
				JSONObject user = js.getJSONObject("result");
				str_nickname = user.getString("nickname");
				str_uid = user.getString("uid");
				str_height = user.getString("height");
				str_school = user.getString("education");
				str_occupation = user.getString("occupation");
				// str_love_state=user.getString("love_status");

				str_introduce = user.getString("introduce");
				// String str_province,str_gender,str_birthyear;
				JSONObject cer = user.getJSONObject("cer");
				str_identity_check = cer.getString("identity_check");
				str_email_check = cer.getString("email");
				str_tel_check = cer.getString("telphone");

				loadImage(HttpUtil.URL + "/" + user.getString("pic"),
						R.id.user_photo);
				JSONArray arr = user.getJSONObject("pics").getJSONArray("list");
				// Log.i(tag, arr+"");
				String[] abc = new String[20];
				for (int i = 0; i < arr.length(); i++) {
					abc[i] = arr.optJSONObject(i).getString("imgurl");
				}
				for (int i = 0; i < abc.length; i++) {
					if (abc[i] != null && !abc[i].equals("0")) {
						final ImageView img = new ImageView(this);
						img.setId(i);
						img.setLayoutParams(new LayoutParams(130, 110));
						img.setScaleType(ImageView.ScaleType.FIT_START);
						loadImage(HttpUtil.URL + "/" + abc[i], img.getId());
						photos.addView(img);
						img.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(
										MS_ViewDataActivity.this,
										SearchPerson_Photos.class);
								int id = img.getId();
								SearchPerson_Photos.imagePosition = img.getId();
								SearchPerson_Photos.fuid = home_uid;
								Log.i("photo_id", id + "");
								startActivity(intent);
							}
						});
					}
				}

				if (str_nickname != null && !str_nickname.equals("0")) {
					nickname.setText(str_nickname);
					username.setText(str_nickname + "的个人中心");
				} else {
					nickname.setText(str_uid);
					username.setText(str_uid + "的个人中心");
				}
				if (str_height != null && !str_height.equals("0")) {
					height.setText("身高： " + str_height + "cm");
				} else {
					height.setText("身高： 未知");
				}
				if (str_school != null && !str_school.equals("0")) {
					String b = null;
					String[] a = getResources().getStringArray(
							R.array.educaction);
					for (int i = 0; i < a.length; i++) {
						if (a[i].contains(str_school)) {
							b = a[i].substring(0, a[i].indexOf(","));
						}
					}
					school.setText("学历： " + b);
				} else {
					school.setText("学历： 未知");
				}
				if (str_occupation != null && !str_occupation.equals("0")) {
					String b = null;
					String[] a = getResources().getStringArray(
							R.array.occupation);
					for (int i = 0; i < a.length; i++) {
						if (a[i].contains(str_occupation)) {
							b = a[i].substring(0, a[i].indexOf(","));
						}
					}

					occupation.setText("职业： " + b);
				} else {
					occupation.setText("职业： 未知");
				}
				if (str_introduce != null && !str_introduce.equals("0")) {
					introduce.setText(str_introduce);
				} else {
					introduce.setText("这家伙很懒，什么都没留下");
				}

				String year = user.getString("birthyear");
				SimpleDateFormat sf = new SimpleDateFormat("yyyy");
				String date = sf.format(new java.util.Date());
				String str_birthyear = (Integer.parseInt(date) - Integer
						.parseInt(year)) + "";

				String str_province = null;
				String pro = user.getString("province");
				if (pro != null && !pro.equals("0")) {
					String[] a = getResources()
							.getStringArray(R.array.province);
					for (int i = 0; i < a.length; i++) {
						if (a[i].contains(pro)) {
							str_province = a[i].substring(0, a[i].indexOf(","));
						}
					}
				} else {
					str_province = "";
				}
				String str_gender = null;
				String sex = user.getString("gender");
				if (sex != null && sex.equals("0")) {
					str_gender = "男";
				} else if (sex != null && sex.equals("1")) {
					str_gender = "女";
				}
				place_gender_age.setText(str_province + " " + str_gender + " "
						+ str_birthyear);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 异步加载相册图片
	 * 
	 * */
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


	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.detail_data) {
			setTabSelection(0);
		} else if (id == R.id.condition_data) {
			setTabSelection(1);
		}
	}

	private void setTabSelection(int i) {
		resetBtn();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (i) {
		case 0:// 当点击了消息tab时，改变控件的图片和文字颜色
			detail_data.setBackground(getResources().getDrawable(R.color.red5));
			if (mvfd == null)
			{
				// 如果MessageFragment为空，则创建一个并添加到界面上
				mvfd = new MS_View_FragmentD();
				transaction.add(R.id.framelayout, mvfd);
			} else
			{
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(mvfd);
			}
			
			break;
		case 1:
			condition_data.setBackground(getResources().getDrawable(R.color.red5));
			if (mvfc == null)
			{
				// 如果MessageFragment为空，则创建一个并添加到界面上
				mvfc = new MS_View_FragmentC();
				transaction.add(R.id.framelayout, mvfc);
			} else
			{
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(mvfc);
			}
			break;

		default:
			break;
		}
		transaction.commit();
		
	}

		private void hideFragments(FragmentTransaction transaction)
		{
			if (mvfc != null)
			{
				transaction.hide(mvfc);
			}
			if (mvfd != null)
			{
				transaction.hide(mvfd);
			}
		}
	

	private void resetBtn()
	{
		detail_data.setBackground(getResources().getDrawable(R.color.red1));
		condition_data.setBackground(getResources().getDrawable(R.color.red1));
	}
	
}
