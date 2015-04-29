package com.qingyuan.service.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.qingyuan.R;
import com.qingyuan.util.TableItem_Visit;

public class XML_Parser_Visit {
	Context context;

	public XML_Parser_Visit() {
		super();
	}

	public List<TableItem_Visit> formatTableItem(String res) {
		List<TableItem_Visit> items = new ArrayList<TableItem_Visit>();
		TableItem_Visit item = null;
		JSONObject json = null;
		/*
		 * String[] arr_pro = null; String[] arr_city = null; String city =
		 * null, province = null;
		 */
		try {
			json = new JSONObject(res);
			JSONObject data = json.getJSONObject("result");
			JSONArray arr = data.getJSONArray("list");
			int num = data.getInt("total");
			if (num > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:HH:ss");
				Calendar nowtime = Calendar.getInstance();
				int yearnow = nowtime.get(Calendar.YEAR);
				for (int i = 0; i < arr.length(); i++) {
					item = new TableItem_Visit();
					item.setVisitorid(arr.optJSONObject(i).getString(
							"visitorid"));
					item.setVid(arr.optJSONObject(i).getString("vid"));
					item.setUid(arr.optJSONObject(i).getString("uid"));// 对方id
					long time = arr.optJSONObject(i).getInt("time");
					item.setTime(sdf.format(time * 1000));
					JSONObject user = arr.optJSONObject(i)
							.getJSONObject("user");
					String user_nick = user.getString("nickname");
					int user_birth = user.getInt("birthyear");
					String age = (yearnow - user_birth) + "";// 年龄

					item.setUser_pic(user.getString("pic"));
					item.setUser_nick(user_nick);
					String user_info = " 今年" + age + "岁";
					item.setUser_info(user_info);
					Log.i("XML_Parser_Visit", item + "");
					items.add(item);
				}
			} else {
				// num=0 不处理
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("XML_Parser_Visit", items + "");
		return items;

	}
}
