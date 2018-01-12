package com.example.administrator.activitycollector.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/1/6.
 */

public class JsonHistoryMessage {

    public String[] getJsonHistory(String jsonHistoryMessage){
        String[] History=new String[30];
        try {
            JSONArray jsonArray=new JSONArray(jsonHistoryMessage);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                History[i]=jsonObject.getString("history");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return History;
    }
}
