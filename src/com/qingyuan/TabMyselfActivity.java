package com.qingyuan;

import org.json.JSONObject;

import com.qingyuan.activity.before.LoginActivity;
import com.qingyuan.activity.userdata.MS_ConditionActivity;
import com.qingyuan.activity.userdata.MS_IntroduceActivity;
import com.qingyuan.activity.userdata.MS_ModifyActivity;
import com.qingyuan.activity.userdata.MS_PurseActivity;
import com.qingyuan.activity.userdata.MS_VIPActivity;
import com.qingyuan.activity.userdata.MS_ViewDataActivity;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.HttpUtil;
import com.qingyuan.util.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class TabMyselfActivity extends Activity implements OnClickListener{
	//初始化
	private ImageView user_img;
	private TextView user_info;
	//
	private String home_uid, home_pic, home_nickname, home_cid;
	//User 
	private User user;
	//图片加载
	private AsyncImageLoader2 asyncImageLoader2=new AsyncImageLoader2();
	
	//获取界面按钮
	private RelativeLayout rl_hyfu,rl_wdqianbao,rl_xgzl,rl_ylzl,rl_zotj,rl_nxdb,rl_zxdl;
	// 要保存的用户信息
	private String login_nickname, login_uid, login_gender, login_cid, login_star,
			user_pic, login_province, login_city, login_birth, true_name,
			login_telphone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_tabmyself);
		user_img =(ImageView) findViewById(R.id.user_img);
		user_info =(TextView) findViewById(R.id.user_info);
		
		getUserInfo();
