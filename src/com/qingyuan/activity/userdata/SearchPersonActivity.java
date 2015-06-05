package com.qingyuan.activity.userdata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qingyuan.PaymentActivity;
import com.qingyuan.R;
import com.qingyuan.activity.expand.QingyuanMall;
import com.qingyuan.activity.message.ChatActivity;
import com.qingyuan.modem.photo.ImagePagerActivity;
import com.qingyuan.modem.photo.MyGridAdapter;
import com.qingyuan.modem.photo.NoScrollGridView;
import com.qingyuan.service.parser.MyAction;
import com.qingyuan.service.parser.MyUtil;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.HttpUtil;
import com.qingyuan.util.Order;
import com.qingyuan.util.User;

public class SearchPersonActivity extends Activity {

	String tag = "searchperson_tag";

	// 用到的变量
	// 1. 读取保存的数据
	private User user;
	private String home_nickname, home_uid, home_gender, home_cid, home_star;
	// 2. search_person_fuid进入个人主页提交参数，
	public static String search_person_fuid;
	// 3. 定义相册图片的url
	private int imagenum = 0;
	private String imageUrl[];
	// 實例化圖片加載类
	private AsyncImageLoader2 asyncImageLoader2 = new AsyncImageLoader2();
	// 4.. 发委托
	private EditText commission_send_edittext;// 委托内容的输入框
	private EditText titleText, contentText;// 邮件内容的输入框
	final String fuid = search_person_fuid;

	// 5. xml中一些用到的id
	private TextView memberText, user_nickname, place_gender_age, user_height,
			user_school, user_occupation, love_state, identity_check,
			email_check, tel_check, introduce, user_income, user_marry,
			user_child, user_house, user_vehicle, user_constellation,
			user_nation, user_home, user_weight, user_body, qiubo, chat, more;
	private RelativeLayout rl_photo;

	private NoScrollGridView grid;// 照片相册gridView=(NoScrollGridView)
	private ImageView user_pic;// 头像
	// 6. 这些个textView需要赋值时用到的变量
	private String memberText1, user_nickname1, user_age1, user_birthyear1,
			user_province1, user_homeprovince1, user_homecity1, user_city1,
			user_gender1, user_height1, user_school1, user_occupation1,
			love_state1, identity_check1, email_check1, tel_check1, introduce1,
			user_income1, user_marry1, user_child1, user_house1, user_vehicle1,
			user_constellation1, user_nation1, user_home1, user_weight1,
			user_body1;

