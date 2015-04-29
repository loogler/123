package com.qingyuan.activity.userdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qingyuan.R;
import com.qingyuan.popupwindow.MySelf_Mod_Photo;
import com.qingyuan.service.parser.MyUtil;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.CustomProgressDialog;
import com.qingyuan.util.HttpUtil;
import com.qingyuan.util.UploadUtil;
import com.qingyuan.util.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MS_ModifyActivity extends Activity implements
		android.view.View.OnClickListener {

	String tag = "MS_ModifyActivity";
	// 当前页面的初始化，查看用户未改动的信息，第一次进入界面时。
	// 各个可以点击的relativelayout。
	private RelativeLayout rl_nickname, rl_marry, rl_height, rl_birthday,
			rl_income, rl_workplace, rl_education, rl_havechild, rl_house,
			rl_time;
	// 用户数据年龄性别等。。。。。 txt_save是保存按钮
	private TextView txt_nickname, txt_marry, txt_height, txt_birthday,
			txt_income, txt_workplace, txt_education, txt_havechild, txt_house,
			txt_time, txt_save;

	private int indexHeight, indexMarriage, pIndex, educationIndex;
	// 用于取数组资源，从中筛选，供用户选择，
	private String[] arrHeight, arrSalary, arrMarriage, keyMarriage,
			arrProvince, keyProvince, arrCity, arrOldsex, arrEducation,
			keyEducation, arrChildren, arrHouse;

	ImageView user_info_user_pic;// 照片:添加照片
	private LinearLayout photos;// 照片相册
	private boolean isPicChange=false;
	boolean isInfoChange = false;

	// 加载中。。。。
	public CustomProgressDialog progressDialog = null;
	// 自定义的弹出框类，选择照片时可用
	private MySelf_Mod_Photo menuWindow;
	// 提交参数，用户参数用于更改
	private static String birthYear, birthMonth, birthDay, nickName, marriage,
			height, salary, province, city, education, children, house, oldsex;

	// 获取保存的用户数据
	public static User user;
	private String home_uid, home_pic;
	
	private String imageUrl[] = new String[20];
	
	private AsyncImageLoader2 asyncImageLoader2 = new AsyncImageLoader2();

	// 拍照参数

	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	// 创建一个以当前时间为名称的文件，获取相册资源
	private File tempFile;
	private File uploadFile;


	

	// 要保存的用户信息
	private String login_nickname, login_uid, login_gender, login_cid,
			login_star, user_pic, login_province, login_city, login_birth,
			true_name, login_telphone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ms_modifyactivity);
		SharedPreferences preferences = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		user = new User();
		user.formatUserFromPreferences(preferences);
		home_uid = preferences.getString("uid", "");
//		home_nickname = preferences.getString("nickname", null);
		home_pic = preferences.getString("pic", null);
