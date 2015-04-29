package com.qingyuan.activity.expand;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.qingyuan.R;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.HttpUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Administrator 加载情缘商城-----虚拟商品
 */
public class QingyuanMall extends Activity {
	private static final String tag = "QingyuanMall";

	private String home_uid;
	private int pageIndex = 1;
	private Message msg;

	private GridView gridView;
	private List<QingyuanMall_Info> list_Infos = new ArrayList<QingyuanMall.QingyuanMall_Info>();
	private PullToRefreshGridView pullToRefreshGridView;
	private QingyuanMall_Adapter adapter_Mall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_qy_mall);
		pullToRefreshGridView=(PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
		
		adapter_Mall= new QingyuanMall_Adapter(QingyuanMall.this, list_Infos,gridView);
		gridView=pullToRefreshGridView.getRefreshableView();
		
		
		new  XML_Parse_Thread().start();
		gridView.setAdapter(adapter_Mall);
		pullToRefreshGridView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_FROM_END);
		pullToRefreshGridView.setOnRefreshListener(new  OnRefreshListener<GridView>() {
			
			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
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
		String url=HttpUtil.BASE_URL
				+ "&f=gift&toType=json&action=LIST&page_size=15&android_uid=" + home_uid
				+ "&page=" + pageIndex;
	
		List<QingyuanMall_Info> infos = new ArrayList<QingyuanMall.QingyuanMall_Info>();
		QingyuanMall_Info info = null;

		@Override
		public void run() {

			try {
				String res=HttpUtil.getRequest(url);
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
				List<QingyuanMall_Info> infos,GridView gridView) {
			this.mgridView=gridView;
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
			String url = HttpUtil.URL + "/" + infos.get(position).getSrc();
			
				view = inflater.inflate(R.layout.item_qy_mall, null);
				viewHolder = new ViewHolder();
				viewHolder.iv_photo = (ImageView) view
						.findViewById(R.id.iv_qy_mall);
				viewHolder.tv_cent = (TextView) view
						.findViewById(R.id.tv_qy_mall_cent);
				viewHolder.tv_name = (TextView) view
						.findViewById(R.id.tv_qy_mall_name);
				viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_qy_mall_price);

				loadImage(url, R.id.iv_qy_mall, view);
				viewHolder.tv_cent.setText(infos.get(position).getScore());
				viewHolder.tv_name.setText(infos.get(position).getName());
				viewHolder.tv_price.setText(infos.get(position).getPrice());
			
			view.setOnClickListener( new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(QingyuanMall.this, "输入接收者id/鲜花种类/支付方式", Toast.LENGTH_LONG).show();

						}
					});
					//TODO  跳入支付界面 
					
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

	/**
	 * 
	 * @author Administrator
	 *
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