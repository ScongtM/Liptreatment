package com.example.administrator.activitycollector.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.activitycollector.JSON.JsonHistoryMessage;
import com.example.administrator.activitycollector.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HistoryMessageActivity extends Activity {

    String username;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_history_message);
        Intent intent=getIntent();
        username = intent.getStringExtra("username");
        getHistory(username);
        textView = (TextView) findViewById(R.id.tv_his);
    }

    public void getHistory(final String username){
        final String serverPath = "http://47.95.193.106:8080/ServletTest/History";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //使用GET方式请求服务器
                    URL url = new URL(serverPath+ "?username=" + username);
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
                                JsonHistoryMessage jsonHistoryMessage=new JsonHistoryMessage();
                                String[] history=jsonHistoryMessage.getJsonHistory(responseMsg);
                                String historyMessage="";
                                for (int i=0;i<history.length;i++){
                                    if (history[i]==null){
                                        break;
                                    }
                                    historyMessage+=history[i]+"\n";
                                }
                                textView.setText(historyMessage);
                            }
                        });
                        bufferedReader.close();
                        httpURLConnection.disconnect();
                    } else {
                        //提示错误代码
                        Toast.makeText(HistoryMessageActivity.this, "responseCode = " + responseCode, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