//		home_cid = preferences.getString("cid", null);

		initView();// 初始化界面
		initStrArrData();// 初始化数组
		initPhotos();
		progressDialog = CustomProgressDialog.createDialog(
				MS_ModifyActivity.this, "加载中...");
		progressDialog.show();// 加载中。。。。。
		initData.start();// 启动线程。
	}

	/**
	 * 初始化界面
	 * */
	private void initView() {
		Log.i(tag, "initView()");
		rl_nickname = (RelativeLayout) findViewById(R.id.changeinfo_relayout_nickname);
		rl_marry = (RelativeLayout) findViewById(R.id.changeinfo_relayout_marry);
		rl_height = (RelativeLayout) findViewById(R.id.changeinfo_relayout_height);
		rl_birthday = (RelativeLayout) findViewById(R.id.changeinfo_relayout_birthday);
		rl_income = (RelativeLayout) findViewById(R.id.changeinfo_relayout_income);
		rl_workplace = (RelativeLayout) findViewById(R.id.changeinfo_relayout_workplace);
		rl_education = (RelativeLayout) findViewById(R.id.changeinfo_relayout_education);
		rl_havechild = (RelativeLayout) findViewById(R.id.changeinfo_relayout_havechild);
		rl_house = (RelativeLayout) findViewById(R.id.changeinfo_relayout_house);
		rl_time = (RelativeLayout) findViewById(R.id.changeinfo_relayout_time);

		txt_nickname = (TextView) findViewById(R.id.changeinfo_txt_nickname);
		txt_marry = (TextView) findViewById(R.id.changeinfo_txt_marry);
		txt_height = (TextView) findViewById(R.id.changeinfo_txt_height);
		txt_birthday = (TextView) findViewById(R.id.changeinfo_txt_birthday);
		txt_income = (TextView) findViewById(R.id.changeinfo_txt_income);
		txt_workplace = (TextView) findViewById(R.id.changeinfo_txt_workplace);
		txt_education = (TextView) findViewById(R.id.changeinfo_txt_education);
		txt_havechild = (TextView) findViewById(R.id.changeinfo_txt_havechild);
		txt_house = (TextView) findViewById(R.id.changeinfo_txt_house);
		txt_time = (TextView) findViewById(R.id.changeinfo_txt_time);
		txt_save = (TextView) findViewById(R.id.txt_saveuserinfo);
		user_info_user_pic = (ImageView) findViewById(R.id.user_info_user_pic);
		photos = (LinearLayout) findViewById(R.id.user_photos1);// 照片展示

		rl_nickname.setOnClickListener(this);
		rl_marry.setOnClickListener(this);
		rl_height.setOnClickListener(this);
		rl_birthday.setOnClickListener(this);
		rl_income.setOnClickListener(this);
		rl_workplace.setOnClickListener(this);
		rl_education.setOnClickListener(this);
		rl_havechild.setOnClickListener(this);
		rl_house.setOnClickListener(this);
		rl_time.setOnClickListener(this);
		txt_save.setOnClickListener(this);
		user_info_user_pic.setOnClickListener(this);
		
	}
	
	
	/**得到图片*/
	public void initPhotos(){
		Log.i(tag, "initPhotos()");
		
		try {
			String url = HttpUtil.BASE_URL + "&toType=json&f=user"
					 + "&android_uid=" + home_uid;
			String res=HttpUtil.getRequest(url);
			JSONObject js=new  JSONObject(res);
//			Log.w(tag, url);
//			Log.w(tag, res);
			if (js.getInt("code")==1) {
				JSONObject user=js.getJSONObject("result");
//				Log.w(tag, user+"===user");
				JSONObject job=user.getJSONObject("pics");
//				Log.w(tag, job+"===job");
				JSONArray  arr=job.getJSONArray("list");
//				Log.w(tag, arr+"===arr");
				for (int i = 0; i < arr.length(); i++) {
					imageUrl[i] = arr.optJSONObject(i).getString("imgurl");
					}
				for (int i = 0; i < arr.length(); i++) {
					if (imageUrl[i] != null && !imageUrl[i].equals("")) {
						final ImageView img = new ImageView(this);
						img.setId(i);
						img.setLayoutParams(new LayoutParams(130, 110));
						img.setScaleType(ImageView.ScaleType.FIT_START);
						loadImage(HttpUtil.URL + "/" + imageUrl[i], img.getId());
						photos.addView(img);
//						Log.w(tag, img+"");
						photos.setOnLongClickListener(new OnLongClickListener() {
							
							@Override
							public boolean onLongClick(View arg0) {
								new AlertDialog.Builder(MS_ModifyActivity.this)
								.setItems(new String[] {"设置为头像","删除该照片"}, new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										switch (arg1) {
										case 0:
											//设置头像
											break;
										case 1:
											//删除照片
										default:
											break;
										}
									}
								})
								.create().show();
								
								return false;
							}
						});
					}
				}
			}
		} catch (Exception e) {
		}
	}
	/***加载图片的方法*/
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
	

	/**
	 * 初始化数组
	 * */
	private void initStrArrData() {
		Log.i(tag, "initStrArrData()");
		arrMarriage = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.marriage), 1), "未知");
		keyMarriage = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.marriage), 0), "0");
		indexMarriage = MyUtil.getIndexOfArr(marriage, keyMarriage);

		arrHeight = new String[201 - 154];
		for (int i = 0; i < arrHeight.length; i++) {
			if (i == 0) {
				arrHeight[i] = "未知";
			} else if (i == 154 - 153) {
				arrHeight[i] = "155以下";
			} else if (i == 201 - 153) {
				arrHeight[i] = "200以上";
			} else {
				arrHeight[i] = String.valueOf(153 + i);
			}
		}
		indexHeight = 0;
		arrSalary = MyUtil.getFullArr(MyUtil.getArr(
				(getResources().getStringArray(R.array.salary)), 1), "未知");
		arrChildren = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.children), 1), "未知");
		arrHouse = MyUtil.getFullArr(
				MyUtil.getArr(getResources().getStringArray(R.array.house), 1),
				"未知");
		arrEducation = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.educaction), 1), "未知");
		keyEducation = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.educaction), 0), "0");
		if (birthYear == null) {
			Calendar c = Calendar.getInstance();
			birthYear = String.valueOf(c.get(Calendar.YEAR));
			birthMonth = String.valueOf(c.get(Calendar.MONTH));
			birthDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		}
		arrProvince = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.province), 1), "未知");
		keyProvince = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.province), 0), "0");
		arrOldsex = MyUtil
				.getFullArr(MyUtil.getArr(
						getResources().getStringArray(R.array.oldsex), 1), "未知");

	}

	/**
	 * 消息处理
	 * **/
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) { // initData();
				Log.i(tag, "msg.what==1");
				try {
					JSONObject resJson = new JSONObject(msg.obj.toString());
					if (resJson.getInt("code") == 1) {
						Log.i(tag, "initData()");
						JSONObject js = resJson.getJSONObject("result");
						long birth = js.getInt("birth");
						if (birth > 0) {
							birthYear = new SimpleDateFormat("yyyy")
									.format(new Date(birth * 1000));
							birthMonth = new SimpleDateFormat("MM")
									.format(new Date(birth * 1000));
							birthDay = new SimpleDateFormat("dd")
									.format(new Date(birth * 1000));
							Log.i(tag + "+++birth", new SimpleDateFormat(
									"yyyy/MM/dd")
									.format(new Date(birth * 1000)));
							nickName = js.getString("nickname");
							marriage = js.getString("marriage");
							Log.i("marriage_code", marriage);
							height = js.getString("height");
							salary = js.getString("salary");
							province = js.getString("province");
							city = js.getString("city");
							education = js.getString("education");
							children = js.getString("children");
							house = js.getString("house");
							oldsex = js.getString("oldsex");
						}
						progressDialog.dismiss();
						showInfo();
					} else if (resJson.getInt("code") == 10000) {
						Toast.makeText(getApplicationContext(),
								msg.obj.toString(), Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "服务器异常，请稍后...",
								Toast.LENGTH_SHORT).show();
						;
					}

				} catch (Exception e) {
				}
			}
			if (msg.what == 2) {// saveUserInfo
				try {
					JSONObject res = new JSONObject(msg.obj.toString());
					if (res.getInt("code") == 1) {
						Log.i(tag, "msg.what==2");
						isInfoChange = false;
						JSONObject jsData = res.getJSONObject("result");
						login_nickname = jsData.getString("nickname");
						login_uid = jsData.getString("uid");
						login_gender = jsData.getString("gender");
						login_cid = jsData.getString("s_cid");
						login_star = jsData.getString("city_star");
						user_pic = jsData.getString("pic");
						login_province = jsData.getString("province");
						login_city = jsData.getString("city");
						login_birth = jsData.getString("birthyear");
						login_telphone = jsData.getString("telphone");
						true_name = jsData.getString("truename");
						Toast.makeText(getApplicationContext(), "修改资料提交成功",
								Toast.LENGTH_SHORT).show();
						saveUserInfo();
					}
					else if (res.getInt("code") == 10000) {
						Toast.makeText(getApplicationContext(),
								"修改资料提交失败" + "  " + msg.obj.toString(),
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(), "服务器异常，请稍后...",
								Toast.LENGTH_LONG).show();
					}

				} catch (Exception e) {
				}
			}
			if (msg.what==3) {
				try {
					JSONObject resjson = new JSONObject(msg.obj.toString());
					if (resjson.getInt("code") == 1) {
						Toast.makeText(getApplicationContext(),
								"修改头像成功,新头像需要审核，稍后才能显示", Toast.LENGTH_SHORT)
								.show();
						System.out.println("messge-->"
								+ resjson.getString("message"));
					isPicChange=false;
					}
					if (resjson.getInt("code") == 10000) {
						Toast.makeText(getApplicationContext(),
								"修改头像失败" + "  " + resjson.getString("message"),
								Toast.LENGTH_SHORT).show();
						System.out.println("messge-->"
								+ resjson.getString("message"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	};

	/** 启动第一次加载时的界面的线程 */
	Thread initData = new Thread() {
		public void run() {
			super.run();
			Message ms = new Message();
			try {
				String url = HttpUtil.getRequest(HttpUtil.BASE_URL
						+ "&f=user&toType=json&android_uid=" + home_uid);
				ms.what = 1;
				ms.obj = url;
				handler.sendMessage(ms);
			} catch (Exception e) {
				e.printStackTrace();

			}
		};
	};

	/** 传递改动以后值的线程 */
	Thread saveInfo = new Thread() {
		public void run() {
			super.run();
			Message ms = new Message();
			Map<String, String> params = new HashMap<String, String>();
			params.put("nickname", nickName);
			params.put("year", birthYear);
			params.put("month", birthMonth);
			params.put("day", birthDay);
			params.put("marriage", marriage);
			params.put("height", height);
			params.put("salary", salary);
			params.put("province", province);
			params.put("city", city);
			params.put("education", education);
			params.put("children", children);
			params.put("house", house);
			params.put("oldsex", oldsex);
			try {
				String res = HttpUtil.postRequest(HttpUtil.BASE_URL
						+ "&f=update&toType=json&android_uid=" + home_uid,
						params);
				progressDialog.dismiss();
				ms.what = 2;
				ms.obj = res;
				handler.sendMessage(ms);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	/** show info 于界面 */
	private void showInfo() {
		Log.i(tag, "showInfo()");
		if (nickName != null && !nickName.equals("")) {
			txt_nickname.setText(nickName);
		}
		if (birthYear != null && !birthYear.equals("0")) {
			txt_birthday.setText(birthYear + "-" + birthMonth + "-" + birthDay);
		}
		if (marriage != null && !marriage.equals("0")) {
			txt_marry.setText(MyUtil.getArrString(marriage, getResources()
					.getStringArray(R.array.marriage), 1));
		}
		if (height != null && !height.equals("0")) {
			indexHeight = Integer.parseInt(height) - 153;
			txt_height.setText(arrHeight[indexHeight] + "cm");
		}
		if (salary != null && !salary.equals("0")) {
			txt_income.setText(arrSalary[Integer.parseInt(salary)]);
		}
		if (province != null && !province.equals("0")) {
			txt_workplace.setText(MyUtil.getArrString(province, getResources()
					.getStringArray(R.array.province), 1));
		}
		if (city != null && !city.equals("0")) {
			txt_workplace.setText(txt_workplace.getText()
					+ MyUtil.getArrString(city,
							getResources().getStringArray(R.array.city), 1));
		}
		if (education != null && !education.equals("0")) {
			txt_education.setText(MyUtil.getArrString(education, getResources()
					.getStringArray(R.array.educaction), 1));
		}
		if (house != null && !house.equals("0")) {
			txt_house.setText(arrHouse[Integer.parseInt(house)]);
		}
		if (oldsex != null && !oldsex.equals("0")) {
			txt_time.setText(arrOldsex[Integer.parseInt(oldsex)]);
		}
		if (children != null && !children.equals("0")) {
			txt_havechild.setText(arrChildren[Integer.parseInt(children)]);
		}
	}

	/** 更改保存以后的本地缓存，即更改生效了 */
	private void saveUserInfo() {
		Log.i(tag, "saveUserInfo()");
		SharedPreferences sharedPreferences = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putString("uid", login_uid);
		editor.putString("nickname", login_nickname);
		editor.putString("telphone", login_telphone);
		editor.putString("gender", login_gender);
		editor.putString("cid", login_cid);
		editor.putString("star", login_star);
		editor.putString("pic", user_pic);
		editor.putString("province", login_province);
		editor.putString("city", login_city);
		editor.putString("truename", true_name);
		editor.commit();// 提交修改
	}

	/** 点击事件实现 **/
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.changeinfo_relayout_nickname) {
			LayoutInflater inflater = LayoutInflater.from(this);
			final View textEntryView = inflater.inflate(
					R.layout.ms_mod_nickchange, null);
			final EditText edit = (EditText) textEntryView
					.findViewById(R.id.dialog_edit_nickname);
			new AlertDialog.Builder(MS_ModifyActivity.this)
					.setTitle("昵称")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(textEntryView)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String str = edit.getText().toString();
									txt_nickname.setText("" + str);
									isInfoChange = true;
									
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
		} else if (id == R.id.changeinfo_relayout_marry) {
			new AlertDialog.Builder(MS_ModifyActivity.this)
					.setTitle("婚姻：")
					.setSingleChoiceItems(arrMarriage, indexMarriage,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									indexMarriage = arg1;
									arg0.dismiss();
									txt_marry.setText(arrMarriage[arg1]);
									if (arg1 != 0) {
										marriage = keyMarriage[arg1];
									}
									isInfoChange=true;
								}
							}).create().show();
		} else if (id == R.id.changeinfo_relayout_height) {
			new AlertDialog.Builder(MS_ModifyActivity.this)
					.setTitle("身高：")
					.setSingleChoiceItems(arrHeight, indexHeight,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
									indexHeight = arg1;
									txt_height.setText(arrHeight[arg1]);
									if (arg1 != 0) {
										height = String.valueOf(153 + arg1);
									}
									isInfoChange=true;
								}
							}).create().show();
		} else if (id == R.id.changeinfo_relayout_birthday) {
			new DatePickerDialog(MS_ModifyActivity.this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker arg0, int arg1,
								int arg2, int arg3) {
							txt_birthday.setText(arg1 + "-" + (arg2 + 1) + "-"
									+ arg3);
							birthYear = String.valueOf(arg1);
							birthMonth = String.valueOf((arg2 + 1));
							birthDay = String.valueOf(arg3);
							isInfoChange=true;
						}
					}, Integer.parseInt(birthYear),
					(Integer.parseInt(birthMonth) - 1),
					Integer.parseInt(birthDay)).show();
		} else if (id == R.id.changeinfo_relayout_income) {
			new AlertDialog.Builder(MS_ModifyActivity.this)
					.setTitle("月收入：")
					.setSingleChoiceItems(arrSalary, Integer.parseInt(salary),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
									txt_income.setText(arrSalary[arg1]);
									if (arg1 != 0) {
										salary = String.valueOf(arg1);
									}
									isInfoChange=true;
								}
							}).create().show();
		} else if (id == R.id.changeinfo_relayout_workplace) {
			pIndex = 0;
			if (!province.equals("0")) {
				pIndex = MyUtil.getIndexOfArr(province, MyUtil.getArr(
						getResources().getStringArray(R.array.province), 0));
				System.out.println("pIndex--->" + pIndex + "  province----->"
						+ province);
			}
			new AlertDialog.Builder(MS_ModifyActivity.this)
					.setTitle("省份/市：")
					.setSingleChoiceItems(arrProvince, pIndex + 1,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
									txt_workplace.setText("");
									txt_workplace.setText(arrProvince[arg1]);
									isInfoChange=true;

									if (arg1 != 0) {
										pIndex = arg1;
										province = String
												.valueOf(keyProvince[arg1]);
									}
									arrCity = MyUtil.getFullArr(
											MyUtil.getCityArr(
													getResources()
															.getStringArray(
																	R.array.city),
													province), "未知");
									if (arrCity != null) {
										new AlertDialog.Builder(
												MS_ModifyActivity.this)
												.setTitle("城市：")
												.setSingleChoiceItems(
														arrCity,
														0,
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																dialog.dismiss();
																txt_workplace
																		.setText(txt_workplace
																				.getText()
																				.toString()
																				+ " "
																				+ arrCity[which]);
																if (which != 0) {
																	city = MyUtil
																			.getArrString(
																					arrCity[which],
																					getResources()
																							.getStringArray(
																									R.array.city),
																					0);
																}
															}
														}).create().show();
									}
								}
							}).create().show();
		} else if (id == R.id.changeinfo_relayout_education) {
			educationIndex = 0;
			if (!education.equals("0")) {
				educationIndex = MyUtil.getIndexOfArr(
						education,
						MyUtil.getArr(
								getResources().getStringArray(
										R.array.educaction), 0));
				System.out.println("educationIndex--->" + educationIndex
						+ "  education----->" + education);
			}
			new AlertDialog.Builder(MS_ModifyActivity.this)
					.setTitle("最高学历：")
					.setSingleChoiceItems(arrEducation, educationIndex + 1,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
									txt_education.setText(arrEducation[arg1]);
									if (arg1 != 0) {
										educationIndex = arg1;
										education = String
												.valueOf(keyEducation[arg1]);
									}
									isInfoChange=true;
								}
							}).create().show();
		} else if (id == R.id.changeinfo_relayout_havechild) {
			new AlertDialog.Builder(MS_ModifyActivity.this)
					.setTitle("有无小孩：")
					.setSingleChoiceItems(arrChildren,
							Integer.parseInt(children),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
									txt_havechild.setText(arrChildren[arg1]);
									if (arg1 != 0) {
										children = String.valueOf(arg1);
									}
									isInfoChange=true;
								}
							}).create().show();
		} else if (id == R.id.changeinfo_relayout_house) {
			new AlertDialog.Builder(MS_ModifyActivity.this)
					.setTitle("住房情况：")
					.setSingleChoiceItems(arrHouse, Integer.parseInt(house),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
									txt_house.setText(arrHouse[arg1]);
									if (arg1 != 0) {
										house = String.valueOf(arg1);
									}
									isInfoChange=true;
								}
							}).create().show();
		} else if (id == R.id.changeinfo_relayout_time) {
			new AlertDialog.Builder(MS_ModifyActivity.this)
					.setTitle("期望多久找到对象：")
					.setSingleChoiceItems(arrOldsex, Integer.parseInt(oldsex),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
									txt_time.setText(arrOldsex[arg1]);
									if (arg1 != 0) {
										oldsex = String.valueOf(arg1);
									}
									isInfoChange=true;
								}
							}).create().show();
		}else if (id == R.id.txt_saveuserinfo) {
			nickName = txt_nickname.getText().toString().trim();
			if (isInfoChange==true&&isPicChange==false) {
				progressDialog = CustomProgressDialog.createDialog(
						MS_ModifyActivity.this, "修改资料中...");
				progressDialog.show();
				new Thread(saveInfo).start();
				 
			}else if (isInfoChange==false&&isPicChange==true) {
				progressDialog = CustomProgressDialog.createDialog(
						MS_ModifyActivity.this, "照片正在提交中...");
				progressDialog.show();
				new Thread(UploadPhoto).start();
			}else if (isInfoChange==true&&isPicChange==true) {
				progressDialog = CustomProgressDialog.createDialog(
						MS_ModifyActivity.this, "修改资料中...");
				progressDialog.show();
				new Thread(saveInfo).start();
				new Thread(UploadPhoto).start();
			} else {
				Toast.makeText(getApplicationContext(), "没有修改，无需保存",
						Toast.LENGTH_SHORT).show();
			}	
		}else if (id==R.id.user_info_user_pic) {
			//上传照片，
			menuWindow=new MySelf_Mod_Photo(MS_ModifyActivity.this,this);//shilihua
			menuWindow.showAtLocation(MS_ModifyActivity.this.findViewById(R.id.linearlayout_1), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			Log.i(tag, "menuWindow执行完毕");
			
		} else if (id == R.id.btn_take_photo) {
			menuWindow.dismiss();
			// 调用系统的拍照功能
			Intent takeintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 指定调用相机拍照后照片的储存路径
			tempFile = new File(Environment.getExternalStorageDirectory(),
					getPhotoFileName(2));
			takeintent
					.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
			startActivityForResult(takeintent, PHOTO_REQUEST_TAKEPHOTO);
		} else if (id == R.id.btn_pick_photo) {
			menuWindow.dismiss();
			Intent pickintent = new Intent(Intent.ACTION_PICK, null);
			pickintent.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(pickintent, PHOTO_REQUEST_GALLERY);
		}

	}
	
	/***获取照片，名称
	 *  使用系统当前日期加以调整作为照片的名称*/
		private String getPhotoFileName(int i) {
			Log.i(tag, "getPhotoFileName获得照片名称");
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"'IMG'_yyyyMMdd_HHmmss");
			if (i == 1) {
				return dateFormat.format(date) + "_crop.JPEG";
			} else {
				return dateFormat.format(date) + ".JPEG";
			}
		}
		/*** 拍照**/
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			Log.i(tag, "void onActivityResult拍照/选照片");
			switch (requestCode) {
			case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
				if (data != null)
					startPhotoZoom(Uri.fromFile(tempFile), 300);
				break;

			case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
				// 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
				if (data != null)
					startPhotoZoom(data.getData(), 300);
				break;

			case PHOTO_REQUEST_CUT:// 返回的结果
				if (data != null) {
					Bitmap bitmap = data.getParcelableExtra("data");
					uploadFile = saveToLocal(bitmap);
					setPicToView(bitmap);
				}
				break;
			}
			super.onActivityResult(requestCode, resultCode, data);

		}
