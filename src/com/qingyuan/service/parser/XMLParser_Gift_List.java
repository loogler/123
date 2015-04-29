package com.qingyuan.service.parser;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import com.qingyuan.util.HttpUtil;
import com.qingyuan.util.TableItem_Gift_List;

public class XMLParser_Gift_List 
{
	
	public XMLParser_Gift_List() {}

	//网络下载xml
	public String getXmlFromUrl_gift_list(String url) {
		String xml = null;
		try {
			xml = HttpUtil.getRequest(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xml;
	}
	
		public List<TableItem_Gift_List> parseTableItem(String is) throws Exception {
			JSONObject json = new JSONObject(is);
			List<TableItem_Gift_List> items = null;
			if(json.getInt("code")==1){
				TableItem_Gift_List item = null;
				JSONArray array = json.getJSONObject("result").getJSONArray("list");
				items = new ArrayList<TableItem_Gift_List>();
				for (int i = 0; i < array.length(); i++) {
					item = new TableItem_Gift_List();
					item.setTableId(array.optJSONObject(i).getString("id"));
					item.setTableGiftName(array.optJSONObject(i).getString("name"));
					if(array.optJSONObject(i).has("num"))
						item.setNum(array.optJSONObject(i).getInt("num"));
					item.setTablePrice(array.optJSONObject(i).getString("price"));
					item.setTableSrc(array.optJSONObject(i).getString("src"));
					items.add(item);item = null;
				}
			}
			return items;
		}//public List<TableItem_News> parseTableItem(InputStream is) throws Exception {括号的另一边

}

