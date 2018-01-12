package com.example.administrator.activitycollector.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

//对返回json数据进行解码
public class JsonData {

	public int[] JsonArrayData(String jsonData) {
		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			JSONArray jsonArray = jsonObject.getJSONArray("result");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				JSONArray jsonArray2 = jsonObject2.getJSONArray("landmark");
				JSONObject jsonObject3 = jsonArray2.getJSONObject(3);
				int x = jsonObject3.getInt("x");
				int y = jsonObject3.getInt("y");
				//System.out.println("x=" + x + "y=" + y);
				int[] array=new int[2];
				array[0]=x;
				array[1]=y;
				return array;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
