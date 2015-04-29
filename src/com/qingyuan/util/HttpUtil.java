package com.qingyuan.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

public class HttpUtil {
	public static String PHPSESSID = null;

	// 创建HttpClient对象
	public static HttpClient httpClient = new DefaultHttpClient();
	public static final String URL = "http://www.07919.com";
	public static final String BASE_URL = "http://www.07919.com/index.php?n=index&h=api&mooApi=android&mooCode=07919";

	/**
	 *
	 * @param url
	 *            发送请求的URL
	 * @return 服务器响应字符串
	 * @throws Exception
	 */
	public static String getRequest(final String url) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						synchronized (httpClient) {
							String url2 = url;
							if (PHPSESSID != null) {
								url2 += "&PHPSESSID=" + PHPSESSID;
							}

							// 创建HttpGet对象。
							HttpGet get = new HttpGet(url2);

							// 发送GET请求
							HttpResponse httpResponse = httpClient.execute(get);
							// 如果服务器成功地返回响应
							if (httpResponse.getStatusLine().getStatusCode() == 200) {
								// 获取服务器响应字符串
								String result = EntityUtils
										.toString(httpResponse.getEntity());
								if (PHPSESSID == null) {
									JSONObject res = new JSONObject(result);
									PHPSESSID = res.getString("PHPSESSID");
								}
								Log.d("url", url2);
								Log.d("result", result);
								return result;
							}
							return null;
						}
					}
				});
		new Thread(task).start();
		return task.get();
	}

	/**
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数
	 * @return 服务器响应字符串
	 * @throws Exception
	 */
	public static String postRequest(final String url,
			final Map<String, String> rawParams) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						String url2 = url;
						if (PHPSESSID != null) {
							url2 += "&PHPSESSID=" + PHPSESSID;
						}
						// 创建HttpPost对象。
						HttpPost post = new HttpPost(url2);
						// 如果传递参数个数比较多的话可以对传递的参数进行封装
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						for (String key : rawParams.keySet()) {
							// 封装请求参数
							params.add(new BasicNameValuePair(key, rawParams
									.get(key)));
						}
						// 设置请求参数
						post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
						// 发送POST请求
						HttpResponse httpResponse = httpClient.execute(post);
						// 如果服务器成功地返回响应
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							// 获取服务器响应字符串
							String result = EntityUtils.toString(httpResponse
									.getEntity());
							if (PHPSESSID == null) {
								JSONObject res = new JSONObject(result);
								PHPSESSID = res.getString("PHPSESSID");
							}
							Log.d("url", url2);
							Log.d("param", rawParams.toString());
							Log.d("result", result);
							return result;
						}
						return null;
					}
				});
		new Thread(task).start();
		return task.get();
	}
}
