package com.qingyuan.activity.userdata;

import com.qingyuan.PaymentActivity;
import com.qingyuan.R;
import com.qingyuan.service.parser.MyUtil;
import com.qingyuan.util.Order;
import com.qingyuan.util.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class MS_VIPActivity extends Activity implements OnClickListener{
	public String tag = "MS_VIPActivityTAG";
	//定义几个用到的xml布局变量
	private RelativeLayout rl_gshy, rl_zshy, rl_cszx;
	private ImageView img_gshy, img_zshy, img_cszx;
	private Button btn_open;
	private LinearLayout zuanshi, gaoji, chengshi;
	//user类
	public static User user;
	//订单的内容
	private String[] arrServices, arrKeyServices;//服务名称和价格
	private int orderKey;//用于区分序列位置，便于取值
	ScrollView introduce;//没啥用，也许后面用到
	public boolean isSelected = false;//判断是否点击。

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ms_vipactivity);
		// init();
		introduce = (ScrollView) findViewById(R.id.detail);
		rl_gshy = (RelativeLayout) findViewById(R.id.vip_relayout_gaoji);
		rl_zshy = (RelativeLayout) findViewById(R.id.vip_relayout_zuanshi);
		rl_cszx = (RelativeLayout) findViewById(R.id.vip_relayout_chengshi);
		img_gshy = (ImageView) findViewById(R.id.image_vip_1);
		img_zshy = (ImageView) findViewById(R.id.image_vip_2);
		img_cszx = (ImageView) findViewById(R.id.image_vip_3);
		btn_open = (Button) findViewById(R.id.btn_vip_openvip);
		zuanshi = (LinearLayout) findViewById(R.id.zuanshi);
		gaoji = (LinearLayout) findViewById(R.id.gaoji);
		chengshi = (LinearLayout) findViewById(R.id.chengshi);
		rl_gshy.setOnClickListener((android.view.View.OnClickListener) this);
		rl_zshy.setOnClickListener((View.OnClickListener) this);
		rl_cszx.setOnClickListener((View.OnClickListener) this);
		btn_open.setOnClickListener((View.OnClickListener) this);

		isSelected = false;// 默认的不加载

		// 取得会员信息，判断收费类型： 普通/续费。
		SharedPreferences sp = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		user = new User();
		user.formatUserFromPreferences(sp);
		int s_cid = user.getScid();
		if (s_cid > 0 && s_cid < 40) {
			arrServices = MyUtil.getArr(
					getResources().getStringArray(R.array.renewalservice), 0);
			arrKeyServices = MyUtil.getArr(
					getResources().getStringArray(R.array.renewalservice), 1);
		} else {
			arrServices = MyUtil.getArr(
					getResources().getStringArray(R.array.service), 0);
			arrKeyServices = MyUtil.getArr(
					getResources().getStringArray(R.array.service), 0);

		}

	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.vip_relayout_gaoji) {
			System.out.println("1");
			resetBtn();
			img_gshy.setImageResource(R.drawable.ms_vip_checkbtn);
			orderKey = 0;
			isSelected = true;
			chengshi.setVisibility(View.GONE);
			zuanshi.setVisibility(View.GONE);
			gaoji.setVisibility(View.VISIBLE);
		} else if (id == R.id.vip_relayout_zuanshi) {
			System.out.println("2");
			resetBtn();
			img_zshy.setImageResource(R.drawable.ms_vip_checkbtn);
			orderKey = 1;
			isSelected = true;
			gaoji.setVisibility(View.GONE);
			chengshi.setVisibility(View.GONE);
			zuanshi.setVisibility(View.VISIBLE);
		} else if (id == R.id.vip_relayout_chengshi) {
			System.out.println("3");
			resetBtn();
			img_cszx.setImageResource(R.drawable.ms_vip_checkbtn);
			orderKey = 2;
			isSelected = true;
			zuanshi.setVisibility(View.GONE);
			gaoji.setVisibility(View.GONE);
			chengshi.setVisibility(View.VISIBLE);
		} else if (id == R.id.btn_vip_openvip) {
			if (isSelected == true) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), PaymentActivity.class);
				Bundle bundle = new Bundle();
				Order order = new Order(0,
						Integer.parseInt(arrKeyServices[orderKey]),
						arrServices[orderKey], "");
				bundle.putSerializable("order", order);
				intent.putExtras(bundle);
				startActivity(intent);
				System.out.println(order.toString());
			}
			if (isSelected == false) {
				Toast.makeText(getApplicationContext(), "请选择开通的会员类型",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Log.e(tag, "没选择不可能选到");
		}

	}

	@SuppressLint("NewApi")
	private void resetBtn() {
		img_gshy.setImageResource(R.drawable.ms_vip_checkbtn1);
		img_zshy.setImageResource(R.drawable.ms_vip_checkbtn1);
		img_cszx.setImageResource(R.drawable.ms_vip_checkbtn1);
	}

	
	
}
