//package AddressBook;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

public class Resolution {
	
	public static JSONObject addressResolution(String txt,char flag)
	{
		JSONObject jsonObject=new JSONObject();
		
		//char flag=txt.charAt(0);//flag为难度级别
		
		int endIndex1=txt.lastIndexOf(",");//取出逗号的索引
		String name=txt.substring(0, endIndex1);//截取姓名
		//取手机号
		String pNumber=null;
		String REPLACE = "";
		String regexP = "0?(13|14|15|18|17)[0-9]{9}";
		Pattern r = Pattern.compile(regexP);
		Matcher m = r.matcher(txt);
		if (m.find()) 
	    { 
	        pNumber=m.group();
	    } 
		txt = m.replaceAll(REPLACE);
		
		/**
		 * txt1用来调api，txt用正则表达式
		 */
		//取走#号
		String txt1=txt;
		int jingIndex=txt1.lastIndexOf("#");
		if(jingIndex!=-1)
			txt1=txt1.substring(0, jingIndex)+txt1.substring(jingIndex+1);
		
		//取出纯地址信息
		int endIndex2=txt.lastIndexOf(".");
		txt=txt.substring(endIndex1+1, endIndex2);
		

		AddressUtils addressutils=new AddressUtils();
		AddressInfo addr=addressutils.addressInfo;//地址信息对象
		addr.setName(name);
		addr.setPhoneNumber(pNumber);
		addressutils.GetAddress(txt1);//参数中不能有“#”，要去掉，使用txt1
		
		//未处理具体地址中包含市县的情况
		String province =addr.getProvince();
		String county=addr.getCounty();
		String city=addr.getCity();
		int l=province.length()+county.length()+city.length();
		String txtCheck=txt.substring(0, l);
		int index=0;
		//地址中是否有省
		if(province.length()>0)
		{
			if(txtCheck.indexOf(province.substring(0, province.length()-1))!=-1)
			{
				index+=province.length()-1;
				if(txtCheck.indexOf(province)!=-1)
					index++;
			}
			else
			{
				addr.setProvince("");
			}
		}
		
		//地址中是否有市
		if(city.length()>0)
		{
			if(txtCheck.indexOf(city.substring(0, city.length()-1))!=-1)
			{
				index+=city.length()-1;
				if(txtCheck.indexOf(city)!=-1)
					index++;
			}
			else
			{
				addr.setCity("");
			}
		}

		//地址中是否有县
		if(county.length()>0)
		{
			if(txtCheck.indexOf(county.substring(0, county.length()-1))!=-1)
			{
				index+=county.length()-1;
				if(txtCheck.indexOf(county)!=-1)
					index++;
			}
			else
			{
				addr.setCounty("");
			}
		}

		txt=txt.substring(index);
		
//		int countyIndex=txt.indexOf(county);
//		if(countyIndex==-1||county==""||countyIndex==0)
//		{
//			int cityIndex=txt.indexOf(city);
//			if(cityIndex==-1)
//			{
//				city=city.substring(0, city.length()-1);
//				cityIndex=txt.indexOf(city);
//			}
//			txt=txt.substring(cityIndex+city.length());
//		}
//		else
//		{
//			txt=txt.substring(countyIndex+county.length());
//		}

		addressutils.addressResolution(txt, flag);
		
		//把地址信息存入json
		jsonObject.put("姓名", addr.getName());
		jsonObject.put("手机", addr.getPhoneNumber());
		JSONArray addressArray = new JSONArray();
		addressArray.put(addr.getProvince());
		addressArray.put(addr.getCity());
		addressArray.put(addr.getCounty());
		addressArray.put(addr.getTown());
		addressArray.put(addr.getVillage1());
		if(flag=='2')
		{
			addressArray.put(addr.getVillage2());
			addressArray.put(addr.getVillage3());
		}
		jsonObject.put("地址", addressArray);
		//System.out.println(jsonObject.toString());

		return jsonObject;
	}
	
}
