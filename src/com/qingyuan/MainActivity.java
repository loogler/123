package com.qingyuan;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.qingyuan.util.HttpUtil;
import com.qingyuan.util.User;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

@SuppressWarnings("deprecation")
@SuppressLint({ "SimpleDateFormat", "InflateParams" })
public class MainActivity extends Activity {

	private LinearLayout feedbackLayout; // 意见反馈VIEW
	public static String home_nickname, home_uid, home_gender, check_date,
			home_pic,home_cid;// 用户资料
			// private boolean is_check;// 检查更新，何时检查
	private User user;// User 类的实例化

	int index = 1;// 当前页卡编号
	ViewPager viewPage;// 页卡内容
	private List<View> listViews;// Tab页面列表
	private RadioGroup radioGroup;// 底部栏
	private LocalActivityManager manager = null;
	private MyPagerAdapter mpAdapter = null;
	PopupMenu popup = null;

	@Override
	protected void onStart() {
		Log.i("", "onStart()");
		super.onStart();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.i("", "onNewIntent()");
		setIntent(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	}

	@Override
	public void onBackPressed() {
		Log.i("", "onBackPressed()");
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		Log.i("", "onPause()");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.i("", "onStop()");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.i("", "onDestroy()");
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (getIntent() != null) {
			// 这个才是默认页面的决定因素
			index = getIntent().getIntExtra("index", 0);
			viewPage.setCurrentItem(index);
			setIntent(null);
		} else {
			if (index < 3) {
				index = index + 1;
				viewPage.setCurrentItem(index);
				index = index - 1;
				viewPage.setCurrentItem(index);

			} else if (index == 3) {
				index = index - 1;
				viewPage.setCurrentItem(index);
				index = index + 1;
				viewPage.setCurrentItem(index);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sharedPreferences();// 读取保存的几个数据
		init(savedInstanceState);// 初始化界面
	}

	/*
	 * 缺少部分：1）自动检查并提示更新 2）消息提示部分，有会员动态时收到提示查看
	 */

	public void sharedPreferences() {
		// 读取保存的几个数据
		SharedPreferences preferences = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		home_pic = preferences.getString("pic", null);
		home_nickname = preferences.getString("nickname", "");
		home_uid = preferences.getString("uid", "");
		home_gender = preferences.getString("gender", "");
		// is_check = preferences.getBoolean("ischeck", false);
		home_cid=preferences.getString("cid", null);
		check_date = preferences.getString("checkdate", null);
		user = new User();
		user.formatUserFromPreferences(preferences);
	}

	public void init(Bundle savedInstanceState) {
		viewPage = (ViewPager) findViewById(R.id.vPager);
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		// 初始化ViewPager
		initViewPager();

		viewPage.setCurrentItem(0);
		viewPage.setOnPageChangeListener(new MyOnPageChangeListener());
		radioGroup = (RadioGroup) this.findViewById(R.id.radiogroup);
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.recommend) {
							index = 0;
							listViews.set(
									0,
									getView("A", new Intent(MainActivity.this,
											TabRecommendActivity.class)));
							mpAdapter.notifyDataSetChanged();
							viewPage.setCurrentItem(0);
						} else if (checkedId == R.id.news) {
							index = 1;
							listViews.set(
									1,
									getView("B", new Intent(MainActivity.this,
											TabNewsActivity.class)));
							mpAdapter.notifyDataSetChanged();
							viewPage.setCurrentItem(1);
						} else if (checkedId == R.id.others) {
							index = 2;
							listViews.set(
									2,
									getView("C", new Intent(MainActivity.this,
											TabOthersActivity.class)));
							mpAdapter.notifyDataSetChanged();
							viewPage.setCurrentItem(2);
						} else if (checkedId == R.id.myself) {
							index = 3;
							listViews.set(
									3,
									getView("D", new Intent(MainActivity.this,
											TabMyselfActivity.class)));
							mpAdapter.notifyDataSetChanged();
							viewPage.setCurrentItem(3);
						}
					}
				});

	}

	private void initViewPager() {
		Intent intent = null;
		listViews = new ArrayList<View>();
		mpAdapter = new MyPagerAdapter(listViews);
		intent = new Intent(MainActivity.this, TabRecommendActivity.class);
		listViews.add(getView("A", intent));
		intent = new Intent(MainActivity.this, TabNewsActivity.class);
		listViews.add(getView("B", intent));
		intent = new Intent(MainActivity.this, TabOthersActivity.class);
		listViews.add(getView("C", intent));
		intent = new Intent(MainActivity.this, TabMyselfActivity.class);
		listViews.add(getView("D", intent));
		viewPage.setOffscreenPageLimit(0);
		viewPage.setAdapter(mpAdapter);

		viewPage.setCurrentItem(0);
		viewPage.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	/***
	 * pageadapter 适配器，
	 *
	 * @link initViewPager();
	 ************************************************************************/
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * RadioGroup 监听切换事件
	 * 
	 * @link initViewPager();
	 * **************************************************************************************/
	public class MyOnPageChangeListener implements OnPageChangeListener {

		public void onPageSelected(int arg0) {
			manager.dispatchResume();
			switch (arg0) {
			case 0:
				index = 0;
				radioGroup.check(R.id.recommend);
				listViews.set(
						0,
						getView("A", new Intent(MainActivity.this,
								TabRecommendActivity.class)));
				mpAdapter.notifyDataSetChanged();
				break;
			case 1:
				index = 1;
				radioGroup.check(R.id.news);
				listViews.set(
						1,
						getView("B", new Intent(MainActivity.this,
								TabNewsActivity.class)));
				mpAdapter.notifyDataSetChanged();
				break;
			case 2:
				index = 2;
				radioGroup.check(R.id.others);
				listViews.set(
						2,
						getView("C", new Intent(MainActivity.this,
								TabOthersActivity.class)));
				mpAdapter.notifyDataSetChanged();
				break;
			case 3:
				index = 3;
				radioGroup.check(R.id.myself);
				listViews.set(
						3,
						getView("D", new Intent(MainActivity.this,
								TabMyselfActivity.class)));
				mpAdapter.notifyDataSetChanged();
				break;

			}
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/** 真机按键，菜单键--返回键--home键 ******************************************************************/

	// 返回键
	// home键默认

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// 如果此前没有按back键，不退出
			if (System.currentTimeMillis() - exitTime > 2000) {
				Toast.makeText(MainActivity.this, "再按一次返回桌面", Toast.LENGTH_LONG)
						.show();
				exitTime = System.currentTimeMillis();
			} else {
				/*
				 * //退出程序的方法 finish(); System.exit(0);
				 */

				// 返回桌面的方法，相当于按一次HOME
				Intent home = new Intent(Intent.ACTION_MAIN);
				home.addCategory(Intent.CATEGORY_HOME);
				startActivity(home);

			}
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	// 菜单键
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = new MenuInflater(this);
		menuInflater.inflate(R.menu.my_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem menuItem) {
		int itemId = menuItem.getItemId();

		// 第一个检查更新
		if (itemId == R.id.check_update) {
			try {
				if (isUpdate()) {
					new AlertDialog.Builder(this)
							.setTitle("升级提示")
							.setMessage("确定升级到最新版本？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											String url = HttpUtil.URL
													+ "/android/index.php";
											Intent intent = new Intent(
													Intent.ACTION_VIEW);
											intent.setData(Uri.parse(url));
											startActivity(intent);
										}
									}).setNegativeButton("取消", null).create()
							.show();
				} else {
					Toast.makeText(this, "已是最新版本！", Toast.LENGTH_LONG);
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// /提交意见反馈按钮

		} else if (itemId == R.id.feedback) {
			feedbackLayout = (LinearLayout) LayoutInflater.from(this).inflate(
					R.layout.mainactivity_feedback, null);
			new AlertDialog.Builder(this).setTitle("意见反馈")
					.setView(feedbackLayout)
					.setPositiveButton("提交", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							MainActivity.this.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub

									EditText contentText = (EditText) feedbackLayout
											.findViewById(R.id.contentText);
									Map<String, String> param = new HashMap<String, String>();
									param.put("message1", contentText.getText()
											.toString().trim());
									param.put("type1", "3");
									param.put("complaint_type", "1");
									try {
										String res = HttpUtil
												.postRequest(
														HttpUtil.BASE_URL
																+ "&f=feedback&toType=json&android_uid="
																+ user.getUid(),
														param);
										JSONObject json = new JSONObject(res);
										int code = json.getInt("code");
										if (code == 1) {
											Toast.makeText(
													getApplicationContext(),
													"谢谢您的反馈，我们会及时跟进！",
													Toast.LENGTH_LONG).show();
										} else {
											Toast.makeText(
													getApplicationContext(),
													json.getString("message"),
													Toast.LENGTH_LONG).show();
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										Toast.makeText(getApplicationContext(),
												"服务器异常，请稍后再试！",
												Toast.LENGTH_SHORT).show();
									}

								}
							});
						}
					}).setNegativeButton("取消", null).create().show();

			// 下面是退出按钮
		} else if (itemId == R.id.logout) {
			new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage("确定退出情缘网？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									finish();
									System.exit(0);
								}
							}).setNegativeButton("取消", null).create().show();
		}

		return true;

	}

	/**
	 * 菜单按钮中用到的isUpdate方法
	 * */

	public boolean isUpdate() throws NameNotFoundException {
		boolean isNew = false;
		String res = null;
		PackageManager packageManager = this.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					this.getPackageName(), 0);
			try {
				String url = HttpUtil.BASE_URL + "&f=checkup&toType=json&ver="
						+ packageInfo.versionName;
				res = HttpUtil.getRequest(url);
				Log.d("url", url);
				Log.d("res", res);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(MainActivity.this, "服务器 异常，请稍后再试！",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			try {
				JSONObject json = new JSONObject(res);
				if (json.getInt("code") == 1) {
					isNew = true;
				} else {
					Log.e("接口错误", json.getString("message"));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (NameNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		SharedPreferences sharedPreferences = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("ischeck", true);
		editor.putString("checkdate", new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date(System.currentTimeMillis())));
		editor.commit();
		return isNew;

	}

}
