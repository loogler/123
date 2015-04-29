package com.qingyuan.listadapter_view;

import java.util.List;

import com.qingyuan.R;
import com.qingyuan.util.AsyncImageLoader;
import com.qingyuan.util.AsyncImageLoader.ImageCallback;
import com.qingyuan.util.TableItem_Gift_List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class L_SearchPerson_GiftList extends BaseAdapter {

	public static List<TableItem_Gift_List> gift_lists;
	private Context context;
	private AsyncImageLoader async = new AsyncImageLoader();

	public L_SearchPerson_GiftList(Context context,
			List<TableItem_Gift_List> lists) {
		this.context = context;
		L_SearchPerson_GiftList.gift_lists = lists;
	}

	@Override
	public int getCount() {
		return gift_lists.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	String imageUrl;

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		View view = null;
		if (convertView != null) {
			view = convertView;
		} else {
			view = LayoutInflater.from(context).inflate(
					R.layout.item_l_searchpersongift, null);
		}
		String Tag = "img_" + gift_lists.get(position).getTableId();
		TextView name = (TextView) view.findViewById(R.id.item_title);// 礼品名称
		TextView content = (TextView) view.findViewById(R.id.item_content);// 礼品介绍
		ImageView img = (ImageView) view.findViewById(R.id.item_img);// 头像
		TextView sendButton = (TextView) view.findViewById(R.id.sendButton);
		if (TextUtils.isEmpty(gift_lists.get(position).getTableSrc())) {
			img.setVisibility(View.VISIBLE);
			imageUrl = "http://www.07919.com/public/default/images/getqrcode.jpeg";
		} else {
			img.setVisibility(View.VISIBLE);
			imageUrl = "http://www.07919.com/"
					+ gift_lists.get(position).getTableSrc();
		}
		int num = gift_lists.get(position).getNum();
		Log.i("position", String.valueOf(position));
		Log.i("num", String.valueOf(num));
		if (num > 0) {
			sendButton.setText("免费赠送(" + num + "次)");
		} else {
			sendButton.setText("立即赠送");
		}
		name.setText(gift_lists.get(position).getTableGiftName());
		content.setText("价格：" + gift_lists.get(position).getTablePrice() + "元");
		img.setTag(Tag);
		Drawable imgCache = async.loadDrawable(imageUrl, Tag,
				new ImageCallback() {
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl, String Tag) {
						ImageView imageViewByTag = (ImageView) parent
								.findViewWithTag(Tag);
						if (imageViewByTag != null) {
							imageViewByTag.setImageDrawable(imageDrawable);
						}
					}
				});
		img.setImageDrawable(imgCache);

		return view;
	}// public View getView(int position, View convertView, ViewGroup parent)
		// {括号的另一边

}// public class ListAdapter_Gift_List extends BaseAdapter括号的另一边

