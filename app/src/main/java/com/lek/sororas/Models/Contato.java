package com.lek.sororas.Models;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Contato {

    String user;
    String last;
    String dataLast;

    public Contato() {

    }

    public Contato(String user, String last, String data) {
        this.user = user;
        this.last = last;
        this.dataLast = data;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getData() {
        return dataLast;
    }

    public void setData(String dataLast) {
        this.dataLast = dataLast;
    }
}
