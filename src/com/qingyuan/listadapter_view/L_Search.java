package com.qingyuan.listadapter_view;

import java.util.List;

import com.qingyuan.R;
import com.qingyuan.TabRecommendActivity;
import com.qingyuan.util.AsyncImageLoader;
import com.qingyuan.util.AsyncImageLoader.ImageCallback;
import com.qingyuan.util.TableItem_Search;
import com.qingyuan.util.pulldown.PullDownView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class L_Search extends BaseAdapter {

	public static List<TableItem_Search> search_lists;
	//private List<TableItem> lists;
	private TabRecommendActivity context;
	private AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
	private PullDownView mPullDownView;
	private String msg;
	
	public L_Search(Context context, List<TableItem_Search> lists, PullDownView mPullDownView) {
		this.context = (TabRecommendActivity) context;
		L_Search.search_lists = lists;
		this.mPullDownView = mPullDownView;
	}

	@Override
	public int getCount() {
		return search_lists.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		String Tag = "img_"+search_lists.get(position).getTableId();
		if (convertView != null) {
			view = convertView;
		} else {
			view = LayoutInflater.from(context).inflate(R.layout.item_l_search, null);
		}
		
		//TextView name = (TextView) view.findViewById(R.id.item_title);//昵称
		TextView content = (TextView) view.findViewById(R.id.item_content);//四条基本信息
		ImageView img = (ImageView)view.findViewById(R.id.item_img);//头像
		TextView introduce= (TextView) view.findViewById(R.id.introduce);//introduce
//		Button send = (Button)view.findViewById(R.id.sendcomfession);
//		TextView statuText = (TextView)view.findViewById(R.id.statuText);

//		send.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				final TableLayout commissionLayout = (TableLayout)LayoutInflater.from(context).inflate(R.layout.send_commission, null);
//				new AlertDialog.Builder(context)
//				.setTitle("发送委托")
//				.setView(commissionLayout)
//				.setPositiveButton("提交", new OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						SharedPreferences preferences = context.getSharedPreferences("userInfo",Activity.MODE_PRIVATE);
//						EditText sendContent = (EditText) commissionLayout.findViewById(R.id.commission_content);
//						final int uid = Integer.parseInt(preferences.getString("uid", "0"));
//						final int toUid = Integer.parseInt(search_lists.get(position).getTableId());
//						final String commissionContent = sendContent.getText().toString().trim();
//						new Thread(new Runnable() {
//							
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								msg = MyAction.sendCommission(uid, toUid, commissionContent);
//								if(msg == null){
//									msg = "发送成功！";
//								}
//								context.runOnUiThread(new Runnable() {
//									
//									@Override
//									public void run() {
//										// TODO Auto-generated method stub
//										Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//									}
//								});
//							}
//						}).start();
//					}
//				})
//				.setNegativeButton("取消", null)
//				.create()
//				.show();
//			}
//		});
		if(TextUtils.isEmpty(search_lists.get(position).getTablePic()))
		{
			imageUrl="http://www.07919.com/public/default/images/getqrcode.jpeg";
		}
		else
		{
			imageUrl="http://www.07919.com/"+search_lists.get(position).getTablePic();
		}
		
		//lists里面的内容判定是否为空不能用equals("")或者equals(null)
		//必须用TextUtils.isEmpty()
		//实现无昵称用户的昵称显示为用户ID
		String str = "";
		if(TextUtils.isEmpty(search_lists.get(position).getTableNickname()))
		{
			str = search_lists.get(position).getTableId();
		}
		else
		{
			str = search_lists.get(position).getTableNickname();
		}	
		
		//显示性别，年龄，省，市地区的，在推荐页面中会默认根据当地ip选择对应地区的会员，
		//如果有其他地区想查看的，请去搜索会员界面
		str += "\n"+search_lists.get(position).getTableGender()+" "+search_lists.get(position).getTableAge()+" "+search_lists.get(position).getTableProvince()+search_lists.get(position).getTableCity();
//		str += "\n寻找 ";
//		显示寻找xxx对象的，省去
//		if(!search_lists.get(position).getTableChioceMinAge().equals("0") && !search_lists.get(position).getTableChioceMaxAge().equals("0")){
//			str += search_lists.get(position).getTableChioceMinAge()+"-"+search_lists.get(position).getTableChioceMaxAge()+"岁";
//		}else{
//			str += "不限";
//		}
//		str += search_lists.get(position).getTableChioceGender()+"士";
		content.setText(str);
//		statuText.setText(search_lists.get(position).getTableLoveStatus());
		//子线程获取图片
		//有一个好的解决方法：http://www.android100.org/html/201203/20/409.html
		//img.setImageBitmap(getBitmap());
		//Log.i("img",imageUrl);
		//img.setId(Integer.parseInt(search_lists.get(position).getTableId()));
		String str1 = null;
		str1=search_lists.get(position).getTableIntroduce();
		if (str1.equals("0")) {
			if (!search_lists.get(position).getTableChioceMinAge().equals("0")
					&& !search_lists.get(position).getTableChioceMaxAge()
							.equals("0")) {
				str1 = "\n寻找 "
						+ search_lists.get(position).getTableChioceMinAge()
						+ "-"
						+ search_lists.get(position).getTableChioceMaxAge()
						+ "岁"
						+ search_lists.get(position).getTableChioceGender()
						+ "士";
			} else {
				str1 = search_lists.get(position).getTableLoveStatus();
			}
			introduce.setText(str1);
		} else {
			introduce.setText(str1);
		}
		
		img.setTag(Tag);
		Drawable cacheImage = asyncImageLoader.loadDrawable(imageUrl,Tag,new ImageCallback() {
            public void imageLoaded(Drawable imageDrawable, String imageUrl, String Tag) {
                ImageView imageViewByTag = (ImageView) mPullDownView.findViewWithTag(Tag);
                if (imageViewByTag != null) {
                    imageViewByTag.setImageDrawable(imageDrawable);
                }
            }
        });
		  if(cacheImage!=null){
			  img.setImageDrawable(cacheImage);
		  }		
		return view;
	}//public View getView(int position, View convertView, ViewGroup parent) {括号的另一边	
}//public class ListAdapter extends BaseAdapter {括号的另一边
