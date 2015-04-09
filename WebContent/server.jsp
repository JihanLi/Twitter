<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page language="java" 
import="src.SQLBase"
import="org.json.simple.JSONObject"
import="org.json.simple.JSONArray"
import="java.util.Vector"
import="java.io.*" 
import="java.util.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
	JSONObject json = new JSONObject();
	try
	{
		String topicString = request.getParameter("Topic");
		String[] items = { "Lattitude", "Longitude", "Display", "Topic", "Sentiment"};
		String table = "TwitterMap";
		
		int topic = Integer.parseInt(topicString);
		
		SQLBase.connectDB();
		List<List<String>> lists;
		
		if (topic != -1)
		{
			lists = SQLBase.selectItem(items, table, topic);
		}
		else
		{
			lists = SQLBase.selectItem(items, table);
		}
		//SQLBase.disconnectDB();
		
		JSONArray array = new JSONArray();
		for (List<String> list : lists)
		{
			JSONObject pos = new JSONObject();
			
			pos.put("lattitude", list.get(0));
			pos.put("longitude", list.get(1));
			pos.put("display", list.get(2));
			pos.put("topic", list.get(3));
			pos.put("sentiment", list.get(4));
			
			array.add(pos);
		}
		
		json.put("success", 1);
		json.put("data", array);
	}
	catch (Exception e)
	{
		json.put("success", 0);
		json.put("e", e.getMessage());
	}
	
	PrintWriter writer = response.getWriter();
	writer.println(json);
	writer.flush();
%>
