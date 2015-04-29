package com.qingyuan.util;

import java.text.SimpleDateFormat;
/**
 * get set 值，  tableNickname;
	 tableAge;
	 tableBirthyear;
	 tableId;
	 tablePic;
	 tableProvince;
	 tableCity;
	 tableHeight;
	 tableEducation;
	 tableLoveStatus;
	 tableGender;
	 tableChioceGender;
	 tableChioceMinAge;
	 tableChioceMaxAge;
	 tableIntroduce;
 * @author no_2
 *
 */
public class TableItem_Search {

	private String tableNickname;
	private String tableAge;
	private String tableBirthyear;
	private String tableId;
	private String tablePic = "";
	private String tableProvince;
	private String tableCity;
	private String tableHeight;
	private String tableEducation;
	private String tableLoveStatus;
	private String tableGender;
	private String tableChioceGender;
	private String tableChioceMinAge;
	private String tableChioceMaxAge;
	private String tableIntroduce;

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTableNickname() {
		return tableNickname;
	}

	public void setTableNickname(String tableNickname) {
		this.tableNickname = tableNickname;
	}

	public String getTableAge() {
		if (tableBirthyear != null && !tableBirthyear.equals("0")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String date = sdf.format(new java.util.Date());
			tableAge = (Integer.parseInt(date) - Integer
					.parseInt(tableBirthyear)) + "";
		} else {
			tableAge = "未知";
		}
		return tableAge;
	}

	public void setTableBirthyear(String tableBirthyear) {
		this.tableBirthyear = tableBirthyear;
	}

	public String getTablePic() {
		return tablePic;
	}

	public void setTablePic(String tablePic) {
		this.tablePic = tablePic;
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

	public String getTableHeight() {
		return tableHeight;
	}

	public void setTableHeight(String tableHeight) {
		this.tableHeight = tableHeight;
	}

	public String getTableEducation() {
		return tableEducation;
	}

	public void setTableEducation(String tableEducation) {
		this.tableEducation = tableEducation;
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

	public String getTableLoveStatus() {
		return tableLoveStatus;
	}

	public void setTableLoveStatus(String tableLoveStatus) {
		this.tableLoveStatus = tableLoveStatus;
	}

	@Override
	public String toString() {
		return "TableItem_Search [tableNickname=" + tableNickname
				+ ", tableAge=" + tableAge + ", tableBirthyear="
				+ tableBirthyear + ", tableId=" + tableId + ", tablePic="
				+ tablePic + ", tableProvince=" + tableProvince
				+ ", tableCity=" + tableCity + ", tableHeight=" + tableHeight
				+ ", tableEducation=" + tableEducation + ", tableLoveStatus="
				+ tableLoveStatus + ", tableGender=" + tableGender
				+ ", tableChioceGender=" + tableChioceGender
				+ ", tableChioceMinAge=" + tableChioceMinAge
				+ ", tableChioceMaxAge=" + tableChioceMaxAge + "]";
	}

	public String getTableIntroduce() {
		return tableIntroduce;
	}

	public void setTableIntroduce(String tableIntroduce) {
		this.tableIntroduce = tableIntroduce;
	}

}
