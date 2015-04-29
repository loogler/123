package com.qingyuan.util;

import android.content.Context;
/**
 * 
 * @author no_2
 * @Notes 访问记录的geter/seter。
 * 
 *
 */
public class TableItem_Visit {
	public TableItem_Visit() {
		super();
	}

	private String vid;// 本次访问记录id
	private String uid;// 互动会员的id号 ，非本人的。
	private String visitorid;// 自己的id
	private String time;// 时间戳
	private String user_pic;// 用户照片
	private String user_nick;// 用户昵称
	private String user_Info;//对方信息

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getVisitorid() {
		return visitorid;
	}

	public void setVisitorid(String visitorid) {
		this.visitorid = visitorid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}

	public String getUser_nick() {
		return user_nick;
	}

	public void setUser_nick(String user_nick) {
		this.user_nick = user_nick;
	}

	public String getUser_info() {
		return user_Info;
	}

	public void setUser_info(String user_info) {
		this.user_Info = user_info;
	}

}
