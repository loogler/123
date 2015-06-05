package com.qingyuan.activity.userdata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingyuan.MainActivity;
import com.qingyuan.R;
import com.qingyuan.modem.photo.ImagePagerActivity;
import com.qingyuan.modem.photo.MyGridAdapter;
import com.qingyuan.service.parser.MyUtil;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.HttpUtil;

public class MS_ViewDataActivity extends Activity {
	String tag = "MS_ViewDataActivity";
	// layout相关的View
	private TextView tv_username, tv_user_nick, tv_place_age_gender, tv_level;
	// json 数据
	private String home_uid, introduce;// 用户id
	// 资料
	private String nickname, gender, birthyear, age, province, city, marriage,
			education, salary, house, children, height, s_cid, city_star,
			weight, body, smoking, drinking, ocupation, vehicle, wantchildren;
	// 择偶条件
	private String fage_min, fage_max, fwork_province, fwork_city, fmarriage,
			feducation, fsalary, fchildren, fheight_min, fheight_max, fbody,
			fweight_min, fweight_max, foccupation, fwantchildren, fdrinking,
			fsmoking;
	// 照片墙
	private GridView grid;
	// 资料展开页
	private ExpandableListView mListView;
	String[] imageUrl;// lu jing

	ProgressDialog progressDialog;

