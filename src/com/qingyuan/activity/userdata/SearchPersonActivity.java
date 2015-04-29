package com.qingyuan.activity.userdata;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qingyuan.PaymentActivity;
import com.qingyuan.R;
import com.qingyuan.activity.message.ChatActivity;
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
	private String imageUrl[] = new String[20];
	private int imagenum = 0;
	// 實例化圖片加載类
	private AsyncImageLoader2 asyncImageLoader2 = new AsyncImageLoader2();
	// 4.. 发委托
	private EditText commission_send_edittext;// 委托内容的输入框
	private String commission_send_str;// 委托内容
	private EditText titleText, contentText;// 邮件内容的输入框
	private String email_send_str;// 邮件内容
	final String fuid = search_person_fuid;

	// 5. xml中一些用到的id
	private TextView memberText, user_nickname, place_gender_age, user_height,
			user_school, user_occupation, love_state, identity_check,
			email_check, tel_check, introduce, user_income, user_marry,
			user_child, user_house, user_vehicle, user_constellation,
			user_nation, user_home, user_weight, user_body, qiubo, like,
			entrust, more;
	private LinearLayout photos;// 照片相册
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
	private String[] items=new String []{
			"发送邮件","赠送礼物","在线聊天"
	};
	LinearLayout layoutEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchpersonactivity);
		init();
		// Log.i("init", "加载了初始化");
		initView();
		// Log.i("initView", "加载了初始化view");
		loadData();
		// Log.i("loadData", "加载了loaddata");

		// 发送委托的按钮
		entrust.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 详见http://www.oschina.net/question/54100_32486
				final TableLayout commissionLayout = (TableLayout) getLayoutInflater()
						.inflate(R.layout.searchperson_entrust, null);
				new AlertDialog.Builder(SearchPersonActivity.this)
						.setTitle("发送委托").setView(commissionLayout)
						.setPositiveButton("提交", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								EditText sendContent = (EditText) commissionLayout
										.findViewById(R.id.commission_content);
								final String commissionContent = sendContent
										.getText().toString().trim();
								SearchPersonActivity.this
										.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												showMsg = MyAction.sendCommission(
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
						}).setNegativeButton("取消", null).create().show();
			}
		});

		// 意中人
		like.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				liker_send();
			}
		});
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
		more.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(SearchPersonActivity.this)
				.setItems(items, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						switch (arg1) {
						//email present chat
						
						case 0:
							Log.i(tag+"more", arg1+"");
							//邮件
							if (home_cid.equals("20") || home_cid.equals("30")) {
								layoutEmail = (LinearLayout) getLayoutInflater().inflate(
										R.layout.searchperson_mail, null);
								new AlertDialog.Builder(SearchPersonActivity.this)
										.setTitle("发送邮件").setView(layoutEmail)
										.setPositiveButton("发送", new OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,
													int which) {
												TextView nicknameText = (TextView) layoutEmail
														.findViewById(R.id.nicknameText);
												nicknameText.setText("收件人："
														+ user_nickname1);
												titleText = (EditText) layoutEmail
														.findViewById(R.id.titleText);
												contentText = (EditText) layoutEmail
														.findViewById(R.id.contentText);
												new EmailThread().start();
											}
										}).setNegativeButton("取消", null).show();
							} else {
								new AlertDialog.Builder(SearchPersonActivity.this)
										.setTitle("提示:")
										.setMessage("只有VIP会员才可以发送电子邮件给对方，现在升级VIP会员？")
										.setPositiveButton("确定", new OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,
													int which) {
												String url = "http://www.07919.com/index.php?n=index&h=govip"; // 升级会员的网址（用浏览器打开）
												Intent intent = new Intent(
														Intent.ACTION_VIEW);
												intent.setData(Uri.parse(url));
												startActivity(intent);
											}
										}).setNegativeButton("取消", null).show();
							}
							break;
							
						case 1:
							// 送礼物
						
									Intent intent = new Intent(SearchPersonActivity.this,
											SearchPerson_Gift.class);
									startActivity(intent);
									break;
							
						case 2:
							int s_cid = user.getScid();
							if (s_cid < 40) {
								ChatActivity.talk_fuid = search_person_fuid;
								ChatActivity.talk_nickname = user_nickname1;

								Intent intent1 = new Intent(SearchPersonActivity.this,
										ChatActivity.class);
								startActivity(intent1);
							} else {
								new AlertDialog.Builder(SearchPersonActivity.this)
										.setMessage("您不能发起在线聊天，要升级会员吗？")
										.setPositiveButton("确定", new OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,
													int which) {
												int s_cid = user.getScid();
												if (s_cid > 0 && s_cid < 40) {
													arrServices = MyUtil
															.getArr(getResources()
																	.getStringArray(
																			R.array.renewalservice),
																	1);
													arrKeyServices = MyUtil
															.getArr(getResources()
																	.getStringArray(
																			R.array.renewalservice),
																	0);
												} else {
													arrServices = MyUtil.getArr(
															getResources().getStringArray(
																	R.array.service), 1);
													arrKeyServices = MyUtil.getArr(
															getResources().getStringArray(
																	R.array.service), 0);
												}
												String[] itemArr = new String[arrServices.length];
												for (int i = 0; i < arrServices.length; i++) {
													String a = "/3个月";
													if (i == 1)
														a = "/6个月";
													else if (i == 2) {
														a = "/1个月";
													}
													itemArr[i] = arrServices[i] + a + " "
															+ arrKeyServices[i] + "元";
												}
												new AlertDialog.Builder(
														SearchPersonActivity.this)
														.setTitle("购买服务")
														.setSingleChoiceItems(itemArr, 0,
																new OnClickListener() {

																	@Override
																	public void onClick(
																			DialogInterface dialog,
																			int which) {
																		orderKey = which;
																	}
																})
														.setPositiveButton("立即购买",
																new OnClickListener() {

																	@Override
																	public void onClick(
																			DialogInterface dialog,
																			int which) {
																		Intent intent = new Intent();
																		intent.setClass(
																				SearchPersonActivity.this,
																				PaymentActivity.class);
																		Bundle bundle = new Bundle();
																		Order order = new Order(
																				0,
																				Integer.parseInt(arrKeyServices[orderKey]),
																				arrServices[orderKey],
																				"");
																		bundle.putSerializable(
																				"order",
																				order);
																		intent.putExtras(bundle);
																		startActivity(intent);
																	}
																})
														.setNegativeButton("取消", null)
														.create().show();
											}
										}).setNegativeButton("取消", null).show();
							}
							break;
							
						}
					}
				}).create().show();
			}
		});
		
		
	}

	/**
	 * 获取用户信息，格式化。
	 * */
	private void init() {
		SharedPreferences sharedPreferences = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		user = new User();
		user.formatUserFromPreferences(sharedPreferences);
		home_nickname = sharedPreferences.getString("nickname", "");
		home_uid = sharedPreferences.getString("uid", "");
		home_gender = sharedPreferences.getString("gender", "");
		home_cid = sharedPreferences.getString("cid", "");
		home_star = sharedPreferences.getString("star", "");
	}

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
		like = (TextView) findViewById(R.id.like);// 意中人
		entrust = (TextView) findViewById(R.id.entrust);// 委托
		more = (TextView) findViewById(R.id.more);// 其他
		//
		photos = (LinearLayout) findViewById(R.id.photos);// 照片展示

	}

	/**
	 * 加载数据
	 * 
	 * */
	@SuppressLint("SimpleDateFormat")
	private void loadData() {
		String url = HttpUtil.BASE_URL + "&toType=json&f=space&fuid="
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
				} else {
					user_gender1 = "男";
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
				// 获取头像资源
				JSONArray arr = user.getJSONArray("pics");
				for (int i = 0; i < arr.length(); i++) {
					imageUrl[i] = arr.optJSONObject(i).getString("imgurl");
					Log.i("src", imageUrl[i]);
					imagenum++;
				}
				user_pic.setVisibility(View.VISIBLE);
				loadImage(HttpUtil.URL + "/" + user.getString("pic"),
						R.id.user_pic);
				// 获取照片资源
				for (int i = 0; i < imageUrl.length; i++) {
					if (imageUrl[i] != null && !imageUrl[i].equals("")) {
						final ImageView img = new ImageView(this);
						img.setId(i);
						img.setLayoutParams(new LayoutParams(130, 110));
						img.setScaleType(ImageView.ScaleType.FIT_START);
						loadImage(HttpUtil.URL + "/" + imageUrl[i], img.getId());
						photos.addView(img);
						img.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(
										SearchPersonActivity.this,
										SearchPerson_Photos.class);
								int id = img.getId();
								SearchPerson_Photos.imagePosition = img.getId();
								SearchPerson_Photos.fuid = fuid;
								Log.i("photo_id", id + "");
								startActivity(intent);
							}
						});
					}
				}

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

	/**
	 * 添加数据
	 * */
	private void entity() {
		// 向textview添加数据
		user_nickname.setText(user_nickname1);
		user_height.setText("身高：" + user_height1);
		user_school.setText("学历" + user_school1);
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
