package com.example.administrator.activitycollector.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.activitycollector.JSON.JsonData;
import com.example.administrator.activitycollector.R;
import com.example.administrator.activitycollector.baiduApi.AuthService;
import com.example.administrator.activitycollector.baiduApi.Base64Util;
import com.example.administrator.activitycollector.baiduApi.FileUtil;
import com.example.administrator.activitycollector.baiduApi.HttpUtil;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.InterruptedByTimeoutException;

public class Main2Activity extends Activity implements View.OnClickListener {
    TextView mTextView;
    String key = "GDoKublppxGetzq0H3AIGDDO";//百度人脸检测api_key
    String secret = "nMXkXrfys7rTH7dSk2rYdAZeH4w0RlKo";//api_secret
    String url = "https://aip.baidubce.com/rest/2.0/face/v1/detect";
    StringBuffer sb = new StringBuffer();

    AuthService gettaken = new AuthService();

    private int b;
    private int g;
    private int r;
    private int a;
    private SharedPreferences pref;

    private SharedPreferences.Editor editor;
    private Button button1;

    String outputString="健康！";
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        pref = getSharedPreferences("lipdata.txt", MODE_PRIVATE);
        mTextView = (TextView) findViewById(R.id.text);
        initColor();
        button1 = (Button) findViewById(R.id.bt_out);
        button1.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.et_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_out:
                initOutRemork();
                break;
            default:
                break;
        }
    }

    private void initColor() {
        final Intent intent = getIntent();
        boolean isclick = intent.getBooleanExtra("isClick", true);
        //设置图片未改变时，不需要重新联网检测
        if (isclick == false) {
            String result = pref.getString("result", "");
            int rgbr = pref.getInt("r", 0);
            int rgbg = pref.getInt("g", 0);
            int rgbb = pref.getInt("b", 0);
            mTextView.setText(result);
            //r，g，b不能同时为0
            if (rgbr != 0 || rgbr != 0 || rgbg != 0) {
                mTextView.setBackgroundColor(Color.rgb(rgbr, rgbg, rgbb));
            }
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String imageUrl = intent.getStringExtra("path");
                        String faceToken = gettaken.getAuth(key, secret);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextView.setText(sb.toString());
                            }
                        });
                        byte[] imgData = FileUtil.readFileByBytes(imageUrl);
                        String imgStr = Base64Util.encode(imgData);
                        String imgParam = URLEncoder.encode(imgStr, "UTF-8");
                        String param = "max_face_num=" + 5 + "&face_fields=" + "age,beauty,expression,faceshape,gender,glasses,landmark,race,qualities" + "&image=" + imgParam;
                        String accessToken = faceToken;
                        String result = HttpUtil.post(url, accessToken, param);
                        JsonData jsonData = new JsonData();
                        int[] array = jsonData.JsonArrayData(result);
                        if (array != null) {
                            int x = array[0];
                            int y = array[1];
                            Bitmap bitmap = BitmapFactory.decodeFile(imageUrl);
                            int pixel = bitmap.getPixel(x, y + 5);
                            a = Color.alpha(pixel);
                            r = Color.red(pixel);
                            g = Color.blue(pixel);
                            b = Color.green(pixel);
                            sb.append("嘴唇颜色RGB值为：");
                            sb.append(r + " " + g + " " + b);
                            sb.append("分析结果：");
                            if (r == 255 && g >= 182 && g <= 192 && b >= 193 && b <= 203) {
                            } else {
                                outputString="不健康！";
                            }
                            sb.append(outputString);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    editor = pref.edit();
                                    editor.putString("result", sb.toString());
                                    editor.putInt("r", r);
                                    editor.putInt("g", g);
                                    editor.putInt("b", b);
                                    editor.commit();
                                    mTextView.setText(sb.toString());
                                    mTextView.setBackgroundColor(Color.rgb(r, g, b));
                                }
                            });
                        } else {
                            sb.append("检测失败！！！\n");
                            sb.append("提示：请选择合适的图片并保持网络畅通！");
                            outputString="检测失败！";
                            runOnUiThread(new Runnable() {


                                @Override
                                public void run() {
                                    mTextView.setText(sb.toString());
                                    editor = pref.edit();
                                    editor.putString("result", sb.toString());
                                    editor.commit();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void initOutRemork() {
        final String serverPath = "http://47.95.193.106:8080/ServletTest/InsertData";
        Intent intent=getIntent();
        final String username=intent.getStringExtra("username");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //使用GET方式请求服务器
                    final String remork=editText.getText().toString();
                    URL url = new URL(serverPath + "?username=" +username+"&remork=" +remork+"&"+"&outputString=" +outputString);
                    Log.d("1111111111111", "run: "+url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0");
                    final int responseCode = httpURLConnection.getResponseCode();
                    if (200 == responseCode) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        //重点，中文返回消息一定要设置编码格式
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                        final String responseMsg = bufferedReader.readLine();
                        //更新主线程
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (responseMsg.equals(false)){
                                    Toast.makeText(Main2Activity.this,"上传失败！请不要输入空数据！",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(Main2Activity.this,"上传成功！",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        bufferedReader.close();
                        httpURLConnection.disconnect();
                    } else {
                        //提示错误代码
                        Toast.makeText(Main2Activity.this, "responseCode = " + responseCode, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}













