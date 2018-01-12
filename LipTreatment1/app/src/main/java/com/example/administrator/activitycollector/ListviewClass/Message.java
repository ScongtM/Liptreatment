package com.example.administrator.activitycollector.ListviewClass;

/**
 * Created by Scong on 2017/12/7.
 */

public class Message {

    private  String title;

    private String content;

    public Message(String title, String content) {
        this.title=title;
        this.content=content;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
