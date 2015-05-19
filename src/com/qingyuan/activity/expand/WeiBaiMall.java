package com.qingyuan.activity.expand;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qingyuan.R;
import com.qingyuan.util.pulldown.ProgressWebView;

public class WeiBaiMall extends Activity {
	private ProgressWebView webView;
	private ProgressBar pro;
	private TextView tv;
	private ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.aty_other_weibai_mall);
		webView = (ProgressWebView) findViewById(R.id.web_weibai);
		pro = (ProgressBar) findViewById(R.id.progress_weibai);
		tv = (TextView) findViewById(R.id.tv_weibai);
		iv = (ImageView) findViewById(R.id.iv_weibai);
		/*
		 * CustomProgressDialog.createDialog(WeiBaiMall.this, "加载中。。。。", 2000)
		 * .show();
		 */

		webView.getSettings().setJavaScriptEnabled(true);
		final Activity activity = this;
		webView.loadUrl("http://www.3v100.com");
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				activity.setProgress(progress * 1000);
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {

				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				pro.setVisibility(View.GONE);
				tv.setVisibility(View.GONE);
				iv.setVisibility(View.GONE);
				webView.setVisibility(View.VISIBLE);

				super.onPageFinished(view, url);
			}
		});

	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			// 如果此前没有按back键，不退出
			if (System.currentTimeMillis() - exitTime > 1000) {

				exitTime = System.currentTimeMillis();
				webView.goBack();
				return true;
			} else {
				/*
				 * //退出程序的方法 finish(); System.exit(0);
				 */
				new AlertDialog.Builder(WeiBaiMall.this)
						.setTitle("提示")
						.setMessage("确定要退出商城吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										finish();

									}
								}).setNegativeButton("再逛逛", null).create()
						.show();

			}
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

}
