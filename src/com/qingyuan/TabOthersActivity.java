package com.qingyuan;

import com.qingyuan.activity.expand.QingyuanMall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TabOthersActivity  extends Activity implements View.OnClickListener{
	Button btn_Mall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_tabothers);
		btn_Mall=(Button) findViewById(R.id.btn_tabothers_mall);
		btn_Mall.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch ( v.getId()) {
		case R.id.btn_tabothers_mall:
			
			Intent i=new  Intent(TabOthersActivity.this,QingyuanMall.class);
			startActivity(i);
			break;

		default:
			break;
		}
	}
}
