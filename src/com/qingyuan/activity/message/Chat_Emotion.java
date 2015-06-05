package com.qingyuan.activity.message;

import java.util.LinkedHashMap;
import java.util.Map;

import com.qingyuan.R;
/**
 *  加载表情的类，静态方法 getFaceMap  返回map列表。
 * @author Administrator
 *
 */
public class Chat_Emotion {

	// ======头像===

	public static final int NUM_PAGE = 6;// 总共有多少页
	public static int NUM = 20;// 每页20个表情,还有最后一个删除button
	private static Map<String, Integer> mFaceMap = new LinkedHashMap<String, Integer>();

	public static Map<String, Integer> getFaceMap() {
		initFaceMap();
		return mFaceMap;

	}

	private static void initFaceMap() {
		mFaceMap.put("qqface/0.gif", R.drawable.f0);
		mFaceMap.put("qqface/1.gif", R.drawable.f1);
		mFaceMap.put("qqface/2.gif", R.drawable.f2);
		mFaceMap.put("qqface/3.gif", R.drawable.f3);
		mFaceMap.put("qqface/4.gif", R.drawable.f4);
		mFaceMap.put("qqface/5.gif", R.drawable.f5);
		mFaceMap.put("qqface/6.gif", R.drawable.f6);
		mFaceMap.put("qqface/7.gif", R.drawable.f7);
		mFaceMap.put("qqface/8.gif", R.drawable.f8);
		mFaceMap.put("qqface/9.gif", R.drawable.f9);
		mFaceMap.put("qqface/10.gif", R.drawable.f10);
		mFaceMap.put("qqface/11.gif", R.drawable.f11);
		mFaceMap.put("qqface/12.gif", R.drawable.f12);
		mFaceMap.put("qqface/13.gif", R.drawable.f13);
		mFaceMap.put("qqface/14.gif", R.drawable.f14);
		mFaceMap.put("qqface/15.gif", R.drawable.f15);
		mFaceMap.put("qqface/16.gif", R.drawable.f16);
		mFaceMap.put("qqface/17.gif", R.drawable.f17);
		mFaceMap.put("qqface/18.gif", R.drawable.f18);
		mFaceMap.put("qqface/19.gif", R.drawable.f19);
		mFaceMap.put("qqface/20.gif", R.drawable.f20);
		mFaceMap.put("qqface/21.gif", R.drawable.f21);

		mFaceMap.put("qqface/22.gif", R.drawable.f22);
		mFaceMap.put("qqface/23.gif", R.drawable.f23);
		mFaceMap.put("qqface/24.gif", R.drawable.f24);
		mFaceMap.put("qqface/25.gif", R.drawable.f25);
		mFaceMap.put("qqface/26.gif", R.drawable.f26);
		mFaceMap.put("qqface/27.gif", R.drawable.f27);
		mFaceMap.put("qqface/28.gif", R.drawable.f28);
		mFaceMap.put("qqface/29.gif", R.drawable.f29);
		mFaceMap.put("qqface/30.gif", R.drawable.f30);
		mFaceMap.put("qqface/31.gif", R.drawable.f31);
		mFaceMap.put("qqface/32.gif", R.drawable.f32);
		mFaceMap.put("qqface/33.gif", R.drawable.f33);
		mFaceMap.put("qqface/34.gif", R.drawable.f34);
		mFaceMap.put("qqface/35.gif", R.drawable.f35);
		mFaceMap.put("qqface/36.gif", R.drawable.f36);
		mFaceMap.put("qqface/37.gif", R.drawable.f37);
		mFaceMap.put("qqface/38.gif", R.drawable.f38);
		mFaceMap.put("qqface/39.gif", R.drawable.f39);
		mFaceMap.put("qqface/40.gif", R.drawable.f40);
		mFaceMap.put("qqface/41.gif", R.drawable.f41);
		mFaceMap.put("qqface/42.gif", R.drawable.f42);

		mFaceMap.put("qqface/43.gif", R.drawable.f43);
		mFaceMap.put("qqface/44.gif", R.drawable.f44);
		mFaceMap.put("qqface/45.gif", R.drawable.f45);
		mFaceMap.put("qqface/46.gif", R.drawable.f46);
		mFaceMap.put("qqface/47.gif", R.drawable.f47);
		mFaceMap.put("qqface/48.gif", R.drawable.f48);
		mFaceMap.put("qqface/49.gif", R.drawable.f49);
		mFaceMap.put("qqface/50.gif", R.drawable.f50);
		mFaceMap.put("qqface/51.gif", R.drawable.f51);
		mFaceMap.put("qqface/52.gif", R.drawable.f52);
		mFaceMap.put("qqface/53.gif", R.drawable.f53);
		mFaceMap.put("qqface/54.gif", R.drawable.f54);
		mFaceMap.put("qqface/55.gif", R.drawable.f55);
		mFaceMap.put("qqface/56.gif", R.drawable.f56);
		mFaceMap.put("qqface/57.gif", R.drawable.f57);
		mFaceMap.put("qqface/58.gif", R.drawable.f58);
		mFaceMap.put("qqface/59.gif", R.drawable.f59);
		mFaceMap.put("qqface/60.gif", R.drawable.f60);
		mFaceMap.put("qqface/61.gif", R.drawable.f61);
		mFaceMap.put("qqface/62.gif", R.drawable.f62);
		mFaceMap.put("qqface/63.gif", R.drawable.f63);

		mFaceMap.put("qqface/64.gif", R.drawable.f64);
		mFaceMap.put("qqface/65.gif", R.drawable.f65);
		mFaceMap.put("qqface/66.gif", R.drawable.f66);
		mFaceMap.put("qqface/67.gif", R.drawable.f67);
		mFaceMap.put("qqface/68.gif", R.drawable.f68);
		mFaceMap.put("qqface/69.gif", R.drawable.f69);
		mFaceMap.put("qqface/70.gif", R.drawable.f70);
		mFaceMap.put("qqface/71.gif", R.drawable.f71);
		mFaceMap.put("qqface/72.gif", R.drawable.f72);
		mFaceMap.put("qqface/73.gif", R.drawable.f73);
		mFaceMap.put("qqface/74.gif", R.drawable.f74);
		mFaceMap.put("qqface/75.gif", R.drawable.f75);
		mFaceMap.put("qqface/76.gif", R.drawable.f76);
		mFaceMap.put("qqface/77.gif", R.drawable.f77);
		mFaceMap.put("qqface/78.gif", R.drawable.f78);
		mFaceMap.put("qqface/79.gif", R.drawable.f79);
		mFaceMap.put("qqface/80.gif", R.drawable.f80);
		mFaceMap.put("qqface/81.gif", R.drawable.f81);
		mFaceMap.put("qqface/82.gif", R.drawable.f82);
		mFaceMap.put("qqface/83.gif", R.drawable.f83);
		mFaceMap.put("qqface/84.gif", R.drawable.f84);

		mFaceMap.put("qqface/85.gif", R.drawable.f85);
		mFaceMap.put("qqface/86.gif", R.drawable.f86);
		mFaceMap.put("qqface/87.gif", R.drawable.f87);
		mFaceMap.put("qqface/88.gif", R.drawable.f88);
		mFaceMap.put("qqface/89.gif", R.drawable.f89);
		mFaceMap.put("qqface/90.gif", R.drawable.f90);
		mFaceMap.put("qqface/91.gif", R.drawable.f91);
		mFaceMap.put("qqface/92.gif", R.drawable.f92);
		mFaceMap.put("qqface/93.gif", R.drawable.f93);
		mFaceMap.put("qqface/94.gif", R.drawable.f94);
		mFaceMap.put("qqface/95.gif", R.drawable.f95);
		mFaceMap.put("qqface/96.gif", R.drawable.f96);
		mFaceMap.put("qqface/97.gif", R.drawable.f97);
		mFaceMap.put("qqface/98.gif", R.drawable.f98);
		mFaceMap.put("qqface/99.gif", R.drawable.f99);
		mFaceMap.put("qqface/100.gif", R.drawable.f100);
		mFaceMap.put("qqface/101.gif", R.drawable.f101);
		mFaceMap.put("qqface/102.gif", R.drawable.f102);
		mFaceMap.put("qqface/103.gif", R.drawable.f103);
		mFaceMap.put("qqface/104.gif", R.drawable.f104);
		mFaceMap.put("qqface/105.gif", R.drawable.f105);

		mFaceMap.put("qqface/106.gif", R.drawable.f106);
		mFaceMap.put("qqface/107.gif", R.drawable.f107);
		mFaceMap.put("qqface/108.gif", R.drawable.f108);
		mFaceMap.put("qqface/109.gif", R.drawable.f109);
		mFaceMap.put("qqface/110.gif", R.drawable.f110);
		mFaceMap.put("qqface/111.gif", R.drawable.f111);
		mFaceMap.put("qqface/112.gif", R.drawable.f112);
		mFaceMap.put("qqface/113.gif", R.drawable.f113);
		mFaceMap.put("qqface/114.gif", R.drawable.f114);
		mFaceMap.put("qqface/115.gif", R.drawable.f115);
		mFaceMap.put("qqface/116.gif", R.drawable.f116);
		mFaceMap.put("qqface/117.gif", R.drawable.f117);
		mFaceMap.put("qqface/118.gif", R.drawable.f118);
		mFaceMap.put("qqface/119.gif", R.drawable.f119);
		mFaceMap.put("qqface/120.gif", R.drawable.f120);
		mFaceMap.put("qqface/121.gif", R.drawable.f121);
		mFaceMap.put("qqface/122.gif", R.drawable.f122);
		mFaceMap.put("qqface/123.gif", R.drawable.f123);
		mFaceMap.put("qqface/124.gif", R.drawable.f124);
		mFaceMap.put("qqface/125.gif", R.drawable.f125);
		mFaceMap.put("qqface/126.gif", R.drawable.f126);
		mFaceMap.put("qqface/127.gif", R.drawable.f127);
		mFaceMap.put("qqface/128.gif", R.drawable.f128);
		mFaceMap.put("qqface/129.gif", R.drawable.f129);
		mFaceMap.put("qqface/130.gif", R.drawable.f130);
		mFaceMap.put("qqface/131.gif", R.drawable.f131);
		mFaceMap.put("qqface/132.gif", R.drawable.f132);
		mFaceMap.put("qqface/133.gif", R.drawable.f133);
		mFaceMap.put("qqface/134.gif", R.drawable.f134);
		mFaceMap.put("face/1.gif", R.drawable.f135);
		mFaceMap.put("face/2.gif", R.drawable.f136);
		mFaceMap.put("face/3.gif", R.drawable.f137);
		mFaceMap.put("face/4.gif", R.drawable.f138);
		mFaceMap.put("face/5.gif", R.drawable.f139);
		mFaceMap.put("face/6.gif", R.drawable.f140);
		mFaceMap.put("face/7.gif", R.drawable.f141);
		mFaceMap.put("face/8.gif", R.drawable.f142);
		mFaceMap.put("face/9.gif", R.drawable.f143);
		mFaceMap.put("face/10.gif", R.drawable.f144);
		mFaceMap.put("face/11.gif", R.drawable.f145);
		mFaceMap.put("face/12.gif", R.drawable.f146);
		mFaceMap.put("face/13.gif", R.drawable.f147);
		mFaceMap.put("face/14.gif", R.drawable.f148);
		mFaceMap.put("face/15.gif", R.drawable.f149);
		mFaceMap.put("face/16.gif", R.drawable.f150);
		mFaceMap.put("face/17.gif", R.drawable.f151);
		mFaceMap.put("face/18.gif", R.drawable.f152);
		mFaceMap.put("face/19.gif", R.drawable.f153);
		mFaceMap.put("face/20.gif", R.drawable.f154);
		mFaceMap.put("face/21.gif", R.drawable.f155);
		mFaceMap.put("face/22.gif", R.drawable.f156);
		mFaceMap.put("face/23.gif", R.drawable.f157);
		mFaceMap.put("face/24.gif", R.drawable.f158);
		mFaceMap.put("face/25.gif", R.drawable.f159);
		mFaceMap.put("face/26.gif", R.drawable.f160);
		mFaceMap.put("face/27.gif", R.drawable.f161);
		mFaceMap.put("face/28.gif", R.drawable.f162);
		mFaceMap.put("face/29.gif", R.drawable.f163);
		mFaceMap.put("face/30.gif", R.drawable.f164);

	}

}