//		showUserInfo();
		init();
		
	}
	/**
	 * 获取用户数据进程
	 */
	private void getUserInfo() {
		
		Thread getUserInfo = new Thread() {
			@Override
			public void run() {
				super.run();
				Message message = new Message();
				try {
					String res = HttpUtil.getRequest(HttpUtil.BASE_URL
							+ "&f=user&toType=json&android_uid=" + home_uid);
					message.obj = res;
					message.what = 1;
					handler.sendMessage(message);
				} catch (Exception e) {
					// 
					e.printStackTrace();
				}
			}
		};
		getUserInfo.start();
	}
	Handler handler=new Handler(){
		private void handlerMessage(Message message) {
			super.handleMessage(message);
			if (message.what==1) {
				try {
					JSONObject resjson=new JSONObject(message.obj.toString());
					if (resjson.getInt("code")==1) {
						JSONObject jsonData = resjson.getJSONObject("result");
						login_nickname = jsonData.getString("nickname");
						login_uid = jsonData.getString("uid");
						login_gender = jsonData.getString("gender");
						login_cid = jsonData.getString("s_cid");
						login_star = jsonData.getString("city_star");
						user_pic = jsonData.getString("pic");
						login_province = jsonData.getString("province");
						login_province = jsonData.getString("city");
						login_birth = jsonData.getString("birthyear");
						login_telphone = jsonData.getString("telphone");
						true_name = jsonData.getString("truename");
						saveUserInfo();
						showUserInfo();
					}
				} catch (Exception e) {
				}
			}
		}
/**保存数据，后期如果需要获得一个独立的推送程序，可能需要更改本处为Activity.MODE_WORLD_READABLE*/
	private void saveUserInfo() {
		SharedPreferences sp=getSharedPreferences("userInfo", Activity.MODE_PRIVATE); 
		Editor ed=sp.edit();
		ed.putString("uid", login_uid);
		ed.putString("nickname", login_nickname);
		ed.putString("telphone", login_telphone);
		ed.putString("gender", login_gender);
		ed.putString("cid", login_cid);
		ed.putString("star", login_star);
		ed.putString("pic", user_pic);
		ed.putString("province", login_province);
		ed.putString("city", login_city);
		ed.putString("truename", true_name);
		ed.commit();// 提交修改
		
	
		}
	};
	/**
	 * 获得用户基本信息 -头像 昵称
	 * */
	private void showUserInfo() {
		
		SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
		user= new User();
		user.formatUserFromPreferences(sharedPreferences);
		home_uid= sharedPreferences.getString("uid", null);//等级
		home_cid=sharedPreferences.getString("cid", null);//等级
		home_nickname =sharedPreferences.getString("nickname", null);//昵称
		home_pic=sharedPreferences.getString("pic", null);//头像
	
		//加载头像
		loadImage(HttpUtil.URL +"/"+ home_pic, R.id.user_img);
		Log.i("img_url", HttpUtil.URL+"/"+ home_pic);
		//会员等级
		String[] levels = getResources().getStringArray(R.array.level);
		String level = "普通会员";
		for (int i = 0; i < levels.length; i++) {
			if (levels[i].contains(home_cid)) {
				
				level = levels[i].substring(0, levels[i].indexOf(","));
				break;
			}
		}
		String info = home_nickname+ "\n"+"\n"+level;
		user_info.setText(info);
		
				
	}


	public void init(){
		rl_hyfu = (RelativeLayout) findViewById(R.id.relayout_hyfu);
		rl_wdqianbao = (RelativeLayout) findViewById(R.id.relayout_wdqianbao);
		rl_xgzl = (RelativeLayout) findViewById(R.id.relayout_xgzl);
		rl_ylzl=(RelativeLayout) findViewById(R.id.relayout_ylzl);
		rl_zotj = (RelativeLayout) findViewById(R.id.relayout_zotj);
		rl_nxdb = (RelativeLayout) findViewById(R.id.relayout_nxdb);
		rl_zxdl = (RelativeLayout) findViewById(R.id.relayout_zxdl);
		user_img = (ImageView) findViewById(R.id.user_pic);
		rl_hyfu.setOnClickListener(this);
		rl_wdqianbao.setOnClickListener(this);
		rl_xgzl.setOnClickListener(this);
		rl_ylzl.setOnClickListener(this);
		rl_zotj.setOnClickListener(this);
		rl_nxdb.setOnClickListener(this);
		rl_zxdl.setOnClickListener(this);
//		user_img.setOnClickListener(this);
		
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.relayout_hyfu:
			Intent intent_hyfu = new Intent(TabMyselfActivity.this,
					MS_VIPActivity.class);
			startActivity(intent_hyfu);
			break;
		case R.id.relayout_wdqianbao:
			Intent intent_wdqianbao = new Intent(TabMyselfActivity.this,
					MS_PurseActivity.class);
			startActivity(intent_wdqianbao);
			break;
		case R.id.relayout_xgzl:
			Intent intent = new Intent(TabMyselfActivity.this,
					MS_ModifyActivity.class);
			startActivity(intent);
			break;
		case R.id.relayout_ylzl:
			Intent intent_ylzl=new Intent(TabMyselfActivity.this,
					MS_ViewDataActivity.class);
			startActivity(intent_ylzl);
			break;
		case R.id.relayout_zotj:
			Intent intent_zotj = new Intent(TabMyselfActivity.this,
					MS_ConditionActivity.class);
			startActivity(intent_zotj);
			break;
		case R.id.relayout_nxdb:
			Intent intent_nxdb = new Intent(TabMyselfActivity.this,
					MS_IntroduceActivity.class);
			startActivity(intent_nxdb);
			break;
		case R.id.relayout_zxdl:
			new AlertDialog.Builder(TabMyselfActivity.this)
			.setTitle("提示：")
			.setMessage("确定要注销吗")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					SharedPreferences sp=getSharedPreferences("userInfo", Context.MODE_PRIVATE);
					Editor edt=sp.edit();
					edt.putString("nickname", "");
					edt.putString("uid", "");
					edt.putString("gender", "");
					edt.commit();// 提交修改,删除本地缓存
					
					Intent i= new Intent(TabMyselfActivity.this,LoginActivity.class);
					startActivity(i);
					TabMyselfActivity.this.finish();
				}
			}).setNegativeButton("取消", null).create()
			.show();
			
			break;
		}
	}
	
	/**activity跳转修改资料界面时，需要重新加载修改的资料*/
	@Override
	protected void onResume() {
		super.onResume();
		showUserInfo();
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
	
	
}
