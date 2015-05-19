package com.qingyuan.activity.expand;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qingyuan.R;

/**
 * 
 * @author Administrator 推荐给好友的功能
 * @function 获取好友列表展示，点击发送系统短消息（内容已订好）
 */
public class Invitation extends Activity {
	List<ContactInfos> infos;
	InvitationAdapter adapter;
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_other_invitation);
		lv = (ListView) findViewById(R.id.lv_invitation_contacts);
		infos = new ArrayList<Invitation.ContactInfos>();
		adapter = new InvitationAdapter(Invitation.this, infos);
		getContact();
		lv.setAdapter(adapter);

	}

	public void getContact() {

		// 获得所有的联系人
		Cursor cur = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		// 循环遍历
		if (cur.moveToFirst()) {
			int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
			int displayNameColumn = cur
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			do {
				ContactInfos in = new ContactInfos();
				// 获得联系人的ID号
				String contactId = cur.getString(idColumn);
				// 获得联系人姓名
				String disPlayName = cur.getString(displayNameColumn);
				in.setName(disPlayName);
				Log.d("联系人姓名： ", disPlayName);
				// 查看该联系人有多少个电话号码。如果没有这返回值为0
				int phoneCount = cur
						.getInt(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				// ////////////
				if (phoneCount > 0) {
					// 获得联系人的电话号码
					Log.i("执行", phoneCount + "");
					Cursor phones = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
					if (phones.moveToFirst()) {
						Log.i("phones.moveto first", "true=========");
						do {
							// 遍历所有的电话号码
							String phoneNumber = phones
									.getString(phones
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							in.setNum(phoneNumber);
							Log.i("联系人号码", phoneNumber);
						} while (phones.moveToNext());
					} else {
						// phones.moveToFirst()==false;
					}
				} else {
					// phoneCount <=0;终止当前循环。数据已经在此取完了

					break;
				}
				// //////////////
				infos.add(in);
			} while (cur.moveToNext());
		} else {
			// cur.moveToFirst()==false;
		}

	}

	/**
	 * 
	 * @author Administrator 适配器，用于加载联系人列表。
	 */
	class InvitationAdapter extends BaseAdapter {
		Context context;
		List<ContactInfos> lists;
		LayoutInflater inflater;

		public InvitationAdapter(Context context, List<ContactInfos> lists) {
			this.context = context;
			this.lists = lists;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int arg0) {
			return lists.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			view = inflater.inflate(R.layout.item_other_invitation, null);
			TextView tv_name = (TextView) view
					.findViewById(R.id.tv_invitation_name);
			TextView tv_num = (TextView) view
					.findViewById(R.id.tv_invitation_number);

			final String name = lists.get(position).getName();
			final String num = lists.get(position).getNum();

			tv_name.setText(name);
			tv_num.setText(num);

			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {

					new AlertDialog.Builder(Invitation.this)
							.setTitle("提示")
							.setMessage("确定给" + name + "发送推荐信息吗")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											Uri smsToUri = Uri.parse("smsto:"
													+ num);// 联系人地址
											Intent mIntent = new Intent(
													android.content.Intent.ACTION_SENDTO,
													smsToUri);
											mIntent.putExtra("sms_body",
													"您好，我在情缘网注册会员了，这里会员三千万，这么多会员，还怕找不到对象 ？您也快去官网www.07919.com注册吧");// 短信内容
											context.startActivity(mIntent);
										}
									}).setNegativeButton("考虑一下", null).create()
							.show();

				}
			});

			return view;
		}

	}

	/**
	 * 联系人
	 * 
	 * @author Administrator
	 *
	 */
	class ContactInfos {
		private String name;
		private String num;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}
	}
}
