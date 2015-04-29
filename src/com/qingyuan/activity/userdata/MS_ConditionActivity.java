package com.qingyuan.activity.userdata;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.qingyuan.R;
import com.qingyuan.service.parser.MyUtil;
import com.qingyuan.util.CustomProgressDialog;
import com.qingyuan.util.HttpUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MS_ConditionActivity extends Activity implements OnClickListener {
	// 被点击的布局
	private RelativeLayout relayout_age, relayout_height, relayout_workplace,
			relayout_marry, relayout_salary, relayout_education,
			relayout_havechild, relayout_photo, relayout_character,
			relayout_figure, relayout_weight, relayout_occupation,
			relayout_nation, relayout_hukou, relayout_smoking, relayout_drink,
			relayout_wantchild;
	// 被选择的layout中的textview.setText(xxx);
	private TextView txt_age1, txt_age2, txt_height1, txt_height2,
			txt_workplace, txt_marry, txt_salary, txt_education, txt_havechild,
			txt_photo, txt_character, txt_figure, txt_weight1, txt_weight2,
			txt_occupation, txt_nation, txt_hukou, txt_smoking, txt_drink,
			txt_wantchild;
	// 被选择的一组网页资源
	private String[] arrAge, arrHeight, arrProvince, keyProvince, arrCity,
			arrMarriage, arrSalary, arrEducation, arrChildren, arrPhoto,
			arrCharacter, arrFigure, arrWeight, arrOccupation, arrNation,
			arrSmoking, arrDrink, arrWantchild;

	// 身高，体重等数字选项
	private int test_age1, test_age2, test_height1, test_height2, test_weight1,
			test_weight2;
	// 网页获取的数值，可以被设置
	private String get_age1, get_age2, get_height1, get_height2,
			get_workprovince, get_workcity, get_marriage, get_salary,
			get_education, get_children, get_hasphoto, get_nature, get_body,
			get_weight1, get_weight2, get_occupation, get_nation,
			get_hometowncity, get_drinking, get_smoking, get_wantchild;
	// 获取数值给编号
	private int index_age1, index_age2, index_height1, index_height2,
			index_province, index_city, index_marriage, index_salary,
			index_education, index_children, index_photo, index_character,
			index_figure, index_weight1, index_weight2, index_occupation,
			index_nation, index_smoking, index_drink, index_wantchild;
	//
	private CustomProgressDialog progressDialog = null;

	// 用户id
	private String user_id, province;
	// 保存按钮
	private TextView txt_save;
	String tag = "MS_ConditionActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ms_conditionactivity);
		SharedPreferences sp = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		user_id = sp.getString("uid", "");
		initUI();
		initArr();
		progressDialog = CustomProgressDialog.createDialog(
				MS_ConditionActivity.this, "加载中...");
		progressDialog.show();
		new Thread(getUserInfo).start();
	}

	private void initArr() {
		arrAge = new String[82 - 18];
		for (int i = 0; i < arrAge.length; i++) {
			if (i == 0) {
				arrAge[i] = "不限";
			} else {
				arrAge[i] = String.valueOf(17 + i);
			}
		}

		arrHeight = new String[201 - 153];
		for (int i = 0; i < arrHeight.length; i++) {
			if (i == 0) {
				arrHeight[i] = "不限";
			} else if (i == 154 - 153) {
				arrHeight[i] = "155以下";
			} else if (i == 201 - 154) {
				arrHeight[i] = "200以上";
			} else {
				arrHeight[i] = String.valueOf(153 + i);
			}
		}

		arrWeight = new String[81 - 38];
		for (int i = 0; i < arrWeight.length; i++) {
			if (i == 0) {
				arrWeight[i] = "不限";
			} else if (i == 39 - 38) {
				arrWeight[i] = "40以下";
			} else if (i == 81 - 39) {
				arrWeight[i] = "80以上";
			} else {
				arrWeight[i] = String.valueOf(38 + i);
			}
		}

		arrProvince = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.province), 1), "不限");

		keyProvince = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.province), 0), "0");

		arrMarriage = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.marriage), 1), "不限");

		arrSalary = MyUtil.getFullArr(MyUtil.getArr(
				(getResources().getStringArray(R.array.salary)), 1), "不限");

		arrEducation = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.educaction), 1), "不限");

		arrChildren = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.children), 1), "不限");

		arrPhoto = MyUtil.getFullArr(
				MyUtil.getArr(getResources().getStringArray(R.array.photo), 1),
				"不限");

		arrCharacter = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.characte), 1), "不限");

		arrFigure = MyUtil.getFullArr(
				MyUtil.getArr(getResources().getStringArray(R.array.body), 1),
				"不限");

		arrOccupation = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.occupation), 1), "不限");

		arrNation = MyUtil
				.getFullArr(MyUtil.getArr(
						getResources().getStringArray(R.array.nation), 1), "不限");

		arrSmoking = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.smoking), 1), "不限");

		arrDrink = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.drinking), 1), "不限");

		arrWantchild = MyUtil.getFullArr(MyUtil.getArr(getResources()
				.getStringArray(R.array.wantchild), 1), "不限");

	}

	private void initUI() {

		relayout_age = (RelativeLayout) findViewById(R.id.condition_relayout_age);
		relayout_height = (RelativeLayout) findViewById(R.id.condition_relayout_height);
		relayout_workplace = (RelativeLayout) findViewById(R.id.condition_relayout_workplace);
		relayout_marry = (RelativeLayout) findViewById(R.id.condition_relayout_marry);
		relayout_salary = (RelativeLayout) findViewById(R.id.condition_relayout_salary);
		relayout_education = (RelativeLayout) findViewById(R.id.condition_relayout_education);
		relayout_havechild = (RelativeLayout) findViewById(R.id.condition_relayout_havechild);
		relayout_photo = (RelativeLayout) findViewById(R.id.condition_relayout_photo);
		relayout_character = (RelativeLayout) findViewById(R.id.condition_relayout_character);
		relayout_figure = (RelativeLayout) findViewById(R.id.condition_relayout_figure);
		relayout_weight = (RelativeLayout) findViewById(R.id.condition_relayout_weight);
		relayout_occupation = (RelativeLayout) findViewById(R.id.condition_relayout_occupation);
		relayout_nation = (RelativeLayout) findViewById(R.id.condition_relayout_nation);
		relayout_hukou = (RelativeLayout) findViewById(R.id.condition_relayout_hukou);
		relayout_smoking = (RelativeLayout) findViewById(R.id.condition_relayout_smoking);
		relayout_drink = (RelativeLayout) findViewById(R.id.condition_relayout_drink);
		relayout_wantchild = (RelativeLayout) findViewById(R.id.condition_relayout_wantchild);

		txt_height1 = (TextView) findViewById(R.id.condition_txt_height1);
		txt_height2 = (TextView) findViewById(R.id.condition_txt_height2);
		txt_age1 = (TextView) findViewById(R.id.condition_txt_age1);
		txt_age2 = (TextView) findViewById(R.id.condition_txt_age2);
		txt_workplace = (TextView) findViewById(R.id.condition_txt_workplace);
		txt_marry = (TextView) findViewById(R.id.condition_txt_marry);
		txt_salary = (TextView) findViewById(R.id.condition_txt_salary);
		txt_education = (TextView) findViewById(R.id.condition_txt_education);
		txt_havechild = (TextView) findViewById(R.id.condition_txt_havechild);
		txt_photo = (TextView) findViewById(R.id.condition_txt_photo);
		txt_character = (TextView) findViewById(R.id.condition_txt_character);
		txt_figure = (TextView) findViewById(R.id.condition_txt_figure);
		txt_weight1 = (TextView) findViewById(R.id.condition_txt_weight1);
		txt_weight2 = (TextView) findViewById(R.id.condition_txt_weight2);
		txt_occupation = (TextView) findViewById(R.id.condition_txt_occupation);
		txt_nation = (TextView) findViewById(R.id.condition_txt_nation);
		txt_hukou = (TextView) findViewById(R.id.condition_txt_hukou);
		txt_smoking = (TextView) findViewById(R.id.condition_txt_smoking);
		txt_drink = (TextView) findViewById(R.id.condition_txt_drink);
		txt_wantchild = (TextView) findViewById(R.id.condition_txt_wantchild);

		txt_save = (TextView) findViewById(R.id.txt_savecondotion);

		relayout_age.setOnClickListener(this);
		relayout_height.setOnClickListener(this);
		relayout_workplace.setOnClickListener(this);
		relayout_marry.setOnClickListener(this);
		relayout_salary.setOnClickListener(this);
		relayout_education.setOnClickListener(this);
		relayout_havechild.setOnClickListener(this);
		relayout_photo.setOnClickListener(this);
		relayout_character.setOnClickListener(this);
		relayout_figure.setOnClickListener(this);
		relayout_weight.setOnClickListener(this);
		relayout_occupation.setOnClickListener(this);
		relayout_nation.setOnClickListener(this);
		relayout_hukou.setOnClickListener(this);
		relayout_smoking.setOnClickListener(this);
		relayout_drink.setOnClickListener(this);
		relayout_wantchild.setOnClickListener(this);

		txt_save.setOnClickListener(this);

	}

	/**
	 * 首次加载时 需要将默认的界面呈现于ui，调用此线程。。。。
	 * **/
	Thread getUserInfo = new Thread() {
		public void run() {
			super.run();
			Message msg = new Message();
			try {
				String res = HttpUtil.getRequest(HttpUtil.BASE_URL
						+ "&f=user&toType=json&android_uid=" + user_id);
				msg.what = 1;
				msg.obj = res;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};
	/**
	 * 设置用户信息，将改变的内容变成一串string，保存
	 * 
	 * **/
	Thread setUserInfo = new Thread() {

		public void run() {
			super.run();
			Message msg = new Message();
			Map<String, String> params = new HashMap<String, String>();
			params.put("age1", get_age1);
			params.put("age2", get_age2);
			params.put("height1", get_height1);
			params.put("height2", get_height2);
			params.put("workProvince", get_workprovince);
			params.put("workCity", get_workcity);
			params.put("marriage2", get_marriage);
			params.put("salary1", get_salary);
			params.put("education2", get_education);
			params.put("children2", get_children);
			params.put("hasphoto", get_hasphoto);
			params.put("nature2", get_nature);
			params.put("body2", get_body);

			params.put("weight1", get_weight1);
			params.put("weight2", get_weight2);
			params.put("occupation2", get_occupation);
			params.put("stock2", get_nation);
			params.put("hometownCity2", get_hometowncity);
			params.put("drinking", (Integer.parseInt(get_drinking) - 1) + "");
			params.put("smoking", (Integer.parseInt(get_smoking) - 1) + "");
			params.put("wantchildren2", get_wantchild);
			Log.d(tag, params.toString());
			try {
				String res = HttpUtil.postRequest(HttpUtil.BASE_URL
						+ "&f=update&toType=json&android_uid=" + user_id,
						params);
				msg.what = 2;
				msg.obj = res;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};

	};

	/**
	 * 用于将得到的更改内容呈现于界面
	 * */
	private void showUserInfo() {
		Log.i(tag, "showUserInfo");
		if (get_age1 != null && !get_age1.equals("")) {
			txt_age1.setText(get_age1);
			index_age1 = Integer.parseInt(get_age1) - 18;
		}
		if (get_age2 != null && !get_age2.equals("0")) {
			txt_age2.setText(get_age2);
			index_age2 = Integer.parseInt(get_age2) - 18;
		}
		if (get_height1 != null && !get_height1.equals("0")) {
			txt_height1.setText(get_height1);
			index_height1 = Integer.parseInt(get_height1) - 153;
		}
		if (get_height2 != null && !get_height2.equals("0")) {
			txt_height2.setText(get_height2);
			index_height2 = Integer.parseInt(get_height2) - 153;
		}
		if (get_workprovince != null && !get_workprovince.equals("0")) {
			txt_workplace.setText(MyUtil.getArrString(get_workprovince,
					getResources().getStringArray(R.array.province), 1));
			index_province = MyUtil
					.getIndexOfArr(
							get_workprovince,
							MyUtil.getArr(
									getResources().getStringArray(
											R.array.province), 0));
		}
		if (get_workcity != null && !get_workcity.equals("0")) {
			txt_workplace.setText(txt_workplace.getText()
					+ " "
					+ MyUtil.getArrString(get_workcity, getResources()
							.getStringArray(R.array.city), 1));
			index_city = MyUtil.getIndexOfArr(get_workcity, MyUtil.getArr(
					getResources().getStringArray(R.array.city), 0));
		}
		if (get_marriage != null && !get_marriage.equals("0")) {
			txt_marry.setText(MyUtil.getArrString(get_marriage, getResources()
					.getStringArray(R.array.marriage), 1));
			index_marriage = MyUtil
					.getIndexOfArr(
							get_marriage,
							MyUtil.getArr(
									getResources().getStringArray(
											R.array.marriage), 0));
		}
		if (get_salary != null && !get_salary.equals("0")) {
			txt_salary.setText(MyUtil.getArrString(get_salary, getResources()
					.getStringArray(R.array.salary), 1));
			index_salary = MyUtil.getIndexOfArr(get_salary, MyUtil.getArr(
					getResources().getStringArray(R.array.salary), 0));
		}
		if (get_education != null && !get_education.equals("0")) {
			txt_education.setText(MyUtil.getArrString(get_education,
					getResources().getStringArray(R.array.educaction), 1));
			index_education = MyUtil.getIndexOfArr(get_education, MyUtil
					.getArr(getResources().getStringArray(R.array.educaction),
							0));
		}

		if (get_children != null && !get_children.equals("0")) {
			txt_havechild.setText(MyUtil.getArrString(get_children,
					getResources().getStringArray(R.array.children), 1));
			index_children = MyUtil
					.getIndexOfArr(
							get_children,
							MyUtil.getArr(
									getResources().getStringArray(
											R.array.children), 0));
		}
		if (get_hasphoto != null && !get_hasphoto.equals("0")) {
			txt_photo.setText(MyUtil.getArrString(get_hasphoto, getResources()
					.getStringArray(R.array.photo), 1));
			index_photo = MyUtil.getIndexOfArr(get_hasphoto, MyUtil.getArr(
					getResources().getStringArray(R.array.photo), 0));
		}
		if (get_nature != null && !get_nature.equals("0")) {
			txt_character.setText(MyUtil.getArrString(get_nature,
					getResources().getStringArray(R.array.characte), 1));
			index_character = MyUtil
					.getIndexOfArr(
							get_nature,
							MyUtil.getArr(
									getResources().getStringArray(
											R.array.characte), 0));
		}
		if (get_body != null && !get_body.equals("0")) {
			txt_figure.setText(MyUtil.getArrString(get_body, getResources()
					.getStringArray(R.array.body), 1));
			index_figure = MyUtil.getIndexOfArr(get_body, MyUtil.getArr(
					getResources().getStringArray(R.array.body), 0));
		}
		if (get_weight1 != null && !get_weight1.equals("0")) {
			txt_weight1.setText(get_weight1);
			index_weight1 = Integer.parseInt(get_weight1) - 38;

		}
		if (get_weight2 != null && !get_weight2.equals("0")) {
			txt_weight2.setText(get_weight2);
			index_weight2 = Integer.parseInt(get_weight2) - 38;
		}
		if (get_occupation != null && !get_occupation.equals("0")) {
			txt_occupation.setText(MyUtil.getArrString(get_occupation,
					getResources().getStringArray(R.array.occupation), 1));
			index_occupation = MyUtil.getIndexOfArr(get_occupation, MyUtil
					.getArr(getResources().getStringArray(R.array.occupation),
							0));
		}
		if (get_nation != null && !get_nation.equals("0")) {
			txt_nation.setText(MyUtil.getArrString(get_nation, getResources()
					.getStringArray(R.array.nation), 1));
			index_nation = MyUtil.getIndexOfArr(get_nation, MyUtil.getArr(
					getResources().getStringArray(R.array.nation), 0));
		}
		if (get_hometowncity != null && !get_hometowncity.equals("0")) {
			txt_hukou.setText(MyUtil.getArrString(get_hometowncity,
					getResources().getStringArray(R.array.city), 1));
		}
		if (get_drinking != null && !get_drinking.equals("0")) {
			txt_drink.setText(MyUtil.getArrString(get_drinking, getResources()
					.getStringArray(R.array.drinking), 1));
			index_drink = MyUtil
					.getIndexOfArr(
							get_drinking,
							MyUtil.getArr(
									getResources().getStringArray(
											R.array.drinking), 0));
		}
		if (get_smoking != null && !get_smoking.equals("0")) {
			txt_smoking.setText(MyUtil.getArrString(get_smoking, getResources()
					.getStringArray(R.array.smoking), 1));
			index_smoking = MyUtil.getIndexOfArr(get_smoking, MyUtil.getArr(
					getResources().getStringArray(R.array.smoking), 0));
		}
		if (get_wantchild != null && !get_wantchild.equals("0")) {
			txt_wantchild.setText(MyUtil.getArrString(get_wantchild,
					getResources().getStringArray(R.array.wantchild), 1));
			index_wantchild = MyUtil
					.getIndexOfArr(
							get_wantchild,
							MyUtil.getArr(
									getResources().getStringArray(
											R.array.wantchild), 0));
		}

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				Log.i(tag, "handleMessage+1");
				try {
					JSONObject js = new JSONObject(msg.obj.toString());
					if (js.getInt("code") == 1) {
						JSONObject res = js.getJSONObject("result");
						JSONObject choice = res.getJSONObject("members_choice");
						get_age1 = choice.getString("age1");
						get_age2 = choice.getString("age2");
						get_height1 = choice.getString("height1");
						get_height2 = choice.getString("height2");
						get_workprovince = choice.getString("workprovince");

						get_workcity = choice.getString("workcity");
						get_marriage = choice.getString("marriage");
						get_salary = choice.getString("salary");
						get_education = choice.getString("education");
						get_children = choice.getString("children");
						get_hasphoto = choice.getString("hasphoto");

						get_nature = choice.getString("nature");
						get_body = choice.getString("body");
						get_weight1 = choice.getString("weight1");
						get_weight2 = choice.getString("weight2");
						get_occupation = choice.getString("occupation");
						get_nation = choice.getString("nation");

						get_hometowncity = choice.getString("hometowncity");
						get_drinking = choice.getString("drinking");
						get_smoking = choice.getString("smoking");
						get_wantchild = choice.getString("wantchildren");

						get_drinking = (Integer.parseInt(get_drinking) + 1)
								+ "";
						get_smoking = (Integer.parseInt(get_smoking) + 1) + "";

						progressDialog.dismiss();
						showUserInfo();
					} else if (js.getInt("code") == 10000) {
						Toast.makeText(getApplicationContext(),
								js.getJSONObject("message").toString(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "",
								Toast.LENGTH_LONG).show();
						;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (msg.what == 2) {
				Log.i(tag, "handleMessage+2");
				try {
					JSONObject rs = new JSONObject(msg.obj.toString());
					if (rs.getInt("code") == 1) {
						Toast.makeText(getApplicationContext(), "修改资料成功",
								Toast.LENGTH_SHORT).show();
						new Thread(getUserInfo).start();
					} else if (rs.getInt("code") == 10000) {
						Toast.makeText(
								getApplicationContext(),
								"修改资料失败" + "  "
										+ rs.getString("message").toString(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "服务器异常,修改资料失败",
								Toast.LENGTH_SHORT).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					new Thread(getUserInfo).start();
				}
			}

		};
	};

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.condition_relayout_age:
			new AlertDialog.Builder(MS_ConditionActivity.this).setTitle("")
			.setSingleChoiceItems(arrAge, index_age1+1, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.dismiss();
					test_age1 = arg1;
//					str_age2 = arrAge[arg1];
					if (arrAge != null) {
						new AlertDialog.Builder(
								MS_ConditionActivity.this)
								.setTitle("最大年龄：")
								.setSingleChoiceItems(arrAge,index_age2+1,new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,int which) {
												dialog.dismiss();
												test_age2 = which;
//												str_age2 = arrAge[which];
												if(test_age1 > test_age2){
													Toast.makeText(getApplicationContext(), "年龄选择错误", Toast.LENGTH_SHORT).show();
												}else{
													txt_age1.setText(arrAge[test_age1]);
													txt_age2.setText(arrAge[test_age2]);
													if(test_age1 != 0){
														get_age1 = arrAge[test_age1];
													}
													if(test_age2 != 0){
														get_age2 = arrAge[test_age2];
													}
													index_age1 = test_age1-1;
													index_age2 = test_age2-1;
												}
											}
										}).create().show();
					}
					
					
				}
			}).create().show();
			break;
		case R.id.condition_relayout_height:
			new AlertDialog.Builder(MS_ConditionActivity.this).setTitle("")
			.setSingleChoiceItems(arrHeight, index_height1, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.dismiss();
//					str_height1 = arrHeight[arg1];
					test_height1 = arg1;
					if (arrAge != null) {
						new AlertDialog.Builder(
								MS_ConditionActivity.this)
								.setTitle("最大身高：")
								.setSingleChoiceItems(arrHeight,index_height2,new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,int which) {
												dialog.dismiss();
//												str_height2 = arrHeight[which];
												test_height2 = which;
												if(test_height1 > test_height2){
													Toast.makeText(getApplicationContext(), "身高选择错误", Toast.LENGTH_SHORT).show();
												}else{
													txt_height1.setText(arrHeight[test_height1]);
													txt_height2.setText(arrHeight[test_height2]);
													if(test_height1 != 0){
														get_height1 = arrHeight[test_height1];
													}
													if(test_height2 != 0){
														get_height2 = arrHeight[test_height2];
													}
													index_height1 = test_height1;
													index_height2 = test_height2;
												}
											}
										}).create().show();
					}
				}
			}).create().show();
			
			break;
		case R.id.condition_relayout_workplace:
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("省份/市：")
			.setSingleChoiceItems(arrProvince, index_province+1,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0,int arg1) {
							arg0.dismiss();
							txt_workplace.setText("");
							txt_workplace.setText(arrProvince[arg1]);

							if (arg1 != 0) {
								index_province = arg1-1;
								get_workprovince = String.valueOf(keyProvince[arg1]);
							}
							if(arg1 ==0){
								return;
							}
							arrCity = MyUtil.getFullArr(MyUtil.getCityArr(getResources().getStringArray(R.array.city),get_workprovince), "不限");
							if (arrCity != null) {
								new AlertDialog.Builder(
										MS_ConditionActivity.this)
										.setTitle("城市：")
										.setSingleChoiceItems(arrCity,index_city+1,new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,int which) {
														dialog.dismiss();
														txt_workplace.setText(txt_workplace.getText().toString()+ " "+ arrCity[which]);
														if (which != 0) {
															index_city = which-1;
															get_workcity = MyUtil.getArrString(arrCity[which],getResources().getStringArray(R.array.city),0);
														}
													}
												}).create().show();
							}
						}
					}).create().show();
			break;
		case R.id.condition_relayout_marry:
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("婚姻：")
			.setSingleChoiceItems(arrMarriage, index_marriage+1,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							arg0.dismiss();
							txt_marry.setText(arrMarriage[arg1]);
							if(arg1 != 0){
							index_marriage = arg1-1;
							get_marriage = MyUtil.getArrString(arrMarriage[arg1],getResources().getStringArray(R.array.marriage),0);
							}
						}
					}).create().show();
			break;
		case R.id.condition_relayout_salary:
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("月收入：")
			.setSingleChoiceItems(arrSalary, index_salary+1,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							arg0.dismiss();
							txt_salary.setText(arrSalary[arg1]);
							if (arg1 != 0) {
								index_salary = arg1-1;
								get_salary = MyUtil.getArrString(arrSalary[arg1],getResources().getStringArray(R.array.salary),0);
							}
						}
					}).create().show();
			break;
		case R.id.condition_relayout_education:
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("最高学历：")
			.setSingleChoiceItems(arrEducation, index_education+1,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							arg0.dismiss();
							txt_education.setText(arrEducation[arg1]);
							if (arg1 != 0) {
								index_education = arg1-1;
								get_education = MyUtil.getArrString(arrEducation[arg1],getResources().getStringArray(R.array.educaction),0);
							}
						}
					}).create().show();
			break;
		case R.id.condition_relayout_havechild:
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("有无小孩：")
			.setSingleChoiceItems(arrChildren,
					index_children+1,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							arg0.dismiss();
							txt_havechild.setText(arrChildren[arg1]);
							if (arg1 != 0) {
								index_children = arg1-1;
								get_children =  MyUtil.getArrString(arrChildren[arg1],getResources().getStringArray(R.array.children),0);
							}
						}
					}).create().show();
			
			break;
		case R.id.condition_relayout_photo:
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("有无照片：")
			.setSingleChoiceItems(arrPhoto,
					index_photo+1,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							arg0.dismiss();
							txt_photo.setText(arrPhoto[arg1]);
							if (arg1 != 0) {
								index_photo = arg1-1;
								get_hasphoto =  MyUtil.getArrString(arrPhoto[arg1],getResources().getStringArray(R.array.photo),0);
							}
						}
					}).create().show();
			break;
			
		case R.id.condition_relayout_character:
			
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("性格：")
			.setSingleChoiceItems(arrCharacter,
					index_character+1,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							arg0.dismiss();
							txt_character.setText(arrCharacter[arg1]);
							if (arg1 != 0) {
								index_character = arg1-1;
								get_nature = MyUtil.getArrString(arrCharacter[arg1],getResources().getStringArray(R.array.characte),0);
							}
						}
					}).create().show();
			break;
			
		case R.id.condition_relayout_figure:
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("体型：")
			.setSingleChoiceItems(arrFigure,
					index_figure+1,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							arg0.dismiss();
							txt_figure.setText(arrFigure[arg1]);
							if (arg1 != 0) {
								index_figure = arg1-1;
								get_body = MyUtil.getArrString(arrFigure[arg1],getResources().getStringArray(R.array.body),0);
							}
						}
					}).create().show();
			
			break;
		case R.id.condition_relayout_weight:
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("最小体重：")
			.setSingleChoiceItems(arrWeight, index_weight1,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0,int arg1) {
							arg0.dismiss();
//							str_weight1 = arrWeight[arg1];
							test_weight1 = arg1;
							if (arrAge != null) {
								new AlertDialog.Builder(
										MS_ConditionActivity.this)
										.setTitle("最大体重：")
										.setSingleChoiceItems(arrWeight,index_weight2,new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,int which) {
														dialog.dismiss();
//														str_weight2 = arrWeight[which];
														test_weight2 = which;
														if(test_weight1 > test_weight2){
															Toast.makeText(getApplicationContext(), "体重选择错误", Toast.LENGTH_SHORT).show();
														}else{
															txt_weight1.setText(arrWeight[test_weight1]);
															txt_weight2.setText(arrWeight[test_weight2]);
															if(test_weight1 != 0){
																get_weight1 = arrWeight[test_weight1];
															}
															if(test_weight2 != 0){
																get_weight2 = arrWeight[test_weight2];
															}
															index_weight1 = test_weight1;
															index_weight2 = test_weight2;
														}
													}
												}).create().show();
							}
						}
					}).create().show();
			break;
			
		case R.id.condition_relayout_occupation:
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("职业：")
			.setSingleChoiceItems(arrOccupation,
					index_occupation+1,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							arg0.dismiss();
							txt_occupation.setText(arrOccupation[arg1]);
							if (arg1 != 0) {
								index_occupation = arg1-1;
								get_occupation = MyUtil.getArrString(arrOccupation[arg1],getResources().getStringArray(R.array.occupation),0);
							}
						}
					}).create().show();
			
			break;
		case R.id.condition_relayout_nation:
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("民族：")
			.setSingleChoiceItems(arrNation,
					index_nation+1,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							arg0.dismiss();
							txt_nation.setText(arrNation[arg1]);
							if (arg1 != 0) {
								index_nation = arg1-1;
								get_nation = MyUtil.getArrString(arrNation[arg1],getResources().getStringArray(R.array.nation),0);
							}
						}
					}).create().show();
			break;
		case R.id.condition_relayout_hukou:
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("省份/市：")
			.setSingleChoiceItems(arrProvince, 0,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0,int arg1) {
							arg0.dismiss();
							txt_hukou.setText("");
							txt_hukou.setText(arrProvince[arg1]);

							if (arg1 != 0) {
								//pIndex = arg1;
								province = String.valueOf(keyProvince[arg1]);
							}if(arg1 ==0){
								return;
							}
							arrCity = MyUtil.getFullArr(MyUtil.getCityArr(getResources().getStringArray(R.array.city),province), "不限");
							if (arrCity != null) {
								new AlertDialog.Builder(
										MS_ConditionActivity.this)
										.setTitle("城市：")
										.setSingleChoiceItems(arrCity,0,new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,int which) {
														dialog.dismiss();
														txt_hukou.setText(txt_hukou.getText().toString()+ " "+ arrCity[which]);
														if (which != 0) {
															get_hometowncity = MyUtil.getArrString(arrCity[which],getResources().getStringArray(R.array.city),0);
														}
													}
												}).create().show();
							}
						}
					}).create().show();
			break;
		case R.id.condition_relayout_smoking:
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("抽烟：")
			.setSingleChoiceItems(arrSmoking,
					index_smoking,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							arg0.dismiss();
							txt_smoking.setText(arrSmoking[arg1]);
							if (arg1 != 0) {
								index_smoking = arg1;
								get_smoking = MyUtil.getArrString(arrSmoking[arg1],getResources().getStringArray(R.array.smoking),0);
							}
						}
					}).create().show();
			break;
			
		case R.id.condition_relayout_drink:
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("喝酒：")
			.setSingleChoiceItems(arrDrink,
					index_drink,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							arg0.dismiss();
							txt_drink.setText(arrDrink[arg1]);
							if (arg1 != 0) {
								index_drink = arg1;
								get_drinking = MyUtil.getArrString(arrDrink[arg1],getResources().getStringArray(R.array.drinking),0);
							}
						}
					}).create().show();
			break;
			
		case R.id.condition_relayout_wantchild:
			new AlertDialog.Builder(MS_ConditionActivity.this)
			.setTitle("是否想要孩子：")
			.setSingleChoiceItems(arrWantchild,
					index_wantchild+1,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							arg0.dismiss();
							txt_wantchild.setText(arrWantchild[arg1]);
							if (arg1 != 0) {
								index_wantchild = arg1-1;
								get_wantchild = MyUtil.getArrString(arrWantchild[arg1],getResources().getStringArray(R.array.wantchild),0);
							}
						}
					}).create().show();
			break;
		case R.id.txt_savecondotion:
			progressDialog = CustomProgressDialog.createDialog(MS_ConditionActivity.this, "保存中...");
			progressDialog.show();
			new Thread(setUserInfo).start();
			break;
		default:
			break;
		}
	}
}
