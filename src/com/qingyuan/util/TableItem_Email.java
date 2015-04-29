package com.qingyuan.util;

public class TableItem_Email {
	private String tableNickname;//发送者/接收者  昵称
	private String TableDate;//发送时间
	private String tableTitle;//消息标题
	private String tableId;//邮件id号
	private String tableUid;//用户id 谁发的
	private String tableFuid;//对方的id,在user节点下，是取uid为发送方的id
	private String tablePic="";//照片
	private String tableStatus;//是否已读
	
	
	private String tableNewsNum;//消息数量
	private String tableContent;//消息内容
	
	private String tableLoveStatus;//爱情状态
	private String tableGender;//性别
	private String tableChioceGender;//性别 取反
	private String tableChioceMinAge;//最小年龄
	private String tableChioceMaxAge;//最大年龄
	
	private String tableAge;//年龄
	private String tableBirthyear;//生日
	private String tableProvince;//工作省份
	private String tableCity;//工作城市

	
	
	public String getTableDate() {
		return TableDate;
	}
	public void setTableDate(String tableDate) {
		TableDate = tableDate;
	}

	public String getTableNickname() {
		return tableNickname;
	}
	public void setTableNickname(String tableNickname) {
		this.tableNickname = tableNickname;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getTableUid() {
		return tableUid;
	}
	public void setTableUid(String tableUid) {
		this.tableUid = tableUid;
	}
	public String getTablePic() {
		return tablePic;
	}
	public void setTablePic(String tablePic) {
		this.tablePic = tablePic;
	}
	public String getTableNewsNum() {
		return tableNewsNum;
	}
	public void setTableNewsNum(String tableNewsNum) {
		this.tableNewsNum = tableNewsNum;
	}
	public String getTableContent() {
		return tableContent;
	}
	public void setTableContent(String tableContent) {
		this.tableContent = tableContent;
	}
	public String getTableTitle() {
		return tableTitle;
	}
	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}
	public String getTableLoveStatus() {
		return tableLoveStatus;
	}
	public void setTableLoveStatus(String tableLoveStatus) {
		this.tableLoveStatus = tableLoveStatus;
	}
	public String getTableGender() {
		return tableGender;
	}
	public void setTableGender(String tableGender) {
		this.tableGender = tableGender;
	}
	public String getTableChioceGender() {
		return tableChioceGender;
	}
	public void setTableChioceGender(String tableChioceGender) {
		this.tableChioceGender = tableChioceGender;
	}
	public String getTableChioceMinAge() {
		return tableChioceMinAge;
	}
	public void setTableChioceMinAge(String tableChioceMinAge) {
		this.tableChioceMinAge = tableChioceMinAge;
	}
	public String getTableChioceMaxAge() {
		return tableChioceMaxAge;
	}
	public void setTableChioceMaxAge(String tableChioceMaxAge) {
		this.tableChioceMaxAge = tableChioceMaxAge;
	}
	public String getTableStatus() {
		return tableStatus;
	}
	public void setTableStatus(String tableStatus) {
		this.tableStatus = tableStatus;
	}
	public String getTableAge() {
		return tableAge;
	}
	public void setTableAge(String tableAge) {
		this.tableAge = tableAge;
	}
	
	public String getTableBirthyear() {
		return tableBirthyear;
	}
	public void setTableBirthyear(String tableBirthyear) {
		this.tableBirthyear = tableBirthyear;
	}
	public String getTableProvince() {
		return tableProvince;
	}
	public void setTableProvince(String tableProvince) {
		this.tableProvince = tableProvince;
	}
	public String getTableCity() {
		return tableCity;
	}
	public void setTableCity(String tableCity) {
		this.tableCity = tableCity;
	}
	public String getTableFuid() {
		return tableFuid;
	}
	public void setTableFuid(String tableFuid) {
		this.tableFuid = tableFuid;
	}
}
