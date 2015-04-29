package com.qingyuan.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;

/** 
 * @serial formatUserFromPreferences
 * 用户基类 */
@SuppressLint("SimpleDateFormat")
public class User {
	private int uid;
	private String nickName;
	private int gender;
	private int province;
	private int city;
	private String telphone;
	private String img; // 头像
	private String loveStatus;
	private int birthyear;
	private int cityStart;
	private int s_cid;
	// 择偶
	private int choiceGender;
	private int choiceMinAge = 0;
	private int choiceMaxAge = 0;

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getUid() {
		return this.uid;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getGender() {
		return this.gender;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getProvince() {
		return this.province;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getCity() {
		return this.city;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getTelphone() {
		return this.telphone;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getImg() {
		return this.img;
	}

	public void setLoveStatus(String loveStatus) {
		this.loveStatus = loveStatus;
	}

	public String getLoveStatus() {
		return this.loveStatus;
	}

	public void setBirthYear(int birthyear) {
		this.birthyear = birthyear;
	}

	public int getBirthYear() {
		return birthyear;
	}

	public void setCityStart(int cityStart) {
		this.cityStart = cityStart;
	}

	public int getCityStart() {
		return cityStart;
	}

	public void setScid(int s_cid) {
		this.s_cid = s_cid;
	}

	public int getScid() {
		return this.s_cid;
	}

	public String getAge() {
		String age = "未知";
		if (birthyear > 0) {
			age = String.valueOf(Integer.parseInt(new SimpleDateFormat("yyyy")
					.format(new Date())) - birthyear);
		}
		return age;
	}

	public void setChoiceGender(int choiceGender) {
		this.choiceGender = choiceGender;
	}

	public int getChoiceGender() {
		return this.choiceGender;
	}

	public void setChoiceMinAge(int choiceMinAge) {
		this.choiceMinAge = choiceMinAge;
	}

	public int getChoiceMinAge() {
		return this.choiceMinAge;
	}

	public void setChoiceMaxAge(int choiceMaxAge) {
		this.choiceMaxAge = choiceMaxAge;
	}

	public int getChoiceMaxAge() {
		return this.choiceMaxAge;
	}

	/**
	 * 格式化用户数据
	 * 
	 * @param preferences
	 * @return
	 */
	public void formatUserFromPreferences(SharedPreferences preferences) {
		this.uid = Integer.parseInt(preferences.getString("uid", "0"));
		this.nickName = preferences.getString("nickname", "");
		this.s_cid = Integer.parseInt(preferences.getString("cid", "0"));
		this.telphone = preferences.getString("telphone", "0");
		this.gender = Integer.parseInt(preferences.getString("gender", "0"));
		this.cityStart = Integer.parseInt(preferences.getString("star", "0"));
		this.img = preferences.getString("pic", "");
		this.province = Integer
				.parseInt(preferences.getString("province", "0"));
		this.city = Integer.parseInt(preferences.getString("city", "0"));
	}
}
