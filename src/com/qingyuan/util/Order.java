package com.qingyuan.util;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Order implements Serializable {
	
	public int id;
	public float price;
	public String body;
	public String subject;
	
	public Order(int id,float price,String type, String body){
		this.id=id;
		this.price = price;
		this.subject = type;
		this.body = body;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", price=" + price + ", body=" + body
				+ ", subject=" + subject + "]";
	}
	
}
