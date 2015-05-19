package com.qingyuan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.qingyuan.activity.expand.Invitation;
import com.qingyuan.activity.expand.QingyuanMall;
import com.qingyuan.activity.expand.WeiBaiMall;
import com.qingyuan.util.CustomProgressDialog;

public class TabOthersActivity extends Activity implements View.OnClickListener {
	Button btn_Mall, btn_WeiBai, btn_invitation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_tabothers);

		btn_Mall = (Button) findViewById(R.id.btn_tabothers_mall);
		btn_WeiBai = (Button) findViewById(R.id.btn_tabothers_weibai);
		btn_invitation = (Button) findViewById(R.id.btn_tabothers_invitation);

		btn_Mall.setOnClickListener(this);
		btn_WeiBai.setOnClickListener(this);
		btn_invitation.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_tabothers_mall:

			Intent i = new Intent(TabOthersActivity.this, QingyuanMall.class);
			CustomProgressDialog.createDialog(TabOthersActivity.this,
					"正在加载情缘商城。。。。", 2000).show();
			startActivity(i);
			break;
		case R.id.btn_tabothers_weibai:
			Intent i1 = new Intent(TabOthersActivity.this, WeiBaiMall.class);
			CustomProgressDialog.createDialog(TabOthersActivity.this,
					"正在进入网上商城。。。。", 2000).show();
			startActivity(i1);

			break;
		case R.id.btn_tabothers_invitation:
			Intent i2 = new Intent(TabOthersActivity.this, Invitation.class);
			CustomProgressDialog.createDialog(TabOthersActivity.this,
					"列表获取中。。。。", 2000).show();
			startActivity(i2);

			break;

		default:
			break;
		}
	}
}
