package com.lek.sororas.Models;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Contato {

    DocumentReference user;
    String last;
    Date data;

    public Contato() {

    }

    public Contato(DocumentReference user, String last, Date data) {
        this.user = user;
        this.last = last;
        this.data = data;
    }

    public DocumentReference getUser() {
        return user;
    }

    public void setUser(DocumentReference user) {
        this.user = user;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
