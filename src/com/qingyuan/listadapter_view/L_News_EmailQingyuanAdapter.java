package com.qingyuan.listadapter_view;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingyuan.R;
import com.qingyuan.util.AsyncImageLoader2;
import com.qingyuan.util.TableItem_Email;

public class L_News_EmailQingyuanAdapter extends BaseAdapter {
	Context context;
	public static List<TableItem_Email> searchList;
	private AsyncImageLoader2 asyncImageLoader2 = new AsyncImageLoader2();

	public L_News_EmailQingyuanAdapter(Context context,
			List<TableItem_Email> searchList) {
		this.context = context;
		L_News_EmailQingyuanAdapter.searchList = searchList;
	}

	@Override
	public int getCount() {
		return searchList.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=null;
		if (convertView==null) {
			view=LayoutInflater.from(context).inflate(R.layout.item_newsadapter, null);
			ImageView iv_recent_avatar = (ImageView) view
					.findViewById(R.id.iv_recent_avatar);
			TextView tv_recent_name = (TextView) view
					.findViewById(R.id.tv_recent_name);
			TextView tv_recent_msg = (TextView) view
					.findViewById(R.id.tv_recent_msg);
			TextView tv_recent_time = (TextView) view
					.findViewById(R.id.tv_recent_time);
		
			
			iv_recent_avatar.setVisibility(View.VISIBLE);
			String imgUrl = "http://www.07919.com/"
					+ searchList.get(position).getTablePic();
			loadImage(imgUrl, R.id.iv_recent_avatar, view);
			tv_recent_name.setText("红娘来信");		
			tv_recent_time.setText(searchList.get(position).getTableDate());
			tv_recent_msg.setText(searchList.get(position).getTableTitle());
		}else {
			view=convertView;
		}
		return view;
	}

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
			((ImageView) view.findViewById(id)).setImageDrawable(cacheImage);
		}
	}
}
