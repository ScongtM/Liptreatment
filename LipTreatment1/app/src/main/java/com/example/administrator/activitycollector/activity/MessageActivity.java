package com.example.administrator.activitycollector.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.activitycollector.JSON.JsonMessage;
import com.example.administrator.activitycollector.ListviewClass.Message;
import com.example.administrator.activitycollector.ListviewClass.MessageAdapter;
import com.example.administrator.activitycollector.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends Activity {

    private List<Message> messageList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message);
        initMessage();
    }

    public void initMessage() {
        final String serverPath = "http://47.95.193.106:8080/ServletTest/Message";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //使用GET方式请求服务器
                    URL url = new URL(serverPath);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0");
                    final int responseCode = httpURLConnection.getResponseCode();
                    if (200 == responseCode) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        //重点，中文返回消息一定要设置编码格式
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "GB2312"));
                        final String responseMsg = bufferedReader.readLine();
                        //更新主线程
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (responseMsg!=null){
                                    JsonMessage jsonMessage=new JsonMessage();
                                    String[][] messageString=jsonMessage.JsonMessageData(responseMsg);
                                    for (int i=0;i<messageString.length;i++){
                                        if (messageString[i][0]==null){
                                            break;
                                        }
                                        String title=messageString[i][0];
                                        String content=messageString[i][1];
                                        Message message=new Message(title,content);
                                        messageList.add(message);
                                    }
                                    //以下操作只能在本线程中执行，否则会无法显示数据
                                    MessageAdapter adapter=new MessageAdapter(MessageActivity.this,R.layout.message_item,messageList);
                                    ListView listView=(ListView) findViewById(R.id.list_view);
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                            Message message=messageList.get(position);
                                            String content=message.getContent();
                                            String title=message.getTitle();
                                            Intent intent=new Intent(MessageActivity.this,MessageShowActivity.class);
                                            intent.putExtra("title",title);
                                            intent.putExtra("content",content);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        });
                        bufferedReader.close();
                        httpURLConnection.disconnect();
                    } else {
                        //提示错误代码
                        Toast.makeText(MessageActivity.this,"responseCode = " + responseCode,Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
