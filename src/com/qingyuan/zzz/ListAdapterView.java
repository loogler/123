package com.qingyuan.zzz;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.qingyuan.R;
import com.qingyuan.util.pulldown.PullDownView;
import com.qingyuan.util.pulldown.PullDownView.OnPullDownListener;

/**
 * 
 * @author no_2
 * @Notes 这是一个写listadapterview的范例，在本app中 基本套用这套滑动加载模式。
 */
public class ListAdapterView extends Activity implements OnPullDownListener,
		OnItemClickListener {

	private static final int WHAT_DID_LOAD_DATA = 0;// 页面初始化时，加载数据
	// private static final int WHAT_DID_REFRESH = 1;// 下拉刷新数据
	private static final int WHAT_DID_MORE = 2;// 上划加载更多数据

	private ListView mListView;// 资讯栏目列表
	private ListAdapter_Example mAdapter;// 列表适配器
	private PullDownView mPullDownView;// 自定义下拉，上划view
	private List<TableItem> email_get_strings = new ArrayList<TableItem>();// 更新列表的数据集合
	private List<TableItem> posters = null;// 解析出的资讯栏目集合

	Message msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listadapter_view_example);

		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mAdapter = new ListAdapter_Example(ListAdapterView.this,
				email_get_strings);
		mListView.setAdapter(mAdapter);
		mPullDownView.enableAutoFetchMore(true, 1);
		// 加载数据
		loadData(0);
	}

	private void loadData(final int type) {
		new Thread(new Runnable() {
			public void run() {
				switch (type) {
				case WHAT_DID_LOAD_DATA:
					handler.obtainMessage(WHAT_DID_LOAD_DATA);
					break;
				case WHAT_DID_MORE:
					handler.obtainMessage(WHAT_DID_MORE);
					break;

				default:
					break;
				}
				msg.obj = null;
				msg.sendToTarget();
			}
		}).start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA:

				break;
			case WHAT_DID_MORE:

				break;
			default:
				break;

			}
		};
	};

	/**
	 * list点击事件，点击效果自定
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	/**
	 * 下面两个都是接口事件。
	 */
	@Override
	public void onRefresh() {

	}

	@Override
	public void onMore() {

	}

}

/**
 * 
 * @author no_2
 * @Notes adapter 适配器，产生子列表试图。
 *
 */
class ListAdapter_Example extends BaseAdapter {
	Context context;
	public static List<TableItem> email_get_lists;

	public ListAdapter_Example(Context context,
			List<TableItem> email_get_strings) {
		this.context = context;
		ListAdapter_Example.email_get_lists = email_get_strings;
	}

	@Override
	public int getCount() {
		return email_get_lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int posintion, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.listadapter_view_example_chilrd, null);
		} else {
			view = convertView;
		}
		return view;
	}

}

/**
 * 
 * @author no_2 单纯get set类
 *
 */
class TableItem {
	String uid;
	String nickname;
	String gender;
	String age;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
}
