package com.qingyuan.service.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import com.qingyuan.util.HttpUtil;
import com.qingyuan.util.TableItem_News;

import android.R.integer;
import android.util.Log;
import android.util.Xml;

/**
 * 
 * @author no_2 消息界面网页的解析器，用于解释并填充list单元格内容来源。set TableItemNews。！
 */
public class XMLParser_News {

	int result_num;
	private final String tag = "XMLParser_News";

	public XMLParser_News() {
	}

	// 网络下载xml
	public String getXmlFromUrl_news(String url) {
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

	public List<TableItem_News> fromatTableItem(String is) {
		List<TableItem_News> items = new ArrayList<TableItem_News>();
		TableItem_News item = null;
		JSONObject json;

		try {
			json = new JSONObject(is);
			if (json.getInt("code") == 1) {
				json = json.getJSONObject("result");
				int email_num = json.getJSONObject("email").getInt("num");
				if (email_num > 0) {
					JSONArray arr_email = json.getJSONObject("email")
							.getJSONArray("rows");
					Log.i(tag, "email" + arr_email.toString());
					for (int j = 0; j < arr_email.length(); j++) {

						int fuid = arr_email.optJSONObject(j).getInt("fuid");

						if (fuid > 0) {

							item = new TableItem_News();
							// arr_email.optJSONObject(i).getJSONObject("user");
							// arr_email.optJSONObject(i).getString("content");
							// arr_email.optJSONObject(i).getString("title");
							// arr_email.optJSONObject(i).getString("time");
							item.setTableMessageId("会员邮件");
							item.setTableUserId(arr_email.optJSONObject(j)
									.getString("fuid"));
							item.setTableNewsNum("1");
							item.setTableMessage(arr_email.optJSONObject(j)
									.getString("title"));
							item.setTableNickname(arr_email.optJSONObject(j)
									.getJSONObject("user")
									.getString("nickname"));
							item.setTablePic(arr_email.optJSONObject(j)
									.getJSONObject("user").getString("pic"));
							long time = arr_email.optJSONObject(j).getInt(
									"time");
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							item.setTableDate(sdf.format(time * 1000));
							Log.w("xmlparser_news date",
									sdf.format(time * 1000));
							items.add(item);
							Log.i(tag, "huiyuan  email" + items.toString());

						} else {
							// 会员邮件为空
						}
						;

						if (fuid == 0) {

							item = new TableItem_News();
							item.setTableMessageId("情缘邮件");
							item.setTableNewsNum("新");
							item.setTableNickname("情缘邮件");
							item.setTableMessage("立即查看");
							item.setTablePic("public/system/images/kefu.png");
							long time = arr_email.optJSONObject(j).getInt(
									"time");
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							item.setTableDate(sdf.format(time * 1000));
							items.add(item);
							Log.i(tag, "qingyuan email" + items.toString());

						} else {
							// 情缘邮件为空
						}
					}

				} else {
					// email为空
				}

				int visited_num = json.getJSONObject("visited").getInt("num");

				if (visited_num > 0) {
					JSONArray arr_visited = json.getJSONObject("visited")
							.getJSONArray("rows");

					item = new TableItem_News();
					item.setTableMessageId("访问");
					item.setTableUserId(arr_visited.optJSONObject(0).getString(
							"fuid"));
					item.setTableNewsNum(visited_num + "");
					// item.setTableMessage(arr_visited.optJSONObject(i).getString("content"));
					item.setTableNickname(arr_visited.optJSONObject(0)
							.getJSONObject("user").getString("nickname"));
					item.setTablePic(arr_visited.optJSONObject(0)
							.getJSONObject("user").getString("pic"));
					long time = arr_visited.optJSONObject(0).getInt("time");
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					item.setTableDate(sdf.format(time * 1000));
					item.setTableMessage("访问了您");
					Log.w("xmlparser_news date", sdf.format(time * 1000));
					items.add(item);
					Log.i(tag, "visited" + items.toString());

				} else {
					// 访问记录为空
				}
				int leer_num = json.getJSONObject("leer").getInt("num");
				if (leer_num > 0) {
					JSONArray arr_leer = json.getJSONObject("leer")
							.getJSONArray("rows");

					item = new TableItem_News();
					item.setTableMessageId("秋波");
					item.setTableUserId(arr_leer.optJSONObject(0).getString(
							"fuid"));
					item.setTableNewsNum(leer_num + "");
					// item.setTableMessage(arr_leer.optJSONObject(i).getString("content"));
					item.setTableNickname(arr_leer.optJSONObject(0)
							.getJSONObject("user").getString("nickname"));
					item.setTablePic(arr_leer.optJSONObject(0)
							.getJSONObject("user").getString("pic"));
					long time = arr_leer.optJSONObject(0).getInt("time");
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					item.setTableDate(sdf.format(time * 1000));
					item.setTableMessage("给您发送了一个秋波");
					Log.w("xmlparser_news date", sdf.format(time * 1000));
					items.add(item);
					Log.i(tag, "leer" + items.toString());

				} else {
					// 秋波为空
				}
				int gift_num = json.getJSONObject("gift").getInt("num");
				if (gift_num > 0) {
					JSONArray arr_gift = json.getJSONObject("gift")
							.getJSONArray("rows");

					item = new TableItem_News();
					item.setTableMessageId("礼物");
					item.setTableUserId(arr_gift.optJSONObject(0).getString(
							"fuid"));
					item.setTableNewsNum(gift_num + "");
					// item.setTableMessage(arr_gift.optJSONObject(i).getString("content"));
					item.setTableNickname(arr_gift.optJSONObject(0)
							.getJSONObject("user").getString("nickname"));
					item.setTablePic(arr_gift.optJSONObject(0)
							.getJSONObject("user").getString("pic"));
					long time = arr_gift.optJSONObject(0).getInt("time");
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					item.setTableDate(sdf.format(time * 1000));
					item.setTableMessage("送您一份礼物");
					Log.w("xmlparser_news date", sdf.format(time * 1000));
					items.add(item);
					Log.i(tag, "gift" + items.toString());

				} else {
					// 鲜花礼物为空
				}
				int friend_num = json.getJSONObject("friend").getInt("num");
				if (friend_num > 0) {
					Log.i("xmlparser_jsonarray", friend_num + "");
					JSONArray arr_friend = json.getJSONObject("friend")
							.getJSONArray("rows");

					item = new TableItem_News();
					Log.i("xmlparser_jsonarray", arr_friend.optJSONObject(0)
							.getString("id"));
					item.setTableMessageId("意中人");
					item.setTableUserId(arr_friend.optJSONObject(0).getString(
							"fuid"));
					item.setTableNewsNum(friend_num + "");
					// item.setTableMessage(arr_friend.optJSONObject(i).getString("content"));
					item.setTableNickname(arr_friend.optJSONObject(0)
							.getJSONObject("user").getString("nickname"));
					item.setTablePic(arr_friend.optJSONObject(0)
							.getJSONObject("user").getString("pic"));
					long time = arr_friend.optJSONObject(0).getInt("time");
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					item.setTableDate(sdf.format(time * 1000));
					item.setTableMessage("已经加你为意中人了");
					Log.w("xmlparser_news date", sdf.format(time * 1000));
					items.add(item);
					Log.i(tag, "friend" + items.toString());

				} else {
					// 意中人为空
				}
				int commission_num = json.getJSONObject("commission").getInt(
						"num");
				if (commission_num > 0) {
					JSONArray arr_commission = json.getJSONObject("commission")
							.getJSONArray("rows");

					item = new TableItem_News();
					item.setTableMessageId("委托");
					item.setTableUserId(arr_commission.optJSONObject(0)
							.getString("fuid"));
					item.setTableNewsNum(commission_num + "");
					// item.setTableMessage(arr_commission.optJSONObject(i).getString("content"));
					item.setTableNickname(arr_commission.optJSONObject(0)
							.getJSONObject("user").getString("nickname"));
					item.setTablePic(arr_commission.optJSONObject(0)
							.getJSONObject("user").getString("pic"));
					long time = arr_commission.optJSONObject(0).getInt("time");
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					item.setTableDate(sdf.format(time * 1000));
					item.setTableMessage("委托红娘联系了您");
					Log.w("xmlparser_news date", sdf.format(time * 1000));
					items.add(item);
					Log.i(tag, "commission" + items.toString());

				} else {
					// 委托为空
				}
				int chat_num = json.getJSONObject("chat").getInt("num");
				if (chat_num > 0) {
					JSONArray arr_chat = json.getJSONObject("chat")
							.getJSONArray("rows");
					for (int i = 0; i < arr_chat.length(); i++) {
						item = new TableItem_News();
						item.setTableMessageId("聊天");
						item.setTableUserId(arr_chat.optJSONObject(i)
								.getString("fuid"));
						item.setTableMessage(arr_chat.optJSONObject(i)
								.getString("content"));
						item.setTableNewsNum("新");
						item.setTableNickname(arr_chat.optJSONObject(i)
								.getJSONObject("user").getString("nickname"));
						item.setTablePic(arr_chat.optJSONObject(i)
								.getJSONObject("user").getString("pic"));
						long time = arr_chat.optJSONObject(i).getInt("time");
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						item.setTableDate(sdf.format(time * 1000));
						Log.w("xmlparser_news date", sdf.format(time * 1000));
						items.add(item);
						Log.i(tag, "chat" + items.toString());
					}
				} else {
					// 聊天一天为空
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("return_list", items.toString());
		return items;
	}

	// @param is获取资讯栏目列表
	// @return items资讯栏目信息数据
	// @throws Exception

	/*
	 * public List<TableItem_News> parseTableItem(InputStream is) throws
	 * Exception { List<TableItem_News> items = new ArrayList<TableItem_News>();
	 * try { TableItem_News item = null;
	 * 
	 * XmlPullParser parser = Xml.newPullParser(); parser.setInput(is, "UTF-8");
	 * 
	 * int eventType = parser.getEventType(); while (eventType !=
	 * XmlPullParser.END_DOCUMENT) { switch (eventType) { case
	 * XmlPullParser.START_DOCUMENT: items = new ArrayList<TableItem_News>();
	 * break; case XmlPullParser.START_TAG:
	 * 
	 * if (parser.getName().equals("email")) { result_num = 1; } else if
	 * (parser.getName().equals("visited")) { result_num = 2; } else if
	 * (parser.getName().equals("chat")) { result_num = 3; } else if
	 * (parser.getName().equals("commission")) { result_num = 4; } else if
	 * (parser.getName().equals("leer")) { result_num = 5; } else if
	 * (parser.getName().equals("gift")) { result_num = 6; } else if
	 * (parser.getName().equals("like")) { result_num = 7; }
	 * 
	 * else if (parser.getName().equals("uid")) { item = new TableItem_News();
	 * eventType = parser.next(); item.setTableMessageId(parser.getText());
	 * 
	 * switch (result_num) { case 1: item.setTableNewsNum("向您发送了邮件"); break;
	 * case 2: item.setTableNewsNum("访问了您"); break; case 3:
	 * item.setTableNewsNum("向您发起聊天"); break; case 4:
	 * item.setTableNewsNum("已经委托红娘联系您"); break; case 5:
	 * item.setTableNewsNum("向您暗送秋波"); break; case 6:
	 * item.setTableNewsNum("送您一件礼物"); break; case 7:
	 * item.setTableNewsNum("将您加为意中人"); break; }
	 * 
	 * } else if (parser.getName().equals("nickname")) { eventType =
	 * parser.next(); item.setTableNickname(parser.getText()); }
	 * 
	 * else if (parser.getName().equals("pic")) { eventType = parser.next();
	 * item.setTablePic(parser.getText()); }
	 * 
	 * break; case XmlPullParser.END_TAG: if (parser.getName().equals("pic")) {
	 * items.add(item); item = null; } break; }// switch (eventType)括号的另一边
	 * 
	 * eventType = parser.next(); }// while括号的另一边 } // try括号的另一边 catch
	 * (Exception e) { } return items; }// public List<TableItem_News>
	 * parseTableItem(InputStream is) throws // Exception {括号的另一边
	 */
}