	// 7.底部Button用到的几个变量
	String showMsg;
	String[] listLeer;
	String msgLeer;
	private String[] arrServices, arrKeyServices;
	int orderKey;
	private String[] items = new String[] { "发送邮件", "赠送礼物", "加意中人", "委托红娘" };
	LinearLayout layoutEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchpersonactivity);
		SharedPreferences sharedPreferences = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		user = new User();
		user.formatUserFromPreferences(sharedPreferences);
		home_nickname = sharedPreferences.getString("nickname", "");
		home_uid = sharedPreferences.getString("uid", "");
		home_gender = sharedPreferences.getString("gender", "");
		home_cid = sharedPreferences.getString("cid", "");
		home_star = sharedPreferences.getString("star", "");

		// Log.i("initView", "加载了初始化view");
		initView();
		// Log.i("loadData", "加载了loaddata");
		loadData();

		// 暗送秋波
		qiubo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						String res = null;
						try {
							res = HttpUtil.getRequest(HttpUtil.BASE_URL
									+ "&f=sinfo&toType=json&type="
									+ home_gender);
							if (res != null) {
								JSONObject json = new JSONObject(res);
								int code = json.getInt("code");
								showMsg = json.getString("message");
								if (code == 1) {
									JSONArray array = json.getJSONObject(
											"result").getJSONArray("list");
									listLeer = new String[array.length()];
									for (int i = 0; i < array.length(); i++) {
										listLeer[i] = array.optJSONObject(i)
												.getString("content");
									}
									SearchPersonActivity.this
											.runOnUiThread(new Runnable() {

												@Override
												public void run() {
													new AlertDialog.Builder(
															SearchPersonActivity.this)
															.setTitle("选择问候语：")
															.setSingleChoiceItems(
																	listLeer,
																	0,
																	new DialogInterface.OnClickListener() {

																		@Override
																		public void onClick(
																				DialogInterface dialog,
																				int which) {
																			msgLeer = listLeer[which];
																		}
																	})
															.setPositiveButton(
																	"发送",
																	new OnClickListener() {

																		@Override
																		public void onClick(
																				DialogInterface dialog,
																				int which) {
																			leer_send();
																		}
																	})
															.setNegativeButton(
																	"取消", null)
															.create().show();
												}
											});
								} else if (code == 10000) {
									SearchPersonActivity.this
											.runOnUiThread(new Runnable() {

												@Override
												public void run() {
													Toast.makeText(
															SearchPersonActivity.this,
															showMsg,
															Toast.LENGTH_SHORT)
															.show();
												}
											});
								} else if (code >= 10000) {
									Log.e("ERROR", showMsg);
									SearchPersonActivity.this
											.runOnUiThread(new Runnable() {

												@Override
												public void run() {
													Toast.makeText(
															SearchPersonActivity.this,
															"服务器异常，请稍后在试！",
															Toast.LENGTH_SHORT)
															.show();
												}
											});
								}
							} else {
								SearchPersonActivity.this
										.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												Toast.makeText(
														SearchPersonActivity.this,
														"服务器异常，请稍后在试！",
														Toast.LENGTH_SHORT)
														.show();
											}
										});
							}
						} catch (Exception e) {
							e.printStackTrace();
							SearchPersonActivity.this
									.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(
													SearchPersonActivity.this,
													"服务器异常，请稍后在试！",
													Toast.LENGTH_SHORT).show();
										}
									});
						}
					}
				}).start();
			}
		});

		chat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int s_cid = user.getScid();
				if (s_cid < 40) {
					ChatActivity.talk_fuid = search_person_fuid;
					ChatActivity.talk_nickname = user_nickname1;

					Intent intent1 = new Intent(SearchPersonActivity.this,
							ChatActivity.class);
					startActivity(intent1);
				} else {
					new AlertDialog.Builder(SearchPersonActivity.this)
							.setMessage("您不是高级会员，不能发起在线聊天")
							.setPositiveButton("确定", null)

							.show();
				}

			}
		});

		more.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(SearchPersonActivity.this)
						.setItems(items, new OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								switch (arg1) {
								// email present chat

								case 0:
									Log.i(tag + "more", arg1 + "");
									// 邮件
									if (home_cid.equals("20")
											|| home_cid.equals("30")) {
										layoutEmail = (LinearLayout) getLayoutInflater()
												.inflate(
														R.layout.searchperson_mail,
														null);
										new AlertDialog.Builder(
												SearchPersonActivity.this)
												.setTitle("发送邮件")
												.setView(layoutEmail)
												.setPositiveButton("发送",
														new OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {

																titleText = (EditText) layoutEmail
																		.findViewById(R.id.titleText);
																contentText = (EditText) layoutEmail
																		.findViewById(R.id.contentText);
																new EmailThread()
																		.start();
															}
														})
												.setNegativeButton("取消", null)
												.show();
									} else {
										new AlertDialog.Builder(
												SearchPersonActivity.this)
												.setTitle("提示:")
												.setMessage(
														"只有VIP会员才可以发送电子邮件给对方，现在升级VIP会员？")
												.setPositiveButton("确定",
														new OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																String url = "http://www.07919.com/index.php?n=index&h=govip"; // 升级会员的网址（用浏览器打开）
																Intent intent = new Intent(
																		Intent.ACTION_VIEW);
																intent.setData(Uri
																		.parse(url));
																startActivity(intent);
															}
														})
												.setNegativeButton("取消", null)
												.show();
									}
									break;

								case 1:
									// 送礼物

									Intent intent = new Intent(
											SearchPersonActivity.this,
											QingyuanMall.class);
									startActivity(intent);
									break;
								case 2:
									// 意中人
									liker_send();
									break;
								case 3:

									final TableLayout commissionLayout = (TableLayout) getLayoutInflater()
											.inflate(
													R.layout.searchperson_entrust,
													null);
									new AlertDialog.Builder(
											SearchPersonActivity.this)
											.setTitle("发送委托")
											.setView(commissionLayout)
											.setPositiveButton("提交",
													new OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															EditText sendContent = (EditText) commissionLayout
																	.findViewById(R.id.commission_content);
															final String commissionContent = sendContent
																	.getText()
																	.toString()
																	.trim();
															SearchPersonActivity.this
																	.runOnUiThread(new Runnable() {

																		@Override
																		public void run() {
																			showMsg = MyAction
																					.sendCommission(
																							Integer.parseInt(home_uid),
																							Integer.parseInt(search_person_fuid),
																							commissionContent);

																			if (showMsg == null) {
																				showMsg = "发送成功！";
																			}
																			Toast.makeText(
																					SearchPersonActivity.this,
																					showMsg,
																					Toast.LENGTH_LONG)
																					.show();
																		}
																	});
														}
													})
											.setNegativeButton("取消", null)
											.create().show();
									break;

								}
							}
						}).create().show();
			}
		});

	}// ////onCreate

	/**
	 * 获取用户信息，格式化。
	 * */

	private void initView() {
		// 定义所有文字的对应控件
		memberText = (TextView) findViewById(R.id.memberText);// 顶部昵称
		user_pic = (ImageView) findViewById(R.id.user_pic);// 用户照片
		user_nickname = (TextView) findViewById(R.id.user_nickname);// 昵称
		place_gender_age = (TextView) findViewById(R.id.place_gender_age);// 年龄地区性别
		user_school = (TextView) findViewById(R.id.search_back_school);// 学历
		user_height = (TextView) findViewById(R.id.search_back_height);// 身高
		user_occupation = (TextView) findViewById(R.id.occupation);// 职业
		love_state = (TextView) findViewById(R.id.love_state);// 爱情状态
		identity_check = (TextView) findViewById(R.id.identity_check);// 身份证验证
		email_check = (TextView) findViewById(R.id.email_check);// 邮箱验证
		tel_check = (TextView) findViewById(R.id.tel_check);// 电话号码验证
		//
		user_income = (TextView) findViewById(R.id.search_back_income);// 收入水平
		user_marry = (TextView) findViewById(R.id.search_back_marry);// 婚姻状况
		user_child = (TextView) findViewById(R.id.search_back_child);// 有无小孩
		user_house = (TextView) findViewById(R.id.search_back_house);// 购房情况
		user_vehicle = (TextView) findViewById(R.id.vehicle);// 购车情况
		user_constellation = (TextView) findViewById(R.id.constellation);// 星座
		user_home = (TextView) findViewById(R.id.home);// 籍贯
		user_nation = (TextView) findViewById(R.id.nation);// 民族
		user_weight = (TextView) findViewById(R.id.weight);// 体重
		user_body = (TextView) findViewById(R.id.body);// 体型
		//
		introduce = (TextView) findViewById(R.id.introduce);// 内心独白
		qiubo = (TextView) findViewById(R.id.qiubo);// 秋波
		chat = (TextView) findViewById(R.id.chat);
		more = (TextView) findViewById(R.id.more);// 其他
		//
		grid = (NoScrollGridView) findViewById(R.id.grid);// 照片展示
		rl_photo = (RelativeLayout) findViewById(R.id.rl_searchperson_photo);

	}

	/**
	 * 加载数据
	 * 
	 * */
	@SuppressLint("SimpleDateFormat")
	private void loadData() {
		String url = HttpUtil.BASE_URL
				+ "&toType=json&f=space&fuid="
				+ search_person_fuid + "&android_uid=" + home_uid;
		try {
			String res = HttpUtil.getRequest(url);
			JSONObject jsonData = new JSONObject(res);
			if (jsonData.getInt("code") == 1) {
				JSONObject user = jsonData.getJSONObject("result")
						.getJSONObject("fuser");
				// 1.昵称
				user_nickname1 = user.getString("nickname");
				if (user_nickname1 != null && !user_nickname1.equals("")) {
					memberText.setText(user_nickname1 + "的个人中心");
				} else {
					memberText.setText(user.getString("uid") + "的个人中心");
					user_nickname1 = search_person_fuid;
				}
				// 年龄
				user_birthyear1 = user.getString("birthyear");
				// 获取系统日期
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				// 日期转为字符串
				String date = sdf.format(new java.util.Date());
				// 通过出生年计算年龄
				user_age1 = (Integer.parseInt(date) - Integer
						.parseInt(user_birthyear1)) + "";
				// 性别
				if (user.getInt("gender") == 1) {
					user_gender1 = "女";
					rl_photo.setBackgroundResource(R.drawable.searchperson_rlback1);
				} else {
					user_gender1 = "男";
					rl_photo.setBackgroundResource(R.drawable.searchperson_rlback0);
				}
				// 获取爱情状态
				love_state1 = user.getString("love_status");// 对于偶尔出现的cer空字段，直接忽略处理，不再显示
				// 省市信息-home
				String a, b;
				String[] as = null;
				a = user.getString("province");
				b = user.getString("hometownprovince");
				if ((a != null && a.length() > 0 && !a.equals("0"))
						|| (b != null && b.length() > 0 && !b.equals("0"))) {
					as = getResources().getStringArray(R.array.province);
				}
				if (a != null && a.length() > 0 && !a.equals("0")) {
					for (int i = 0; i < as.length; i++) {
						if (as[i].contains(a)) {
							user_province1 = as[i].substring(0,
									as[i].indexOf(","));
							break;
						}
					}
				}
				if (b != null && b.length() > 0 && !b.equals("0")) {
					for (int i = 0; i < as.length; i++) {
						if (as[i].contains(b)) {
							user_homeprovince1 = as[i].substring(0,
									as[i].indexOf(","));
							break;
						}
					}
				} else {
					user_homeprovince1 = "";
				}

				a = user.getString("city");
				b = user.getString("hometowncity");
				if ((a != null && a.length() > 0 && !a.equals("0"))
						|| (b != null && b.length() > 0 && !b.equals("0"))) {
					as = getResources().getStringArray(R.array.city);
				}
				if (a != null && a.length() > 0 && !a.equals("0")) {
					for (int i = 0; i < as.length; i++) {
						if (as[i].contains(a)) {
							user_city1 = as[i].substring(0, as[i].indexOf(","));
							break;
						} else {
							user_city1 = " ";
						}
					}
				} else {
					user_city1 = " ";
				}
				if (b != null && b.length() > 0 && !b.equals("0")) {
					for (int i = 0; i < as.length; i++) {
						if (as[i].contains(b)) {
							user_homecity1 = as[i].substring(0,
									as[i].indexOf(","));
							break;
						} else {
							user_city1 = " ";
						}
					}
				} else {
					user_homecity1 = " ";
				}
				Log.i("user_city1", user_city1);
				Log.i("user_homecity1", user_homecity1);
				Log.i("user_homeprovince1", user_homeprovince1);
				Log.i("user_province1", user_province1);
				// 学历
				String education = user.getString("education");
				if (education != null && education.length() > 0
						&& !education.equals("0")) {
					String[] educations = getResources().getStringArray(
							R.array.educaction);
					for (int i = 0; i < educations.length; i++) {
						if (educations[i].contains(education)) {
							user_school1 = educations[i].substring(0,
									educations[i].indexOf(","));
							break;
						}
					}
				} else {
					user_school1 = "未知";
				}
				// 身高
				user_height1 = user.getString("height");
				user_height1 = user_height1 == "0" ? "未知" : user_height1 + "cm";
				// 收入水平
				String salary = user.getString("salary");
				if (salary != null && salary.length() > 0
						&& !salary.equals("0")) {
					String[] salarys = getResources().getStringArray(
							R.array.salary);
					for (int i = 0; i < salarys.length; i++) {
						if (salarys[i].contains(salary)) {
							user_income1 = salarys[i].substring(0,
									salarys[i].indexOf(","));
							break;
						}
					}
				} else {
					user_income1 = "未知";
				}
				// 结婚情况
				String marrage = user.getString("marriage");
				if (marrage != null && marrage.length() > 0
						&& !marrage.equals("0")) {
					String[] marrages = getResources().getStringArray(
							R.array.marriage);
					for (int i = 0; i < marrages.length; i++) {
						if (marrages[i].contains(marrage)) {
							user_marry1 = marrages[i].substring(0,
									marrages[i].indexOf(","));
							break;
						}
					}
				} else {
					user_marry1 = "未知";
				}
				// 有无孩子
				String children = user.getString("children");
				if (children != null && children.length() > 0
						&& !children.equals("0")) {
					String[] childrens = getResources().getStringArray(
							R.array.children);
					for (int i = 0; i < childrens.length; i++) {
						if (childrens[i].contains(children)) {
							user_child1 = childrens[i].substring(0,
									childrens[i].indexOf(","));
							break;
						}
					}
				} else {
					user_child1 = "未知";
				}
				// 买房情况
				String house = user.getString("house");
				if (house != null && house.length() > 0 && !house.equals("0")) {
					String[] houses = getResources().getStringArray(
							R.array.house);
					for (int i = 0; i < houses.length; i++) {
						if (houses[i].contains(house)) {
							user_house1 = houses[i].substring(0,
									houses[i].indexOf(","));
							break;
						}
					}
				} else {
					user_house1 = "未知";
				}
				// 购车情况
				a = user.getString("vehicle");
				if (a != null && a.length() > 0 && !a.equals("0")) {
					as = getResources().getStringArray(R.array.vehicle);
					for (int i = 0; i < as.length; i++) {
						if (as[i].contains(a)) {
							user_vehicle1 = as[i].substring(0,
									as[i].indexOf(","));
						}
					}
				} else {
					user_vehicle1 = "未知";
				}
				// 职业
				a = user.getString("occupation");
				if (a != null && a.length() > 0 && !a.equals("0")) {
					as = getResources().getStringArray(R.array.occupation);
					for (int i = 0; i < as.length; i++) {
						if (as[i].contains(a)) {
							user_occupation1 = as[i].substring(0,
									as[i].indexOf(","));
						}
					}
				} else {
					user_occupation1 = "未知";
				}
				// 国籍、籍贯
				a = user.getString("nation");
				if (a != null && a.length() > 0 && !a.equals("0")) {
					as = getResources().getStringArray(R.array.nation);
					for (int i = 0; i < as.length; i++) {
						if (as[i].contains(a)) {
							user_nation1 = as[i].substring(0,
									as[i].indexOf(","));
						}
					}
				} else {
					user_nation1 = "未知";
				}
				// 体重
				a = user.getString("weight");
				if (a != null && a.length() > 0 && !a.equals("0")) {
					user_weight1 = a + "公斤";
				} else {
					user_weight1 = "未知";
				}
				// 体型
				a = user.getString("body");
				if (a != null && a.length() > 0 && !a.equals("0")) {
					as = getResources().getStringArray(R.array.body);
					for (int i = 0; i < as.length; i++) {
						if (as[i].contains(a)) {
							user_body1 = as[i].substring(0, as[i].indexOf(","));
						}
					}
				} else {
					user_body1 = "未知";
				}
				// 星座
				a = user.getString("constellation");
				if (a != null && a.length() > 0 && !a.equals("0")) {
					as = getResources().getStringArray(R.array.constellation);
					for (int i = 0; i < as.length; i++) {
						if (as[i].contains(a)) {
							user_constellation1 = as[i].substring(0,
									as[i].indexOf(","));
						}
					}
				} else {
					user_constellation1 = "未知";
				}
				// 内心独白
				a = user.getString("introduce");
				if (a != null && a.length() > 0) {
					introduce1 = a;
				} else {
					introduce1 = "未填写";
				}

				JSONArray arr = user.getJSONArray("pics");
				imageUrl = new String[arr.length()];
				for (int i = 0; i < arr.length(); i++) {
					imageUrl[i] = HttpUtil.URL + "/"
							+ arr.optJSONObject(i).getString("imgurl");

				}
				int size = imageUrl.length;
				Log.i(tag, "item.size()" + size);
				int length = 100;

				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				float density = dm.density;
				int gridviewWidth = (int) (size * (length + 4) * density);
				int itemWidth = (int) (length * density);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
				grid.setLayoutParams(params);
				grid.setColumnWidth(itemWidth);

				grid.setHorizontalSpacing(5);
				grid.setStretchMode(GridView.NO_STRETCH);
				grid.setVerticalScrollBarEnabled(true);
				grid.setNumColumns(size);
				grid.setAdapter(new MyGridAdapter(imageUrl,
						SearchPersonActivity.this));
				grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						imageBrower(position, imageUrl);
					}
				});
				/*
				 * int size = items.size(); Log.i(tag, "item.size()" + size);
				 * int length = 100;
				 * 
				 * 
				 * 被舍弃的原先照片预览页 DisplayMetrics dm = new DisplayMetrics();
				 * getWindowManager().getDefaultDisplay().getMetrics(dm); float
				 * density = dm.density; int gridviewWidth = (int) (size *
				 * (length + 4) * density); int itemWidth = (int) (length *
				 * density);
				 * 
				 * LinearLayout.LayoutParams params = new
				 * LinearLayout.LayoutParams( gridviewWidth,
				 * LinearLayout.LayoutParams.FILL_PARENT);
				 * grid.setLayoutParams(params); grid.setColumnWidth(itemWidth);
				 * 
				 * grid.setHorizontalSpacing(5);
				 * grid.setStretchMode(GridView.NO_STRETCH);
				 * grid.setVerticalScrollBarEnabled(true);
				 * grid.setNumColumns(size); GridViewAdapter ada = new
				 * GridViewAdapter( SearchPersonActivity.this, imgs);
				 * grid.setAdapter(ada); ada.notifyDataSetChanged();
				 * grid.setOnItemClickListener(new
				 * AdapterView.OnItemClickListener() {
				 * 
				 * @Override public void onItemClick(AdapterView<?> arg0, View
				 * view, int position, long arg3) { imageBrower(position,
				 * imgs.toArray(new String[imgs.size()]));
				 * 
				 * }
				 * 
				 * });
				 */

				// 获取头像资源
				user_pic.setVisibility(View.VISIBLE);
				loadImage(HttpUtil.URL + "/" + user.getString("pic"),
						R.id.user_pic);

				// Log.i("entity", "加载了entity");
				entity();
			} else if (jsonData.getInt("code") == 10000) {
				new AlertDialog.Builder(SearchPersonActivity.this)
						.setTitle("提示")
						.setMessage(jsonData.getString("message"))
						.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								SearchPersonActivity.this.finish();
							}
						}).create().show();

			} else {
				Log.e("用户数据错误", jsonData.getString("message"));
				SearchPersonActivity.this.finish();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent(SearchPersonActivity.this,
				ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}

	/**
	 * 相册适配
	 * 
	 * @author Administrator
	 *
	 */

	public class GridViewAdapter extends BaseAdapter {
		Context context;
		List<String> list;
		LayoutInflater inflater;

		public GridViewAdapter(Context _context, List<String> _list) {
			this.list = _list;
			this.context = _context;
			inflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			convertView = layoutInflater.inflate(
					R.layout.item_searchperson_photos, null);
			ImageView iv_photo = (ImageView) convertView
					.findViewById(R.id.iv_serchperson_photos);
			final String imgs = HttpUtil.URL + "/" + list.get(position);
			loadImage(imgs, R.id.iv_serchperson_photos, convertView);
			//
			/*
			 * final String [] abc=new String[list.size()];
			 * convertView.setOnClickListener(new View.OnClickListener() {
			 * 
			 * @Override public void onClick(View arg0) {
			 * Toast.makeText(getApplicationContext(), "第" + position + "张",
			 * Toast.LENGTH_SHORT).show();
			 * 
			 * imageBrower(position, imgs, list.toArray(abc)); } });
			 */
			return convertView;
		}

	}

	/**
	 * 添加数据
	 * */
	private void entity() {
		// 向textview添加数据
		user_nickname.setText(user_nickname1 + "(" + home_uid + ")");
		user_height.setText("身高：" + user_height1);
		user_school.setText("学历: " + user_school1);
		user_occupation.setText("职业：" + user_occupation1);
		user_marry.setText("婚姻状况：" + user_marry1);
		user_vehicle.setText("购车情况：" + user_vehicle1);
		user_home.setText("籍贯：" + user_home1);
		user_constellation.setText("星座：" + user_constellation1);
		user_nation.setText("民族：" + user_nation1);
		user_weight.setText("体重：" + user_weight1);
		user_body.setText("体型：" + user_body1);
		introduce.setText(introduce1);
		user_income.setText("收入：" + user_income1);
		user_child.setText("有无孩子：" + user_child1);
		love_state.setText(love_state1);

		if (user_homeprovince1.length() > 0 || user_city1.length() > 0) {
			user_home.setText(user_homeprovince1 + user_homecity1);
			Log.i("user_homeprovince1", user_homeprovince1);
			Log.i("user_city1", user_city1);
		} else {
			user_home.setText("未知");
		}
		user_nickname
				.setText(TextUtils.isEmpty(user_nickname1) ? search_person_fuid
						: user_nickname1);
		String placegenderage = "";
		if (user_province1 == "0" && user_city1 == "0") {
			placegenderage += "未知";
		} else {
			placegenderage += user_province1 + user_city1;
		}
		placegenderage += "，" + user_gender1 + "，" + user_age1 + "岁";
		place_gender_age.setText(placegenderage);
	}

	/**
	 * 
	 * 
	 * 按钮
	 */
	// 定义真机物理按钮的功能
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			SearchPersonActivity.this.finish();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// new AlertDialog.Builder(SearchPersonActivity.this)
			// .setTitle("")
			// .setMessage("暂无此功能")
			// .setPositiveButton("确定",null)
			// .show();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 异步加载相册图片
	 * 
	 * */
	private void loadImage(final String url, final int id, final View view) {
		// 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
		Drawable cacheImage = asyncImageLoader2.loadDrawable(url,
				new AsyncImageLoader2.ImageCallback() {
					// 请参见实现：如果第一次加载url时下面方法会执行
					public void imageLoaded(Drawable imageDrawable) {
						((ImageView) view.findViewById(id))
								.setImageDrawable(imageDrawable);
					}
				});
		if (cacheImage != null) {
			((ImageView) view.findViewById(id)).setImageDrawable(cacheImage);
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

	/** 加为意中人的子线程 */
	public void liker_send() {
		SearchPersonActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				String res;
				try {
					res = HttpUtil.getRequest(HttpUtil.BASE_URL
							+ "&f=liker&toType=json&action=ADD"
							+ "&android_uid=" + home_uid + "&fuid="
							+ search_person_fuid);

					JSONObject json = new JSONObject(res);
					int code = json.getInt("code");
					showMsg = json.getString("message");
					if (code == 1) {
						Toast.makeText(SearchPersonActivity.this, "添加成功！",
								Toast.LENGTH_LONG).show();
					} else if (code == 10000) {
						Toast.makeText(SearchPersonActivity.this, showMsg,
								Toast.LENGTH_LONG).show();
					} else {
						Log.e("ERROE", showMsg);
						Toast.makeText(SearchPersonActivity.this,
								"服务器异常，请稍后再试！", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e1) {
					Toast.makeText(SearchPersonActivity.this, "服务器异常，请稍后再试！",
							Toast.LENGTH_SHORT).show();
					e1.printStackTrace();
				}
			}
		});
	}

	// 发送秋波的方法
	public void leer_send() {
		SearchPersonActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				String res;
				Map<String, String> param = new HashMap<String, String>();
				param.put("fuid", search_person_fuid);
				param.put("sendinfo", msgLeer);
				try {
					res = HttpUtil.postRequest(HttpUtil.BASE_URL
							+ "&toType=json&f=leer&action=SEND&android_uid="
							+ home_uid, param);
					JSONObject json = new JSONObject(res);
					int code = json.getInt("code");
					showMsg = json.getString("message");
					if (code == 1) {
						Toast.makeText(SearchPersonActivity.this, showMsg,
								Toast.LENGTH_LONG).show();
					} else if (code == 10000) {
						Toast.makeText(SearchPersonActivity.this, showMsg,
								Toast.LENGTH_LONG).show();
					} else {
						Log.e("ERROR", showMsg);
						Toast.makeText(SearchPersonActivity.this,
								"服务器异常，请稍候再试！", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Toast.makeText(SearchPersonActivity.this, "服务器异常，请稍候再试！",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

			}
		});
	}

	// 发送邮件的子线程
	class EmailThread extends Thread {
		public void run() {
			int uid = Integer.parseInt(home_uid);
			int toUid = Integer.parseInt(search_person_fuid);
			String title = titleText.getText().toString().trim();
			String content = contentText.getText().toString().trim();
			int id = 0;
			showMsg = MyAction.sendEmail(uid, toUid, id, title, content);
			if (showMsg == null) {
				showMsg = "发送成功！";
			}
			SearchPersonActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(SearchPersonActivity.this, showMsg,
							Toast.LENGTH_LONG).show();
				}
			});
		}

	}

}
