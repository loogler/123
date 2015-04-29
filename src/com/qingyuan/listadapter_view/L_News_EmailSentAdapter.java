package com.qingyuan.listadapter_view;

import java.util.List;

import com.qingyuan.R;
import com.qingyuan.util.AsyncImageLoader;
import com.qingyuan.util.TableItem_Email;
import com.qingyuan.util.AsyncImageLoader.ImageCallback;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class L_News_EmailSentAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	public static List<TableItem_Email> mData;
	private Context context;
	
	private AsyncImageLoader asyncImageLoader=new AsyncImageLoader();
	
	public L_News_EmailSentAdapter(Context context, 
			List<TableItem_Email> search_strings) {
//		inflater=LayoutInflater.from(context);
		this.context=context;
		mData=search_strings;
		
		
	}
	String imgUrl;
	
	@Override
	public int getCount() {
		return mData.size();
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
	public View getView(int position, View convertView, final ViewGroup parent) {
		String Tag = "img_" + mData.get(position).getTableUid();
		if (convertView==null) {
			convertView = inflater.from(context).inflate(R.layout.item_newsadapter, parent,
					false);
			ImageView iv_recent_avatar = (ImageView) convertView
					.findViewById(R.id.iv_recent_avatar);
			TextView tv_recent_name = (TextView) convertView
					.findViewById(R.id.tv_recent_name);
			TextView tv_recent_msg = (TextView) convertView
					.findViewById(R.id.tv_recent_msg);
			TextView tv_recent_time = (TextView) convertView
					.findViewById(R.id.tv_recent_time);
			TextView tv_recent_unread = (TextView) convertView
					.findViewById(R.id.tv_recent_unread);
			
			iv_recent_avatar.setVisibility(View.VISIBLE);
			imgUrl = "http://www.07919.com/"
					+ mData.get(position).getTablePic();
			Drawable imgCache = asyncImageLoader.loadDrawable(imgUrl, Tag,
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
			
			iv_recent_avatar.setImageDrawable(imgCache);
			tv_recent_name.setText(mData.get(position).getTableNickname());
//			if (mData.get(position).getTableNewsNum().equals("1")) {
//				tv_recent_unread.setVisibility(View.GONE);
//			}else {
//				//不处理，默认显示一个红色点点。
//			}
//			
			tv_recent_time.setText(mData.get(position).getTableDate());
			tv_recent_msg.setText(mData.get(position).getTableTitle());
		}
				
				
		return convertView;
	}

}

