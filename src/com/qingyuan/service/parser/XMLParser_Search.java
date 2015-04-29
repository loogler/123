package com.qingyuan.service.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.qingyuan.util.HttpUtil;
import com.qingyuan.util.TableItem_Search;

import android.util.Log;
import android.util.Xml;

public class XMLParser_Search {
	public XMLParser_Search() {

	}


	//网络下载xml
	public String getXmlFromUrl(String url) {
		String xml = null;
		try {
			xml = HttpUtil.getRequest(url);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Log.e("访问错误", "服务器访问错误");
			e1.printStackTrace();
		}
		return xml;
	}
	
	
	/**
	* @param is获取资讯栏目列表
	* @return items资讯栏目信息数据
	* @throws Exception*/

	public List<TableItem_Search> parseTableItem(InputStream is) throws Exception {
		List<TableItem_Search> items = new ArrayList<TableItem_Search>();
		try {
			TableItem_Search item = null;

			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");

			
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				for(int i=0;i<100;i++)
				{
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						items = new ArrayList<TableItem_Search>();
						break;
					case XmlPullParser.START_TAG:
						
						if (parser.getName().equals("nodeValue"+i)) 
						{
							item = new TableItem_Search();
						} 
						else if (parser.getName().equals("uid")) 
						{
							eventType = parser.next();
							item.setTableId(parser.getText());
						}
						else if (parser.getName().equals("nickname")) 
						{
							eventType = parser.next();
							item.setTableNickname(parser.getText());
						} 
						else if (parser.getName().equals("birthyear")) 
						{
							eventType = parser.next();
							item.setTableBirthyear(parser.getText());
						} 
						else if (parser.getName().equals("pic")) 
						{
							eventType = parser.next();
							item.setTablePic(parser.getText());
						} 
						else if (parser.getName().equals("height"))
						{
							eventType = parser.next();
							item.setTableHeight(parser.getText());
						}
						else if (parser.getName().equals("education"))
						{
							eventType = parser.next();
							//if (parser.getText().equals("0")){item.setTableEducation("保密");}	    	
						    if(parser.getText().equals("3")){item.setTableEducation("高中及以下学历");}	    	
						    else if(parser.getText().equals("4")){item.setTableEducation("大专学历");}	    	
						    else if(parser.getText().equals("5")){item.setTableEducation("大学本科学历");}	    	
						    else if(parser.getText().equals("6")){item.setTableEducation("硕士学位");}	    	
						    else if(parser.getText().equals("7")){item.setTableEducation("博士学位");}
						    else
						    {
						    	item.setTableEducation("保密");
						    }
							//item.setTableEducation(parser.getText());
						}
						else if (parser.getName().equals("province"))
						{
							eventType = parser.next();
							//item.setTableProvince(parser.getText());
							     if(parser.getText().equals("10102000")){item.setTableProvince("北京");}	    	
						    else if(parser.getText().equals("10103000")){item.setTableProvince("上海");}	
						    else if(parser.getText().equals("10101002")){item.setTableProvince("广州");}
						    else if(parser.getText().equals("10101201")){item.setTableProvince("深圳");}
						    else if(parser.getText().equals("10101000")){item.setTableProvince("广东");}    	
						    else if(parser.getText().equals("10104000")){item.setTableProvince("天津");}	    	
						    else if(parser.getText().equals("10105000")){item.setTableProvince("重庆");}	    	
						    else if(parser.getText().equals("10106000")){item.setTableProvince("安徽");}	    	
						    else if(parser.getText().equals("10107000")){item.setTableProvince("福建");}	    	
						    else if(parser.getText().equals("10108000")){item.setTableProvince("甘肃");}	    	
						    else if(parser.getText().equals("10109000")){item.setTableProvince("广西");}	    	
						    else if(parser.getText().equals("10110000")){item.setTableProvince("贵州");}	    	
						    else if(parser.getText().equals("10111000")){item.setTableProvince("海南");}	    	
						    else if(parser.getText().equals("10112000")){item.setTableProvince("河北");}	    	
						    else if(parser.getText().equals("10113000")){item.setTableProvince("河南");}	    	
						    else if(parser.getText().equals("10114000")){item.setTableProvince("黑龙江");}	    	
						    else if(parser.getText().equals("10115000")){item.setTableProvince("湖北");}	    	
						    else if(parser.getText().equals("10116000")){item.setTableProvince("湖南");}	    	
						    else if(parser.getText().equals("10117000")){item.setTableProvince("吉林");}	    	
						    else if(parser.getText().equals("10118000")){item.setTableProvince("江苏");}	    	
						    else if(parser.getText().equals("10119000")){item.setTableProvince("江西");}	    	
						    else if(parser.getText().equals("10120000")){item.setTableProvince("辽宁");}	    	
						    else if(parser.getText().equals("10121000")){item.setTableProvince("内蒙古");}	    	
						    else if(parser.getText().equals("10122000")){item.setTableProvince("宁夏");}	    	
						    else if(parser.getText().equals("10123000")){item.setTableProvince("青海");}	    	
						    else if(parser.getText().equals("10124000")){item.setTableProvince("山东");}	    	
						    else if(parser.getText().equals("10125000")){item.setTableProvince("山西");}	    	
						    else if(parser.getText().equals("10126000")){item.setTableProvince("陕西");}	    	
						    else if(parser.getText().equals("10127000")){item.setTableProvince("四川");}	    	
						    else if(parser.getText().equals("10128000")){item.setTableProvince("西藏");}	    	
						    else if(parser.getText().equals("10129000")){item.setTableProvince("新疆");}	    	
						    else if(parser.getText().equals("10130000")){item.setTableProvince("云南");}	    	
						    else if(parser.getText().equals("10131000")){item.setTableProvince("浙江");}	    	
						    else if(parser.getText().equals("10132000")){item.setTableProvince("澳门");}	    	
						    else if(parser.getText().equals("10133000")){item.setTableProvince("香港");}	    	
						    else if(parser.getText().equals("10134000")){item.setTableProvince("台湾");}  	
						    else
						    {
						    	item.setTableProvince("保密");
						    }
						}
						else if (parser.getName().equals("city"))
						{
							eventType = parser.next();
							//item.setTableCity(parser.getText());
							//全部地市级的代码
						    //if(parser.getText().equals("不限")){item.setTableCity("0");}
						    //直辖市和特区
						    //else if(parser.getText().equals("北京")){item.setTableCity("0");}    	
						    //else if(parser.getText().equals("上海")){item.setTableCity("0");}	    	
						    //else if(parser.getText().equals("天津")){item.setTableCity("0");}	    	
						    //else if(parser.getText().equals("重庆")){item.setTableCity("0");}    	
						    //else if(parser.getText().equals("香港")){item.setTableCity("0");}	    	
						    //else if(parser.getText().equals("澳门")){item.setTableCity("0");}
						    //台湾	    	
						    if(parser.getText().equals("10134030")){item.setTableCity("台北");}	    		    	
						    else if(parser.getText().equals("10134031")){item.setTableCity("高雄");}	    	
						    else if(parser.getText().equals("10134041")){item.setTableCity("基隆");}	    	
						    else if(parser.getText().equals("10134042")){item.setTableCity("台中");}	    	
						    else if(parser.getText().equals("10134043")){item.setTableCity("台南");}	    	
						    else if(parser.getText().equals("10134033")){item.setTableCity("新竹");}	    	
						    else if(parser.getText().equals("10134044")){item.setTableCity("嘉义");}
						    //河北	
						    else if(parser.getText().equals("10112001")){item.setTableCity("石家庄");}	    	
						    else if(parser.getText().equals("10112006")){item.setTableCity("唐山");}	    	
						    else if(parser.getText().equals("10112008")){item.setTableCity("秦皇岛");}	    	
						    else if(parser.getText().equals("10112004")){item.setTableCity("邯郸");}	    	
						    else if(parser.getText().equals("10112003")){item.setTableCity("邢台");}	    	
						    else if(parser.getText().equals("10112010")){item.setTableCity("保定");}	    	
						    else if(parser.getText().equals("10112011")){item.setTableCity("张家口");}	    	
						    else if(parser.getText().equals("10112009")){item.setTableCity("承德");}	    	
						    else if(parser.getText().equals("10112005")){item.setTableCity("沧州");}	    	
						    else if(parser.getText().equals("10112007")){item.setTableCity("廊坊");}	    	
						    else if(parser.getText().equals("10112002")){item.setTableCity("衡水");}
						    //山西	    	
						    else if(parser.getText().equals("10125001")){item.setTableCity("太原");}	    	
						    else if(parser.getText().equals("10125005")){item.setTableCity("大同");}	    	
						    else if(parser.getText().equals("10125034")){item.setTableCity("朔州");}	    	
						    else if(parser.getText().equals("10125009")){item.setTableCity("阳泉");}	    	
						    else if(parser.getText().equals("10125010")){item.setTableCity("长治");}	    	
						    else if(parser.getText().equals("10125011")){item.setTableCity("晋城");}	    	
						    else if(parser.getText().equals("10125003")){item.setTableCity("忻州");}	    	
						    else if(parser.getText().equals("10125107")){item.setTableCity("晋中");}	    	
						    else if(parser.getText().equals("10125006")){item.setTableCity("临汾");}	    	
						    else if(parser.getText().equals("10125008")){item.setTableCity("运城");}	    	
						    else if(parser.getText().equals("10125108")){item.setTableCity("吕梁");}
						    //陕西
						    else if(parser.getText().equals("10126001")){item.setTableCity("西安");}	    	
						    else if(parser.getText().equals("10126010")){item.setTableCity("铜川");}	    	
						    else if(parser.getText().equals("10126006")){item.setTableCity("宝鸡");}	    	
						    else if(parser.getText().equals("10126011")){item.setTableCity("咸阳");}	    	
						    else if(parser.getText().equals("10126002")){item.setTableCity("渭南");}	    	
						    else if(parser.getText().equals("10126003")){item.setTableCity("延安");}	    	
						    else if(parser.getText().equals("10126008")){item.setTableCity("汉中");}	    	
						    else if(parser.getText().equals("10126005")){item.setTableCity("榆林");}	    	
						    else if(parser.getText().equals("10126007")){item.setTableCity("安康");}	    	
						    else if(parser.getText().equals("10126098")){item.setTableCity("商洛");}
						    //山东	
						    else if(parser.getText().equals("10124003")){item.setTableCity("济南");}	    	
						    else if(parser.getText().equals("10124001")){item.setTableCity("青岛");}	    	
						    else if(parser.getText().equals("10124004")){item.setTableCity("淄博");}	    	
						    else if(parser.getText().equals("10124014")){item.setTableCity("枣庄");}	    	
						    else if(parser.getText().equals("10124007")){item.setTableCity("东营");}	    	
						    else if(parser.getText().equals("10124009")){item.setTableCity("烟台");}	    	
						    else if(parser.getText().equals("10124008")){item.setTableCity("潍坊");}	    	
						    else if(parser.getText().equals("10124015")){item.setTableCity("济宁");}	    	
						    else if(parser.getText().equals("10124011")){item.setTableCity("泰安");}	    	
						    else if(parser.getText().equals("10124002")){item.setTableCity("威海");}	    	
						    else if(parser.getText().equals("10124016")){item.setTableCity("日照");}	    	
						    else if(parser.getText().equals("10124068")){item.setTableCity("莱芜");}	    	
						    else if(parser.getText().equals("10124013")){item.setTableCity("临沂");}	    	
						    else if(parser.getText().equals("10124006")){item.setTableCity("德州");}	    	
						    else if(parser.getText().equals("10124005")){item.setTableCity("聊城");}	    	
						    else if(parser.getText().equals("10124018")){item.setTableCity("滨州");}	    	
						    else if(parser.getText().equals("10124012")){item.setTableCity("菏泽");}
						    //河南	
						    else if(parser.getText().equals("10113001")){item.setTableCity("郑州");}	    	
						    else if(parser.getText().equals("10113013")){item.setTableCity("开封");}	    	
						    else if(parser.getText().equals("10113009")){item.setTableCity("洛阳");}	    	
						    else if(parser.getText().equals("10113010")){item.setTableCity("平顶山");}	    	
						    else if(parser.getText().equals("10113003")){item.setTableCity("安阳");}	    	
						    else if(parser.getText().equals("10113015")){item.setTableCity("鹤壁");}	    	
						    else if(parser.getText().equals("10113002")){item.setTableCity("新乡");}	    	
						    else if(parser.getText().equals("10113017")){item.setTableCity("焦作");}	    	
						    else if(parser.getText().equals("10113016")){item.setTableCity("濮阳");}	    	
						    else if(parser.getText().equals("10113004")){item.setTableCity("许昌");}	    	
						    else if(parser.getText().equals("10113006")){item.setTableCity("漯河");}	    	
						    else if(parser.getText().equals("10113011")){item.setTableCity("三门峡");}	    	
						    else if(parser.getText().equals("10113012")){item.setTableCity("南阳");}	    	
						    else if(parser.getText().equals("10113014")){item.setTableCity("商丘");}	    	
						    else if(parser.getText().equals("10113007")){item.setTableCity("信阳");}	    	
						    else if(parser.getText().equals("10113008")){item.setTableCity("周口");}	    	
						    else if(parser.getText().equals("10113005")){item.setTableCity("驻马店");}
						    //辽宁
						    else if(parser.getText().equals("10120001")){item.setTableCity("沈阳");}	    	
						    else if(parser.getText().equals("10120006")){item.setTableCity("大连");}	    	
						    else if(parser.getText().equals("10120004")){item.setTableCity("鞍山");}	    	
						    else if(parser.getText().equals("10120003")){item.setTableCity("抚顺");}	    	
						    else if(parser.getText().equals("10120007")){item.setTableCity("本溪");}	    	
						    else if(parser.getText().equals("10120008")){item.setTableCity("丹东");}	    	
						    else if(parser.getText().equals("10120009")){item.setTableCity("锦州");}	    	
						    else if(parser.getText().equals("10120005")){item.setTableCity("营口");}	    	
						    else if(parser.getText().equals("10120011")){item.setTableCity("阜新");}	    	
						    else if(parser.getText().equals("10120013")){item.setTableCity("辽阳");}	    	
						    else if(parser.getText().equals("10120012")){item.setTableCity("盘锦");}	    	
						    else if(parser.getText().equals("10120002")){item.setTableCity("铁岭");}	    	
						    else if(parser.getText().equals("10120010")){item.setTableCity("朝阳");}    	
						    else if(parser.getText().equals("10120014")){item.setTableCity("葫芦岛");}
						    //吉林
						    else if(parser.getText().equals("10117001")){item.setTableCity("长春");}	    	
						    else if(parser.getText().equals("10117002")){item.setTableCity("吉林");}	    	
						    else if(parser.getText().equals("10117006")){item.setTableCity("四平");}	    	
						    else if(parser.getText().equals("10117028")){item.setTableCity("辽源");}	    	
						    else if(parser.getText().equals("10117004")){item.setTableCity("通化");}	    	
						    else if(parser.getText().equals("10117048")){item.setTableCity("白山");}	    	
						    else if(parser.getText().equals("10117008")){item.setTableCity("松原");}	    	
						    else if(parser.getText().equals("10117007")){item.setTableCity("白城");}	    	
						    else if(parser.getText().equals("10117049")){item.setTableCity("延边");}
						    //黑龙江
						    else if(parser.getText().equals("10114001")){item.setTableCity("哈尔滨");}	    	
						    else if(parser.getText().equals("10114005")){item.setTableCity("齐齐哈尔");}	    	
						    else if(parser.getText().equals("10114020")){item.setTableCity("鹤岗");}	    	
						    else if(parser.getText().equals("10114024")){item.setTableCity("双鸭山");}	    	
						    else if(parser.getText().equals("10114009")){item.setTableCity("鸡西");}	    	
						    else if(parser.getText().equals("10114007")){item.setTableCity("大庆");}	    	
						    else if(parser.getText().equals("10114042")){item.setTableCity("伊春");}	    	
						    else if(parser.getText().equals("10114004")){item.setTableCity("牡丹江");}	    	
						    else if(parser.getText().equals("10114003")){item.setTableCity("佳木斯");}	    	
						    else if(parser.getText().equals("10114067")){item.setTableCity("七台河");}	    	
						    else if(parser.getText().equals("10114053")){item.setTableCity("黑河");}	    	
						    else if(parser.getText().equals("10114002")){item.setTableCity("绥化");}	    	
						    else if(parser.getText().equals("10114008")){item.setTableCity("大兴安岭");}
						    //江苏
						    else if(parser.getText().equals("10118001")){item.setTableCity("南京");}	    	
						    else if(parser.getText().equals("10118003")){item.setTableCity("无锡");}	    	
						    else if(parser.getText().equals("10118004")){item.setTableCity("徐州");}	    	
						    else if(parser.getText().equals("10118005")){item.setTableCity("常州");}	    	
						    else if(parser.getText().equals("10118002")){item.setTableCity("苏州");}
						    else if(parser.getText().equals("10118011")){item.setTableCity("南通");}	    	
						    else if(parser.getText().equals("10118007")){item.setTableCity("连云港");}	    	
						    else if(parser.getText().equals("10118016")){item.setTableCity("淮安");}	    	
						    else if(parser.getText().equals("10118009")){item.setTableCity("盐城");}	    	
						    else if(parser.getText().equals("10118010")){item.setTableCity("扬州");}	    	
						    else if(parser.getText().equals("10118006")){item.setTableCity("镇江");}	    	
						    else if(parser.getText().equals("10118051")){item.setTableCity("泰州");}	    	
						    else if(parser.getText().equals("10118063")){item.setTableCity("宿迁");}
						    //浙江
						    else if(parser.getText().equals("10131001")){item.setTableCity("杭州");}	    	
						    else if(parser.getText().equals("10131003")){item.setTableCity("宁波");}	    	
						    else if(parser.getText().equals("10131002")){item.setTableCity("温州");}	    	
						    else if(parser.getText().equals("10131006")){item.setTableCity("嘉兴");}	    	
						    else if(parser.getText().equals("10131005")){item.setTableCity("湖州");}	    	
						    else if(parser.getText().equals("10131004")){item.setTableCity("绍兴");}	    	
						    else if(parser.getText().equals("10131009")){item.setTableCity("金华");}	    	
						    else if(parser.getText().equals("10131011")){item.setTableCity("衢州");}	    	
						    else if(parser.getText().equals("10131015")){item.setTableCity("舟山");}	    	
						    else if(parser.getText().equals("10131012")){item.setTableCity("台州");}	    	
						    else if(parser.getText().equals("10131010")){item.setTableCity("丽水");}
						    //安徽	    	
						    else if(parser.getText().equals("10106001")){item.setTableCity("合肥");}	    	
						    else if(parser.getText().equals("10106009")){item.setTableCity("芜湖");}	    	
						    else if(parser.getText().equals("10106003")){item.setTableCity("蚌埠");}	    	
						    else if(parser.getText().equals("10106002")){item.setTableCity("淮南");}	    	
						    else if(parser.getText().equals("10106075")){item.setTableCity("马鞍山");}
						    else if(parser.getText().equals("10106042")){item.setTableCity("淮北");}	    	
						    else if(parser.getText().equals("10106013")){item.setTableCity("铜陵");}	    	
						    else if(parser.getText().equals("10106011")){item.setTableCity("安庆");}	    	
						    else if(parser.getText().equals("10106012")){item.setTableCity("黄山");}	    	
						    else if(parser.getText().equals("10106008")){item.setTableCity("滁州");}	    	
						    else if(parser.getText().equals("10106005")){item.setTableCity("阜阳");}	    	
						    else if(parser.getText().equals("10106004")){item.setTableCity("宿州");}	    	
						    else if(parser.getText().equals("10106006")){item.setTableCity("六安");}	    	
						    else if(parser.getText().equals("10106072")){item.setTableCity("亳州");}	    	
						    else if(parser.getText().equals("10106079")){item.setTableCity("池州");}	    	
						    else if(parser.getText().equals("10106080")){item.setTableCity("宣城");}
						    //江西
						    else if(parser.getText().equals("10119001")){item.setTableCity("南昌");}	    	
						    else if(parser.getText().equals("10119003")){item.setTableCity("景德镇");}	    	
						    else if(parser.getText().equals("10119007")){item.setTableCity("萍乡");}	    	
						    else if(parser.getText().equals("10119002")){item.setTableCity("九江");}	    	
						    else if(parser.getText().equals("10119040")){item.setTableCity("新余");}	    	
						    else if(parser.getText().equals("10119005")){item.setTableCity("鹰潭");}	    	
						    else if(parser.getText().equals("10119008")){item.setTableCity("赣州");}	    	
						    else if(parser.getText().equals("10119009")){item.setTableCity("吉安");}	    	
						    else if(parser.getText().equals("10119006")){item.setTableCity("宜春");}	    	
						    else if(parser.getText().equals("10119010")){item.setTableCity("抚州");}	    	
						    else if(parser.getText().equals("10119004")){item.setTableCity("上饶");}
						    //福建
						    else if(parser.getText().equals("10107001")){item.setTableCity("福州");}	    	
						    else if(parser.getText().equals("10107002")){item.setTableCity("厦门");}	    	
						    else if(parser.getText().equals("10107010")){item.setTableCity("莆田");}	    	
						    else if(parser.getText().equals("10107009")){item.setTableCity("三明");}	    	
						    else if(parser.getText().equals("10107003")){item.setTableCity("泉州");}	    	
						    else if(parser.getText().equals("10107007")){item.setTableCity("漳州");}	    	
						    else if(parser.getText().equals("10107004")){item.setTableCity("南平");}	    	
						    else if(parser.getText().equals("10107008")){item.setTableCity("龙岩");}	    	
						    else if(parser.getText().equals("10107013")){item.setTableCity("宁德");}
						    //湖北
						    else if(parser.getText().equals("10115001")){item.setTableCity("武汉");}	    	
						    else if(parser.getText().equals("10115002")){item.setTableCity("黄石");}	    	
						    else if(parser.getText().equals("10115007")){item.setTableCity("十堰");}	    	
						    else if(parser.getText().equals("10115010")){item.setTableCity("荆州");}	    	
						    else if(parser.getText().equals("10115008")){item.setTableCity("宜昌");}	    	
						    else if(parser.getText().equals("10115005")){item.setTableCity("襄樊");}	    	
						    else if(parser.getText().equals("10115004")){item.setTableCity("鄂州");}	    	
						    else if(parser.getText().equals("10115012")){item.setTableCity("荆门");}	    	
						    else if(parser.getText().equals("10115013")){item.setTableCity("孝感");}	    	
						    else if(parser.getText().equals("10115011")){item.setTableCity("黄冈");}	    	
						    else if(parser.getText().equals("10115006")){item.setTableCity("咸宁");}	    	
						    else if(parser.getText().equals("10115062")){item.setTableCity("随州");}	    	
						    else if(parser.getText().equals("10115009")){item.setTableCity("恩施");}
						    //湖南
						    else if(parser.getText().equals("10116001")){item.setTableCity("长沙");}	    	
						    else if(parser.getText().equals("10116002")){item.setTableCity("株洲");}	    	
						    else if(parser.getText().equals("10116014")){item.setTableCity("湘潭");}	    	
						    else if(parser.getText().equals("10116009")){item.setTableCity("衡阳");}	    	
						    else if(parser.getText().equals("10116010")){item.setTableCity("邵阳");}	    	
						    else if(parser.getText().equals("10116004")){item.setTableCity("岳阳");}	    	
						    else if(parser.getText().equals("10116005")){item.setTableCity("常德");}	    	
						    else if(parser.getText().equals("10116013")){item.setTableCity("张家界");}	    	
						    else if(parser.getText().equals("10116003")){item.setTableCity("益阳");}	    	
						    else if(parser.getText().equals("10116011")){item.setTableCity("郴州");}	    	
						    else if(parser.getText().equals("10116075")){item.setTableCity("永州");}	    	
						    else if(parser.getText().equals("10116008")){item.setTableCity("怀化");}	    	
						    else if(parser.getText().equals("10116007")){item.setTableCity("娄底");}	    	
						    else if(parser.getText().equals("10116097")){item.setTableCity("湘西");}
						    //四川
						    else if(parser.getText().equals("10127001")){item.setTableCity("成都");}	    	
						    else if(parser.getText().equals("10127014")){item.setTableCity("自贡");}	    	
						    else if(parser.getText().equals("10127017")){item.setTableCity("攀枝花");}	    	
						    else if(parser.getText().equals("10127016")){item.setTableCity("泸州");}	    	
						    else if(parser.getText().equals("10127018")){item.setTableCity("德阳");}	    	
						    else if(parser.getText().equals("10127005")){item.setTableCity("绵阳");}	    	
						    else if(parser.getText().equals("10127010")){item.setTableCity("广元");}	    	
						    else if(parser.getText().equals("10127117")){item.setTableCity("遂宁");}	    	
						    else if(parser.getText().equals("10127013")){item.setTableCity("内江");}	    	
						    else if(parser.getText().equals("10127002")){item.setTableCity("乐山");}	    	
						    else if(parser.getText().equals("10127011")){item.setTableCity("南充");}	    	
						    else if(parser.getText().equals("10127056")){item.setTableCity("眉山");}	    	
						    else if(parser.getText().equals("10127015")){item.setTableCity("宜宾");}	    	
						    else if(parser.getText().equals("10127070")){item.setTableCity("广安");}	    	
						    else if(parser.getText().equals("10127137")){item.setTableCity("达州");}	    	
						    else if(parser.getText().equals("10127008")){item.setTableCity("雅安");}	    	
						    else if(parser.getText().equals("10127125")){item.setTableCity("巴中");}	    	
						    else if(parser.getText().equals("10127021")){item.setTableCity("资阳");}	    	
						    else if(parser.getText().equals("10127007")){item.setTableCity("阿坝");}	    	
						    else if(parser.getText().equals("10127009")){item.setTableCity("甘孜");}	    	
						    else if(parser.getText().equals("10127003")){item.setTableCity("凉山");}
						    //贵州
						    else if(parser.getText().equals("10110001")){item.setTableCity("贵阳");}	    	
						    else if(parser.getText().equals("10110002")){item.setTableCity("六盘水");}	    	
						    else if(parser.getText().equals("10110007")){item.setTableCity("遵义");}	    	
						    else if(parser.getText().equals("10110006")){item.setTableCity("安顺");}	    	
						    else if(parser.getText().equals("10110081")){item.setTableCity("铜仁");}	    	
						    else if(parser.getText().equals("10110021")){item.setTableCity("毕节");}	    	
						    else if(parser.getText().equals("10110082")){item.setTableCity("黔西南");}	    	
						    else if(parser.getText().equals("10110083")){item.setTableCity("黔东南");}	    	
						    else if(parser.getText().equals("10110084")){item.setTableCity("黔南");}
						    //云南
						    else if(parser.getText().equals("10130001")){item.setTableCity("昆明");}	    	
						    else if(parser.getText().equals("10130002")){item.setTableCity("曲靖");}	    	
						    else if(parser.getText().equals("10130011")){item.setTableCity("玉溪");}	    	
						    else if(parser.getText().equals("10130010")){item.setTableCity("保山");}	    	
						    else if(parser.getText().equals("10130003")){item.setTableCity("昭通");}	    	
						    else if(parser.getText().equals("10130030")){item.setTableCity("丽江");}	    	
						    else if(parser.getText().equals("10130052")){item.setTableCity("普洱");}	    	
						    else if(parser.getText().equals("10130009")){item.setTableCity("临沧");}	    	
						    else if(parser.getText().equals("10130005")){item.setTableCity("文山");}	    	
						    else if(parser.getText().equals("10130098")){item.setTableCity("红河");}	    	
						    else if(parser.getText().equals("10130127")){item.setTableCity("西双版纳");}	    	
						    else if(parser.getText().equals("10130008")){item.setTableCity("楚雄");}	    	
						    else if(parser.getText().equals("10130007")){item.setTableCity("大理");}	    	
						    else if(parser.getText().equals("10130128")){item.setTableCity("德宏");}	    	
						    else if(parser.getText().equals("10130129")){item.setTableCity("怒江");}	    	
						    else if(parser.getText().equals("10130130")){item.setTableCity("迪庆");}
						    //广东
						    else if(parser.getText().equals("10101002")){item.setTableCity("广州");}	    	
						    else if(parser.getText().equals("10101201")){item.setTableCity("深圳");}	    	
						    else if(parser.getText().equals("10101005")){item.setTableCity("珠海");}	    	
						    else if(parser.getText().equals("10101013")){item.setTableCity("汕头");}	    	
						    else if(parser.getText().equals("10101015")){item.setTableCity("韶关");}	    	
						    else if(parser.getText().equals("10101003")){item.setTableCity("佛山");}	    	
						    else if(parser.getText().equals("10101016")){item.setTableCity("江门");}	    	
						    else if(parser.getText().equals("10101004")){item.setTableCity("湛江");}	    	
						    else if(parser.getText().equals("10101012")){item.setTableCity("茂名");}	    	
						    else if(parser.getText().equals("10101006")){item.setTableCity("肇庆");}	    	
						    else if(parser.getText().equals("10101008")){item.setTableCity("惠州");}	    	
						    else if(parser.getText().equals("10101014")){item.setTableCity("梅州");}	    	
						    else if(parser.getText().equals("10101028")){item.setTableCity("汕尾");}	    	
						    else if(parser.getText().equals("10101023")){item.setTableCity("河源");}	    	
						    else if(parser.getText().equals("10101022")){item.setTableCity("阳江");}	    	
						    else if(parser.getText().equals("10101018")){item.setTableCity("清远");}	    	
						    else if(parser.getText().equals("10101007")){item.setTableCity("东莞");}	    	
						    else if(parser.getText().equals("10101011")){item.setTableCity("中山");}	    	
						    else if(parser.getText().equals("10101020")){item.setTableCity("潮州");}	    	
						    else if(parser.getText().equals("10101026")){item.setTableCity("揭阳");}	    	
						    else if(parser.getText().equals("10101068")){item.setTableCity("云浮");}
						    //广西	    	
						    else if(parser.getText().equals("10109001")){item.setTableCity("南宁");}	    	
						    else if(parser.getText().equals("10109002")){item.setTableCity("柳州");}	    	
						    else if(parser.getText().equals("10109007")){item.setTableCity("桂林");}	    	
						    else if(parser.getText().equals("10109008")){item.setTableCity("梧州");}	    	
						    else if(parser.getText().equals("10109010")){item.setTableCity("北海");}	    	
						    else if(parser.getText().equals("10109006")){item.setTableCity("防城港");}	    	
						    else if(parser.getText().equals("10109003")){item.setTableCity("钦州");}	    	
						    else if(parser.getText().equals("10109014")){item.setTableCity("贵港");}	    	
						    else if(parser.getText().equals("10109005")){item.setTableCity("玉林");}	    	
						    else if(parser.getText().equals("10109004")){item.setTableCity("百色");}	    	
						    else if(parser.getText().equals("10109089")){item.setTableCity("贺州");}	    	
						    else if(parser.getText().equals("10109009")){item.setTableCity("河池");}	    	
						    else if(parser.getText().equals("10109018")){item.setTableCity("来宾");}	    	
						    else if(parser.getText().equals("10109083")){item.setTableCity("崇左");}
						    //海南
						    else if(parser.getText().equals("10111001")){item.setTableCity("海口");}	    	
						    else if(parser.getText().equals("10111002")){item.setTableCity("三亚");}	    	
						    //else if(parser.getText().equals("三沙")){item.setTableCity("0");}
						    //甘肃
						    else if(parser.getText().equals("10108001")){item.setTableCity("兰州");}    	
						    else if(parser.getText().equals("10108006")){item.setTableCity("金昌");}    	
						    else if(parser.getText().equals("10108078")){item.setTableCity("白银");}	    	
						    else if(parser.getText().equals("10108007")){item.setTableCity("天水");}	    	
						    else if(parser.getText().equals("10108023")){item.setTableCity("嘉峪关");}	    	
						    else if(parser.getText().equals("10108003")){item.setTableCity("武威");}	    	
						    else if(parser.getText().equals("10108002")){item.setTableCity("张掖");}	    	
						    else if(parser.getText().equals("10108009")){item.setTableCity("平凉");}	    	
						    else if(parser.getText().equals("10108004")){item.setTableCity("酒泉");}	    	
						    else if(parser.getText().equals("10108064")){item.setTableCity("庆阳");}	    	
						    else if(parser.getText().equals("10108008")){item.setTableCity("定西");}	    	
						    else if(parser.getText().equals("10108084")){item.setTableCity("陇南");}	    	
						    else if(parser.getText().equals("10108085")){item.setTableCity("临夏");}	    	
						    else if(parser.getText().equals("10108012")){item.setTableCity("甘南");}
						    //青海	
						    else if(parser.getText().equals("10123001")){item.setTableCity("西宁");}	    	
						    else if(parser.getText().equals("10123045")){item.setTableCity("海东");}	    	
						    else if(parser.getText().equals("10123046")){item.setTableCity("海北");}	    	
						    else if(parser.getText().equals("10123047")){item.setTableCity("黄南");}	    	
						    else if(parser.getText().equals("10123048")){item.setTableCity("海南");}	    	
						    else if(parser.getText().equals("10123002")){item.setTableCity("果洛");}	    	
						    else if(parser.getText().equals("10123003")){item.setTableCity("玉树");}	    	
						    else if(parser.getText().equals("10123005")){item.setTableCity("海西");}
						    //内蒙古
						    else if(parser.getText().equals("10121001")){item.setTableCity("呼和浩特");}	    	
						    else if(parser.getText().equals("10121003")){item.setTableCity("包头");}	    	
						    else if(parser.getText().equals("10121005")){item.setTableCity("乌海");}	    	
						    else if(parser.getText().equals("10121008")){item.setTableCity("赤峰");}	    	
						    else if(parser.getText().equals("10121011")){item.setTableCity("通辽");}	    	
						    else if(parser.getText().equals("10121092")){item.setTableCity("鄂尔多斯");}	    	
						    else if(parser.getText().equals("10121093")){item.setTableCity("呼伦贝尔");}	    	
						    else if(parser.getText().equals("10121094")){item.setTableCity("巴彦淖尔");}	    	
						    else if(parser.getText().equals("10121095")){item.setTableCity("乌兰察布");}	    	
						    else if(parser.getText().equals("10121089")){item.setTableCity("锡林郭勒");}	    	
						    else if(parser.getText().equals("10121091")){item.setTableCity("兴安");}	    	
						    else if(parser.getText().equals("10121090")){item.setTableCity("阿拉善");}
						    //宁夏	
						    else if(parser.getText().equals("10122001")){item.setTableCity("银川");}	    	
						    else if(parser.getText().equals("10122002")){item.setTableCity("石嘴山");}	    	
						    else if(parser.getText().equals("10122010")){item.setTableCity("吴忠");}	    	
						    else if(parser.getText().equals("10122003")){item.setTableCity("固原");}	    	
						    else if(parser.getText().equals("10122011")){item.setTableCity("中卫");}
						    //新疆
						    else if(parser.getText().equals("10129001")){item.setTableCity("乌鲁木齐");}	    	
						    else if(parser.getText().equals("10129004")){item.setTableCity("克拉玛依");}	    	
						    else if(parser.getText().equals("10129009")){item.setTableCity("吐鲁番");}	    	
						    else if(parser.getText().equals("10129008")){item.setTableCity("哈密");}	    	
						    else if(parser.getText().equals("10129012")){item.setTableCity("和田");}	    	
						    else if(parser.getText().equals("10129010")){item.setTableCity("阿克苏");}	    	
						    else if(parser.getText().equals("10129011")){item.setTableCity("喀什");}	    	
						    else if(parser.getText().equals("10129086")){item.setTableCity("塔城");}	    	
						    else if(parser.getText().equals("10129006")){item.setTableCity("阿勒泰");}	    	
						    else if(parser.getText().equals("10129088")){item.setTableCity("克孜勒苏");}	    	
						    else if(parser.getText().equals("10129007")){item.setTableCity("巴音郭楞");}	    	
						    else if(parser.getText().equals("10129017")){item.setTableCity("昌吉");}	    	
						    else if(parser.getText().equals("10129089")){item.setTableCity("博尔塔拉");}	    	
						    else if(parser.getText().equals("10129090")){item.setTableCity("伊犁");}
						    //西藏	
						    else if(parser.getText().equals("10128001")){item.setTableCity("拉萨");}	    	
						    else if(parser.getText().equals("10128002")){item.setTableCity("那曲");}	    	
						    else if(parser.getText().equals("10128003")){item.setTableCity("昌都");}	    	
						    else if(parser.getText().equals("10128004")){item.setTableCity("山南");}    	
						    else if(parser.getText().equals("10128005")){item.setTableCity("日喀则");}	    	
						    else if(parser.getText().equals("10128006")){item.setTableCity("阿里");}	    	
						    else if(parser.getText().equals("10128007")){item.setTableCity("林芝");}
						    
						    else if(parser.getText().equals("")){item.setTableCity(" ");}
						    else if(parser.getText().equals("0")){item.setTableCity(" ");}
						    else
						    {
						    	item.setTableCity("保密");
						    }
						}
						
						break;
					case XmlPullParser.END_TAG:
						if (parser.getName().equals("nodeValue"+i)) {
							items.add(item);
							item = null;
						}
						break;
					}//switch括号的另一边
				}//for括号的另一边
				
				eventType = parser.next();
			}//while括号的另一边
		} //try括号的另一边
		catch (Exception e) {}
		return items;
	}
}

