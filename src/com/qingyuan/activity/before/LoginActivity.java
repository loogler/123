package com.qingyuan.activity.before;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.qingyuan.MainActivity;
import com.qingyuan.R;
import com.qingyuan.util.CustomProgressDialog;
import com.qingyuan.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {  
	  
	Button button_home_enter_1;
	TextView  register, forget;
	EditText login_name,login_password;
	//本来有记住是否自动登录的复选框，但是我发现所有app都默认自动登录，就删除了
	private CustomProgressDialog progressDialog = null;
	
	String login_name_str, login_password_str,home_uid,login_telphone;
	String login_code,login_message;
	
	String login_nickname,login_uid,login_gender,login_cid,login_star,user_pic,login_province,login_city,login_birth,true_name;
	//ProgressDialog progressDialog;

	

   @Override  
   public void onCreate(Bundle savedInstanceState)  {  
       super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       setContentView(R.layout.aty_loginactivity); 
       try {
			String pkName = this.getPackageName();
			String versionName = this.getPackageManager().getPackageInfo(
					pkName, 0).versionName;
//			int versionCode = this.getPackageManager()
//					.getPackageInfo(pkName, 0).versionCode;
			//return pkName + "   " + versionName + "  " + versionCode;
			TextView versionNumber = (TextView) findViewById(R.id.versionNumber);
			versionNumber.setText("Version: " + versionName );
			//VersionCode 需要时添加
		} catch (Exception e) {
		}
       register=(TextView) findViewById(R.id.register);
       forget=(TextView) findViewById(R.id.forget);
       
       button_home_enter_1=(Button)findViewById(R.id.button_home_enter_1);
       
       login_name=(EditText)findViewById(R.id.login_name);
       login_password=(EditText)findViewById(R.id.login_password);
       
       	SharedPreferences preferences = getSharedPreferences("userInfo",Activity.MODE_PRIVATE);   
		home_uid = preferences.getString("uid", null);
		if(home_uid!=null && home_uid.length()>0){
			Intent intent=new Intent(LoginActivity.this,MainActivity.class);              			
    		startActivity(intent);
    		LoginActivity.this.finish();
		}
       /*
       //纯手打，结果没用
       //使用ConnectivityManager判断是否有网络连接
       //获取ConnectivityManager实例     ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
       //获取NetworkInfo实例
       final NetworkInfo activeNetwork =cm.getActiveNetworkInfo();
       //判断是否有网络连接
       boolean isConnected = activeNetwork.isConnectedOrConnecting();      
       */
       //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	   //StrictMode.setThreadPolicy(policy);       
       /*************************请×××叫×××我×××分×××割×××线*************************/
       //点击这个按钮进入注册界面
       register.setOnClickListener(new View.OnClickListener() {			
   		@Override
   		public void onClick(View v) {
   			//打开注册界面（切换至RegisterActivity）
   			Intent intent=new Intent(LoginActivity.this,RegistActivity.class);
   			startActivity(intent);
   			//淡入淡出效果（没用）
   			//overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
   			//关闭LoginActivity就不能返回了
   			//LoginActivity.this.finish();
   		}
   	});
       
       /*************************请×××叫×××我×××分×××割×××线*************************/
       
       //点击这个按钮可以进入主界面
       //判定方法：用户名（或邮箱、手机）与密码匹配则进入主界面
       button_home_enter_1.setOnClickListener(new View.OnClickListener() {
    	   
    		   public void onClick(View v) {
    			   //判断空字符串的方法
    			   
    			   if(login_name.getText().toString().equals(""))
    			   {
    				   new  AlertDialog.Builder(LoginActivity.this)    
    				   .setTitle("")  
    				   .setMessage("请填写用户名")  
    				   .setPositiveButton("确定",null)  
    				   .show();     
    			   }
    			   
    			   else if(login_password.getText().toString().equals(""))
    			   {
    				   new  AlertDialog.Builder(LoginActivity.this)    
    				   .setTitle("" )  
    				   .setMessage("请填写密码！" )  
    				   .setPositiveButton("确定" ,  null )  
    				   .show();   
    			   }
    			   
    			   
    			   else if(checkNetworkInfo() ==false)
    			   {
    				   new  AlertDialog.Builder(LoginActivity.this)    
    				   .setTitle("" )  
    				   .setMessage("请检查你的网络连接！" )  
    				   .setPositiveButton("确定" ,  null )  
    				   .show();
    			   }
    			   
    			   
    			   else
    			   {
    				   /*************************请×××叫×××我×××分×××割×××线*************************/ 
    				   //login_0();

    				 //  progressDialog = ProgressDialog.show(LoginActivity.this, "Loading...",
    					//		"Please wait...", true, false);



    			

    				   progressDialog = CustomProgressDialog.createDialog(LoginActivity.this, "登录中...");
    				   progressDialog.show();

    				   new LoginThread().start();
    				   /*************************请×××叫×××我×××分×××割×××线*************************/
    			   }//以上都是网络的
    		   }

      	});
   
   }






/*************************请×××叫×××我×××分×××割×××线*************************/
//详见http://zhidao.baidu.com/link?url=8hmteqMtie-Tii4T1oVNKiPWOlG3IrvUz0iZkisJqxDclx36STEs3CDc_F1QhkZlKmdWSsm2rHY7DkStVDSPl81sSwFWZvn-m5dhiQ1l_xy
//State的import必须是NetworkInfo
//判断是否有网络连接的方法
public boolean checkNetworkInfo() {
	  ConnectivityManager conMan = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
	  State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
	  State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
	  if (mobile == State.CONNECTED || mobile == State.CONNECTING)
	   return true;
	  if (wifi == State.CONNECTED || wifi == State.CONNECTING)
	   return true;
	  return false;
	 }

/*************************请×××叫×××我×××分×××割×××线*************************/
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
 //登陆界面按BACK就是直接退出
 if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
	 finish();
 	 System.exit(0);
     return true;
 }
 if(keyCode==KeyEvent.KEYCODE_MENU&&event.getAction()==KeyEvent.ACTION_DOWN)
 {
	  new  AlertDialog.Builder(LoginActivity.this)    
	   .setTitle("")  
	   .setMessage("登录之前目录无效") 
	   .setPositiveButton("确定",null)  
	   .show(); 
 }
 return super.onKeyDown(keyCode, event);
}
/*************************请×××叫×××我×××分×××割×××线*************************/
//登录的子线程
class LoginThread extends Thread
{
	
