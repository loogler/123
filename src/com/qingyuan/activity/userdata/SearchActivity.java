package com.qingyuan.activity.userdata;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingyuan.MainActivity;
import com.qingyuan.R;
import com.qingyuan.service.parser.MyUtil;
import com.qingyuan.util.HttpUtil;

public class SearchActivity extends Activity implements
		android.view.View.OnClickListener {
	private Button conditional, accurate;
	private Button[] buttons;
	private ViewPager mViewPager;
	private ViewGroup mViewGroup;
	private View conditionalView, accurateView;
	private List<View> pagerViews;
	private String home_uid, home_gender;

	private TextView ageText, genderText, pcText, heighText, salaryText,
			educationText, marriageText;
	private Button searchBtn, accBtn;
	private EditText accEditText;
	private int minAge, maxAge;
	private ListView lvSalary, lvEducaction, lvMarriage = null;

	// 定义返回结果的字符串
	private String strResult = null;
	// 定义URL字符串
	private String httpUrl = null;
	// 定义选择
	private String work_province, work_city;
	private String ageString = "", salaryString = "", educactionString = "",
			marriageString = "", genderString = "", pcString = "",
			heightString = "", photoString = "";
	// 全局数组
	private String[] arrGender, arrAge, arrProvince, arrKeyProvince, arrCity,
			arrSalary, arrEducaction, arrMarriage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.aty_tabrecommend_search);

		LayoutInflater inflater = getLayoutInflater();
		pagerViews = new ArrayList<View>();
		conditionalView = inflater.inflate(
				R.layout.aty_tabrecommend_search_con, null);
		genderText = (TextView) conditionalView.findViewById(R.id.genderText);
		ageText = (TextView) conditionalView.findViewById(R.id.ageText);
		pcText = (TextView) conditionalView.findViewById(R.id.pcText);
		salaryText = (TextView) conditionalView.findViewById(R.id.salaryText);
		educationText = (TextView) conditionalView
				.findViewById(R.id.educationText);
		marriageText = (TextView) conditionalView
				.findViewById(R.id.marriageText);
		searchBtn = (Button) conditionalView
				.findViewById(R.id.button_search_widely);
		genderText.setOnClickListener(this);
		ageText.setOnClickListener(this);
		pcText.setOnClickListener(this);
		salaryText.setOnClickListener(this);
		educationText.setOnClickListener(this);
		marriageText.setOnClickListener(this);
		searchBtn.setOnClickListener(this);

		accurateView = inflater.inflate(R.layout.aty_tabrecommend_search_acc,
				null);
		accBtn = (Button) accurateView
				.findViewById(R.id.button_search_accurate);
		accEditText = (EditText) accurateView
				.findViewById(R.id.edit_search_accurate);
		accBtn.setOnClickListener(this);
		accEditText.setOnClickListener(this);
		pagerViews.add(conditionalView);
		pagerViews.add(accurateView);

		mViewGroup = (ViewGroup) inflater.inflate(
				R.layout.aty_tabrecommend_search, null);
		setContentView(mViewGroup);
		buttons = new Button[pagerViews.size()];

		conditional = (Button) findViewById(R.id.btn_tabrecommend_con);
		accurate = (Button) findViewById(R.id.btn_tabrecommend_acc);
		conditional.setOnClickListener(new GuideButtonClickListener(0));
		accurate.setOnClickListener(new GuideButtonClickListener(1));
		buttons[0] = conditional;
		buttons[1] = accurate;
		mViewPager = (ViewPager) mViewGroup.findViewById(R.id.vp_search);
		mViewPager.setAdapter(new ListViewPager());
		mViewPager.setOnPageChangeListener(new GuidePageChangeListener());

		home_uid = MainActivity.home_uid;
		home_gender = MainActivity.home_gender;

	}

	/**
	 * 翻页适配器
	 * 
	 * @author Administrator
	 *
	 */
	class ListViewPager extends PagerAdapter {

		@Override
		public int getCount() {
			return buttons.length;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {

			((ViewPager) container).addView((pagerViews).get(position));
			return pagerViews.get(position);
		}

		@Override
		public int getItemPosition(Object object) {

			return super.getItemPosition(object);
		}
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {

			for (int i = 0; i < buttons.length; i++) {
				buttons[arg0].setBackgroundResource(R.color.yellow_back);
				if (arg0 != i) {
					buttons[i].setBackgroundResource(R.color.gray2);
				}
			}

		}

	}

	/**
	 * 监听点击事件tab切换 默认 index=0 加载第0页；
	 * 
	 * @author Administrator
	 *
	 */
	class GuideButtonClickListener implements OnClickListener {

		int index = 0;// 默认第一个

		/* the constructor */
		GuideButtonClickListener(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View arg0) {
			mViewPager.setCurrentItem(index, true);
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.genderText:
			arrGender = MyUtil.getArr(
					getResources().getStringArray(R.array.gender), 1);
			new AlertDialog.Builder(SearchActivity.this)
					.setTitle("你要找？")
					.setSingleChoiceItems(arrGender,
							Integer.parseInt(home_gender),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									genderText.setText(arrGender[which]);
									// home_gender = String.valueOf(which);
									genderString = "&gender=" + which;
									dialog.dismiss();
								}
							}).create().show();
			if (home_gender.equals("1")) {
				genderString = "&gender=0";
				genderText.setText("男士");
			} else {
				genderString = "&gender=1";
				genderText.setText("女士");
			}
			break;
		case R.id.ageText:
			arrAge = new String[84];
			for (int i = 0; i < arrAge.length; i++) {
				arrAge[i] = String.valueOf(16 + i);
			}
			new AlertDialog.Builder(SearchActivity.this)
					.setTitle("最小年龄：")
					.setSingleChoiceItems(arrAge, minAge - 16,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									minAge = 16 + arg1;
									ageString = "&age_start=" + minAge;
									arg0.dismiss();
									final String[] arrAge2 = new String[100 - minAge];
									for (int i = 0; i < arrAge2.length; i++) {
										arrAge2[i] = String.valueOf(minAge + i);
									}
									new AlertDialog.Builder(SearchActivity.this)
											.setTitle("最大年龄：")
											.setSingleChoiceItems(
													arrAge2,
													maxAge - 16,
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															maxAge = minAge
																	+ which;
															ageText.setText(String
																	.valueOf(minAge)
																	+ "-"
																	+ String.valueOf(maxAge)
																	+ "岁");
															ageString = "&age_end="
																	+ maxAge;
															dialog.dismiss();
														}
													}).create().show();
								}
							}).create().show();
			break;
		case R.id.pcText:
			pcText.setText(MyUtil.getArrString(work_province, getResources()
					.getStringArray(R.array.province), 1)
					+ MyUtil.getArrString(work_city, getResources()
							.getStringArray(R.array.city), 1));
			arrProvince = MyUtil.getArr(
					getResources().getStringArray(R.array.province), 1);
			arrKeyProvince = MyUtil.getArr(
					getResources().getStringArray(R.array.province), 0);
			int pIndex = MyUtil
					.getIndexOfArr(
							work_province,
							MyUtil.getArr(
									getResources().getStringArray(
											R.array.province), 0));
			new AlertDialog.Builder(SearchActivity.this)
					.setTitle("省份/市：")
					.setSingleChoiceItems(arrProvince, pIndex,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									pcString = "&work_province="
											+ arrKeyProvince[arg1];
									pcText.setText(arrProvince[arg1]);
									work_province = String
											.valueOf(arrKeyProvince[arg1]);
									arg0.dismiss();
									arrCity = MyUtil.getCityArr(getResources()
											.getStringArray(R.array.city),
											work_province);
									if (arrCity != null) {
										new AlertDialog.Builder(
												SearchActivity.this)
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
																pcString += "&work_city="
																		+ MyUtil.getArrString(
																				arrCity[which],
																				getResources()
																						.getStringArray(
																								R.array.city),
																				0);
																pcText.setText(pcText
																		.getText()
																		+ arrCity[which]);
															}
														}).create().show();
									}
								}
							}).create().show();
			break;
		case R.id.salaryText:
			arrSalary = MyUtil
					.getFullArr(
							MyUtil.getArr((getResources()
									.getStringArray(R.array.salary)), 1), "不限");
			final boolean[] boolSalary = new boolean[arrSalary.length];
			for (int i = 0; i < arrSalary.length; i++) {
				if (i == 0) {
					boolSalary[i] = true;
				} else {
					boolSalary[i] = false;
				}
			}
			final AlertDialog alertSalry = new AlertDialog.Builder(
					SearchActivity.this)
					.setTitle("月收入：")
					.setMultiChoiceItems(arrSalary, boolSalary,
							new DialogInterface.OnMultiChoiceClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									if (isChecked) {
										if (which == 0) {
											for (int j = 0; j < boolSalary.length; j++) {
												if (j != which) {
													lvSalary.setItemChecked(j,
															false);
													boolSalary[j] = false;
												}
											}
										} else {
											lvSalary.setItemChecked(0, false);
											boolSalary[0] = false;
										}
									}
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									salaryText.setText("");
									salaryString = "";
									for (int i = 0; i < arrSalary.length; i++) {
										if (lvSalary.getCheckedItemPositions()
												.get(i)) {
											if (salaryText.getText().equals("")) {
												salaryText
														.setText(arrSalary[i]);
											} else {
												salaryText.setText(salaryText
														.getText()
														+ ","
														+ arrSalary[i]);
											}
											salaryString += "&salay[]="
													+ String.valueOf(i);
										}
									}
								}
							}).setNegativeButton("取消", null).create();
			lvSalary = alertSalry.getListView();
			alertSalry.show();
			break;
		case R.id.educationText:
			arrEducaction = MyUtil.getFullArr(MyUtil.getArr(getResources()
					.getStringArray(R.array.educaction), 1), "不限");
			final boolean[] boolEducaction = new boolean[arrEducaction.length];
			for (int i = 0; i < boolEducaction.length; i++) {
				if (i == 0) {
					boolEducaction[i] = true;
				} else {
					boolEducaction[i] = false;
				}
			}
			final AlertDialog alertEducaction = new AlertDialog.Builder(
					SearchActivity.this)
					.setTitle("学历：")
					.setMultiChoiceItems(arrEducaction, boolEducaction,
							new DialogInterface.OnMultiChoiceClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1, boolean arg2) {
									if (arg2) {
										if (arg1 == 0) {
											for (int i = 0; i < boolEducaction.length; i++) {
												if (i != arg1) {
													boolEducaction[i] = false;
													lvEducaction
															.setItemChecked(i,
																	false);
												}
											}
										} else {
											boolEducaction[0] = false;
											lvEducaction.setItemChecked(0,
													false);
										}
									}
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									educationText.setText("");
									educactionString = "";
									for (int i = 0; i < arrEducaction.length; i++) {
										if (lvEducaction
												.getCheckedItemPositions().get(
														i)) {
											if (salaryText.getText().equals("")) {
												educationText
														.setText(arrEducaction[i]);
											} else {
												educationText.setText(educationText
														.getText()
														+ " "
														+ arrEducaction[i]);
											}
											educactionString += "&education[]="
													+ String.valueOf(i);
										}
									}
								}
							}).setNegativeButton("取消", null).create();
			lvEducaction = alertEducaction.getListView();
			alertEducaction.show();
			break;
		case R.id.marriageText:
			arrMarriage = MyUtil
					.getFullArr(
							MyUtil.getArr(
									getResources().getStringArray(
											R.array.marriage), 1), "不限");
			final boolean[] boolMarriage = new boolean[arrMarriage.length];
			for (int i = 0; i < boolMarriage.length; i++) {
				if (i == 0) {
					boolMarriage[i] = true;
				} else {
					boolMarriage[i] = false;
				}
			}
			final AlertDialog alertMarriage = new AlertDialog.Builder(
					SearchActivity.this)
					.setTitle("是否结婚：")
					.setMultiChoiceItems(arrMarriage, boolMarriage,
							new DialogInterface.OnMultiChoiceClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1, boolean arg2) {
									if (arg2) {
										if (arg1 == 0) {
											for (int i = 0; i < boolMarriage.length; i++) {
												if (i != arg1) {
													boolMarriage[i] = false;
													lvMarriage.setItemChecked(
															i, false);
												}
											}
										} else {
											boolMarriage[0] = false;
											lvMarriage.setItemChecked(0, false);
										}
									}
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									marriageText.setText("");
									marriageString = "";
									for (int i = 0; i < arrMarriage.length; i++) {
										if (lvMarriage
												.getCheckedItemPositions().get(
														i)) {
											if (marriageText.getText().equals(
													"")) {
												marriageText
														.setText(arrMarriage[i]);
											} else {
												marriageText.setText(marriageText
														.getText()
														+ ","
														+ arrMarriage[i]);
											}
											marriageString += "&marriage[]="
													+ String.valueOf(i);
										}
									}
								}
							}).setNegativeButton("取消", null).create();
			lvMarriage = alertMarriage.getListView();
			alertMarriage.show();
			break;
		case R.id.button_search_widely:
			conditionalSearch();
			break;
		case R.id.button_search_accurate:
			searchAccurate();
			break;

		default:
			break;
		}
	}

	private void conditionalSearch() {

		// 条件搜索的方法
		final CheckBox checkbox_photo_able = (CheckBox) findViewById(R.id.checkbox_photo_able);

		/************************* 请×××叫×××我×××分×××割×××线 *************************/
		// 判定是否需要照片
		if (checkbox_photo_able.isChecked() == true) {
			photoString = "&photo=1";
		}
		// if(checkbox_photo_able.isChecked()==false)
		else {
			photoString = "&photo=0";
		}
		// 保存httpUrl
		SharedPreferences sharedPreferences = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putString("search_url", photoString + genderString
				+ salaryString + educactionString + marriageString + ageString
				+ heightString + pcString + "&page_size=12");
		editor.commit();// 提交修改

		Intent intent = new Intent(SearchActivity.this,
				SearchResultActivity.class);
		startActivity(intent);

	}

	private void searchAccurate() {
		try {
			String inputString = accEditText.getText().toString().trim();
			if (inputString != null) {
				String res = HttpUtil.getRequest(HttpUtil.BASE_URL
						+ "&f=search&toType=json&android_uid=" + home_uid
						+ "&uid=" + inputString);
				JSONObject json = new JSONObject(res);
				int code = json.getInt("code");
				if (code == 1) {
					// 设置fuid
					SearchPersonActivity.search_person_fuid = inputString;
					// 切换页面
					Intent intent = new Intent(SearchActivity.this,
							SearchPersonActivity.class);
					startActivity(intent);
				} else if (code == 10000) {
					Toast.makeText(SearchActivity.this,
							json.getString("message"), Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(SearchActivity.this, "服务器异常，请稍后再试！",
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(SearchActivity.this, "ID还没输入呢！",
						Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e1) {
			Toast.makeText(SearchActivity.this, "服务器异常，请稍后再试！",
					Toast.LENGTH_SHORT).show();
			e1.printStackTrace();
		}
	}
}
