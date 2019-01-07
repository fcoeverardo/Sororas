package com.lek.sororas.Models;

import java.util.HashMap;

public class Message {

    String text;
    String date;
    String senderId;
    String reciverId;

    public Message(String text, String date, String senderId, String reciverId) {
        this.text = text;
        this.date = date;
        this.senderId = senderId;
        this.reciverId = reciverId;
    }

    public Message(HashMap<String,String> message){
        this.text = message.get("text");
        this.date = message.get("date");
        this.senderId = message.get("senderId");
        this.reciverId = message.get("reciverId");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }
}
