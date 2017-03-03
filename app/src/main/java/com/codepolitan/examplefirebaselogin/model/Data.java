package com.codepolitan.examplefirebaselogin.model;

/**
 * Created by root on 03/03/17.
 */

public class Data {

    private String username;
    private String title;
    private String content;
    private String datetime;

    public Data() {
    }

    public Data(String username, String title, String content, String datetime) {
        this.username = username;
        this.title = title;
        this.content = content;
        this.datetime = datetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "content: " + content + ", datetime: " + datetime + ", title: " + title + ", username: " + username;
    }
}