/***
 * void startActivityForResult
 * */
		private void startPhotoZoom(Uri uri, int size) {
			Log.i(tag, "void startPhotoZoom还是拍照/选照片，裁剪");
			
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			// crop为true是设置在开启的intent中设置显示的view可以剪裁
			intent.putExtra("crop", "true");

			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);

			// outputX,outputY 是剪裁图片的宽高
			intent.putExtra("outputX", size);
			intent.putExtra("outputY", size);
			intent.putExtra("return-data", true);

			startActivityForResult(intent, PHOTO_REQUEST_CUT);
		}

		/****将进行剪裁后的图片显示到UI界面上*/
		private void setPicToView(Bitmap bitmap) {
//			Drawable drawable = new BitmapDrawable(bitmap);
//			imag_pic.setImageDrawable(drawable);
//			picChange = 1;
			isPicChange=true;
			Log.i(tag, "setPicToView显示至ui");
			
			Toast.makeText(getApplicationContext(), "照片已提交，审核通过后才可以显示！", Toast.LENGTH_LONG).show();;
		}
		
		/**保存至本地*/
		public File saveToLocal(Bitmap bitmap) {
			Log.i(tag, "saveToLocal保存至本地");
			// 需要裁剪后保存为新图片

			File f = new File(Environment.getExternalStorageDirectory(),
					getPhotoFileName(1));
			try {
				f.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			try {
				fOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			/*
			 * fileName = f.getName(); uploadFilePath = f.getPath();
			 */
			return f;

		}
		/**上传至个人空间
		 * url参数isimage=1上传头像，否则上传相册
		 * */
		Thread UploadPhoto = new Thread() {
			
			@Override
			public void run() {
				Log.i(tag, "UploadPhoto上传至网站");
				super.run();
				String request = null;
				String url = HttpUtil.BASE_URL + "&f=upload&isimage=0&android_uid="
						+ home_uid + "&toType=json";
				System.out.println("uploadFile--->" + uploadFile.getName());
				com.qingyuan.util.UploadPhoto up = new com.qingyuan.util.UploadPhoto();
				Message message = new Message();
				request = UploadUtil.uploadFile(uploadFile, url);
				progressDialog.dismiss();
				message.what = 3;
				message.obj = request;
				handler.sendMessage(message);
			}
		};
}
