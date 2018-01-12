package com.example.administrator.activitycollector.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.activitycollector.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Scong on 2017/9/27 0027.
 *
 */

public class LoginActivity extends Activity {

    //SharedPreferences数据存储化方式
    private SharedPreferences.Editor editor;

    private EditText accountEdit;

    private EditText passwordEdit;

    private Button login;

    private static final int OK = 200;
    private CheckBox rememberPass;
    private SharedPreferences sp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //getWindow().setBackgroundDrawableResource(R.drawable.login);//第二种方式设置背景图片
        sp = getSharedPreferences("info1.txt", MODE_PRIVATE);
        accountEdit = (EditText) findViewById(R.id.user);
        passwordEdit = (EditText) findViewById(R.id.pass);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        login = (Button) findViewById(R.id.login);
        boolean isRemember = sp.getBoolean("remember_password", false);
        if (isRemember) {
            //将账号密码都设置到文本框中
            String account = sp.getString("account", "");
            String password = sp.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = accountEdit.getText().toString();
                final String password = passwordEdit.getText().toString();
                //服务器ip地址
                final String serverPath = "http://47.95.193.106:8080/ServletTest/login";
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this,"用户名或密码不能为空！",Toast.LENGTH_SHORT).show();
                } else {
                    editor = sp.edit();
                    if (rememberPass.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", username);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.commit();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //使用GET方式请求服务器
                                URL url = new URL(serverPath + "?username=" + username + "&password=" + password);
                                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                httpURLConnection.setRequestMethod("GET");
                                httpURLConnection.setConnectTimeout(5000);
                                httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0");
                                int responseCode = httpURLConnection.getResponseCode();
                                if (200 == responseCode) {
                                    InputStream inputStream = httpURLConnection.getInputStream();
                                    //设置读取数据编码格式
                                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                                    final String responseMsg = bufferedReader.readLine();
                                    //更新主线程
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (responseMsg.equals("true")){
                                                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
                                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                                intent.putExtra("username",username);
                                                startActivity(intent);
                                            }else {
                                                Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    bufferedReader.close();
                                    httpURLConnection.disconnect();
                                } else {
                                    //报告错误原因
                                    Toast.makeText(LoginActivity.this,"responseCode = " + responseCode,Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });

        TextView tv_register=(TextView) findViewById(R.id.register);
        tv_register.setClickable(true);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent2);
            }
        });
    }
}















