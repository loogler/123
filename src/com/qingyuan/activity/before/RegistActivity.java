package com.qingyuan.activity.before;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;







import com.qingyuan.MainActivity;
import com.qingyuan.R;
import com.qingyuan.service.parser.MyDateSpinner;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import android.net.ParseException;
import android.os.Bundle;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;

public class RegistActivity extends Activity {

	Button button_home_enter_2, button_login_enter_2;
	TextView textView_agreement_enter;
	EditText set_email, set_phone, set_password, set_password_again;
	String set_email_str, set_phone_str, set_password_str,
			set_password_again_str;
	CheckBox agreement_read = null;
	Spinner spinner_province, spinner_city;
	RadioButton radioButton_boy, radioButton_girl;
	String strResult_str;

	// 注册用的数据
	String register_username, register_password, register_telephone,
			register_gender, register_province, register_city, register_year,
			register_month, register_day;

	// 注册的返回值
	String register_code, register_message, register_uid;

	// 全国所有省
	private String[] set_province = new String[] { "北京", "上海", "天津", "重庆",
			"香港", "澳门", "台湾", "河北", "山西", "陕西", "山东", "河南", "辽宁", "吉林", "黑龙江",
			"江苏", "浙江", "安徽", "江西", "福建", "湖北", "湖南", "四川", "贵州", "云南", "广东",
			"广西", "海南", "甘肃", "青海", "内蒙古", "宁夏", "新疆", "西藏" };
	// 全国所有市
	private String[][] set_city = new String[][] {
			{ "北京" },
			{ "上海" },
			{ "天津" },
			{ "重庆" },
			{ "香港" },
			{ "澳门" },
			{ "台北", "高雄", "基隆", "台中", "台南", "新竹", "嘉义" },
			{ "石家庄", "唐山", "秦皇岛", "邯郸", "邢台", "保定", "张家口", "承德", "沧州", "廊坊",
					"衡水" },
			{ "太原", "大同", "朔州", "阳泉", "长治", "晋城", "忻州", "晋中", "临汾", "运城", "吕梁" },
			{ "西安", "铜川", "宝鸡", "咸阳", "渭南", "延安", "汉中", "榆林", "安康", "商洛" },
			{ "济南", "青岛", "淄博", "枣庄", "东营", "烟台", "潍坊", "济宁", "泰安", "威海", "日照",
					"莱芜", "临沂", "德州", "聊城", "滨州", "菏泽" },
			{ "郑州", "开封", "洛阳", "平顶山", "安阳", "鹤壁", "新乡", "焦作", "濮阳", "许昌",
					"漯河", "三门峡", "南阳", "商丘", "信阳", "周口", "驻马店" },
			{ "沈阳", "大连", "鞍山", "抚顺", "本溪", "丹东", "锦州", "营口", "阜新", "辽阳", "盘锦",
					"铁岭", "朝阳", "葫芦岛" },
			{ "长春", "吉林", "四平", "辽源", "通化", "白山", "松原", "白城", "延边" },
			{ "哈尔滨", "齐齐哈尔", "鹤岗", "双鸭山", "鸡西", "大庆", "伊春", "牡丹江", "佳木斯",
					"七台河", "黑河", "绥化", "大兴安岭" },
			{ "南京", "无锡", "徐州", "常州", "苏州", "南通", "连云港", "淮安", "盐城", "扬州",
					"镇江", "泰州", "宿迁" },
			{ "杭州", "宁波", "温州", "嘉兴", "湖州", "绍兴", "金华", "衢州", "舟山", "台州", "丽水" },
			{ "合肥", "芜湖", "蚌埠", "淮南", "马鞍山", "淮北", "铜陵", "安庆", "黄山", "滁州",
					"阜阳", "宿州", "六安", "亳州", "池州", "宣城" },
			{ "南昌", "景德镇", "萍乡", "九江", "新余", "鹰潭", "赣州", "吉安", "宜春", "抚州", "上饶" },
			{ "福州", "厦门", "莆田", "三明", "泉州", "漳州", "南平", "龙岩", "宁德" },
			{ "武汉", "黄石", "十堰", "荆州", "宜昌", "襄樊", "鄂州", "荆门", "孝感", "黄冈", "咸宁",
					"随州", "恩施" },
			{ "长沙", "株洲", "湘潭", "衡阳", "邵阳", "岳阳", "常德", "张家界", "益阳", "郴州",
					"永州", "怀化", "娄底", "湘西" },
			{ "成都", "自贡", "攀枝花", "泸州", "德阳", "绵阳", "广元", "遂宁", "内江", "乐山",
					"南充", "眉山", "宜宾", "广安", "达州", "雅安", "巴中", "资阳", "阿坝", "甘孜",
					"凉山" },
			{ "贵阳", "六盘水", "遵义", "安顺", "铜仁", "毕节", "黔西南", "黔东南", "黔南" },
			{ "昆明", "曲靖", "玉溪", "保山", "昭通", "丽江", "普洱", "临沧", "文山", "红河",
					"西双版纳", "楚雄", "大理", "德宏", "怒江", "迪庆" },
			{ "广州", "深圳", "珠海", "汕头", "韶关", "佛山", "江门", "湛江", "茂名", "肇庆", "惠州",
					"梅州", "汕尾", "河源", "阳江", "清远", "东莞", "中山", "潮州", "揭阳", "云浮" },
			{ "南宁", "柳州", "桂林", "梧州", "北海", "防城港", "钦州", "贵港", "玉林", "百色",
					"贺州", "河池", "来宾", "崇左" },
			{ "海口", "三亚", "三沙" },
			{ "兰州", "金昌", "白银", "天水", "嘉峪关", "武威", "张掖", "平凉", "酒泉", "庆阳",
					"定西", "陇南", "临夏", "甘南" },
			{ "西宁", "海东", "海北", "黄南", "海南", "果洛", "玉树", "海西" },
			{ "呼和浩特", "包头", "乌海", "赤峰", "通辽", "鄂尔多斯", "呼伦贝尔", "巴彦淖尔", "乌兰察布",
					"锡林郭勒", "兴安", "阿拉善" },
			{ "银川", "石嘴山", "吴忠", "固原", "中卫" },
			{ "乌鲁木齐", " 克拉玛依", " 吐鲁番", "哈密", "和田", "阿克苏", "喀什", "塔城", "阿勒泰",
					"克孜勒苏", "巴音郭楞", " 昌吉 ", "博尔塔拉 ", "伊犁 " },
			{ "拉萨", "那曲", "昌都", "山南", "日喀则", "阿里", "林芝" } };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registactivity);

		// button_login_enter_2=(Button)findViewById(R.id.button_login_enter_2);
		button_home_enter_2 = (Button) findViewById(R.id.button_home_enter_2);
		textView_agreement_enter = (TextView) findViewById(R.id.textView_agreement_enter);
		// 读取四个基本数据
		set_email = (EditText) findViewById(R.id.set_email);
		set_phone = (EditText) findViewById(R.id.set_phone);
		set_password = (EditText) findViewById(R.id.set_password);
		set_password_again = (EditText) findViewById(R.id.set_password_again);

		radioButton_boy = (RadioButton) findViewById(R.id.radioButton_boy);
		radioButton_girl = (RadioButton) findViewById(R.id.radioButton_girl);
		agreement_read = (CheckBox) findViewById(R.id.agreement_read);
		spinner_province = (Spinner) findViewById(R.id.spinner_province);
		spinner_city = (Spinner) findViewById(R.id.spinner_city);

		// 将可选内容与ArrayAdapter连接起来,并设置spinner的风格
		ArrayAdapter<String> adapter_province = new ArrayAdapter<String>(
				RegistActivity.this, android.R.layout.simple_spinner_item,
				set_province);
		adapter_province
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		// ArrayAdapter<String> adapter_city=new
		// ArrayAdapter<String>(RegisterActivity.this,android.R.layout.test_list_item);
		// //市的数据转为字符串

		// 将adapter 添加到spinner中
		spinner_province.setAdapter(adapter_province);

		// 默认“阅读同意”没被选中
		agreement_read.setChecked(false);

		// context=this;
		/************************* 请×××叫×××我×××分×××割×××线 *************************/
		// 省市二级联动
		// 添加事件Spinner事件监听
		spinner_province
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// 创建适配器
						ArrayAdapter<String> adapter_city = new ArrayAdapter<String>(
								RegistActivity.this,
								android.R.layout.simple_spinner_item,
								set_city[arg2]);
						adapter_city
								.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
						// 绑定该控件
						spinner_city.setAdapter(adapter_city);
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
		/************************* 请×××叫×××我×××分×××割×××线 *************************/
		// 所有信息填写后进入主界面
		button_home_enter_2.setOnClickListener(new OnClickListener() {
			// 点击的效果
			@Override
			public void onClick(View v) {
				if (set_email.getText().toString().equals("")) {
					new AlertDialog.Builder(RegistActivity.this).setTitle("")
							.setMessage("请填写您的常用邮箱！")
							.setPositiveButton("确定", null).show();
				} else if (set_phone.getText().toString().equals("")) {
					new AlertDialog.Builder(RegistActivity.this).setTitle("")
							.setMessage("请填写您的手机号码！")
							.setPositiveButton("确定", null).show();
				}

				else if (set_password.getText().toString().equals("")) {
					new AlertDialog.Builder(RegistActivity.this).setTitle("")
							.setMessage("请设置密码！").setPositiveButton("确定", null)
							.show();
				} else if (set_password_again.getText().toString().equals("")) {
					new AlertDialog.Builder(RegistActivity.this).setTitle("")
							.setMessage("请再次确认密码！")
							.setPositiveButton("确定", null).show();
				} else if (set_password_again.getText().toString()
						.equals(set_password.getText().toString()) == false) {
					new AlertDialog.Builder(RegistActivity.this).setTitle("")
							.setMessage("两次输入密码不一致！")
							.setPositiveButton("确定", null).show();
				}

				/*
				 * 多加几个else if，尽量排除所有可能的错误输入；如果以上情况都未发生，
				 * 这就说明已选择性别、生日、所在地，填写邮箱、手机、密码，阅读《情缘注册条款》 可以成功注册了
				 */

				else {
					// 全部填写正确只需验证是否阅读并同意《情缘注册条款》了
					if (agreement_read.isChecked() == true) {
						register_tostring();
						// register_login();

						if (TextUtils.isEmpty(register_year)
								|| TextUtils.isEmpty(register_month)
								|| TextUtils.isEmpty(register_day)) {
							new AlertDialog.Builder(RegistActivity.this)
									// .setTitle("" )
									.setMessage("你不可能是今天出生的！")
									.setPositiveButton("确定", null).show();
						} else {
							new RegisterThread().start();
						}

					} else {
						new AlertDialog.Builder(RegistActivity.this)
								.setTitle("").setMessage("请阅读并同意《情缘服务条款》！")
								.setPositiveButton("确定", null).show();
					}
				}

			}// 这是public void onClick(View v)的括号的另一边
		});// 这是button_home_enter_2.setOnClickListener(new
			// OnClickListener()括号的另一边

		/************************* 请×××叫×××我×××分×××割×××线 *************************/
		// 已有帐号？那么返回登录吧！
