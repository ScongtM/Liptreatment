package com.example.administrator.activitycollector.activity;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

 import com.example.administrator.activitycollector.R;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView textView;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Button button_lip = (Button) findViewById(R.id.bt_lip);
        button_lip.setOnClickListener(this);

        Button button_message = (Button) findViewById(R.id.bt_message);
        button_message.setOnClickListener(this);

        Button button_his = (Button) findViewById(R.id.bt_history);
        button_his.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.tv_message);
        initMessage();

        Intent intent2 = getIntent();
        username = intent2.getStringExtra("username");
    }

    public void initMessage() {
        final String serverPath = "http://47.95.193.106:8080/ServletTest/MainMessage";
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
                                textView.setText(responseMsg);
                            }
                        });
                        bufferedReader.close();
                        httpURLConnection.disconnect();
                    } else {
                        //提示错误代码
                        Toast.makeText(MainActivity.this, "responseCode = " + responseCode, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_lip:
                Intent intent = new Intent(MainActivity.this, PicActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                break;
            case R.id.bt_message:
                Intent intent1 = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent1);
                break;
            case R.id.bt_history:
                Intent intent3=new Intent(MainActivity.this,HistoryMessageActivity.class);
                intent3.putExtra("username",username);
                startActivity(intent3);
            default:
                break;
        }
    }
}









