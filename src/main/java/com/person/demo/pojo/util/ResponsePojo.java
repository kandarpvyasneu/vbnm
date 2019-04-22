package com.person.demo.pojo.util;

public class ResponsePojo {

    private int status;
    private String body;

    public ResponsePojo(int status, String body){
        this.body = body;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