	public void run()
	{
		String url = HttpUtil.BASE_URL + "&f=login&toType=json";
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", login_name.getText().toString().trim());
		map.put("password", login_password.getText().toString().trim());
		String res = null;
		progressDialog.dismiss();
		try {
			res = HttpUtil.postRequest(url, map);
			progressDialog.dismiss();
		} catch (Exception e1) {
			LoginActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(LoginActivity.this, "服务器 应异常，请稍后再试！", Toast.LENGTH_SHORT).show();
				}
			});
		}
		JSONObject jsonData;
		try {
			jsonData = new JSONObject(res);
			int code = jsonData.getInt("code");
			login_message = jsonData.getString("message");
			if(code==1){
				jsonData = jsonData.getJSONObject("result");Log.i("jsonData", jsonData.toString());
				login_code = "登录成功";
				login_nickname =jsonData.getString("nickname");
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
				LoginActivity.this.runOnUiThread(new Runnable() {
                    public void run() 
                    {
                    	//保存登录信息到本地
    	        		SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);   
    	        		Editor editor = sharedPreferences.edit();//获取编辑器   
    	        		editor.putString("uid",login_uid ); 
    	        		editor.putString("nickname",login_nickname );   
    	        		editor.putString("telphone", login_telphone);
    	        		editor.putString("gender",login_gender );
    	        		editor.putString("cid",login_cid ); 
    	        		editor.putString("star",login_star );
    	        		editor.putString("pic", user_pic);
    	        		editor.putString("province", login_province);
    	        		editor.putString("city", login_city);
    	        		editor.putString("truename", true_name);
    	        		editor.commit();//提交修改
    	        		Intent intent=new Intent(LoginActivity.this,MainActivity.class);              			
    	        		startActivity(intent);
    	        		LoginActivity.this.finish();
                    }
        		});
			}else if(code==10000){
				LoginActivity.this.runOnUiThread(new Runnable() {
                    public void run() 
                    {
                    	new  AlertDialog.Builder(LoginActivity.this)    
     	       		   .setTitle("" )   
     	       		   .setMessage(login_message)
     	       		   .setPositiveButton("确定",null )  
     	       		   .show();
                    }
                });
			}else{
				Log.e("程序异常错误", login_message);
			}
		} catch (JSONException e) {
			Log.e("资源格式化错误", "null");
		}		
	}
}//class LoginThread extends Thread括号的另一边


/*************************请×××叫×××我×××分×××割×××线*************************/
}//public class LoginActivity extends Activity implements OnClickListener括号的另一边

