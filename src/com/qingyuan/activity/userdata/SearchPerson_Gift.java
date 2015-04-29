package com.qingyuan.activity.userdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qingyuan.R;
import com.qingyuan.listadapter_view.L_SearchPerson_GiftList;
import com.qingyuan.service.parser.XMLParser_Gift_List;
import com.qingyuan.util.HttpUtil;
import com.qingyuan.util.TableItem_Gift_List;
import com.qingyuan.util.pulldown.PullDownView;
import com.qingyuan.util.pulldown.PullDownView.OnPullDownListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 发送鲜花
 * 
 * @author Administrator
 *
 */
@SuppressLint("HandlerLeak")
public class SearchPerson_Gift extends Activity implements OnPullDownListener,
		OnItemClickListener {

	// 定义两个发送的值：礼物的id和接受礼物的人的id
	String gift_fid, gift_link;
	String gift_fuid = "" + SearchPersonActivity.search_person_fuid;
	// 发送给接收方的祝福语
	String gift_content = "你好！";

	/************************* 请×××叫×××我×××分×××割×××线 *************************/
	private static final int WHAT_DID_LOAD_DATA = 0;// 页面初始化时，加载数据
	// private static final int WHAT_DID_REFRESH = 1;// 下拉刷新数据
	private static final int WHAT_DID_MORE = 2;// 上划加载更多数据

	private ListView mListView;// 资讯栏目列表
	private L_SearchPerson_GiftList mAdapter;// 列表适配器

	private PullDownView mPullDownView;// 自定义下拉，上划view
	private List<TableItem_Gift_List> gift_list_strings = new ArrayList<TableItem_Gift_List>();// 更新列表的数据集合

	private String xml = "";// 存储XML数据

	private List<TableItem_Gift_List> posters = null;// 解析出的资讯栏目集合
	private Message msg;// 向Handler发送的消息

	String home_uid, home_gender;
	private int pageIndex = 1, numGift = 0;
	private String showMsg;
	private String[] listLeer;
	private String msgLeer;
	private String[] arrPay = { "钱包支付", "积分支付" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchperson_gift);

		SharedPreferences preferences = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		home_uid = preferences.getString("uid", "");
		home_gender = preferences.getString("gender", "");

		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mAdapter = new L_SearchPerson_GiftList(SearchPerson_Gift.this,
				gift_list_strings);
		mListView.setAdapter(mAdapter);
		mPullDownView.enableAutoFetchMore(true, 1);

		loadData(0);
	}// onCreate括号的另一边

	/************************* 请×××叫×××我×××分×××割×××线 *************************/
	// 数据加载
	// @param type
	// 判断是上划加载更多type= 1、下拉更新：type=2、数据初始化：type=0

	private void loadData(final int type) {
		final XMLParser_Gift_List parser = new XMLParser_Gift_List();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				List<TableItem_Gift_List> strings = null;
				strings = new ArrayList<TableItem_Gift_List>();

				switch (type) {
				case 0:
					msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
					break;
				case 1:
					msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
					break;

				default:
					break;
				}

				// 获取XML数据
				// url加上页码
				xml = parser
						.getXmlFromUrl_gift_list(HttpUtil.BASE_URL
								+ "&f=gift&toType=json&action=LIST&page_size=8&android_uid="
								+ home_uid + "&page=" + pageIndex);
				if (!TextUtils.isEmpty(xml)) {
					try {
						JSONObject json = new JSONObject(xml);
						int code = json.getInt("code");
						showMsg = json.getString("message");
						if (code == 1) {
							try {
								posters = parser.parseTableItem(xml);
								for (TableItem_Gift_List body : posters) {
									strings.add(body);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (code == 10000) {
							SearchPerson_Gift.this
									.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(
													SearchPerson_Gift.this,
													showMsg, Toast.LENGTH_LONG)
													.show();
										}
									});
						} else {
							SearchPerson_Gift.this
									.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(
													SearchPerson_Gift.this,
													"服务器异常，请稍后再试",
													Toast.LENGTH_SHORT).show();
										}
									});
							Log.e("ERROE", showMsg);
						}
					} catch (JSONException e1) {
						e1.printStackTrace();
						SearchPerson_Gift.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(SearchPerson_Gift.this,
										"服务器异常，请稍后再试", Toast.LENGTH_SHORT)
										.show();
							}
						});
					}
				} else {
					SearchPerson_Gift.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(SearchPerson_Gift.this,
									"服务器异常，请稍后再试", Toast.LENGTH_SHORT).show();
						}
					});
				}
				msg.obj = strings;
				msg.sendToTarget();
			}
		}).start();
	}

	// 刷新
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pageIndex = 1;
				loadData(2);
			}
		}).start();
	}

	// 加载更多
	public void onMore() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pageIndex++;
				loadData(1);
			}
		}).start();
	}

	// 更新页面数据（只有加载更多有用）
	private Handler mUIHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA: {
				if (msg.obj != null) {
					List<TableItem_Gift_List> strings = (List<TableItem_Gift_List>) msg.obj;
					if (!strings.isEmpty()) {
						gift_list_strings.addAll(strings);
						mAdapter.notifyDataSetChanged();
					}
				}
				mAdapter.notifyDataSetChanged();
				// 诉它数据加载完毕;
				mPullDownView.notifyDidLoad();
				break;
			}

			case WHAT_DID_MORE: {
				List<TableItem_Gift_List> strings = (List<TableItem_Gift_List>) msg.obj;
				if (!strings.isEmpty()) {
					gift_list_strings.addAll(strings);
					mAdapter.notifyDataSetChanged();
				}
				// 告诉它获取更多完毕
				mPullDownView.notifyDidMore();
				break;
			}
			}
		}

	};

	/************************* 请×××叫×××我×××分×××割×××线 *************************/
	// item被点中的事件
	// 进入消息列表
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 先确定礼物的号码
		gift_fid = L_SearchPerson_GiftList.gift_lists.get(position)
				.getTableId();
		numGift = L_SearchPerson_GiftList.gift_lists.get(position).getNum();
		Log.i("num", String.valueOf(numGift));
		SearchPerson_Gift.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				String res = null;
				try {
					res = HttpUtil.getRequest(HttpUtil.BASE_URL
							+ "&f=sinfo&toType=json&type=" + home_gender);
					if (res != null) {
						JSONObject json = new JSONObject(res);
						int code = json.getInt("code");
						showMsg = json.getString("message");
						if (code == 1) {
							JSONArray array = json.getJSONObject("result")
									.getJSONArray("list");
							listLeer = new String[array.length()];
							for (int i = 0; i < array.length(); i++) {
								listLeer[i] = array.optJSONObject(i).getString(
										"content");
							}
							SearchPerson_Gift.this
									.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											String nextButton = numGift > 0 ? "立即赠送"
													: "下一步";
											new AlertDialog.Builder(
													SearchPerson_Gift.this)
													.setTitle("选择问候语：")
													.setSingleChoiceItems(
															listLeer,
															0,
															new DialogInterface.OnClickListener() {

																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	msgLeer = listLeer[which];
																}
															})
													.setPositiveButton(
															nextButton,
															new OnClickListener() {
																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	dialog.dismiss();
																	gift_link = "0";
																	if (numGift <= 0) {
																		new AlertDialog.Builder(
																				SearchPerson_Gift.this)
																				.setTitle(
																						"支付方式：")
																				.setSingleChoiceItems(
																						arrPay,
																						0,
																						new OnClickListener() {

																							@Override
																							public void onClick(
																									DialogInterface dialog,
																									int which) {
																								if (which == 1) {
																									gift_link = "1";
																								}
																							}
																						})
																				.setPositiveButton(
																						"立即赠送",
																						new OnClickListener() {

																							@Override
																							public void onClick(
																									DialogInterface dialog,
																									int which) {
																								new GiftSendThread()
																										.start();
																							}
																						})
																				.setNegativeButton(
																						"取消",
																						null)
																				.create()
																				.show();
																	} else {
																		new GiftSendThread()
																				.start();
																	}
																}
															})
													.setNegativeButton("取消",
															null).create()
													.show();
										}
									});
						} else if (code == 10000) {
							Toast.makeText(SearchPerson_Gift.this, showMsg,
									Toast.LENGTH_SHORT).show();
						} else if (code >= 10000) {
							Log.e("ERROR", showMsg);
							Toast.makeText(SearchPerson_Gift.this,
									"服务器异常，请稍后在试！", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(SearchPerson_Gift.this, "服务器异常，请稍后在试！",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Toast.makeText(SearchPerson_Gift.this, "服务器异常，请稍后在试！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/************************* 请×××叫×××我×××分×××割×××线 *************************/
	class GiftSendThread extends Thread {

		public void run() {
			String res;
			Map<String, String> param = new HashMap<String, String>();
			param.put("fuid", gift_fuid);
			param.put("fid", gift_fid);
			param.put("link", gift_link);
			param.put("content", msgLeer);
			try {
				res = HttpUtil.postRequest(HttpUtil.BASE_URL
						+ "&f=gift&action=SEND&toType=json&android_uid"
						+ home_uid, param);
				JSONObject json = new JSONObject(res);
				int code = json.getInt("code");
				showMsg = json.getString("message");
				SearchPerson_Gift.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(SearchPerson_Gift.this, showMsg,
								Toast.LENGTH_LONG).show();
					}
				});
			} catch (Exception e1) {
				SearchPerson_Gift.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(SearchPerson_Gift.this, "服务器异常，请稍候再试！",
								Toast.LENGTH_SHORT).show();
					}
				});
				e1.printStackTrace();
			}
		}
	}

	/************************* 请×××叫×××我×××分×××割×××线 *************************/

}// public class GiftListActivity extends Activity implements
	// OnPullDownListener, OnItemClickListener括号的另一边