	AsyncImageLoader2 asyncImageLoader2 = new AsyncImageLoader2();
	// 数据处理类
	List<String> parent = new ArrayList<String>();
	Map<String, List<String>> map = new HashMap<String, List<String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ms_viewdataactivity);
		SharedPreferences sp = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		home_uid = sp.getString("uid", null);

		tv_username = (TextView) findViewById(R.id.username);
		tv_user_nick = (TextView) findViewById(R.id.nickname);
		tv_place_age_gender = (TextView) findViewById(R.id.place_gender_age);
		tv_level = (TextView) findViewById(R.id.s_cid);
		grid = (GridView) findViewById(R.id.grid);
		mListView = (ExpandableListView) this.findViewById(R.id.mlistview);
		entity();

		mListView.setAdapter(new MyAdapter());
	}

	/** 加载登录,初始化界面 **/
	void entity() {

		String url = HttpUtil.BASE_URL + "&toType=json&f=user"
				+ "&android_uid=" + home_uid;

		try {
			String res = HttpUtil.getRequest(url);
			JSONObject js = new JSONObject(res);
			if (js.getInt("code") == 1) {
				JSONObject user = js.getJSONObject("result");

				loadImage(HttpUtil.URL + "/" + user.getString("pic"),
						R.id.user_photo);
				// ////////////////////////////////////////资料获得区
				// /////////////////////////////////////////////
				nickname = user.getString("nickname");
				String sex = user.getString("gender");
				gender = sex == "0" ? "女" : "男";
				birthyear = user.getString("birthyear");
				age = (Calendar.getInstance().get(Calendar.YEAR) - Integer
						.parseInt(birthyear)) + "";
				String p = user.getString("province");
				String c = user.getString("city");
				String ps[] = getResources().getStringArray(R.array.province);
				String cs[] = getResources().getStringArray(R.array.city);

				if (p != null && !p.equals("0")) {
					province = MyUtil.getArrString(p, ps, 1);
				} else {
					province = "未知地区";
				}
				if (c != null && !c.equals("0")) {
					city = MyUtil.getArrString(c, cs, 1);
				} else {
					city = " ";
				}
				String ma = user.getString("marriage");
				if (ma.equals("1")) {
					marriage = "未婚";
				} else if (ma.equals("3")) {
					marriage = "离异";
				} else if (ma.equals("4")) {
					marriage = "丧偶";
				} else {
					marriage = "未知";
				}
				String edu = user.getString("education");
				if (edu.equals("3")) {
					education = "高中及以下";
				} else if (edu.equals("4")) {
					education = "大专";
				} else if (edu.equals("5")) {
					education = "大学本科";
				} else if (edu.equals("6")) {
					education = "硕士";
				} else if (edu.equals("7")) {
					education = "博士";
				} else {
					education = "未知学历";
				}

				String sal = user.getString("salary");
				if (sal != null && !sal.equals("0")) {
					salary = MyUtil.getArrString(sal, getResources()
							.getStringArray(R.array.salary), 1);
				} else {
					salary = "未知收入";
				}
				String hou = user.getString("house");
				if (hou != null && !hou.equals("0")) {
					house = MyUtil.getArrString(hou, getResources()
							.getStringArray(R.array.house), 1);
				} else {
					house = "未填写";
				}
				String chil = user.getString("children");
				if (chil != null && !chil.equals("0")) {
					children = MyUtil.getArrString(chil, getResources()
							.getStringArray(R.array.children), 1);
				} else {
					children = "未填写";
				}
				String hhhh = user.getString("height");
				if (hhhh != null && !hhhh.equals("0")) {
					height = hhhh + "cm";

				} else {
					height = "未填写";
				}
				String s = user.getString("s_cid");
				s_cid = s == "40" ? "普通会员" : (s == "30" ? "高级会员"
						: (s == "20" ? "钻石会员" : " "));
				String ccc = user.getString("city_star");
				city_star = ccc == "0" ? " " : "城市之星";
				String www = user.getString("weight");
				if (www != null && !www.equals("0")) {
					weight = www + "kg";
				} else {
					weight = "未填写";
				}
				String bbb = user.getString("body");
				if (bbb != null && !bbb.equals("0")) {
					body = MyUtil.getArrString(bbb, getResources()
							.getStringArray(R.array.body), 1);
				} else {
					body = "未填写";
				}
				String ssss = user.getString("smoking");
				if (ssss != null && !ssss.equals("0")) {
					smoking = MyUtil.getArrString(ssss, getResources()
							.getStringArray(R.array.smoking), 1);
				} else {
					smoking = "未填写";
				}
				String dddd = user.getString("drinking");
				if (dddd != null && !dddd.equals("0")) {
					drinking = MyUtil.getArrString(dddd, getResources()
							.getStringArray(R.array.drinking), 1);
				} else {
					drinking = "未填写";
				}
				String ooo = user.getString("occupation");
				if (ooo != null && !ooo.equals("0")) {
					ocupation = MyUtil.getArrString(ooo, getResources()
							.getStringArray(R.array.occupation), 1);
				} else {
					ocupation = "未填写";
				}
				String vvv = user.getString("vehicle");
				if (vvv != null && !vvv.equals("0")) {
					vehicle = MyUtil.getArrString(vvv, getResources()
							.getStringArray(R.array.vehicle), 1);
				} else {
					vehicle = "未填写";
				}
				String ww = user.getString("wantchildren");
				if (ww != null && !ww.equals("0")) {
					wantchildren = MyUtil.getArrString(ww, getResources()
							.getStringArray(R.array.wantchild), 1);
				} else {
					wantchildren = "未填写";
				}

				// 获取择偶条件
				JSONObject fuser = user.getJSONObject("members_choice");
				fage_min = fuser.getString("age1");
				fage_max = fuser.getString("age2");
				String fww = fuser.getString("workprovince");
				if (fww != null && !fww.equals("0")) {
					fwork_province = MyUtil.getArrString(fww, getResources()
							.getStringArray(R.array.province), 1);

				} else {
					fwork_province = "未填写";
				}
				String fccc = fuser.getString("workcity");
				if (fccc != null && !fccc.equals("0")) {
					fwork_city = MyUtil.getArrString(fccc, getResources()
							.getStringArray(R.array.city), 1);
				} else {
					fwork_city = " ";
				}
				String fmm = fuser.getString("marriage");
				if (fmm != null && !fmm.equals("0")) {
					fmarriage = MyUtil.getArrString(fmm, getResources()
							.getStringArray(R.array.marriage), 1);
				} else {
					marriage = "未填写";
				}
				String fee = fuser.getString("education");
				if (fee != null && !fee.equals("0")) {
					feducation = MyUtil.getArrString(fee, getResources()
							.getStringArray(R.array.educaction), 1);

				} else {
					feducation = "未填写";
				}
				String fsss = fuser.getString("salary");
				if (fsss != null && !fsss.equals("0")) {
					fsalary = MyUtil.getArrString(fsss, getResources()
							.getStringArray(R.array.salary), 1);
				} else {
					fsalary = "未填写";
				}
				String fc = fuser.getString("children");
				if (fc != null && !fc.equals("0")) {
					fchildren = MyUtil.getArrString(fc, getResources()
							.getStringArray(R.array.children), 1);
				} else {
					fchildren = "未填写";
				}
				if (fuser.getString("height1") != null
						&& !fuser.getString("height1").equals("0")) {

					fheight_min = fuser.getString("height1") + "- cm ";
				} else {
					fheight_min = "不限";
				}
				if (fuser.getString("height2") != null
						&& !fuser.getString("height2").equals("0")) {
					fheight_max = fuser.getString("height2") + "cm";
				} else {
					fheight_max = " ";
				}
				String fb = fuser.getString("body");
				if (fb != null && !fb.equals("0")) {
					fbody = MyUtil.getArrString(fb, getResources()
							.getStringArray(R.array.body), 1);
				} else {
					fbody = "未填写";
				}
				String fmin = fuser.getString("weight1");
				if (fmin != null && !fmin.equals("0")) {
					fweight_min = fmin + "kg - ";
				} else {
					fweight_min = "未填写";
				}
				String fmax = fuser.getString("weight2");
				if (fmax != null && !fmax.equals("0")) {
					fweight_max = fmax + "kg";
				} else {
					fweight_max = " ";
				}
				String fo = fuser.getString("occupation");
				if (fo != null && !fo.equals("0")) {
					foccupation = MyUtil.getArrString(fo, getResources()
							.getStringArray(R.array.occupation), 1);
				} else {
					foccupation = "未填写";
				}
				String fw = fuser.getString("wantchildren");
				if (fw != null && !fw.equals("0")) {
					fwantchildren = MyUtil.getArrString(fw, getResources()
							.getStringArray(R.array.wantchild), 1);
				} else {
					fwantchildren = "未填写";
				}
				String fddd = fuser.getString("drinking");
				if (fddd != null && !fddd.equals("0")) {
					fdrinking = MyUtil.getArrString(fddd, getResources()
							.getStringArray(R.array.drinking), 1);
				} else {
					fdrinking = "未填写";
				}
				String fs = fuser.getString("smoking");
				if (fs != null && !fs.equals("0")) {
					fsmoking = MyUtil.getArrString(fs, getResources()
							.getStringArray(R.array.smoking), 1);
				} else {
					fsmoking = "未填写";
				}
				String in=user.getString("introduce");
				if (in != null && !in.equals("")) {
					introduce = in;

				} else {
					introduce = "未填写";
				}

				// //////////以上为资料获得区,一下为 资料填充区///////////////

				tv_username.setText(nickname + "的个人中心");
				tv_user_nick.setText(nickname + "(id:" + MainActivity.home_uid
						+ ")");
				tv_place_age_gender.setText(gender + " " + age + "岁" + " 来自"
						+ province + " " + city);
				tv_level.setText("会员等级：" + s_cid + " " + city_star);

				// //设置可展开的列表
				parent.add("内心独白");
				parent.add("详细资料");
				parent.add("择偶条件");
				List<String> list1 = new ArrayList<String>();
				list1.add(introduce);
				map.put("内心独白", list1);
				List<String> list2 = new ArrayList<String>();
				list2.add("学历：  " + education);
				list2.add("收入：  " + salary);
				list2.add("职业：  " + ocupation);
				list2.add("身高：  " + height);
				list2.add("体重：  " + weight);
				list2.add("体型：  " + body);
				list2.add("抽烟情况： " + smoking);
				list2.add("饮酒情况： " + drinking);
				list2.add("孩子需求： " + wantchildren);
				list2.add("是否有子： " + children);
				list2.add("自有物业： " + house);
				list2.add("结婚情况： " + marriage);
				list2.add("购车情况： " + vehicle);
				map.put("详细资料", list2);
				List<String> list3 = new ArrayList<String>();
				list3.add("年龄要求： " + fage_min + "-" + fage_max + "岁");
				list3.add("工作地区： " + fwork_province + " " + fwork_city);
				list3.add("结婚情况： " + fmarriage);
				list3.add("文化程度： " + feducation);
				list3.add("收入水平： " + fsalary);
				list3.add("有无孩子： " + fchildren);
				list3.add("身高要求： " + fheight_min + fheight_max);
				list3.add("体型要求： " + fbody);
				list3.add("体重要求： " + fweight_min + fweight_max);
				list3.add("职业要求：" + foccupation);
				list3.add("孩子需求： " + fwantchildren);
				list3.add("饮酒情况： " + fdrinking);
				list3.add("抽烟情况： " + fsmoking);
				map.put("择偶条件", list3);
				JSONArray arr = user.getJSONObject("pics").getJSONArray("list");

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
						MS_ViewDataActivity.this));
				grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						imageBrower(position, imageUrl);
					}
				});

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param position
	 *            坐标
	 * @param urls
	 *            路径 网址。
	 */

	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent(MS_ViewDataActivity.this,
				ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
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

	class MyAdapter extends BaseExpandableListAdapter {

		// 得到子item需要关联的数据
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			String key = parent.get(groupPosition);
			return (map.get(key).get(childPosition));
		}

		// 得到子item的ID
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		// 设置子item的组件
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			String key = MS_ViewDataActivity.this.parent.get(groupPosition);
			String info = map.get(key).get(childPosition);
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) MS_ViewDataActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_ms_viewdata_child,
						null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.child);
			tv.setText(info);
			return tv;
		}

		// 获取当前父item下的子item的个数
		@Override
		public int getChildrenCount(int groupPosition) {
			String key = MS_ViewDataActivity.this.parent.get(groupPosition);
			int size = map.get(key).size();
			return size;
		}

		// 获取当前父item的数据
		@Override
		public Object getGroup(int groupPosition) {
			return parent.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return parent.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		// 设置父item组件
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) MS_ViewDataActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.item_ms_viewdata_parent, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.parent);
			tv.setText(MS_ViewDataActivity.this.parent.get(groupPosition));
			return tv;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

}
