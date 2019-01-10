package com.lek.sororas.Models;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;
import java.util.HashMap;

public class Evaluation {

    String nota;
    String comentario;
    String user;
    String date;


    public Evaluation(){

    }

    public Evaluation(HashMap<String,String> map){

        this.nota = map.get("nota");
        this.comentario = map.get("comentario");
        this.user = map.get("user");
        this.date = map.get("date");

    }

    public Evaluation(String nota, String comentario, String user, String date) {
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

