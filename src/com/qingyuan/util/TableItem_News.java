package com.qingyuan.util;

/**
 * @author no_2 get set tableDate; tableNickname; tableId; tablePic; tableNewsNum;
 * 消息解析栏目，
 *
 */
public class TableItem_News {

	private String tableNickname;
	private String tableMessageId;
	private String tableMessage;

	private String tableUserId;
	private String tablePic = "";
	private String tableNewsNum;
	private String tableDate;

	public String getTableMessage() {
		return tableMessage;
	}
	
	public void setTableMessage(String tableMessage) {
		this.tableMessage = tableMessage;
	}
	public String getTableMessageId() {
		return tableMessageId;
	}

	public void setTableMessageId(String tablemessageId) {
		this.tableMessageId = tablemessageId;
	}

	public String getTableUserId() {
		return tableUserId;
	}

	public void setTableUserId(String tableUserId) {
		this.tableUserId = tableUserId;
	}

	public String getTableDate() {
		return tableDate;
	}

	public void setTableDate(String tableDate) {
		this.tableDate = tableDate;
	}

	

	public String getTableNewsNum() {
		return tableNewsNum;
	}

	public void setTableNewsNum(String tableNewsNum) {
		this.tableNewsNum = tableNewsNum;
	}

	public String getTableNickname() {
		return tableNickname;
	}

	public void setTableNickname(String tableNickname) {
		this.tableNickname = tableNickname;
	}

	

	public String getTablePic() {
		return tablePic;
	}

	public void setTablePic(String tablePic) {
		this.tablePic = tablePic;
	}

}
