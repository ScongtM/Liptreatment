package com.example.administrator.activitycollector.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Scong on 2018/1/2 0002.
 */
//对服务器返回的json类型数据进行解码
public class JsonMessage {

    public String[][] JsonMessageData(String messageData){

        String [][]messageString=new String[100][2];
        try{
            JSONArray jsonArray=new JSONArray(messageData);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                messageString[i][0]=jsonObject.getString("title");
                messageString[i][1]=jsonObject.getString("content");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return messageString;
    }
}
