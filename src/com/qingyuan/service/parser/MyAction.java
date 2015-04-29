package com.qingyuan.service.parser;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.qingyuan.util.HttpUtil;

import android.util.Log;


/**
 * 操作类
 * 多个地方调用操作方法到此一游
 * @author Administrator
 *
 */
public class MyAction {
	
	/**发送委托
	 * 
	 * @param uid 自己
	 * @param toUid 接收者
	 * @param content 内容
	 * @return msg 结果
	 */
	
	public static String sendCommission(int uid,int toUid,String content){
		String msg = null;
		String url = HttpUtil.BASE_URL+"&f=commission&action=SEND&toType=json&android_uid"+uid+"&fuid="+toUid+"&content="+content;
		String res = null;
		try {
			res = HttpUtil.getRequest(url);
			JSONObject json = new JSONObject(res);
			int code = json.getInt("code");
			if(code == 10000){
				msg = json.getString("message");
			}else if (code > 10000) {
				msg = "服务器异常，请稍候再试！";
				Log.e("ERROR", json.getString("message"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg = "服务器异常，请稍候再试！";
		}
		return msg;
	}
	/**
	 * 发邮件
	 * @param uid 
	 * @param toUid
	 * @param id
	 * @param title
	 * @param content
	 * @return
	 */
	public static String sendEmail(int uid,int toUid,int id, String title, String content){
		String msg = null;
		String res;
		Map<String, String> param = new HashMap<String, String>();
		param.put("fuid", String.valueOf(toUid));
		param.put("title", title);
		param.put("content", content);
		param.put("id", String.valueOf("id"));
		try {
			res = HttpUtil.postRequest(HttpUtil.BASE_URL+"&f=email&action=SEND&toType=json&android_uid="+uid, param);
			JSONObject json = new JSONObject(res);
			int code = json.getInt("code");
			if(code == 10000){
				msg = json.getString("message");
			}else if (code > 10000) {
				Log.e("ERROR", json.getString("message"));
				msg = "服务器异常，请稍后再试！";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			msg = "服务器异常，请稍后再试！";
			e.printStackTrace();
		}
		return msg;
	}
	
	public static String sendLeer(int uid, int toUid, String content){
		String showMsg = null;
		Map<String, String> param = new HashMap<String, String>();
		param.put("fuid", String.valueOf(toUid));
		param.put("sendinfo", content);
		try {
			String res = HttpUtil.postRequest(HttpUtil.BASE_URL+"&toType=json&f=leer&action=SEND&android_uid="+uid, param);
			JSONObject json = new JSONObject(res);
			int code = json.getInt("code");
			if (code == 10000) {
				showMsg = json.getString("message");
			}else if (code > 10000) {
				Log.e("ERROR", showMsg);
				showMsg = "服务器异常，请稍候再试！";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			showMsg = "服务器异常，请稍候再试！";
			e.printStackTrace();
		}
		return showMsg;
	}
}
