package com.qingyuan.util;


public class TableItem_Gift_List 
{
	
	private String tableId;
	private String tableGiftName;
	private String tablePrice;
	private String tableSrc="";
	private int num = 0;
	
	
	
	public String getTableId()
	{
		return tableId;
	}
	public void setTableId(String tableId)
	{
		this.tableId=tableId;
	}
	
	public String getTableGiftName()
	{
		return tableGiftName;
	}
	public void setTableGiftName(String tableGiftName)
	{
		this.tableGiftName=tableGiftName;
	}
	
	public String getTablePrice()
	{
		return tablePrice;
	}
	public void setTablePrice(String tablePrice)
	{
		this.tablePrice=tablePrice;
	}
	
	public String getTableSrc()
	{
		return tableSrc;
	}
	public void setTableSrc(String tableSrc)
	{
		this.tableSrc=tableSrc;
	}
	
	public int getNum(){
		return this.num;
	}
	public void setNum(int num){
		this.num = num;
	}
}

