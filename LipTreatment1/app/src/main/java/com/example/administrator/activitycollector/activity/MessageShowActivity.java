package com.example.administrator.activitycollector.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.administrator.activitycollector.R;

public class MessageShowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message_show);
        Intent intent=getIntent();
        String content=intent.getStringExtra("content");
        String title=intent.getStringExtra("title");
        TextView textView=(TextView) findViewById(R.id.tv_content);
        TextView textView1=(TextView) findViewById(R.id.tv_title);
        textView1.setText(title);
        textView.setText(content);
    }
}
