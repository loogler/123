package com.qingyuan;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.qingyuan.alipay.Keys;
import com.qingyuan.alipay.Result;
import com.qingyuan.alipay.Rsa;
import com.qingyuan.service.parser.MyUtil;
import com.qingyuan.util.HttpUtil;
import com.qingyuan.util.Order;

public class PaymentActivity extends Activity {

	private static final int RQF_PAY = 1;
	private static final int RQF_LOGIN = 2;
	RadioButton advance, diamonds, stars;
	RadioGroup payment;
	private String channel;
	private String orderNo;
	private String price;
	private EditText moneyText;
	private Order order;
	public static String home_uid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paymentactivity);
		// 读取用户数据
		SharedPreferences preferences = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		home_uid = preferences.getString("uid", "0");
		// 参数传递

		Intent intent = getIntent();
		order = (Order) intent.getSerializableExtra("order");
		// EditText subjectText = (EditText) findViewById(R.id.moneyText);
		moneyText = (EditText) findViewById(R.id.moneyText);

		moneyText.setText("【" + order.subject + "】");
		channel = MyUtil.getPayType(order.subject);
		if (order.price > 0) {
			moneyText.setText(String.valueOf(order.price));
		}
		channel = MyUtil.getPayType(order.subject);

		Button payButton = (Button) findViewById(R.id.payButton);
		payButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				PaymentActivity.this.runOnUiThread(new Runnable() {

					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						price = moneyText.getText().toString().trim();
						Map<String, String> param = new HashMap<String, String>();
						param.put("channel", channel);
						param.put("total_free", price);
						try {
							String res = HttpUtil.postRequest(HttpUtil.BASE_URL
									+ "&f=payment&toType=json&android_uid="
									+ home_uid, param);
							JSONObject json = new JSONObject(res);
							int code = json.getInt("code");
							if (code == 1) {
								orderNo = json.getJSONObject("result")
										.getString("out_trade_no");
								String info = getNewOrderInfo();
								String sign = Rsa.sign(info, Keys.PRIVATE);
								sign = URLEncoder.encode(sign);
								info += "&sign=\"" + sign + "\"&"
										+ getSignType();
								Log.i("ExternalPartner", "start pay");
								// start the pay.
								Log.i("alipay", "info = " + info);
								final String orderInfo = info;
								new Thread(new Runnable() {

									@Override
									public void run() {
										AliPay alipay = new AliPay(
												PaymentActivity.this, mHandler);
										// alipay.setSandBox(true); //沙箱模式
										String result = alipay.pay(orderInfo);
										Log.i("alipay", "result = " + result);
										Message msg = new Message();
										msg.what = RQF_PAY;
										msg.obj = result;
										mHandler.sendMessage(msg);
									}
								}).start();

							} else if (code == 10000) {
								Toast.makeText(PaymentActivity.this,
										json.getString("message"),
										Toast.LENGTH_LONG);
							} else {
								Toast.makeText(PaymentActivity.this,
										"服务器异常，请稍后再试！", Toast.LENGTH_SHORT)
										.show();
							}
						} catch (Exception e) {
							Toast.makeText(PaymentActivity.this,
									"服务器异常，请稍后再试！", Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	@SuppressWarnings("deprecation")
	private String getNewOrderInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(orderNo);
		sb.append("\"&subject=\"");
		sb.append(order.subject);
		if (!TextUtils.isEmpty(order.body)) {
			sb.append("\"&body=\"");
			sb.append("");
		}
		sb.append("\"&total_fee=\"");
		sb.append(price);
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode(HttpUtil.URL
				+ "/module/payment/androidforalipay/notify_url.php"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		// sb.append("\"&return_url=\"");
		// sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");
		return new String(sb);
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);

			switch (msg.what) {
			case RQF_PAY:
			case RQF_LOGIN: {
				Toast.makeText(PaymentActivity.this, result.getResult(),
						Toast.LENGTH_SHORT).show();
				new AlertDialog.Builder(PaymentActivity.this)
						.setTitle("提示")
						.setMessage("是否支付成功？")
						.setPositiveButton("支付成功",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										PaymentActivity.this.finish();
									}
								})
						.setNegativeButton("支付失败",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										Toast.makeText(PaymentActivity.this,
												"支付失败，请重新支付",
												Toast.LENGTH_SHORT).show();
										PaymentActivity.this.finish();
									}
								}).create().show();
			}
				break;
			default:
				break;
			}
		};
	};
}