//		button_login_enter_2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				RegisterActivity.this.finish();
//			}
//		});

		/************************* 请×××叫×××我×××分×××割×××线 *************************/
		// 文字也可以视为按钮
		// 点击可以看到《情缘服务条款》
		// 设置文字可以被点击
		textView_agreement_enter.setClickable(true);
		textView_agreement_enter.setFocusable(true);
		textView_agreement_enter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 注意文字和按钮的区别！！！
				ComponentName componentname = new ComponentName(
						RegistActivity.this, UserAgreementActivity.class);
				Intent intent = new Intent(RegistActivity.this,
						UserAgreementActivity.class);
				RegistActivity.this.startActivity(intent);
				intent.setComponent(componentname);
				// RegisterActivity.this.finish();
				// overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			}
		});
	}

	/************************* 请×××叫×××我×××分×××割×××线 *************************/
	// BACK返回登录界面
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			RegistActivity.this.finish();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			new AlertDialog.Builder(RegistActivity.this).setTitle("")
					.setMessage("登录之前目录无效").setPositiveButton("确定", null)
					.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	/************************* 请×××叫×××我×××分×××割×××线 *************************/
	public void register_tostring() {
		// 常规数据
		register_username = set_email.getText().toString();
		register_password = set_password.getText().toString();
		register_telephone = set_phone.getText().toString();

		if (radioButton_girl.isChecked()) {
			register_gender = "1";
		} else {
			register_gender = "0";
		}
		// 省，市
		if (spinner_province.getSelectedItem().toString().equals("北京")) {
			register_province = "10102000";
		} else if (spinner_province.getSelectedItem().toString().equals("上海")) {
			register_province = "10103000";
		} else if (spinner_province.getSelectedItem().toString().equals("广东")) {
			register_province = "10101000";
		} else if (spinner_province.getSelectedItem().toString().equals("天津")) {
			register_province = "10104000";
		} else if (spinner_province.getSelectedItem().toString().equals("重庆")) {
			register_province = "10105000";
		} else if (spinner_province.getSelectedItem().toString().equals("安徽")) {
			register_province = "10106000";
		} else if (spinner_province.getSelectedItem().toString().equals("福建")) {
			register_province = "10107000";
		} else if (spinner_province.getSelectedItem().toString().equals("甘肃")) {
			register_province = "10108000";
		} else if (spinner_province.getSelectedItem().toString().equals("广西")) {
			register_province = "10109000";
		} else if (spinner_province.getSelectedItem().toString().equals("贵州")) {
			register_province = "10110000";
		} else if (spinner_province.getSelectedItem().toString().equals("海南")) {
			register_province = "10111000";
		} else if (spinner_province.getSelectedItem().toString().equals("河北")) {
			register_province = "10112000";
		} else if (spinner_province.getSelectedItem().toString().equals("河南")) {
			register_province = "10113000";
		} else if (spinner_province.getSelectedItem().toString().equals("黑龙江")) {
			register_province = "10114000";
		} else if (spinner_province.getSelectedItem().toString().equals("湖北")) {
			register_province = "10115000";
		} else if (spinner_province.getSelectedItem().toString().equals("湖南")) {
			register_province = "10116000";
		} else if (spinner_province.getSelectedItem().toString().equals("吉林")) {
			register_province = "10117000";
		} else if (spinner_province.getSelectedItem().toString().equals("江苏")) {
			register_province = "10118000";
		} else if (spinner_province.getSelectedItem().toString().equals("江西")) {
			register_province = "10119000";
		} else if (spinner_province.getSelectedItem().toString().equals("辽宁")) {
			register_province = "10120000";
		} else if (spinner_province.getSelectedItem().toString().equals("内蒙古")) {
			register_province = "10121000";
		} else if (spinner_province.getSelectedItem().toString().equals("宁夏")) {
			register_province = "10122000";
		} else if (spinner_province.getSelectedItem().toString().equals("青海")) {
			register_province = "10123000";
		} else if (spinner_province.getSelectedItem().toString().equals("山东")) {
			register_province = "10124000";
		} else if (spinner_province.getSelectedItem().toString().equals("山西")) {
			register_province = "10125000";
		} else if (spinner_province.getSelectedItem().toString().equals("陕西")) {
			register_province = "10126000";
		} else if (spinner_province.getSelectedItem().toString().equals("四川")) {
			register_province = "10127000";
		} else if (spinner_province.getSelectedItem().toString().equals("西藏")) {
			register_province = "10128000";
		} else if (spinner_province.getSelectedItem().toString().equals("新疆")) {
			register_province = "10129000";
		} else if (spinner_province.getSelectedItem().toString().equals("云南")) {
			register_province = "10130000";
		} else if (spinner_province.getSelectedItem().toString().equals("浙江")) {
			register_province = "10131000";
		} else if (spinner_province.getSelectedItem().toString().equals("澳门")) {
			register_province = "10132000";
		} else if (spinner_province.getSelectedItem().toString().equals("香港")) {
			register_province = "10133000";
		} else if (spinner_province.getSelectedItem().toString().equals("台湾")) {
			register_province = "10134000";
		} else {
			register_province = "0";
		}
		if (spinner_city.getSelectedItem().toString().equals("不限")) {
			register_city = "0";
		}
		// 直辖市和特区
		else if (spinner_city.getSelectedItem().toString().equals("北京")) {
			register_city = "0";
		} else if (spinner_city.getSelectedItem().toString().equals("上海")) {
			register_city = "0";
		} else if (spinner_city.getSelectedItem().toString().equals("天津")) {
			register_city = "0";
		} else if (spinner_city.getSelectedItem().toString().equals("重庆")) {
			register_city = "0";
		} else if (spinner_city.getSelectedItem().toString().equals("香港")) {
			register_city = "0";
		} else if (spinner_city.getSelectedItem().toString().equals("澳门")) {
			register_city = "0";
		}
		// 台湾
		else if (spinner_city.getSelectedItem().toString().equals("台北")) {
			register_city = "10134030";
		} else if (spinner_city.getSelectedItem().toString().equals("高雄")) {
			register_city = "10134031";
		} else if (spinner_city.getSelectedItem().toString().equals("基隆")) {
			register_city = "10134041";
		} else if (spinner_city.getSelectedItem().toString().equals("台中")) {
			register_city = "10134042";
		} else if (spinner_city.getSelectedItem().toString().equals("台南")) {
			register_city = "10134043";
		} else if (spinner_city.getSelectedItem().toString().equals("新竹")) {
			register_city = "10134033";
		} else if (spinner_city.getSelectedItem().toString().equals("嘉义")) {
			register_city = "10134044";
		}
		// 河北
		else if (spinner_city.getSelectedItem().toString().equals("石家庄")) {
			register_city = "10112001";
		} else if (spinner_city.getSelectedItem().toString().equals("唐山")) {
			register_city = "10112006";
		} else if (spinner_city.getSelectedItem().toString().equals("秦皇岛")) {
			register_city = "10112008";
		} else if (spinner_city.getSelectedItem().toString().equals("邯郸")) {
			register_city = "10112004";
		} else if (spinner_city.getSelectedItem().toString().equals("邢台")) {
			register_city = "10112003";
		} else if (spinner_city.getSelectedItem().toString().equals("保定")) {
			register_city = "10112010";
		} else if (spinner_city.getSelectedItem().toString().equals("张家口")) {
			register_city = "10112011";
		} else if (spinner_city.getSelectedItem().toString().equals("承德")) {
			register_city = "10112009";
		} else if (spinner_city.getSelectedItem().toString().equals("沧州")) {
			register_city = "10112005";
		} else if (spinner_city.getSelectedItem().toString().equals("廊坊")) {
			register_city = "10112007";
		} else if (spinner_city.getSelectedItem().toString().equals("衡水")) {
			register_city = "10112002";
		}
		// 山西
		else if (spinner_city.getSelectedItem().toString().equals("太原")) {
			register_city = "10125001";
		} else if (spinner_city.getSelectedItem().toString().equals("大同")) {
			register_city = "10125005";
		} else if (spinner_city.getSelectedItem().toString().equals("朔州")) {
			register_city = "10125034";
		} else if (spinner_city.getSelectedItem().toString().equals("阳泉")) {
			register_city = "10125009";
		} else if (spinner_city.getSelectedItem().toString().equals("长治")) {
			register_city = "10125010";
		} else if (spinner_city.getSelectedItem().toString().equals("晋城")) {
			register_city = "10125011";
		} else if (spinner_city.getSelectedItem().toString().equals("忻州")) {
			register_city = "10125003";
		} else if (spinner_city.getSelectedItem().toString().equals("晋中")) {
			register_city = "10125107";
		} else if (spinner_city.getSelectedItem().toString().equals("临汾")) {
			register_city = "10125006";
		} else if (spinner_city.getSelectedItem().toString().equals("运城")) {
			register_city = "10125008";
		} else if (spinner_city.getSelectedItem().toString().equals("吕梁")) {
			register_city = "10125108";
		}
		// 陕西
		else if (spinner_city.getSelectedItem().toString().equals("西安")) {
			register_city = "10126001";
		} else if (spinner_city.getSelectedItem().toString().equals("铜川")) {
			register_city = "10126010";
		} else if (spinner_city.getSelectedItem().toString().equals("宝鸡")) {
			register_city = "10126006";
		} else if (spinner_city.getSelectedItem().toString().equals("咸阳")) {
			register_city = "10126011";
		} else if (spinner_city.getSelectedItem().toString().equals("渭南")) {
			register_city = "10126002";
		} else if (spinner_city.getSelectedItem().toString().equals("延安")) {
			register_city = "10126003";
		} else if (spinner_city.getSelectedItem().toString().equals("汉中")) {
			register_city = "10126008";
		} else if (spinner_city.getSelectedItem().toString().equals("榆林")) {
			register_city = "10126005";
		} else if (spinner_city.getSelectedItem().toString().equals("安康")) {
			register_city = "10126007";
		} else if (spinner_city.getSelectedItem().toString().equals("商洛")) {
			register_city = "10126098";
		}
		// 山东
		else if (spinner_city.getSelectedItem().toString().equals("济南")) {
			register_city = "10124003";
		} else if (spinner_city.getSelectedItem().toString().equals("青岛")) {
			register_city = "10124001";
		} else if (spinner_city.getSelectedItem().toString().equals("淄博")) {
			register_city = "10124004";
		} else if (spinner_city.getSelectedItem().toString().equals("枣庄")) {
			register_city = "10124014";
		} else if (spinner_city.getSelectedItem().toString().equals("东营")) {
			register_city = "10124007";
		} else if (spinner_city.getSelectedItem().toString().equals("烟台")) {
			register_city = "10124009";
		} else if (spinner_city.getSelectedItem().toString().equals("潍坊")) {
			register_city = "10124008";
		} else if (spinner_city.getSelectedItem().toString().equals("济宁")) {
			register_city = "10124015";
		} else if (spinner_city.getSelectedItem().toString().equals("泰安")) {
			register_city = "10124011";
		} else if (spinner_city.getSelectedItem().toString().equals("威海")) {
			register_city = "10124002";
		} else if (spinner_city.getSelectedItem().toString().equals("日照")) {
			register_city = "10124016";
		} else if (spinner_city.getSelectedItem().toString().equals("莱芜")) {
			register_city = "10124068";
		} else if (spinner_city.getSelectedItem().toString().equals("临沂")) {
			register_city = "10124013";
		} else if (spinner_city.getSelectedItem().toString().equals("德州")) {
			register_city = "10124006";
		} else if (spinner_city.getSelectedItem().toString().equals("聊城")) {
			register_city = "10124005";
		} else if (spinner_city.getSelectedItem().toString().equals("滨州")) {
			register_city = "10124018";
		} else if (spinner_city.getSelectedItem().toString().equals("菏泽")) {
			register_city = "10124012";
		}
		// 河南
		else if (spinner_city.getSelectedItem().toString().equals("郑州")) {
			register_city = "10113001";
		} else if (spinner_city.getSelectedItem().toString().equals("开封")) {
			register_city = "10113013";
		} else if (spinner_city.getSelectedItem().toString().equals("洛阳")) {
			register_city = "10113009";
		} else if (spinner_city.getSelectedItem().toString().equals("平顶山")) {
			register_city = "10113010";
		} else if (spinner_city.getSelectedItem().toString().equals("安阳")) {
			register_city = "10113003";
		} else if (spinner_city.getSelectedItem().toString().equals("鹤壁")) {
			register_city = "10113015";
		} else if (spinner_city.getSelectedItem().toString().equals("新乡")) {
			register_city = "10113002";
		} else if (spinner_city.getSelectedItem().toString().equals("焦作")) {
			register_city = "10113017";
		} else if (spinner_city.getSelectedItem().toString().equals("濮阳")) {
			register_city = "10113016";
		} else if (spinner_city.getSelectedItem().toString().equals("许昌")) {
			register_city = "10113004";
		} else if (spinner_city.getSelectedItem().toString().equals("漯河")) {
			register_city = "10113006";
		} else if (spinner_city.getSelectedItem().toString().equals("三门峡")) {
			register_city = "10113011";
		} else if (spinner_city.getSelectedItem().toString().equals("南阳")) {
			register_city = "10113012";
		} else if (spinner_city.getSelectedItem().toString().equals("商丘")) {
			register_city = "10113014";
		} else if (spinner_city.getSelectedItem().toString().equals("信阳")) {
			register_city = "10113007";
		} else if (spinner_city.getSelectedItem().toString().equals("周口")) {
			register_city = "10113008";
		} else if (spinner_city.getSelectedItem().toString().equals("驻马店")) {
			register_city = "10113005";
		}
		// 辽宁
		else if (spinner_city.getSelectedItem().toString().equals("沈阳")) {
			register_city = "10120001";
		} else if (spinner_city.getSelectedItem().toString().equals("大连")) {
			register_city = "10120006";
		} else if (spinner_city.getSelectedItem().toString().equals("鞍山")) {
			register_city = "10120004";
		} else if (spinner_city.getSelectedItem().toString().equals("抚顺")) {
			register_city = "10120003";
		} else if (spinner_city.getSelectedItem().toString().equals("本溪")) {
			register_city = "10120007";
		} else if (spinner_city.getSelectedItem().toString().equals("丹东")) {
			register_city = "10120008";
		} else if (spinner_city.getSelectedItem().toString().equals("锦州")) {
			register_city = "10120009";
		} else if (spinner_city.getSelectedItem().toString().equals("营口")) {
			register_city = "10120005";
		} else if (spinner_city.getSelectedItem().toString().equals("阜新")) {
			register_city = "10120011";
		} else if (spinner_city.getSelectedItem().toString().equals("辽阳")) {
			register_city = "10120013";
		} else if (spinner_city.getSelectedItem().toString().equals("盘锦")) {
			register_city = "10120012";
		} else if (spinner_city.getSelectedItem().toString().equals("铁岭")) {
			register_city = "10120002";
		} else if (spinner_city.getSelectedItem().toString().equals("朝阳")) {
			register_city = "10120010";
		} else if (spinner_city.getSelectedItem().toString().equals("葫芦岛")) {
			register_city = "10120014";
		}
		// 吉林
		else if (spinner_city.getSelectedItem().toString().equals("长春")) {
			register_city = "10117001";
		} else if (spinner_city.getSelectedItem().toString().equals("吉林")) {
			register_city = "10117002";
		} else if (spinner_city.getSelectedItem().toString().equals("四平")) {
			register_city = "10117006";
		} else if (spinner_city.getSelectedItem().toString().equals("辽源")) {
			register_city = "10117028";
		} else if (spinner_city.getSelectedItem().toString().equals("通化")) {
			register_city = "10117004";
		} else if (spinner_city.getSelectedItem().toString().equals("白山")) {
			register_city = "10117048";
		} else if (spinner_city.getSelectedItem().toString().equals("松原")) {
			register_city = "10117008";
		} else if (spinner_city.getSelectedItem().toString().equals("白城")) {
			register_city = "10117007";
		} else if (spinner_city.getSelectedItem().toString().equals("延边")) {
			register_city = "10117049";
		}
		// 黑龙江
		else if (spinner_city.getSelectedItem().toString().equals("哈尔滨")) {
			register_city = "10114001";
		} else if (spinner_city.getSelectedItem().toString().equals("齐齐哈尔")) {
			register_city = "10114005";
		} else if (spinner_city.getSelectedItem().toString().equals("鹤岗")) {
			register_city = "10114020";
		} else if (spinner_city.getSelectedItem().toString().equals("双鸭山")) {
			register_city = "10114024";
		} else if (spinner_city.getSelectedItem().toString().equals("鸡西")) {
			register_city = "10114009";
		} else if (spinner_city.getSelectedItem().toString().equals("大庆")) {
			register_city = "10114007";
		} else if (spinner_city.getSelectedItem().toString().equals("伊春")) {
			register_city = "10114042";
		} else if (spinner_city.getSelectedItem().toString().equals("牡丹江")) {
			register_city = "10114004";
		} else if (spinner_city.getSelectedItem().toString().equals("佳木斯")) {
			register_city = "10114003";
		} else if (spinner_city.getSelectedItem().toString().equals("七台河")) {
			register_city = "10114067";
		} else if (spinner_city.getSelectedItem().toString().equals("黑河")) {
			register_city = "10114053";
		} else if (spinner_city.getSelectedItem().toString().equals("绥化")) {
			register_city = "10114002";
		} else if (spinner_city.getSelectedItem().toString().equals("大兴安岭")) {
			register_city = "10114008";
		}
		// 江苏
		else if (spinner_city.getSelectedItem().toString().equals("南京")) {
			register_city = "10118001";
		} else if (spinner_city.getSelectedItem().toString().equals("无锡")) {
			register_city = "10118003";
		} else if (spinner_city.getSelectedItem().toString().equals("徐州")) {
			register_city = "10118004";
		} else if (spinner_city.getSelectedItem().toString().equals("常州")) {
			register_city = "10118005";
		} else if (spinner_city.getSelectedItem().toString().equals("苏州")) {
			register_city = "10118002";
		} else if (spinner_city.getSelectedItem().toString().equals("南通")) {
			register_city = "10118011";
		} else if (spinner_city.getSelectedItem().toString().equals("连云港")) {
			register_city = "10118007";
		} else if (spinner_city.getSelectedItem().toString().equals("淮安")) {
			register_city = "10118016";
		} else if (spinner_city.getSelectedItem().toString().equals("盐城")) {
			register_city = "10118009";
		} else if (spinner_city.getSelectedItem().toString().equals("扬州")) {
			register_city = "10118010";
		} else if (spinner_city.getSelectedItem().toString().equals("镇江")) {
			register_city = "10118006";
		} else if (spinner_city.getSelectedItem().toString().equals("泰州")) {
			register_city = "10118051";
		} else if (spinner_city.getSelectedItem().toString().equals("宿迁")) {
			register_city = "10118063";
		}
		// 浙江
		else if (spinner_city.getSelectedItem().toString().equals("杭州")) {
			register_city = "10131001";
		} else if (spinner_city.getSelectedItem().toString().equals("宁波")) {
			register_city = "10131003";
		} else if (spinner_city.getSelectedItem().toString().equals("温州")) {
			register_city = "10131002";
		} else if (spinner_city.getSelectedItem().toString().equals("嘉兴")) {
			register_city = "10131006";
		} else if (spinner_city.getSelectedItem().toString().equals("湖州")) {
			register_city = "10131005";
		} else if (spinner_city.getSelectedItem().toString().equals("绍兴")) {
			register_city = "10131004";
		} else if (spinner_city.getSelectedItem().toString().equals("金华")) {
			register_city = "10131009";
		} else if (spinner_city.getSelectedItem().toString().equals("衢州")) {
			register_city = "10131011";
		} else if (spinner_city.getSelectedItem().toString().equals("舟山")) {
			register_city = "10131015";
		} else if (spinner_city.getSelectedItem().toString().equals("台州")) {
			register_city = "10131012";
		} else if (spinner_city.getSelectedItem().toString().equals("丽水")) {
			register_city = "10131010";
		}
		// 安徽
		else if (spinner_city.getSelectedItem().toString().equals("合肥")) {
			register_city = "10106001";
		} else if (spinner_city.getSelectedItem().toString().equals("芜湖")) {
			register_city = "10106009";
		} else if (spinner_city.getSelectedItem().toString().equals("蚌埠")) {
			register_city = "10106003";
		} else if (spinner_city.getSelectedItem().toString().equals("淮南")) {
			register_city = "10106002";
		} else if (spinner_city.getSelectedItem().toString().equals("马鞍山")) {
			register_city = "10106075";
		} else if (spinner_city.getSelectedItem().toString().equals("淮北")) {
			register_city = "10106042";
		} else if (spinner_city.getSelectedItem().toString().equals("铜陵")) {
			register_city = "10106013";
		} else if (spinner_city.getSelectedItem().toString().equals("安庆")) {
			register_city = "10106011";
		} else if (spinner_city.getSelectedItem().toString().equals("黄山")) {
			register_city = "10106012";
		} else if (spinner_city.getSelectedItem().toString().equals("滁州")) {
			register_city = "10106008";
		} else if (spinner_city.getSelectedItem().toString().equals("阜阳")) {
			register_city = "10106005";
		} else if (spinner_city.getSelectedItem().toString().equals("宿州")) {
			register_city = "10106004";
		} else if (spinner_city.getSelectedItem().toString().equals("六安")) {
			register_city = "10106006";
		} else if (spinner_city.getSelectedItem().toString().equals("亳州")) {
			register_city = "10106072";
		} else if (spinner_city.getSelectedItem().toString().equals("池州")) {
			register_city = "10106079";
		} else if (spinner_city.getSelectedItem().toString().equals("宣城")) {
			register_city = "10106080";
		}
		// 江西
		else if (spinner_city.getSelectedItem().toString().equals("南昌")) {
			register_city = "10119001";
		} else if (spinner_city.getSelectedItem().toString().equals("景德镇")) {
			register_city = "10119003";
		} else if (spinner_city.getSelectedItem().toString().equals("萍乡")) {
			register_city = "10119007";
		} else if (spinner_city.getSelectedItem().toString().equals("九江")) {
			register_city = "10119002";
		} else if (spinner_city.getSelectedItem().toString().equals("新余")) {
			register_city = "10119040";
		} else if (spinner_city.getSelectedItem().toString().equals("鹰潭")) {
			register_city = "10119005";
		} else if (spinner_city.getSelectedItem().toString().equals("赣州")) {
			register_city = "10119008";
		} else if (spinner_city.getSelectedItem().toString().equals("吉安")) {
			register_city = "10119009";
		} else if (spinner_city.getSelectedItem().toString().equals("宜春")) {
			register_city = "10119006";
		} else if (spinner_city.getSelectedItem().toString().equals("抚州")) {
			register_city = "10119010";
		} else if (spinner_city.getSelectedItem().toString().equals("上饶")) {
			register_city = "10119004";
		}
		// 福建
		else if (spinner_city.getSelectedItem().toString().equals("福州")) {
			register_city = "10107001";
		} else if (spinner_city.getSelectedItem().toString().equals("厦门")) {
			register_city = "10107002";
		} else if (spinner_city.getSelectedItem().toString().equals("莆田")) {
			register_city = "10107010";
		} else if (spinner_city.getSelectedItem().toString().equals("三明")) {
			register_city = "10107009";
		} else if (spinner_city.getSelectedItem().toString().equals("泉州")) {
			register_city = "10107003";
		} else if (spinner_city.getSelectedItem().toString().equals("漳州")) {
			register_city = "10107007";
		} else if (spinner_city.getSelectedItem().toString().equals("南平")) {
			register_city = "10107004";
		} else if (spinner_city.getSelectedItem().toString().equals("龙岩")) {
			register_city = "10107008";
		} else if (spinner_city.getSelectedItem().toString().equals("宁德")) {
			register_city = "10107013";
		}
		// 湖北
		else if (spinner_city.getSelectedItem().toString().equals("武汉")) {
			register_city = "10115001";
		} else if (spinner_city.getSelectedItem().toString().equals("黄石")) {
			register_city = "10115002";
		} else if (spinner_city.getSelectedItem().toString().equals("十堰")) {
			register_city = "10115007";
		} else if (spinner_city.getSelectedItem().toString().equals("荆州")) {
			register_city = "10115010";
		} else if (spinner_city.getSelectedItem().toString().equals("宜昌")) {
			register_city = "10115008";
		} else if (spinner_city.getSelectedItem().toString().equals("襄樊")) {
			register_city = "10115005";
		} else if (spinner_city.getSelectedItem().toString().equals("鄂州")) {
			register_city = "10115004";
		} else if (spinner_city.getSelectedItem().toString().equals("荆门")) {
			register_city = "10115012";
		} else if (spinner_city.getSelectedItem().toString().equals("孝感")) {
			register_city = "10115013";
		} else if (spinner_city.getSelectedItem().toString().equals("黄冈")) {
			register_city = "10115011";
		} else if (spinner_city.getSelectedItem().toString().equals("咸宁")) {
			register_city = "10115006";
		} else if (spinner_city.getSelectedItem().toString().equals("随州")) {
			register_city = "10115062";
		} else if (spinner_city.getSelectedItem().toString().equals("恩施")) {
			register_city = "10115009";
		}
		// 湖南
		else if (spinner_city.getSelectedItem().toString().equals("长沙")) {
			register_city = "10116001";
		} else if (spinner_city.getSelectedItem().toString().equals("株洲")) {
			register_city = "10116002";
		} else if (spinner_city.getSelectedItem().toString().equals("湘潭")) {
			register_city = "10116014";
		} else if (spinner_city.getSelectedItem().toString().equals("衡阳")) {
			register_city = "10116009";
		} else if (spinner_city.getSelectedItem().toString().equals("邵阳")) {
			register_city = "10116010";
		} else if (spinner_city.getSelectedItem().toString().equals("岳阳")) {
			register_city = "10116004";
		} else if (spinner_city.getSelectedItem().toString().equals("常德")) {
			register_city = "10116005";
		} else if (spinner_city.getSelectedItem().toString().equals("张家界")) {
			register_city = "10116013";
		} else if (spinner_city.getSelectedItem().toString().equals("益阳")) {
			register_city = "10116003";
		} else if (spinner_city.getSelectedItem().toString().equals("郴州")) {
			register_city = "10116011";
		} else if (spinner_city.getSelectedItem().toString().equals("永州")) {
			register_city = "10116075";
		} else if (spinner_city.getSelectedItem().toString().equals("怀化")) {
			register_city = "10116008";
		} else if (spinner_city.getSelectedItem().toString().equals("娄底")) {
			register_city = "10116007";
		} else if (spinner_city.getSelectedItem().toString().equals("湘西")) {
			register_city = "10116097";
		}
		// 四川
		else if (spinner_city.getSelectedItem().toString().equals("成都")) {
			register_city = "10127001";
		} else if (spinner_city.getSelectedItem().toString().equals("自贡")) {
			register_city = "10127014";
		} else if (spinner_city.getSelectedItem().toString().equals("攀枝花")) {
			register_city = "10127017";
		} else if (spinner_city.getSelectedItem().toString().equals("泸州")) {
			register_city = "10127016";
		} else if (spinner_city.getSelectedItem().toString().equals("德阳")) {
			register_city = "10127018";
		} else if (spinner_city.getSelectedItem().toString().equals("绵阳")) {
			register_city = "10127005";
		} else if (spinner_city.getSelectedItem().toString().equals("广元")) {
			register_city = "10127010";
		} else if (spinner_city.getSelectedItem().toString().equals("遂宁")) {
			register_city = "10127117";
		} else if (spinner_city.getSelectedItem().toString().equals("内江")) {
			register_city = "10127013";
		} else if (spinner_city.getSelectedItem().toString().equals("乐山")) {
			register_city = "10127002";
		} else if (spinner_city.getSelectedItem().toString().equals("南充")) {
			register_city = "10127011";
		} else if (spinner_city.getSelectedItem().toString().equals("眉山")) {
			register_city = "10127056";
		} else if (spinner_city.getSelectedItem().toString().equals("宜宾")) {
			register_city = "10127015";
		} else if (spinner_city.getSelectedItem().toString().equals("广安")) {
			register_city = "10127070";
		} else if (spinner_city.getSelectedItem().toString().equals("达州")) {
			register_city = "10127137";
		} else if (spinner_city.getSelectedItem().toString().equals("雅安")) {
			register_city = "10127008";
		} else if (spinner_city.getSelectedItem().toString().equals("巴中")) {
			register_city = "10127125";
		} else if (spinner_city.getSelectedItem().toString().equals("资阳")) {
			register_city = "10127021";
		} else if (spinner_city.getSelectedItem().toString().equals("阿坝")) {
			register_city = "10127007";
		} else if (spinner_city.getSelectedItem().toString().equals("甘孜")) {
			register_city = "10127009";
		} else if (spinner_city.getSelectedItem().toString().equals("凉山")) {
			register_city = "10127003";
		}
		// 贵州
		else if (spinner_city.getSelectedItem().toString().equals("贵阳")) {
			register_city = "10110001";
		} else if (spinner_city.getSelectedItem().toString().equals("六盘水")) {
			register_city = "10110002";
		} else if (spinner_city.getSelectedItem().toString().equals("遵义")) {
			register_city = "10110007";
		} else if (spinner_city.getSelectedItem().toString().equals("安顺")) {
			register_city = "10110006";
		} else if (spinner_city.getSelectedItem().toString().equals("铜仁")) {
			register_city = "10110081";
		} else if (spinner_city.getSelectedItem().toString().equals("毕节")) {
			register_city = "10110021";
		} else if (spinner_city.getSelectedItem().toString().equals("黔西南")) {
			register_city = "10110082";
		} else if (spinner_city.getSelectedItem().toString().equals("黔东南")) {
			register_city = "10110083";
		} else if (spinner_city.getSelectedItem().toString().equals("黔南")) {
			register_city = "10110084";
		}
		// 云南
		else if (spinner_city.getSelectedItem().toString().equals("昆明")) {
			register_city = "10130001";
		} else if (spinner_city.getSelectedItem().toString().equals("曲靖")) {
			register_city = "10130002";
		} else if (spinner_city.getSelectedItem().toString().equals("玉溪")) {
			register_city = "10130011";
		} else if (spinner_city.getSelectedItem().toString().equals("保山")) {
			register_city = "10130010";
		} else if (spinner_city.getSelectedItem().toString().equals("昭通")) {
			register_city = "10130003";
		} else if (spinner_city.getSelectedItem().toString().equals("丽江")) {
			register_city = "10130030";
		} else if (spinner_city.getSelectedItem().toString().equals("普洱")) {
			register_city = "10130052";
		} else if (spinner_city.getSelectedItem().toString().equals("临沧")) {
			register_city = "10130009";
		} else if (spinner_city.getSelectedItem().toString().equals("文山")) {
			register_city = "10130005";
		} else if (spinner_city.getSelectedItem().toString().equals("红河")) {
			register_city = "10130098";
		} else if (spinner_city.getSelectedItem().toString().equals("西双版纳")) {
			register_city = "10130127";
		} else if (spinner_city.getSelectedItem().toString().equals("楚雄")) {
			register_city = "10130008";
		} else if (spinner_city.getSelectedItem().toString().equals("大理")) {
			register_city = "10130007";
		} else if (spinner_city.getSelectedItem().toString().equals("德宏")) {
			register_city = "10130128";
		} else if (spinner_city.getSelectedItem().toString().equals("怒江")) {
			register_city = "10130129";
		} else if (spinner_city.getSelectedItem().toString().equals("迪庆")) {
			register_city = "10130130";
		}
		// 广东
		else if (spinner_city.getSelectedItem().toString().equals("广州")) {
			register_city = "10101002";
		} else if (spinner_city.getSelectedItem().toString().equals("深圳")) {
			register_city = "10101201";
		} else if (spinner_city.getSelectedItem().toString().equals("珠海")) {
			register_city = "10101005";
		} else if (spinner_city.getSelectedItem().toString().equals("汕头")) {
			register_city = "10101013";
		} else if (spinner_city.getSelectedItem().toString().equals("韶关")) {
			register_city = "10101015";
		} else if (spinner_city.getSelectedItem().toString().equals("佛山")) {
			register_city = "10101003";
		} else if (spinner_city.getSelectedItem().toString().equals("江门")) {
			register_city = "10101016";
		} else if (spinner_city.getSelectedItem().toString().equals("湛江")) {
			register_city = "10101004";
		} else if (spinner_city.getSelectedItem().toString().equals("茂名")) {
			register_city = "10101012";
		} else if (spinner_city.getSelectedItem().toString().equals("肇庆")) {
			register_city = "10101006";
		} else if (spinner_city.getSelectedItem().toString().equals("惠州")) {
			register_city = "10101008";
		} else if (spinner_city.getSelectedItem().toString().equals("梅州")) {
			register_city = "10101014";
		} else if (spinner_city.getSelectedItem().toString().equals("汕尾")) {
			register_city = "10101028";
		} else if (spinner_city.getSelectedItem().toString().equals("河源")) {
			register_city = "10101023";
		} else if (spinner_city.getSelectedItem().toString().equals("阳江")) {
			register_city = "10101022";
		} else if (spinner_city.getSelectedItem().toString().equals("清远")) {
			register_city = "10101018";
		} else if (spinner_city.getSelectedItem().toString().equals("东莞")) {
			register_city = "10101007";
		} else if (spinner_city.getSelectedItem().toString().equals("中山")) {
			register_city = "10101011";
		} else if (spinner_city.getSelectedItem().toString().equals("潮州")) {
			register_city = "10101020";
		} else if (spinner_city.getSelectedItem().toString().equals("揭阳")) {
			register_city = "10101026";
		} else if (spinner_city.getSelectedItem().toString().equals("云浮")) {
			register_city = "10101068";
		}
		// 广西
		else if (spinner_city.getSelectedItem().toString().equals("南宁")) {
			register_city = "10109001";
		} else if (spinner_city.getSelectedItem().toString().equals("柳州")) {
			register_city = "10109002";
		} else if (spinner_city.getSelectedItem().toString().equals("桂林")) {
			register_city = "10109007";
		} else if (spinner_city.getSelectedItem().toString().equals("梧州")) {
			register_city = "10109008";
		} else if (spinner_city.getSelectedItem().toString().equals("北海")) {
			register_city = "10109010";
		} else if (spinner_city.getSelectedItem().toString().equals("防城港")) {
			register_city = "10109006";
		} else if (spinner_city.getSelectedItem().toString().equals("钦州")) {
			register_city = "10109003";
		} else if (spinner_city.getSelectedItem().toString().equals("贵港")) {
			register_city = "10109014";
		} else if (spinner_city.getSelectedItem().toString().equals("玉林")) {
			register_city = "10109005";
		} else if (spinner_city.getSelectedItem().toString().equals("百色")) {
			register_city = "10109004";
		} else if (spinner_city.getSelectedItem().toString().equals("贺州")) {
			register_city = "10109089";
		} else if (spinner_city.getSelectedItem().toString().equals("河池")) {
			register_city = "10109009";
		} else if (spinner_city.getSelectedItem().toString().equals("来宾")) {
			register_city = "10109018";
		} else if (spinner_city.getSelectedItem().toString().equals("崇左")) {
			register_city = "10109083";
		}
		// 海南
		else if (spinner_city.getSelectedItem().toString().equals("海口")) {
			register_city = "10111001";
		} else if (spinner_city.getSelectedItem().toString().equals("三亚")) {
			register_city = "10111002";
		} else if (spinner_city.getSelectedItem().toString().equals("三沙")) {
			register_city = "0";
		}
		// 甘肃
		else if (spinner_city.getSelectedItem().toString().equals("兰州")) {
			register_city = "10108001";
		} else if (spinner_city.getSelectedItem().toString().equals("金昌")) {
			register_city = "10108006";
		} else if (spinner_city.getSelectedItem().toString().equals("白银")) {
			register_city = "10108078";
		} else if (spinner_city.getSelectedItem().toString().equals("天水")) {
			register_city = "10108007";
		} else if (spinner_city.getSelectedItem().toString().equals("嘉峪关")) {
			register_city = "10108023";
		} else if (spinner_city.getSelectedItem().toString().equals("武威")) {
			register_city = "10108003";
		} else if (spinner_city.getSelectedItem().toString().equals("张掖")) {
			register_city = "10108002";
		} else if (spinner_city.getSelectedItem().toString().equals("平凉")) {
			register_city = "10108009";
		} else if (spinner_city.getSelectedItem().toString().equals("酒泉")) {
			register_city = "10108004";
		} else if (spinner_city.getSelectedItem().toString().equals("庆阳")) {
			register_city = "10108064";
		} else if (spinner_city.getSelectedItem().toString().equals("定西")) {
			register_city = "10108008";
		} else if (spinner_city.getSelectedItem().toString().equals("陇南")) {
			register_city = "10108084";
		} else if (spinner_city.getSelectedItem().toString().equals("临夏")) {
			register_city = "10108085";
		} else if (spinner_city.getSelectedItem().toString().equals("甘南")) {
			register_city = "10108012";
		}
		// 青海
		else if (spinner_city.getSelectedItem().toString().equals("西宁")) {
			register_city = "10123001";
		} else if (spinner_city.getSelectedItem().toString().equals("海东")) {
			register_city = "10123045";
		} else if (spinner_city.getSelectedItem().toString().equals("海北")) {
			register_city = "10123046";
		} else if (spinner_city.getSelectedItem().toString().equals("黄南")) {
			register_city = "10123047";
		} else if (spinner_city.getSelectedItem().toString().equals("海南")) {
			register_city = "10123048";
		} else if (spinner_city.getSelectedItem().toString().equals("果洛")) {
			register_city = "10123002";
		} else if (spinner_city.getSelectedItem().toString().equals("玉树")) {
			register_city = "10123003";
		} else if (spinner_city.getSelectedItem().toString().equals("海西")) {
			register_city = "10123005";
		}
		// 内蒙古
		else if (spinner_city.getSelectedItem().toString().equals("呼和浩特")) {
			register_city = "10121001";
		} else if (spinner_city.getSelectedItem().toString().equals("包头")) {
			register_city = "10121003";
		} else if (spinner_city.getSelectedItem().toString().equals("乌海")) {
			register_city = "10121005";
		} else if (spinner_city.getSelectedItem().toString().equals("赤峰")) {
			register_city = "10121008";
		} else if (spinner_city.getSelectedItem().toString().equals("通辽")) {
			register_city = "10121011";
		} else if (spinner_city.getSelectedItem().toString().equals("鄂尔多斯")) {
			register_city = "10121092";
		} else if (spinner_city.getSelectedItem().toString().equals("呼伦贝尔")) {
			register_city = "10121093";
		} else if (spinner_city.getSelectedItem().toString().equals("巴彦淖尔")) {
			register_city = "10121094";
		} else if (spinner_city.getSelectedItem().toString().equals("乌兰察布")) {
			register_city = "10121095";
		} else if (spinner_city.getSelectedItem().toString().equals("锡林郭勒")) {
			register_city = "10121089";
		} else if (spinner_city.getSelectedItem().toString().equals("兴安")) {
			register_city = "10121091";
		} else if (spinner_city.getSelectedItem().toString().equals("阿拉善")) {
			register_city = "10121090";
		}
		// 宁夏
		else if (spinner_city.getSelectedItem().toString().equals("银川")) {
			register_city = "10122001";
		} else if (spinner_city.getSelectedItem().toString().equals("石嘴山")) {
			register_city = "10122002";
		} else if (spinner_city.getSelectedItem().toString().equals("吴忠")) {
			register_city = "10122010";
		} else if (spinner_city.getSelectedItem().toString().equals("固原")) {
			register_city = "10122003";
		} else if (spinner_city.getSelectedItem().toString().equals("中卫")) {
			register_city = "10122011";
		}
		// 新疆
		else if (spinner_city.getSelectedItem().toString().equals("乌鲁木齐")) {
			register_city = "10129001";
		} else if (spinner_city.getSelectedItem().toString().equals("克拉玛依")) {
			register_city = "10129004";
		} else if (spinner_city.getSelectedItem().toString().equals("吐鲁番")) {
			register_city = "10129009";
		} else if (spinner_city.getSelectedItem().toString().equals("哈密")) {
			register_city = "10129008";
		} else if (spinner_city.getSelectedItem().toString().equals("和田")) {
			register_city = "10129012";
		} else if (spinner_city.getSelectedItem().toString().equals("阿克苏")) {
			register_city = "10129010";
		} else if (spinner_city.getSelectedItem().toString().equals("喀什")) {
			register_city = "10129011";
		} else if (spinner_city.getSelectedItem().toString().equals("塔城")) {
			register_city = "10129086";
		} else if (spinner_city.getSelectedItem().toString().equals("阿勒泰")) {
			register_city = "10129006";
		} else if (spinner_city.getSelectedItem().toString().equals("克孜勒苏")) {
			register_city = "10129088";
		} else if (spinner_city.getSelectedItem().toString().equals("巴音郭楞")) {
			register_city = "10129007";
		} else if (spinner_city.getSelectedItem().toString().equals("昌吉")) {
			register_city = "10129017";
		} else if (spinner_city.getSelectedItem().toString().equals("博尔塔拉")) {
			register_city = "10129089";
		} else if (spinner_city.getSelectedItem().toString().equals("伊犁")) {
			register_city = "10129090";
		}
		// 西藏
		else if (spinner_city.getSelectedItem().toString().equals("拉萨")) {
			register_city = "10128001";
		} else if (spinner_city.getSelectedItem().toString().equals("那曲")) {
			register_city = "10128002";
		} else if (spinner_city.getSelectedItem().toString().equals("昌都")) {
			register_city = "10128003";
		} else if (spinner_city.getSelectedItem().toString().equals("山南")) {
			register_city = "10128004";
		} else if (spinner_city.getSelectedItem().toString().equals("日喀则")) {
			register_city = "10128005";
		} else if (spinner_city.getSelectedItem().toString().equals("阿里")) {
			register_city = "10128006";
		} else if (spinner_city.getSelectedItem().toString().equals("林芝")) {
			register_city = "10128007";
		} else {
			register_city = "0";
		}
		// 年，月，日
		// 注意！此处有bug，出生日期可以为昨天，甚至可以为明天！
		// 但是如果用户选择今天出生，就会报错
		register_year = MyDateSpinner.timeyear;
		register_month = MyDateSpinner.timemonth;
		register_day = MyDateSpinner.timeday;

	}

	/************************* 请×××叫×××我×××分×××割×××线 *************************/
	/*
	 * public void register_login() { String httpUrl=
	 * "http://www.07919.com/index.php?n=index&h=api&mooApi=android&mooCode=07919&f=register&toType=xml"
	 * ; //创建HttpPost对象 HttpPost httpRequest=new HttpPost(httpUrl); //设置HTTP
	 * POST请求参数必须用NameValuePair对象 List<NameValuePair> params=new
	 * ArrayList<NameValuePair>(); params.add(new
	 * BasicNameValuePair("mooApi","android")); params.add(new
	 * BasicNameValuePair("mooCode","07919")); params.add(new
	 * BasicNameValuePair("toType","xml"));
	 * 
	 * params.add(new BasicNameValuePair("username",register_username.trim()));
	 * params.add(new BasicNameValuePair("password",register_password.trim()));
	 * params.add(new BasicNameValuePair("telphone",register_telephone.trim()));
	 * params.add(new BasicNameValuePair("gender",register_gender.trim()));
	 * params.add(new BasicNameValuePair("province",register_province.trim()));
	 * params.add(new BasicNameValuePair("city",register_city.trim()));
	 * params.add(new BasicNameValuePair("year",register_year.trim()));
	 * params.add(new BasicNameValuePair("month",register_month.trim()));
	 * params.add(new BasicNameValuePair("day",register_day.trim()));
	 * 
	 * HttpEntity httpentity = null; try { httpentity = new
	 * UrlEncodedFormEntity(params,"utf8"); } catch
	 * (UnsupportedEncodingException e) { e.printStackTrace(); }
	 * 
	 * //提交数据（压入post值） httpRequest.setEntity(httpentity); //定义客户端接收数据的对象名
	 * HttpClient httpclient=new DefaultHttpClient(); //定义获取返回值的对象名 HttpResponse
	 * httpResponse = null; try { //使用execute方法发送HTTP POST请求，并返回HttpResponse对象
	 * //执行请求获取响应（返回值） httpResponse = httpclient.execute(httpRequest); } catch
	 * (ClientProtocolException e) { e.printStackTrace(); } catch (IOException
	 * e) { e.printStackTrace(); }
	 * 
	 * if(httpResponse.getStatusLine().getStatusCode()==200) { String strResult
	 * = null; try { //使用getEntity方法获得返回结果，取得回应字符串 strResult =
	 * EntityUtils.toString(httpResponse.getEntity());
	 * 
	 * //pull方法解析xml //创建XmlPullParserFactory类 XmlPullParserFactory factory;
	 * 
	 * //实例化XmlPullParserFactory类 factory=XmlPullParserFactory.newInstance();
	 * //获取XmlPullParser的实例 XmlPullParser xmlPullParser=factory.newPullParser();
	 * //设置输入流 xml文件 xmlPullParser.setInput(new StringReader(""+strResult));
	 * //开始解析 //定义Pull解析常用事件类型 int eventType=xmlPullParser.getEventType();
	 * 
	 * while (eventType != XmlPullParser.END_DOCUMENT) { //这里以后会添加别的登录返回值
	 * if((eventType==XmlPullParser.START_TAG) &&
	 * (xmlPullParser.getName().equals("code"))) { xmlPullParser.next();
	 * if(xmlPullParser.getText().equals("1")) { register_code="注册生效"; } } else
	 * if((eventType==XmlPullParser.START_TAG) &&
	 * (xmlPullParser.getName().equals("message"))) { xmlPullParser.next();
	 * register_message=""+xmlPullParser.getText(); } else
	 * if((eventType==XmlPullParser.START_TAG) &&
	 * (xmlPullParser.getName().equals("uid"))) { xmlPullParser.next();
	 * register_uid=(xmlPullParser.getText().toString()); }
	 * 
	 * 
	 * eventType = xmlPullParser.next(); } } catch (ParseException e) {
	 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
	 * catch (XmlPullParserException e) { e.printStackTrace(); }
	 * 
	 * if(register_code=="注册生效") { SharedPreferences sharedPreferences =
	 * getSharedPreferences("userInfo", Context.MODE_PRIVATE); Editor editor =
	 * sharedPreferences.edit();//获取编辑器 editor.putString("username","没有昵称的" );
	 * editor.putString("uid",register_uid );
	 * editor.putString("gender",register_gender ); editor.putString("cid","40"
	 * ); editor.putString("star","0" );
	 * 
	 * editor.commit();//提交修改
	 * 
	 * Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
	 * startActivity(intent); RegisterActivity.this.finish(); } else { new
	 * AlertDialog.Builder(RegisterActivity.this) .setTitle("" )
	 * .setMessage(register_message+""+strResult) .setPositiveButton("确定",null )
	 * .show(); }
	 * 
	 * 
	 * } else { new AlertDialog.Builder(RegisterActivity.this) .setTitle("" )
	 * .setMessage("请检查你的网络连接！") .setPositiveButton("确定",null) .show(); }
	 * 
	 * }//public void register_login()括号的另一边
	 */
	/************************* 请×××叫×××我×××分×××割×××线 *************************/
	// 注册的子线程
	class RegisterThread extends Thread {

		public void run() {

			String httpUrl = "http://www.07919.com/index.php?n=index&h=api&mooApi=android&mooCode=07919&f=register&toType=xml";
			// 创建HttpPost对象
			HttpPost httpRequest = new HttpPost(httpUrl);
			// 设置HTTP POST请求参数必须用NameValuePair对象
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("mooApi", "android"));
			params.add(new BasicNameValuePair("mooCode", "07919"));
			params.add(new BasicNameValuePair("toType", "xml"));

			params.add(new BasicNameValuePair("username", register_username
					.trim()));
			params.add(new BasicNameValuePair("password", register_password
					.trim()));
			params.add(new BasicNameValuePair("telphone", register_telephone
					.trim()));
			params.add(new BasicNameValuePair("gender", register_gender.trim()));
			params.add(new BasicNameValuePair("province", register_province
					.trim()));
			params.add(new BasicNameValuePair("city", register_city.trim()));
			params.add(new BasicNameValuePair("year", register_year.trim()));
			params.add(new BasicNameValuePair("month", register_month.trim()));
			params.add(new BasicNameValuePair("day", register_day.trim()));

			HttpEntity httpentity = null;
			try {
				httpentity = new UrlEncodedFormEntity(params, "utf8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			// 提交数据（压入post值）
			httpRequest.setEntity(httpentity);
			// 定义客户端接收数据的对象名
			HttpClient httpclient = new DefaultHttpClient();
			// 定义获取返回值的对象名
			HttpResponse httpResponse = null;
			try {
				// 使用execute方法发送HTTP POST请求，并返回HttpResponse对象
				// 执行请求获取响应（返回值）
				httpResponse = httpclient.execute(httpRequest);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = null;
				try {
					// 使用getEntity方法获得返回结果，取得回应字符串
					strResult = EntityUtils.toString(httpResponse.getEntity());

					// pull方法解析xml
					// 创建XmlPullParserFactory类
					XmlPullParserFactory factory;

					// 实例化XmlPullParserFactory类
					factory = XmlPullParserFactory.newInstance();
					// 获取XmlPullParser的实例
					XmlPullParser xmlPullParser = factory.newPullParser();
					// 设置输入流 xml文件
					xmlPullParser.setInput(new StringReader("" + strResult));
					// 开始解析
					// 定义Pull解析常用事件类型
					int eventType = xmlPullParser.getEventType();

					while (eventType != XmlPullParser.END_DOCUMENT) {
						// 这里以后会添加别的登录返回值
						if ((eventType == XmlPullParser.START_TAG)
								&& (xmlPullParser.getName().equals("code"))) {
							xmlPullParser.next();
							if (xmlPullParser.getText().equals("1")) {
								register_code = "注册生效";
							}
						} else if ((eventType == XmlPullParser.START_TAG)
								&& (xmlPullParser.getName().equals("message"))) {
							xmlPullParser.next();
							register_message = "" + xmlPullParser.getText();
						} else if ((eventType == XmlPullParser.START_TAG)
								&& (xmlPullParser.getName().equals("uid"))) {
							xmlPullParser.next();
							register_uid = (xmlPullParser.getText().toString());
						}

						eventType = xmlPullParser.next();
					}
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}

				if (register_code == "注册生效") {
					RegistActivity.this.runOnUiThread(new Runnable() {
						public void run() {

							SharedPreferences sharedPreferences = getSharedPreferences(
									"userInfo", Context.MODE_PRIVATE);
							Editor editor = sharedPreferences.edit();// 获取编辑器
							editor.putString("username", "没有昵称的");
							editor.putString("uid", register_uid);
							editor.putString("gender", register_gender);
							editor.putString("cid", "40");
							editor.putString("star", "0");
							editor.commit();// 提交修改

							Intent intent = new Intent(RegistActivity.this,
									MainActivity.class);
							startActivity(intent);
							RegistActivity.this.finish();

						}
					});

				} else {
					strResult_str = strResult;
					RegistActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							new AlertDialog.Builder(RegistActivity.this)
							// .setTitle("" )
									.setMessage(register_message)
									// .setMessage(register_message+""+strResult_str)
									.setPositiveButton("确定", null).show();
						}
					});

				}

			} else {

				RegistActivity.this.runOnUiThread(new Runnable() {
					public void run() {

						new AlertDialog.Builder(RegistActivity.this)
								.setTitle("").setMessage("请检查你的网络连接！")
								.setPositiveButton("确定", null).show();

					}
				});

			}

		}

	}// class RegisterThread extends Thread括号的另一边

	/************************* 请×××叫×××我×××分×××割×××线 *************************/

}// public class RegisterActivity extends Activity {括号的另一边


