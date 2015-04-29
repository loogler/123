package com.qingyuan.service.parser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.qingyuan.util.HttpUtil;
import com.qingyuan.util.TableItem_Email;

public class XMLParser_Email {

	String tag = "XMLParser_Email";

	public XMLParser_Email() {
	}

	// 网络下载xml
	public String getXmlFromUrl_leer_gift_liker(String url) {

		String xml = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			xml = EntityUtils.toString(httpEntity, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xml;
	}

	public String getStrFromUrl(String url) {
		String str = null;
		try {
			str = HttpUtil.getRequest(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * @Notes 红娘来信使用方法
	 * @param res
	 * @return json取得的值。
	 */
	@SuppressLint("SimpleDateFormat")
	public List<TableItem_Email> parserTableItem(String res){
		List<TableItem_Email> items = new ArrayList<TableItem_Email>();
		TableItem_Email item = null;
		JSONObject json = null;
		try {
			json = new JSONObject(res);
				JSONObject json1 = json.getJSONObject("result");
				
				int messageNum = json1.getInt("total");
				if (messageNum > 0) {
					JSONArray arr = json1.getJSONArray("list");
					item = new TableItem_Email();
					for (int i = 0; i < messageNum; i++) {
						item = new TableItem_Email();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						long time = arr.optJSONObject(i).getInt("cdate");
						item.setTableDate(sdf.format(time * 1000));
						item.setTableId(arr.optJSONObject(i).getString("id"));
						item.setTableStatus(arr.optJSONObject(i).getString(
								"status"));
						item.setTableUid(arr.optJSONObject(i).getString("uid"));
						item.setTableFuid(arr.optJSONObject(i)
								.getString("fuid"));
						item.setTableTitle(arr.optJSONObject(i).getString(
								"title"));
						

						items.add(item);

					}
				} else {
					// messageNum=0 ,null
				}

			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;
		
	}
/**
 * @Notes 会员邮件使用的解析器，主要区别在于要不要取得user节点
 * @param res
 * @return json取得的值
 */
	public List<TableItem_Email> formatTableItem(String res) {
		List<TableItem_Email> items = new ArrayList<TableItem_Email>();
		TableItem_Email item = null;
		JSONObject json = null;
		try {
			json = new JSONObject(res);
				JSONObject json1 = json.getJSONObject("result");
				
				int messageNum = json1.getInt("total");
				if (messageNum > 0) {
					JSONArray arr = json1.getJSONArray("list");
					
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					for (int i = 0; i < messageNum; i++) {
						item = new TableItem_Email();
						long time = arr.optJSONObject(i).getInt("cdate");
						item.setTableDate(sdf.format(time * 1000));
						item.setTableId(arr.optJSONObject(i).getString("id"));
						item.setTableStatus(arr.optJSONObject(i).getString(
								"status"));
						item.setTableUid(arr.optJSONObject(i).getString("uid"));
						item.setTableFuid(arr.optJSONObject(i)
								.getString("fuid"));
						item.setTableTitle(arr.optJSONObject(i).getString(
								"title"));
						JSONObject user = arr.optJSONObject(i).getJSONObject(
								"user");
						item.setTableNickname(user.getString("nickname"));
						item.setTablePic(user.getString("pic"));

						items.add(item);

					}
				} else {
					// messageNum=0 ,null
				}

			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;

	}

}
