package com.qingyuan.service.parser;


import java.util.ArrayList;
import java.util.List;

import com.qingyuan.util.MyArray;

import android.util.Log;

/**
 * 我的类库
 * @author Administrator
 *
 */
public class MyUtil {
	
	/**
	 * 数组匹配取key/val
	 * @param str 传入的json值 ，通过json从yoghurt中心取到的
	 * @param arrs  数组资源
	 * @param type  type =1或者 ！=1；  等于1时  取“，”前面的，！=1 时 取“，”后面的
	 * @return  取到的数组中指定的值。
	 */
	public static String getArrString(String str,String[] arrs,int type){
		String val = type == 1 ? "不限":"0" ;
		if(str != null){
			for (int i = 0; i < arrs.length; i++) {
				if(type == 1){	// 取中文val
					if(str.equals(arrs[i].substring(arrs[i].indexOf(",")+1))){
						val = arrs[i].substring(0, arrs[i].indexOf(","));
						break;
					}
				}else {	//取数字key
					if(str.equals(arrs[i].substring(0,arrs[i].indexOf(",")))){
						val = arrs[i].substring(arrs[i].indexOf(",")+1);
						break;
					}
				}
			}
		}
		return val;
	}
	
	/**
	 * 取数组
	 * @param arrs 插入的数组
	 * @param type if =1 （arrs,"key,value"）取“,”之前的内容，//// ！=1  取 “key,value”后面的内容
	 *   
	 * @return  根据参数type界定后得到的数组 （键/值集合）
	 */
	public static String[] getArr(String[] arrs, int type){
		String[] val = new String[arrs.length];
		for (int i = 0; i < arrs.length; i++) {
			if(type == 1){
				val[i] = arrs[i].substring(0, arrs[i].indexOf(","));
			}else{
				val[i] = arrs[i].substring(arrs[i].indexOf(",")+1);
			}
		}
		return val;
	}
	/**
	 * 
	 * @param arrs
	 * @return 
	 */
	public static List<MyArray> getArrays(String[] arrs){
		List<MyArray> val = new ArrayList<MyArray>();
		for (int i = 0; i < arrs.length; i++) {
			MyArray ma = new MyArray(arrs[i].substring(0, arrs[i].indexOf(",")), arrs[i].substring(arrs[i].indexOf(",")+1));
			val.add(ma);
		}
		return val;
	}
	
	/**
	 * 取索引
	 * @param str
	 * @param arrs
	 * @return
	 */
	public static int getIndexOfArr(String str, String[] arrs){
		int index = 0;
		if(str!=null){
			for (int i = 0; i < arrs.length; i++) {
				if(str.equals(arrs[i])){
					index = i;break;
				}
			}
		}
		return index;
	}
	
	/**
	 * 城市
	 * @param arr
	 * @param province
	 * @return
	 */
	public static String[] getCityArr(String[] arr,String province){
		String[] val = null;
		Log.d("province", province);
		if(province != null && !province.equals("10102000") && !province.equals("10103000") && !province.equals("10101201") && !province.equals("10101002") && !province.equals("10104000") && !province.equals("10105000") && !province.equals("10133000") && !province.equals("10132000") && !province.equals("10134000") && !province.equals("0")){
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < arr.length; i++) {
				if(!arr[i].substring(arr[i].indexOf(",")+1).equals("0")){
					String j = province.substring(0, 5);
					String k = arr[i].substring(arr[i].indexOf(",")+1).substring(0, 5);
					if(j.equals(k)){
						list.add(arr[i].substring(0, arr[i].indexOf(",")));
					}
				}
			}
			val = (String[]) list.toArray(new String[list.size()]);
		}
		return val;
	}
	
	public static String[] getFullArr(String[] arr, String str){
		if(arr == null) return null;
		String[] val = new String[arr.length+1];
		for (int i = 0; i < val.length; i++) {
			if(i==0){
				val[i] = str;
			}else{
				val[i] = arr[i-1];
			}			
		}
		return val;
	}
	
	public static String getPayType(String channel){
		String result="";
		if(channel.contains("钻石会员")){
			result = "pay_diamond";
		}else if (channel.contains("城市之星")) {
			result = "city_star";
		}else if (channel.contains("鲜花赠送")) {
			result = "flower";
		}else if (channel.contains("钱包充值")) {
			result = "recharge";
		}else if (channel.contains("积分购买")) {
			result = "points";
		}else {
			result = "pay";
		}
		return result;
	}
}

