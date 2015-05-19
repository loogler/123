package com.qingyuan.activity.expand;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.qingyuan.MainActivity;
import com.qingyuan.R;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.CustomProgressDialog;
import com.qingyuan.util.HttpUtil;

/**
 * 
 * @author Administrator 加载情缘商城-----虚拟商品
 */
public class QingyuanMall extends Activity implements OnItemClickListener {
	private static final String tag = "QingyuanMall";

	private String home_uid;
	private int pageIndex = 1;
	private Message msg;

	private GridView gridView;
	private List<QingyuanMall_Info> list_Infos = new ArrayList<QingyuanMall.QingyuanMall_Info>();
	private PullToRefreshGridView pullToRefreshGridView;
	private QingyuanMall_Adapter adapter_Mall;

	private boolean payType = false;// 参数
									// link为真，支付方式为积分，link为假，或者不加link参数时，支付方式为钱包支付。
	private String fuid;// 接受edit输入参数。
	// 弹窗
	private Dialog dia;
	// 用于传递值的变量。
	private String mn, ct, i;

	AsyncImageLoader2 asyncImageLoader2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_other_qy_mall);
		pullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);

		adapter_Mall = new QingyuanMall_Adapter(QingyuanMall.this, list_Infos,
				gridView);
		gridView = pullToRefreshGridView.getRefreshableView();

		new XML_Parse_Thread().start();
		gridView.setAdapter(adapter_Mall);
		pullToRefreshGridView
				.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_FROM_END);
		pullToRefreshGridView
				.setOnRefreshListener(new OnRefreshListener<GridView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<GridView> refreshView) {
						pageIndex++;
						new XML_Parse_Thread().start();
					}
				});

	}

	/**
	 * 
	 * @author Administrator 网络解析器（json）
	 */
	class XML_Parse_Thread extends Thread {

		String showMSG;
		/** 尝试从json处获取资源 */
		String url = HttpUtil.BASE_URL
				+ "&f=gift&toType=json&action=LIST&page_size=15&android_uid="
				+ home_uid + "&page=" + pageIndex;

		List<QingyuanMall_Info> infos = new ArrayList<QingyuanMall.QingyuanMall_Info>();
		QingyuanMall_Info info = null;

		@Override
		public void run() {

			try {
				String res = HttpUtil.getRequest(url);
				JSONObject js = new JSONObject(res);
				showMSG = js.getString("message");
				if (js.getInt("code") == 1) {
					JSONArray arr = js.getJSONObject("result").getJSONArray(
							"list");
					for (int i = 0; i < arr.length(); i++) {
						info = new QingyuanMall_Info();
						info.setId(arr.optJSONObject(i).getString("id"));
						info.setName(arr.optJSONObject(i).getString("name"));
						info.setPrice(arr.optJSONObject(i).getString("price"));
						info.setScore(arr.optJSONObject(i).getString("score"));
						info.setSrc(arr.optJSONObject(i).getString("src"));
						infos.add(info);
					}

				} else if (js.getInt("code") == 10000) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(QingyuanMall.this, showMSG,
									Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					// code=10001
					Log.e(tag, "jsoncode>10000");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			msg = handler.obtainMessage();
			msg.obj = infos;
			msg.sendToTarget();
		}

	}

	/**
	 * 
	 * @author Administrator 列表适配器
	 */
	class QingyuanMall_Adapter extends BaseAdapter {
		GridView mgridView;
		Context context;
		List<QingyuanMall_Info> infos;
		LayoutInflater inflater;
		AsyncImageLoader2 asyncImageLoader2 = new AsyncImageLoader2();

		public QingyuanMall_Adapter(Context context,
				List<QingyuanMall_Info> infos, GridView gridView) {
			this.mgridView = gridView;
			this.context = context;
			this.infos = infos;
			inflater = LayoutInflater.from(this.context);

		}

		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		//
		class ViewHolder {
			ImageView iv_photo;
			TextView tv_name;
			TextView tv_price;
			TextView tv_cent;

		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {

			ViewHolder viewHolder = null;
			final String url = HttpUtil.URL + "/"
					+ infos.get(position).getSrc();
			final String flowerIds = infos.get(position).getId();
			final String flowerPoints = infos.get(position).getScore();
			final String flowerPrice = infos.get(position).getPrice();
			final String flowerName = infos.get(position).getName();

			view = inflater.inflate(R.layout.item_qy_mall, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_photo = (ImageView) view
					.findViewById(R.id.iv_qy_mall);
			viewHolder.tv_cent = (TextView) view
					.findViewById(R.id.tv_qy_mall_cent);
			viewHolder.tv_name = (TextView) view
					.findViewById(R.id.tv_qy_mall_name);
			viewHolder.tv_price = (TextView) view
					.findViewById(R.id.tv_qy_mall_price);

			loadImage(url, R.id.iv_qy_mall, view);

			viewHolder.tv_cent.setText(flowerPoints);
			viewHolder.tv_name.setText(flowerName);
			viewHolder.tv_price.setText(flowerPrice);

			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d(tag, "item点击事件是否相应=======");

					dia = new Dialog(QingyuanMall.this);
					dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dia.show();
					final Window wd = dia.getWindow();
					wd.setContentView(R.layout.dialog_qy_mall);
					TextView tv_qyMall = (TextView) wd
							.findViewById(R.id.tv_qyMall);
					ImageView iv_qyMall = (ImageView) wd
							.findViewById(R.id.iv_qyMall);
					FrameLayout frame_Cash = (FrameLayout) wd
							.findViewById(R.id.frame_qyMallCash);
					TextView tv_Cash = (TextView) wd
							.findViewById(R.id.tv_qyMallCash);
					final ImageView iv_Cash = (ImageView) wd
							.findViewById(R.id.iv_qyMallCash);
					FrameLayout frame_Cent = (FrameLayout) wd
							.findViewById(R.id.frame_qyMallCent);
					TextView tv_Cent = (TextView) wd
							.findViewById(R.id.tv_qyMallCent);
					final ImageView iv_Cent = (ImageView) wd
							.findViewById(R.id.iv_qyMallCent);
					final EditText edt_qyMall = (EditText) wd
							.findViewById(R.id.edt_qyMall);
					Button btn_qyMall = (Button) wd
							.findViewById(R.id.btn_qyMall);

					tv_qyMall.setText(flowerName);
					iv_qyMall.setImageDrawable(asyncImageLoader2.loadDrawable(
							url, new AsyncImageLoader2.ImageCallback() {
								public void imageLoaded(Drawable imageDrawable) {
									((ImageView) wd
											.findViewById(R.id.iv_qyMall))
											.setImageDrawable(imageDrawable);
								}
							}));
					tv_Cash.setText("现金支付： " + flowerPrice + " 元");
					tv_Cent.setText("积分支付： " + flowerPoints + " 分");
					frame_Cash.setOnTouchListener(new View.OnTouchListener() {

						@Override
						public boolean onTouch(View arg0, MotionEvent event) {
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								iv_Cash.setImageResource(android.R.drawable.presence_online);
								iv_Cent.setImageResource(android.R.drawable.presence_invisible);
							}
							payType = false;
							return false;
						}
					});
					frame_Cent.setOnTouchListener(new View.OnTouchListener() {

						@Override
						public boolean onTouch(View arg0, MotionEvent event) {
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								iv_Cash.setImageResource(android.R.drawable.presence_invisible);
								iv_Cent.setImageResource(android.R.drawable.presence_online);
							}
							payType = true;// 赋值后，支付方式参数link为真，
							return false;
						}
					});

					btn_qyMall.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// 发送结果判断,首先判断id选了没，支付方式选了没（默认的支付方式为现金），
							// 在判断钱包内现金/积分是否足够支付，不够则提示冲值后支付，否则直接支付
							fuid = edt_qyMall.getText().toString().trim();

							if (fuid.equals("") && fuid != null) {

								Toast.makeText(QingyuanMall.this, "请输入对方ID",
										Toast.LENGTH_SHORT).show();

							} else {
								// 发送

								CustomProgressDialog.createDialog(
										QingyuanMall.this, "提交中，请稍等。。。", 2500)
										.show();

								mn = flowerPrice;
								ct = flowerPoints;
								i = flowerIds;

								new PayThread().start();

							}

						}
					});

				}
			});

			return view;
		}

		/**
		 * 
		 * @param url
		 *            ==>img路径
		 * @param id
		 *            res路径，将img摆放于id位置，
		 * @param view
		 *            父容器
		 */
		private void loadImage(final String url, final int id, final View view) {
			// 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
			Drawable cacheImage = asyncImageLoader2.loadDrawable(url,
					new AsyncImageLoader2.ImageCallback() {
						// 请参见实现：如果第一次加载url时下面方法会执行
						public void imageLoaded(Drawable imageDrawable) {
							((ImageView) view.findViewById(id))
									.setImageDrawable(imageDrawable);
						}
					});
			if (cacheImage != null) {
				((ImageView) view.findViewById(id))
						.setImageDrawable(cacheImage);
			}
		}

	}

	Handler handler = new Handler() {

		// List<QingyuanMall_Info> infos=new
		// ArrayList<QingyuanMall.QingyuanMall_Info>();

		public void handleMessage(Message msg) {
			List<QingyuanMall_Info> infos = (List<QingyuanMall_Info>) msg.obj;
			if (infos != null) {

				list_Infos.addAll(infos);

			} else {
				// null
			}
			adapter_Mall.notifyDataSetChanged();
			pullToRefreshGridView.onRefreshComplete();

		};

	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long ids) {
	}

	// 当前类使用的参数
	private String moneyfloat;
	private String cent;
	private String jsMSG;

	/**
	 * 
	 * @author Administrator 赠送鲜花的线程，
	 */
	class PayThread extends Thread {
		@Override
		public void run() {
			try {
				String url_get = HttpUtil.getRequest(HttpUtil.BASE_URL
						+ "&toType=json&f=purse" + "&uid="
						+ MainActivity.home_uid);
				JSONObject js = new JSONObject(url_get);
				jsMSG = js.getString("message");
				JSONObject json = js.getJSONObject("result");
				if (js.getInt("code") == 1) {
					moneyfloat = json.getString("money");
					cent = json.getString("points");
				} else {
					// 没查到
					Log.e(tag, "余额查询失败，,,,," + jsMSG);
				}
			} catch (Exception e) {
				Log.e(tag, "余额查询 exception");
			}

			if (payType) {
				// 积分支付
				int fp = Integer.parseInt(ct);
				int ce = Integer.parseInt(cent);
				if (ce >= fp) {// 可以支付
					try {
						String urlCent = HttpUtil.getRequest(HttpUtil.BASE_URL
								+ "&uid=" + MainActivity.home_uid + "&fuid="
								+ fuid + "&id=" + i
								+ "&action=SEND&link=1&f=gift&toType=json");
						JSONObject js = new JSONObject(urlCent);
						jsMSG = js.getString("message");
						if (js.getInt("code") == 1) {
							// 发送成功（积分）,回到鲜花商城
							runOnUiThread(new Runnable() {
								public void run() {
									new AlertDialog.Builder(QingyuanMall.this)
											.setTitle("提示：").setMessage(jsMSG)
											.setPositiveButton("确定", null)
											.create().show();
									dia.cancel();

								}
							});
						} else if (js.getInt("code") == 10000) {
							// 失败，
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(QingyuanMall.this,
											"发送失败： " + jsMSG, Toast.LENGTH_LONG)
											.show();
								}
							});
						} else {
							Log.e(tag, "积分支付结果code>10000");
						}

					} catch (Exception e) {
						Log.e(tag, "积分支付时失败Exception");
					}

				} else {
					// 余额不足，尝试现金支付或者签到
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(QingyuanMall.this, "积分不足，尝试现金支付",
									Toast.LENGTH_LONG).show();
							dia.cancel();
						}
					});
				}

			} else {
				// 现金支付
				float fp = Float.parseFloat(mn);
				float mf = Float.parseFloat(moneyfloat);
				if (mf >= fp) {
					// 支付
					try {
						String urlCent = HttpUtil.getRequest(HttpUtil.BASE_URL
								+ "&uid=" + MainActivity.home_uid + "&fuid="
								+ fuid + "&id=" + i
								+ "&action=SEND&f=gift&toType=json");
						JSONObject js = new JSONObject(urlCent);
						jsMSG = js.getString("message");
						if (js.getInt("code") == 1) {
							// 发送成功（积分）,回到鲜花商城
							runOnUiThread(new Runnable() {
								public void run() {
									new AlertDialog.Builder(QingyuanMall.this)
											.setTitle("提示：").setMessage(jsMSG)
											.setPositiveButton("确定", null)
											.create().show();
									dia.cancel();

								}
							});
						} else if (js.getInt("code") == 10000) {
							// 失败，
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(QingyuanMall.this,
											"发送失败： " + jsMSG, Toast.LENGTH_LONG)
											.show();
								}
							});
						} else {
							Log.e(tag, "现金支付结果code>10000");
						}

					} catch (Exception e) {
						Log.e(tag, "现金支付时失败Exception");
					}

				} else {
					// 余额不足，让他冲值
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(QingyuanMall.this, "余额不足，请冲值后尝试",
									Toast.LENGTH_LONG).show();
							dia.cancel();
						}
					});
				}

			}

		}

	}

	void pay() {

	}

	/**
	 * 
	 * @author Administrator 鲜花信息 价格 id 名称 ，积分值，路径地址
	 */
	class QingyuanMall_Info {
		private String id;
		private String name;
		private String price;
		private String score;
		private String src;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getScore() {
			return score;
		}

		public void setScore(String score) {
			this.score = score;
		}

		public String getSrc() {
			return src;
		}

		public void setSrc(String src) {
			this.src = src;
		}

	}

}