package com.lek.sororas.Models;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;
import java.util.HashMap;

public class Evaluation {

    String nota;
    String comentario;
    DocumentReference user;
    Date date;


    public Evaluation(){

    }

    public Evaluation(String nota, String comentario, DocumentReference user, Date date) {
        this.nota = nota;
        this.comentario = comentario;
        this.user = user;
        this.date = date;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public DocumentReference getUser() {
        return user;
    }

    public void setUser(DocumentReference user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

